package com.aquilaflycloud.mdc.result.shop;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SystemShopInfoResult
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShopInfoDetailResult extends ShopInfo {
    @ApiModelProperty(value = "标签信息列表")
    List<FolksonomyInfo> folksonomyInfoList;
}
