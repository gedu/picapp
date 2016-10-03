package com.gemapps.picapp.networking;

import com.gemapps.picapp.BuildConfig;

/**
 * Created by edu on 9/30/16.
 * Do the calls to the Flickr API.
 */
public class FlickrClient extends BaseHttpClient {

    private static final String TAG = "FlickrClient";

    public static final String PHOTOS_KEY = "photos";
    public static final String PHOTO_KEY = "photo";
    public static final String OWNER_NAME_KEY = "ownername";
    public static final String TITLE_KEY = "title";
    public static final String URL_N_KEY = "url_n";

    private static final String FLICKR_SEARCH_URL = "https://api.flickr.com/services/rest/" +
            "?method=flickr.photos.getRecent" +
            "&api_key=%s&per_page=%s&sort=relevance&parse_tags=1&" +
            "extras=count_comments,count_faves,owner_name,url_n,url_c,url_b" +
            "&page=1&text=atardecer&format=json&nojsoncallback=1";

    /**
     * farmId, serverId, id, secret and size
     *
     * size:
     * n for mobile/tablet list (320)
     *
     * c for mobile item (800)
     * b for tablet item (1024)
     */
    private static final String FLICKR_IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    private static final String PHOTO_PER_PAGE = "45";

    public void getPhotoList(CallbackResponse responseListener){

        doGet(String.format(FLICKR_SEARCH_URL, BuildConfig.FLICKR_API_KEY, PHOTO_PER_PAGE), responseListener);
    }

    public String buildPhotoUrl(String farm, String serverId, String id, String secret){
        return buildPhotoUrl(farm, serverId, id, secret, "n");
    }

    public String buildPhotoUrl(String farm, String serverId, String id, String secret, String size){
        return String.format(FLICKR_IMAGE_URL, farm, serverId, id, secret, size);
    }
}
