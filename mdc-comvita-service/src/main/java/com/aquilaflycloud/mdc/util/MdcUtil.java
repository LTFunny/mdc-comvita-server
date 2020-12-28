package com.aquilaflycloud.mdc.util;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.aquilaflycloud.auth.bean.*;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedColNameEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.feign.consumer.org.ShopConsumer;
import com.aquilaflycloud.mdc.mapper.SystemConfigMapper;
import com.aquilaflycloud.mdc.mapper.SystemCryptionMapper;
import com.aquilaflycloud.mdc.mapper.SystemIncrementMapper;
import com.aquilaflycloud.mdc.message.MemberErrorEnum;
import com.aquilaflycloud.mdc.model.IsvAppInfo;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.system.SystemConfig;
import com.aquilaflycloud.mdc.model.system.SystemCryption;
import com.aquilaflycloud.mdc.model.system.SystemIncrement;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.result.system.UnifiedMemberConfig;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.aquilaflycloud.org.service.provider.entity.PShopInfo;
import com.aquilaflycloud.util.RedisUtil;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * MdcUtil
 *
 * @author star
 * @date 2019-09-20
 */
@Slf4j
public class MdcUtil {

    public static String[] getIgnoreNames() {
        return new String[]{"id", "createTime", "lastUpdateTime", "tenantId", "subTenantId", "creatorId",
                "creatorName", "creatorOrgIds", "creatorOrgNames", "lastOperatorId", "lastOperatorName", "appKey"};
    }

    public static String getServerName() {
        return SpringUtil.getProperty("spring.application.name");
    }

    public static long getSnowflakeId() {
        return IdWorker.getId();
    }

    public static String getSnowflakeIdStr() {
        return IdWorker.getIdStr();
    }

    public static ExecutorService getTtlExecutorService() {
        ExecutorService executor = GlobalThreadPool.getExecutor();
        return TtlExecutors.getTtlExecutorService(executor);
    }

