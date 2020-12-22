package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.param.coupon.*;
import com.aquilaflycloud.mdc.result.coupon.CouponInfoResult;
import com.aquilaflycloud.mdc.service.CatalogService;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * CouponApi
 *
 * @author star
 * @date 2020-03-10
 */
@RestController
@Api(tags = "优惠券相关接口")
public class CouponApi {
    @Resource
    private CatalogService catalogService;
    @Resource
    private CouponInfoService couponInfoService;

    @ApiOperation(value = "优惠券分类列表查询", notes = "会员优惠券分类列表查询")
    @ApiMapping(value = "mdc.coupon.catalog.list", method = RequestMethod.POST)
    public List<CatalogInfo> listCouponCatalog() {
        return catalogService.listCouponCatalog();
    }

    @ApiOperation(value = "优惠券列表查询(分页)", notes = "优惠券列表查询(分页)")
    @ApiMapping(value = "mdc.coupon.info.page", method = RequestMethod.POST)
    public IPage<CouponInfoResult> pageCoupon(CouponInfoPageParam param) {
        return couponInfoService.pageCouponInfo(param);
    }

    @ApiOperation(value = "获取优惠券详情", notes = "获取优惠券详情")
    @ApiMapping(value = "mdc.coupon.info.get", method = RequestMethod.POST)
    public CouponInfoResult getCoupon(CouponGetParam param) {
        return couponInfoService.getCouponInfo(param);
    }

    @ApiOperation(value = "领取优惠券", notes = "领取优惠券(返回记录id)")
    @ApiMapping(value = "mdc.coupon.rel.add", method = RequestMethod.POST)
    public BaseResult<String> addRel(CouponGetParam param) {
        return couponInfoService.addRel(param);
    }

    @ApiOperation(value = "会员优惠券记录查询(分页)", notes = "会员优惠券记录查询(分页)")
    @ApiMapping(value = "mdc.coupon.rel.page", method = RequestMethod.POST)
    public IPage<CouponMemberRel> pageCouponRel(CouponInfoRelPageParam param) {
        return couponInfoService.pageCouponInfoRel(param);
    }

    @ApiOperation(value = "获取会员优惠券详情", notes = "获取会员优惠券详情")
    @ApiMapping(value = "mdc.coupon.rel.get", method = RequestMethod.POST)
    public CouponMemberRel getCouponRel(CouponRelGetParam param) {
        return couponInfoService.getCouponInfoRel(param);
    }

    @ApiOperation(value = "核销会员优惠券", notes = "核销会员优惠券")
    @ApiMapping(value = "mdc.coupon.rel.use", method = RequestMethod.POST, permission = true)
    public CouponMemberRel useCouponRel(CouponMemberRelUseParam param) {
        return couponInfoService.useCouponRel(param);
    }

    @ApiOperation(value = "商铺获取优惠券", notes = "商铺获取优惠券")
    @ApiMapping(value = "mdc.exchange.goods.pageShopCoupon", method = RequestMethod.POST)
    public IPage<CouponInfoResult> pageShopCoupon(CouponInfoShopPageParam param) {
        return couponInfoService.pageShopCoupon(param);
    }
}
