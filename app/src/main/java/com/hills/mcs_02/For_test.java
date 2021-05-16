package com.hills.mcs_02;


//为Publish碎片向Home页面通信，暂时作为演示存在
public interface For_test {
    public void button_AddItem();
    //取巧做法，需要改动
    public void jump_to_2rdPage(String page, int position);
    public void jump_to_SearchActivity();
    public void jump_to_loginPage();
    public void jump_to_TaskDetailActivity(String taskGson);
    public void jump_to_func_sportActivity();
    public void jump_to_func_foodActivity();
    public void jump_to_editInfo();
}
