package com.mall.discover.persistence.dao.impl;

import com.doubo.common.util.DateUtil;
import com.doubo.common.util.StringUtil;
import com.mongodb.client.result.UpdateResult;
import com.mall.discover.persistence.dao.MemberDiscoverDao;
import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;
import com.mall.discover.persistence.entity.ProductShareSummaryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Component
public class MemberDiscoverDaoImpl implements MemberDiscoverDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveMemberDiscover(MemberDiscoverEntity entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public List<MemberDiscoverEntity> pageDiscover(Map paramMap) {
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer pageNow  = (Integer) paramMap.get("pageNow");
        Query query = this.getApiCommonQuery(paramMap);
        query.skip((pageNow - 1) * pageSize);
        query.limit(pageSize);
        query.with(new Sort(Sort.Direction.DESC , "createTime"));
        return mongoTemplate.find(query, MemberDiscoverEntity.class);
    }

    @Override
    public List<MemberDiscoverEntity> getMemberDiscoverPage(Map paramMap) throws ParseException {
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer pageNow  = (Integer) paramMap.get("pageNow");
        Query query = this.getAdminCommonQuery(paramMap);
        query.skip((pageNow - 1) * pageSize);
        query.limit(pageSize);
        query.with(new Sort(Sort.Direction.DESC , "createTime"));
        return mongoTemplate.find(query, MemberDiscoverEntity.class);
    }

    @Override
    public long count(Map paramMap) {
        Query query = this.getApiCommonQuery(paramMap);
        return mongoTemplate.count(query, MemberDiscoverEntity.class);
    }

    @Override
    public long getMemberDiscoverPageCount(Map paramMap) throws ParseException {
        Query query = this.getAdminCommonQuery(paramMap);
        return mongoTemplate.count(query, MemberDiscoverEntity.class);
    }

    private Query getApiCommonQuery(Map paramMap){
        Integer memberId = paramMap.get("memberId") == null ? null : Integer.parseInt(paramMap.get("memberId").toString());
        String cateId = paramMap.get("cateId") == null ? null : paramMap.get("cateId").toString();
        String productId = paramMap.get("productId") == null ? null : paramMap.get("productId").toString();
        Query query = new Query();
        query.addCriteria(Criteria.where("display").is(true));
        if(!StringUtil.isNull(memberId)){
            query.addCriteria(Criteria.where("memberId").is(memberId));
        }

        if(!StringUtil.isNull(cateId)){
            query.addCriteria(Criteria.where("cateId").is(cateId));
        }

        if(!StringUtil.isNull(productId)){
            query.addCriteria(Criteria.where("sameProductFlag").is(true).andOperator(Criteria.where("sameProductId").is(Integer.parseInt(productId))));
        }

        return query;
    }

    private Query getAdminCommonQuery(Map paramMap) throws ParseException {
        String name = paramMap.get("q_name") == null ? null : paramMap.get("q_name").toString();
        String phone = paramMap.get("q_phone") == null ? "" : paramMap.get("q_phone").toString();
        String status = paramMap.get("q_status") == null ? "" : paramMap.get("q_status").toString();
        String recomend = paramMap.get("q_recomend") == null ? "" : paramMap.get("q_recomend").toString();
        String startTime = paramMap.get("q_startTime") == null ? "" : paramMap.get("q_startTime").toString();
        String endTime = paramMap.get("q_endTime") == null ? "" : paramMap.get("q_endTime").toString();
        Query query = new Query();
        if(!StringUtil.isNull(name)){
            query.addCriteria(Criteria.where("memberName").is(name));
        }

        if(!StringUtil.isNull(phone)){
            query.addCriteria(Criteria.where("memberPhone").is(phone));
        }

        if(!StringUtil.isNull(status)){
            query.addCriteria(Criteria.where("display").is("1".equals(status)));
        }

        if(!StringUtil.isNull(recomend)){
            query.addCriteria(Criteria.where("recomend").is("1".equals(recomend)));
        }

        Criteria createTime = Criteria.where("createTime");
        if(!StringUtil.isNull(startTime)){
            createTime.gte(new SimpleDateFormat(DateUtil.Y_M_D_HMS).parse(startTime));
        }

        if(!StringUtil.isNull(endTime)){
            createTime.lte(new SimpleDateFormat(DateUtil.Y_M_D_HMS).parse(endTime));
        }

        if(!StringUtil.isNull(startTime) || !StringUtil.isNull(endTime)){
            query.addCriteria(createTime);
        }
        return query;
    }

    @Override
    public MemberDiscoverEntity findById(String id) {
        MemberDiscoverEntity entity = mongoTemplate.findById(id, MemberDiscoverEntity.class);
        return entity;
    }

    @Override
    public void saveMemberDiscoverCate(MemberDiscoverCateEntity entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public List<MemberDiscoverCateEntity> listCate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("display").is(1));
        query.with(new Sort(Sort.Direction.ASC , "order"));

        return mongoTemplate.find(query, MemberDiscoverCateEntity.class);
    }

    @Override
    public long updateViews(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("views", 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberDiscoverEntity.class);
       return updateResult.getModifiedCount();
    }

    @Override
    public long incProductShare(Integer productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        Update update = new Update();
        update.inc("shareCount", 1);
        UpdateResult upsert = mongoTemplate.upsert(query, update, ProductShareSummaryEntity.class);
        return upsert.getModifiedCount();
    }

    @Override
    public int getProductShareCount(Integer productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        ProductShareSummaryEntity one = mongoTemplate.findOne(query, ProductShareSummaryEntity.class);
        return one != null ? one.getShareCount() : 0;
    }

    @Override
    public long updateForward(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("forwards", 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberDiscoverEntity.class);
        return updateResult.getModifiedCount();
    }

    @Override
    public long updateByPrimaryKeySelective(Map paramMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(paramMap.get("id")));
        Update update = new Update();
        if(!StringUtil.isNull(paramMap.get("recomend"))){
            update.set("recomend",Boolean.parseBoolean(paramMap.get("recomend").toString()));
        }
        if(!StringUtil.isNull(paramMap.get("display"))){
            update.set("display",Boolean.parseBoolean(paramMap.get("display").toString()));
        }
        update.set("updateTime", DateUtil.getNowDate());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberDiscoverEntity.class);
        return updateResult.getModifiedCount();
    }
}
