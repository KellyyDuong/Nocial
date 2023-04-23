package com.nocial.ui.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nocial.R;

import java.util.ArrayList;

public class GroupsListAdapter extends RecyclerView.Adapter<GroupsListAdapter.ViewHolder> {

    ArrayList<String> groupNames;
    ArrayList<String> userPlaces;
    ArrayList<ArrayList<String>> profilePictures;
    Context context;
    View.OnClickListener onClickListener;

    public GroupsListAdapter(@NonNull Context context, ArrayList<String> groupNames, ArrayList<String> userPlaces, ArrayList<ArrayList<String>> profilePictures, View.OnClickListener onClickListener) {
        super();

        this.groupNames = groupNames;
        this.userPlaces = userPlaces;
        this.profilePictures = profilePictures;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public GroupsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_group_item, parent, false);

        view.setOnClickListener(this.onClickListener);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsListAdapter.ViewHolder holder, int position) {

        Resources resources = context.getResources();

        holder.groupName.setText(groupNames.get(position));
        holder.userPlace.setText(userPlaces.get(position));
        holder.firstUserImage.setImageResource(resources.getIdentifier(profilePictures.get(position).get(0), "drawable", context.getPackageName()));
        holder.secondUserImage.setImageResource(resources.getIdentifier(profilePictures.get(position).get(1), "drawable", context.getPackageName()));
        holder.thirdUserImage.setImageResource(resources.getIdentifier(profilePictures.get(position).get(2), "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView groupName;
        private final TextView userPlace;
        private final ImageView firstUserImage;
        private final ImageView secondUserImage;
        private final ImageView thirdUserImage;

        public ViewHolder(View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.group_name);
            userPlace = itemView.findViewById(R.id.user_place);
            firstUserImage = itemView.findViewById(R.id.first_user_image);
            secondUserImage = itemView.findViewById(R.id.second_user_place);
            thirdUserImage = itemView.findViewById(R.id.third_user_image);
        }
    }
}
