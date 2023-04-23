package com.nocial.ui.groups;

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
import java.util.Random;

public class GroupPlayerListAdapter extends RecyclerView.Adapter<GroupPlayerListAdapter.ViewHolder> {

    ArrayList<String> userNames;
    ArrayList<String> profilePictures;
    ArrayList<Integer> userScreentime;
    ArrayList<Integer> reactions;
    Context context;

    public GroupPlayerListAdapter(@NonNull Context context, ArrayList<String> userNames, ArrayList<Integer> userScreenTime, ArrayList<Integer> reactions, ArrayList<String> profilePictures) {
        super();

        this.context = context;
        this.userNames = userNames;
        this.userScreentime = userScreenTime;
        this.reactions = reactions;
        this.profilePictures = profilePictures;
    }

    @NonNull
    @Override
    public GroupPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_player_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupPlayerListAdapter.ViewHolder holder, int position) {
        holder.username.setText(userNames.get(position));

        int place = position + 1;

        if (place == 1) holder.place.setText(place + "st");
        else if (place == 2) holder.place.setText(place + "nd");
        else if (place == 3) holder.place.setText(place + "rd");
        else holder.place.setText(place + "th");

        Random random = new Random();

        int smilingReactions = random.nextInt(10);
        int nauseatedReactions = random.nextInt(10);

        holder.smilingReactions.setText(String.valueOf(smilingReactions));
        holder.nauseatedReactions.setText(String.valueOf(nauseatedReactions));

        holder.progressBarTime.setText(String.valueOf(userScreentime.get(position)));
        holder.progressBar.setProgress((int) ((userScreentime.get(position) / 60.0) * 100));
        Resources resources = context.getResources();
        holder.profilePic.setImageResource(resources.getIdentifier(profilePictures.get(position), "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView place;
        private final TextView smilingReactions;
        private final TextView nauseatedReactions;
        private final TextView progressBarTime;
        private final ProgressBar progressBar;
        private final ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.player_name);
            place = itemView.findViewById(R.id.player_place);
            smilingReactions = itemView.findViewById(R.id.reactionSmileCount);
            nauseatedReactions = itemView.findViewById(R.id.reactionNauseatedCount);
            progressBarTime = itemView.findViewById(R.id.progressBarTime);
            progressBar = itemView.findViewById(R.id.progressBar);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }
}
