package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hills.mcs_02.MainActivity;
import com.hills.mcs_02.R;
import com.hills.mcs_02.languageChange.MultiLanguageUtil;

import java.util.List;

public class AdapterRadioBtnTvMultiLanguage extends BaseAdapter{
    public List<String> textList;
    public Context mContext;
    public int checkPosition;                   /** Checked TAG. -1 presents first entrance */

    public AdapterRadioBtnTvMultiLanguage(List<String> textList, Context context) {
        this.textList = textList;
        mContext = context;
        Log.i("test",MultiLanguageUtil.getAppLocale(mContext).getCountry());
        if(MultiLanguageUtil.getAppLocale(mContext).getCountry().equals("ZH")) checkPosition = 0;
        else checkPosition = 1;
    }

    @Override
    public int getCount() {
        return textList.size();
    }

    @Override
    public Object getItem(int position) {
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = View.inflate(mContext, R.layout.listview_setting_multilanguage,null);
        }else{
            view = convertView;
        }
        /** Match Text */
        TextView textView = view.findViewById(R.id.listView_multi_language_tv);
        textView.setText(textList.get(position));
        /**  Math Radio View */
        RadioButton radioBtn = view.findViewById(R.id.listView_multi_language_rb);
        /** Bind the event Listener for the view */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Single check while all keys of HashMap are false */
                checkPosition = position;
                switch (position){
                    case 0:
                        /**  Responding the language change event. 0 means Chinese, 1 means English */
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "zh", "ZH");
                        Toast.makeText(mContext,"你选择了中文语言",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "en", "US");
                        Toast.makeText(mContext,"Turn to English Version",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        /** The default is Chinese */
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "zh", "ZH");
                        break;
                }
                /** Reload activity */
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                /** Notify the changes */
                AdapterRadioBtnTvMultiLanguage.this.notifyDataSetChanged();
            }
        });
        /** Change the checked Tag */
        if(checkPosition == position) radioBtn.setChecked(true);
        else radioBtn.setChecked(false);

        return view;
    }

}
