package com.mario.persistence.support;

import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import java.util.List;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;

/**
 * Description:利用 批量处理数据 Author: 陈二伟 Date:2018/11/18
 */
public abstract class DaoBatchUtils {

  /**
   * 批量插入
   *
   * @param entity     指定实体类型
   * @param entityList 数据集合
   * @param batchSize  每次处理多少数据
   * @return
   */
  public static <T> boolean insertBatch(Class<T> entity, List<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList)) {
      throw new IllegalArgumentException("Error: entityList must not be empty");
    }
    try (SqlSession batchSqlSession = sqlSessionBatch(entity)) {
      int size = entityList.size();
      String sqlStatement = sqlStatement(entity, SqlMethod.INSERT_ONE);
      for (int i = 0; i < size; i++) {
        batchSqlSession.insert(sqlStatement, entityList.get(i));
        if (i >= 1 && i % batchSize == 0) {
          batchSqlSession.flushStatements();
        }
      }
      batchSqlSession.flushStatements();
    } catch (Throwable e) {
      throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
    }
    return true;
  }

  /**
   * 通过whereEntity 批量修改
   *
   * @param entityList
   * @param wrapperList
   * @param batchSize
   * @return
   */
  public static <T> boolean updateBatch(Class<T> entity, List<T> entityList,
      List<Wrapper> wrapperList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList)) {
      throw new IllegalArgumentException("Error: entityList must not be empty");
    }
    if (CollectionUtils.isEmpty(wrapperList)) {
      throw new IllegalArgumentException("Error: wrapperList must not be empty");
    }
    if (entityList.size() != wrapperList.size()) {
      throw new IllegalArgumentException("Error: entityList 与 wrapperList size 必须相等");
    }
    try (SqlSession batchSqlSession = sqlSessionBatch(entity)) {
      int size = entityList.size();

      String sqlStatement = sqlStatement(entity, SqlMethod.UPDATE);
      for (int i = 0; i < size; i++) {
        MapperMethod.ParamMap param = new MapperMethod.ParamMap<>();
        param.put("et", entityList.get(i));
        param.put("ew", wrapperList.get(i));

        batchSqlSession.update(sqlStatement, param);
        if (i >= 1 && i % batchSize == 0) {
          batchSqlSession.flushStatements();
        }
      }
      batchSqlSession.flushStatements();
    } catch (Throwable e) {
      throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", e);
    }
    return true;
  }

  /**
   * 获取SqlStatement
   *
   * @param sqlMethod
   * @return
   */
  protected static String sqlStatement(Class entity, SqlMethod sqlMethod) {
    return SqlHelper.table(entity).getSqlStatement(sqlMethod.getMethod());
  }

  /**
   * <p>
   * 批量操作 SqlSession
   * </p>
   */
  protected static SqlSession sqlSessionBatch(Class entity) {
    return SqlHelper.sqlSessionBatch(entity);
  }
}
