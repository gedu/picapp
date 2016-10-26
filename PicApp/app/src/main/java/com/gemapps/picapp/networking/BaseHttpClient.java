package com.gemapps.picapp.networking;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edu on 9/30/16.
 */

public class BaseHttpClient {

    private static final String TAG = "BaseHttpClient";
    protected static final String FLICKR_URL = "https://api.flickr.com/services/rest?";


    public interface CallbackResponse {
        void onFailure();

        void onSuccess(String response);
    }

    private Handler mHandler;
    private CallbackResponse mListener;

    public BaseHttpClient() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    protected void doGet(String url, CallbackResponse listener){

        mListener = listener;

        final OkHttpClient httpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String body = response.body().string();
                response.body().close();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onSuccess(body);
                    }
                });
            }
        });
    }

}
