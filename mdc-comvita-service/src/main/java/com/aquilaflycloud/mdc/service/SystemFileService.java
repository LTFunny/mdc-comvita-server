package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.system.SystemExportLog;
import com.aquilaflycloud.mdc.param.system.ClientFileUploadParam;
import com.aquilaflycloud.mdc.param.system.ExcelDownloadParam;
import com.aquilaflycloud.mdc.param.system.ExcelPageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * SystemFileService
 *
 * @author star
 * @date 2019-12-09
 */
public interface SystemFileService {
    BaseResult<String> downloadExcel(ExcelDownloadParam param);

    IPage<SystemExportLog> pageExcelLog(ExcelPageParam param);

    BaseResult<String> uploadFile(ClientFileUploadParam param);
}

