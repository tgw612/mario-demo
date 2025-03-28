package com.mario.persistence.support.interceptor;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.doubo.common.model.PageParameter;
import com.doubo.common.threadlocal.SerialNo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;

//import com.alibaba.druid.sql.PagerUtils;
//import com.alibaba.druid.util.JdbcConstants;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2016/3/7 Description:myBatis分页分表插件
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,
    Integer.class})})
@Slf4j
public class MyBatisInterceptor implements Interceptor {

  private final static String DEFAULT_PAGE_SQL_ID = ".*Page$"; // 需要拦截的ID(正则匹配)
  private final static String DEFAULT_DIALECT = "mysql"; //默认为 mysql
  private final static String ORACLE_REGEX = "^(select)(.*?)(from)";
  private final static Pattern ORACLE_PATTERN = Pattern
      .compile(ORACLE_REGEX, Pattern.CASE_INSENSITIVE);
  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
  private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
  private String dialect = DEFAULT_DIALECT; //支持的数据库类型 oracle/mysql
  private String pageSqlId = DEFAULT_PAGE_SQL_ID; // 需要拦截的ID(正则匹配)

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
//        long startTime = System.currentTimeMillis();
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
        DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

//        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);
    // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
    while (metaStatementHandler.hasGetter("h")) {
      metaStatementHandler.getValue("h");
      metaStatementHandler = MetaObject.forObject(statementHandler,
          DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    }
    // 分离最后一个代理对象的目标类
    while (metaStatementHandler.hasGetter("target")) {
      metaStatementHandler.getValue("target");
      metaStatementHandler = MetaObject.forObject(statementHandler,
          DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    }

    MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
        .getValue("delegate.mappedStatement");
    BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");



        /*Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        dialect = configuration.getVariables().getProperty("dialect");
        if (null == dialect || "".equals(dialect)) {
            dialect = DEFAULT_DIALECT;
        }
        pageSqlId = configuration.getVariables().getProperty("pageSqlId");
        if (null == pageSqlId || "".equals(pageSqlId)) {
            pageSqlId = DEFAULT_PAGE_SQL_ID;
        }*/
    String sql = boundSql.getSql();

    // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
    if (mappedStatement.getId().matches(pageSqlId)) {
      Object parameterObject = boundSql.getParameterObject();
      if (parameterObject == null) {
        log.error("[{}] parameterObject is null!", SerialNo.getSerialNo());
        throw new NullPointerException("parameterObject is null!");
      } else {
        PageParameter page = (PageParameter) metaStatementHandler
            .getValue("delegate.boundSql.parameterObject.page");
        // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        Connection connection = (Connection) invocation.getArgs()[0];
        // 重设分页参数里的总页数等
        setPageParameter(sql, connection, mappedStatement, boundSql, page);
        // 重写sql
        sql = buildPageSql(sql, page);
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("[{}] 执行SQL =====>[{}]", SerialNo.getSerialNo(), sql);
    }
    metaStatementHandler.setValue("delegate.boundSql.sql", sql);
    // 将执行权交给下一个拦截器
    return invocation.proceed();
  }

  /**
   * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
   * <code>PageParameter</code>获得相关信息。
   *
   * @param sql
   * @param connection
   * @param mappedStatement
   * @param boundSql
   * @param page
   */
  private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
      BoundSql boundSql, PageParameter page) {
//        String reg = "(?i)order\\s*(?i)BY\\s*.*";
//        sql = sql.replaceAll(RegPattern.SQL_COUNT_REPLACE, "");
    // 记录总记录数
//        String countSql = "select count(0)  as total from (" + sql + ") T";

    String countSql = PagerUtils.count(sql, JdbcConstants.MYSQL);

    PreparedStatement countStmt = null;
    ResultSet rs = null;
    try {
      countStmt = connection.prepareStatement(countSql);
      BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
          boundSql.getParameterMappings(), boundSql.getParameterObject());
      setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
      rs = countStmt.executeQuery();
      int totalCount = 0;
      if (rs.next()) {
        totalCount = rs.getInt(1);
      }
      page.setTotalCount(totalCount);
      int totalPage =
          totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
      page.setTotalPage(totalPage);

    } catch (SQLException e) {
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } catch (SQLException e) {
      }
      try {
        if (countStmt != null) {
          countStmt.close();
        }
      } catch (SQLException e) {
      }
    }

  }

  /**
   * 对SQL参数(?)设值
   *
   * @param ps
   * @param mappedStatement
   * @param boundSql
   * @param parameterObject
   * @throws SQLException
   */
  private void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
      BoundSql boundSql,
      Object parameterObject) throws SQLException {
    ParameterHandler parameterHandler = new CustomDefaultParameterHandler(mappedStatement,
        parameterObject, boundSql);
    parameterHandler.setParameters(ps);
  }

  /**
   * 根据数据库类型，生成特定的分页sql
   *
   * @param sql
   * @param page
   * @return
   */
  private String buildPageSql(String sql, PageParameter page) {
    if (page != null) {
      //直接写死---调用mysql分页
      StringBuilder pageSql = buildPageSqlForMysql(sql, page);
            /*if ("mysql".equals(dialect)) {
                pageSql = buildPageSqlForMysql(sql, page);
            } else if ("oracle".equals(dialect)) {
                pageSql = buildPageSqlForOracle(sql, page);
            } else {
                return sql;
            }*/
      return pageSql.toString();
    } else {
      return sql;
    }
  }

  /**
   * 参考hibernate的实现完成oracle的分页
   *
   * @param sql
   * @param page
   * @return String
   */
  public StringBuilder buildPageSqlForOracle(String sql, PageParameter page) {
    StringBuilder pageSql = new StringBuilder(100);
    String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
    String endrow = String.valueOf(page.getCurrentPage() * page.getPageSize());

    String cols = getMatchedString(sql.replace("\n", ""), 2);

    String replaceRegex = "\\w+\\.";
    String targetCols = replaceMatchedString(replaceRegex, cols, "temp.");

    replaceRegex = "(\\w+\\.)?\\w+\\s+as\\s+|(\\w+\\.)?\\w+\\s+AS\\s+|(\\w+\\.)?\\w+\\s+As\\s+|(\\w+\\.)?\\w+\\s+aS\\s+";
    targetCols = replaceMatchedString(replaceRegex, targetCols, "");

    pageSql.append("SELECT ").append(targetCols).append(" FROM (select ").append(targetCols)
        .append(",  ROWNUM ROWNUM_ from ( ");
    pageSql.append(sql);
    pageSql.append(" ) temp ");
    pageSql.append(" where ROWNUM <= ").append(endrow).append(") temp");
    pageSql.append("   WHERE ROWNUM_ > ").append(beginrow);
    return pageSql;
  }

  /**
   * mysql 的分页实现
   *
   * @param sql  查询sql
   * @param page 分页参数
   * @return 分页实现后的sql
   */
  public StringBuilder buildPageSqlForMysql(String sql, PageParameter page) {
    StringBuilder pageSql = new StringBuilder(100);
    String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
    pageSql.append(sql);
    pageSql.append(" limit " + beginrow + "," + page.getPageSize());
    return pageSql;
  }

  @Override
  public Object plugin(Object target) {
    // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
    if (target instanceof StatementHandler) {
      return Plugin.wrap(target, this);
    } else {
      return target;
    }
  }

  @Override
  public void setProperties(Properties properties) {
  }


  /**
   * 从文本text中找到regex首次匹配的字符串，不区分大小写
   *
   * @param text：欲查找的字符串
   * @return regex首次匹配的字符串，如未匹配返回空
   */
  private String getMatchedString(String text, int groupIndex) {
    Matcher matcher = ORACLE_PATTERN.matcher(text);

    while (matcher.find()) {
      return matcher.group(groupIndex);
    }

    return text;
  }

  /**
   * 从文本text中找到regex首次匹配的字符串，并替换为目标字符
   *
   * @param regex：       正则表达式
   * @param text：欲查找的字符串
   * @return 替换后的sql
   */
  private String replaceMatchedString(String regex, String text, String targetStr) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(sb, targetStr);
    }
    matcher.appendTail(sb);
    return sb.toString();
  }
}
