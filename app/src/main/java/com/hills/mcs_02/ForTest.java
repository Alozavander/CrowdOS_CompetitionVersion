package com.hills.mcs_02;


/** Providing the jump method from fragments to other sub-pages. */
public interface ForTest {
    public void jumpToSearchActivity();
    public void jumpToLoginPage();
    public void jumpToTaskDetailActivity(String taskGson);
    public void jumpToEditInfo();
}
