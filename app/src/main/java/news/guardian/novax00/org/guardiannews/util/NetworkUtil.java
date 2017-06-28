package news.guardian.novax00.org.guardiannews.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static news.guardian.novax00.org.guardiannews.util.AppUtil.LOG_TAG;

/**
 * Created by dn110 on 28.06.2017.
 */

public class NetworkUtil {
    public static String API_KEY = "test";
    public static String SECTIONS_URL = "https://content.guardianapis.com/sections";
    public static String SEARCH_URL = "https://content.guardianapis.com/search";
    private static int READ_TIMEOUT = 5000;
    private static int CONNECTION_TIMEOUT = 5000;


    public static String loadFromNetwork(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(false);
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();


        if (urlConnection.getResponseCode() < 400) {
            InputStream inputStream = urlConnection.getInputStream();
            return readFromStream(inputStream);
        } else {
            Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            InputStream inputStream = urlConnection.getErrorStream();
            if(inputStream!=null){
                throw new IOException(readFromStream(inputStream));
            } else {
                throw new IOException(urlConnection.getResponseMessage());
            }
        }

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

//        Log.v(LOG_TAG, "readFromStream");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        int msize = 4096;
        byte[] buf = new byte[msize];
        int s;
        while ((s = inputStream.read(buf, 0, msize)) != -1) {
            b.write(buf, 0, s);
        }
        return new String(b.toByteArray());
    }

    public static boolean checkConnectivity(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnectedOrConnecting();
    }




}
