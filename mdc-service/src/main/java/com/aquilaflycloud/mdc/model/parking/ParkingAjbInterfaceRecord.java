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
@TableName(value = "parking_ajb_interface_record")
public class ParkingAjbInterfaceRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 响应状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "响应状态")
    private Boolean status;

    /**
     * 错误代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "错误代码")
    private String code;

    /**
     * 错误信息
     */
    @TableField(value = "msg")
    @ApiModelProperty(value = "错误信息")
    private String msg;

    /**
     * 接口参数
     */
    @TableField(value = "param_content")
    @ApiModelProperty(value = "接口参数")
    private String paramContent;

    /**
     * 接口返回结果
     */
    @TableField(value = "result_content")
    @ApiModelProperty(value = "接口返回结果")
    private String resultContent;

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