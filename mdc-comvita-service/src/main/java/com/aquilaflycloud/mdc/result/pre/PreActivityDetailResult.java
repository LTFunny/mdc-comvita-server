package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * PreActivityDetailResult
 * @author linkq
 */
@Data
public class PreActivityDetailResult {

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动类型")
    private ActivityTypeEnum activityType;

    @ApiModelProperty(value = "状态（1-未开始 2-进行中 3-已结束 4-已下架）")
    private ActivityStateEnum activityState;


    @ApiModelProperty(value = "活动标签名")
    private List<String> folksonomyNames;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

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

    /**
     * 活动照片
     */
    @ApiModelProperty(value = "活动照片")
    private String activityPicture;

    /**
     * 活动介绍
     */
    @ApiModelProperty(value = "活动介绍")
    private String activityProfile;

}
