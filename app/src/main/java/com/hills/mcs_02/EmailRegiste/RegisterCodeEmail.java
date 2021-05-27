package com.hills.mcs_02.EmailRegiste;

import android.os.AsyncTask;

public class RegisterCodeEmail {
    private long mVerifyCode;

    public final String M_USER_NAME = "zeronandroidtest@163.com";
    public final String M_PASSWORD = "UGQEGXIPKFFLNHEQ";
    public final String M_SUBJECT = "WeSense 账户注册验证码";
    public final String M_PRE_CONTENT = "邮箱注册验证码为:";

    public RegisterCodeEmail() {
        super();
    }

    /** Send the mail method */
    public void sendEmail(String emailAddress) {
        new emailAsyncTask(emailAddress).execute();
    }

    /** Verify the code */
    public boolean verify(String inputNumber) {
        long inputLong = Long.parseLong(inputNumber);
        if (mVerifyCode == inputLong) return true;
        else return false;
    }
    /** To perform the send mail operation */
    public class emailAsyncTask extends AsyncTask<String, Integer, String> {
        private String emailRecipient;

        public emailAsyncTask(String emailRecipient) {
            this.emailRecipient = emailRecipient;
        }

        @Override
        protected String doInBackground(String... strings) {
           /** Randomly generate a 6-bit CAPTCHA */
            mVerifyCode = (int) ((Math.random() * 9 + 1) * 100000);
            SendEmail sendE = new SendEmail(emailRecipient, M_USER_NAME, M_PASSWORD);
            try {
                sendE.sendTextEmail(M_SUBJECT, M_PRE_CONTENT + mVerifyCode);
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
