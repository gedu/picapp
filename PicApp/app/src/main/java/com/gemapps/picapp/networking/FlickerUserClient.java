package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.UserItem;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by edu on 10/5/16.
 */

public class FlickerUserClient extends BaseHttpClient implements BaseHttpClient.CallbackResponse{

    private static final String TAG = "FlickerUserClient";
    public interface UserListener {
        void onFailure();
        void onSuccess(UserItem userItem);
    }

    public static final String USER_INFO_URL = "https://api.flickr.com/services/rest?" +
            "method=flickr.people.getInfo&" +
            "api_key=%s&user_id=%s&format=json&nojsoncallback=1";

    private UserListener mListener;

    public void getUserInfo(String userId, FlickerUserClient.UserListener responseListener) {

        mListener = responseListener;

        doGet(String.format(USER_INFO_URL, BuildConfig.FLICKR_API_KEY, userId), this);
    }

    @Override
    public void onFailure() {

        mListener.onFailure();
    }

    @Override
    public void onSuccess(String response) {

        try {
            JSONObject userObj = new JSONObject(response);

            Gson gson = new Gson();
            mListener.onSuccess(gson.fromJson(userObj.getString("person"), UserItem.class));

        } catch (JSONException e) {
            onFailure();
        }
    }


}