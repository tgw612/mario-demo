package com.mall.discover.request.video;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class TaskVideo extends CommonRequest {

    private String id;

    /**
     * 业务CODE
     */
    private String bizCode;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 主播用户ID
     */
    private Integer memberId;

    /**
     * 关联任务id
     */
    private String taskId;

    /**
     * 发布任务商家ID
     */
    private Integer sellerId;

    /**
     * 任务类型，0：非任务视频；1：普通任务视频；2：抢单任务视频；
     */
    private Integer taskType;


    /**
     * 视频任务来源 -1无来源、0商家来源、1平台来源
     */
    private Integer pubType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * SDK上传视频返回ID
     */
    private String vodID;

    /**
     * 短视频封面
     */
    private String coverUrl;

    /**
     * 短视频url
     */
    private String videoUrl;

    /**
     * 视频第一帧地址
     */
    private String firstFrame;

    /**
     * 视频状态
     */
    private Integer videoStatus;

    /**
     * 视频时长（单位秒）
     */
    private Integer duration;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人ID
     */
    private Integer auditorId;

    /** 审核平台 */
    private Integer platform;

    /**
     * 审核不通过原因
     */
    private String verifyFailReason;

    /**
     * 上线状态 0.下线 1.上线（默认）,2. 审核不通过，主播自行发布
     */
    private Integer onlineStatus;

    /**
     * 下线原因
     */
    private String offLineReason;

    /**
     * 整点抢单任务主推商品的索引，从0开始
     */
    private Integer majorPrductIdx;

    /**
     * 关联商品ID列表
     */
    private List<Integer> productIdList;

    /**
     * 是否允许APP显示：1：是；2：否；
     */
    private Integer allowAppPlay;

    /**
     * 是否允许APP显示 1：是；2：否；
     */
    private Integer allowMicroappPlay;

    /**
     * 上传来源 1：APP；2：小程序
     */
    private Integer uploadFrom;

    /**
     * 关联的音乐ID
     */
    private String musicId;

    /**
     * 是否已删除 0：否、 1：是；
     */
    private Integer deleted;

    /**
     * 视频审核标签
     */
    private List<String> tagIdList;

    /**
     * 拍一条任务视频获得的任务奖金（分）
     */
    private Integer award;

    /**
     * 视频关联商品卖出后获得的累计收益（分）,每成交一次则累加一次saleProfit
     */
    private Integer saleProfit;

    /**
     * 奖金到账状态 0：未到账；1：全额到账：2：部分到账；
     */
    private Integer awardStatus;

    /**
     * 内容
     */
    private String content;

    /**
     * 视频关联的圈子
     */
    private List<String> circleList;

    /**
     * 关联度商品个数
     */
    private Integer goodsNum;

    /**
     * 发布用户来源 1：用户，2：运营;
     */
    private Integer memberType;

    /**
     * 短视频云审核结果
     */
    private String checkResult;

    /**
     * 短视频云审核结果备注
     */
    private String checkRemark;

    /**
     * 视频下载地址
     */
    private String downloadUrl;

    /**
     * 原始视频地址
     */
    private String videoCopy;

    //*************************************** 统计字段 Start ***************************/
    /**
     * 视频PV
     */
    private Integer pv;

    /**
     * 视频UV
     */
    private Integer uv;

    /**
     * 喜欢数
     */
    private Integer likeNum;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 喜欢数数
     */
    private Integer realLikeNum;

    /**
     * 分享数
     */
    private Integer shareNum;

    /**
     * 真实分享数
     */
    private Integer realShareNum;

    /**
     * 浏览播放数
     */
    private Integer playNum;

    /**
     * 真实浏览播放数
     */
    private Integer realPlayNum;

    /**
     * 单用户播放数
     */
    private Integer uvPlayNum;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 举报数
     */
    private Integer reportNum;

    /**
     * 交易订单（只增不减）
     */
    private Integer orderNum;
    //*************************************** 统计字段 End ***************************/


    //*************************************** 运营相关字段 Start ***************************/
    /**
     * 热门分数值
     */
    private Integer score;

    /**
     * 是否强推广告
     */
    private Integer isAdv;

    /**
     * 平台推荐 0：否； 1：是；
     */
    private Integer platRecommend;

    /**
     * APP视频推荐到首页 1：是，2否
     */
    private Integer toHomePage;

    /**
     * 纯视频页推荐排序
     */
    private Integer orderForVideo;

    /**
     * 小程序视频/图文推荐到首页 1：是，2否
     */
    private Integer toHomePageMix;

    /**
     * 图文视频混合页推荐排序
     */
    private Integer orderForMix;

    /**
     * 是否推荐至发布页 1-是 0-否
     */
    private Integer toPublishPage;

    /**
     * 将单独视频推荐至首页 1：是，否则否
     */
    private Integer toHomePageVideo;

    //*************************************** 运营相关字段 End ***************************/

    /**
     * 类型 1：视频； 2：图片；
     */
    private Integer type;

    /**
     * 关联图片列表
     */
    private List<String> pictureUrls;

    /**
     * 小程序发布的信息状态:1启用(默认) 2停用
     */
    private Integer status;

    /**
     * 标签
     */
    private String tag;

    /**
     * 图文视频推荐到热门 1：是；2：否；
     */
    private Integer toHotPage;

    /**
     * 话题id
     */
    private String topicId;

    /**
     * 话题
     */
    private String topic;

    /**
     * 话题热门排序，小的排在前面
     */
    private Integer sortTopicHot;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 创建用户ID
     */
    private Integer createId;

    /**
     * 更新用户ID
     */
    private Integer updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     *功能描述:运营上传图文内容的关联内容ID列表
     * @author 秦垄
     * @date 2019/7/26 17:43
     */
    private List<String> idList;
}
