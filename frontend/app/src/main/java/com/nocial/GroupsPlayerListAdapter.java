package com.nocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupsPlayerListAdapter extends RecyclerView.Adapter<GroupsPlayerListAdapter.ViewHolder> {

    ArrayList<String> userNames;
    ArrayList<Integer> userScreentime;
    ArrayList<Integer> reactions;
    Context context;

    public GroupsPlayerListAdapter(@NonNull Context context, ArrayList<String> userNames, ArrayList<Integer> userScreenTime, ArrayList<Integer> reactions) {
        super();

        this.context = context;
        this.userNames = userNames;
        this.userScreentime = userScreenTime;
        this.reactions = reactions;
    }

    @NonNull
    @Override
    public GroupsPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_player_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsPlayerListAdapter.ViewHolder holder, int position) {
        holder.username.setText(userNames.get(position));
        holder.place.setText(position + 1 + " th");
        holder.reactions.setText(String.valueOf(reactions.get(position)));
        holder.progressBarTime.setText(String.valueOf(userScreentime.get(position)));
        holder.progressBar.setProgress((int) ((userScreentime.get(position) / 60.0) * 100));
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView place;
        private final TextView reactions;
        private final TextView progressBarTime;
        private final ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.player_name);
            place = (TextView) itemView.findViewById(R.id.player_place);
            reactions = (TextView) itemView.findViewById(R.id.reactionCount);
            progressBarTime = (TextView) itemView.findViewById(R.id.progressBarTime);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        public TextView getTextView() {
            return username;
        }
    }
}