    public static String getAppKey() {
        if (ServiceContext.getCurrentContext().getOpenContext() != null) {
            return ServiceContext.getCurrentContext().getOpenContext().getAppId();
        }
        return null;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            return ((AdminUserDetails) authentication.getPrincipal()).getUser();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            return ((AdminUserDetails) authentication.getPrincipal()).getUser().getId();
        }
        return null;
    }

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            return ((AdminUserDetails) authentication.getPrincipal()).getUser().getUsername();
        }
        return null;
    }

    public static UserInfo getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            return ((AdminUserDetails) authentication.getPrincipal()).getUserInfo();
        }
        return null;
    }

    public static String getCurrentOrgIds() {
        UserInfo userInfo = MdcUtil.getCurrentUserInfo();
        if (userInfo != null) {
            Object[] departmentIds = {userInfo.getMainDepartment().getId()};
            if (userInfo.getDepartmentList() != null) {
                ArrayUtil.append(departmentIds, userInfo.getDepartmentList().stream().map(Organization::getId).toArray());
            }
            return StrUtil.join(",", departmentIds);
        }
        return null;
    }

    public static String getCurrentOrgNames() {
        UserInfo userInfo = MdcUtil.getCurrentUserInfo();
        if (userInfo != null) {
            Object[] departmentNames = {userInfo.getMainDepartment().getOrgName()};
            if (userInfo.getDepartmentList() != null) {
                ArrayUtil.append(departmentNames, userInfo.getDepartmentList().stream().map(Organization::getOrgName).toArray());
            }
            return StrUtil.join(",", departmentNames);
        }
        return null;
    }

    public static Long getCurrentTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long tenantId = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof AdminUserDetails) {
                tenantId = ((AdminUserDetails) authentication.getPrincipal()).getUser().getTenantId();
            } else if (authentication.getPrincipal() instanceof Isv) {
                tenantId = ((Isv) authentication.getPrincipal()).getTenantId();
            }
        }
        if (tenantId == null) {
            tenantId = Convert.toLong(ServiceContext.getCurrentContext().get(DataAuthConstant.TENANT_ID));
        }
        return tenantId;
    }

    public static Long getCurrentSubTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long subTenantId = null;
        if (authentication.getPrincipal() instanceof AdminUserDetails) {
            subTenantId = ((AdminUserDetails) authentication.getPrincipal()).getUser().getSubTenantId();
        } else if (authentication.getPrincipal() instanceof Isv) {
            subTenantId = ((Isv) authentication.getPrincipal()).getSubTenantId();
        }
        if (subTenantId == null) {
            subTenantId = Convert.toLong(ServiceContext.getCurrentContext().get(DataAuthConstant.TENANT_ID));
        }
        if (subTenantId == null) {
            subTenantId = Convert.toLong(ServiceContext.getCurrentContext().get(DataAuthConstant.SUB_TENANT_ID));
        }
        return subTenantId;
    }

    public static String getCurrentMemberSession() {
        String memberSession = ServiceContext.getCurrentContext().getRequest().getHeader(MdcConstant.MEMBER_SESSION);
        if (StrUtil.isBlankOrUndefined(memberSession)) {
            memberSession = null;
        }
        return memberSession;
    }

    private static MemberInfoResult getMember() {
        String memberSession = ServiceContext.getCurrentContext().getRequest().getHeader(MdcConstant.MEMBER_SESSION);
        try {
            return RedisUtil.<MemberInfoResult>valueRedis().get(StrUtil.emptyIfNull(memberSession));
        } catch (Exception e) {
            log.error("根据缓存获取会员信息失败");
            return null;
        }
    }

    public static MemberInfoResult getCurrentMember() {
        return getMember();
    }

    public static Long getCurrentMemberId() {
        MemberInfoResult memberInfoResult = getMember();
        if (memberInfoResult != null) {
            return memberInfoResult.getId();
        }
        return null;
    }

    public static MemberInfoResult getRequireCurrentMember() {
        MemberInfoResult memberInfoResult = getMember();
        if (memberInfoResult != null) {
            //把会员的子租户id放进上下文
            ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, memberInfoResult.getSubTenantId());
            return memberInfoResult;
        }
        throw MemberErrorEnum.MEMBER_ERROR_10001.getErrorMeta().getException();
    }

    public static Long getRequireCurrentMemberId() {
        MemberInfoResult memberInfoResult = getMember();
        if (memberInfoResult != null) {
            //把会员的子租户id放进上下文
            ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, memberInfoResult.getSubTenantId());
            return memberInfoResult.getId();
        }
        throw MemberErrorEnum.MEMBER_ERROR_10001.getErrorMeta().getException();
    }

    public static MemberInfoResult getMiniSessionKey() {
        MemberInfoResult memberInfoResult = getMember();
        if (memberInfoResult != null) {
            if (StrUtil.isNotBlank(memberInfoResult.getSessionKey())) {
                return memberInfoResult;
            }
        }
        throw MemberErrorEnum.MEMBER_ERROR_10001.getErrorMeta().getException();
    }

    public static String getOtherAppId() {
        IsvAppInfo isvAppInfo = Convert.convert(IsvAppInfo.class, ServiceContext.getCurrentContext().get("ISV"));
        if (isvAppInfo != null) {
            return isvAppInfo.getOtherAppId();
        }
        return null;
    }

    public static String getConfigValue(ConfigTypeEnum configTypeEnum) {
        String key = "getSystemConfig" + configTypeEnum.getType();
        String value = RedisUtil.<String>valueRedis().get(key);
        if (value == null) {
            value = "";
            SystemConfigMapper systemConfigMapper = SpringUtil.getBean(SystemConfigMapper.class);
            SystemConfig config = systemConfigMapper.selectOne(Wrappers.<SystemConfig>lambdaQuery().eq(SystemConfig::getConfigCode, configTypeEnum.getType()));
            if (config != null) {
                value = StrUtil.nullToEmpty(config.getConfigValue());
            }
        }
        RedisUtil.valueRedis().set(key, StrUtil.nullToEmpty(value), 1, TimeUnit.MINUTES);
        return value;
    }

    public static String getConfigValueWithoutRedis(ConfigTypeEnum configTypeEnum) {
        String value = null;
        SystemConfigMapper systemConfigMapper = SpringUtil.getBean(SystemConfigMapper.class);
        SystemConfig config = systemConfigMapper.selectOne(Wrappers.<SystemConfig>lambdaQuery().eq(SystemConfig::getConfigCode, configTypeEnum.getType()));
        if (config != null) {
            value = StrUtil.nullToEmpty(config.getConfigValue());
        }
        return value;
    }

    public static Long setCryption(String content) {
        SystemCryptionMapper systemCryptionMapper = SpringUtil.getBean(SystemCryptionMapper.class);
        SystemCryption cryption = systemCryptionMapper.selectOne(Wrappers.<SystemCryption>lambdaQuery().eq(SystemCryption::getContent, content));
        if (cryption != null) {
            return cryption.getId();
        } else {
            Long id = getSnowflakeId();
            SystemCryption systemCryption = new SystemCryption();
            systemCryption.setId(id);
            systemCryption.setContent(content);
            systemCryptionMapper.insert(systemCryption);
            return id;
        }
    }

    public static String getCryption(Long id) {
        return RedisUtil.valueGet("getCryption" + id, 7, () -> {
            SystemCryptionMapper systemCryptionMapper = SpringUtil.getBean(SystemCryptionMapper.class);
            SystemCryption cryption = systemCryptionMapper.selectById(id);
            if (cryption != null) {
                return cryption.getContent();
            }
            return "";
        });
    }

    public static long getTenantIncId(String key) {
        return getIncId(key, getCurrentTenantId(), null);
    }

    public static long getSubTenantIncId(String key) {
        return getIncId(key, getCurrentTenantId(), getCurrentSubTenantId());
    }

    public static long getIncId(String key) {
        return getIncId(key, null, null);
    }

    /**
     * 获取租户唯一自增类型的字符id,返回格式key+num位自增,举例如下
     * <pre>
     * MdcUtil.getIncIdStr("1901", 2), 第3次调用, = 190103
     * MdcUtil.getIncIdStr("1901", 5), 第3次调用, = 190100003
     * MdcUtil.getIncIdStr("1901", 2), 第113次调用(自增位数会突破2), = 1901113
     * </pre>
     *
     * @param key 自增主键key
     * @param num 自增位数
     * @return 自增型字符串id
     */
    public static String getTenantIncIdStr(String prefix, String key, int num) {
        return key + String.format("%0" + num + "d", getTenantIncId(prefix + key));
    }

    /**
     * 获取子租户唯一自增类型的字符id,返回格式key+num位自增,举例如下
     * <pre>
     * MdcUtil.getIncIdStr("1901", 2), 第3次调用, = 190103
     * MdcUtil.getIncIdStr("1901", 5), 第3次调用, = 190100003
     * MdcUtil.getIncIdStr("1901", 2), 第113次调用(自增位数会突破2), = 1901113
     * </pre>
     *
     * @param key 自增主键key
     * @param num 自增位数
     * @return 自增型字符串id
     */
    public static String getSubTenantIncIdStr(String prefix, String key, int num) {
        return key + String.format("%0" + num + "d", getSubTenantIncId(prefix + key));
    }

    /**
     * 获取系统唯一自增类型的字符id,返回格式key+num位自增,举例如下
     * <pre>
     * MdcUtil.getIncIdStr("1901", 2), 第3次调用, = 190103
     * MdcUtil.getIncIdStr("1901", 5), 第3次调用, = 190100003
     * MdcUtil.getIncIdStr("1901", 2), 第113次调用(自增位数会突破2), = 1901113
     * </pre>
     *
     * @param key 自增主键key
     * @param num 自增位数
     * @return 自增型字符串id
     */
    public static String getIncIdStr(String prefix, String key, int num) {
        return key + String.format("%0" + num + "d", getIncId(prefix + key));
    }

    private static long getIncId(String key, Long tenantId, Long subTenantId) {
        SystemIncrementMapper systemIncrementMapper = SpringUtil.getBean(SystemIncrementMapper.class);
        Future<Long> result = getTtlExecutorService().submit(() -> RedisUtil.syncLoad("getIncIdLock" + tenantId + subTenantId, () -> {
            SystemIncrement increment = systemIncrementMapper.selectOne(Wrappers.<SystemIncrement>lambdaQuery()
                    .eq(SystemIncrement::getPkKey, key)
                    .eq(tenantId != null, SystemIncrement::getTenantId, tenantId)
                    .eq(subTenantId != null, SystemIncrement::getSubTenantId, subTenantId)
            );
            long id;
            if (increment == null) {
                id = 1L;
                increment = new SystemIncrement();
                increment.setPkKey(key);
                increment.setPkValue(id);
                increment.setTenantId(tenantId);
                increment.setSubTenantId(subTenantId);
                systemIncrementMapper.insert(increment);
            } else {
                id = increment.getPkValue() + 1;
                SystemIncrement update = new SystemIncrement();
                update.setId(increment.getId());
                update.setPkValue(id);
                systemIncrementMapper.updateById(update);
            }
            return id;
        }));
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceException("创建自增id失败");
        }
    }

    public static String getMemberAppId(MemberInfo memberInfo) {
        //若设置了统一会员,返回统一会员的appId
        SystemTenantConfigService systemTenantConfigService = SpringUtil.getBean(SystemTenantConfigService.class);
        SystemTenantConfigResult result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.UNIFIEDMEMBER);
        if (result != null && result.getUnifiedMemberConfig() != null && result.getUnifiedMemberConfig().getUnified()) {
            UnifiedMemberConfig unifiedMemberConfig = result.getUnifiedMemberConfig();
            if (unifiedMemberConfig.getUnified() && unifiedMemberConfig.getUnifiedCol() == UnifiedColNameEnum.APPID) {
                return unifiedMemberConfig.getUnifiedValue();
            }
        }
        SourceEnum source = memberInfo.getSource();
        if (source == SourceEnum.GZH || source == SourceEnum.MINI) {
            return memberInfo.getWxAppId();
        }
        return StrUtil.isNotBlank(memberInfo.getAliAppId()) ? memberInfo.getAliAppId() : memberInfo.getWxAppId();
    }

    public static void dynaBeanSafeSet(DynaBean bean, String fieldName, Object value) {
        try {
            bean.set(fieldName, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static <T> T setMemberInfo(T object, MemberInfo memberInfo) {
        DynaBean bean = DynaBean.create(object);
        dynaBeanSafeSet(bean, "memberId", memberInfo.getId());
        dynaBeanSafeSet(bean, "phoneNumber", memberInfo.getPhoneNumber());
        dynaBeanSafeSet(bean, "nickName", memberInfo.getNickName());
        dynaBeanSafeSet(bean, "avatarUrl", memberInfo.getAvatarUrl());
        SourceEnum source = memberInfo.getSource();
        if (source == SourceEnum.GZH || source == SourceEnum.MINI) {
            dynaBeanSafeSet(bean, "appId", memberInfo.getWxAppId());
            dynaBeanSafeSet(bean, "openId", memberInfo.getOpenId());
        }
        return bean.getBean();
    }

    public static <T> T setOrganInfo(T object) {
        DynaBean bean = DynaBean.create(object);
        String designateOrgIds = bean.safeGet("designateOrgIds");
        if (StrUtil.isNotBlank(designateOrgIds)) {
            List<Long> designateOrgIdList = CollUtil.toList(StrUtil.split(designateOrgIds, ",")).stream().map((Convert::toLong)).collect(Collectors.toList());
            List<PShopInfo> pShopInfoList = (List<PShopInfo>) ServiceContext.getCurrentContext().get("organInfoList");
            if (pShopInfoList == null) {
                ShopConsumer shopConsumer = SpringUtil.getBean(ShopConsumer.class);
                pShopInfoList = shopConsumer.listShopInfoByIds(designateOrgIdList);
                ServiceContext.getCurrentContext().set("organInfoList", pShopInfoList);
            }
            designateOrgIds = CollUtil.join(pShopInfoList.stream().map(PShopInfo::getId).filter(ObjectUtil::isNotNull)
                    .collect(Collectors.toList()), ",");
            String designateOrgNames = CollUtil.join(pShopInfoList.stream().map(PShopInfo::getName).filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList()), ",");
            dynaBeanSafeSet(bean, "designateOrgIds", designateOrgIds);
            dynaBeanSafeSet(bean, "designateOrgNames", designateOrgNames);
        }
        return bean.getBean();
    }

    public static void publishTransactionalEvent(ApplicationEvent event) {
        SpringUtil.getApplicationContext().publishEvent(event);
    }
}
