package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.ButtonStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.PreActivityRewardParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreActivityInfoApiResult
 *
 * @author star
 * @date 2021/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreActivityInfoApiResult extends PreActivityInfo {
    @ApiModelProperty(value = "奖励规则")
    private List<PreActivityRewardParam> rewardRuleList;

    @ApiModelProperty(value = "按钮状态（pre.ButtonStateEnum）")
    private ButtonStateEnum buttonState;

    @ApiModelProperty(value = "可选门店列表")
    private List<ShopInfo> shopList;

    @Data
    public static class ShopInfo {
        @ApiModelProperty(value = "可选门店id")
        private Long shopId;

        @ApiModelProperty(value = "可选门店名称")
        private String shopName;
    }
}
