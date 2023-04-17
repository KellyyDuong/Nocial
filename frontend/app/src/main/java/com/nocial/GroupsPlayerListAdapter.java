package com.nocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupsPlayerListAdapter extends RecyclerView.Adapter<GroupsPlayerListAdapter.ViewHolder> {

    ArrayList<String> userNames;
    Context context;

    public GroupsPlayerListAdapter(@NonNull Context context, ArrayList<String> userNames) {
        super();

        this.context = context;
        this.userNames = userNames;
    }

    @NonNull
    @Override
    public GroupsPlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_player_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsPlayerListAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(userNames.get(position));
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;

        public ViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.player_name);
        }

        public TextView getTextView() {
            return nameText;
        }
    }
}
