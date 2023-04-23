package com.nocial.ui.customize;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nocial.R;

public class CustomizeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customize, container, false);
//
//        root.findViewById(R.id.editImage).setOnClickListener(v -> {
//            FragmentManager fragmentManager = getParentFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, CustomizeFragment.class, null)
//                    .setReorderingAllowed(true)
//                    .addToBackStack("name") // name can be null
//                    .commit();
//        });

        return root;
    }
}