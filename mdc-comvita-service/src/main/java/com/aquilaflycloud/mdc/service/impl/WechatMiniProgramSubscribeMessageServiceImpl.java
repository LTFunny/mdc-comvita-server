package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramMessageMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramMessage;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WechatMiniProgramSubscribeMessageServiceImpl
 *
 * @author star
 * @date 2020-03-03
 */
@Slf4j
@Service
public class WechatMiniProgramSubscribeMessageServiceImpl implements WechatMiniProgramSubscribeMessageService {
    @Resource
    private WechatMiniService wechatMiniService;
    @Resource
    private WechatMiniProgramMessageMapper wechatMiniProgramMessageMapper;

    @Override
    public List<WxMaSubscribeService.CategoryData> listCategory(WechatAuthorSiteGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
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
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
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
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
            return wxMaSubscribeService.getPubTemplateKeyWordsById(param.getTid());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public BaseResult<String> addTemplate(MiniTemplateAddParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
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
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
            return wxMaSubscribeService.getTemplateList();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public void deleteTemplate(MiniTemplateGetParam param) {
        try {
            WxMaSubscribeService wxMaSubscribeService = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getSubscribeService();
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
            //验证消息模板格式是否符合业务功能需求
            switch (param.getMessageType()) {
                /*case APPLYRECORDAUDIT: {
                    if (params.size() != 4) {
                        throw new ServiceException(MiniMessageTypeEnum.APPLYRECORDAUDIT.getName() + "消息模板字段需要4个");
                    }
                    break;
                }*/
                case PREORDERSIGN: {
                    if (params.size() != 5) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERSIGN.getName() + "消息模板字段需要5个");
                    }
                    break;
                }
                case PREORDERDELIVERY: {
                    if (params.size() != 5) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERDELIVERY.getName() + "消息模板字段需要5个");
                    }
                    break;
                }
                case PREORDERGOODSSIGN: {
                    if (params.size() != 4) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERGOODSSIGN.getName() + "消息模板字段需要4个");
                    }
                    break;
                }
                case PREORDERGOODSELIVERY: {
                    if (params.size() != 5) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERGOODSELIVERY.getName() + "消息模板字段需要5个");
                    }
                    break;
                }
                case PREORDERCHANGE: {
                    if (params.size() != 4) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERCHANGE.getName() + "消息模板字段需要4个");
                    }
                    break;
                }
                case PREORDERREFUNDAUDIT: {
                    if (params.size() != 4) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERREFUNDAUDIT.getName() + "消息模板字段需要4个");
                    }
                    break;
                }
                case PREORDERAUDIT: {
                    if (params.size() != 4) {
                        throw new ServiceException(MiniMessageTypeEnum.PREORDERAUDIT.getName() + "消息模板字段需要4个");
                    }
                    break;
                }
                default:
            }
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
        return wechatMiniProgramMessageMapper.selectList(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                .eq(WechatMiniProgramMessage::getAppId, param.getAppId())
                .in(CollUtil.isNotEmpty(param.getMessageTypeList()), WechatMiniProgramMessage::getMessageType, param.getMessageTypeList())
        );
    }

    @Override
    public List<String> listTmplId(MiniMessageTmplIdListParam param) {
        String appId = MdcUtil.getOtherAppId();
        return wechatMiniProgramMessageMapper.selectList(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                .eq(WechatMiniProgramMessage::getAppId, appId)
                .in(WechatMiniProgramMessage::getMessageType, param.getMessageTypeList())
        ).stream().map(WechatMiniProgramMessage::getPriTmplId).collect(Collectors.toList());
    }

    /**
     * 发送审核结果通知,小程序订阅消息
     *
     * @param miniMemberInfos 需要发送的小程序会员对象列表
     * @param messageType     发送订阅消息类型
     * @param id              业务数据id,消息需跳转小程序页面时传入
     * @param contents        发送字段对应内容
     */
    @Override
    public void sendMiniMessage(List<MiniMemberInfo> miniMemberInfos, MiniMessageTypeEnum messageType, Long id, String... contents) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            WechatMiniProgramMessage message = wechatMiniProgramMessageMapper.selectOne(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                    .eq(WechatMiniProgramMessage::getMessageType, messageType)
            );
            if (message != null) {
                Map<String, List<MiniMemberInfo>> miniMap = miniMemberInfos.stream().collect(Collectors.groupingBy(MiniMemberInfo::getAppId));
                for (Map.Entry<String, List<MiniMemberInfo>> entry : miniMap.entrySet()) {
                    //微信小程序报名才发送订阅消息
                    if (StrUtil.startWith(entry.getKey(), "wx")) {
                        WxMaMsgService wxMaMsgService = wechatMiniService.getWxMaServiceByAppId(entry.getKey()).getMsgService();
                        for (MiniMemberInfo miniMemberInfo : entry.getValue()) {
                            WxMaSubscribeMessage subscribeMessage = new WxMaSubscribeMessage();
                            subscribeMessage.setToUser(miniMemberInfo.getOpenId());
                            subscribeMessage.setMiniprogramState(message.getMiniState().name());
                            subscribeMessage.setTemplateId(message.getPriTmplId());
                            if (StrUtil.isNotBlank(message.getPagePath())) {
                                subscribeMessage.setPage(id != null ? message.getPagePath() + "?id=" + message.getPagePath() : message.getPagePath());
                            }
                            subscribeMessage.setLang(message.getMiniLang().name());
                            String[] paramName = message.getParamName().split(",");
                            for (int i = 0; i < paramName.length; i++) {
                                WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
                                String name = paramName[i];
                                data.setName(name);
                                String value = contents[i];
                                if (StrUtil.startWith(name, "thing")) {
                                    value = StrUtil.subPre(value, 20);
                                } else if (StrUtil.startWith(name, "number")) {
                                    value = StrUtil.subPre(value, 32);
                                } else if (StrUtil.startWith(name, "letter")) {
                                    value = StrUtil.subPre(value, 32);
                                } else if (StrUtil.startWith(name, "symbol")) {
                                    value = StrUtil.subPre(value, 5);
                                } else if (StrUtil.startWith(name, "character_string")) {
                                    value = StrUtil.subPre(value, 32);
                                } else if (StrUtil.startWith(name, "phone_number")) {
                                    value = StrUtil.subPre(value, 17);
                                } else if (StrUtil.startWith(name, "car_number")) {
                                    value = StrUtil.subPre(value, 8);
                                } else if (StrUtil.startWith(name, "phrase")) {
                                    value = StrUtil.subPre(value, 5);
                                }
                                data.setValue(value);
                                subscribeMessage.addData(data);
                            }
                            try {
                                wxMaMsgService.sendSubscribeMsg(subscribeMessage);
                            } catch (WxErrorException e) {
                                log.error("发送小程序订阅消息失败", e);
                            }
                        }
                    }
                }
            }
        });
    }
}
