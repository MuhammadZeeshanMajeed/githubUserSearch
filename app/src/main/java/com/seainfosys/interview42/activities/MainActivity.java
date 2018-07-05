package com.seainfosys.interview42.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.gson.Gson;
import com.seainfosys.interview42.R;
import com.seainfosys.interview42.models.User;
import com.seainfosys.interview42.network.NetworkHandler;
import com.seainfosys.interview42.network.NetworkInteractor;
import com.seainfosys.interview42.network.URLManager;
import com.seainfosys.interview42.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public class MainActivity extends AppCompatActivity implements NetworkInteractor
{
    private Utils utils;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        utils = new Utils(this);
    }

    @OnClick(R.id.btnSubmit)
    void searchUser()
    {
        String searchString = etSearch.getText().toString();

        if (!searchString.isEmpty())
        {
            utils.hideSoftKeyboard(this, getCurrentFocus(), true);

            NetworkHandler networkHandler = new NetworkHandler(this, this);
            networkHandler.execute(URLManager.BASE_URL + searchString);
        } else
        {
            etSearch.setError("Please enter the user email");
        }
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
        User user = gson.fromJson(response, User.class);
        if (user != null)
        {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestError(String message)
    {
        utils.hideDialog();

        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle("Git Hub User Search")
                .setMessage(message)
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
