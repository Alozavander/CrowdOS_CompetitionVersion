package com.hills.mcs_02.fragmentsPack;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hills.mcs_02.viewsAdapters.Adapter_ListView_remind;
import com.hills.mcs_02.dataBeans.Bean_ListView_remind;
import com.hills.mcs_02.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the interface
 * to handle interaction events.
 * Use the {@link Fragment_mine_minor1_published#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_mine_minor3_favorite extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ListView mListView;



    public Fragment_mine_minor3_favorite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_mine_minor1_published.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_mine_minor3_favorite newInstance(String param1, String param2) {
        Fragment_mine_minor3_favorite fragment = new Fragment_mine_minor3_favorite();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_mine_minor3_favorite,container,false);
        mContext = getActivity().getApplicationContext();

        //???????????????
        initList(view);

        return view;
    }

    private void initList(View view){
        List<Bean_ListView_remind> list = new ArrayList<>();

        //????????????
        //list.add(new Bean_ListView_remind(R.drawable.haimian_usericon, R.drawable.testphoto_4, "Xminer", "??????1??????", "????????????","???????????????????????????????????????????????????????????????????????????????????????????????????", "5","10"));

        mListView = view.findViewById(R.id.minepage_minor3_lv);
        mListView.setAdapter(new Adapter_ListView_remind( mContext,list));
        //???????????????????????????
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
