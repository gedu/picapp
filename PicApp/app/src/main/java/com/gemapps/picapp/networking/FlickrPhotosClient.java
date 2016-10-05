package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;
import com.gemapps.picapp.ui.model.PicItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edu on 9/30/16.
 * Do the calls to the Flickr API.
 */
public class FlickrPhotosClient extends BaseHttpClient implements BaseHttpClient.CallbackResponse {

    public interface FlickrListener {
        void onFailure();

        void onSuccess(List<PicItem> items);
    }

    private static final String TAG = "FlickrPhotosClient";

    public static final String PHOTOS_KEY = "photos";
    public static final String PHOTO_KEY = "photo";

    private static final String QUERY_PARAM = "text=%s&";
    private static final String GET_RECENT_METHOD = "getRecent";
    private static final String SEARCH_METHOD = "search";

    /**
     * 1 api_key
     * 2 per_page
     * 3 pagination
     * 4 query
     * 5 method
     */
    private static final String FLICKR_SEARCH_URL = "https://api.flickr.com/services/rest?" +
            "sort=relevance&parse_tags=1&content_type=7&api_key=%s&per_page=%s&page=%s&" +
            "extras=owner,date_taken,count_comments,count_faves,owner_name,url_n,url_c,url_b" +
            "&%sformat=json&nojsoncallback=1&method=flickr.photos.%s";

    /**
     * farmId, serverId, id, secret and size
     * <p>
     * size:
     * n for mobile/tablet list (320)
     * <p>
     * c for mobile item (800)
     * b for tablet item (1024)
     */
    private static final String FLICKR_IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    private static final String PHOTO_PER_PAGE = "45";

    private FlickrListener mListener;

    public void getPhotoList(String page, String query, FlickrListener responseListener) {

        mListener = responseListener;

        String queryParam = query.length() > 0 ? String.format(QUERY_PARAM, query).replace(" ", "%20") : "";
        String method = query.length() > 0 ? SEARCH_METHOD : GET_RECENT_METHOD;

        doGet(String.format(FLICKR_SEARCH_URL, BuildConfig.FLICKR_API_KEY, PHOTO_PER_PAGE, page, queryParam, method), this);
    }

    public static String buildPhotoUrl(String farm, String serverId, String id, String secret) {
        return buildPhotoUrl(farm, serverId, id, secret, "n");
    }

    public static String buildPhotoUrl(String farm, String serverId, String id, String secret, String size) {
        return String.format(FLICKR_IMAGE_URL, farm, serverId, id, secret, size);
    }

    @Override
    public void onFailure() {

        if (mListener != null) mListener.onFailure();
    }

    @Override
    public void onSuccess(String response) {

        JSONObject photoObj = null;
        try {
            photoObj = new JSONObject(response);

            JSONObject photoContent = new JSONObject(photoObj.getString(PHOTOS_KEY));
            JSONArray photos = new JSONArray(photoContent.getString(PHOTO_KEY));

            int length = photos.length();
            Gson gson = new Gson();
            List<PicItem> picItems = new ArrayList<PicItem>();
            for (int i = 0; i < length; i++) {

                PicItem picItem = gson.fromJson(photos.getString(i), PicItem.class);
                picItems.add(picItem);
            }

            if (mListener != null) mListener.onSuccess(picItems);

        } catch (JSONException e) {
            onFailure();
        }

    }
}
