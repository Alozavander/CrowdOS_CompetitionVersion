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

import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.activities.ActivityMineMinor1Publish;
import com.hills.mcs_02.activities.ActivityMineMinor2Accepted;
import com.hills.mcs_02.activities.ActivityMineMinor4Wallet;
import com.hills.mcs_02.activities.ActivityMineMinor5SensorData;
import com.hills.mcs_02.activities.ActivityMineMinor7Setting;
import com.hills.mcs_02.dataBeans.BeanListViewMine;
import com.hills.mcs_02.ForTest;
import com.hills.mcs_02.R;
import com.hills.mcs_02.taskSubmit.SelectDialog;
import com.hills.mcs_02.viewsAdapters.AdapterListViewMine;

public class FragmentMine extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

        /** Initialize the button to determine if it is automatically logged in */
        initButton();

        /** Initialize the list data */
        initBeanLvMine(view);

        return view;

    }


    private void initButton() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForTest.jumpToLoginPage();
            }
        });

        /**  Edit the data */
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForTest.jumpToEditInfo();
            }
        });

        /** Login or not based on the user information in the current app */
        String username  = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userName", "");
        if(username.equals("")) userInfoRecover();
        else userInfoRefresh();
    }


    private void initBeanLvMine(View view) {
        List<BeanListViewMine> listViewMine = new ArrayList<>();

        listViewMine.add(new BeanListViewMine(R.drawable.icon_yifabu, getResources().getString(R.string.fragment_mine_funclist_published)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_yijieshou, getResources().getString(R.string.fragment_mine_funclist_received)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_star, getResources().getString(R.string.fragment_mine_funclist_favorite)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_wallet, getResources().getString(R.string.fragment_mine_funclist_wallet)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_promotion, getResources().getString(R.string.setting_sensorfunction)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_message, getResources().getString(R.string.fragment_mine_funclist_notificaiton)));
        listViewMine.add(new BeanListViewMine(R.drawable.icon_setting, getResources().getString(R.string.fragment_mine_funclist_setting)));

        mListView = (ListView) view.findViewById(R.id.minepage_login_funciton_lv);
        mListView.setAdapter(new AdapterListViewMine(listViewMine, mContext));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                   /** Check if you are logged in */
                    if (loginUserId == -1) {
                        Intent intent = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), ActivityMineMinor1Publish.class);
                        startActivity(intent);
                    }
                }else if(position == 1){
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                    /** Check if you are logged in */
                    if (loginUserId == -1) {
                        Intent intent = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), ActivityMineMinor2Accepted.class);
                        startActivity(intent);
                    }
                }else if(position == 3){
                    int loginUserId = Integer.parseInt(mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                    /** Check if you are logged in */
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

   /** Refresh user information from user page */
    public void userInfoRefresh() {
        String username = getActivity().getSharedPreferences("user", mContext.MODE_PRIVATE).getString("userName", getResources().getString(R.string.fail_to_get_username));
        usernameTv.setText(username);
        loginBtn.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.VISIBLE);
    }

    /** Refresh user information from user page */
    public void userInfoRecover() {
        usernameTv.setText(getText(R.string.not_login));
        editBtn.setVisibility(View.INVISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context); /** Set the broadcast listener */
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
        context.registerReceiver(receiver, filter);
        mContext = context;

        try {
            mForTest = (ForTest) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement For_TestInterface");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /** Unbind the broadcast receiver */
        mContext.unregisterReceiver(receiver);
    }
}
