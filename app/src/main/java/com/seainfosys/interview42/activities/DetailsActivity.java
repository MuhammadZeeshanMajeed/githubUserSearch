package com.seainfosys.interview42.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seainfosys.interview42.R;
import com.seainfosys.interview42.adapters.FollowersAdapter;
import com.seainfosys.interview42.models.User;
import com.seainfosys.interview42.network.NetworkHandler;
import com.seainfosys.interview42.network.NetworkInteractor;
import com.seainfosys.interview42.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public class DetailsActivity extends AppCompatActivity implements NetworkInteractor
{
    private User user;
    private Utils utils;
    private ArrayList<User> userArrayList;

    @BindView(R.id.user_avatar)
    ImageView userAvatar;

    @BindView(R.id.text_username)
    TextView userName;

    @BindView(R.id.text_email)
    TextView email;

    @BindView(R.id.btn_fetch_followers)
    Button fetchFollowers;

    @BindView(R.id.rvFollowers)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        utils = new Utils(this);

        if (getIntent().hasExtra("user"))
        {
            user = (User) getIntent().getSerializableExtra("user");
        }

        setUI();
    }

    private void setUI()
    {
        Picasso.get()
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.user_avatar_placeholder)
                .into(userAvatar);

        if (!TextUtils.isEmpty(user.getName()))
            userName.setText(user.getName().isEmpty() ? "" : user.getName());

        if (!TextUtils.isEmpty(user.getEmail()))
            email.setText(user.getEmail().isEmpty() ? "" : user.getEmail());
    }

    @OnClick(R.id.btn_fetch_followers)
    void fetchUserFollowers()
    {
        NetworkHandler networkHandler = new NetworkHandler(this, this);
        networkHandler.execute(user.getFollowersUrl());
    }

    @Override
    public void onRequestStart()
    {
        utils.showDialog();
    }

    @Override
    public void onRequestCompleted(String response)
    {
        utils.hideDialog();
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(response);
        JsonArray jsonArray = elem.getAsJsonArray();

        if (jsonArray.size() > 0)
        {
            userArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                userArrayList.add(gson.fromJson(jsonObject, User.class));
            }
            showFollowers();
        } else
        {
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                    .setTitle("Followers Search")
                    .setMessage(user.getName() + " has no followers")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onRequestError(String s)
    {
        utils.hideDialog();
    }

    private void showFollowers()
    {
        fetchFollowers.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        FollowersAdapter adapter = new FollowersAdapter(userArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
