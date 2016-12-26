package antov.scraper.Models;

import android.graphics.Bitmap;

public class NewsDataObject {
    private String mHeadline;
    private String mShortInfo;
    private Bitmap mImage;
    private String mUrl;
    private String mDateTime;

    public NewsDataObject(String headline, String shortInfo, String url, Bitmap image, String dateTime){
        mHeadline = headline;
        mShortInfo = shortInfo;
        mUrl = url;
        mImage = image;
        mDateTime = dateTime;
    }

    NewsDataObject(String headline, String shortInfo, String url){
        this(headline, shortInfo, url, null, null);
    }

    NewsDataObject(String headline, String shortInfo){
        this(headline, shortInfo, null, null, null);
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmShortInfo() {
        return mShortInfo;
    }

    public void setmShortInfo(String mShortInfo) {
        this.mShortInfo = mShortInfo;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String url) {
        this.mUrl = url;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap image) {
        this.mImage = image;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String dateTime) {
        this.mDateTime = dateTime;
    }
}