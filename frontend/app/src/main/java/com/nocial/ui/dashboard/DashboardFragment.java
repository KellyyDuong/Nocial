package com.nocial.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.nocial.R;
import com.nocial.databinding.FragmentDashboardBinding;
import com.nocial.ui.groups.GroupsFragment;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding dashboardBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = dashboardBinding.getRoot();

        final TextView textView = dashboardBinding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button button = (Button) root.findViewById(R.id.to_groups_button);

        button.setOnClickListener(v -> {

            System.out.println("GROUP BUTTON CLICKED");

            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroupsFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
        });


//        setTimeout(() -> {
//            groupsBinding = FragmentGroupsBinding.inflate(inflater, container, false);
//            View root2 = groupsBinding.getRoot();
//        }, 10000);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardBinding = null;
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}