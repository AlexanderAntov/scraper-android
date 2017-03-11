package antov.scraper.Services;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;

import antov.scraper.Models.NewsDataObject;
import antov.scraper.OnDataSendToActivity;

public class NewsProviderTask extends AsyncTask<String, Void, ArrayList<NewsDataObject>> {
    private HttpConstants mHttpConstants;
    private HttpProvider mHttpProvider;
    private OnDataSendToActivity mDataSendToActivity;

    public NewsProviderTask(Activity activity) {
        mHttpConstants = new HttpConstants();
        mHttpProvider = new HttpProvider();
        mDataSendToActivity = (OnDataSendToActivity)activity;
    }

    @Override
    protected ArrayList<NewsDataObject> doInBackground(String... urlRoute) {
        ArrayList<NewsDataObject> results = new ArrayList<NewsDataObject>();
        String newsResponse = null;

        try {
            newsResponse = mHttpProvider.performGetRequest(mHttpConstants.mRestAppUrl + urlRoute[0]);
            JSONArray JsonNewsResults = new JSONArray(newsResponse);
            for (int i = 0; i < JsonNewsResults.length(); i++) {
                JSONObject jsonObject = JsonNewsResults.getJSONObject(i);
                Bitmap newsItemImage = null;
                String imageUrl = jsonObject.getString("image");
                if (imageUrl != null && !imageUrl.contains("null")) {
                    newsItemImage = BitmapFactory.decodeStream(new URL(imageUrl.replaceAll(" ", "%20")).openConnection().getInputStream());
                }
                NewsDataObject obj = new NewsDataObject(
                        jsonObject.getString("title"),
                        jsonObject.getString("info"),
                        jsonObject.getString("url"),
                        newsItemImage,
                        jsonObject.getString("dateTime")
                );
                results.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsDataObject> result) {
        super.onPostExecute(result);
        mDataSendToActivity.sendData(result);
    }
}
