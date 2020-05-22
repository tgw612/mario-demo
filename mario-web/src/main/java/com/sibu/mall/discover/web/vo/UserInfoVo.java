package com.mall.discover.web.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class UserInfoVo implements Serializable {
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 会员等级
     */
    private Integer grade;
    /**
     * 会员经验值
     */
    private Integer gradeValue;
    /**
     * 会员积分
     */
    private Integer integral;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    /**
     * 登陆次数
     */
    private Integer loginNumber;
    /**
     * 上次使用的地址
     */
    private Integer lastAddressId;
    /**
     * 上次使用的支付方式
     */
    private Integer lastPaymentCode;
    /**
     * 性别0、保密；1、男；2、女
     */
    private Integer gender;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 邮箱
     */
    private String email;
    /**
     * qq
     */
    private String qq;
    /**
     * mobile
     */
    private String mobile;
    /**
     * 电话
     */
    private String phone;
    /**
     * 密码输入错误次数
     */
    private Integer pwdErrCount;
    /**
     * 会员来源1、pc；2、H5；3、Android；4、IOS ;5、微信商城
     */
    private Integer source;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 账户支付密码
     */
    private String balancePwd;
    /**
     * 是否验证邮箱0、未验证；1、验证
     */
    private Integer isEmailVerify;
    /**
     * 是否验证手机0、未验证；1、验证
     */
    private Integer isSmsVerify;
    /**
     * 短信验证码
     */
    private String smsVerifyCode;
    /**
     * 邮件验证码
     */
    private String emailVerifyCode;
    /**
     * 是否接受短信0、不接受；1、接受
     */
    private Integer canReceiveSms;
    /**
     * 是否接收邮件
     */
    private Integer canReceiveEmail;
    /**
     * 1、正常使用；2、停用
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 用户头像
     */
    private String headImg;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 新微商用户id
     */
    private String xwsMemberId;
    /**
     * 是否同意平台服务协议（1是，0否）
     */
    private Integer isAgree;
    /**
     * 推荐人ID
     */
    private Integer referrerId;
    /**
     * 是否可以发布动态
     */
    private Byte  canPublish;

}
