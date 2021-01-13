package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.GoodsSalesVolumeResult;
import com.aquilaflycloud.mdc.service.PreGoodsInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ProductInformationController
 *
 * @author zly
 */
@RestController
@Api(tags = "商品管理")
public class PreGoodsInfoController {

    @Resource
    private PreGoodsInfoService preGoodsInfoService;

    @ApiOperation(value = "查询商品列表", notes = "查询商品列表")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.page", method = RequestMethod.POST, permission = true)
    public IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param) {
        return preGoodsInfoService.pagePreGoodsInfoList(param);
    }

    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.add", method = RequestMethod.POST, permission = true)
    public void addPreGoodsInfo(ReturnGoodsInfoParam param) {
         preGoodsInfoService.addPreGoodsInfo(param);
    }

    @ApiOperation(value = "编辑商品信息", notes = "编辑商品信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.edit", method = RequestMethod.POST, permission = true)
    public void editPreGoodsInfo(ReturnGoodsInfoParam param) {
        preGoodsInfoService.editPreGoodsInfo(param);
    }

    @ApiOperation(value = "商品上下架", notes = "商品上下架")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.change", method = RequestMethod.POST, permission = true)
    public void changeGoodsType(ChangeGoodsInfoParam param) {
        preGoodsInfoService.changeGoodsType(param);
    }

    @ApiOperation(value = "商品详细信息", notes = "商品详细信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.data", method = RequestMethod.POST, permission = true)
    public BaseResult<ReturnGoodsInfoParam> goodsData(GoodsInfoParam param) {
        return new BaseResult<ReturnGoodsInfoParam>().setResult(preGoodsInfoService.goodsData(param));
    }
    @ApiOperation(value = "商品销量信息", notes = "商品销量信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.volume.data", method = RequestMethod.POST, permission = true)
    public GoodsSalesVolumeResult goodsVolume(GoodsSaleNumParam param) {
        return preGoodsInfoService.goodsVolume(param);
    }

}
