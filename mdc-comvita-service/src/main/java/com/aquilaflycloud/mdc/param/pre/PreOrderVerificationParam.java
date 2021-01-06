package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 15:30
 * @Version 1.0
 */
@Data
public class PreOrderVerificationParam {

    @ApiModelProperty(value = "提货卡号")
    private String pickingCode;

    @ApiModelProperty(value = "核销人id")
    private Long verificaterId;

    @ApiModelProperty(value = "核销人名称")
    private String verificaterName;

    @ApiModelProperty(value = "核销人所属部门ids")
    private String verificaterOrgIds;

    @ApiModelProperty(value = "核销人所属部门名称")
    private String verificaterOrgNames;


}
