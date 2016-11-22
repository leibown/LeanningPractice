package com.leibown.practiceprojects;

/**
 * Created by Administrator on 2016/11/22.
 */

public class CobWebData {
    private String title;
    private double data;

    public CobWebData(String title, double data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
