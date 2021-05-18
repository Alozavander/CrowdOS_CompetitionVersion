package com.hills.mcs_02.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

public class ActivityMineMinor5SensorDataDelete extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "SensorDataDelete";
    private int[] mSensorTypeList;
    int mYear, mMonth, mDay;
    String dateString;
    public TextView mTextViewStartTime;
    public TextView mTextViewEndTime;
    private AlertDialog mSensorMultiAlertDialog;
    private boolean[] mBooleans;
    public String[] mSensors;
    public Button mConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor5_sensor_data__delete);
        initView();
    }

    private void initView() {
        String[] tempSensors = new SenseHelper(this).getSensorList_TypeInt_Strings();
        mSensors = new String[tempSensors.length + 1];          //多出全部这一选择项
        mSensors[0] = getString(R.string.all);
        for (int temp = 0; temp < tempSensors.length; temp++) mSensors[temp + 1] = tempSensors[temp];
        mBooleans = new boolean[mSensors.length];


        //给时间选择栏添加绑定
        initTimeTV();
        TextView sensorChooseTv = findViewById(R.id.setting_sensorData_delete_sensor_choose);
        sensorChooseTv.setOnClickListener(this);
        sensorChooseTv.addTextChangedListener(this);
        mConfirmBtn = findViewById(R.id.setting_sensorData_delete_confirm_button);
        mConfirmBtn.setOnClickListener(this);
        if (checkThreeTextNull()) mConfirmBtn .setEnabled(false);
        else mConfirmBtn.setEnabled(true);
    }

    //TODO:时间Date与long、与时间戳的转换
    private void initTimeTV() {
        //给开始时间选择栏添加绑定
        mTextViewStartTime = findViewById(R.id.setting_sensorData_delete_startTime_choose);
        mTextViewStartTime.addTextChangedListener(this);
        mTextViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);


                //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
                DatePickerDialog dialog = new DatePickerDialog(ActivityMineMinor5SensorDataDelete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String strMonth = "";
                        if (mMonth > 0 && mMonth < 10) strMonth = "0" + mMonth;    //补足0
                        else strMonth = mMonth + "";
                        mDay = dayOfMonth;
                        String strDay = "";
                        if (mDay > 0 && mDay < 10) strDay = "0" + mDay;    //补足0
                        else strDay = mDay + "";
                        dateString = mYear + "-" + strMonth + "-" + strDay + " 00:00:00";
                        mTextViewStartTime.setText(dateString);
                    }

                }, mYear, mMonth, mDay);
                //设置最小时限
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(new Date().getTime());

                dialog.show();
            }
        });

        //给结束时间选择栏添加绑定
        mTextViewEndTime = findViewById(R.id.setting_sensorData_delete_endTime_choose);
        mTextViewEndTime.addTextChangedListener(this);
        mTextViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);


                //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
                DatePickerDialog dialog = new DatePickerDialog(ActivityMineMinor5SensorDataDelete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String strMonth = "";
                        if (mMonth > 0 && mMonth < 10) strMonth = "0" + mMonth;    //补足0
                        else strMonth = mMonth + "";
                        mDay = dayOfMonth;
                        String strDay = "";
                        if (mDay > 0 && mDay < 10) strDay = "0" + mDay;    //补足0
                        else strDay = mDay + "";
                        dateString = mYear + "-" + strMonth + "-" + strDay + " 23:59:59";
                        mTextViewEndTime.setText(dateString);
                    }

                }, mYear, mMonth, mDay);
                //设置最小时限
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(new Date().getTime());

                dialog.show();
            }
        });
    }


    //创建传感器选择菜单，TODO：全部按钮的添加，其余索引+1
    private void multiChooseDialogCreate() {

        mSensorMultiAlertDialog = new AlertDialog.Builder(this).setMultiChoiceItems(mSensors, mBooleans, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.i(TAG, which + ";" + isChecked);
                mBooleans[which] = isChecked;
            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSensorTvText();
            }
        }).create();
        mSensorMultiAlertDialog.show();
    }

    private void setSensorTvText() {
        TextView tempTv = findViewById(R.id.setting_sensorData_delete_sensor_choose);
        String chooseSensor = "";
        //遍历取得选择的传感器
        if (mBooleans[0] == true) tempTv.setText(getString(R.string.all));
        else {
            for (int temp = 0; temp < mBooleans.length; temp++) {
                if (mBooleans[temp] == true) chooseSensor = chooseSensor + " " + mSensors[temp];
            }
            tempTv.setText(chooseSensor);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_sensorData_delete_confirm_button:
                btnConfirm();
                break;
            case R.id.setting_sensorData_delete_sensor_choose:
                multiChooseDialogCreate();
                break;
        }
    }

    //TODO:开启进度条以及子进程
    private void btnConfirm() {
        //先获取传感器对应的int值
        SenseHelper temSenseHelper = new SenseHelper(this);
        if (mBooleans[0] == true)
            mSensorTypeList = temSenseHelper.getSensorList_TypeInt_Integers();    //如果选择的是全部，则直接通过内置方法获取所有传感器的type值
        else {
            //对选择的传感器文本框获取文本内容并进行分割得到字符串数组，并根据内容
            String[] tempString = ((TextView) findViewById(R.id.setting_sensorData_delete_sensor_choose)).getText().toString().split(" ");
            Log.e(TAG,"now sensorType chosen is : " + LogStrings(tempString));
            mSensorTypeList = temSenseHelper.sensorList_NameStrings2TypeInts(tempString);
        }

        String startTime = mTextViewStartTime.getText().toString();
        String endTime = mTextViewEndTime.getText().toString();

        //对选择的每一个传感器都进行删除操作
        int delAllNumb = 0;
        for (int i : mSensorTypeList) {
            Log.e(TAG,"now delete the sensor : " + i);
            // -2是在SenseHelper类中sensorXmlName2Type方法里定义的错误传感器识别码，在这里如果识别错误后应该跳出循环并给予提示
            if(i == -3) {
                Toast.makeText(ActivityMineMinor5SensorDataDelete.this,getString(R.string.sensorIdentifyError),Toast.LENGTH_SHORT).show();
                continue;
            }else{
                delAllNumb = delAllNumb + SQLiteDelete(i + "", startTime, endTime);
            }
        }
        Toast.makeText(ActivityMineMinor5SensorDataDelete.this, getString(R.string.delSensorDataRemind) + ": " + delAllNumb, Toast.LENGTH_SHORT).show();
    }

    private int SQLiteDelete(String sensorType, String startTime, String endTime) {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(this).getReadableDatabase();
        String whereClaus = StringStore.SensorDataTable_SenseType + "=?" + " AND " + StringStore.SensorDataTable_SenseTime + " > ? AND " + StringStore.SensorDataTable_SenseTime + " < ?";
        int lI = db.delete(StringStore.SensorDataTable_Name,
               whereClaus , new String[]{sensorType, startTime, endTime});
        Log.e(TAG,"Where Claus is : " + whereClaus);
        Log.e(TAG, "StartTime is : " + startTime);
        Log.e(TAG, "EndTime is : " + endTime);
        Log.e(TAG, "Delete result : " + lI);
        //StringStore.SensorDataTable_SenseType + "=?" , new String[]{"2"});
        //db.execSQL("delete from " + StringStore.SensorDataTable_Name + " where " + StringStore.SensorDataTable_SenseType + " = " + sensorType);
        return lI;
    }


    //监控传感器、删除时间段时间文本框，监听时间并改变confirm按钮状态
    @Override
    public void beforeTextChanged(CharSequence seq, int start, int count, int after) {
        if (checkThreeTextNull()) mConfirmBtn.setEnabled(false);
        else mConfirmBtn.setEnabled(true);
    }

    @Override
    public void onTextChanged(CharSequence seq, int start, int before, int count) {
        if (checkThreeTextNull()) mConfirmBtn.setEnabled(false);
        else mConfirmBtn.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable edit) {
        if (checkThreeTextNull()) mConfirmBtn.setEnabled(false);
        else mConfirmBtn.setEnabled(true);
    }

    public boolean checkThreeTextNull() {
        TextView tempSensorTv = (TextView) findViewById(R.id.setting_sensorData_delete_sensor_choose);
        if (
                (mTextViewEndTime.getText().toString() == null)
                        || (mTextViewStartTime.getText().toString() == null)
                        || (tempSensorTv.getText().toString() == null)
                        || tempSensorTv.getText().toString().equals(getString(R.string.chooseSensors))
                        || mTextViewStartTime.getText().toString().equals(getString(R.string.chooseTime))
                        || mTextViewEndTime.getText().toString().equals(getString(R.string.chooseTime))
        ) return true; //如果有一个为空则返回真
        else return false;
    }

    String LogStrings(String[] string){
        String str = "";
        for(String tempStr : string){
            str = str + tempStr;
        }
        return str;
    }
}
