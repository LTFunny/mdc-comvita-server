package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * UpdateScenicSpotInfoDesignateOrgIdsByIdParam 根据景区id更新指定部门ids
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@Data
@Accessors(chain = true)
public class ScenicSpotInfoUpdateByIdParam implements Serializable {
    private static final long serialVersionUID = 9055015094850526100L;
    @ApiModelProperty(value = "景区id", required = true)
    @NotNull(message = "景区id不能为空")
    private Long id;

    @ApiModelProperty(value = "指定可看部门ids(多个以,分隔)", required = true)
    @NotNull(message = "指定可看部门不能为空")
    private String designateOrgIds;

    @ApiModelProperty(value = "账号id", required = true)
    @NotNull(message = "账号信息不能为空")
    private Long accountId;
}
