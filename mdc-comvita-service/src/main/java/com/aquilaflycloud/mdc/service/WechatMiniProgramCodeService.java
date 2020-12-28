package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.enums.wechat.CodeAuditStateEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramCode;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniProgramPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;

import java.util.List;

/**
 * WechatMiniProgramCodeService
 *
 * @author star
 * @date 2019-10-09
 */
public interface WechatMiniProgramCodeService {

    IPage<WechatMiniProgramCode> pageCode(CodeListParam param);

    IPage pageAccount(CodeAccountListParam param);

    WechatMiniProgramCode getDevCode(CodeGetParam param);

    WechatMiniProgramCode getAuditCode(CodeGetParam param);

    WechatMiniProgramCode getProdCode(CodeGetParam param);

    void commit(CodeCommitParam param);

    void add(CodeAddParam param);

    String getQrcode(CodeQrCodeGetParam param);

    List<WxOpenMaCategory> getCategory(CodeGetParam param);

    WechatMiniProgramPageResult getPage(CodeGetParam param);

    void submit(CodeSubmitParam param);

    void undo(CodeGetParam param);

    void updateAudit(String appId, Long auditTime, String reason, String screenShot, CodeAuditStateEnum status);

    void updateAudit(CodeAuditUpdateParam param);

    void release(CodeGetParam param);

    void revert(CodeGetParam param);

    void changeVisit(CodeVisitChangeParam param);
}

