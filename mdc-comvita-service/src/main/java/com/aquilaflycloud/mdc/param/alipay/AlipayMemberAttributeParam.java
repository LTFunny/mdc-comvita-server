package com.aquilaflycloud.mdc.param.alipay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * AlipayMemberAttributeParam
 *
 * @author Zengqingjie
 * @date 2020-05-27
 */
@Data
@Accessors(chain = true)
public class AlipayMemberAttributeParam implements Serializable {
    @ApiModelProperty(value = "开始日期(yyyy-MM-dd)", required = true)
    @NotBlank(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM-dd)", required = true)
    @NotBlank(message = "结束日期不能为空")
    private Date endDate;

    @ApiModelProperty(value = "支付宝授权账号id", required = true)
    @NotNull(message = "授权账号id不能为空")
    private Long alipayAuthorSiteId;
}
