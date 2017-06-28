package news.guardian.novax00.org.guardiannews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import news.guardian.novax00.org.guardiannews.structs.Article;
import news.guardian.novax00.org.guardiannews.util.ArticleLoader;
import news.guardian.novax00.org.guardiannews.util.NetworkUtil;
import static news.guardian.novax00.org.guardiannews.util.AppUtil.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, AbsListView.OnScrollListener {

    private ArticleAdapter adapter;
    private String previosSection = "";
    private int currentPage;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(findViewById(R.id.emptyMessage));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = (Article) adapterView.getItemAtPosition(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(article.getWebUrl()));
                startActivity(browserIntent);
            }
        });

        listView.setOnScrollListener(this);



        // Create a new {@link ArrayAdapter} of earthquakes
        this.adapter = new ArticleAdapter(this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(adapter);


        if(NetworkUtil.checkConnectivity(this.getApplicationContext())) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            findViewById(R.id.progress).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.emptyMessage)).setText("No internet connection");
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPrefs.getString(
                getString(R.string.section),
                getString(R.string.settings_politic));

        if(!this.previosSection.equals(section)){
            this.currentPage = 1;
            this.previosSection = section;
            this.adapter.setData(null);
        }

        return new ArticleLoader(this, section, this.currentPage);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        findViewById(R.id.progress).setVisibility(View.GONE);
        if(data==null){
            ((TextView)findViewById(R.id.emptyMessage)).setText("no data available");
        } else{
            this.adapter.addData(data);
            this.currentPage++;
            this.loading = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        findViewById(R.id.progress).setVisibility(View.GONE);
        adapter.setData(null);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.v(LOG_TAG, "onScrollStateChanged");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.v(LOG_TAG, "onScroll");
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
        if(loadMore && adapter!=null && !loading){
            getLoaderManager().restartLoader(0, null, this);
            loading = true;
//            adapter.addData(new Article("title"+firstVisibleItem + visibleItemCount,
//                    "", new Date()));
        }
    }
}
