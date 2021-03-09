package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.PreActivityRewardParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.util.List;

/**
 * PreActivityPageResult
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreActivityPageResult extends PreActivityInfo {

    @ApiModelProperty(value = "关联商品")
    private List<PreActivityRefGoodsResult> refGoodsCode;

    @ApiModelProperty(value = "活动标签")
    private List<PreActivityFolksonomyResult> folksonomyIds;

    @ApiModelProperty(value = "奖励规则")
    private List<PreActivityRewardParam> rewardRuleList;

    @ApiModelProperty(value = "关联门店列表")
    private List<RefShopInfoResult> refShops;

    @ApiModelProperty(value = "参加人数")
    private int participationCount;
}
