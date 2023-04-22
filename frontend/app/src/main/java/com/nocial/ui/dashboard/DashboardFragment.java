package com.nocial.ui.dashboard;

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
import com.nocial.databinding.FragmentDashboardBinding;
import com.nocial.ui.groups.GroupsFragment;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding dashboardBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = dashboardBinding.getRoot();

        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("Mi Familia");
        groupNames.add("Amigos");
        groupNames.add("Homegirls");
        groupNames.add("Study Group");
        groupNames.add("Gang");

        ArrayList<String> userPlaces = new ArrayList<>();
        userPlaces.add("5th");
        userPlaces.add("3rd");
        userPlaces.add("5th");
        userPlaces.add("2nd");
        userPlaces.add("1st");

        ArrayList<ArrayList<String>> profilePictures = new ArrayList<>();

        ArrayList<String> subPFPs = new ArrayList<>();
        subPFPs.add("cow");
        subPFPs.add("cat");
        subPFPs.add("smilecat");

        profilePictures.add(subPFPs);
        profilePictures.add(subPFPs);
        profilePictures.add(subPFPs);
        profilePictures.add(subPFPs);
        profilePictures.add(subPFPs);

        View.OnClickListener cardClick = v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
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