package com.aquilaflycloud.mdc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.shop.*;
import com.aquilaflycloud.mdc.enums.system.BrandAptitudeTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "shop_info")
public class ShopInfo implements Serializable {
    private static final long serialVersionUID = 7581699825864786585L;

    @ApiModelProperty(value = "主键id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "关联id")
    @TableField("relation_id")
    private Long relationId;

    @ApiModelProperty(value = "商户类型(shop.ShopTypeEnum)：0：场内商户，1：场外商户")
    @TableField("shop_type")
    private ShopTypeEnum shopType;

    @ApiModelProperty(value = "关联的品牌id")
    @TableField("brand_id")
    private Long brandId;

    @ApiModelProperty(value = "业态id")
    @TableField("formats_type_id")
    private Long formatsTypeId;

    @ApiModelProperty(value = "商户简称")
    @TableField("shop_name")
    private String shopName;

    @ApiModelProperty(value = "商户主体全称")
    @TableField("shop_full_name")
    private String shopFullName;

    @ApiModelProperty(value = "租赁类型(shop.ShopTenantTypeEnum)")
    @TableField("tenant_type")
    private ShopTenantTypeEnum tenantType;

    @ApiModelProperty(value = "商户资质类型(shop.ShopAptitudeTypeEnum) 1：自营，2：联营，3：租赁")
    @TableField("shop_aptitude_type")
    private ShopAptitudeTypeEnum shopAptitudeType;

    @ApiModelProperty(value = "品牌资质类型(system.BrandAptitudeTypeEnum) 1：加盟，2：联营，3：直营")
    @TableField("brand_aptitude_type")
    private BrandAptitudeTypeEnum brandAptitudeType;

    @ApiModelProperty(value = "品牌资质到期时间")
    @TableField("brand_aptitude_expiry")
    private String brandAptitudeExpiry;

    @ApiModelProperty(value = "商户备注")
    @TableField("shop_remark")
    private String shopRemark;

    @ApiModelProperty(value = "标语")
    @TableField("slogan")
    private String slogan;

    @ApiModelProperty(value = "商户logo")
    @TableField("shop_logo")
    private String shopLogo;

    @ApiModelProperty(value = "商户简介图片")
    @TableField("shop_introduction_pic")
    private String shopIntroductionPic;

    @ApiModelProperty(value = "商户文件库")
    @TableField("shop_file")
    private String shopFile;

    @ApiModelProperty(value = "支付系统商户号")
    @TableField("payment_system_number")
    private String paymentSystemNumber;

    @ApiModelProperty(value = "商户联系人信息")
    @TableField("shop_linkman_info")
    private String shopLinkmanInfo;

    @ApiModelProperty(value = "商户状态(shop.ShopStateEnum)：0：意向，1：签约，：2：暂停合作，3：营业中，4：进场，5：装修中，6：非营业中")
    @TableField("state")
    private ShopStateEnum state;

    @ApiModelProperty(value = "跳转类型")
    @TableField("jump_type")
    private ShopJumpTypeEnum jumpType;

    @ApiModelProperty(value = "跳转信息")
    @TableField("jump_info")
    private String jumpInfo;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @JSONField(serialize = false)
    private Integer isDelete;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    @JSONField(serialize = false)
    private Long creatorId;

    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    @TableField(value = "last_operator_id", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人id", hidden = true)
    @JSONField(serialize = false)
    private Long lastOperatorId;

    @TableField(value = "last_operator_name", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人名称")
    private String lastOperatorName;

    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    @ApiModelProperty(value = "品牌名称")
    @TableField("brand_name")
    private String brandName;

    @ApiModelProperty(value = "业态名称")
    @TableField("formats_name")
    private String formatsName;

    @ApiModelProperty(value = "商铺号")
    @TableField("store_code")
    private String storeCode;

    @ApiModelProperty(value = "所属楼层数值")
    @TableField("floor_num")
    private String floorNum;

    @ApiModelProperty(value = "所属楼层名称")
    @TableField("floor_name")
    private String floorName;

    @ApiModelProperty(value = "是否使用店铺列表图(shop.ShopShowListPicEnum)(0|空:使用商户logo图;1:使用店铺列表图)")
    @TableField("show_list_pic")
    private ShopShowListPicEnum showListPic;

    @ApiModelProperty(value = "商户列表图url的json")
    @TableField("list_pic_url")
    private String listPicUrl;

    @ApiModelProperty(value = "是否使用店铺介绍图(shop.ShopShowIntroductionPicEnum)(0|空:使用商户logo图;1:使用店铺介绍图)")
    @TableField("show_introduction_pic")
    private ShopShowIntroductionPicEnum showIntroductionPic;

    @ApiModelProperty(value = "店铺介绍图url的json")
    @TableField("introduction_pic_url")
    private String introductionPicUrl;

    @ApiModelProperty(value = "是否使用店铺简介(shop.ShopShowIntroductionDescEnum)(0|空:使用商户备注;1:使用店铺简介)")
    @TableField("show_introduction_desc")
    private ShopShowIntroductionDescEnum showIntroductionDesc;

    @ApiModelProperty(value = "店铺简介")
    @TableField("introduction_desc")
    private String introductionDesc;

    @ApiModelProperty(value = "营业时间json")
    @TableField("business_time")
    private String businessTime;

    @ApiModelProperty(value = "地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty(value = "电话")
    @TableField("contact_number")
    private String contactNumber;

    @ApiModelProperty(value = "统一社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty(value = "营业执照url")
    @TableField("business_license_url")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "店铺二维码")
    @TableField("qr_code")
    private String qrCode;

    @ApiModelProperty(value = "店铺分类id")
    @TableField("category_id")
    private Long categoryId;

    @ApiModelProperty(value = "是否推荐(shop.ShopRecommendEnum)")
    @TableField("is_recommend")
    private ShopRecommendEnum isRecommend;

    @ApiModelProperty(value = "是否百年老店(shop.ShopCenturyShopEnum)")
    @TableField("is_century_shop")
    private ShopCenturyShopEnum isCenturyShop;

    @ApiModelProperty(value = "查询详情点击次数")
    @TableField("detail_click_count")
    private Long detailClickCount;

    @ApiModelProperty(value = "商场名称")
    @TableField("mall_name")
    private String mallName;
}
