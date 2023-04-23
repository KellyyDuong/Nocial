package com.nocial.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nocial.R;
import com.nocial.data.LoginRepository;
import com.nocial.data.model.LoggedInUser;
import com.nocial.databinding.FragmentDashboardBinding;
import com.nocial.ui.groups.GroupsFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding dashboardBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = dashboardBinding.getRoot();

        LoginRepository instance = LoginRepository.getInstance();
        LoggedInUser user = instance.getUser();

        TextView welcomeText = root.findViewById(R.id.userWelcomeString);
        welcomeText.setText("Welcome, " + user.getDisplayName());

        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("Mi Familia");
        groupNames.add("Amigos");
//        groupNames.add("Homegirls");

        ArrayList<String> userPlaces = new ArrayList<>();
        userPlaces.add("1st");
        userPlaces.add("3rd");
//        userPlaces.add("4th");

        ArrayList<ArrayList<String>> profilePictures = new ArrayList<>();

        ArrayList<String> firstSet = new ArrayList<>();

        firstSet.add("penguin");
        firstSet.add("cat");
        firstSet.add("smilecat");

        ArrayList<String> secondSet = new ArrayList<>();

        secondSet.add("dog");
        secondSet.add("fox");
        secondSet.add("penguin");

        profilePictures.add(firstSet);
        profilePictures.add(secondSet);

        View.OnClickListener cardClick = v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("GroupsFragment") // name can be null
                    .commit();
        };

        RecyclerView groupsRecyclerView = root.findViewById(R.id.groups_recycler_view);
        GroupsListAdapter adapter = new GroupsListAdapter(requireActivity(), groupNames, userPlaces, profilePictures, cardClick);

        groupsRecyclerView.setAdapter(adapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardBinding = null;
    }
}