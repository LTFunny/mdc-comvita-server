package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.exchange.RecommendGoodsAddParam;
import com.aquilaflycloud.mdc.param.exchange.RecommendGoodsDeleteParam;
import com.aquilaflycloud.mdc.param.exchange.RecommendGoodsEditParam;
import com.aquilaflycloud.mdc.param.exchange.RecommendGoodsPageParam;
import com.aquilaflycloud.mdc.result.exchange.RecommendGoodsResult;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ExchangeRecommendController
 *
 * @author star
 * @date 2020-04-19
 */
@RestController
@Api(tags = "兑换商城推荐管理")
public class ExchangeRecommendController {

    @Resource
    private ExchangeService exchangeService;

    @ApiOperation(value = "兑换商品推荐列表查询(分页)", notes = "兑换商品推荐列表查询(分页)")
    @PreAuthorize("hasAuthority('mdc:exchangeRecommend:list')")
    @ApiMapping(value = "backend.mdc.exchange.recommend.page", method = RequestMethod.POST, permission = true)
    public IPage<RecommendGoodsResult> pageRecommend(RecommendGoodsPageParam param) {
        return exchangeService.pageRecommend(param);
    }

    @ApiOperation(value = "新增推荐商品", notes = "新增推荐商品")
    @PreAuthorize("hasAuthority('mdc:exchangeRecommend:add')")
    @ApiMapping(value = "backend.mdc.exchange.recommend.add", method = RequestMethod.POST, permission = true)
    public void addRecommend(RecommendGoodsAddParam param) {
        exchangeService.addRecommend(param);
    }

    @ApiOperation(value = "修改推荐商品", notes = "修改推荐商品")
    @PreAuthorize("hasAuthority('mdc:exchangeRecommend:edit')")
    @ApiMapping(value = "backend.mdc.exchange.recommend.edit", method = RequestMethod.POST, permission = true)
    public void editRecommend(RecommendGoodsEditParam param) {
        exchangeService.editRecommend(param);
    }

    @ApiOperation(value = "删除推荐商品", notes = "删除推荐商品")
    @PreAuthorize("hasAuthority('mdc:exchangeRecommend:delete')")
    @ApiMapping(value = "backend.mdc.exchange.recommend.delete", method = RequestMethod.POST, permission = true)
    public void deleteRecommend(RecommendGoodsDeleteParam param) {
        exchangeService.deleteRecommend(param);
    }
}
