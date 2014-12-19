package pl.jahu.quizzy.ui;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends ListFragment implements SeekBar.OnSeekBarChangeListener {

    private static final int MARK_COLOR = Color.rgb(81, 171, 240);

    private Map<String, Integer[]> stats;
    private List<String> categories;
    private int diffLevel = Constants.DIFFICULTY_LEVEL_ALL;
    private int total = 0;

    private TextView totalLabel;
    private TextView diffInfoLabel;
    private Button startButton;

    public SetupFragment() {
        categories = new ArrayList<>();
    }

    public void setStats(Map<String, Integer[]> stats) {
        this.stats = stats;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        totalLabel = (TextView) rootView.findViewById(R.id.totalCountLabel);
        diffInfoLabel = (TextView) rootView.findViewById(R.id.difficultInfoLabel);
        startButton = (Button) rootView.findViewById(R.id.startQuizButton);

        categories.addAll(stats.keySet());
        Collections.sort(categories);
        setListAdapter(new CategoryListAdapter(inflater.getContext(), categories));

        SeekBar difficultBar = (SeekBar) rootView.findViewById(R.id.difficultBar);
        difficultBar.setOnSeekBarChangeListener(this);

        updateDifficultInfoLabel("All questions", Color.GREEN);
        return rootView;
    }

    private void updateDifficulty() {
        switch (diffLevel) {
            case Constants.DIFFICULTY_LEVEL_ALL:
                updateDifficultInfoLabel(getResources().getString(R.string.diff_level_desc_all), Color.GREEN);
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_75:
                updateDifficultInfoLabel(getResources().getString(R.string.diff_level_desc_75), Color.rgb(235, 235, 95));
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_50:
                updateDifficultInfoLabel(getResources().getString(R.string.diff_level_desc_50), Color.rgb(255, 179, 0));
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_25:
                updateDifficultInfoLabel(getResources().getString(R.string.diff_level_desc_25), Color.RED);
                break;
        }

        ListView listView = getListView();
        total = 0;
        for (int i = 0; i < listView.getChildCount(); i++) {
            View rowView = listView.getChildAt(i);
            TextView sizeLabel = (TextView) rowView.findViewById(R.id.categorySizeLabel);
            CheckBox chosenCheckBox = (CheckBox) rowView.findViewById(R.id.categoryChosenCheckBox);
            int questionsCount = stats.get(categories.get(i))[diffLevel];
            sizeLabel.setText(String.valueOf(questionsCount));
            if (chosenCheckBox.isChecked()) {
                total += questionsCount;
            }
        }
        totalLabel.setText(String.valueOf(total));
    }

    private void updateDifficultInfoLabel(String message, int color) {
        diffInfoLabel.setText(message);
        diffInfoLabel.setTextColor(color);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        diffLevel = progress;
        updateDifficulty();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
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
