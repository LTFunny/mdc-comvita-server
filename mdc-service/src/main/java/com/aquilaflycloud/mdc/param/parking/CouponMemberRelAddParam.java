package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CouponMemberRelAddParam
 *
 * @author star
 * @date 2020-01-15
 */
@NoneBlank(fieldNames = {"memberId", "carNo", "cardId", "shortCardId"}, message = "会员id,车牌号,车卡号,短车卡号不能同时为空")
@Data
@Accessors(chain = true)
public class CouponMemberRelAddParam {
    @ApiModelProperty(value = "停车券id", required = true)
    @NotNull(message = "停车券id不能为空")
    private Long couponId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员编码")
    private String memberCode;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    @ApiModelProperty(value = "派发金额")
    @DecimalMin(value = "0.01", message = "派发金额不能小于0.01")
    private BigDecimal value;

    @ApiModelProperty(value = "派发数")
    @Min(value = 1, message = "派发数不能小于1")
    private Integer count;

    @ApiModelProperty(value = "记录名称")
    private String recordName;

    @ApiModelProperty(value = "记录金额")
    private String recordAmount;

    @ApiModelProperty(value = "是否自动核销")
    private Boolean autoConsume = false;

    @ApiModelProperty(value = "派券说明")
    private String remark;

    @ApiModelProperty(value = "是否取消停车券领取和使用限制")
    private Boolean cancelLimit = false;
}
