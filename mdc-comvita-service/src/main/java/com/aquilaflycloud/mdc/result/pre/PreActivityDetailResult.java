package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * PreActivityDetailResult
 * @author linkq
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreActivityDetailResult extends PreActivityInfo {
    @ApiModelProperty(value = "活动标签名")
    private List<String> folksonomyNames;

    @ApiModelProperty(value = "活动标签名")
    private List<PreActivityRewardResult> rewardRuleList;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "商品编号")
    private String refGoodsCode;

    /**
     * 关联的销售规则
     */
    @ApiModelProperty(value = "销售规则名")
    private String refRuleName;
}
