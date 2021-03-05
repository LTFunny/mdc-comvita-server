package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.PreActivityRewardParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreActivityPageApiResult
 *
 * @author star
 * @date 2021/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreActivityPageApiResult extends PreActivityInfo {
    @ApiModelProperty(value = "奖励规则")
    private List<PreActivityRewardParam> rewardRuleList;
}
