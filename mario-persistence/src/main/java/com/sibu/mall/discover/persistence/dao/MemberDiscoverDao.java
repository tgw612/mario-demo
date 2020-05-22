package com.mall.discover.persistence.dao;


import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface MemberDiscoverDao{

     void saveMemberDiscover(MemberDiscoverEntity entity);

     List<MemberDiscoverEntity>  pageDiscover(Map paramMap);

     List<MemberDiscoverEntity>  getMemberDiscoverPage(Map paramMap) throws ParseException;

     long count(Map paramMap);

     long getMemberDiscoverPageCount(Map paramMap) throws ParseException;

     MemberDiscoverEntity findById(String id);

     void saveMemberDiscoverCate(MemberDiscoverCateEntity entity);

     List<MemberDiscoverCateEntity> listCate();

     long updateViews(String id);

     long incProductShare(Integer productId);

     int getProductShareCount(Integer productId);

     long updateForward(String id);

     long updateByPrimaryKeySelective(Map paramMap);

}