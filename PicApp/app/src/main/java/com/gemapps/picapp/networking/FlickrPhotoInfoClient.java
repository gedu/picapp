package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.FlickrDeserializer;
import com.gemapps.picapp.ui.model.PhotoInfoItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.gemapps.picapp.ui.model.FlickrDeserializer.PHOTO_KEY;
/**
 * Created by edu on 10/26/16.
 * Flickr api to get info about the photo
 */
public class FlickrPhotoInfoClient extends BaseHttpClient
        implements BaseHttpClient.CallbackResponse{

    private static final String TAG = "FlickrPhotoInfoClient";

    public interface PhotoInfoListener {
        void onFailure();
        void onSuccess(PhotoInfoItem photoItem);
    }

    public static final String PHOTO_INFO_URL = FLICKR_URL +
            "method=flickr.photos.getInfo&api_key=%s&photo_id=%s&format=json&nojsoncallback=1";

    private PhotoInfoListener mListener;

    public void getPhotoInfo(String photoId, PhotoInfoListener listener){

        mListener = listener;

        doGet(String.format(PHOTO_INFO_URL, BuildConfig.FLICKR_API_KEY, photoId), this);
    }


    @Override
    public void onFailure() {

        mListener.onFailure();
    }

    @Override
    public void onSuccess(String response) {

        Gson gson = new GsonBuilder().registerTypeAdapter(PhotoInfoItem.class,
                new FlickrDeserializer<PhotoInfoItem>(PHOTO_KEY)).create();

        PhotoInfoItem photoItem = gson.fromJson(response, PhotoInfoItem.class);

        mListener.onSuccess(photoItem);
    }
}
