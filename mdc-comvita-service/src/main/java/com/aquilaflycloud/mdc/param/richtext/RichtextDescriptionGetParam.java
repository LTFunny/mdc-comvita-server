package com.aquilaflycloud.mdc.param.richtext;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.richtext.RichtextDescriptionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * RichtextDescriptionGetParam
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
@Data
@Accessors(chain = true)
public class RichtextDescriptionGetParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "描述类型(richtext.RichtextDescriptionTypeEnum)", required = true)
    @NotNull(message = "描述类型不能为空")
    private RichtextDescriptionTypeEnum type;
}


