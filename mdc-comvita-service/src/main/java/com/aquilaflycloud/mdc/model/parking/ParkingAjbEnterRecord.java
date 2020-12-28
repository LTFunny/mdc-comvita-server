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
@TableName(value = "parking_ajb_enter_record")
public class ParkingAjbEnterRecord implements Serializable {
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
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 车辆类型
     */
    @TableField(value = "in_car_type")
    @ApiModelProperty(value = "车辆类型")
    private String inCarType;

    /**
     * 卡片类型
     */
    @TableField(value = "card_type")
    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    /**
     * 进离状态(取值:1=进场、2=离场)
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "进离状态(取值:1=进场、2=离场)")
    private String status;

    /**
     * 强行开闸(取值:1=是、2=否)
     */
    @TableField(value = "upseason")
    @ApiModelProperty(value = "强行开闸(取值:1=是、2=否)")
    private String upseason;

    /**
     * 刷卡时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "acc_time")
    @ApiModelProperty(value = "刷卡时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String accTime;

    /**
     * 图片地址
     */
    @TableField(value = "file_path")
    @ApiModelProperty(value = "图片地址")
    private String filePath;

    /**
     * 操作人员
     */
    @TableField(value = "operator")
    @ApiModelProperty(value = "操作人员")
    private String operator;

    /**
     * 手持操作(备注:0=否、1=是)
     */
    @TableField(value = "is_handset")
    @ApiModelProperty(value = "手持操作(备注:0=否、1=是)")
    private String isHandset;

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