package news.guardian.novax00.org.guardiannews;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import news.guardian.novax00.org.guardiannews.structs.Article;

/**
 * Created by dn110 on 28.06.2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
    public final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public ArticleAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article info = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        String time = timeFormat.format(info.getPublishDate());
        String date = dateFormat.format(info.getPublishDate());
        ((TextView) convertView.findViewById(R.id.date)).setText(date+"\n"+time);
        ((TextView) convertView.findViewById(R.id.article)).setText(info.getTitle());
        return convertView;
    }

    public void setData(List<Article> data) {
        clear();
        if (data != null) {
            addAll(data);
        }

    }
}
