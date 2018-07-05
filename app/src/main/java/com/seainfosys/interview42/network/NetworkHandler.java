package com.seainfosys.interview42.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.seainfosys.interview42.utils.Utils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public class NetworkHandler extends AsyncTask<String, Void, String>
{
    private NetworkInteractor responseListener;
    private Context context;
    private boolean error;
    private boolean apiLimitExceeded = false;
    private JSONArray items;
    private String total_count, incomplete_results;

    public NetworkHandler(NetworkInteractor responseListener, Context context)
    {
        this.responseListener = responseListener;
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        if (this.responseListener != null)
        {
            responseListener.onRequestStart();
            Utils utils = new Utils(context);
            if (!utils.isNetworkConnected())
            {
                responseListener.onRequestError("No internet connection available.");
                this.cancel(true);
            }
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            OkHttpClient client = new OkHttpClient();

            String urlString = params[0];
            Log.d("url", urlString);

            URL url = new URL(urlString);

            Request request = new Request.Builder()
                    .url(url).build();

            Response response = client.newCall(request).execute();
            Log.d("response", response.toString());

            if (response.code() == 404)
            {
                error = true;
                return response.message();
            }

            InputStream inputStream = response.body().byteStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp;
            StringBuilder result = new StringBuilder();

            while ((temp = bufferedReader.readLine()) != null)
            {
                result.append(temp);
            }
            Log.e("web api json object", result.toString());

            return result.toString();
        } catch (Exception exception)
        {
            exception.printStackTrace();
            error = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response)
    {
        if (error)
        {
            responseListener.onRequestError(response);
        } else
        {
            responseListener.onRequestCompleted(response);
        }
    }
}
