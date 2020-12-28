package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.exchange.ExchangeSpecValueInfo;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueAddParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueGetParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValuePageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ExchangeSpecValueInfoService {

    IPage<ExchangeSpecValueInfo> page(ExchangeSpecValuePageParam param);

    ExchangeSpecValueInfo add(ExchangeSpecValueAddParam param);

    ExchangeSpecValueInfo get(ExchangeSpecValueGetParam param);
}
