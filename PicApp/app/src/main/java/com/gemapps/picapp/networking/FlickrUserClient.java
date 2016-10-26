package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.UserItem;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by edu on 10/5/16.
 *
 * Flickr api to get a user info
 */

public class FlickrUserClient extends BaseHttpClient implements BaseHttpClient.CallbackResponse{

    private static final String TAG = "FlickrUserClient";
    public interface UserListener {
        void onFailure();
        void onSuccess(UserItem userItem);
    }

    public static final String USER_INFO_URL = FLICKR_URL +
            "method=flickr.people.getInfo&" +
            "api_key=%s&user_id=%s&format=json&nojsoncallback=1";

    private UserListener mListener;

    public void getUserInfo(String userId, FlickrUserClient.UserListener responseListener) {

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

            mListener.onSuccess(parse(response));

        } catch (JSONException e) {
            onFailure();
        }
    }

    public UserItem parse(String response) throws JSONException {
        JSONObject userObj = new JSONObject(response);

        Gson gson = new Gson();
        return gson.fromJson(userObj.getString("person"), UserItem.class);
    }

}
