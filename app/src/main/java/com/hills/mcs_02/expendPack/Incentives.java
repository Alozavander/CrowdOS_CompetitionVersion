package com.hills.mcs_02.expendPack;

import com.hills.mcs_02.dataBeans.Task;
import com.hills.mcs_02.dataBeans.Bean_UserAccount;

import java.security.PublicKey;

public interface Incentives {
    /*
     * @param task  任务类，包含任务名称、任务发布时间、发布者、激励金、数量等相关信息
     * @param account 账户类，包含用户名等相关信息
     */
    public void incentive_get(Task task, Bean_UserAccount account);
    public void incentive_pay(Task task, Bean_UserAccount account);

    /*
     * @param account 账户类，包含用户名等相关信息
     * @param key 私钥，用于检验解密服务器端传回的验证信息
     */
    public String incentive_verify(Bean_UserAccount account, PublicKey key);
}
