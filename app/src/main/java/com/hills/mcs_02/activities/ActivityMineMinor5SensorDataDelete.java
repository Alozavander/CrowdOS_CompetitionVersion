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

import java.util.Calendar;
import java.util.Date;

import com.hills.mcs_02.R;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorSQLiteOpenHelper;

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
        String[] tempSensors = new SenseHelper(this).getSensorListTypeIntStrings();
        mSensors = new String[tempSensors.length + 1];       /** All options are available */
        mSensors[0] = getString(R.string.all);
        for (int temp = 0; temp < tempSensors.length; temp++) mSensors[temp + 1] = tempSensors[temp];
        mBooleans = new boolean[mSensors.length];
        /**  Add a binding to the time selection bar*/
        initTimeTV();
        TextView sensorChooseTv = findViewById(R.id.setting_sensorData_delete_sensor_choose);
        sensorChooseTv.setOnClickListener(this);
        sensorChooseTv.addTextChangedListener(this);
        mConfirmBtn = findViewById(R.id.setting_sensorData_delete_confirm_button);
        mConfirmBtn.setOnClickListener(this);
        if (checkThreeTextNull()) mConfirmBtn .setEnabled(false);
        else mConfirmBtn.setEnabled(true);
    }

    private void initTimeTV() {
        /**  Add a binding to the start time selection bar */
        mTextViewStartTime = findViewById(R.id.setting_sensorData_delete_startTime_choose);
        mTextViewStartTime.addTextChangedListener(this);
        mTextViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**  Get the current date */
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                /** Create a dialog box for date selection and bind the Listener for date selection */
                DatePickerDialog dialog = new DatePickerDialog(ActivityMineMinor5SensorDataDelete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String strMonth = "";
                        if (mMonth > 0 && mMonth < 10) strMonth = "0" + mMonth;
                        else strMonth = mMonth + "";
                        mDay = dayOfMonth;
                        String strDay = "";
                        if (mDay > 0 && mDay < 10) strDay = "0" + mDay;
                        else strDay = mDay + "";
                        dateString = mYear + "-" + strMonth + "-" + strDay + " 00:00:00";
                        mTextViewStartTime.setText(dateString);
                    }

                }, mYear, mMonth, mDay);
                /**  Set the minimum time limit */
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(new Date().getTime());

                dialog.show();
            }
        });

        /**  Add a binding to the end time selection bar */
        mTextViewEndTime = findViewById(R.id.setting_sensorData_delete_endTime_choose);
        mTextViewEndTime.addTextChangedListener(this);
        mTextViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Get the current date */
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);

                /** Create a dialog box for date selection and bind the Listener for date selection */
                DatePickerDialog dialog = new DatePickerDialog(ActivityMineMinor5SensorDataDelete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        String strMonth = "";
                        if (mMonth > 0 && mMonth < 10) strMonth = "0" + mMonth;
                        else strMonth = mMonth + "";
                        mDay = dayOfMonth;
                        String strDay = "";
                        if (mDay > 0 && mDay < 10) strDay = "0" + mDay;
                        else strDay = mDay + "";
                        dateString = mYear + "-" + strMonth + "-" + strDay + " 23:59:59";
                        mTextViewEndTime.setText(dateString);
                    }

                }, mYear, mMonth, mDay);
                /**  Set the minimum time limit */
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(new Date().getTime());
                dialog.show();
            }
        });
    }

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
        /**  iterate through the selected sensor */
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

    private void btnConfirm() {
        /** Get the int value of the sensor first */
        SenseHelper temSenseHelper = new SenseHelper(this);
        if (mBooleans[0] == true)
            mSensorTypeList = temSenseHelper.getSensorListTypeIntIntegers();    /** If all is selected, the type values of all sensors are obtained directly from the built-in method*/
        else {
            String[] tempString = ((TextView) findViewById(R.id.setting_sensorData_delete_sensor_choose)).getText().toString().split(" ");
            Log.e(TAG,"now sensorType chosen is : " + LogStrings(tempString));
            mSensorTypeList = temSenseHelper.sensorListNameStrings2TypeInts(tempString);
        }

        String startTime = mTextViewStartTime.getText().toString();
        String endTime = mTextViewEndTime.getText().toString();

        /**  Delete each sensor selected */
        int delAllNumb = 0;
        for (int i : mSensorTypeList) {
            Log.e(TAG,"now delete the sensor : " + i);
            /** -2 is the error sensor identifier defined in the SensorXMLName2Type method in the SenseHelper class */
            if(i == -3) {
                Toast.makeText(ActivityMineMinor5SensorDataDelete.this,getString(R.string.sensorIdentifyError),Toast.LENGTH_SHORT).show();
                continue;
            }else{
                delAllNumb = delAllNumb + SqliteDelete(i + "", startTime, endTime);
            }
        }
        Toast.makeText(ActivityMineMinor5SensorDataDelete.this, getString(R.string.delSensorDataRemind) + ": " + delAllNumb, Toast.LENGTH_SHORT).show();
    }

    private int SqliteDelete(String sensorType, String startTime, String endTime) {
        SQLiteDatabase db = new SensorSQLiteOpenHelper(this).getReadableDatabase();
        String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?" + " AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME
            + " > ? AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME + " < ?";
        int lI = db.delete(StringStore.SENSOR_DATATABLE_NAME,
               whereClaus , new String[]{sensorType, startTime, endTime});
        Log.e(TAG,"Where Claus is : " + whereClaus);
        Log.e(TAG, "StartTime is : " + startTime);
        Log.e(TAG, "EndTime is : " + endTime);
        Log.e(TAG, "Delete result : " + lI);
        return lI;
    }
    /**  Monitor the sensor, delete the time time text box, monitor the time and change the confirm button status*/
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
        ) return true;
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
