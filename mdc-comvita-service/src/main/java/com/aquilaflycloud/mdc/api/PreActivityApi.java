package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.pre.PreActivityGetParam;
import com.aquilaflycloud.mdc.param.pre.PreActivityPageParam;
import com.aquilaflycloud.mdc.result.pre.PreActivityInfoApiResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityPageApiResult;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PreActivityApi
 * @author linkq
 */
@RestController
@Api(tags = "活动接口")
public class PreActivityApi {

    @Resource
    private PreActivityService preActivityService;

    @ApiOperation(value = "获取活动列表", notes = "获取活动列表")
    @ApiMapping(value = "comvita.pre.activity.info.page", method = RequestMethod.POST)
    public IPage<PreActivityPageApiResult> pagePreActivity(PreActivityPageParam param) {
        return preActivityService.pagePreActivity(param);
    }

    @ApiOperation(value = "获取活动详情", notes = "获取活动详情")
    @ApiMapping(value = "comvita.pre.activity.info.get", method = RequestMethod.POST)
    public PreActivityInfoApiResult getPreActivity(PreActivityGetParam param) {
        return preActivityService.getPreActivity(param);
    }
}
