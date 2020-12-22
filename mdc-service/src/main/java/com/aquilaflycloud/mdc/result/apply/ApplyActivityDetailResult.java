package com.aquilaflycloud.mdc.result.apply;

import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ApplyActivityDetailResult
 *
 * @author star
 * @date 2020-02-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyActivityDetailResult extends ApplyActivity {
    @ApiModelProperty(value = "浏览数")
    private Long readNum;

    @ApiModelProperty(value = "报名人数")
    private Long applyNum;

    @ApiModelProperty(value = "成功报名人数")
    private Long applySuccessNum;

    @ApiModelProperty(value = "是否已报名")
    private List<ApplyMemberRecord> recordList;

    @ApiModelProperty(value = "已报名人头像")
    private List<String> appliedAvatar;
}
