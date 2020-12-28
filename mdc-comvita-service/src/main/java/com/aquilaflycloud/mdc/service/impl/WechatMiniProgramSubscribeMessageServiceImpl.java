package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.util.WechatFactoryUtil;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramMessageMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramMessage;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WechatMiniProgramSubscribeMessageServiceImpl
 *
 * @author star
 * @date 2020-03-03
 */
@Service
public class WechatMiniProgramSubscribeMessageServiceImpl implements WechatMiniProgramSubscribeMessageService {
    @Resource
    private WechatMiniProgramMessageMapper wechatMiniProgramMessageMapper;

    @Override
    public List<WxMaSubscribeService.CategoryData> listCategory(WechatAuthorSiteGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            return wxMaSubscribeService.getCategory();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public IPage<WxMaPubTemplateTitleListResult.TemplateItem> pagePubTemplateTitle(MiniPubTemplateTitlePageParam param) {
        try {
            int start = Convert.toInt(param.page().offset());
            int limit = Convert.toInt(param.getPageSize());
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            WxMaPubTemplateTitleListResult result = wxMaSubscribeService.getPubTemplateTitleList(param.getIds(), start, limit);
            IPage<WxMaPubTemplateTitleListResult.TemplateItem> page = new Page<>(param.getPageNum(), param.getPageSize(), result.getCount());
            page.setRecords(result.getData());
            return page;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxMaSubscribeService.PubTemplateKeyword> listPubTemplateKeyword(MiniPubTemplateKeywordGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            return wxMaSubscribeService.getPubTemplateKeyWordsById(param.getTid());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public BaseResult<String> addTemplate(MiniTemplateAddParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            String priTmplId = wxMaSubscribeService.addTemplate(param.getTid(), param.getKidList(), param.getSceneDesc());
            return new BaseResult<String>().setResult(priTmplId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxMaSubscribeService.TemplateInfo> listTemplate(WechatAuthorSiteGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            return wxMaSubscribeService.getTemplateList();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public void deleteTemplate(MiniTemplateGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = WechatFactoryUtil.getService(param.getAppId(), "getSubscribeService", "18");
            boolean flag = wxMaSubscribeService.delTemplate(param.getPriTmplId());
            if (!flag) {
                throw new ServiceException("删除失败");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public void saveMessage(MiniMessageSaveParam param) {
        WechatMiniProgramMessage message = wechatMiniProgramMessageMapper.selectOne(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                .eq(WechatMiniProgramMessage::getMessageType, param.getMessageType())
        );
        int count;
        String content = param.getContent();
        List<String> params = null;
        if (StrUtil.isNotBlank(content)) {
            params = ReUtil.findAll("(?<=\\{\\{)[^}]*(?=\\.)", content, 0);
        }
        //验证消息模板格式是否符合业务功能需求
        switch (param.getMessageType()) {
            case APPLYRECORDAUDIT: {
                if (params.size() != 4) {
                    throw new ServiceException(MiniMessageTypeEnum.APPLYRECORDAUDIT.getName() + "消息模板字段需要4个");
                }
                break;
            }
            default:
        }
        if (message == null) {
            message = new WechatMiniProgramMessage();
            BeanUtil.copyProperties(param, message);
            if (params != null) {
                message.setParamName(CollUtil.join(params, ","));
            }
            count = wechatMiniProgramMessageMapper.insert(message);
        } else {
            WechatMiniProgramMessage update = new WechatMiniProgramMessage();
            BeanUtil.copyProperties(param, update);
            update.setId(message.getId());
            if (params != null) {
                message.setParamName(CollUtil.join(params, ","));
            }
            count = wechatMiniProgramMessageMapper.updateById(update);
        }
        if (count <= 0) {
            throw new ServiceException("保存消息模板失败");
        }
    }

    @Override
    public void editMessage(MiniMessageEditParam param) {
        WechatMiniProgramMessage update = new WechatMiniProgramMessage();
        BeanUtil.copyProperties(param, update);
        int count = wechatMiniProgramMessageMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑消息模板失败");
        }
    }

    @Override
    public void toggleMessage(MiniMessageGetParam param) {
        WechatMiniProgramMessage message = wechatMiniProgramMessageMapper.selectById(param.getId());
        WechatMiniProgramMessage update = new WechatMiniProgramMessage();
        update.setId(message.getId());
        update.setState(message.getState() == StateEnum.NORMAL ? StateEnum.DISABLE : StateEnum.NORMAL);
        int count = wechatMiniProgramMessageMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public WechatMiniProgramMessage getMessage(MiniMessageGetParam param) {
        return wechatMiniProgramMessageMapper.selectById(param.getId());
    }

    @Override
    public List<WechatMiniProgramMessage> listMessage(MiniMessageListParam param) {
        if (CollUtil.isNotEmpty(param.getMessageTypeList())) {
            return wechatMiniProgramMessageMapper.selectList(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                    .eq(WechatMiniProgramMessage::getAppId, param.getAppId())
                    .in(WechatMiniProgramMessage::getMessageType, param.getMessageTypeList())
            );
        }
        return CollUtil.newArrayList();
    }

    @Override
    public List<String> listTmplId(MiniMessageTmplIdListParam param) {
        String appId = MdcUtil.getOtherAppId();
        return wechatMiniProgramMessageMapper.selectList(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                .eq(WechatMiniProgramMessage::getAppId, appId)
                .in(WechatMiniProgramMessage::getMessageType, param.getMessageType())
        ).stream().map(WechatMiniProgramMessage::getPriTmplId).collect(Collectors.toList());
    }
}
