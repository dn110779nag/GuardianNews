package news.guardian.novax00.org.guardiannews.util;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import news.guardian.novax00.org.guardiannews.structs.Article;
import news.guardian.novax00.org.guardiannews.structs.Section;

/**
 * Created by dn110 on 28.06.2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private String section;

    public ArticleLoader(Context context, String section) {
        super(context);
        this.section = section;
    }


    @Override
    public List<Article> loadInBackground() {
        try {
            URL url = buildURL();
            String json = NetworkUtil.loadFromNetwork(url);
            return parseJson(json);
        } catch (Exception ex) {
            Log.e(AppUtil.LOG_TAG, "Error section loading:", ex);
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    private List<Article> parseJson(String json) throws JSONException, ParseException {
        if (json == null) {
            return null;
        }
        List<Article> list = new ArrayList<>();
        JSONObject data = new JSONObject(json);
        JSONObject response = data.getJSONObject("response");
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject section = results.getJSONObject(i);
            list.add(new Article(
                    section.getString("webTitle"),
                    section.getString("webUrl"),
                    sdf.parse(section.getString("webPublicationDate"))
            ));
        }
        return list;
    }

    private URL buildURL() throws MalformedURLException {
        Uri baseUri = Uri.parse(NetworkUtil.SEARCH_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", NetworkUtil.API_KEY);
        uriBuilder.appendQueryParameter("lang", "en");
        uriBuilder.appendQueryParameter("section", section);
        return new URL(uriBuilder.toString());
    }
}
