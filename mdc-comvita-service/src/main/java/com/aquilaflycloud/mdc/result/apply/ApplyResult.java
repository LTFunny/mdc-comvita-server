package com.aquilaflycloud.mdc.result.apply;

import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ApplyResult
 *
 * @author star
 * @date 2020-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyResult extends ApplyActivity {
    @ApiModelProperty(value = "浏览数")
    private Long readNum;

    @ApiModelProperty(value = "报名人数")
    private Long applyNum;

    @ApiModelProperty(value = "成功报名人数")
    private Long applySuccessNum;
}
