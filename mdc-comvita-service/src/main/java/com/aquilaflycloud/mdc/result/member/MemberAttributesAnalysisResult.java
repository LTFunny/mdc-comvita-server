package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemberAttributesAnalysisResult
 *
 * @author Zengqingjie
 * @date 2020-05-27
 */
@Data
public class MemberAttributesAnalysisResult {
    @ApiModelProperty(value = "性别数据")
    List<SexAttributes> sex;

    @ApiModelProperty(value = "年龄x轴数据")
    List<String> ageX;

    @ApiModelProperty(value = "年龄y轴数据")
    List<Long> ageY;

    @ApiModelProperty(value = "等级x轴数据")
    List<String> gradeX;

    @ApiModelProperty(value = "等级y轴数据")
    List<Long> gradeY;

    @Data
    public static class SexAttributes implements Serializable {
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "数量")
        private Long value;

        public void setSexAttributes(String name, Long value) {
            this.name = name;
            this.value = value;
        }
    }

    public MemberAttributesAnalysisResult(List<SexAttributes> sex, List<String> ageX, List<Long> ageY, List<String> gradeX, List<Long> gradeY) {
        this.sex = sex;
        this.ageX = ageX;
        this.ageY = ageY;
        this.gradeX = gradeX;
        this.gradeY = gradeY;
    }

    public MemberAttributesAnalysisResult() {
    }
}
