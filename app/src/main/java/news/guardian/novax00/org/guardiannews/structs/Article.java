package news.guardian.novax00.org.guardiannews.structs;

import java.util.Date;

/**
 * Created by dn110 on 28.06.2017.
 */

public class Article {
    private final String title;
    private final String webUrl;
    private final Date publishDate;


    public Article(String title, String webUrl, Date publishDate) {
        this.title = title;
        this.webUrl = webUrl;
        this.publishDate = publishDate;
    }


    public String getTitle() {
        return title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }
}
