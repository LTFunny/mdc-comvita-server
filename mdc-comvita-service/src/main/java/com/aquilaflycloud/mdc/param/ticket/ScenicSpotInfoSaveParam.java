package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * ScenicSpotInfoSaveParam 保存权限数据
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@Data
@Accessors(chain = true)
public class ScenicSpotInfoSaveParam implements Serializable {
    private static final long serialVersionUID = -4701788634065776571L;

    @ApiModelProperty(value = "景区名称", required = true)
    @NotBlank(message = "景区名称不能为空")
    private String name;

    @ApiModelProperty(value = "景区简称", required = true)
    @NotBlank(message = "景区简称不能为空")
    private String simpleName;

    @ApiModelProperty(value = "景区图片url", required = true)
    @NotBlank(message = "景区图片不能为空")
    private String picUrl;

    @ApiModelProperty(value = "地址", required = true)
    @NotBlank(message = "地址不能为空")
    private String address;

    @ApiModelProperty(value = "营业开始时间", required = true)
    @NotBlank(message = "营业开始时间不能为空")
    private String businessStartTime;

    @ApiModelProperty(value = "营业结束时间", required = true)
    @NotBlank(message = "营业结束时间不能为空")
    private String businessEndTime;

    @ApiModelProperty(value = "客服联系电话", required = true)
    @NotBlank(message = "客服联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "景区简介", required = true)
    @NotBlank(message = "景区简介不能为空")
    private String introduction;

    @ApiModelProperty(value = "经度")
    @NotBlank(message = "经度不能为空")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @NotBlank(message = "纬度不能为空")
    private String latitude;

    @ApiModelProperty(value = "省编码")
    @NotBlank(message = "省编码不能为空")
    private String province;

    @ApiModelProperty(value = "市编码")
    @NotBlank(message = "市编码不能为空")
    private String city;

    @ApiModelProperty(value = "特色景点")
    private String specialScenicSpot;

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)", required = true)
    @NotNull(message = "景区类型不能为空")
    private ScenicSpotTypeEnum type;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}
