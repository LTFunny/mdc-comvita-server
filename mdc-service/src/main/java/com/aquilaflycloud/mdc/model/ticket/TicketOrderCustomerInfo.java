package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单出行人记录表
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-12-05
 */
@Data
public class TicketOrderCustomerInfo implements Serializable {
    private static final long serialVersionUID = 8101336335474335551L;
    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "姓名")
    @TableField("id_card_name")
    private String idCardName;

    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    @TableField("id_card_no")
    private String idCardNo;

    @ApiModelProperty(value = "商户订单号与OTA订单号关系ID", hidden = true)
    @TableField("orderno_otaorderno_relation_id")
    private Long ordernoOtaordernoRelationId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "会员id")
    @TableField("member_id")
    private Long memberId;

    @ApiModelProperty(value = "道控产品id")
    @TableField("product_id")
    private Integer productId;

    @ApiModelProperty(value = "租户id", hidden = true)
    @TableField("tenant_id")
    @JSONField(serialize = false)
    private Long tenantId;

    @ApiModelProperty(value = "子租户id", hidden = true)
    @TableField("sub_tenant_id")
    @JSONField(serialize = false)
    private Long subTenantId;

    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    public TicketOrderCustomerInfo(String idCardName, String mobile, String idCardNo, Long ordernoOtaordernoRelationId, Long memberId, Integer productId, String creatorOrgIds, String designateOrgIds) {
        this.idCardName = idCardName;
        this.mobile = mobile;
        this.idCardNo = idCardNo;
        this.ordernoOtaordernoRelationId = ordernoOtaordernoRelationId;
        this.memberId = memberId;
        this.productId = productId;
        this.creatorOrgIds = creatorOrgIds;
        this.designateOrgIds = designateOrgIds;
    }

    public TicketOrderCustomerInfo() {
    }
}
