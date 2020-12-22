package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.exchange.SpecValueTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ExchangeSpecValueGetParam
 *
 * @author zengqingjie
 * @date 2020-07-22
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "type", fieldValue = "VALUE", notNullFieldName = "pId", message = "父id不能为空"),
})
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeSpecValueGetParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "类型(exchange.SpecValueTypeEnum)", required = true)
    @NotNull(message = "类型不能为空")
    private SpecValueTypeEnum type;

    @ApiModelProperty(value = "父id(规格值必传)")
    private Long pId;

}
