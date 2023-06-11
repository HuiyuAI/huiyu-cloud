package com.huiyu.service.core.constant;

public enum StateEnum implements BaseEnum<Integer>{

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer dictKey;

    private String desc;


    StateEnum(Integer dictKey, String desc) {
        this.dictKey = dictKey;
        this.desc = desc;
    }

    @Override
    public Integer getDictKey() {
        return dictKey;
    }

    public void setDictKey(Integer dictKey) {
        this.dictKey = dictKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}