package com.example.zuoye.bean;

import java.util.List;

/**
 * Created by 蒋順聪 on 2017/9/6.
 */

public class PindaoJson {
    private String name;
    private boolean isSelect;

    @Override
    public String toString() {
        return "PindaoJson{" +
                "name='" + name + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public PindaoJson() {

    }

    public PindaoJson(String name, boolean isSelect) {

        this.name = name;
        this.isSelect = isSelect;
    }
}
