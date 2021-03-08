package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PreQrcodeDownloadParam
 * @author linkq
 */
@Data
public class PreQrcodeDownloadParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "门店信息列表", required = true)
    @NotEmpty(message = "门店id列表不能为空")
    private List<Long> orgIdList;

}
