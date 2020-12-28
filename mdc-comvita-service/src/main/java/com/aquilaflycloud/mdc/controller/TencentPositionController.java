package com.aquilaflycloud.mdc.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.aquilaflycloud.mdc.extra.tencentPosition.req.SuggestionListReq;
import com.aquilaflycloud.mdc.extra.tencentPosition.resp.SuggestionListResp;
import com.aquilaflycloud.mdc.extra.tencentPosition.util.TencentPositionUtil;
import com.aquilaflycloud.mdc.param.tencentPosition.SuggestionListParam;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TencentPositionController
 *
 * @author star
 * @date 2019-12-05
 */
@RestController
@Api(tags = "腾讯位置服务接口")
public class TencentPositionController {

    @ApiOperation(value = "查询地址列表", notes = "根据关键词查询地址列表")
    @ApiMapping(value = "backend.comvita.tencentPosition.suggestion.list", method = RequestMethod.POST, permission = true)
    public List<SuggestionListResp> list(SuggestionListParam param) {
        SuggestionListReq req = new SuggestionListReq();
        BeanUtil.copyProperties(param, req, CopyOptions.create().ignoreError());
        return TencentPositionUtil.getSuggestionList(req
                .setPageIndex(param.getPageNum()).setRegionFix(param.getRegionFix().getType()));
    }
}
