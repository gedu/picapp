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

    private static final String QUERY_PARAM = "text=%s&";
    private static final String GET_RECENT_METHOD = "getRecent";
    private static final String SEARCH_METHOD = "search";

    /**
     * 1 api_key
     * 2 per_page
     * 3 query
     * 4 method
     */
    private static final String FLICKR_SEARCH_URL = "https://api.flickr.com/services/rest?" +
            "sort=relevance&parse_tags=1&content_type=7&api_key=%s&per_page=%s&" +
            "extras=count_comments,count_faves,owner_name,url_n,url_c,url_b" +
            "&page=1&%sformat=json&nojsoncallback=1&method=flickr.photos.%s";

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

    public void getPhotoList(String query, CallbackResponse responseListener){

        String queryParam = query.length() > 0 ? String.format(QUERY_PARAM, query).replace(" ", "%20") : "";
        String method = query.length() > 0 ? SEARCH_METHOD : GET_RECENT_METHOD;

        doGet(String.format(FLICKR_SEARCH_URL, BuildConfig.FLICKR_API_KEY, PHOTO_PER_PAGE, queryParam, method), responseListener);
    }

    public String buildPhotoUrl(String farm, String serverId, String id, String secret){
        return buildPhotoUrl(farm, serverId, id, secret, "n");
    }

    public String buildPhotoUrl(String farm, String serverId, String id, String secret, String size){
        return String.format(FLICKR_IMAGE_URL, farm, serverId, id, secret, size);
    }
}
