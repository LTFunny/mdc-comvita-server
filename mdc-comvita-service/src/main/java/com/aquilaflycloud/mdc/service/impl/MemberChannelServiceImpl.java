package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.aliyun.oss.model.OSSObject;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.MemberInfoMapper;
import com.aquilaflycloud.mdc.mapper.MemberRegisterChannelMapper;
import com.aquilaflycloud.mdc.mapper.MemberRegisterChannelRelMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberRegisterChannel;
import com.aquilaflycloud.mdc.model.member.MemberRegisterChannelRel;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.RegisterChannelAnalysisResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelStatisticsResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelTopResult;
import com.aquilaflycloud.mdc.service.MemberChannelService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.result.OssResult;
import com.aquilaflycloud.util.AliOssUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MemberChannelServiceImpl
 *
 * @author star
 * @date 2020-02-19
 */
@Slf4j
@Service
public class MemberChannelServiceImpl implements MemberChannelService {
    @Resource
    private MemberRegisterChannelMapper memberRegisterChannelMapper;
    @Resource
    private MemberRegisterChannelRelMapper memberRegisterChannelRelMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private WechatMiniService wechatMiniService;

    @Override
    public IPage<RegisterChannelResult> pageRegisterChannel(RegisterChannelPageParam param) {
        return memberRegisterChannelMapper.selectPage(param.page(), Wrappers.<MemberRegisterChannel>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRegisterChannel::getAppId, param.getAppId())
                .eq(StrUtil.isNotBlank(param.getChannelCode()), MemberRegisterChannel::getChannelCode, param.getChannelCode())
                .like(StrUtil.isNotBlank(param.getChannelName()), MemberRegisterChannel::getChannelName, param.getChannelName())
                .eq(param.getState() != null, MemberRegisterChannel::getState, param.getState())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), MemberRegisterChannel::getDesignateOrgNames, param.getDesignateOrgNames())
                .ge(param.getCreateTimeStart() != null, MemberRegisterChannel::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, MemberRegisterChannel::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(MemberRegisterChannel::getCreateTime)
        ).convert(channel -> {
            RegisterChannelResult result = new RegisterChannelResult();
            BeanUtil.copyProperties(channel, result);
            int count = memberRegisterChannelRelMapper.selectCount(Wrappers.<MemberRegisterChannelRel>lambdaQuery()
                    .eq(MemberRegisterChannelRel::getChannelId, channel.getId())
                    .ge(param.getCreateTimeStart() != null, MemberRegisterChannelRel::getCreateTime, param.getRelCreateTimeStart())
                    .le(param.getCreateTimeEnd() != null, MemberRegisterChannelRel::getCreateTime, param.getRelCreateTimeEnd())
            );
            result.setMemberCount(count);
            return result;
        });
    }

    @Transactional
    @Override
    public void addRegisterChannel(RegisterChannelAddParam param) {
        MemberRegisterChannel channel = new MemberRegisterChannel();
        BeanUtil.copyProperties(param, channel);
        MdcUtil.setOrganInfo(channel);
        channel.setChannelCode(MdcUtil.getTenantIncIdStr("registerChannelCode", "", 4));
        int count = memberRegisterChannelMapper.insert(channel);
        if (count <= 0) {
            throw new ServiceException("新增渠道失败");
        }
        createMiniCode(channel);
    }

    @Transactional
    @Override
    public void batchAddRegisterChannel(RegisterChannelBatchAddParam param) {
        List<MemberRegisterChannel> channelList = new ArrayList<>();
        for (RegisterChannelBatchAddParam.Channel channel : param.getChannelList()) {
            MemberRegisterChannel registerChannel = new MemberRegisterChannel();
            BeanUtil.copyProperties(channel, registerChannel);
            registerChannel.setChannelCode(MdcUtil.getTenantIncIdStr("registerChannelCode", "", 4));
            registerChannel.setAppId(param.getAppId());
            registerChannel.setPagePath(param.getPagePath());
            MdcUtil.setOrganInfo(registerChannel);
            channelList.add(registerChannel);
        }
        int count = 0;
        if (CollUtil.isNotEmpty(channelList)) {
            count = memberRegisterChannelMapper.insertAllBatch(channelList);
        }
        if (count <= 0) {
            throw new ServiceException("批量保存渠道失败");
        }
        createMiniCode(channelList.toArray(new MemberRegisterChannel[]{}));
    }

    private void createMiniCode(MemberRegisterChannel... channelList) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            for (MemberRegisterChannel channel : channelList) {
                if (StrUtil.isBlank(channel.getMiniCodeUrl())) {
                    try {
                        File file = wechatMiniService.getWxMaServiceByAppId(channel.getAppId())
                                .getQrcodeService().createWxaCodeUnlimit(Convert.toStr(channel.getId()), channel.getPagePath(),
                                        430, false, null, true);
                        String path = channel.getAppId() + "/" + channel.getPagePath().replace("/", ".");
                        OssResult ossResult = AliOssUtil.uploadFileReturn(path, StrUtil.appendIfMissing(channel.getChannelName() + "_" + DateTime.now().getTime(),
                                ".png"), new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
                        MemberRegisterChannel update = new MemberRegisterChannel();
                        update.setMiniCodeFileKey(ossResult.getObjectKey());
                        update.setMiniCodeUrl(ossResult.getUrl());
                        update.setId(channel.getId());
                        memberRegisterChannelMapper.updateById(update);
                    } catch (WxErrorException | FileNotFoundException e) {
                        log.error("创建小程序码失败", e);
                    }
                }
            }
        });
    }

    @Override
    public void editRegisterChannel(RegisterChannelEditParam param) {
        MemberRegisterChannel update = new MemberRegisterChannel();
        BeanUtil.copyProperties(param, update);
        MdcUtil.setOrganInfo(update);
        int count = memberRegisterChannelMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑渠道失败");
        }
    }

    @Override
    public void toggleRegisterChannel(RegisterChannelGetParam param) {
        MemberRegisterChannel channel = memberRegisterChannelMapper.selectById(param.getId());
        MemberRegisterChannel update = new MemberRegisterChannel();
        update.setId(channel.getId());
        if (channel.getState() == StateEnum.NORMAL) {
            update.setState(StateEnum.DISABLE);
        } else if (channel.getState() == StateEnum.DISABLE) {
            update.setState(StateEnum.NORMAL);
        }
        int count = memberRegisterChannelMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("更改渠道状态失败");
        }
    }

    @Override
    public BaseResult<String> downloadChannelMiniCode(RegisterChannelBatchGetParam param) {
        if (CollUtil.isEmpty(param.getIdList())) {
            throw new ServiceException("渠道id列表为空");
        }
        List<MemberRegisterChannel> channelList = memberRegisterChannelMapper.selectList(Wrappers.<MemberRegisterChannel>lambdaQuery()
                .in(MemberRegisterChannel::getId, param.getIdList())
        );
        List<InputStream> isList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        for (MemberRegisterChannel channel : channelList) {
            OSSObject ossObject = AliOssUtil.getObject(channel.getMiniCodeFileKey());
            isList.add(ossObject.getObjectContent());
            pathList.add(StrUtil.join("-", channel.getChannelCode(), channel.getChannelPosition(), channel.getChannelName()) + ".png");
        }
        try {
            File tmpDirFile = Files.createTempDirectory("channel-codes-temp").toFile();
            File resultFile = File.createTempFile("codes", ".zip", tmpDirFile);
            File zipFile = ZipUtil.zip(resultFile, pathList.toArray(new String[0]), isList.toArray(new InputStream[0]));
            String url = AliOssUtil.uploadFile("channelCodeZip.zip", new FileInputStream(zipFile));
            return new BaseResult<String>().setResult(url);
        } catch (IOException e) {
            log.error("压缩上传oss失败", e);
            throw new ServiceException("批量下载小程序码失败");
        }
    }

    @Override
    public RegisterChannelStatisticsResult getChannelStatistics(RegisterChannelTimeGetParam param) {
        RegisterChannelStatisticsResult result = new RegisterChannelStatisticsResult();
        Integer totalCount = memberInfoMapper.selectCount(Wrappers.<MemberInfo>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberInfo::getWxAppId, param.getAppId())
                .ge(MemberInfo::getCreateTime, param.getCreateTimeStart())
                .le(MemberInfo::getCreateTime, param.getCreateTimeEnd())
        );
        Integer channelCount = memberRegisterChannelRelMapper.selectCount(Wrappers.<MemberRegisterChannelRel>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRegisterChannelRel::getAppId, param.getAppId())
                .ge(MemberRegisterChannelRel::getCreateTime, param.getCreateTimeStart())
                .le(MemberRegisterChannelRel::getCreateTime, param.getCreateTimeEnd())
        );
        result.setTotalCount(totalCount);
        result.setChannelCount(channelCount);
        String percent = "0%";
        if (totalCount > 0) {
            percent = NumberUtil.formatPercent(NumberUtil.div(channelCount, totalCount).doubleValue(), 2);
        }
        result.setPercent(percent);
        return result;
    }

    @Override
    public List<RegisterChannelAnalysisResult> getChannelAnalysis(RegisterChannelTimeGetParam param) {
        Map<String, RegisterChannelAnalysisResult> analysis = memberRegisterChannelRelMapper.selectMaps(new QueryWrapper<MemberRegisterChannelRel>()
                .select("date_format(create_time,'%Y-%m-%d') channel_date, count(1) member_count")
                .groupBy("channel_date")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRegisterChannelRel::getAppId, param.getAppId())
                .ge(MemberRegisterChannelRel::getCreateTime, param.getCreateTimeStart())
                .lt(MemberRegisterChannelRel::getCreateTime, param.getCreateTimeEnd()))
                .stream().map((map) -> BeanUtil.fillBeanWithMap(map, new RegisterChannelAnalysisResult(), true,
                        CopyOptions.create().ignoreError()))
                .collect(Collectors.toMap(RegisterChannelAnalysisResult::getChannelDate, result -> result));
        return DateUtil.rangeToList(param.getCreateTimeStart(), param.getCreateTimeEnd(), DateField.DAY_OF_YEAR).stream().map(dateTime -> {
            RegisterChannelAnalysisResult result = analysis.get(dateTime.toDateStr());
            if (result == null) {
                result = new RegisterChannelAnalysisResult();
                result.setChannelDate(dateTime.toDateStr());
                result.setMemberCount(0);
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RegisterChannelTopResult> listChannelTop(RegisterChannelTopListParam param) {
        return memberRegisterChannelRelMapper.selectMaps(new QueryWrapper<MemberRegisterChannelRel>()
                .select("channel_id, count(1) member_count")
                .groupBy("channel_id")
                .orderByDesc("member_count")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRegisterChannelRel::getAppId, param.getAppId())
                .last("limit " + param.getLimit())
        ).stream().map(map -> {
            RegisterChannelTopResult result = new RegisterChannelTopResult();
            BeanUtil.fillBeanWithMap(map, result, true, CopyOptions.create().ignoreError());
            MemberRegisterChannel channel = memberRegisterChannelMapper.selectById(result.getChannelId());
            if (channel != null) {
                result.setChannelName(channel.getChannelName());
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public void addChannelRel(RegisterChannelGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        MemberRegisterChannel channel = memberRegisterChannelMapper.selectById(param.getId());
        if (channel != null && channel.getState() == StateEnum.NORMAL) {
            MemberRegisterChannelRel rel = new MemberRegisterChannelRel();
            rel.setAppId(channel.getAppId());
            rel.setChannelId(param.getId());
            rel.setMemberId(memberId);
            int count = memberRegisterChannelRelMapper.insert(rel);
            if (count <= 0) {
                throw new ServiceException("关联失败");
            }
        } else {
            throw new ServiceException("渠道不可用");
        }
    }
}
