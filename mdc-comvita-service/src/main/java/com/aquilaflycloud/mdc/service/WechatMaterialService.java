package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.wechat.MaterialGetParam;
import com.aquilaflycloud.mdc.param.wechat.MaterialListParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialVideoInfoResult;

/**
 * WechatMaterialService
 *
 * @author star
 * @date 2019-10-09
 */
public interface WechatMaterialService {

    IPage<WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> pageMaterialNews(MaterialListParam param);

    IPage<WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> pageMaterial(MaterialListParam param);

    WxMpMaterialNews getMaterialNews(MaterialGetParam param);

    WxMpMaterialVideoInfoResult getMaterialVideo(MaterialGetParam param);

    BaseResult<String> getMaterial(MaterialGetParam param);
}

