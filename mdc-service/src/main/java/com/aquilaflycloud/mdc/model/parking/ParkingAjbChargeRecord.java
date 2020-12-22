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
@TableName(value = "parking_ajb_charge_record")
public class ParkingAjbChargeRecord implements Serializable {
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
     * 日志标识
     */
    @TableField(value = "log_id")
    @ApiModelProperty(value = "日志标识")
    private String logId;

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
     * 机器编号
     */
    @TableField(value = "mac_code")
    @ApiModelProperty(value = "机器编号")
    private String macCode;

    /**
     * 物理卡号
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "物理卡号")
    private String cardId;

    /**
     * 卡片类型
     */
    @TableField(value = "card_type")
    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    /**
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 收费状态(备注:1=成功、2=失败)
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "收费状态(备注:1=成功、2=失败)")
    private String status;

    /**
     * 记录类型(取值:1=正常收费、2=人工收费)
     */
    @TableField(value = "rec_type")
    @ApiModelProperty(value = "记录类型(取值:1=正常收费、2=人工收费)")
    private String recType;

    /**
     * 计费标准
     */
    @TableField(value = "charge_car_type")
    @ApiModelProperty(value = "计费标准")
    private String chargeCarType;

    /**
     * 进场时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "pst_time")
    @ApiModelProperty(value = "进场时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String pstTime;

    /**
     * 收费时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "pet_time")
    @ApiModelProperty(value = "收费时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String petTime;

    /**
     * 停车时长(单位:分)
     */
    @TableField(value = "park_time")
    @ApiModelProperty(value = "停车时长(单位:分)")
    private String parkTime;

    /**
     * 计费时长(单位:分)
     */
    @TableField(value = "pfee_time")
    @ApiModelProperty(value = "计费时长(单位:分)")
    private String pfeeTime;

    /**
     * 应收金额(单位:元)
     */
    @TableField(value = "allfee")
    @ApiModelProperty(value = "应收金额(单位:元)")
    private String allfee;

    /**
     * 实收金额(单位:元)
     */
    @TableField(value = "editfee")
    @ApiModelProperty(value = "实收金额(单位:元)")
    private String editfee;

    /**
     * 优惠类型
     */
    @TableField(value = "fav_type")
    @ApiModelProperty(value = "优惠类型")
    private String favType;

    /**
     * 优惠数值
     */
    @TableField(value = "fav_value")
    @ApiModelProperty(value = "优惠数值")
    private String favValue;

    /**
     * 优惠金额(单位:元)
     */
    @TableField(value = "fav_money")
    @ApiModelProperty(value = "优惠金额(单位:元)")
    private String favMoney;

    /**
     * 优惠时间(单位:分)
     */
    @TableField(value = "fav_time")
    @ApiModelProperty(value = "优惠时间(单位:分)")
    private String favTime;

    /**
     * 收费人员
     */
    @TableField(value = "operator")
    @ApiModelProperty(value = "收费人员")
    private String operator;

    /**
     * 进场类型(取值:1=刷卡)
     */
    @TableField(value = "enter_type")
    @ApiModelProperty(value = "进场类型(取值:1=刷卡)")
    private String enterType;

    /**
     * 进场车类
     */
    @TableField(value = "in_car_type")
    @ApiModelProperty(value = "进场车类")
    private String inCarType;

    /**
     * 进场通道
     */
    @TableField(value = "in_door_name")
    @ApiModelProperty(value = "进场通道")
    private String inDoorName;

    /**
     * 收费类型
     */
    @TableField(value = "out_car_type")
    @ApiModelProperty(value = "收费类型")
    private String outCarType;

    /**
     * 收费通道
     */
    @TableField(value = "door_name")
    @ApiModelProperty(value = "收费通道")
    private String doorName;

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