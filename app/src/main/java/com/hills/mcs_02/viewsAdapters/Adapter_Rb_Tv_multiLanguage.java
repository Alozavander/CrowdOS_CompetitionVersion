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

public class Adapter_Rb_Tv_multiLanguage extends BaseAdapter{
    public List<String> textList;
    public Context mContext;
    public int checkposition;                   //为正确显示radioButton选中项设置的标志,-1表示第一次进入app

    public Adapter_Rb_Tv_multiLanguage(List<String> textList, Context context) {
        this.textList = textList;
        mContext = context;
        Log.i("test",MultiLanguageUtil.getAppLocale(mContext).getCountry());
        if(MultiLanguageUtil.getAppLocale(mContext).getCountry().equals("ZH")) checkposition = 0;
        else checkposition = 1;
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
        //判定是否为初始化的View还是复用View
        if(convertView == null){
            view = View.inflate(mContext, R.layout.listview_setting_multilanguage,null);
        }else{
            view = convertView;
        }
        //匹配文字
        TextView textView = view.findViewById(R.id.listView_multi_language_tv);
        textView.setText(textList.get(position));
        //匹配Radio
        RadioButton radioButton = view.findViewById(R.id.listView_multi_language_rb);
        //给view绑定上点击事件监听器
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HashMap中所有key值全为false，相当于单选
                checkposition = position;
                switch (position){
                    case 0:
                        //响应语言改变事件，0为中文，1为英文
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "zh", "ZH");
                        Toast.makeText(mContext,"你选择了中文语言",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "en", "US");
                        Toast.makeText(mContext,"Turn to English Version",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        //默认为中文
                        MultiLanguageUtil.changeLanguage(mContext.getApplicationContext(), "zh", "ZH");
                        break;
                }
                //清楚所有活动并重新加载
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                //通知所有改变
                Adapter_Rb_Tv_multiLanguage.this.notifyDataSetChanged();
            }
        });
        //选中标志
        if(checkposition == position) radioButton.setChecked(true);
        else radioButton.setChecked(false);

        return view;
    }

}
