package com.huiyu.service.core.utils;

import javafx.beans.NamedArg;
import javafx.util.Pair;

import java.io.Serializable;

/**
 * @author wAnG
 * @Date 2023-06-19  01:39
 */
public class NewPair<K, V> implements Serializable {

    private K key;


    public K getKey() {
        return key;
    }


    private V value;


    public V getValue() {
        return value;
    }


    public NewPair(@NamedArg("key") K key, @NamedArg("value") V value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public String toString() {
        return key + "=" + value;
    }


    @Override
    public int hashCode() {

        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            NewPair pair = (NewPair) o;
            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }

}
