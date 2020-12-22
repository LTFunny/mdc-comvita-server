package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.mdc.enums.shop.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * BaseInfoGetParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopInfoEditParam implements Serializable {

    private static final long serialVersionUID = 2953887925972694455L;

    @ApiModelProperty(value = "基础信息id", required = true)
    @NotNull(message = "基础信息id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否推荐")
    private ShopRecommendEnum isRecommend;

    @ApiModelProperty(value = "是否百年老店")
    private ShopCenturyShopEnum isCenturyShop;

    @ApiModelProperty(value = "店铺分类id", required = true)
    @NotNull(message = "店铺分类id不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;

    @ApiModelProperty(value = "是否使用店铺列表图(0|空:使用商户logo图;1:使用店铺列表图)")
    private ShopShowListPicEnum showListPic;

    @ApiModelProperty(value = "商户列表图url的json")
    private String listPicUrl;

    @ApiModelProperty(value = "是否使用店铺介绍图(0|空:使用商户logo图;1:使用店铺介绍图)")
    private ShopShowIntroductionPicEnum showIntroductionPic;

    @ApiModelProperty(value = "店铺介绍图url的json")
    private String introductionPicUrl;

    @ApiModelProperty(value = "是否使用店铺简介(0|空:使用商户备注;1:使用店铺简介)")
    private ShopShowIntroductionDescEnum showIntroductionDesc;

    @ApiModelProperty(value = "店铺简介")
    private String introductionDesc;

    @ApiModelProperty(value = "营业时间json")
    private String businessTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "电话")
    private String contactNumber;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "营业执照url")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "店铺二维码")
    private String qrCode;

    @ApiModelProperty(value = "标语")
    private String slogan;

    @ApiModelProperty(value = "跳转类型")
    private ShopJumpTypeEnum jumpType;

    @ApiModelProperty(value = "跳转信息")
    private String jumpInfo;
}
