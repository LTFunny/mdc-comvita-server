package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.information.Information;
import com.aquilaflycloud.mdc.param.information.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * InformationService
 *
 * @author star
 * @date 2020-03-07
 */
public interface InformationService {
    IPage<Information> pageInfo(InfoPageParam param);

    Information getInfo(InfoGetParam param);

    Information getInfo(InfoOneGetParam param);

    void addInfo(InfoAddParam param);

    void editInfo(InfoEditParam param);

    void toggleInfo(InfoGetParam param);

    void deleteInfo(InfoGetParam param);

    List<Information> listInformation(InfoListParam param);

    Information getImportantestInfo(InfoListParam param);
}
