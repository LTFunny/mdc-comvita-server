package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.result.pre.PreGoods4ActivityResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * PreActivityUpdateParam
 * @author linkq
 */
@Data
public class PreActivityUpdateParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    @NotNull(message = "活动名称不能为空")
    @Size(message = "名称不能超过{max}字符",max=100)
    private String activityName;

    /**
     * 活动类型
     */
    @ApiModelProperty(value = "活动类型(pre.ActivityTypeEnum)")
    @NotNull(message = "活动类型不能为空")
    private ActivityTypeEnum activityType;

    /**
     * 状态（1-未开始 2-进行中 3-已结束 4-已下架）
     */
    @ApiModelProperty(value = "活动状态(pre.ActivityStateEnum)")
    private ActivityStateEnum activityState = ActivityStateEnum.NOT_STARTED;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    @NotNull(message = "活动结束时间不能为空")
    private Date endTime;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品")
    private PreGoods4ActivityResult refGoods;

    /**
     * 关联的销售规则
     */
    @ApiModelProperty(value = "关联的销售规则")
    private PreEnableRuleResult refRule;

    /**
     * 活动照片
     */
    @ApiModelProperty(value = "活动照片")
    @NotNull(message = "活动照片不能为空")
    private String activityPicture;

    /**
     * 活动介绍
     */
    @ApiModelProperty(value = "活动介绍")
    @NotNull(message = "活动介绍不能为空")
    private String activityProfile;


    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}