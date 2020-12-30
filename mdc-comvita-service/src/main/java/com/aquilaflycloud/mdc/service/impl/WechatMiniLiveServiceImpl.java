package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaLiveResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveScreenTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveStatusEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.WechatMiniLiveInfoMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniLiveInfo;
import com.aquilaflycloud.mdc.param.wechat.WechatAuthorSiteGetParam;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniLiveGoodsResult;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniLiveInfoResult;
import com.aquilaflycloud.mdc.service.WechatMiniLiveService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * WechatMiniLiveServiceImpl
 *
 * @author star
 * @date 2020-04-27
 */
@Slf4j
@Service
public class WechatMiniLiveServiceImpl implements WechatMiniLiveService {
    @Resource
    private WechatMiniService wechatMiniService;
    @Resource
    private WechatMiniLiveInfoMapper wechatMiniLiveInfoMapper;

    @Transactional
    @Override
    public void loadLiveInfo(WechatAuthorSiteGetParam param) {
        wechatMiniLiveInfoMapper.delete(Wrappers.<WechatMiniLiveInfo>lambdaQuery()
                .eq(WechatMiniLiveInfo::getAppId, param.getAppId())
        );
        try {
            List<WxMaLiveResult.RoomInfo> roomInfoList = wechatMiniService.getWxMaServiceByAppId(param.getAppId())
                    .getLiveService().getLiveInfos();
            List<WechatMiniLiveInfo> liveInfoList = new ArrayList<>();
            for (WxMaLiveResult.RoomInfo roomInfo : roomInfoList) {
                WechatMiniLiveInfo liveInfo = new WechatMiniLiveInfo();
                BeanUtil.copyProperties(roomInfo, liveInfo, CopyOptions.create().ignoreError());
                liveInfo.setAppId(param.getAppId());
                liveInfo.setStartTime(DateUtil.date(roomInfo.getStartTime() * 1000));
                liveInfo.setEndTime(DateUtil.date(roomInfo.getEndTime() * 1000));
                liveInfo.setLiveStatus(EnumUtil.likeValueOf(LiveStatusEnum.class, roomInfo.getLiveStatus()));
                liveInfo.setType(EnumUtil.likeValueOf(LiveTypeEnum.class, roomInfo.getType()));
                liveInfo.setScreenType(EnumUtil.likeValueOf(LiveScreenTypeEnum.class, roomInfo.getScreenType()));
                liveInfo.setCloseLike(EnumUtil.likeValueOf(WhetherEnum.class, roomInfo.getCloseLike()));
                liveInfo.setCloseGoods(EnumUtil.likeValueOf(WhetherEnum.class, roomInfo.getCloseGoods()));
                liveInfo.setCloseComment(EnumUtil.likeValueOf(WhetherEnum.class, roomInfo.getCloseComment()));
                liveInfo.setGoodsContent(JSONUtil.toJsonStr(roomInfo.getGoods()));
                liveInfoList.add(liveInfo);
            }
            wechatMiniLiveInfoMapper.insertAllBatch(liveInfoList);
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 1000) {
                throw new ServiceException("直播间已关闭");
            }
            log.error("获取直播间列表报错", e);
        }
    }

    private WechatMiniLiveInfo stateHandler(WechatMiniLiveInfo wechatMiniLiveInfo) {
        DateTime now = DateTime.now();
        if (wechatMiniLiveInfo.getLiveStatus().getType() < 104) {
            if (now.isAfterOrEquals(wechatMiniLiveInfo.getStartTime()) && now.isBeforeOrEquals(wechatMiniLiveInfo.getEndTime())) {
                wechatMiniLiveInfo.setLiveStatus(LiveStatusEnum.LIVING);
            } else if (now.isBefore(wechatMiniLiveInfo.getStartTime())) {
                wechatMiniLiveInfo.setLiveStatus(LiveStatusEnum.UNSTART);
            } else if (now.isAfter(wechatMiniLiveInfo.getEndTime())) {
                wechatMiniLiveInfo.setLiveStatus(LiveStatusEnum.ENDED);
            }
        }
        return wechatMiniLiveInfo;
    }

    @Override
    public IPage<WechatMiniLiveInfoResult> pageLive(PageParam<WechatMiniLiveInfo> param) {
        String appId = MdcUtil.getOtherAppId();
        return wechatMiniLiveInfoMapper.selectPage(param.page(), Wrappers.<WechatMiniLiveInfo>lambdaQuery()
                .eq(WechatMiniLiveInfo::getAppId, appId)
        ).convert(liveInfo -> {
            liveInfo = stateHandler(liveInfo);
            WechatMiniLiveInfoResult result = BeanUtil.copyProperties(liveInfo, WechatMiniLiveInfoResult.class);
            List<WechatMiniLiveGoodsResult> goods = JSONUtil.toList(JSONUtil.parseArray(result.getGoodsContent()), WechatMiniLiveGoodsResult.class);
            result.setGoods(goods);
            return result;
        });
    }
}
