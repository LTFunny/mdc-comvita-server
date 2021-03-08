package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
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
public class PreActivityPageParam extends PageParam<PreActivityInfo> {

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动类型(pre.ActivityTypeEnum)")
    private ActivityTypeEnum activityType;

    @ApiModelProperty(value = "状态(pre.ActivityStateEnum)")
    private ActivityStateEnum activityState;

    @ApiModelProperty(value = "活动标签id")
    private List<Long> folksonomyIds;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "关联门店名称")
    private String shopName;

}


