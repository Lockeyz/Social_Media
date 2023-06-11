package com.lucky.social_media_lemon;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    TextView statusTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        statusTextView = view.findViewById(R.id.status_text_view);

        statusTextView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreatePostActivity.class));
        });

        return view;

    }
}