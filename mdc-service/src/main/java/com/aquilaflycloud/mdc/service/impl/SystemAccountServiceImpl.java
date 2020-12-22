package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilafly.easypay.config.EasyPayConfig;
import com.aquilafly.easypay.util.EasyPayUtil;
import com.aquilaflycloud.ajbcloud.config.AjbCloudConfig;
import com.aquilaflycloud.ajbcloud.req.PostAddReq;
import com.aquilaflycloud.ajbcloud.resp.PostAddResp;
import com.aquilaflycloud.ajbcloud.util.AjbCloudUtil;
import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import com.aquilaflycloud.mdc.enums.system.AccountTypeEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.mapper.ParkingAjbInterfaceRecordMapper;
import com.aquilaflycloud.mdc.mapper.SystemAccountConfigMapper;
import com.aquilaflycloud.mdc.model.parking.ParkingAjbInterfaceRecord;
import com.aquilaflycloud.mdc.model.system.SystemAccountConfig;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.AjbCloudAccountResult;
import com.aquilaflycloud.mdc.result.system.EasyPayAccountResult;
import com.aquilaflycloud.mdc.result.system.TencentPositionAccountResult;
import com.aquilaflycloud.mdc.service.SystemAccountService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.taobao.api.ApiException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SystemAccountServiceImpl
 *
 * @author star
 * @date 2019-12-04
 */
@Service
@DependsOn("springUtil")
public class SystemAccountServiceImpl implements SystemAccountService {
    @Resource
    private SystemAccountConfigMapper systemAccountConfigMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<SystemAccountConfig> listAccount(AccountListParam param) {
        return systemAccountConfigMapper.selectList(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(param.getAccountType() != null, SystemAccountConfig::getAccountType, param.getAccountType())
                .eq(param.getAccountSubType() != null, SystemAccountConfig::getAccountSubType, param.getAccountSubType())
        );
    }

