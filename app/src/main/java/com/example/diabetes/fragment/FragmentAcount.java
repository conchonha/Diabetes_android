package com.example.diabetes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diabetes.R;
import com.example.diabetes.activity.ActivityIntroduce;
import com.example.diabetes.activity.LoginActivity;
import com.example.diabetes.activity.MyProfile;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;

public class FragmentAcount extends Fragment {
    private View view;
    private RelativeLayout relativeAcountMyprofile,reliveHelpsSupport,reliveAbout,reliveLogout;
    private SharePrefs sharePrefs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_acount,container,false);
       init();
       listenerOnclicked();
        return view;
    }

    private void listenerOnclicked() {
        relativeAcountMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyProfile.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_ride);
            }
        });
        reliveHelpsSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActivityIntroduce.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_ride);
            }
        });

        reliveLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePrefs.clearUser();
                startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_ride);
            }
        });

        reliveAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShow.showDialogAcount(view.getContext(),"About",Constant.mAbout);
            }
        });
    }


    private void init() {
        sharePrefs = new SharePrefs(getActivity().getApplication());
        reliveLogout = view.findViewById(R.id.reliveLogout);
        reliveAbout = view.findViewById(R.id.reliveAbout);
        reliveHelpsSupport = view.findViewById(R.id.relivelayoutHelpsSupport);
        relativeAcountMyprofile = view.findViewById(R.id.relativeAcountMyprofile);
    }
}
