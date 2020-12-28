package com.aquilaflycloud.mdc.result.shop;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * AlipayShopInfoResult
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
public class ShopInfoResult extends ShopInfo implements Serializable {
    private static final long serialVersionUID = 8623456304573821558L;

    @ApiModelProperty(value = "标签信息列表")
    List<FolksonomyInfo> folksonomyInfoList = new ArrayList<>();

    @ApiModelProperty(value = "评分")
    BigDecimal average = new BigDecimal(0);
}
