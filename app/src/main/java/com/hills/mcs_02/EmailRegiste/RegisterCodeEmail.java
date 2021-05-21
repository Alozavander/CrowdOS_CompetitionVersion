package com.hills.mcs_02.EmailRegiste;

import android.os.AsyncTask;

public class RegisterCodeEmail {
    private long mVerifyCode;
    //邮箱信息在此可改
    public final String mUsername = "zeronandroidtest@163.com";
    public final String mPassword = "UGQEGXIPKFFLNHEQ";
    //发送内容在此更改
    public final String mSubject = "WeSense 账户注册验证码";
    public final String mPreContent = "邮箱注册验证码为:";

    public RegisterCodeEmail() {
        super();
    }

    //发送邮件方法
    public void sendEmail(String email_adress) {
        new emailAsyncTask(email_adress).execute();
    }

    //验证码验证方法
    public boolean verify(String input_number) {
        long input_Long = Long.parseLong(input_number);
        if (mVerifyCode == input_Long) return true;
        else return false;
    }

    //内部类异步任务类，执行发送邮件操作
    public class emailAsyncTask extends AsyncTask<String, Integer, String> {
        private String email_recipient;

        public emailAsyncTask(String emailRecipient) {
            this.email_recipient = emailRecipient;
        }

        @Override
        protected String doInBackground(String... strings) {
            //随机生成6位验证码
            mVerifyCode = (int) ((Math.random() * 9 + 1) * 100000);
            SendEmail se = new SendEmail(email_recipient, mUsername, mPassword);
            try {
                se.sendTextEmail(mSubject, mPreContent + mVerifyCode);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
        }
    }
}
