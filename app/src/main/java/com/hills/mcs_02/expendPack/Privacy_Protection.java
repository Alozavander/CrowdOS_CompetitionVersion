package com.hills.mcs_02.expendPack;

import javax.crypto.spec.SecretKeySpec;

//隐私加密功能接口，实现本地化存储数据的对称加密，拓展多种
public interface Privacy_Protection {
    /*
     * @param data 明文或加密数据
     * @param MODE 两种参数,ENCRYPT_MODE或者DECRYPT_MODE
     * @param ENCRYPTION_MODE 加密算法的种类
     * @param key 密钥，分为String类型和SecretKeySpec类
     */
    public String data_EncryptOrDecrypt(String data, int MODE,int ENCRYPTION_MODE, SecretKeySpec key);
    public String data_EncryptOrDecrypt(String data, int MODE,int ENCRYPTION_MODE, String key);
    public byte[] data_EncryptOrDecryptForByte(String data, int MODE,int ENCRYPTION_MODE, SecretKeySpec key);
    public byte[] data_EncryptOrDecryptForByte(String data, int MODE,int ENCRYPTION_MODE, String key);
}
