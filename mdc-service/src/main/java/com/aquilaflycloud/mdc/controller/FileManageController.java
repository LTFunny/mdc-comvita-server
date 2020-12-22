package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.system.SystemExportLog;
import com.aquilaflycloud.mdc.param.system.ExcelDownloadParam;
import com.aquilaflycloud.mdc.param.system.ExcelPageParam;
import com.aquilaflycloud.mdc.param.system.FileUploadParam;
import com.aquilaflycloud.mdc.service.SystemFileService;
import com.aquilaflycloud.util.AliOssUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import com.gitee.sop.servercommon.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * FileManageController
 *
 * @author star
 * @date 2019-11-26
 */
@RestController
@Api(tags = "文件管理")
public class FileManageController {

    @Resource
    private SystemFileService systemFileService;

    @ApiOperation(value = "文件上传", notes = "文件上传,返回文件路径")
    @ApiMapping(value = "backend.mdc.file.info.upload", method = RequestMethod.POST, permission = true)
    public BaseResult<String> upload(FileUploadParam param) {
        try {
            MultipartFile multipartFile = param.getFile();
            String url = AliOssUtil.uploadFile(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            return new BaseResult<String>().setResult(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ServiceException("上传文件失败");
    }

    @ApiOperation(value = "Excel导出", notes = "Excel文件导出下载")
    @ApiMapping(value = "backend.mdc.file.excel.download", method = RequestMethod.POST, permission = true)
    public BaseResult<String> downloadExcel(ExcelDownloadParam param) {
        return systemFileService.downloadExcel(param);
    }

    @ApiOperation(value = "查询Excel导出记录", notes = "查询Excel导出记录")
    @PreAuthorize("hasAuthority('mdc:system:excelList')")
    @ApiMapping(value = "backend.mdc.file.excel.page", method = RequestMethod.POST, permission = true)
    public IPage<SystemExportLog> pageExcelLog(ExcelPageParam param) {
        return systemFileService.pageExcelLog(param);
    }
}
