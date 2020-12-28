package com.aquilaflycloud.mdc.model.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.member.IdTypeEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "member_health_record")
public class MemberHealthRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 健康码
     */
    @TableField(value = "health_code")
    @ApiModelProperty(value = "健康码")
    private String healthCode;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 微信用户id
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信用户id")
    private String openId;

    /**
     * 支付宝用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "支付宝用户id")
    private String userId;

    /**
     * 微信或支付宝昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    /**
     * 微信或支付宝头像
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "微信或支付宝头像")
    private String avatarUrl;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 报备手机
     */
    @TableField(value = "report_phone")
    @ApiModelProperty(value = "报备手机")
    private String reportPhone;

    /**
     * 证件类型
     */
    @TableField(value = "id_type")
    @ApiModelProperty(value = "证件类型")
    private IdTypeEnum idType;

    /**
     * 证件号码
     */
    @TableField(value = "id_card")
    @ApiModelProperty(value = "证件号码")
    private String idCard;

    /**
     * 性别
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "性别")
    private SexEnum sex;

    /**
     * 健康状态1
     */
    @TableField(value = "health_state1")
    @ApiModelProperty(value = "健康状态1")
    private String healthState1;

    /**
     * 健康状态2
     */
    @TableField(value = "health_state2")
    @ApiModelProperty(value = "健康状态2")
    private String healthState2;

    /**
     * 健康状态3
     */
    @TableField(value = "health_state3")
    @ApiModelProperty(value = "健康状态3")
    private String healthState3;

    /**
     * 小程序码参数
     */
    @TableField(value = "scene_str")
    @ApiModelProperty(value = "小程序码参数")
    private String sceneStr;

    /**
     * 参数名称
     */
    @TableField(value = "scene_name")
    @ApiModelProperty(value = "参数名称")
    private String sceneName;

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
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}