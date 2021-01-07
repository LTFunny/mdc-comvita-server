package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 报表查询参数
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ReportFormParam extends PageParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品主键")
    @NotNull(message = "商品主键不能为空")
    private String id;


}
