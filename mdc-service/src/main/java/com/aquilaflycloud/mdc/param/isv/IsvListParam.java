package com.aquilaflycloud.mdc.param.isv;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * IsvListParam
 *
 * @author star
 * @date 2019-12-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class IsvListParam extends PageParam {
    @ApiModelProperty(value = "isv应用id")
    private String appKey;
}
