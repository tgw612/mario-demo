package com.mall.discover.persistence.dao;

import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;

import java.util.List;
import java.util.Map;

public interface AdminDiscoverDao {

    Long count(Map paramMap);

    List<MemberDiscoverEntity> selectList(Map paramMap);

    Long selectListCateCount(Map paramMap);

    List<MemberDiscoverCateEntity> selectCateListPage(Map paramMap);

    void add(MemberDiscoverEntity entity);

    long update(MemberDiscoverEntity entity);

    void delete(String id);

    MemberDiscoverEntity findById(String id);

    void addCate(MemberDiscoverCateEntity entity);
    void deleteCate(String id);
    MemberDiscoverCateEntity findCateById(String id);
    long updateCateById(MemberDiscoverCateEntity entity);
}
