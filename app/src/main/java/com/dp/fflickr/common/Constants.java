package com.dp.fflickr.common;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DinhPhuc on 24/04/2016.
 */
public class Constants {

    public static final String API_KEY = "64c0f179f8aec0444033c8b2c57a7db0";
    public static final String API_SECRET = "";

    public static final String ERR_CODE_FLICKR_UNAVAILABLE = "105";
    public static final int PHOTOS_PER_PAGE = 20;

    public static final Set<String> EXTRAS = new HashSet<String>();
    static {
        EXTRAS.add("owner_name");
        EXTRAS.add("url_q");  /* large square 150x150 */
        EXTRAS.add("url_n");  /* small, 240 on longest side */
        EXTRAS.add("url_h");
        EXTRAS.add("url_o");
        EXTRAS.add("views");
        EXTRAS.add("description");
        EXTRAS.add("tags");
    }
}
