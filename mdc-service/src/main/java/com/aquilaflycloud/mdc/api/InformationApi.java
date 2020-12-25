package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.information.Information;
import com.aquilaflycloud.mdc.param.information.InfoListParam;
import com.aquilaflycloud.mdc.service.InformationService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * InformationApi
 *
 * @author star
 * @date 2020-03-07
 */
@RestController
@Api(tags = "资讯公共接口")
public class InformationApi {

    @Resource
    private InformationService informationService;

    @ApiOperation(value = "获取资讯列表", notes = "获取资讯列表")
    @ApiMapping(value = "comvita.information.info.list", method = RequestMethod.POST)
    public List<Information> listInformation(InfoListParam param) {
        return informationService.listInformation(param);
    }

    @ApiOperation(value = "获取单个资讯(最新最重要)", notes = "获取单个资讯(最新最重要)")
    @ApiMapping(value = "comvita.information.importantest.get", method = RequestMethod.POST)
    public Information getImportantestInfo(InfoListParam param) {
        return informationService.getImportantestInfo(param);
    }
}
