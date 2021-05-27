package com.hills.mcs_02.taskSubmit;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hills.mcs_02.R;

import java.util.List;


public class SelectDialog extends Dialog implements OnClickListener,OnItemClickListener {
    private SelectDialogListener mListener;
    private Activity mActivity;
    private Button mBtnCancel;
    private TextView mTvTitle;
    private List<String> mName;
    private String mTitle;
    private boolean mUseCustomColor = false;
    private int mFirstItemColor;
    private int mOtherItemColor;

    public interface SelectDialogListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /** Cancel event monitoring interface */
    private SelectDialogCancelListener mCancelListener;

    public interface SelectDialogCancelListener {
        public void onCancelClick(View view);
    }

    public SelectDialog(Activity activity, int theme,
                        SelectDialogListener listener,List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;

        setCanceledOnTouchOutside(true);
    }

    /** The activity that calls the pop-up menu */
    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,SelectDialogCancelListener cancelListener ,List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;

        /** Set whether to click on the periphery to not dismiss  */
        setCanceledOnTouchOutside(false);
    }

    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,List<String> names,String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;
        mTitle = title;

        setCanceledOnTouchOutside(true);
    }

    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,SelectDialogCancelListener cancelListener,List<String> names,String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;
        mTitle = title;

        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.view_dialog_select,
                null);
        setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        /**Set the animation of display */
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        /** Ensure that the button can fill the screen horizontally */
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        /** Set the display location */
        onWindowAttributesChanged(wl);

        initViews();
    }

    private void initViews() {
        DialogAdapter dialogAdapter=new DialogAdapter(mName);
        ListView dialogList=(ListView) findViewById(R.id.dialog_list);
        dialogList.setOnItemClickListener(this);
        dialogList.setAdapter(dialogAdapter);
        mBtnCancel = (Button) findViewById(R.id.mBtn_Cancel);
        mTvTitle = (TextView) findViewById(R.id.mTv_Title);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCancelListener != null){
                    mCancelListener.onCancelClick(view);
                }
                dismiss();
            }
        });

        if(!TextUtils.isEmpty(mTitle) && mTvTitle != null){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mTitle);
        }else{
            mTvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        mListener.onItemClick(parent, view, position, id);
        dismiss();
    }
    private class DialogAdapter extends BaseAdapter {
        private List<String> mStrings;
        private SelectDialog.viewHolder viewHolder;
        private LayoutInflater layoutInflater;
        public DialogAdapter(List<String> strings) {
            this.mStrings = strings;
            this.layoutInflater=mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                viewHolder =new viewHolder();
                convertView=layoutInflater.inflate(R.layout.view_dialog_item, null);
                viewHolder.dialogItemButton=(TextView) convertView.findViewById(R.id.dialog_item_bt);
                convertView.setTag(viewHolder);
            }else{
                viewHolder =(SelectDialog.viewHolder) convertView.getTag();
            }
            viewHolder.dialogItemButton.setText(mStrings.get(position));
            if (!mUseCustomColor) {
                mFirstItemColor = mActivity.getResources().getColor(R.color.blue);
                mOtherItemColor = mActivity.getResources().getColor(R.color.blue);
            }
            if (1 == mStrings.size()) {
                viewHolder.dialogItemButton.setTextColor(mFirstItemColor);
                viewHolder.dialogItemButton.setBackgroundResource(R.drawable.dialog_item_bg_only);
            } else if (position == 0) {
                viewHolder.dialogItemButton.setTextColor(mFirstItemColor);
                viewHolder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_top);
            } else if (position == mStrings.size() - 1) {
                viewHolder.dialogItemButton.setTextColor(mOtherItemColor);
                viewHolder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_buttom);
            } else {
                viewHolder.dialogItemButton.setTextColor(mOtherItemColor);
                viewHolder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_center);
            }
            return convertView;
        }

    }

    public static class viewHolder {
        public TextView dialogItemButton;
    }

    /** Set the text color of the list item */
    public void setItemColor(int firstItemColor, int otherItemColor) {
        mFirstItemColor = firstItemColor;
        mOtherItemColor = otherItemColor;
        mUseCustomColor = true;
    }
}
