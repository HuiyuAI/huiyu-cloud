package com.huiyu.service.core.sd.submit.chooseStrategy.constant;

/**
 * @author wAnG
 * @Date 2023-06-28  00:58
 */
public enum ExecStrategyTypeEnum {

    POINT(1, "按照工作量计算");

    private Integer code;

    private String desc;

    ExecStrategyTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
