package com.aquilaflycloud.mdc.model.wechat;

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
@TableName(value = "wechat_mini_program_device_info")
public class WechatMiniProgramDeviceInfo implements Serializable {
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
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信appId")
    private String appId;

    /**
     * 设备品牌
     */
    @TableField(value = "brand")
    @ApiModelProperty(value = "设备品牌")
    private String brand;

    /**
     * 设备型号
     */
    @TableField(value = "model")
    @ApiModelProperty(value = "设备型号")
    private String model;

    /**
     * 设备像素比
     */
    @TableField(value = "pixel_ratio")
    @ApiModelProperty(value = "设备像素比")
    private String pixelRatio;

    /**
     * 屏幕宽度，单位px
     */
    @TableField(value = "screen_width")
    @ApiModelProperty(value = "屏幕宽度，单位px")
    private String screenWidth;

    /**
     * 屏幕高度，单位px
     */
    @TableField(value = "screen_height")
    @ApiModelProperty(value = "屏幕高度，单位px")
    private String screenHeight;

    /**
     * 可使用窗口宽度，单位px
     */
    @TableField(value = "window_width")
    @ApiModelProperty(value = "可使用窗口宽度，单位px")
    private String windowWidth;

    /**
     * 可使用窗口高度，单位px
     */
    @TableField(value = "window_height")
    @ApiModelProperty(value = "可使用窗口高度，单位px")
    private String windowHeight;

    /**
     * 状态栏的高度，单位px
     */
    @TableField(value = "status_bar_height")
    @ApiModelProperty(value = "状态栏的高度，单位px")
    private String statusBarHeight;

    /**
     * 微信设置的语言
     */
    @TableField(value = "language")
    @ApiModelProperty(value = "微信设置的语言")
    private String language;

    /**
     * 微信版本号
     */
    @TableField(value = "version")
    @ApiModelProperty(value = "微信版本号")
    private String version;

    /**
     * 操作系统及版本
     */
    @TableField(value = "system")
    @ApiModelProperty(value = "操作系统及版本")
    private String system;

    /**
     * 客户端平台
     */
    @TableField(value = "platform")
    @ApiModelProperty(value = "客户端平台	")
    private String platform;

    /**
     * 用户字体大小，单位px
     */
    @TableField(value = "font_size_setting")
    @ApiModelProperty(value = "用户字体大小，单位px")
    private String fontSizeSetting;

    /**
     * 客户端基础库版本
     */
    @TableField(value = "sdk_version")
    @ApiModelProperty(value = "客户端基础库版本")
    private String sdkVersion;

    /**
     * 蓝牙的系统开关
     */
    @TableField(value = "bluetooth_enabled")
    @ApiModelProperty(value = "蓝牙的系统开关")
    private String bluetoothEnabled;

    /**
     * 地理位置的系统开关
     */
    @TableField(value = "location_enabled")
    @ApiModelProperty(value = "地理位置的系统开关")
    private String locationEnabled;

    /**
     * Wi-Fi 的系统开关
     */
    @TableField(value = "wifi_enabled")
    @ApiModelProperty(value = "Wi-Fi 的系统开关")
    private String wifiEnabled;

    /**
     * 设备信息json
     */
    @TableField(value = "info_text")
    @ApiModelProperty(value = "设备信息json")
    private String infoText;

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