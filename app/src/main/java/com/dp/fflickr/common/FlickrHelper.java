package com.dp.fflickr.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.interestingness.InterestingnessInterface;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by DinhPhuc on 24/04/2016.
 */
public final class FlickrHelper {

    public static final String TAG = "fflickr/flickrHelper";
    private static FlickrHelper instance;

    public static FlickrHelper getInstance() {
        if(instance == null)
            instance = new FlickrHelper();
        return instance;
    }

    public Flickr getFlickr() {
        try{
            return new Flickr(Constants.API_KEY, Constants.API_SECRET, new REST());
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    public InterestingnessInterface getInterestingInterface() {
        Flickr f = getFlickr();
        if (f != null) {
            return f.getInterestingnessInterface();
        } else {
            return null;
        }
    }

    public boolean handleFlickrUnavailable(Context context, Exception e) {
        if (e != null && e instanceof FlickrException) {
            if (((FlickrException) e).getErrorCode().equals(
                    Constants.ERR_CODE_FLICKR_UNAVAILABLE)) {
                e.printStackTrace();
                Log.w(TAG, "Flickr is not available");
                Toast.makeText(context, "Flickr is not available at the moment",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}
