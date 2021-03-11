package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreOrderInfoResult
 * @author zly
 */
@Accessors(chain = true)
@Data
public class PreOrderInfoResult extends PreOrderInfo {

    @ApiModelProperty(value = "活动名称")
    private String activityName;
}
