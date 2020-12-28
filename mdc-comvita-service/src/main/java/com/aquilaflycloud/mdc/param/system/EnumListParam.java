package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * EnumListParam
 *
 * @author star
 * @date 2019-12-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EnumListParam extends PageParam {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 枚举名称(英文字母)
     */
    @ApiModelProperty(value = "枚举名称(英文字母)", required = true)
    private String enumName;

    /**
     * 枚举包名
     */
    @ApiModelProperty(value = "枚举包名", required = true)
    private String enumPackage;
}
