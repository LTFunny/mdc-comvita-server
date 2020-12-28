package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import com.aquilaflycloud.mdc.param.recommendation.RecommendationGetParam;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationApiResult;
import com.aquilaflycloud.mdc.service.RecommendationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * RecommendationApi
 *
 * @author star
 * @date 2020-03-28
 */
@RestController
@Api(tags = "最新推荐接口")
public class RecommendationApi {

    @Resource
    private RecommendationService recommendationService;

    @ApiOperation(value = "查询最新推荐列表", notes = "查询最新推荐列表")
    @ApiMapping(value = "comvita.recommendation.info.page", method = RequestMethod.POST)
    public IPage<Recommendation> page(PageParam param) {
        return recommendationService.page(param);
    }

    @ApiOperation(value = "获取最新推荐列表详情", notes = "获取最新推荐列表详情")
    @ApiMapping(value = "comvita.recommendation.info.get", method = RequestMethod.POST)
    public RecommendationApiResult get(RecommendationGetParam param) {
        return recommendationService.get(param);
    }
}
