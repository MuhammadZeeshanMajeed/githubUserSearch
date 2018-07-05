package com.seainfosys.interview42.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seainfosys.interview42.R;
import com.seainfosys.interview42.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder>
{
    private ArrayList<User> userList;

    public FollowersAdapter(ArrayList<User> userList)
    {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_follower, parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.followerName.setText(userList.get(position).getLogin());
    }

    @Override
    public int getItemCount()
    {
        return userList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.follower_name)
        TextView followerName;

        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}