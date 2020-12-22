package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeAuditStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeReleaseStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeVisitEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramCodeMapper;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramTemplateConfigMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramCode;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramTemplateConfig;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniAccountResult;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniProgramCodeResult;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniProgramPageResult;
import com.aquilaflycloud.mdc.service.WechatMiniProgramCodeService;
import com.aquilaflycloud.util.AliOssUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.ma.WxMaOpenCommitExtInfo;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;
import me.chanjar.weixin.open.bean.message.WxOpenMaSubmitAuditMessage;
import me.chanjar.weixin.open.bean.result.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WechatMiniProgramCodeServiceImpl
 *
 * @author star
 * @date 2019-10-09
 */
@Service
public class WechatMiniProgramCodeServiceImpl implements WechatMiniProgramCodeService {
    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;
    @Resource
    private WechatMiniProgramCodeMapper wechatMiniProgramCodeMapper;
    @Resource
    private WechatMiniProgramTemplateConfigMapper wechatMiniProgramTemplateConfigMapper;
    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;

    @Override
    public IPage<WechatMiniProgramCode> pageCode(CodeListParam param) {
        return wechatMiniProgramCodeMapper.selectPage(param.page(), Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), WechatMiniProgramCode::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getCodeVersion()), WechatMiniProgramCode::getCodeVersion, param.getCodeVersion())
                .like(StrUtil.isNotBlank(param.getCodeDesc()), WechatMiniProgramCode::getCodeDesc, param.getCodeDesc())
                .eq(param.getTemplateType() != null, WechatMiniProgramCode::getTemplateType, param.getTemplateType())
                .eq(param.getAuditState() != null, WechatMiniProgramCode::getAuditState, param.getAuditState())
                .eq(param.getReleaseState() != null, WechatMiniProgramCode::getReleaseState, param.getReleaseState())
                .orderByDesc(WechatMiniProgramCode::getCreateTime)
        ).convert((code) -> {
            WechatMiniProgramCodeResult result = new WechatMiniProgramCodeResult();
            BeanUtil.copyProperties(code, result);
            result.setAppName(wechatOpenPlatformService.getWechatAuthorSiteByAppId(code.getAppId()).getNickName());
            return result;
        });
    }

    @Override
    public IPage<WechatMiniAccountResult> pageAccount(CodeAccountListParam param) {
        return wechatAuthorSiteMapper.selectPage(param.page(), Wrappers.<WechatAuthorSite>lambdaQuery()
                .eq(WechatAuthorSite::getSource, SiteSourceEnum.MINIPRO)
                .like(StrUtil.isNotBlank(param.getNickName()), WechatAuthorSite::getNickName, param.getNickName())
                .orderByDesc(WechatAuthorSite::getCreateTime)
        ).convert((authorSite) -> {
            WechatMiniAccountResult accountResult = new WechatMiniAccountResult();
            BeanUtil.copyProperties(authorSite, accountResult);
            WechatMiniProgramCode code = getDevCode(new CodeGetParam().setAppId(accountResult.getAppId()));
            if (code != null) {
                accountResult.setIsSubmit(WhetherEnum.YES);
                if (code.getReleaseState() != null) {
                    accountResult.setIsRelease(WhetherEnum.YES);
                }
            }
            return accountResult;
        });
    }

    @Override
    public WechatMiniProgramCode getDevCode(CodeGetParam param) {
        return wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(WechatMiniProgramCode::getAppId, param.getAppId())
                .ne(WechatMiniProgramCode::getReleaseState, CodeReleaseStateEnum.REVERT)
                .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                .orderByDesc(WechatMiniProgramCode::getCreateTime)
                .last("limit 1")
        );
    }

    @Override
    public WechatMiniProgramCode getAuditCode(CodeGetParam param) {
        return wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(WechatMiniProgramCode::getAppId, param.getAppId()).isNull(WechatMiniProgramCode::getReleaseState)
                .and(i -> i.eq(WechatMiniProgramCode::getAuditState, CodeAuditStateEnum.AUDITING)
                        .or().eq(WechatMiniProgramCode::getAuditState, CodeAuditStateEnum.SUCCESS))
                .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                .orderByDesc(WechatMiniProgramCode::getCreateTime)
                .last("limit 1")
        );
    }

    @Override
    public WechatMiniProgramCode getProdCode(CodeGetParam param) {
        return wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(WechatMiniProgramCode::getAppId, param.getAppId())
                .ne(WechatMiniProgramCode::getReleaseState, CodeReleaseStateEnum.REVERT)
                .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                .orderByDesc(WechatMiniProgramCode::getCreateTime)
                .last("limit 1")
        );
    }

    @Override
    @Transactional
    public void commit(CodeCommitParam param) {
        WechatMiniProgramTemplateConfig template = wechatMiniProgramTemplateConfigMapper.selectById(param.getTemplateConfigId());
        if (template == null) {
            throw new ServiceException("小程序代码模板配置id有误");
        }
        WxMaOpenCommitExtInfo extInfo = JSONUtil.toBean(template.getExtConfig(), WxMaOpenCommitExtInfo.class);
        try {
            WxOpenResult result = wechatOpenPlatformService.getWxOpenComponentService().getWxMaServiceByAppid(param.getAppId())
                    .codeCommit(template.getTemplateId(), template.getUserVersion(), template.getUserDesc(), extInfo);
            if (result.isSuccess()) {
                WechatMiniProgramCode code = wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                        .eq(WechatMiniProgramCode::getAppId, param.getAppId())
                        .isNull(WechatMiniProgramCode::getAuditState)
                        .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                        .last("limit 1")
                );
                boolean insert = true;
                if (code == null) {
                    code = new WechatMiniProgramCode();
                    code.setAppId(param.getAppId());
                } else {
                    insert = false;
                }
                code.setTemplateConfigId(template.getId());
                code.setTemplateConfigName(template.getTemplateConfigName());
                code.setTemplateId(template.getTemplateId());
                code.setTemplateType(template.getTemplateType());
                code.setSourceMiniProgramAppId(template.getSourceMiniProgramAppId());
                code.setSourceMiniProgram(template.getSourceMiniProgram());
                code.setUserVersion(template.getUserVersion());
                code.setUserDesc(template.getUserDesc());
                code.setDeveloper(template.getDeveloper());
                code.setDefaultPage(template.getDefaultPage());
                code.setPageConfig(template.getPageConfig());
                code.setExtConfig(template.getExtConfig());
                code.setCodeVersion(param.getCodeVersion());
                code.setCodeDesc(param.getCodeDesc());
                code.setSubmitTime(DateTime.now());
                int count;
                if (insert) {
                    count = wechatMiniProgramCodeMapper.insert(code);
                } else {
                    count = wechatMiniProgramCodeMapper.updateById(code);
                }
                if (count <= 0) {
                    throw new ServiceException("提交失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    private WechatMiniProgramCode getNotAuditCode(String appId) {
        return wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(WechatMiniProgramCode::getAppId, appId)
                .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                .orderByDesc(WechatMiniProgramCode::getCreateTime)
                .last("limit 1")
        );
    }

    private WechatMiniProgramCode getAuditingCode(String appId) {
        return wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                .eq(WechatMiniProgramCode::getAppId, appId)
                .eq(WechatMiniProgramCode::getAuditState, CodeAuditStateEnum.AUDITING)
                .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                .last("limit 1")
        );
    }

    @Override
    @Transactional
    public void add(CodeAddParam param) {
        WechatMiniProgramCode code = getNotAuditCode(param.getAppId());
        int count;
        if (code == null) {
            code = new WechatMiniProgramCode();
            code.setPageConfig("*");
            code.setAppId(param.getAppId());
            WechatMiniProgramCode preCode = getDevCode(new CodeGetParam().setAppId(param.getAppId()));
            if (preCode != null) {
                code.setDefaultPage(preCode.getDefaultPage());
            }
            code.setCodeVersion(param.getCodeVersion());
            code.setCodeDesc(param.getCodeDesc());
            code.setSubmitTime(DateTime.now());
            count = wechatMiniProgramCodeMapper.insert(code);
        } else {
            code.setPageConfig("*");
            code.setCodeVersion(param.getCodeVersion());
            code.setCodeDesc(param.getCodeDesc());
            code.setSubmitTime(DateTime.now());
            count = wechatMiniProgramCodeMapper.updateById(code);
        }
        if (count <= 0) {
            throw new ServiceException("创建失败");
        }
    }

    @Override
    @Transactional
    public String getQrcode(CodeQrCodeGetParam param) {
        WechatMiniProgramCode code = getDevCode(new CodeGetParam().setAppId(param.getAppId()));
        if (StrUtil.isNotBlank(code.getQrcodeUrl()) && StrUtil.isBlank(param.getPath())) {
            return code.getQrcodeUrl();
        }
        try {
            Map<String, String> paramMap = new HashMap<>();
            String paramStr = StrUtil.subAfter(param.getPath(), "?", true);
            for (String p : StrUtil.split(paramStr, "&")) {
                String[] pArray = StrUtil.split(p, "=");
                paramMap.put(pArray[0], pArray[1]);
            }
            File file = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).getTestQrcode(param.getPath(), paramMap);
            //上传文件至服务器并返回url
            try {
                String name = param.getAppId() + "_code_" + System.currentTimeMillis();
                String qrcodeUrl = AliOssUtil.uploadFile(StrUtil.appendIfMissing(name, ".png"), new FileInputStream(file));
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(code.getId());
                update.setQrcodeUrl(qrcodeUrl);
                update.setDefaultPage(param.getPath());
                wechatMiniProgramCodeMapper.updateById(code);
                return qrcodeUrl;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ServiceException("上传体验码失败");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxOpenMaCategory> getCategory(CodeGetParam param) {
        try {
            WxOpenMaCategoryListResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).getCategoryList();
            if (result.isSuccess()) {
                return result.getCategoryList();
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public WechatMiniProgramPageResult getPage(CodeGetParam param) {
        WechatMiniProgramCode code = getDevCode(new CodeGetParam().setAppId(param.getAppId()));
        try {
            WxOpenMaPageListResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).getPageList();
            if (result.isSuccess()) {
                WechatMiniProgramPageResult pageResult = new WechatMiniProgramPageResult();
                pageResult.setPageList(result.getPageList().stream().filter(page ->
                        code.getPageConfig().contains(page + ";") || "*".equals(code.getPageConfig())).collect(Collectors.toList()));
                pageResult.setNotPageList(result.getPageList().stream().filter(page ->
                        !code.getPageConfig().contains(page + ";") && !"*".equals(code.getPageConfig())).collect(Collectors.toList()));
                return pageResult;
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    @Transactional
    public void submit(CodeSubmitParam param) {
        WechatMiniProgramCode code;
        if (param.getId() != null) {
            code = wechatMiniProgramCodeMapper.selectById(param.getId());
        } else {
            code = getDevCode(new CodeGetParam().setAppId(param.getAppId()));
        }
        if (code.getAuditState() != null
                && (code.getAuditState() == CodeAuditStateEnum.AUDITING
                || code.getAuditState() == CodeAuditStateEnum.SUCCESS)) {
            throw new ServiceException("重复提审");
        }
        WxOpenMaSubmitAuditMessage submitAudit = new WxOpenMaSubmitAuditMessage();
        submitAudit.setItemList(param.getItemList());
        try {
            WxOpenMaSubmitAuditResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).submitAudit(submitAudit);
            if (result.isSuccess()) {
                result.getAuditId();
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(code.getId());
                update.setAuditId(result.getAuditId());
                update.setAuditContent(JSONUtil.toJsonStr(submitAudit));
                update.setAuditState(CodeAuditStateEnum.AUDITING);
                update.setSubmitAuditTime(DateTime.now());
                int count = wechatMiniProgramCodeMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("提交失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    @Transactional
    public void undo(CodeGetParam param) {
        //查询是否有审核中版本
        WechatMiniProgramCode code = getAuditingCode(param.getAppId());
        if (code == null) {
            throw new ServiceException("没有审核中版本");
        }
        try {
            WxOpenResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).undoCodeAudit();
            if (result.isSuccess()) {
                Long id = code.getId();
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(id);
                update.setAuditState(CodeAuditStateEnum.REVOKED);
                int count = wechatMiniProgramCodeMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("撤回失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    @Transactional
    public void updateAudit(String appId, Long auditTime, String reason, String screenShot, CodeAuditStateEnum status) {
        WechatMiniProgramCode code = getAuditingCode(appId);
        if (code != null) {
            Long id = code.getId();
            DateTime time = new DateTime(auditTime);
            WechatMiniProgramCode update = new WechatMiniProgramCode();
            update.setId(id);
            update.setSuccessAuditTime(time);
            update.setFailedReason(reason);
            update.setFailedScreenShot(screenShot);
            update.setAuditState(status);
            wechatMiniProgramCodeMapper.normalUpdateById(update);
        }
    }

    @Override
    @Transactional
    public void updateAudit(CodeAuditUpdateParam param) {
        WechatMiniProgramCode code = wechatMiniProgramCodeMapper.selectById(param.getId());
        try {
            WxOpenMaQueryAuditResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(code.getAppId()).getAuditStatus(code.getAuditId());
            if (result.isSuccess()) {
                int count = updateAudit(code.getId(), result.getStatus(), result.getReason(), result.getScreenShot(), DateTime.now());
                if (count <= 0) {
                    throw new ServiceException("更新失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    private int updateAudit(Long id, Integer status, String reason, String screenShot, DateTime time) {
        WechatMiniProgramCode update = new WechatMiniProgramCode();
        update.setId(id);
        CodeAuditStateEnum auditState;
        switch (status) {
            case 0:
                auditState = CodeAuditStateEnum.SUCCESS;
                update.setSuccessAuditTime(time);
                break;
            case 1:
                auditState = CodeAuditStateEnum.FAILED;
                update.setSuccessAuditTime(time);
                update.setFailedReason(reason);
                update.setFailedScreenShot(screenShot);
                break;
            case 2:
                auditState = CodeAuditStateEnum.AUDITING;
                break;
            default:
                auditState = CodeAuditStateEnum.REVOKED;
                break;
        }
        update.setAuditState(auditState);
        return wechatMiniProgramCodeMapper.updateById(update);
    }

    @Override
    @Transactional
    public void release(CodeGetParam param) {
        try {
            WxOpenResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).releaseAudited();
            if (result.isSuccess()) {
                WechatMiniProgramCode code = wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                        .eq(WechatMiniProgramCode::getAppId, param.getAppId()).isNull(WechatMiniProgramCode::getReleaseState)
                        .eq(WechatMiniProgramCode::getAuditState, CodeAuditStateEnum.SUCCESS)
                        .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                        .orderByDesc(WechatMiniProgramCode::getCreateTime)
                        .last("limit 1")
                );
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(code.getId());
                update.setReleaseState(CodeReleaseStateEnum.PUBLISH);
                update.setReleaseTime(DateTime.now());
                update.setIsVisit(CodeVisitEnum.OPEN);
                int count = wechatMiniProgramCodeMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("发布失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    @Transactional
    public void revert(CodeGetParam param) {
        try {
            WxOpenResult result = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMaServiceByAppid(param.getAppId()).revertCodeRelease();
            if (result.isSuccess()) {
                WechatMiniProgramCode code = getProdCode(new CodeGetParam().setAppId(param.getAppId()));
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(code.getId());
                update.setReleaseState(CodeReleaseStateEnum.REVERT);
                int count = wechatMiniProgramCodeMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("回退失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    @Transactional
    public void changeVisit(CodeVisitChangeParam param) {
        JSONObject params = new JSONObject();
        params.set("action", param.getAction().name().toLowerCase());
        try {
            String resultJson = wechatOpenPlatformService.getWxOpenComponentService().getWxMaServiceByAppid(param.getAppId()).post("https://api.weixin.qq.com/wxa/change_visitstatus", params.toString());
            WxOpenResult result = JSONUtil.toBean(resultJson, WxOpenResult.class);
            if (result.isSuccess()) {
                WechatMiniProgramCode code = wechatMiniProgramCodeMapper.selectOne(Wrappers.<WechatMiniProgramCode>lambdaQuery()
                        .eq(WechatMiniProgramCode::getAppId, param.getAppId())
                        .eq(WechatMiniProgramCode::getReleaseState, CodeReleaseStateEnum.PUBLISH)
                        .orderByAsc(WechatMiniProgramCode::getSubTenantId)
                        .orderByDesc(WechatMiniProgramCode::getCreateTime)
                        .last("limit 1")
                );
                WechatMiniProgramCode update = new WechatMiniProgramCode();
                update.setId(code.getId());
                update.setIsVisit(param.getAction());
                int count = wechatMiniProgramCodeMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("设置失败");
                }
            }
            throw new ServiceException(result.getErrmsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }
}
