package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentViewStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author linkq
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PreCommentPageParam extends PageParam<PreCommentInfo> {

    @ApiModelProperty(value = "活动标签id列表")
    private List<Long> folksonomyIds;

//    @ApiModelProperty(value = "活动点评id列表")
//    private List<Long> commentIds;

    @ApiModelProperty(value = "活动点评状态(pre.ActivityCommentStateEnum)")
    private ActivityCommentStateEnum comState;

    @ApiModelProperty(value = "点评人")
    private String commentator;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "展示状态（pre.ActivityCommentViewStateEnum）")
    private ActivityCommentViewStateEnum comViewState;

    @ApiModelProperty(value = "点评开始时间")
    private Date commentStartTime;

    @ApiModelProperty(value = "点评结束时间")
    private Date commentEndTime;

    @ApiModelProperty(value = "审核开始时间")
    private Date auditStartTime;

    @ApiModelProperty(value = "审核结束时间")
    private Date auditEndTime;

}


