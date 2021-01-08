package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.pre.PreActivityPageParam;
import com.aquilaflycloud.mdc.result.pre.PreActivityPageResult;
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
@Api(tags = "预售活动接口")
public class PreActivityApi {

    @Resource
    private PreActivityService preActivityService;

    @ApiOperation(value = "获取预售活动列表", notes = "获取预售活动列表")
    @ApiMapping(value = "comvita.pre.activity.info.page", method = RequestMethod.POST)
    public IPage<PreActivityPageResult> pagePreActivity(PreActivityPageParam param) {
        return preActivityService.pagePreActivity(param);
    }

}
