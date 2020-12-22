package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import com.aquilaflycloud.mdc.param.apply.*;
import com.aquilaflycloud.mdc.result.apply.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * ApplyActivityService
 *
 * @author star
 * @date 2020-02-27
 */
public interface ApplyActivityService {
    IPage<ApplyResult> pageApply(ApplyPageParam param);

    ApplyDetailResult getApply(ApplyGetParam param);

    void addApply(ApplyAddParam param);

    void editApply(ApplyEditParam param);

    void toggleTop(ApplyGetParam param);

    void toggleState(ApplyGetParam param);

    StatisticsResult getStatistics();

    void auditRecord(RecordAuditParam param);

    void batchAuditRecord(RecordBatchAuditParam param);

    IPage<ApplyMemberRecord> pageRecord(RecordPageParam param);

    IPage<ApplyResult> pageApplyActivity(ApplyPageParam param);

    ApplyActivityDetailResult getApplyActivity(ApplyGetParam param);

    void addApplyRecord(RecordAddParam param);

    IPage<ApplyMemberRecord> pageApplyRecord(ApplyRecordPageParam param);

    IPage<ApplyMemberRecordResult> pageApplyMemberRecord(PageParam<ApplyMemberRecord> param);
}
