package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PreQrcodeAddParam
 *
 * @author star
 * @date 2021/3/8
 */
@Data
public class PreQrcodeAddParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "门店信息列表", required = true)
    @NotEmpty(message = "门店信息列表不能为空")
    private List<OrgInfo> orgInfoList;

    @Data
    public static class OrgInfo {
        @ApiModelProperty(value = "门店id", required = true)
        @NotNull(message = "门店id不能为空")
        private Long orgId;

        @ApiModelProperty(value = "门店名称", required = true)
        @NotEmpty(message = "门店名称不能为空")
        private String orgName;
    }

}
