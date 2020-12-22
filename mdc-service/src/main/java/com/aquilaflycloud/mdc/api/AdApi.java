package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.ad.AdInfo;
import com.aquilaflycloud.mdc.param.ad.AdGetParam;
import com.aquilaflycloud.mdc.param.ad.AdListParam;
import com.aquilaflycloud.mdc.result.ad.AdInfoResult;
import com.aquilaflycloud.mdc.service.AdService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * AdApi
 *
 * @author star
 * @date 2019-11-20
 */
@RestController
@Api(tags = "广告接口")
public class AdApi {

    @Resource
    private AdService adService;

    @ApiOperation(value = "获取广告列表", notes = "获取广告列表")
    @ApiMapping(value = "mdc.ad.info.list", method = RequestMethod.POST)
    public List<AdInfoResult> listAd(AdListParam param) {
        return adService.listAd(param);
    }

    @ApiOperation(value = "获取广告详情", notes = "获取广告详情")
    @ApiMapping(value = "mdc.ad.info.get", method = RequestMethod.POST)
    public AdInfo get(AdGetParam param) {
        return adService.getAdInfo(param);
    }

}
