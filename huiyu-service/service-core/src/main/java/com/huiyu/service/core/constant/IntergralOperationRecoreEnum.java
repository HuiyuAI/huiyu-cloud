package com.huiyu.service.core.constant;

public enum IntergralOperationRecoreEnum implements BaseEnum<Integer>{

    REDUCE(0,"减少"),
    ADD(1,"增加");

    private Integer dictKey;

    private String desc;


    IntergralOperationRecoreEnum(Integer dictKey, String desc) {
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
