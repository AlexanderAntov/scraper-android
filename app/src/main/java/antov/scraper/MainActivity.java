package antov.scraper;

import android.app.ProgressDialog;
import android.content.Intent;
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import antov.scraper.Models.NewsDataObject;
import antov.scraper.Services.NewsProviderTask;

public class MainActivity extends AppCompatActivity implements OnDataSendToActivity {
    private final String mRestAppUrl = "https://scraper-api.herokuapp.com/";
    private final String mRestAppNewsSuffix = "news";
    private final String mRestAppWeatherSuffix = "weather";
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

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(new ArrayList<NewsDataObject>());
        mRecyclerView.setAdapter(mAdapter);
        mProgressDialog = new ProgressDialog(this);
        showLoader();

        new NewsProviderTask(this).execute(mRestAppUrl + mRestAppNewsSuffix);
    }

    @Override
    public void sendData(ArrayList<NewsDataObject> list) {
        try {
            mAdapter = new RecyclerViewAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
            mProgressDialog.dismiss();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.i("Error!", exceptionAsString);
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
        if (id == R.id.action_news) {
            new NewsProviderTask(this).execute(mRestAppUrl + mRestAppNewsSuffix);
            showLoader();
        } else if (id == R.id.action_weather) {
            new NewsProviderTask(this).execute(mRestAppUrl + mRestAppWeatherSuffix);
            showLoader();
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

    private void showLoader() {
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait while we scrape the net");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
