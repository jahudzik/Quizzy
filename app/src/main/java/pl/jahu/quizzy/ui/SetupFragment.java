package pl.jahu.quizzy.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.jahu.quizzy.app.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends Fragment {

    public SetupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        return rootView;
    }

}
