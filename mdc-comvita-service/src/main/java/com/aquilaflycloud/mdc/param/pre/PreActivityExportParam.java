package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * PreActivityExportParam
 * 导出预售活动详情参数
 * @author linkq
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreActivityExportParam extends PageParam<PreActivityInfo> {

    @ApiModelProperty(value = "活动名称")
    private String activityName;

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
}
