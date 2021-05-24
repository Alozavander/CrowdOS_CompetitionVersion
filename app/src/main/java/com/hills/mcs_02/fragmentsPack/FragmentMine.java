package com.hills.mcs_02.fragmentsPack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.activities.ActivityMineMinor1Publish;
import com.hills.mcs_02.activities.ActivityMineMinor2Accepted;
import com.hills.mcs_02.activities.ActivityMineMinor4Wallet;
import com.hills.mcs_02.activities.ActivityMineMinor5SensorData;
import com.hills.mcs_02.activities.ActivityMineMinor7Setting;
import com.hills.mcs_02.dataBeans.Bean_ListView_mine;
import com.hills.mcs_02.ForTest;
import com.hills.mcs_02.R;
import com.hills.mcs_02.taskSubmit.SelectDialog;
import com.hills.mcs_02.viewsAdapters.AdapterListeViewMine;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentMine#newInstance} factory method to
 * create an instance of this fragment.
 */
//“我的”分页面，还未开始编写
public class FragmentMine extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private Context mContext;
    private ForTest mForTest;
    private TextView usernameTv;
    private BroadcastReceiver receiver;
    private Button loginBtn;
    private Button editBtn;
    private ImageView userIcon;



    public FragmentMine() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_mine.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMine newInstance(String param1, String param2) {
        FragmentMine fragment = new FragmentMine();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_login, container, false);

        usernameTv = (TextView) view.findViewById(R.id.minepage_login_userName);
        editBtn = (Button) view.findViewById(R.id.minepage_infoEdit_bt);
        loginBtn = (Button) view.findViewById(R.id.minepage_login_bt);
        userIcon = view.findViewById(R.id.minepage_login_userIcon);

        //初始化按钮，判定是否自动登录
        initButton();

        //初始化列表数据
        initBeanLvMine(view);

        return view;

    }

    //暂时弹出框用不上
    private void initUserIcon() {
        //绑定点击事件
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> choiceList = new ArrayList<String>();
                choiceList.add(getString(R.string.checkUserIcon));
                choiceList.add(getString(R.string.updateUserIcon));
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){

                        }
                        if(position == 1){

                        }
                    }
                }, choiceList);
            }
        });
    }

    private void initButton() {
        //“登陆”按钮绑定相应事件——点击进入登陆页面，暂定他的int参数为用不到的0
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*for test
                //写入一些信息方便其他子页面能够使用
                SharedPreferences user_SP = getActivity().getSharedPreferences("user",mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = user_SP.edit();
                editor.putString("userID","Anna");
                editor.commit();
                //发送刷新Fragment_mine的广播
                Intent intent = new Intent();
                intent.setAction("action_Fragment_mine_userInfo_fresh");
                getActivity().sendBroadcast(intent);*/
                mForTest.jumpToLoginPage();
            }
        });


        //编辑资料的button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //11为此处不会用到的参数
                mForTest.jumpToEditInfo();
            }
        });

        //根据当前app中的用户信息判定是否登录
        String username  = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userName", "");
        if(username.equals("")) userInfoRecover();
        else userInfoRefresh();
    }


    private void initBeanLvMine(View view) {
        List<Bean_ListView_mine> listView_mine = new ArrayList<>();

        //填充数据,有几行填充几行
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_yifabu, getResources().getString(R.string.fragment_mine_funclist_published)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_yijieshou, getResources().getString(R.string.fragment_mine_funclist_received)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_star, getResources().getString(R.string.fragment_mine_funclist_favorite)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_wallet, getResources().getString(R.string.fragment_mine_funclist_wallet)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_promotion, getResources().getString(R.string.setting_sensorfunction)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_message, getResources().getString(R.string.fragment_mine_funclist_notificaiton)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_setting, getResources().getString(R.string.fragment_mine_funclist_setting)));

        //关联到布局文件中的listview
        mListView = (ListView) view.findViewById(R.id.minepage_login_funciton_lv);
        mListView.setAdapter(new AdapterListeViewMine(listView_mine, mContext));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                    //检查是否登录
                    if (loginUserId == -1) {
                        Intent intent = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), ActivityMineMinor1Publish.class);
                        startActivity(intent);
                    }
                }else if(position == 1){
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                    //检查是否登录
                    if (loginUserId == -1) {
                        Intent intent = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), ActivityMineMinor2Accepted.class);
                        startActivity(intent);
                    }
                }else if(position == 3){
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                    //检查是否登录
                    if (loginUserId == -1) {
                        Intent intent = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), ActivityMineMinor4Wallet.class);
                        startActivity(intent);
                    }
                }else if (position == 4){
                    Intent intent = new Intent(getActivity(), ActivityMineMinor5SensorData.class);
                    startActivity(intent);
                }else if(position == 6){
                    Intent intent = new Intent(getActivity(), ActivityMineMinor7Setting.class);
                    startActivity(intent);
                }
                else Toast.makeText(mContext,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });

    }

    //刷新用户页用户信息的方法
    public void userInfoRefresh() {
        //这里的sharedPreferences用法暂时保存，其实也可以在login页面使用intent传送对应信息
        String username = getActivity().getSharedPreferences("user", mContext.MODE_PRIVATE).getString("userName", getResources().getString(R.string.fail_to_get_username));
        usernameTv.setText(username);
        loginBtn.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.VISIBLE);
    }

    //刷新用户页用户信息的方法
    public void userInfoRecover() {
        usernameTv.setText(getText(R.string.not_login));
        editBtn.setVisibility(View.INVISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style.transparentFrameWindowStyle, listener, names);
        //从activity的isFinishing转换而来，测试点
        if (!this.isRemoving()) {
            dialog.show();
        }
        return dialog;
    }

    //为Fragment之间通信设置的回调Activity的方法
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);//设置广播监听器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("action_Fragment_mine_userInfo_login")) {
                    userInfoRefresh();
                }
                if(action.equals("action_Fragment_mine_userInfo_quit")){
                    userInfoRecover();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("action_Fragment_mine_userInfo_login");
        filter.addAction("action_Fragment_mine_userInfo_quit");
        //注册了跳转页面的广播接收器，记得要解绑
        context.registerReceiver(receiver, filter);
        mContext = context;

        // 保证容器Activity实现了回调接口 否则抛出异常警告
        try {
            mForTest = (ForTest) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑广播接收器
        mContext.unregisterReceiver(receiver);
    }
}
