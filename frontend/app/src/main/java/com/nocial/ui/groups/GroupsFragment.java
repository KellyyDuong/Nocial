package com.nocial.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nocial.GroupsPlayerListAdapter;
import com.nocial.R;
import com.nocial.databinding.FragmentGroupsBinding;

import java.util.ArrayList;
import java.util.Objects;

public class GroupsFragment extends Fragment {

    private FragmentGroupsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroupsViewModel groupsViewModel =
                new ViewModelProvider(this).get(GroupsViewModel.class);

        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println("GROUPS FRAGMENT ON CREATE");

        ArrayList<String> userNames = new ArrayList<>();

        userNames.add("one");
        userNames.add("two");
        userNames.add("three");
        userNames.add("four");
        userNames.add("five");
        userNames.add("six");

        RecyclerView playerRecyclerView = root.findViewById(R.id.playerRecyclerView);
        GroupsPlayerListAdapter adapter = new GroupsPlayerListAdapter(requireActivity(), userNames);

        playerRecyclerView.setAdapter(adapter);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

//        final TextView textView = binding.textGroups;
//        groupsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}