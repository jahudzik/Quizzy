package pl.jahu.quizzy.ui;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private static final int MARK_COLOR = Color.rgb(81, 171, 240);

    private Map<String, Integer[]> stats;
    private List<String> categories;
    private int diffLevel = Constants.DIFFICULTY_LEVEL_ALL;
    private int total = 0;

    private TextView totalLabel;
    private Button startButton;

    public SetupFragment() {
        categories = new ArrayList<>();
    }

    public void setStats(Map<String, Integer[]> stats) {
        this.stats = stats;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        totalLabel = (TextView) rootView.findViewById(R.id.totalCountLabel);
        startButton = (Button) rootView.findViewById(R.id.startQuizButton);

        categories.addAll(stats.keySet());
        Collections.sort(categories);
        setListAdapter(new CategoryListAdapter(inflater.getContext(), categories));

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
            sizeLabel.setText(stats.get(category)[Constants.DIFFICULTY_LEVEL_ALL] + "");
            row.setOnTouchListener(this);
            return row;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            CheckBox categoryChosenCheckBox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
                    TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);
                    boolean isChecked = !categoryChosenCheckBox.isChecked();
                    categoryChosenCheckBox.setChecked(isChecked);
                    String categoryName = nameLabel.getText().toString();
                    int questionsCount = stats.get(categoryName)[diffLevel];
                    total = (isChecked) ? total + questionsCount : total - questionsCount;
                    nameLabel.setTextColor((isChecked) ? MARK_COLOR : Color.BLACK);
                    sizeLabel.setTextColor((isChecked) ? MARK_COLOR : Color.BLACK);
                    totalLabel.setText(String.valueOf(total));
                    startButton.setEnabled(total > 0);
                    break;
            }
            return true;
        }
    }

}
