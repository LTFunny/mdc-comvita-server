package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "parking_ajb_lock_record")
public class ParkingAjbLockRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 数据标识
     */
    @TableField(value = "ajb_id")
    @ApiModelProperty(value = "数据标识")
    private String ajbId;

    /**
     * 企业编号
     */
    @TableField(value = "ltd_code")
    @ApiModelProperty(value = "企业编号")
    private String ltdCode;

    /**
     * 车场编号
     */
    @TableField(value = "park_code")
    @ApiModelProperty(value = "车场编号")
    private String parkCode;

    /**
     * 车场名称
     */
    @TableField(value = "park_name")
    @ApiModelProperty(value = "车场名称")
    private String parkName;

    /**
     * 卡片编号
     */
    @TableField(value = "card_sn_id")
    @ApiModelProperty(value = "卡片编号")
    private String cardSnId;

    /**
     * 物理卡号
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "物理卡号")
    private String cardId;

    /**
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 卡片类型
     */
    @TableField(value = "card_type")
    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    /**
     * 锁车状态(取值:0=解锁、1=锁车)
     */
    @TableField(value = "is_lock")
    @ApiModelProperty(value = "锁车状态(取值:0=解锁、1=锁车)")
    private String isLock;

    /**
     * 操作结果(取值:1=成功、0=失败)
     */
    @TableField(value = "is_suc")
    @ApiModelProperty(value = "操作结果(取值:1=成功、0=失败)")
    private String isSuc;

    /**
     * 操作时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "action_time")
    @ApiModelProperty(value = "操作时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String actionTime;

    /**
     * 创建时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "create_date")
    @ApiModelProperty(value = "创建时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String createDate;

    /**
     * 推送内容
     */
    @TableField(value = "data_content")
    @ApiModelProperty(value = "推送内容")
    private String dataContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

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