package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.ChangeGoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.GoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.PreGoodsInfoListParam;
import com.aquilaflycloud.mdc.param.pre.ReturnGoodsInfoParam;
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
    @ApiMapping(value = "backend.comvita.goods.info.page", method = RequestMethod.POST, permission = false)
    public IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param) {
        return preGoodsInfoService.pagePreGoodsInfoList(param);
    }

    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.add", method = RequestMethod.POST, permission = false)
    public BaseResult<String> addPreGoodsInfo(ReturnGoodsInfoParam param) {
        return new BaseResult().setResult( preGoodsInfoService.addPreGoodsInfo(param));
    }

    @ApiOperation(value = "编辑商品信息", notes = "编辑商品信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.edit", method = RequestMethod.POST, permission = false)
    public BaseResult<String> editPreGoodsInfo(ReturnGoodsInfoParam param) {
        return new BaseResult().setResult( preGoodsInfoService.editPreGoodsInfo(param));
    }

    @ApiOperation(value = "商品上下架", notes = "商品上下架")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.info.change", method = RequestMethod.POST, permission = false)
    public void changeGoodsType(ChangeGoodsInfoParam param) {
        preGoodsInfoService.changeGoodsType(param);
    }

    @ApiOperation(value = "商品详细信息", notes = "商品详细信息")
//    @PreAuthorize("hasAuthority('mdc:preGoodsInfo:list')")
    @ApiMapping(value = "backend.comvita.goods.data", method = RequestMethod.POST, permission = false)
    public BaseResult<ReturnGoodsInfoParam> goodsData(GoodsInfoParam param) {
        return new BaseResult<ReturnGoodsInfoParam>().setResult(preGoodsInfoService.goodsData(param));

    }
}
