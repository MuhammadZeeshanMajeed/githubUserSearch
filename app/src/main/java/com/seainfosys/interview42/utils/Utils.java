package com.seainfosys.interview42.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.seainfosys.interview42.R;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public class Utils
{
    private ProgressDialog progressDialog;
    private Context context;

    public Utils(Context context)
    {
        this.context = context;
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(context, R.style.ProgressDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting Data ...");
        progressDialog.show();
    }

    public void hideDialog()
    {
        if (null != progressDialog)
        {
            progressDialog.hide();
        }
    }

    public static void hideSoftKeyboard(Context context, View view, boolean clearFocus)
    {
        if (context == null || view == null)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

        if (clearFocus)
            view.clearFocus();
    }
}
