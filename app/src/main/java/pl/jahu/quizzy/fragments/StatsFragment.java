package pl.jahu.quizzy.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;


public class StatsFragment extends Fragment {

    private TextView correctCountTextView;
    private TextView wrongCountTextView;

    public StatsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        correctCountTextView = (TextView) rootView.findViewById(R.id.correctCountValue);
        wrongCountTextView = (TextView) rootView.findViewById(R.id.wrongCountValue);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setValues(int overall, int correct, int wrong) {
        String correctLabel = ((correct < 10) ? " " : "") + correct + "/" + overall;
        correctCountTextView.setText(correctLabel);
        wrongCountTextView.setText(String.valueOf(wrong));
    }

}
