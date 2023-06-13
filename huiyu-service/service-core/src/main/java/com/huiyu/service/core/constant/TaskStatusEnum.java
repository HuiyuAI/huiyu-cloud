package com.huiyu.service.core.constant;

public enum TaskStatusEnum implements BaseEnum<Integer> {
    UNEXECUTED(0, "未执行"),
    EXECUTED(1, "已执行"),
    IN_QUEUE(2, "在队列中");


    private Integer dictKey;

    private String desc;

    TaskStatusEnum(Integer dictKey, String desc) {
        this.dictKey = dictKey;
        this.desc = desc;
    }

    @Override
    public Integer getDictKey() {
        return dictKey;
    }

    void setDictKey(Integer dictKey) {
        this.dictKey = dictKey;
    }

    String getDesc() {
        return desc;
    }

    void setDesc(String desc) {
        this.desc = desc;
    }

}
