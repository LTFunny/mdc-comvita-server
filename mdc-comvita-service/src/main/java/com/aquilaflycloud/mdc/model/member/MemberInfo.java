package com.aquilaflycloud.mdc.model.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.SubSourceEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "member_info")
public class MemberInfo implements Serializable {
    /**
     * 会员id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "会员id")
    private Long id;

    /**
     * 会员编码
     */
    @TableField(value = "member_code")
    @ApiModelProperty(value = "会员编码")
    private String memberCode;

    /**
     * 会员名称
     */
    @TableField(value = "member_name")
    @ApiModelProperty(value = "会员名称")
    private String memberName;

    /**
     * 会员密码
     */
    @TableField(value = "member_pwd")
    @ApiModelProperty(value = "会员密码", hidden = true)
    @JSONField(serialize = false)
    private String memberPwd;

    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 身份证号码
     */
    @TableField(value = "id_card")
    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 性别
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "性别")
    private SexEnum sex;

    /**
     * 国家
     */
    @TableField(value = "country")
    @ApiModelProperty(value = "国家")
    private String country;

    /**
     * 省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 区域
     */
    @TableField(value = "county")
    @ApiModelProperty(value = "区域")
    private String county;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 会员头像
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "会员头像")
    private String avatarUrl;

    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    /**
     * 是否授权
     */
    @TableField(value = "is_auth")
    @ApiModelProperty(value = "是否授权")
    private WhetherEnum isAuth;

    /**
     * 授权时间
     */
    @TableField(value = "auth_time")
    @ApiModelProperty(value = "授权时间")
    private Date authTime;

    /**
     * 最新授权时间
     */
    @TableField(value = "last_auth_time")
    @ApiModelProperty(value = "最新授权时间")
    private Date lastAuthTime;

    /**
     * 微信appId
     */
    @TableField(value = "wx_app_id")
    @ApiModelProperty(value = "微信appId")
    private String wxAppId;

    /**
     * 微信openId
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信openId")
    private String openId;

    /**
     * 微信unionId
     */
    @TableField(value = "union_id")
    @ApiModelProperty(value = "微信unionId")
    private String unionId;

    /**
     * 微信授权信息
     */
    @TableField(value = "wx_content")
    @ApiModelProperty(value = "微信授权信息")
    private String wxContent;

    /**
     * 支付宝appId
     */
    @TableField(value = "ali_app_id")
    @ApiModelProperty(value = "支付宝appId")
    private String aliAppId;

    /**
     * 支付宝userId
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "支付宝userId")
    private String userId;

    /**
     * 支付宝授权信息
     */
    @TableField(value = "ali_content")
    @ApiModelProperty(value = "支付宝授权信息")
    private String aliContent;

    /**
     * 小程序最后互动操作时间
     */
    @TableField(value = "last_operation_time")
    @ApiModelProperty(value = "小程序最后互动操作时间")
    private Date lastOperationTime;

    /**
     * 是否需要清洗
     */
    @TableField(value = "need_merge")
    @ApiModelProperty(value = "是否需要清洗", hidden = true)
    @JSONField(serialize = false)
    private WhetherEnum needMerge;

    /**
     * 会员类型
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "会员类型")
    private SourceEnum source;

    /**
     * 会员子类型
     */
    @TableField(value = "sub_source")
    @ApiModelProperty(value = "会员子类型")
    private SubSourceEnum subSource;

    /**
     * 导购员id
     */
    @TableField(value = "guide_id")
    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    /**
     * 子租户id
     */
    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;

    /**
     * 创建记录人名称
     */
    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

    /**
     * 创建用户所属部门ids
     */
    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    /**
     * 创建用户所属部门名称
     */
    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    /**
     * 最后操作人id
     */
    @TableField(value = "last_operator_id", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人id", hidden = true)
    @JSONField(serialize = false)
    private Long lastOperatorId;

    /**
     * 最后操作人名称
     */
    @TableField(value = "last_operator_name", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人名称")
    private String lastOperatorName;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}
