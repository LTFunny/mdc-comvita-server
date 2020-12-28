package com.aquilaflycloud.mdc.param.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ShopCommentInfoDeleteParam implements Serializable {
    private static final long serialVersionUID = 964760282570096374L;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
