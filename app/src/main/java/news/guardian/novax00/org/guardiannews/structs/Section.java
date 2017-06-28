package news.guardian.novax00.org.guardiannews.structs;

/**
 * Created by dn110 on 28.06.2017.
 */

public class Section {

    private String id;
    private String title;

    public Section(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
