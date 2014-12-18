package pl.jahu.quizzy.ui;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends ListFragment {

    private Map<String, Integer[]> stats;

    public SetupFragment() {
    }

    public void setStats(Map<String, Integer[]> stats) {
        this.stats = stats;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        TextView label = (TextView)rootView.findViewById(R.id.setupSummaryTextView);

        List<String> list = new ArrayList<>();
        list.addAll(stats.keySet());
        Collections.sort(list);
        setListAdapter(new CategoryListAdapter(inflater.getContext(), list));

        return rootView;
    }


    private class CategoryListAdapter extends ArrayAdapter<String> implements View.OnTouchListener {


        public CategoryListAdapter(Context context, List<String> items) {
            super(context, R.layout.category_row, R.id.categoryNameLabel, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView nameLabel = (TextView)row.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView)row.findViewById(R.id.categorySizeLabel);
            String category = getItem(position);
            nameLabel.setText(category);
            sizeLabel.setText("(" + stats.get(category)[Constants.DIFFICULTY_LEVEL_ALL] + ")");
            row.setOnTouchListener(this);
            return row;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            CheckBox categoryChosenCheckBox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    categoryChosenCheckBox.setChecked(!categoryChosenCheckBox.isChecked());
                    break;
            }
            return true;
        }
    }

}
