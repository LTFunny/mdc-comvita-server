package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreActivityPageResult
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreActivityPageResult extends PreActivityInfo {

    @ApiModelProperty(value = "关联商品编号")
    private String refGoodsCode;

    @ApiModelProperty(value = "活动标签Id")
    private List<Long> folksonomyIds;

}
