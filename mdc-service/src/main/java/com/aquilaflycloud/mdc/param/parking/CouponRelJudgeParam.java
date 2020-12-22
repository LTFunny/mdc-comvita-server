package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * CouponRelUseParam
 *
 * @author star
 * @date 2020-02-11
 */
@NoneBlank(fieldNames = {"carNo", "cardId"}, message = "车牌号,车卡号不能同时为空")
@Data
@Accessors(chain = true)
public class CouponRelJudgeParam {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "停车券领取记录id", required = true)
    @NotEmpty(message = "停车券领取记录id不能为空")
    private List<Long> couponMemberRelIds;
}
