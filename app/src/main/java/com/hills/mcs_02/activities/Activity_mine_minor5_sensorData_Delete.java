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

public class Activity_mine_minor5_sensorData_Delete extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "Activity_mine_minor5_sensorData_Delete";
    private int[] mSensorTypeList;
    int mYear, mMonth, mDay;
    String date_tvString;
    public TextView mTextView_startTime;
    public TextView mTextView_endTime;
    private AlertDialog mSensorMultiAlertDialog;
    private boolean[] mBooleans;
    public String[] mSensors;
    public Button mConfirm_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor5_sensor_data__delete);
        initView();
    }

    private void initView() {
        String[] temp_Sensors = new SenseHelper(this).getSensorList_TypeInt_Strings();
        mSensors = new String[temp_Sensors.length + 1];          //多出全部这一选择项
        mSensors[0] = getString(R.string.all);
        for (int i = 0; i < temp_Sensors.length; i++) mSensors[i + 1] = temp_Sensors[i];
        mBooleans = new boolean[mSensors.length];


        //给时间选择栏添加绑定
        initTimeTV();
        TextView sensor_choose_tv = findViewById(R.id.setting_sensorData_delete_sensor_choose);
        sensor_choose_tv.setOnClickListener(this);
        sensor_choose_tv.addTextChangedListener(this);
        mConfirm_bt = findViewById(R.id.setting_sensorData_delete_confirm_button);
        mConfirm_bt.setOnClickListener(this);
        if (checkThreeTextNull()) mConfirm_bt.setEnabled(false);
        else mConfirm_bt.setEnabled(true);
    }

    //TODO:时间Date与long、与时间戳的转换
    private void initTimeTV() {
        //给开始时间选择栏添加绑定
        mTextView_startTime = findViewById(R.id.setting_sensorData_delete_startTime_choose);
        mTextView_startTime.addTextChangedListener(this);
        mTextView_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);


                //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
                DatePickerDialog dialog = new DatePickerDialog(Activity_mine_minor5_sensorData_Delete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String str_Month = "";
                        if (mMonth > 0 && mMonth < 10) str_Month = "0" + mMonth;    //补足0
                        else str_Month = mMonth + "";
                        mDay = dayOfMonth;
                        String str_Day = "";
                        if (mDay > 0 && mDay < 10) str_Day = "0" + mDay;    //补足0
                        else str_Day = mDay + "";
                        date_tvString = mYear + "-" + str_Month + "-" + str_Day + " 00:00:00";
                        mTextView_startTime.setText(date_tvString);
                    }

                }, mYear, mMonth, mDay);
                //设置最小时限
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(new Date().getTime());

                dialog.show();
            }
        });

        //给结束时间选择栏添加绑定
        mTextView_endTime = findViewById(R.id.setting_sensorData_delete_endTime_choose);
        mTextView_endTime.addTextChangedListener(this);
        mTextView_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);


                //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
                DatePickerDialog dialog = new DatePickerDialog(Activity_mine_minor5_sensorData_Delete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String str_Month = "";
                        if (mMonth > 0 && mMonth < 10) str_Month = "0" + mMonth;    //补足0
                        else str_Month = mMonth + "";
                        mDay = dayOfMonth;
                        String str_Day = "";
                        if (mDay > 0 && mDay < 10) str_Day = "0" + mDay;    //补足0
                        else str_Day = mDay + "";
                        date_tvString = mYear + "-" + str_Month + "-" + str_Day + " 23:59:59";
                        mTextView_endTime.setText(date_tvString);
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
        TextView temp_tv = findViewById(R.id.setting_sensorData_delete_sensor_choose);
        String chooseSensor = "";
        //遍历取得选择的传感器
        if (mBooleans[0] == true) temp_tv.setText(getString(R.string.all));
        else {
            for (int i = 0; i < mBooleans.length; i++) {
                if (mBooleans[i] == true) chooseSensor = chooseSensor + " " + mSensors[i];
            }
            temp_tv.setText(chooseSensor);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_sensorData_delete_confirm_button:
                bt_confirm();
                break;
            case R.id.setting_sensorData_delete_sensor_choose:
                multiChooseDialogCreate();
                break;
        }
    }

    //TODO:开启进度条以及子进程
    private void bt_confirm() {
        //先获取传感器对应的int值
        SenseHelper tem_SenseHelper = new SenseHelper(this);
        if (mBooleans[0] == true)
            mSensorTypeList = tem_SenseHelper.getSensorList_TypeInt_Integers();    //如果选择的是全部，则直接通过内置方法获取所有传感器的type值
        else {
            //对选择的传感器文本框获取文本内容并进行分割得到字符串数组，并根据内容
            String[] temp_String = ((TextView) findViewById(R.id.setting_sensorData_delete_sensor_choose)).getText().toString().split(" ");
            Log.e(TAG,"now sensorType chosen is : " + LogStrings(temp_String));
            mSensorTypeList = tem_SenseHelper.sensorList_NameStrings2TypeInts(temp_String);
        }

        String startTime = mTextView_startTime.getText().toString();
        String endTime = mTextView_endTime.getText().toString();

        //对选择的每一个传感器都进行删除操作
        int del_allNumb = 0;
        for (int i : mSensorTypeList) {
            Log.e(TAG,"now delete the sensor : " + i);
            // -2是在SenseHelper类中sensorXmlName2Type方法里定义的错误传感器识别码，在这里如果识别错误后应该跳出循环并给予提示
            if(i == -3) {
                Toast.makeText(Activity_mine_minor5_sensorData_Delete.this,getString(R.string.sensorIdentifyError),Toast.LENGTH_SHORT).show();
                continue;
            }else{
                del_allNumb = del_allNumb + SQLiteDelete(i + "", startTime, endTime);
            }
        }
        Toast.makeText(Activity_mine_minor5_sensorData_Delete.this, getString(R.string.delSensorDataRemind) + ": " + del_allNumb, Toast.LENGTH_SHORT).show();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (checkThreeTextNull()) mConfirm_bt.setEnabled(false);
        else mConfirm_bt.setEnabled(true);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (checkThreeTextNull()) mConfirm_bt.setEnabled(false);
        else mConfirm_bt.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (checkThreeTextNull()) mConfirm_bt.setEnabled(false);
        else mConfirm_bt.setEnabled(true);
    }

    public boolean checkThreeTextNull() {
        TextView temp_sensor_tv = (TextView) findViewById(R.id.setting_sensorData_delete_sensor_choose);
        if (
                (mTextView_endTime.getText().toString() == null)
                        || (mTextView_startTime.getText().toString() == null)
                        || (temp_sensor_tv.getText().toString() == null)
                        || temp_sensor_tv.getText().toString().equals(getString(R.string.chooseSensors))
                        || mTextView_startTime.getText().toString().equals(getString(R.string.chooseTime))
                        || mTextView_endTime.getText().toString().equals(getString(R.string.chooseTime))
        ) return true; //如果有一个为空则返回真
        else return false;
    }

    String LogStrings(String[] s){
        String lS = "";
        for(String temp_s : s){
            lS = lS + temp_s;
        }
        return lS;
    }
}
