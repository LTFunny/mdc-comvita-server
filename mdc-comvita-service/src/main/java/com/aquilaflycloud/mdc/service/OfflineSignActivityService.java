package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import com.aquilaflycloud.mdc.param.sign.*;
import com.aquilaflycloud.mdc.result.sign.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * OfflineSignActivityService
 *
 * @author star
 * @date 2020-05-07
 */
public interface OfflineSignActivityService {
    IPage<OfflineSignActivity> pageSign(SignPageParam param);

    SignResult getSign(SignGetParam param);

    void addSign(SignAddParam param);

    void editSign(SignEditParam param);

    void toggleState(SignGetParam param);

    IPage<OfflineSignRecordResult> pageRecord(RecordPageParam param);

    OfflineSignRewardResult addOfflineSignRecord(SignGetParam param);

    IPage<OfflineSignActivity> pageOfflineSign(PageParam<OfflineSignActivity> param);

    OfflineSignResult getOfflineSign(SignGetParam param);

    IPage<RecordResult> pageOfflineSignRecord(SignRecordPageParam param);

    IPage<SignRecordResult> pageOfflineSignMemberRecord(PageParam<OfflineSignMemberRecord> param);
}
