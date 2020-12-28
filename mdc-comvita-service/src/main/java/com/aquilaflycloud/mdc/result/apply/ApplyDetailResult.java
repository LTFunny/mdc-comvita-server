package com.aquilaflycloud.mdc.result.apply;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ApplyDetailResult
 *
 * @author star
 * @date 2020-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyDetailResult extends ApplyActivity {
    @ApiModelProperty(value = "浏览数")
    private Long readNum;

    @ApiModelProperty(value = "报名人数")
    private Long applyNum;

    @ApiModelProperty(value = "成功报名人数")
    private Long applySuccessNum;

    @ApiModelProperty(value = "待审报名人数")
    private Long applyPendingNum;

    @ApiModelProperty(value = "剩余报名人数")
    private Long applySurplusNum;

    @ApiModelProperty(value = "报名规则")
    private ApplyRule applyRule;

    @ApiModelProperty(value = "标签信息列表")
    private List<FolksonomyInfo> folksonomyInfoList;
}
