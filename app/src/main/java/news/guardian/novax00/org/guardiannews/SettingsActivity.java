package news.guardian.novax00.org.guardiannews;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import news.guardian.novax00.org.guardiannews.structs.Article;
import news.guardian.novax00.org.guardiannews.structs.Section;
import news.guardian.novax00.org.guardiannews.util.SectionLoader;

/**
 * Created by dn110 on 28.06.2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }


    public static class SettingsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, LoaderManager.LoaderCallbacks<List<Section>> {
        private ListPreference listPreference;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);




//            Preference sections = findPreference(getString(R.string.section));
//            bindPreferenceSummaryToValue(sections);

            PreferenceScreen screen = this.getPreferenceScreen();

            PreferenceCategory category = new PreferenceCategory(screen.getContext());
            category.setTitle("Sections");
            screen.addPreference(category);


            listPreference =
                    new ListPreference(screen.getContext());

            listPreference.setKey("section");
            listPreference.setTitle("Chosen section");
            listPreference.setDefaultValue("politics");
            listPreference.setEntries(new String[]{
                    "Politics",
                    "Sports"
            });

            listPreference.setEntryValues(
                    new String[]{
                            "politics",
                            "sport"
                    });

            category.addPreference(listPreference);
            bindPreferenceSummaryToValue(listPreference);
            getLoaderManager().initLoader(1, null, this);
        }

//        @Override
//        public boolean onPreferenceChange(Preference preference, Object value) {
//            String stringValue = value.toString();
//            preference.setSummary(stringValue);
//            return true;
//        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public Loader<List<Section>> onCreateLoader(int id, Bundle args) {
            return new SectionLoader(this.getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<Section>> loader, List<Section> data) {
            String[] keys = new String[data.size()];
            String[] values = new String[data.size()];
            for(int i=0;i<keys.length; i++){
                keys[i] = data.get(i).getId();
                values[i] = data.get(i).getTitle();
            }
            listPreference.setEntries(values);
            listPreference.setEntryValues(keys);

        }

        @Override
        public void onLoaderReset(Loader<List<Section>> loader) {

        }
    }

}
