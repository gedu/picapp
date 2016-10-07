package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.CommentItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edu on 10/7/16.
 *
 * Flickr api to get comments from a photo
 */
public class FlickrCommentsClient extends BaseHttpClient implements BaseHttpClient.CallbackResponse {

    public interface CommentsListener {
        void onFailure();
        void onSuccess(ArrayList<CommentItem> comments);
    }

    private static final String COMMENTS_URL = "https://api.flickr.com/services/rest?" +
            "method=flickr.photos.comments.getList&api_key=%s&photo_id=%s&format=json&nojsoncallback=1";

    private CommentsListener mListener;

    public void getComments(String photoId, CommentsListener listener){

        mListener = listener;

        doGet(String.format(COMMENTS_URL, BuildConfig.FLICKR_API_KEY, photoId), this);
    }

    @Override
    public void onFailure() {

        if(mListener != null) mListener.onFailure();
    }

    @Override
    public void onSuccess(String response) {

        try {
            JSONObject commentObj = new JSONObject(response);
            JSONArray comments = new JSONArray(commentObj.getString("comment"));

            ArrayList<CommentItem> commentItems = new ArrayList<>();
            Gson gson = new Gson();
            int length = comments.length();

            for (int i = 0; i < length; i++) {
                commentItems.add(gson.fromJson(comments.getString(i), CommentItem.class));
            }

            if(mListener != null) mListener.onSuccess(commentItems);

        } catch (JSONException e) {
            onFailure();
        }
    }
}
