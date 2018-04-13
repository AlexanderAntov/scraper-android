package antov.scraper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import antov.scraper.Models.NewsDataObject;
import antov.scraper.Services.HttpConstants;
import antov.scraper.Services.NewsProviderTask;

public class MainActivity extends AppCompatActivity implements OnDataSendToActivity {
    private HttpConstants mHttpConstants;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHttpConstants = new HttpConstants();
        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(new ArrayList<NewsDataObject>());
        mRecyclerView.setAdapter(mAdapter);
        mProgressDialog = new ProgressDialog(this);

        if (isNetworkAvailable()) {
            showLoader();
            new NewsProviderTask(this).execute(mHttpConstants.mNewsSuffix);
        } else {
            ImageView networkErrorImage = (ImageView)findViewById(R.id.network_error_image);
            networkErrorImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendData(ArrayList<NewsDataObject> list) {
        try {
            if (list.isEmpty()) {
                ImageView noNewDataImage = (ImageView)findViewById(R.id.no_new_data_image);
                noNewDataImage.setVisibility(View.VISIBLE);
            }

            mAdapter = new RecyclerViewAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
            mProgressDialog.dismiss();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.i("Error!", exceptionAsString);

            ImageView networkErrorImage = (ImageView)findViewById(R.id.network_error_image);
            networkErrorImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_all_news:
                requestData(mHttpConstants.mNewsSuffix);
                break;
            case R.id.action_weather:
                requestData(mHttpConstants.mWeather);
                break;
            case R.id.action_bbc:
                requestData(mHttpConstants.mBBCNews);
                break;
            case R.id.action_cnn:
                requestData(mHttpConstants.mCNNNews);
                break;
            case R.id.action_google:
                requestData(mHttpConstants.mGoogleNews);
                break;
            case R.id.action_nyt:
                requestData(mHttpConstants.mNYTNews);
                break;
            case R.id.action_reuters:
                requestData(mHttpConstants.mReutersNews);
                break;
            case R.id.tech_and_science:
                requestData(mHttpConstants.mTechAndScience);
                break;
            case R.id.programming:
                requestData(mHttpConstants.mProgramming);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter
                .ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String url = ((RecyclerViewAdapter) mAdapter).getDataObject(position).getmUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(browserIntent, "Chose browser"));
            }
        });
    }

    private void requestData(String url) {
        new NewsProviderTask(this).execute(url);
        showLoader();
    }

    private void showLoader() {
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait while we scrape the net");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
