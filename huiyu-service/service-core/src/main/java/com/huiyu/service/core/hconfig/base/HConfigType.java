package com.huiyu.service.core.hconfig.base;

/**
 * @author wAnG
 * @Date 2023-06-28  01:09
 */
public enum HConfigType {

    YAML("yaml"),
    PROPERTIES("properties"),
    JSON("json");

    private String type;


    HConfigType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
