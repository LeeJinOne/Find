package com.find.find;

/**
 * Created by Administrator on 2017-04-21.
 */

public class ListViewItem {
    private String titleStr;
    String descStr;

    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
