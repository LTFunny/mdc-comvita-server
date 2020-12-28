package com.aquilaflycloud.mdc.param.richtext;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.richtext.RichtextDescriptionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * RichtextDescriptionGetParam
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
@Data
@Accessors(chain = true)
public class RichtextDescriptionEditParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "描述内容")
    private String content;

    @ApiModelProperty(value = "描述类型(richtext.RichtextDescriptionTypeEnum)")
    private RichtextDescriptionTypeEnum type;
}


