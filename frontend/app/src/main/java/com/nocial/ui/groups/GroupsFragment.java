package com.nocial.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nocial.R;
import com.nocial.databinding.FragmentGroupsBinding;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    private FragmentGroupsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroupsViewModel groupsViewModel =
                new ViewModelProvider(this).get(GroupsViewModel.class);

        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<String> userNames = new ArrayList<>();
        userNames.add("Cow");
        userNames.add("Cat");
        userNames.add("Smiling Cat");
        userNames.add("Fox");
        userNames.add("Dog");
        userNames.add("Penguin");

        ArrayList<Integer> userScreenTime = new ArrayList<>();
        userScreenTime.add(10);
        userScreenTime.add(20);
        userScreenTime.add(30);
        userScreenTime.add(40);
        userScreenTime.add(50);
        userScreenTime.add(60);

        ArrayList<Integer> reactions = new ArrayList<>();
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);

        ArrayList<String> profilePictures = new ArrayList<>();
        profilePictures.add("cow");
        profilePictures.add("cat");
        profilePictures.add("smilecat");
        profilePictures.add("fox");
        profilePictures.add("dog");
        profilePictures.add("penguin");

        RecyclerView playerRecyclerView = root.findViewById(R.id.playerRecyclerView);
        GroupPlayerListAdapter adapter = new GroupPlayerListAdapter(requireActivity(), userNames, userScreenTime, reactions, profilePictures);

        playerRecyclerView.setAdapter(adapter);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}