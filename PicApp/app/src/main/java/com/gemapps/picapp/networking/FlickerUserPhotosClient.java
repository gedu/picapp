package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.UserPhotoItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edu on 10/20/16.
 * Flickr api to get photos from a specific user
 */
public class FlickerUserPhotosClient extends BaseHttpClient
        implements BaseHttpClient.CallbackResponse {

    private static final String TAG = "FlickerUserPhotosClient";
    public interface UserPhotosListener {
        void onFailure();
        void onSuccess(ArrayList<UserPhotoItem> userPhotos);
    }

    /**
     * 1 api key
     * 2 user id
     */
    public static final String USER_PHOTOS_URL = "https://api.flickr.com/services/rest?" +
            "method=flickr.people.getPhotos&" +
            "api_key=%s&user_id=%s&format=json&nojsoncallback=1&" +
            "per_page=20&page=1";

    private UserPhotosListener mListener;

    public void getUserPhotos(String userId, UserPhotosListener listener){

        mListener = listener;

        doGet(String.format(USER_PHOTOS_URL, BuildConfig.FLICKR_API_KEY, userId), this);
    }

    @Override
    public void onFailure() {

        if(mListener != null) mListener.onFailure();
    }

    @Override
    public void onSuccess(String response) {

        try {
            JSONObject resObj = new JSONObject(response);
            JSONObject photosObj = new JSONObject(resObj.getString("photos"));
            JSONArray photos = new JSONArray(photosObj.getString("photo"));
            ArrayList<UserPhotoItem> userPhotos = new ArrayList<>();
            Gson gson = new Gson();

            for (int i = 0; i < photos.length(); i++) {
                userPhotos.add(gson.fromJson(photos.getString(i), UserPhotoItem.class));
            }

            if(mListener != null) mListener.onSuccess(userPhotos);

        } catch (JSONException e) {
            onFailure();
        }
    }
}
