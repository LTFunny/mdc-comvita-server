package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.exchange.SpecValueTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeSpecValueInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ExchangeSpecValuePageParam
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "type", fieldValue = "VALUE", notNullFieldName = "pId", message = "父id不能为空"),
})
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeSpecValuePageParam extends PageAuthParam<ExchangeSpecValueInfo> implements Serializable {
    @ApiModelProperty(value = "类型(exchange.SpecValueTypeEnum)", required = true)
    @NotNull(message = "类型不能为空")
    private SpecValueTypeEnum type;

    @ApiModelProperty(value = "父id")
    private Long pId;
}
