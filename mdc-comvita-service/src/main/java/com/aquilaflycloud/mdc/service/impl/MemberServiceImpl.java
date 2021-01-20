package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.VerificateStateEnum;
import com.aquilaflycloud.mdc.enums.member.*;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.message.MemberErrorEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.member.*;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatFansInfo;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramDeviceInfo;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.*;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.result.system.UnifiedMemberConfig;
import com.aquilaflycloud.mdc.service.*;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.mdc.util.PoiUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.redisson.api.RMapCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toMap;

/**
 * MemberServiceImpl
 *
 * @author star
 * @date 2019-10-28
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private MemberUnifiedInfoMapper memberUnifiedInfoMapper;
    @Resource
    private WechatFansInfoMapper wechatFansInfoMapper;
    @Resource
    private MemberFaceMapper memberFaceMapper;
    @Resource
    private MemberExtraInfoMapper memberExtraInfoMapper;
    @Resource
    private WechatMiniProgramDeviceInfoMapper wechatMiniProgramDeviceInfoMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private MemberGradeService memberGradeService;
    @Resource
    private WechatMiniService wechatMiniService;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private SystemTenantConfigService systemTenantConfigService;
    @Resource
    private FolksonomyService folksonomyService;

    //======controller=======//
    @Override
    public IPage<MemberInfo> pageMemberInfo(MemberPageParam param) {
        String appId = param.getAppId();
        String wxAppId = null, aliAppId = null;
        if (StrUtil.isNotBlank(appId)) {
            if (StrUtil.startWith(appId, "wx")) {
                wxAppId = appId;
            } else {
                aliAppId = appId;
            }
        }
        return memberInfoMapper.selectPage(param.page(), Wrappers.<MemberInfo>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getMemberCode()), MemberInfo::getMemberCode, param.getMemberCode())
                .isNull(param.getHasPhoneNumber() == WhetherEnum.NO, MemberInfo::getPhoneNumber)
                .isNotNull(param.getHasPhoneNumber() == WhetherEnum.YES, MemberInfo::getPhoneNumber)
                .likeRight(StrUtil.isNotBlank(param.getPhoneNumber()), MemberInfo::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberInfo::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getRealName()), MemberInfo::getRealName, param.getRealName())
                .eq(param.getSex() != null, MemberInfo::getSex, param.getSex())
                .eq(StrUtil.isNotBlank(wxAppId), MemberInfo::getWxAppId, wxAppId)
                .eq(StrUtil.isNotBlank(aliAppId), MemberInfo::getAliAppId, aliAppId)
                .eq(StrUtil.isNotBlank(param.getCountry()), MemberInfo::getCountry, param.getCountry())
                .eq(StrUtil.isNotBlank(param.getProvince()), MemberInfo::getProvince, param.getProvince())
                .eq(StrUtil.isNotBlank(param.getCity()), MemberInfo::getCity, param.getCity())
                .eq(StrUtil.isNotBlank(param.getCounty()), MemberInfo::getCounty, param.getCounty())
                .eq(StrUtil.isNotBlank(param.getAddress()), MemberInfo::getAddress, param.getAddress())
                .like(StrUtil.isNotBlank(param.getCountryLike()), MemberInfo::getCountry, param.getCountryLike())
                .like(StrUtil.isNotBlank(param.getProvinceLike()), MemberInfo::getProvince, param.getProvinceLike())
                .like(StrUtil.isNotBlank(param.getCityLike()), MemberInfo::getCity, param.getCityLike())
                .like(StrUtil.isNotBlank(param.getCountyLike()), MemberInfo::getCounty, param.getCountyLike())
                .like(StrUtil.isNotBlank(param.getAddressLike()), MemberInfo::getAddress, param.getAddressLike())
                .ge(param.getBirthdayStart() != null, MemberInfo::getBirthday, param.getBirthdayStart())
                .le(param.getBirthdayEnd() != null, MemberInfo::getBirthday, param.getBirthdayEnd())
                .eq(param.getIsAuth() != null, MemberInfo::getIsAuth, param.getIsAuth())
                .eq(param.getIsChina() != null && param.getIsChina() == WhetherEnum.YES, MemberInfo::getCountry, "中国")
                .ne(param.getIsChina() != null && param.getIsChina() == WhetherEnum.NO, MemberInfo::getCountry, "中国")
                .eq(param.getSource() != null, MemberInfo::getSource, param.getSource())
                .eq(param.getSubSource() != null, MemberInfo::getSubSource, param.getSubSource())
                .ge(param.getCreateTimeStart() != null, MemberInfo::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, MemberInfo::getCreateTime, param.getCreateTimeEnd())
                .le(param.getCreateTimeEnd() == null, MemberInfo::getCreateTime, DateTime.now())
                .orderByDesc(MemberInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public MemberDetailResult getMemberDetailInfo(MemberGetParam param) {
        MemberInfo memberInfo = memberInfoMapper.selectById(param.getMemberId());
        if (memberInfo == null) {
            throw MemberErrorEnum.MEMBER_ERROR_10002.getErrorMeta().getException();
        }
        MemberDetailResult result = BeanUtil.copyProperties(memberInfo, MemberDetailResult.class);
        WechatAuthorSite site = wechatMiniService.getWxAuthorByAppId(memberInfo.getWxAppId());
        result.setAppName(site != null ? site.getNickName() : null);
        WechatMiniProgramDeviceInfo deviceInfo = wechatMiniProgramDeviceInfoMapper.selectOne(Wrappers.<WechatMiniProgramDeviceInfo>lambdaQuery()
                .eq(WechatMiniProgramDeviceInfo::getMemberId, memberInfo.getId())
        );
        result.setModel(deviceInfo != null ? deviceInfo.getModel() : null);
        Integer growth = memberRewardService.getMemberTotalReward(memberInfo.getId(), RewardTypeEnum.GROWTH);
        result.setGrowthValue(growth);
        result.setScoreValue(memberRewardService.getMemberTotalReward(memberInfo.getId(), RewardTypeEnum.SCORE));
        MemberGrade grade = memberGradeService.getRewardGrade(memberInfo.getWxAppId(), RewardTypeEnum.GROWTH, growth);
        result.setGradeTitle(grade != null ? grade.getGradeTitle() : null);
        result.setFolksonomyInfoList(folksonomyService.listMemberRelFolksonomy(null, memberInfo.getId()));
        return result;
    }

    @Override
    public MemberInfo getMemberInfo(MemberInfoGetParam param) {
        return memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getMemberCode, param.getMemberCode())
        );
    }

    @Override
    public IPage<WechatFansInfo> pageWechatFans(WechatFansPageParam param) {
        return wechatFansInfoMapper.selectPage(param.page(), Wrappers.<WechatFansInfo>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), WechatFansInfo::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getNickName()), WechatFansInfo::getNickName, param.getNickName())
                .eq(param.getGender() != null, WechatFansInfo::getGender, param.getGender())
                .like(StrUtil.isNotBlank(param.getCountry()), WechatFansInfo::getCountry, param.getCountry())
                .like(StrUtil.isNotBlank(param.getProvince()), WechatFansInfo::getProvince, param.getProvince())
                .like(StrUtil.isNotBlank(param.getCity()), WechatFansInfo::getCity, param.getCity())
                .ge(param.getSubscribeTimeStart() != null, WechatFansInfo::getSubscribeTime, param.getSubscribeTimeStart())
                .le(param.getSubscribeTimeEnd() != null, WechatFansInfo::getSubscribeTime, param.getSubscribeTimeEnd())
                .ge(param.getUnsubscribeTimeStart() != null, WechatFansInfo::getUnsubscribeTime, param.getUnsubscribeTimeStart())
                .le(param.getUnsubscribeTimeEnd() != null, WechatFansInfo::getUnsubscribeTime, param.getUnsubscribeTimeEnd())
                .eq(param.getSubscribeScene() != null, WechatFansInfo::getSubscribeScene, param.getSubscribeScene())
                .eq(param.getSubscribeState() != null, WechatFansInfo::getSubscribeState, param.getSubscribeState())
        );
    }

    private RewardTypeEnum getRewardType(RewardTypeEnum rewardType, String appId, String itemName) {
        if (rewardType == null) {
            rewardType = clientConfigService.getItemByName(appId, itemName);
        }
        return rewardType;
    }

    @Override
    public MemberOtherResult getMemberOther(MemberOtherGetParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String appId = MdcUtil.getMemberAppId(memberInfo);
        RMapCache<String, MemberOtherResult> map = RedisUtil.redisson().getMapCache("memberOtherInfo");
        MemberOtherResult result = map.get(appId + memberInfo.getId() + SecureUtil.md5(JSONUtil.toJsonStr(param)));
        if (result == null) {
            result = new MemberOtherResult();
            DateTime now = DateTime.now();
            if (param.getGetCouponCount()) {
                int couponCount = couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                        .eq(CouponMemberRel::getMemberId, memberInfo.getId())
                        .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                        .ge(CouponMemberRel::getEffectiveEndTime, now)
                );
                result.setCouponCount(couponCount);
            }
            RewardTypeEnum totalType = getRewardType(param.getTotalType(), appId, "totalType");
            if (totalType != null) {
                String key = memberRewardService.getRankCacheKey(null, null, appId, totalType);
                Double score = RedisUtil.<Long>zSetRedis().score(key, memberInfo.getId());
                Integer total = Convert.toInt(score, 0);
                result.setTotal(total);
            }
            RewardTypeEnum gradeType = getRewardType(param.getGradeType(), appId, "gradeType");
            if (gradeType != null) {
                String key = memberRewardService.getRankCacheKey(null, null, appId, gradeType);
                Double score = RedisUtil.<Long>zSetRedis().score(key, memberInfo.getId());
                Integer reward = Convert.toInt(score, 0);
                String grade = memberGradeService.getRewardGrade(appId, gradeType, reward).getGradeTitle();
                result.setGradeName(grade);
            }
            RewardTypeEnum gradeRateType = getRewardType(param.getGradeRateType(), appId, "gradeRateType");
            if (gradeRateType != null) {
                String key = memberRewardService.getRankCacheKey(null, null, appId, gradeRateType);
                Double score = RedisUtil.<Long>zSetRedis().score(key, memberInfo.getId());
                Integer reward = Convert.toInt(score, 0);
                MemberGrade grade = memberGradeService.getRewardGrade(appId, gradeRateType, reward);
                BigDecimal gradeRate = BigDecimal.ZERO;
                if (grade.getMaxValue() != null && grade.getMaxValue() > 0) {
                    gradeRate = NumberUtil.toBigDecimal(NumberUtil.div(reward - grade.getMinValue(), grade.getMaxValue() - grade.getMinValue(), 2));
                }
                result.setGradeRate(gradeRate);
            }
            RewardTypeEnum rankType = getRewardType(param.getRankType(), appId, "rankType");
            if (rankType != null) {
                String key = memberRewardService.getRankCacheKey(null, null, appId, rankType);
                Long rank = RedisUtil.<Long>zSetRedis().reverseRank(key, memberInfo.getId());
                rank = rank == null ? null : Convert.toLong(rank, 0L) + 1;
                result.setRank(rank);
            }
            map.fastPut(appId + memberInfo.getId(), result, 10, TimeUnit.MINUTES);
        }
        return result;
    }

    @Override
    public void saveMemberExtra(MemberExtraSaveParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        MemberExtraInfo memberExtraInfo = memberExtraInfoMapper.selectOne(Wrappers.<MemberExtraInfo>lambdaQuery()
                .eq(MemberExtraInfo::getMemberId, memberInfo.getId())
        );
        int count;
        if (memberExtraInfo == null) {
            memberExtraInfo = new MemberExtraInfo();
            MdcUtil.setMemberInfo(memberExtraInfo, memberInfo);
            BeanUtil.copyProperties(param, memberExtraInfo, CopyOptions.create().ignoreNullValue());
            count = memberExtraInfoMapper.insert(memberExtraInfo);
        } else {
            BeanUtil.copyProperties(param, memberExtraInfo, CopyOptions.create().ignoreNullValue());
            count = memberExtraInfoMapper.updateById(memberExtraInfo);
        }
        if (count <= 0) {
            throw new ServiceException("保存会员额外信息失败");
        }
    }

    //======api=======//
    private String login2Session(MemberInfo memberInfo, String... param) {
        String memberSession = MdcUtil.getCurrentMemberSession();
        //获取旧的有效的sessionKey
        String sessionKey = null;
        if (StrUtil.isBlank(memberSession)) {
            memberSession = MdcUtil.getSnowflakeIdStr();
        } else {
            try {
                MemberInfoResult memberInfoResult = RedisUtil.<MemberInfoResult>valueRedis().get(memberSession);
                if (memberInfoResult != null) {
                    boolean isSameAlipay = StrUtil.isAllNotBlank(memberInfoResult.getAliAppId(), memberInfo.getAliAppId(),
                            memberInfoResult.getUserId(), memberInfo.getUserId())
                            && StrUtil.equals(memberInfoResult.getAliAppId(), memberInfo.getAliAppId())
                            && StrUtil.equals(memberInfoResult.getUserId(), memberInfo.getUserId());
                    boolean isSameWechat = StrUtil.isAllNotBlank(memberInfoResult.getWxAppId(), memberInfo.getWxAppId(),
                            memberInfoResult.getOpenId(), memberInfo.getOpenId())
                            && StrUtil.equals(memberInfoResult.getWxAppId(), memberInfo.getWxAppId())
                            && StrUtil.equals(memberInfoResult.getOpenId(), memberInfo.getOpenId());
                    boolean isSameId = ObjectUtil.equal(memberInfoResult.getId(), memberInfo.getId());
                    if (isSameAlipay || isSameWechat || isSameId) {
                        sessionKey = memberInfoResult.getSessionKey();
                    } else {
                        memberSession = MdcUtil.getSnowflakeIdStr();
                    }
                } else {
                    memberSession = MdcUtil.getSnowflakeIdStr();
                }
            } catch (Exception e) {
                log.error("根据缓存获取会员信息失败");
                memberSession = MdcUtil.getSnowflakeIdStr();
            }
        }
        if (memberInfo != null) {
            MemberInfoResult memberInfoResult = new MemberInfoResult();
            BeanUtil.copyProperties(memberInfo, memberInfoResult, CopyOptions.create().ignoreNullValue());
            if (param.length > 0) {
                sessionKey = param[0];
            }
            memberInfoResult.setSessionKey(sessionKey);
            RedisUtil.valueRedis().set(memberSession, memberInfoResult);
            RedisUtil.redis().expire(memberSession, 3, TimeUnit.DAYS);
        }
        return memberSession;
    }

    private MemberInfo parseUnifiedMember(MemberInfo memberInfo) {
        SystemTenantConfigResult result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.UNIFIEDMEMBER);
        if (memberInfo != null && result != null && result.getUnifiedMemberConfig() != null && result.getUnifiedMemberConfig().getUnified()) {
            UnifiedMemberConfig unifiedMemberConfig = result.getUnifiedMemberConfig();
            boolean isIncludeSource = unifiedMemberConfig.getIncludeSource().contains(memberInfo.getSource());
            boolean isIncludeValue = false;
            if (unifiedMemberConfig.getIncludeCol() == UnifiedColNameEnum.APPID) {
                isIncludeValue = CollUtil.containsAny(unifiedMemberConfig.getIncludeValue(), CollUtil.newArrayList(memberInfo.getAliAppId(), memberInfo.getWxAppId()));
            }
            if (isIncludeSource && isIncludeValue) {
                Object unifiedValue = BeanUtil.getFieldValue(memberInfo, unifiedMemberConfig.getUnifiedJudgeCol().getColName());
                if (unifiedValue != null) {
                    String hashKey = StrUtil.toString(unifiedValue);
                    RMapCache<String, MemberUnifiedInfo> map = RedisUtil.redisson().getMapCache("unifiedMemberRMap" + "_" + MdcUtil.getCurrentTenantId());
                    MemberUnifiedInfo memberUnified = map.get(hashKey);
                    if (memberUnified == null) {
                        memberUnified = RedisUtil.syncLoad("parseUnifiedMember_" + unifiedValue + MdcUtil.getCurrentTenantId(), () -> {
                            MemberUnifiedInfo memberUnifiedInfo = memberUnifiedInfoMapper.selectOne(Wrappers.<MemberUnifiedInfo>query()
                                    .eq(StrUtil.toUnderlineCase(unifiedMemberConfig.getUnifiedJudgeCol().getColName()), unifiedValue)
                                    .lambda()
                                    .select(MemberUnifiedInfo::getMemberId, MemberUnifiedInfo::getMemberCode)
                                    .last("limit 1")
                            );
                            if (memberUnifiedInfo == null) {
                                memberUnifiedInfo = new MemberUnifiedInfo();
                                MemberInfo unifiedMember = null;
                                if (unifiedMemberConfig.getUnifiedCol() == UnifiedColNameEnum.APPID) {
                                    if (StrUtil.equalsAny(unifiedMemberConfig.getUnifiedValue(), memberInfo.getAliAppId(), memberInfo.getWxAppId())
                                            && memberInfo.getSource() == unifiedMemberConfig.getUnifiedSource()) {
                                        unifiedMember = memberInfo;
                                    }
                                }
                                if (unifiedMember == null) {
                                    unifiedMember = memberInfoMapper.selectOne(Wrappers.<MemberInfo>query()
                                            .eq(StrUtil.toUnderlineCase(unifiedMemberConfig.getUnifiedJudgeCol().getColName()), unifiedValue)
                                            .lambda()
                                            .eq(MemberInfo::getSource, unifiedMemberConfig.getUnifiedSource())
                                            .nested(unifiedMemberConfig.getUnifiedCol() == UnifiedColNameEnum.APPID, i -> i
                                                    .eq(MemberInfo::getAliAppId, unifiedMemberConfig.getUnifiedValue()).or()
                                                    .eq(MemberInfo::getWxAppId, unifiedMemberConfig.getUnifiedValue()))
                                            .last("limit 1")
                                    );
                                }
                                if (unifiedMember == null) {
                                    unifiedMember = memberInfo;
                                }
                                BeanUtil.copyProperties(unifiedMember, memberUnifiedInfo, MdcUtil.getIgnoreNames());
                                memberUnifiedInfo.setMemberId(unifiedMember.getId());
                                memberUnifiedInfo.setLastOperationTime(DateTime.now());
                                int count = memberUnifiedInfoMapper.insert(memberUnifiedInfo);
                                if (count <= 0) {
                                    throw new ServiceException("获取统一会员有误");
                                }
                            }
                            return memberUnifiedInfo;
                        });
                        map.fastPut(hashKey, memberUnified, 7, TimeUnit.DAYS);
                    }
                    memberInfo.setId(memberUnified.getMemberId());
                    memberInfo.setMemberCode(memberUnified.getMemberCode());
                }
            }
        }
        return memberInfo;
    }

    @Override
    public MemberInfo getUnifiedMember(LambdaQueryWrapper<MemberInfo> wrappers, Boolean... isNormalSelect) {
        MemberInfo memberInfo;
        if (isNormalSelect.length > 0 && isNormalSelect[0]) {
            memberInfo = memberInfoMapper.normalSelectOne(wrappers);
            //设置此次请求租户id
            ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, memberInfo.getTenantId());
            ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, memberInfo.getSubTenantId());
        } else {
            memberInfo = memberInfoMapper.selectOne(wrappers);
        }
        return parseUnifiedMember(memberInfo);
    }

    private Integer saveMember(MemberInfo memberInfo) {
        String lockName;
        LambdaQueryWrapper<MemberInfo> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(memberInfo.getWxAppId()) && StrUtil.isNotBlank(memberInfo.getOpenId())) {
            lockName = "saveMember_" + memberInfo.getWxAppId() + memberInfo.getOpenId();
            wrapper.eq(MemberInfo::getWxAppId, memberInfo.getWxAppId()).eq(MemberInfo::getOpenId, memberInfo.getOpenId());
        } else if (StrUtil.isNotBlank(memberInfo.getAliAppId()) && StrUtil.isNotBlank(memberInfo.getUserId())) {
            lockName = "saveMember_" + memberInfo.getAliAppId() + memberInfo.getUserId();
            wrapper.eq(MemberInfo::getAliAppId, memberInfo.getAliAppId()).eq(MemberInfo::getUserId, memberInfo.getUserId());
        } else if (StrUtil.isNotBlank(memberInfo.getPhoneNumber())) {
            lockName = "saveMember_" + memberInfo.getPhoneNumber();
            wrapper.eq(MemberInfo::getPhoneNumber, memberInfo.getPhoneNumber());
        } else {
            throw new ServiceException("注册会员失败,缺少必填信息");
        }
        memberInfo.setLastOperationTime(DateTime.now());
        return RedisUtil.syncLoad(lockName, () -> {
            MemberInfo member = memberInfoMapper.selectOne(wrapper);
            int count;
            DateTime now = DateTime.now();
            memberInfo.setLastAuthTime(now);
            if (member == null) {
                memberInfo.setAuthTime(now);
                memberInfo.setLastAuthTime(now);
                count = memberInfoMapper.insert(memberInfo);
                updateMemberWithCode(memberInfo);
            } else {
                memberInfo.setLastAuthTime(now);
                memberInfo.setId(member.getId());
                count = memberInfoMapper.updateById(memberInfo);
            }
            if (count <= 0) {
                throw new ServiceException("保存会员信息失败");
            }
            return count;
        });
    }

    private void saveMember(String appId, String openId) {
        MemberInfo memberInfo = new MemberInfo();
        if (StrUtil.startWith(appId, "wx")) {
            WechatFansInfo wechatFansInfo = wechatFansInfoMapper.selectOne(Wrappers.<WechatFansInfo>lambdaQuery()
                    .eq(WechatFansInfo::getAppId, appId)
                    .eq(WechatFansInfo::getOpenId, openId)
            );
            if (wechatFansInfo == null) {
                throw MemberErrorEnum.MEMBER_ERROR_10002.getErrorMeta().getException();
            }
            BeanUtil.copyProperties(wechatFansInfo, memberInfo, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
            memberInfo.setWxAppId(appId);
            memberInfo.setSource(SourceEnum.GZH);
            memberInfo.setIsAuth(WhetherEnum.YES);
            memberInfo.setNeedMerge(WhetherEnum.NO);
            memberInfo.setLastOperationTime(DateTime.now());
        }
        int count = saveMember(memberInfo);
        if (count <= 0) {
            throw new ServiceException("保存会员失败");
        }
    }

    private void updateLastTime(Long memberId) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            memberInfoMapper.update(null, Wrappers.<MemberInfo>lambdaUpdate()
                    .set(MemberInfo::getLastOperationTime, DateTime.now())
                    .eq(MemberInfo::getId, memberId)
            );
            memberUnifiedInfoMapper.update(null, Wrappers.<MemberUnifiedInfo>lambdaUpdate()
                    .set(MemberUnifiedInfo::getLastOperationTime, DateTime.now())
                    .eq(MemberUnifiedInfo::getMemberId, memberId)
            );
        });
    }

    private void updateMemberWithCode(MemberInfo memberInfo) {
        if (StrUtil.isBlank(memberInfo.getMemberCode())) {
            //异步加锁生成会员编码
            MdcUtil.getTtlExecutorService().submit(() -> {
                RedisUtil.syncLoad("buildMemberCode" + MdcUtil.getCurrentTenantId(), () -> {
                    String code = buildMemberCode(memberInfo.getId());
                    memberInfoMapper.update(null, Wrappers.<MemberInfo>lambdaUpdate()
                            .set(MemberInfo::getMemberCode, code)
                            .eq(MemberInfo::getId, memberInfo.getId())
                    );
                    return code;
                });
            });
        }
    }

    private void initMemberCode() {
        String setKey = "memberCodeSet" + MdcUtil.getCurrentTenantId();
        String mapKey = "memberCodeMap" + MdcUtil.getCurrentTenantId();
        if (!RedisUtil.redis().hasKey(setKey)) {
            List<MemberInfo> memberInfoList = memberInfoMapper.selectList(Wrappers.<MemberInfo>lambdaQuery()
                    .select(MemberInfo::getMemberCode, MemberInfo::getId)
                    .isNotNull(MemberInfo::getMemberCode)
            );
            if (memberInfoList.size() > 0) {
                RedisUtil.setRedis().add(setKey, memberInfoList.stream().map(MemberInfo::getMemberCode).distinct().toArray(String[]::new));
                RedisUtil.hashRedis().putAll(mapKey, memberInfoList.stream().collect(toMap(MemberInfo::getId, MemberInfo::getMemberCode)));
                RedisUtil.redis().expire(setKey, 30, TimeUnit.DAYS);
                RedisUtil.redis().expire(mapKey, 30, TimeUnit.DAYS);
            }
        }
    }

    private String buildMemberCode(Long memberId) {
        if (MdcUtil.getCurrentTenantId() == null) {
            return null;
        }
        String setKey = "memberCodeSet" + MdcUtil.getCurrentTenantId();
        String mapKey = "memberCodeMap" + MdcUtil.getCurrentTenantId();
        if (!RedisUtil.redis().hasKey(setKey)) {
            initMemberCode();
        }
        String code = RedisUtil.<Long, String>hashRedis().get(mapKey, memberId);
        if (code != null) {
            return code;
        }
        code = RandomUtil.randomNumbers(8);
        Long count = RedisUtil.setRedis().add(setKey, code);
        if (count < 1) {
            code = buildMemberCode(memberId);
        }
        RedisUtil.hashRedis().put(mapKey, memberId, code);
        RedisUtil.redis().expire(setKey, 30, TimeUnit.DAYS);
        RedisUtil.redis().expire(mapKey, 30, TimeUnit.DAYS);
        return code;
    }

    @Override
    public BaseResult<String> register(MemberRegisterParam param) {
        MemberInfo memberInfo = new MemberInfo();
        BeanUtil.copyProperties(param, memberInfo);
        memberInfo.setSource(SourceEnum.EXTRA);
        memberInfo.setIsAuth(WhetherEnum.YES);
        int count = saveMember(memberInfo);
        if (count > 0) {
            memberInfo = memberInfoMapper.selectById(memberInfo.getId());
            String memberSession = login2Session(parseUnifiedMember(memberInfo));
            return new BaseResult<String>().setResult(memberSession);
        }
        throw new ServiceException("注册失败");
    }

    @Override
    public BaseResult<String> login(MemberLoginParam param) {
        LambdaQueryWrapper<MemberInfo> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(param.getAppId()) && StrUtil.isNotBlank(param.getOpenId())) {
            wrapper.eq(MemberInfo::getWxAppId, param.getAppId()).eq(MemberInfo::getOpenId, param.getOpenId());
        } else if (StrUtil.isNotBlank(param.getAppId()) && StrUtil.isNotBlank(param.getUserId())) {
            wrapper.eq(MemberInfo::getAliAppId, param.getAppId()).eq(MemberInfo::getUserId, param.getUserId());
        } else if (StrUtil.isNotBlank(param.getPhoneNumber())) {
            wrapper.eq(MemberInfo::getPhoneNumber, param.getPhoneNumber());
        } else {
            throw new ServiceException("登录会员失败,缺少必填信息");
        }
        MemberInfo member = memberInfoMapper.selectOne(wrapper);
        if (member == null && StrUtil.isNotBlank(param.getOpenId())) {
            saveMember(param.getAppId(), param.getOpenId());
            member = memberInfoMapper.selectOne(wrapper);
        }
        if (member != null) {
            updateLastTime(member.getId());
            updateMemberWithCode(member);
            MemberInfo memberUnified = parseUnifiedMember(member);
            String memberSession = login2Session(memberUnified);
            return new BaseResult<String>().setResult(memberSession);
        }
        throw MemberErrorEnum.MEMBER_ERROR_10002.getErrorMeta().getException();
    }

    @Override
    public void edit(MemberEditParam param) {
        MemberInfo member = MdcUtil.getRequireCurrentMember();
        MemberInfo update = new MemberInfo();
        BeanUtil.copyProperties(param, update, CopyOptions.create().ignoreNullValue());
        int count = memberInfoMapper.update(update, Wrappers.<MemberInfo>lambdaUpdate()
                .eq(StrUtil.isAllBlank(member.getWxAppId(), member.getAliAppId()), MemberInfo::getId, member.getId())
                .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getWxAppId, member.getWxAppId())
                .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getOpenId, member.getOpenId())
                .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getAliAppId, member.getAliAppId())
                .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getUserId, member.getUserId())
        );
        if (count <= 0) {
            throw new ServiceException("更新失败");
        } else {
            MemberInfo newMember = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(StrUtil.isAllBlank(member.getWxAppId(), member.getAliAppId()), MemberInfo::getId, member.getId())
                    .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getWxAppId, member.getWxAppId())
                    .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getOpenId, member.getOpenId())
                    .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getAliAppId, member.getAliAppId())
                    .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getUserId, member.getUserId())
            );
            if (StrUtil.isAllNotBlank(newMember.getWxAppId(), newMember.getOpenId())) {
                String hashKey = newMember.getWxAppId() + "_" + newMember.getOpenId();
                RedisUtil.redisson().getMapCache("miniMemberRMap").fastRemove(hashKey);
            } else if (StrUtil.isAllNotBlank(newMember.getAliAppId(), newMember.getUserId())) {
                String hashKey = newMember.getAliAppId() + "_" + newMember.getUserId();
                RedisUtil.redisson().getMapCache("tinyMemberRMap").fastRemove(hashKey);
            }
            newMember.setId(member.getId());
            newMember.setMemberCode(member.getMemberCode());
            login2Session(newMember);
        }
    }

    @Override
    public void editPhone(MemberInfo member, String phoneNumber) {
        int count = memberInfoMapper.selectCount(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getPhoneNumber, phoneNumber)
                .ne(MemberInfo::getId, member.getId())
                .eq(MemberInfo::getSource, member.getSource())
                .eq(StrUtil.isNotBlank(member.getWxAppId()), MemberInfo::getWxAppId, member.getWxAppId())
                .eq(StrUtil.isNotBlank(member.getAliAppId()), MemberInfo::getAliAppId, member.getAliAppId())
        );
        if (count > 0) {
            throw MemberErrorEnum.MEMBER_ERROR_10005.getErrorMeta().getException();
        }
        MemberInfo update = new MemberInfo();
        update.setPhoneNumber(phoneNumber);
        count = memberInfoMapper.update(update, Wrappers.<MemberInfo>lambdaUpdate()
                .eq(StrUtil.isAllBlank(member.getWxAppId(), member.getAliAppId()), MemberInfo::getId, member.getId())
                .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getWxAppId, member.getWxAppId())
                .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getOpenId, member.getOpenId())
                .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getAliAppId, member.getAliAppId())
                .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getUserId, member.getUserId())
        );
        if (count <= 0) {
            throw new ServiceException("更新手机失败");
        } else {
            MemberInfo newMember = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(StrUtil.isAllBlank(member.getWxAppId(), member.getAliAppId()), MemberInfo::getId, member.getId())
                    .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getWxAppId, member.getWxAppId())
                    .eq(StrUtil.isAllNotBlank(member.getWxAppId(), member.getOpenId()), MemberInfo::getOpenId, member.getOpenId())
                    .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getAliAppId, member.getAliAppId())
                    .eq(StrUtil.isAllNotBlank(member.getAliAppId(), member.getUserId()), MemberInfo::getUserId, member.getUserId())
            );
            if (StrUtil.isAllNotBlank(newMember.getWxAppId(), newMember.getOpenId())) {
                String hashKey = newMember.getWxAppId() + "_" + newMember.getOpenId();
                RedisUtil.redisson().getMapCache("miniMemberRMap").fastRemove(hashKey);
            } else if (StrUtil.isAllNotBlank(newMember.getAliAppId(), newMember.getUserId())) {
                String hashKey = newMember.getAliAppId() + "_" + newMember.getUserId();
                RedisUtil.redisson().getMapCache("tinyMemberRMap").fastRemove(hashKey);
            }
            login2Session(parseUnifiedMember(newMember));
        }
    }

    @Override
    public void editPhone(MemberPhoneEditParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String msgCode = RedisUtil.<String>valueRedis().get("editPhone_" + param.getPhoneNumber());
        if (StrUtil.equals(param.getMsgCode(), msgCode)) {
            editPhone(memberInfo, param.getPhoneNumber());
        }
        throw new ServiceException("验证码不正确");
    }

    private static final String[] LABELS = {"富可敌国", "家财万贯", "好运连连",
            "神采奕奕", "温文尔雅", "眉飞色舞",
            "眉开眼笑", "笑逐颜开", "从容自若",
            "舒眉展眼", "神清气爽", "悠然自得",
            "神采奕奕", "温文尔雅", "眉飞色舞",
            "眉开眼笑", "笑逐颜开", "从容自若",
            "舒眉展眼", "神清气爽", "悠然自得"};

    @Override
    public MemberFace addFace(MemberFaceAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        MemberFace face = new MemberFace();
        BeanUtil.copyProperties(param, face);
        MdcUtil.setMemberInfo(face, memberInfo);
        face.setFaceScore(RandomUtil.randomInt(90, 101));
        Set<String> labelSet = new HashSet<>();
        while (labelSet.size() < 3) {
            int j = RandomUtil.randomInt(0, LABELS.length);
            labelSet.add(LABELS[j]);
        }
        face.setFaceLabel(StrUtil.join(",", labelSet.toArray()));
        int count = memberFaceMapper.insert(face);
        if (count <= 0) {
            throw new ServiceException("保存失败");
        }
        return face;
    }

    @Override
    public BaseResult<String> loginMiniMember(MiniMemberLoginParam param) {
        if (StrUtil.equals(param.getJsCode(), "error")) {
            throw new ServiceException("登录异常");
        }
        String otherAppId = MdcUtil.getOtherAppId();
        WechatAuthorSite site = wechatMiniService.getWxAuthorByAppId(otherAppId);
        if (site == null) {
            throw new ServiceException("授权号不存在");
        }
        try {
            WxMaJscode2SessionResult result = wechatMiniService.getWxMaServiceByAppId(otherAppId).jsCode2SessionInfo(param.getJsCode());
            String hashKey = otherAppId + "_" + result.getOpenid();
            RMapCache<String, MemberInfo> map = RedisUtil.redisson().getMapCache("miniMemberRMap");
            MemberInfo memberInfo = map.get(hashKey);
            if (memberInfo == null) {
                memberInfo = RedisUtil.syncLoad("saveMember_" + otherAppId + result.getOpenid(), () -> {
                    MemberInfo member = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                            .eq(MemberInfo::getWxAppId, otherAppId).eq(MemberInfo::getOpenId, result.getOpenid()));
                    if (member == null) {
                        member = new MemberInfo();
                        member.setWxAppId(otherAppId);
                        member.setOpenId(result.getOpenid());
                        member.setUnionId(result.getUnionid());
                        member.setSource(SourceEnum.MINI);
                        member.setIsAuth(WhetherEnum.NO);
                        member.setNeedMerge(WhetherEnum.NO);
                        member.setLastOperationTime(DateTime.now());
                        int count = memberInfoMapper.insert(member);
                        if (count > 0) {
                            member = memberInfoMapper.selectById(member.getId());
                        } else {
                            throw new ServiceException("登录失败");
                        }
                    }
                    return member;
                });
            }
            updateLastTime(memberInfo.getId());
            updateMemberWithCode(memberInfo);
            map.fastPut(hashKey, memberInfo, 10, TimeUnit.MINUTES);
            MemberInfo memberUnified = parseUnifiedMember(memberInfo);
            String memberSession = login2Session(memberUnified, result.getSessionKey());
            return new BaseResult<String>().setResult(memberSession);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof WxErrorException) {
                WxErrorException wxErrorException = (WxErrorException) e;
                throw new ServiceException(String.valueOf(wxErrorException.getError().getErrorCode()), wxErrorException.getError().getErrorMsg());
            } else {
                throw new ServiceException("登录失败");
            }
        }
    }

    @Override
    public void authorizeMiniMember(MiniMemberAuthorizeParam param) {
        MemberInfoResult sessionKey = MdcUtil.getMiniSessionKey();
        String appId = sessionKey.getWxAppId();
        boolean check = wechatMiniService.getWxMaServiceByAppId(appId).getUserService()
                .checkUserInfo(sessionKey.getSessionKey(), param.getRawData(), param.getSignature());
        if (check) {
            WxMaUserInfo userInfo;
            if (StrUtil.isNotBlank(param.getEncryptedData()) && StrUtil.isNotBlank(param.getIv())) {
                userInfo = wechatMiniService.getWxMaServiceByAppId(appId)
                        .getUserService().getUserInfo(sessionKey.getSessionKey(), param.getEncryptedData(), param.getIv());
            } else {
                userInfo = new WxMaUserInfo();
                BeanUtil.copyProperties(param.getUserInfo(), userInfo);
            }
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setIsAuth(WhetherEnum.YES);
            DateTime now = DateTime.now();
            if (sessionKey.getAuthTime() == null) {
                memberInfo.setAuthTime(now);
            }
            memberInfo.setLastAuthTime(now);
            memberInfo.setNickName(userInfo.getNickName());
            memberInfo.setAvatarUrl(userInfo.getAvatarUrl());
            memberInfo.setSex(EnumUtil.likeValueOf(SexEnum.class, Convert.toInt(userInfo.getGender())));
            memberInfo.setCountry(userInfo.getCountry());
            memberInfo.setProvince(userInfo.getProvince());
            memberInfo.setCity(userInfo.getCity());
            memberInfo.setUnionId(userInfo.getUnionId());
            memberInfo.setWxContent(JSONUtil.toJsonStr(userInfo));
            if (sessionKey.getGuideId() == null) {
                memberInfo.setGuideId(param.getGuideId());
            }
            int count = memberInfoMapper.update(memberInfo, Wrappers.<MemberInfo>lambdaUpdate()
                    .eq(MemberInfo::getWxAppId, sessionKey.getWxAppId())
                    .eq(MemberInfo::getOpenId, sessionKey.getOpenId())
            );
            if (count > 0) {
                memberInfo = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                        .eq(MemberInfo::getWxAppId, sessionKey.getWxAppId())
                        .eq(MemberInfo::getOpenId, sessionKey.getOpenId())
                );
                String hashKey = memberInfo.getWxAppId() + "_" + memberInfo.getOpenId();
                RedisUtil.redisson().getMapCache("miniMemberRMap").fastRemove(hashKey);
                memberInfo.setId(sessionKey.getId());
                memberInfo.setMemberCode(sessionKey.getMemberCode());
                login2Session(memberInfo);
            } else {
                throw new ServiceException("授权失败");
            }
            return;
        }
        throw new ServiceException("加密数据校验不通过");
    }

    @Override
    public BaseResult<String> getMiniPhone(MiniPhoneGetParam param) {
        MemberInfoResult sessionKey = MdcUtil.getMiniSessionKey();
        String appId = sessionKey.getWxAppId();
        try {
            WxMaPhoneNumberInfo phoneNumberInfo = wechatMiniService.getWxMaServiceByAppId(appId)
                    .getUserService().getPhoneNoInfo(sessionKey.getSessionKey(), param.getEncryptedData(), param.getIv());
            return new BaseResult<String>().setResult(phoneNumberInfo.getPhoneNumber());
        } catch (Exception e) {
            log.error("微信小程序接口解密手机号失败", e);
            throw new ServiceException("获取小程序手机失败");
        }
    }

    @Override
    public BaseResult<String> editMiniPhone(MiniPhoneGetParam param) {
        MemberInfoResult sessionKey = MdcUtil.getMiniSessionKey();
        String appId = sessionKey.getWxAppId();
        WxMaPhoneNumberInfo phoneNumberInfo = wechatMiniService.getWxMaServiceByAppId(appId)
                .getUserService().getPhoneNoInfo(sessionKey.getSessionKey(), param.getEncryptedData(), param.getIv());
        editPhone(sessionKey, phoneNumberInfo.getPhoneNumber());
        return new BaseResult<String>().setResult(phoneNumberInfo.getPhoneNumber());
    }

    @Override
    public void saveMiniDevice(MiniDeviceSaveParam param) {
        MemberInfoResult memberInfoResult = MdcUtil.getRequireCurrentMember();
        JSONObject jsonObject = JSONUtil.parseObj(param.getDeviceInfo());
        WechatMiniProgramDeviceInfo deviceInfo = JSONUtil.toBean(jsonObject, WechatMiniProgramDeviceInfo.class);
        deviceInfo.setSdkVersion(jsonObject.getStr("SDKVersion"));
        deviceInfo.setAppId(memberInfoResult.getWxAppId());
        deviceInfo.setMemberId(memberInfoResult.getId());
        int count = wechatMiniProgramDeviceInfoMapper.update(deviceInfo, Wrappers.<WechatMiniProgramDeviceInfo>lambdaUpdate()
                .eq(WechatMiniProgramDeviceInfo::getMemberId, memberInfoResult.getId()));
        if (count <= 0) {
            count = wechatMiniProgramDeviceInfoMapper.insert(deviceInfo);
            if (count <= 0) {
                throw new ServiceException("保存设备信息失败");
            }
        }
    }

    @Override
    public MemberAttributesAnalysisResult memberAttributesAnalysis(MemberAttributesAnalysisParam param) {
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getWxAppId, param.getAppId())
                .or().eq(MemberInfo::getAliAppId, param.getAppId())
                .eq(MemberInfo::getIsAuth, WhetherEnum.YES)
        );

        //分析获取性别数量
        long maleCount = memberInfos.stream().filter(item -> ObjectUtil.equal(item.getSex(), SexEnum.MALE)).count();
        long femaleCount = memberInfos.stream().filter(item -> ObjectUtil.equal(item.getSex(), SexEnum.FEMALE)).count();
        long unknownCount = memberInfos.stream().filter(item -> ObjectUtil.equal(item.getSex(), SexEnum.UNKNOWN) || ObjectUtil.isNull(item.getSex())).count();

        //封装性别数据
        List<MemberAttributesAnalysisResult.SexAttributes> sex = new ArrayList<>();
        List<Object> sexType = EnumUtil.getFieldValues(SexEnum.class, "type");
        for (Object o : sexType) {
            MemberAttributesAnalysisResult.SexAttributes sexAttributes = new MemberAttributesAnalysisResult.SexAttributes();
            SexEnum sexEnum = EnumUtil.likeValueOf(SexEnum.class, o);
            if (ObjectUtil.equal(SexEnum.UNKNOWN, sexEnum)) {
                sexAttributes.setSexAttributes(sexEnum.getName(), unknownCount);
            } else if (ObjectUtil.equal(SexEnum.MALE, sexEnum)) {
                sexAttributes.setSexAttributes(sexEnum.getName(), maleCount);
            } else if (ObjectUtil.equal(SexEnum.FEMALE, sexEnum)) {
                sexAttributes.setSexAttributes(sexEnum.getName(), femaleCount);
            }
            sex.add(sexAttributes);
        }

        //年龄数据处理
        List<String> ageX = new ArrayList<>();
        List<Long> ageY = new ArrayList<>();
        Map<String, BigDecimal> ageMap = memberInfoMapper.selectAgeCount(param.getAppId());
        List<String> ageItem = new ArrayList<String>() {{
            add("未知");
            add("17岁以下");
            add("18-24岁");
            add("25-29岁");
            add("30-39岁");
            add("40-49岁");
            add("50岁以上");
        }};
        for (String name : ageItem) {
            ageX.add(name);
            ageY.add(null == ageMap || null == ageMap.get(name) ? 0L : ageMap.get(name).longValue());
        }

        //等级数据处理
        List<String> gradeX = new ArrayList<>();
        List<Long> gradeY = new ArrayList<>();
        MemberGradeListParam memberGradeListParam = new MemberGradeListParam();
        memberGradeListParam.setRewardType(param.getRewardType());
        memberGradeListParam.setAppId(param.getAppId());
        List<MemberGradeResult> memberGradeResults = memberGradeService.listMemberGrade(memberGradeListParam);

        if (null != memberGradeResults && memberGradeResults.size() > 0) {
            for (MemberGradeResult item : memberGradeResults) {
                gradeX.add(item.getGradeTitle());
                gradeY.add(item.getMemberCount());
            }
        }

        return new MemberAttributesAnalysisResult(sex, ageX, ageY, gradeX, gradeY);
    }

    private static final Pattern PHONE_PATTERN = Pattern.compile("[1]\\d{10}");

    @Override
    public void addMemberInfo(MemberAddParam param) {
        //判断手机号是否为13位数字
        boolean matches = PHONE_PATTERN.matcher(param.getPhoneNumber().trim()).matches();
        if (!matches) {
            throw new ServiceException("手机号不合法");
        }

        //验证数据库是否存在
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getPhoneNumber, param.getPhoneNumber())
                .eq(MemberInfo::getSource, param.getSource())
        );

        int count = 0;
        MemberInfo info = new MemberInfo();
        BeanUtil.copyProperties(param, info);


        if (null != memberInfos && memberInfos.size() > 1) {
            //多条手机号存在，抛异常
            throw new ServiceException("已存在多条该手机号的会员信息");
        } else if (null != memberInfos && memberInfos.size() == 1) {
            //存在一条，更新
            info.setId(memberInfos.get(0).getId());
            count = memberInfoMapper.updateById(info);
        } else if (null == memberInfos || memberInfos.size() == 0) {
            //不存在，保存
            count = memberInfoMapper.insert(info);
        }

        if (count <= 0) {
            throw new ServiceException("新增会员信息失败，请重试");
        }
    }

    @Override
    public void batchAddMemberInfo(MemberBatchAddParam param) {
        try {
            JSONObject jsonObject = PoiUtil.readExcel(param.getFile());
            JSONArray memberInfos = jsonObject.getJSONArray("memberInfo");

            if (null == memberInfos || memberInfos.size() <= 0) {
                throw new ServiceException("数据为空，请检查数据或者模板是否正确");
            }

            boolean repeat = repeatMemberInfo(memberInfos);
            if (repeat) {
                throw new ServiceException("手机号不能重复");
            }

            //查询数据库会员导入信息,(手机号:id)
            List<MemberInfo> dbMembers = memberInfoMapper.selectList(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(MemberInfo::getSource, SourceEnum.IMPORT)
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );
            Map<String, Long> dbMap = new HashMap<>();

            if (null != dbMembers && dbMembers.size() > 0) {
                dbMap = dbMembers.stream().filter(item -> StrUtil.isNotBlank(item.getPhoneNumber())).collect(toMap(MemberInfo::getPhoneNumber, MemberInfo::getId));
            }

            //生成更新和保存的对象集合
            List<MemberInfo> updateInfos = new ArrayList<>();
            List<MemberInfo> insertInfos = new ArrayList<>();
            for (Object memberInfo : memberInfos) {
                JSONObject json = new JSONObject(memberInfo);
                MemberInfo info = new MemberInfo();
                BeanUtil.copyProperties(json, info, CopyOptions.create().ignoreError());
                if (StrUtil.isBlank(info.getPhoneNumber().trim())) {
                    continue;
                }

                info.setSex(EnumUtil.likeValueOf(SexEnum.class, json.getStr("sex")));
                info.setSource(SourceEnum.IMPORT);
                info.setSubSource(EnumUtil.likeValueOf(SubSourceEnum.class, json.getStr("subSource")));

                //判断数据库是否存在该号码
                Long id = dbMap.get(info.getPhoneNumber());
                if (null == id) {
                    //不存在，新增
                    insertInfos.add(info);
                } else {
                    //存在更新
                    info.setId(id);
                    updateInfos.add(info);
                }
            }

            log.info("batchAddMemberInfo(导入会员信息)文件记录总数为：" + memberInfos.size());
            if (null != updateInfos && updateInfos.size() > 0) {
                int updateCount = 0;
                for (MemberInfo updateInfo : updateInfos) {
                    int count = memberInfoMapper.updateById(updateInfo);
                    updateCount = updateCount + count;
                }

                log.info("batchAddMemberInfo(导入会员信息)更新记录总数为：" + updateCount);
            }

            if (null != insertInfos && insertInfos.size() > 0) {
                int insertCount = memberInfoMapper.insertAllBatch(insertInfos);
                log.info("batchAddMemberInfo(导入会员信息)保存记录总数为：" + insertCount);
            }
        } catch (Exception e) {
            throw new ServiceException("导入信息失败");
        }
    }

    /**
     * 判断预导入的会员信息是否存在手机号重复数据：重复：true;不重复：false
     *
     * @param memberInfos 会员数据
     * @return 重复：true;不重复：false
     */
    private boolean repeatMemberInfo(JSONArray memberInfos) {
        Map<String, JSONObject> map = new HashMap<>();
        for (Object memberInfo : memberInfos) {
            JSONObject item = new JSONObject(memberInfo);
            String key = item.getStr("phoneNumber");

            JSONObject mapItem = map.get(key);

            //excel中已经有重复手机号
            if (null != mapItem) {
                return true;
            }

            map.put(key, item);
        }

        return false;
    }
}
