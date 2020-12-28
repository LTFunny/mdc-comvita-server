package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.RichtextDescriptionMapper;
import com.aquilaflycloud.mdc.model.richtext.RichtextDescriptionInfo;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionEditParam;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionGetParam;
import com.aquilaflycloud.mdc.service.RichtextDescriptionService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RichtextDescriptionServiceImpl
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
@Service
public class RichtextDescriptionServiceImpl implements RichtextDescriptionService {

    @Resource
    private RichtextDescriptionMapper richtextDescriptionMapper;

    @Override
    public RichtextDescriptionInfo getByType(RichtextDescriptionGetParam param) {
        RichtextDescriptionInfo richtextDescriptionInfo = richtextDescriptionMapper.selectOne(Wrappers.<RichtextDescriptionInfo>lambdaQuery()
                .eq(RichtextDescriptionInfo::getType, param.getType())
        );
        if (null != richtextDescriptionInfo) {
            return richtextDescriptionInfo;
        }

        return null;
    }

    @Override
    public RichtextDescriptionInfo edit(RichtextDescriptionEditParam param) {
        //不包含id标记
        boolean noHasId = ObjectUtil.isNull(param.getId()) ? true : false;

        //无id时校验type和内容都不能为空
        if (noHasId && (ObjectUtil.isNull(param.getType()) || StrUtil.isBlank(param.getContent()))) {
            throw new ServiceException("描述类型和内容都不能为空");
        }

        //无id时校验数据库是否存在记录了
        if (noHasId) {
            RichtextDescriptionInfo info = richtextDescriptionMapper.selectOne(Wrappers.<RichtextDescriptionInfo>lambdaQuery()
                    .eq(RichtextDescriptionInfo::getType, param.getType())
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam())
            );

            if (ObjectUtil.isNotNull(info)) {
                throw new ServiceException("已存在描述记录，请刷新重试");
            }
        }

        //无id保存记录
        RichtextDescriptionInfo result = new RichtextDescriptionInfo();
        BeanUtil.copyProperties(param, result);

        int count;

        if (noHasId) {
            Long id = MdcUtil.getSnowflakeId();
            result.setId(id);
            count = richtextDescriptionMapper.insert(result);
        } else {
            count = richtextDescriptionMapper.updateById(result);
            result = richtextDescriptionMapper.selectById(param.getId());
        }

        if (count <= 0) {
            throw new ServiceException("更新失败，请刷新重试");
        }

        return result;
    }
}
