package com.momu.tale.item;

/**
 * Created by songm on 2016-12-30.
 */

public class SetupItem {
    private String title;
    private String sub;
    private boolean isSync;

    public String getTitle() {
        return title;
    }

    public String getSub() {
        return sub;
    }

    public boolean getIsSync(){return isSync;}

    public SetupItem(String title, String sub){
        this.title = title;
        this.sub =sub;
    }

    public SetupItem(String title, boolean isSync){
        this.title=title;
        this.isSync =isSync;
    }

}
