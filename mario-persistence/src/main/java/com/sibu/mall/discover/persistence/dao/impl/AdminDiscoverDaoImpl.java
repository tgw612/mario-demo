package com.mall.discover.persistence.dao.impl;

import com.doubo.common.util.DateUtil;
import com.doubo.common.util.StringUtil;
import com.mongodb.client.result.UpdateResult;
import com.mall.discover.persistence.dao.AdminDiscoverDao;
import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;
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
import java.util.regex.Pattern;

@Component
public class AdminDiscoverDaoImpl implements AdminDiscoverDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Long count(Map paramMap) {
        Query query = null;
        try {
            query = this.getAdminCommonQuery(paramMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mongoTemplate.count(query, MemberDiscoverEntity.class);
    }

    @Override
    public List<MemberDiscoverEntity> selectList(Map paramMap) {
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer pageNow = (Integer) paramMap.get("pageNow");
        Query query = null;
        try {
            query = this.getAdminCommonQuery(paramMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.skip((pageNow - 1) * pageSize);
        query.limit(pageSize);
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        return mongoTemplate.find(query, MemberDiscoverEntity.class);
    }

    @Override
    public Long selectListCateCount(Map paramMap) {
        Query query = null;
        try {
            query = this.getAdminCommonQuery(paramMap);
        } catch (ParseException e) {
        }
        return mongoTemplate.count(query, MemberDiscoverCateEntity.class);
    }

    @Override
    public List<MemberDiscoverCateEntity> selectCateListPage(Map paramMap) {
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer pageNow = (Integer) paramMap.get("pageNow");
        Query query = null;
        try {
            query = this.getAdminCommonQuery(paramMap);
        } catch (ParseException e) {
        }
        query.skip((pageNow - 1) * pageSize);
        query.limit(pageSize);
        query.with(new Sort(Sort.Direction.ASC, "order"));
        return mongoTemplate.find(query, MemberDiscoverCateEntity.class);
    }

    @Override
    public void add(MemberDiscoverEntity entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public long update(MemberDiscoverEntity entity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(entity.getId()));
        Update update = new Update();

        update.set("memberId", entity.getMemberId());
        update.set("memberLogo", entity.getMemberLogo());
        update.set("memberName", entity.getMemberName());
        update.set("memberPhone", entity.getMemberPhone());
        update.set("cateId", entity.getCateId());
        update.set("cateName", entity.getCateName());
        update.set("title", entity.getTitle());
        update.set("content", entity.getContent());
        if (entity.getPictures() != null)
            update.set("pictures", entity.getPictures());
        if (entity.getThumbnailSmall() != null) {
            update.set("thumbnailSmall", entity.getThumbnailSmall());
        }
        update.set("video", entity.getVideo());
        if (entity.getVideoHeight() != null) {
            update.set("videoHeight", entity.getVideoHeight());
        }
        if (entity.getVideoWidth() != null) {
            update.set("videoWidth", entity.getVideoWidth());
        }
        update.set("productId", entity.getProductId());
        if (entity.getRecomend() != null)
            update.set("recomend", entity.getRecomend());
        if (entity.getDisplay() != null)
            update.set("display", entity.getDisplay());
        if (entity.getViews() != null)
            update.set("views", entity.getViews());
        update.set("sameProductId", entity.getSameProductId());
        update.set("sameProductFlag", entity.getSameProductFlag());
        if (entity.getOrder() != null)
            update.set("order", entity.getOrder());
        update.set("updateTime", DateUtil.getNowDate());

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberDiscoverEntity.class);
        return updateResult.getModifiedCount();
    }

    @Override
    public void delete(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, MemberDiscoverEntity.class);
    }

    @Override
    public MemberDiscoverEntity findById(String id) {
        MemberDiscoverEntity entity = mongoTemplate.findById(id, MemberDiscoverEntity.class);
        return entity;
    }

    @Override
    public void addCate(MemberDiscoverCateEntity entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public void deleteCate(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, MemberDiscoverCateEntity.class);
    }

    @Override
    public MemberDiscoverCateEntity findCateById(String id) {
        MemberDiscoverCateEntity entity = mongoTemplate.findById(id, MemberDiscoverCateEntity.class);
        return entity;
    }

    @Override
    public long updateCateById(MemberDiscoverCateEntity entity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(entity.getId()));
        Update update = new Update();
        update.set("cateName", entity.getCateName());
        update.set("display", entity.getDisplay());
        update.set("order", entity.getOrder());
        update.set("updateTime", DateUtil.getNowDate());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberDiscoverCateEntity.class);
        return updateResult.getModifiedCount();
    }

    private Query getAdminCommonQuery(Map paramMap) throws ParseException {
        String name = paramMap.get("q_name") == null ? null : paramMap.get("q_name").toString();
        String phone = paramMap.get("q_phone") == null ? "" : paramMap.get("q_phone").toString();
        String status = paramMap.get("q_status") == null ? "" : paramMap.get("q_status").toString();
        String recomend = paramMap.get("q_recomend") == null ? "" : paramMap.get("q_recomend").toString();
        String startTime = paramMap.get("q_startTime") == null ? "" : paramMap.get("q_startTime").toString();
        String endTime = paramMap.get("q_endTime") == null ? "" : paramMap.get("q_endTime").toString();
        String cateName = paramMap.get("q_cateName") == null ? "" : paramMap.get("q_cateName").toString();
        String cateId = paramMap.get("q_cateId") == null ? "" : paramMap.get("q_cateId").toString();
        String content = paramMap.get("q_content") == null ? "" : paramMap.get("q_content").toString();
        String title = paramMap.get("q_title") == null ? "" : paramMap.get("q_title").toString();
        Query query = new Query();

        if (!StringUtil.isNull(title)) {
            Pattern pattern = Pattern.compile(title,Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("title").regex(pattern));
        }
        if (!StringUtil.isNull(content)) {
            Pattern pattern = Pattern.compile(content,Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("content").regex(pattern));
        }

        if (!StringUtil.isNull(name)) {
            query.addCriteria(Criteria.where("memberName").is(name));
        }

        if (!StringUtil.isNull(phone)) {
            query.addCriteria(Criteria.where("memberPhone").is(phone));
        }

        if (!StringUtil.isNull(status)) {
            query.addCriteria(Criteria.where("display").is("1".equals(status)));
        }

        if (!StringUtil.isNull(recomend)) {
            query.addCriteria(Criteria.where("recomend").is("1".equals(recomend)));
        }

        Criteria createTime = Criteria.where("createTime");
        if (!StringUtil.isNull(startTime)) {
            createTime.gte(new SimpleDateFormat(DateUtil.Y_M_D_HMS).parse(startTime));
        }

        if (!StringUtil.isNull(endTime)) {
            createTime.lte(new SimpleDateFormat(DateUtil.Y_M_D_HMS).parse(endTime));
        }

        if (!StringUtil.isNull(startTime) || !StringUtil.isNull(endTime)) {
            query.addCriteria(createTime);
        }

        if (!StringUtil.isNull(cateName)) {
            Pattern pattern = Pattern.compile(cateName, Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("cateName").regex(pattern));

        }

        if (!StringUtil.isNull(cateId)) {
            query.addCriteria(Criteria.where("cateId").is(cateId));
        }

        return query;
    }

}
