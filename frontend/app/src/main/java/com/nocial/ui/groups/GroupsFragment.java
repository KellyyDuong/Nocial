package com.nocial.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nocial.R;
import com.nocial.data.LoginRepository;
import com.nocial.databinding.FragmentGroupsBinding;
import com.nocial.ui.customize.CustomizeFragment;
import com.nocial.ui.memberview.MemberviewFragment;

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
        userNames.add("Dog");
        userNames.add("Fox");
        userNames.add(LoginRepository.getInstance().getUser().getDisplayName());
        userNames.add("Cow");
        userNames.add("Cat");
        userNames.add("Smiling Cat");
        userNames.add("Cow");
        userNames.add("Cat");
        userNames.add("Smiling Cat");

        ArrayList<Integer> userScreenTime = new ArrayList<>();
        userScreenTime.add(15);
        userScreenTime.add(30);
        userScreenTime.add(45);
        userScreenTime.add(60);
        userScreenTime.add(90);
        userScreenTime.add(180);
        userScreenTime.add(60);
        userScreenTime.add(90);
        userScreenTime.add(180);

        ArrayList<Integer> reactions = new ArrayList<>();
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);
        reactions.add(202);

        ArrayList<String> profilePictures = new ArrayList<>();
        profilePictures.add("dog");
        profilePictures.add("fox");
        profilePictures.add("penguin");
        profilePictures.add("cow");
        profilePictures.add("cat");
        profilePictures.add("smilecat");
        profilePictures.add("cat");
        profilePictures.add("dog");
        profilePictures.add("smilecat");

        View.OnClickListener playerClick = v -> {

            System.out.println("CLICK");
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MemberviewFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("MemberviewFragment") // name can be null
                    .commit();
        };

        RecyclerView playerRecyclerView = root.findViewById(R.id.playerRecyclerView);
        GroupPlayerListAdapter adapter = new GroupPlayerListAdapter(requireActivity(), userNames, userScreenTime, reactions, profilePictures, playerClick);

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