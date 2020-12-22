package com.aquilaflycloud.mdc.extra.tencentPosition.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SuggestionListResp {
    @ApiModelProperty(value = "POI唯一标识")
    private String id;

    @ApiModelProperty(value = "提示文字")
    private String title;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "POI类型，值说明：0:普通POI / 1:公交车站 / 2:地铁站 / 3:公交线路 / 4:行政区划")
    private int type;

    @ApiModelProperty(value = "提示所述位置坐标")
    private Location location;

    @ApiModelProperty(value = "行政区划代码")
    private long adcode;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @Data
    private class Location {
        @ApiModelProperty(value = "纬度")
        private double lat;

        @ApiModelProperty(value = "经度")
        private double lng;
    }
}
