package com.example.cartandorder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cartandorder.R;
import com.example.cartandorder.contact.ChatActivity;
import com.example.cartandorder.login.SignInByPass;
import com.example.cartandorder.profile.EditProfileActivity;
import com.example.cartandorder.profile.HelpCenterActivity;
import com.example.cartandorder.profile.SellActivity;
import com.example.cartandorder.profile.NotificationProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    Button btn_profile,btn_notification,btn_Language ,btn_helpcenter,btn_logout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        btn_profile = view.findViewById(R.id.btn_profile);
        btn_notification =view.findViewById(R.id.btn_notification);
        btn_Language = view.findViewById(R.id.btn_Language);
        btn_helpcenter = view.findViewById(R.id.btn_helpcenter);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_profile.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });
        btn_notification.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), NotificationProfileActivity.class);
            startActivity(intent);
        });
        btn_Language.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), SellActivity.class);
            startActivity(intent);
        });
        btn_helpcenter.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), HelpCenterActivity.class);
            startActivity(intent);
        });
        btn_logout.setOnClickListener(v->{
            SharedPreferences sharedPreferences =requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.apply();

            Intent intent = new Intent(requireActivity(), SignInByPass.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        ImageView btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ChatActivity.class));
        });
        return view;
    }
}