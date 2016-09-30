package com.gemapps.picapp.networking;

import android.util.Log;

import com.gemapps.picapp.BuildConfig;

/**
 * Created by edu on 9/30/16.
 * Do the calls to the Flickr API.
 */
public class FlickrClient extends BaseHttpClient {

    private static final String TAG = "FlickrClient";

    private static final String FLICKR_SEARCH_URL = "https://api.flickr.com/services/rest/?" +
            "method=flickr.photos.getRecent&api_key=%s&per_page=%s&format=json&nojsoncallback=1";

    private static final String PHOTO_PER_PAGE = "50";

    public void getPhotoList(){

        doGet(String.format(FLICKR_SEARCH_URL, BuildConfig.FLICKR_API_KEY, PHOTO_PER_PAGE), new CallbackResponse() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
            }
        });
    }
}
