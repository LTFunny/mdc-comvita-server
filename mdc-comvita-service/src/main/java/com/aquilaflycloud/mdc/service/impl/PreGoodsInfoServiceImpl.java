package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.mapper.PreGoodsInfoMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.ChangeGoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.GoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.PreGoodsInfoListParam;
import com.aquilaflycloud.mdc.param.pre.ReturnGoodsInfoParam;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.PreGoodsInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zouliyong
 */
@Service
@Slf4j
public class PreGoodsInfoServiceImpl implements PreGoodsInfoService {
    @Resource
    private PreGoodsInfoMapper preGoodsInfoMapper;

    @Resource
    private FolksonomyService folksonomyService;

    @Override
    public IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param) {
        IPage<PreGoodsInfo> list=preGoodsInfoMapper.selectPage(param.page(), Wrappers.<PreGoodsInfo>lambdaQuery()
                .like( StrUtil.isNotBlank(param.getGoodsName()),PreGoodsInfo::getGoodsName, param.getGoodsName())
                .like( param.getFolksonomyId()!=null,PreGoodsInfo::getFolksonomyId, param.getFolksonomyId())
                .eq( StrUtil.isNotBlank(param.getGoodsState().getName()),PreGoodsInfo::getGoodsState, param.getGoodsState())
                .eq( StrUtil.isNotBlank(param.getGoodsType().getName()),PreGoodsInfo::getGoodsType, param.getGoodsType())
                .eq( StrUtil.isNotBlank(param.getGoodsCode()),PreGoodsInfo::getGoodsCode, param.getGoodsCode())
        );
        return list;
    }

    @Override
    public void addPreGoodsInfo(ReturnGoodsInfoParam param) {
        //判读是否存在名称和编号
        if(StringUtils.isNotBlank(getCount(param.getGoodsName(),param.getGoodsCode()))){
            throw new SecurityException(getCount(param.getGoodsName(),param.getGoodsCode()));
        }
        String tagId=null;
        if(CollectionUtils.isEmpty(param.getFolksonomyIds())) {
             tagId=param.getFolksonomyIds().toString();
        }
        PreGoodsInfo preGoodsInfo=new PreGoodsInfo();
        preGoodsInfo.setGoodsCode( param.getGoodsCode());
        preGoodsInfo.setGoodsName(param.getGoodsName());
        preGoodsInfo.setGoodsType(param.getGoodsType());
        preGoodsInfo.setGoodsPrice( param.getGoodsPrice());
        preGoodsInfo.setGoodsDescription(param.getGoodsDescription());
        preGoodsInfo.setGoodsState(param.getGoodsState().getType());
        preGoodsInfo.setFolksonomyId(tagId);
        int count = preGoodsInfoMapper.insert(preGoodsInfo);
        if (count == 1) {
            //保存业务功能标签
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREGOODS, preGoodsInfo.getId(), param.getFolksonomyIds());
            log.info("保存商品信息成功");
        } else {
            throw new ServiceException("保存商品信息失败: count=" + count);
        }
    }

    @Override
    public void editPreGoodsInfo(ReturnGoodsInfoParam param) {
        if(param.getId()==null) {
            throw new ServiceException("修改的数据主键未传" );
        }
        PreGoodsInfo info=  preGoodsInfoMapper.selectById(param.getId());
        //判读是否存在名称和编号
        if(!param.getGoodsCode().equals(info.getGoodsCode())){
            if(StringUtils.isNotBlank(getCount(null,param.getGoodsCode()))){
                throw new SecurityException(getCount(null,param.getGoodsCode()));
            }
        }
        if(!param.getGoodsName().equals(info.getGoodsName())){
            if(StringUtils.isNotBlank(getCount(param.getGoodsName(),null))){
                throw new SecurityException(getCount(param.getGoodsName(),null));
            }
        }

        BeanUtil.copyProperties(param, info,"id","goodsState");
        if(CollectionUtils.isEmpty(param.getFolksonomyIds())) {
            String tagId=param.getFolksonomyIds().toString();
            info.setFolksonomyId(tagId);
        }
        preGoodsInfoMapper.updateById(info);
        log.info("修改商品信息成功");
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREGOODS, info.getId(), param.getFolksonomyIds());

    }

    @Override
    public void changeGoodsType(ChangeGoodsInfoParam param) {
        PreGoodsInfo info=  preGoodsInfoMapper.selectById(param.getId());
        info.setGoodsState(param.getGoodsState().getType());
        preGoodsInfoMapper.updateById(info);
    }

    @Override
    public ReturnGoodsInfoParam goodsData(GoodsInfoParam param) {
        PreGoodsInfo info=  preGoodsInfoMapper.selectById(param.getId());
        ReturnGoodsInfoParam returnGoodsInfoParam=new ReturnGoodsInfoParam();
        BeanUtil.copyProperties(info, returnGoodsInfoParam);
        if(StringUtils.isNotBlank(info.getFolksonomyId())){
            String[] tagId=info.getFolksonomyId().split(",");
            List<Long> list=new ArrayList<>();
            for(String id:tagId){
                list.add(Long.parseLong(id));
            }
            returnGoodsInfoParam.setFolksonomyIds(list);
        }
        return returnGoodsInfoParam;
    }

    //查询是否存在相应的数据
    private String getCount(String name,String code ){
        if(StringUtils.isNotBlank(name)){
            List<PreGoodsInfo>  list =   preGoodsInfoMapper.selectList( Wrappers.<PreGoodsInfo>lambdaQuery()
                    .eq( name!=null,PreGoodsInfo::getGoodsName, name)
            );
            if(list.size()>0){
                return "商品名称已存在，请修改再提交";
            }
        }
     if(StringUtils.isNotBlank(code)){
         List<PreGoodsInfo>  list2 =   preGoodsInfoMapper.selectList( Wrappers.<PreGoodsInfo>lambdaQuery()
                 .eq( code!=null,PreGoodsInfo::getGoodsState, code)
         );
         if(list2.size()>0){
             return "商品编号已存在，请修改再提交";
         }
     }
        return null;
    }
}
