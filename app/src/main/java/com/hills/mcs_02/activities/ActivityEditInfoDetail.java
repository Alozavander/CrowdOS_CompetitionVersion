package com.hills.mcs_02.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.R;

/** Edit pages for nicknames, phone numbers, and profiles，You need to return the intent with the resulting data when the page finishes. */
public class ActivityEditInfoDetail extends BaseActivity {
    private ListView mListView;
    private static final String TAG = "activity_enditInfo";
    private EditText textEditView;
    private File userIconFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo_detail);

        /** Initialization information */
        initInfo();

        initBtn();
    }

    private void initInfo() {
        TextView tempTitleTv = findViewById(R.id.minepage_editInfo_detail_title);
        /** Change the title of the current page based on the key value of the kind in the intent. */
        int kind = getIntent().getIntExtra("kind",-1);
        switch (kind){
            /** the default value sends an alert */
            case -1 :
                Toast.makeText(ActivityEditInfoDetail.this,"Info error!", Toast.LENGTH_SHORT);
                break;
            /** nickname:0 */
            case 0 :
                tempTitleTv.setText(getString(R.string.nickname));
                break;
            /** phone:1 */
            case 1 :
                tempTitleTv.setText(getString(R.string.mobilePhone));
                break;
            /** introduction:2 */
            case 2 :
                tempTitleTv.setText(getString(R.string.introduction));
                break;
        }

        textEditView = findViewById(R.id.minepage_editInfo_detail_editview);
       /** Place the obtained information in the edit box. */
        textEditView.setText(getIntent().getStringExtra("text"));
    }


    private void initBtn() {
        /** Go back to the image binding event listener. */
        findViewById(R.id.minepage_editInfo_detail_backerrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /** Go back and cancel code. */
                ActivityEditInfoDetail.this.setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        /** bind click events to confirm */
        Button confirmBtn = findViewById(R.id.minepage_editInfo_detail_confirm_bt);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEditView.getText().toString();
                Intent tempIntent = new Intent();
                tempIntent.putExtra("text",text);
              /*  Add the Intent to the Result in the Activity to make it easier for the arousing person to get it. */
                ActivityEditInfoDetail.this.setResult(Activity.RESULT_OK,tempIntent);
                finish();
            }
        });
    }
}
