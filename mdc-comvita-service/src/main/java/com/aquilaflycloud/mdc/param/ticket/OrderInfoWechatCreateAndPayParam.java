package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单创建和支付入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatCreateAndPayParam implements Serializable {
    private static final long serialVersionUID = 6129343102404258439L;

    @ApiModelProperty(value = "ota订单编号", hidden = true)
    private String otaOrderNo;

    @ApiModelProperty(value = "景区id", required = true)
    @NotNull(message = "景区id不能为空")
    private Long scenicSpotId;

    @ApiModelProperty(value = "渠道id,有时需要传")
    private Long channelId;

    @ApiModelProperty(value = "创建人部门ids", required = true)
    @NotBlank(message = "创建人部门不能为空")
    private String creatorOrgIds;

    @ApiModelProperty(value = "指定部门ids(多个以,分隔)", required = true)
    @NotBlank(message = "指定部门不能为空")
    private String designateOrgIds;

    @ApiModelProperty(value = "产品集合", required = true)
    @NotEmpty(message = "产品集合不能为空")
    @Valid
    private List<OrderInfoDetailInfo> orderInfoDetailInfoList;

    @ApiModelProperty(value = "游玩时间", required = true)
    @NotNull(message = "游玩时间不能为空")
    private Date playDate;

    @ApiModelProperty(value = "总金额", required = true)
    @NotNull(message = "总金额不能为空")
    private BigDecimal amount;
}
