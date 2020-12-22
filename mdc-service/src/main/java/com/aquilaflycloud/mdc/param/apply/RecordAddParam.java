package com.aquilaflycloud.mdc.param.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RecordAddParam
 *
 * @author star
 * @date 2020-02-29
 */
@Data
@Accessors(chain = true)
public class RecordAddParam {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private Long applyId;

    @ApiModelProperty(value = "报名人姓名")
    private String applyName;

    @ApiModelProperty(value = "报名电话")
    private String applyPhone;

    @ApiModelProperty(value = "报名备注")
    private String applyRemark;

    @ApiModelProperty(value = "报名图片")
    private List<String> applyImgList;
}