    private int saveAccount(Object param, AccountTypeEnum accountType, AccountSubTypeEnum accountSubType) {
        SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.selectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(SystemAccountConfig::getAccountType, accountType)
                .eq(accountSubType != null, SystemAccountConfig::getAccountSubType, accountSubType)
        );
        int count;
        if (systemAccountConfig == null) {
            systemAccountConfig = new SystemAccountConfig();
            systemAccountConfig.setAccountContent(JSONUtil.toJsonStr(param));
            systemAccountConfig.setAccountType(accountType);
            systemAccountConfig.setAccountSubType(accountSubType);
            count = systemAccountConfigMapper.insert(systemAccountConfig);
        } else {
            SystemAccountConfig update = new SystemAccountConfig();
            update.setId(systemAccountConfig.getId());
            update.setAccountContent(JSONUtil.toJsonStr(param));
            count = systemAccountConfigMapper.updateById(update);
        }
        return count;
    }

    @Override
    public TencentPositionAccountResult getTencentPositionAccount() {
        Long tenantId = MdcUtil.getCurrentTenantId();
        return RedisUtil.valueGet("getTPAccount" + tenantId, () -> {
            SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.selectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                    .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.TENCENTPOSITION));
            if (systemAccountConfig == null) {
                throw new ServiceException("腾讯位置服务账号不存在");
            }
            TencentPositionAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), TencentPositionAccountResult.class);
            result.setId(systemAccountConfig.getId());
            return result;
        });
    }

    @Override
    public void saveTencentPositionAccount(TencentPositionAccountSaveParam param) {
        int count = saveAccount(param, AccountTypeEnum.TENCENTPOSITION, null);
        if (count <= 0) {
            throw new ServiceException("保存腾讯位置服务账号失败");
        }
        Long tenantId = MdcUtil.getCurrentTenantId();
        RedisUtil.redis().delete("getTPAccount" + tenantId);
    }

    @Override
    public EasyPayAccountResult getEasyPayAccount(EasyPayAccountGetParam param) {
        Long tenantId = MdcUtil.getCurrentTenantId();
        String key = "getEasyAccount" + tenantId + param.getAccountSubType().getType();
        return RedisUtil.valueGet(key, () -> {
            SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.selectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                    .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.EASYPAY)
                    .eq(SystemAccountConfig::getAccountSubType, param.getAccountSubType())
            );
            if (systemAccountConfig == null) {
                throw new ServiceException("惠云支付账号不存在");
            }
            EasyPayAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), EasyPayAccountResult.class);
            result.setId(systemAccountConfig.getId());
            return result;
        });
    }

    @Override
    public void saveEasyPayAccount(EasyPayAccountSaveParam param) {
        EasyPayAccountResult account = new EasyPayAccountResult();
        BeanUtil.copyProperties(param, account);
        EasyPayConfig config = new EasyPayConfig();
        BeanUtil.copyProperties(account, config);
        config.setConfigId(MdcUtil.getCurrentTenantId().toString() + param.getAccountSubType().getType());
        EasyPayUtil.addConfig(config);
        int count = saveAccount(param, AccountTypeEnum.EASYPAY, param.getAccountSubType());
        if (count <= 0) {
            throw new ServiceException("保存惠云支付账号失败");
        }
        Long tenantId = MdcUtil.getCurrentTenantId();
        String key = "getEasyAccount" + tenantId + param.getAccountSubType().getType();
        RedisUtil.redis().delete(key);
    }

    @PostConstruct
    public void initEasyPay() {
        EasyPayUtil.initCache(redisTemplate);
        //设置接口地址,默认是生产环境地址
        String apiDomain = MdcUtil.getConfigValueWithoutRedis(ConfigTypeEnum.EASYPAY_API_DOMAIN_NAME);
        if (StrUtil.isNotBlank(apiDomain)) {
            EasyPayUtil.setApiDomain(apiDomain);
        }
        //设置支付回调地址
        String payNotifyUrl = StrBuilder.create().append(MdcUtil.getConfigValueWithoutRedis(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/unionPayNotify").toString();
        EasyPayUtil.setPayNotifyUrl(payNotifyUrl);
        //设置退款回调地址
        String refundNotifyUrl = StrBuilder.create().append(MdcUtil.getConfigValueWithoutRedis(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/refundNotify").toString();
        EasyPayUtil.setRefundNotifyUrl(refundNotifyUrl);
        List<SystemAccountConfig> configList = systemAccountConfigMapper.normalSelectList(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.EASYPAY)
        );
        for (SystemAccountConfig systemAccountConfig : configList) {
            EasyPayAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), EasyPayAccountResult.class);
            EasyPayConfig config = new EasyPayConfig();
            BeanUtil.copyProperties(result, config);
            config.setConfigId(systemAccountConfig.getTenantId().toString() + systemAccountConfig.getAccountSubType().getType());
            EasyPayUtil.addConfig(config);
        }
    }

    @Override
    public SystemAccountConfig getAjbCloudAccountByParkCode(String parkCode) {
        return RedisUtil.valueGet("getAJBAccount" + parkCode, 30, () -> {
            SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.normalSelectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                    .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.AJBCLOUD)
                    .like(SystemAccountConfig::getAccountContent, "\"parkCode\":\"" + parkCode + "\"")
                    .last("limit 1")
            );
            if (systemAccountConfig == null) {
                throw new ServiceException("安居宝账号不存在");
            }
            return systemAccountConfig;
        });
    }

    @Override
    public AjbCloudAccountResult getAjbCloudAccount() {
        Long tenantId = MdcUtil.getCurrentTenantId();
        return RedisUtil.valueGet("getAJBAccount" + tenantId, () -> {
            SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.normalSelectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                    .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.AJBCLOUD)
                    .eq(SystemAccountConfig::getTenantId, tenantId)
                    .last("limit 1")
            );
            if (systemAccountConfig == null) {
                throw new ServiceException("安居宝账号不存在");
            }
            AjbCloudAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), AjbCloudAccountResult.class);
            result.setId(systemAccountConfig.getId());
            return result;
        });
    }

    @Override
    public void saveAjbCloudAccount(AjbCloudAccountSaveParam param) {
        AjbCloudAccountResult account = new AjbCloudAccountResult();
        BeanUtil.copyProperties(param, account);
        AjbCloudConfig cloudConfig = new AjbCloudConfig();
        BeanUtil.copyProperties(account, cloudConfig);
        cloudConfig.setConfigId(MdcUtil.getCurrentTenantId().toString());
        AjbCloudUtil.addConfig(cloudConfig, true);
        List<AjbCloudAccountResult.PostPathInfo> postPathList = new ArrayList<>();
        for (PostAddReq.Type type : PostAddReq.Type.values()) {
            String postPath = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                    .append("/rest/").append(MdcUtil.getServerName()).append(type.getPostPath()).toString();
            String sucFlag = "success";
            PostAddReq postAddReq = new PostAddReq().setPostPath(postPath).setSucFlag(sucFlag).setType(type);
            PostAddResp resp = AjbCloudUtil.Post.addPost(MdcUtil.getCurrentTenantId().toString(), postAddReq);
            if (resp.getStatus()) {
                AjbCloudAccountResult.PostPathInfo postPathInfo = new AjbCloudAccountResult.PostPathInfo();
                postPathInfo.setPostPath(postPath);
                postPathInfo.setType(type.name());
                postPathInfo.setSucFlag(sucFlag);
                postPathList.add(postPathInfo);
            }
        }
        account.setSecretId(cloudConfig.getSecretId());
        account.setMerchantId(cloudConfig.getMerchantId());
        account.setPostPathList(postPathList);
        int count = saveAccount(account, AccountTypeEnum.AJBCLOUD, null);
        if (count <= 0) {
            throw new ServiceException("保存安居宝账号失败");
        }
        Long tenantId = MdcUtil.getCurrentTenantId();
        RedisUtil.redis().delete("getAJBAccount" + tenantId);
        RedisUtil.redis().delete("getAJBAccount" + param.getParkCode());
    }

    @PostConstruct
    public void initAjbCloud() {
        AjbCloudUtil.initCache(redisTemplate);
        AjbCloudUtil.initHandler((url, handlerResult) -> {
            if (handlerResult != null) {
                if ((handlerResult instanceof JSONObject && !((JSONObject) handlerResult).getBool("status"))
                        || handlerResult instanceof String) {
                    MdcUtil.getTtlExecutorService().submit(() -> {
                        ParkingAjbInterfaceRecordMapper service = SpringUtil.getBean(ParkingAjbInterfaceRecordMapper.class);
                        ParkingAjbInterfaceRecord record = new ParkingAjbInterfaceRecord();
                        String resultContent = null, code = null;
                        if (handlerResult instanceof JSONObject) {
                            JSONObject result = (JSONObject) handlerResult;
                            record.setStatus(result.getBool("status"));
                            record.setCode(result.getStr("code"));
                            record.setMsg(result.getStr("msg"));
                            code = result.getStr("code");
                            resultContent = result.toString();
                        } else if (handlerResult instanceof String) {
                            resultContent = (String) handlerResult;
                        }
                        record.setParamContent(url);
                        record.setResultContent(resultContent);
                        service.insert(record);
                        //0179代表车辆不在场,0190代表免费卡不需缴费,不需发送警报
                        if (!StrUtil.equalsAny(code, "0179", "0190", "0208")) {
                            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=77bf33830656ad597be0648f4e0542d5ea41a7985854fd39787d074b6fb61671");
                            OapiRobotSendRequest request = new OapiRobotSendRequest();
                            request.setMsgtype("text");
                            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                            text.setContent("安居宝停车系统调用接口:" + url + " ," + DateTime.now().toString() + ",接口结果:" + resultContent);
                            request.setText(text);
                            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
                            at.setAtMobiles(Arrays.asList("13650989413", "18316023928", "13632334651"));
                            request.setAt(at);
                            try {
                                OapiRobotSendResponse response = client.execute(request);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        List<SystemAccountConfig> configList = systemAccountConfigMapper.normalSelectList(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.AJBCLOUD)
        );
        for (SystemAccountConfig systemAccountConfig : configList) {
            AjbCloudAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), AjbCloudAccountResult.class);
            AjbCloudConfig cloudConfig = new AjbCloudConfig();
            BeanUtil.copyProperties(result, cloudConfig);
            cloudConfig.setConfigId(systemAccountConfig.getTenantId().toString());
            AjbCloudUtil.addConfig(cloudConfig, true);
        }
    }
}
