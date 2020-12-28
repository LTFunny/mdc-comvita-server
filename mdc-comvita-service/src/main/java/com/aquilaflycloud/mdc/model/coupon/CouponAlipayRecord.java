package com.aquilaflycloud.mdc.model.coupon;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "coupon_alipay_record")
public class CouponAlipayRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 优惠券id
     */
    @TableField(value = "coupon_id")
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    /**
     * 券模板id
     */
    @TableField(value = "template_id")
    @ApiModelProperty(value = "券模板id")
    private String templateId;

    /**
     * 资金订单号
     */
    @TableField(value = "fund_order_no")
    @ApiModelProperty(value = "资金订单号")
    private String fundOrderNo;

    /**
     * 模板支付确认链接
     */
    @TableField(value = "confirm_uri")
    @ApiModelProperty(value = "模板支付确认链接")
    private String confirmUri;

    /**
     * 券类型
     */
    @TableField(value = "voucher_type")
    @ApiModelProperty(value = "券类型")
    private String voucherType;

    /**
     * 券名称
     */
    @TableField(value = "voucher_name")
    @ApiModelProperty(value = "券名称")
    private String voucherName;

    /**
     * 券使用场景
     */
    @TableField(value = "voucher_use_scene")
    @ApiModelProperty(value = "券使用场景")
    private String voucherUseScene;

    /**
     * 出资人登录账号
     */
    @TableField(value = "fund_account")
    @ApiModelProperty(value = "出资人登录账号")
    private String fundAccount;

    /**
     * 品牌信息
     */
    @TableField(value = "brand_name")
    @ApiModelProperty(value = "品牌信息")
    private String brandName;

    /**
     * 发放开始时间
     */
    @TableField(value = "publish_start_time")
    @ApiModelProperty(value = "发放开始时间")
    private Date publishStartTime;

    /**
     * 发放结束时间
     */
    @TableField(value = "publish_end_time")
    @ApiModelProperty(value = "发放结束时间")
    private Date publishEndTime;

    /**
     * 券有效期
     */
    @TableField(value = "voucher_valid_period")
    @ApiModelProperty(value = "券有效期")
    private String voucherValidPeriod;

    /**
     * 最低额度
     */
    @TableField(value = "floor_amount")
    @ApiModelProperty(value = "最低额度")
    private BigDecimal floorAmount;

    /**
     * 券使用说明
     */
    @TableField(value = "voucher_description")
    @ApiModelProperty(value = "券使用说明")
    private String voucherDescription;

    /**
     * 外部业务单号
     */
    @TableField(value = "out_biz_no")
    @ApiModelProperty(value = "外部业务单号")
    private String outBizNo;

    /**
     * 面额
     */
    @TableField(value = "amount")
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;

    /**
     * 拟发行券的数量
     */
    @TableField(value = "voucher_quantity")
    @ApiModelProperty(value = "拟发行券的数量")
    private Integer voucherQuantity;

    /**
     * 重定向地址
     */
    @TableField(value = "redirect_uri")
    @ApiModelProperty(value = "重定向地址")
    private String redirectUri;

    /**
     * 券变动异步通知地址
     */
    @TableField(value = "notify_uri")
    @ApiModelProperty(value = "券变动异步通知地址")
    private String notifyUri;

    /**
     * 规则配置
     */
    @TableField(value = "rule_conf")
    @ApiModelProperty(value = "规则配置")
    private String ruleConf;

    /**
     * 扩展字段
     */
    @TableField(value = "extension_info")
    @ApiModelProperty(value = "扩展字段")
    private String extensionInfo;

    /**
     * 模板状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "模板状态")
    private String status;

    /**
     * 总金额面额
     */
    @TableField(value = "total_amount")
    @ApiModelProperty(value = "总金额面额")
    private BigDecimal totalAmount;

    /**
     * 已发放张数
     */
    @TableField(value = "publish_count")
    @ApiModelProperty(value = "已发放张数")
    private Integer publishCount;

    /**
     * 已发放总金额
     */
    @TableField(value = "publish_amount")
    @ApiModelProperty(value = "已发放总金额")
    private BigDecimal publishAmount;

    /**
     * 已使用张数
     */
    @TableField(value = "used_count")
    @ApiModelProperty(value = "已使用张数")
    private Integer usedCount;

    /**
     * 已使用总金额
     */
    @TableField(value = "used_amount")
    @ApiModelProperty(value = "已使用总金额")
    private BigDecimal usedAmount;

    /**
     * 退回金额
     */
    @TableField(value = "recycle_amount")
    @ApiModelProperty(value = "退回金额")
    private BigDecimal recycleAmount;

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
     * 创建记录人id
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    @JSONField(serialize = false)
    private Long creatorId;

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
     * 指定用户部门ids
     */
    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    /**
     * 指定用户部门名称
     */
    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}