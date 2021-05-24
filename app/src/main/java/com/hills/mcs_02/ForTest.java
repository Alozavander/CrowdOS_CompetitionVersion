package com.hills.mcs_02;


//为Publish碎片向Home页面通信，暂时作为演示存在
public interface ForTest {
    public void buttonAddItem();
    //取巧做法，需要改动
    public void jumpTo2rdPage(String page, int position);
    public void jumpToSearchActivity();
    public void jumpToLoginPage();
    public void jumpToTaskDetailActivity(String taskGson);
    public void jumpToFuncSportActivity();
    public void jumpToFuncFoodActivity();
    public void jumpToEditInfo();
}
