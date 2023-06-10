package com.huiyu.service.core.constant;

public enum IntergralSoureceRecoreEnum implements BaseEnum<String> {
    REGISTER(0,"register","注册");


    private Integer code;

    private String dictKey;

    private String desc;

    IntergralSoureceRecoreEnum(Integer code, String dictKey, String desc) {
        this.code = code;
        this.dictKey = dictKey;
        this.desc = desc;
    }

    @Override
    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
