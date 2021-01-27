package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.WechatMiniQrcodeInfoMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniQrcodeInfo;
import com.aquilaflycloud.mdc.param.wechat.MiniQrcodeGetParam;
import com.aquilaflycloud.mdc.service.WechatMiniFunctionService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.AliOssUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * WechatMiniFunctionServiceImpl
 *
 * @author star
 * @date 2021/1/9
 */
@Slf4j
@Service
public class WechatMiniFunctionServiceImpl implements WechatMiniFunctionService {
    @Resource
    private WechatMiniService wechatMiniService;
    @Resource
    private WechatMiniQrcodeInfoMapper wechatMiniQrcodeInfoMapper;

    @Override
    public BaseResult<String> getMiniQrcode(MiniQrcodeGetParam param) {
        String lockName = SecureUtil.md5(param.getAppId() + param.getPath() + param.getWidth());
        WechatMiniQrcodeInfo wechatMiniQrcodeInfo = RedisUtil.syncLoad(lockName, () -> {
            WechatMiniQrcodeInfo qrcodeInfo = wechatMiniQrcodeInfoMapper.selectOne(Wrappers.<WechatMiniQrcodeInfo>lambdaQuery()
                    .eq(WechatMiniQrcodeInfo::getAppId, param.getAppId())
                    .eq(WechatMiniQrcodeInfo::getPath, param.getPath())
                    .eq(WechatMiniQrcodeInfo::getWidth, param.getWidth())
            );
            if (qrcodeInfo == null) {
                try {
                    File file = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getQrcodeService().createQrcode(param.getPath(), param.getWidth());
                    //上传文件至服务器并返回url
                    try {
                        String path = param.getAppId() + "/" + StrUtil.split(param.getPath(), "?")[0].replace("/", ".");
                        String name = MdcUtil.getSnowflakeIdStr();
                        String url = AliOssUtil.uploadFile(path, StrUtil.appendIfMissing(name, ".png"), new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
                        qrcodeInfo = new WechatMiniQrcodeInfo();
                        BeanUtil.copyProperties(param, qrcodeInfo);
                        qrcodeInfo.setUrl(url);
                        wechatMiniQrcodeInfoMapper.insert(qrcodeInfo);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        throw new ServiceException("上传小程序二维码失败");
                    }
                } catch (WxErrorException e) {
                    e.printStackTrace();
                    throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
                }
            }
            return qrcodeInfo;
        });
        return BaseResult.buildResult(wechatMiniQrcodeInfo.getUrl());
    }
}
