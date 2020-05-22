package com.mall.discover.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.persistence.dao.mysql.ClientArticleMapper;
import com.mall.discover.persistence.dao.mysql.ClientRelationMapper;
import com.mall.discover.persistence.entity.mysql.RelationsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service
public class ClientLikeBiz {

    @Autowired
    private ClientArticleMapper clientArticleMapper;

    @Autowired
    private ClientRelationMapper clientRelationMapper;

    /**
     *  文章点赞用户缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;


    /**
     * 更新文章点赞
     * @return
     */
    public Boolean updateUserLikeArticle() {
        //获取所有文章id
        List<Long> articleIds = clientArticleMapper.queryAllArticleId();
        for (Long articleId : articleIds) {
            //获取点赞数量
            Long likeCount = RedisTemplateUtil.getLong(DiscoverCountEnum.ARTICLE_LIKE_COUNT.getCode() + articleId);
            //获取点赞用户
            LinkedHashSet<Integer> cacheUserIds = articleLikeUserListCache.get(articleId);
            //获取DB中点赞用户
            LinkedHashSet<Integer> dbUserIds = getDbAllFromId(articleId);

            //如果size不相等，更新缓存;如果size相等，更新DB
//            if (ObjectUtils.isEmpty(likeCount) || ObjectUtils.isEmpty(cacheUserIds) || likeCount != cacheUserIds.size()) {
            boolean flag = likeCount == null || cacheUserIds == null || likeCount != cacheUserIds.size();
            if (flag) {
                articleLikeUserListCache.put(articleId, dbUserIds);
                RedisTemplateUtil.setLong(DiscoverCountEnum.ARTICLE_LIKE_COUNT.getCode() + articleId, dbUserIds.size());
            }else {
                //需删除集合
                List<Integer> removeUserIds = new ArrayList<>();
                dbUserIds.forEach(userId -> {
                    if (!cacheUserIds.contains(userId)) {
                        removeUserIds.add(userId);
                    }
                });
                //需新增集合
                List<Integer> addUserIds = new ArrayList<>();
                cacheUserIds.forEach(userId -> {
                    if (!dbUserIds.contains(userId)) {
                        addUserIds.add(userId);
                    }
                });
                //逻辑删除
                if (removeUserIds.size() > 0) {
                    clientRelationMapper.batchRemoveByToId(articleId, removeUserIds, RelationTypeEnum.USER_LIKE_ARTICLE.getCode());
                }
                //批量新增
                batchAddRelation(articleId, RelationTypeEnum.USER_LIKE_ARTICLE.getCode(), addUserIds);
            }
        }
        return true;
    }

    /**
     * 批量新增
     * @param relationType
     * @param addUserIds
     */
    private void batchAddRelation(Long toId, Integer relationType, List<Integer> addUserIds) {
        if (ObjectUtils.isEmpty(addUserIds)) {
            return;
        }
        int page = 0;
        int pageSize = 500;
        while (addUserIds.size() > page * pageSize) {
            List<Integer> collect = addUserIds.stream().skip(page * pageSize).limit(pageSize).collect(Collectors.toList());
            //封装信息
            List<RelationsEntity> relationsEntities = new ArrayList<>();
            collect.forEach(userId -> {
                RelationsEntity entity = new RelationsEntity()
                        .setFromId(Long.valueOf(userId))
                        .setRelationType(relationType)
                        .setToId(toId)
                        .setCount(1L)
                        .setCreateTime(System.currentTimeMillis())
                        .setUpdateTime(System.currentTimeMillis())
                        .setCreateUserId("0");
                relationsEntities.add(entity);
            });

            //批量插入
            clientRelationMapper.batchAddRelation(relationsEntities, toId);
            page++;
        }
    }

    /**
     * 根据toId从DB获取所有用户id
     * @param toId
     * @return
     */
    private LinkedHashSet<Integer> getDbAllFromId(Long toId) {
        int pageSize = 10000;
        int page = 1;
        LinkedHashSet<Integer> dbFromIds = clientRelationMapper.queryFromIdPage(toId, 0, pageSize);
        //减少DB查询次数
        if (dbFromIds.size() == pageSize) {
            Integer totalCount = clientRelationMapper.queryFromIdCount(toId);
            while (totalCount > (page * pageSize)) {
                LinkedHashSet<Integer> tempUserId = clientRelationMapper.queryFromIdPage(toId, page * pageSize, pageSize);
                dbFromIds.addAll(tempUserId);
                page++;
            }
        }
        return dbFromIds;
        //用户id转为Integer类型
//        return dbFromIds.stream().map(Long::intValue).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
