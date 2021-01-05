package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.pre.PreExpressInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zengqingjie
 * @Date 2021-01-05
 */
@Data
public class PreExpressInfoPageParam extends PageParam<PreExpressInfo> {
    @ApiModelProperty(value = "快递名称")
    private String expressName;
}
