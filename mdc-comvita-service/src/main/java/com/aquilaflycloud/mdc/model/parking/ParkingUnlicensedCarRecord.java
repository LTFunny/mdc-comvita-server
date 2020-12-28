package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.parking.CarStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "parking_unlicensed_car_record")
public class ParkingUnlicensedCarRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

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
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 车卡号
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "车卡号")
    private String cardId;

    /**
     * 短车卡号
     */
    @TableField(value = "short_card_id")
    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    /**
     * 卡片类型
     */
    @TableField(value = "card_type")
    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    /**
     * 车辆状态
     */
    @TableField(value = "car_state")
    @ApiModelProperty(value = "车辆状态")
    private CarStateEnum carState;

    /**
     * 进场扫码参数
     */
    @TableField(value = "in_scan_param")
    @ApiModelProperty(value = "进场扫码参数")
    private String inScanParam;

    /**
     * 出场扫码参数
     */
    @TableField(value = "out_scan_param")
    @ApiModelProperty(value = "出场扫码参数")
    private String outScanParam;

    /**
     * 进场是否强行开闸
     */
    @TableField(value = "in_force_up")
    @ApiModelProperty(value = "进场是否强行开闸")
    private WhetherEnum inForceUp;

    /**
     * 离场是否强行开闸
     */
    @TableField(value = "out_force_up")
    @ApiModelProperty(value = "离场是否强行开闸")
    private WhetherEnum outForceUp;

    /**
     * 进场操作人员
     */
    @TableField(value = "in_operator")
    @ApiModelProperty(value = "操作人员")
    private String inOperator;

    /**
     * 离场操作人员
     */
    @TableField(value = "out_operator")
    @ApiModelProperty(value = "操作人员")
    private String outOperator;

    /**
     * 进场时间
     */
    @TableField(value = "in_time")
    @ApiModelProperty(value = "进场时间")
    private Date inTime;

    /**
     * 离场时间
     */
    @TableField(value = "out_time")
    @ApiModelProperty(value = "离场时间")
    private Date outTime;

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