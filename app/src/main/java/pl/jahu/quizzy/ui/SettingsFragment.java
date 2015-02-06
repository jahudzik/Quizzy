package pl.jahu.quizzy.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import pl.jahu.quizzy.app.R;

/**
 * Quizzy
 * Created by jahudzik on 2015-02-06.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
