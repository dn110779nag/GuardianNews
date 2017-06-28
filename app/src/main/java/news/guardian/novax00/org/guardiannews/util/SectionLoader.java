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
import java.util.ArrayList;
import java.util.List;

import news.guardian.novax00.org.guardiannews.structs.Section;

/**
 * Created by dn110 on 28.06.2017.
 */

public class SectionLoader extends AsyncTaskLoader<List<Section>> {


    public SectionLoader(Context context) {
        super(context);
    }

    @Override
    public List<Section> loadInBackground() {
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



    private List<Section> parseJson(String json) throws JSONException {
        if (json == null) {
            return null;
        }
        List<Section> list = new ArrayList<>();
        JSONObject data = new JSONObject(json);
        JSONObject response = data.getJSONObject("response");
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject section = results.getJSONObject(i);
            list.add(new Section(
                    section.getString("id"),
                    section.getString("webTitle")
            ));
        }
        return list;
    }

    private URL buildURL() throws MalformedURLException {
        Uri baseUri = Uri.parse(NetworkUtil.SECTIONS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", NetworkUtil.API_KEY);
        return new URL(uriBuilder.toString());
    }
}
