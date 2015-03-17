package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.utils.Constants;

import java.util.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends ListFragment implements SeekBar.OnSeekBarChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String CHOSEN_LEVEL_BUNDLE_KEY = "levelChosen";
    public static final String CHOSEN_CATEGORIES_BUNDLE_KEY = "categoriesChosen";
    private static final String ALL_CATEGORIES_BUNDLE_KEY = "categoriesAll";
    private static final String QUIZ_SIZE_PREFERENCE = "preference_quiz_size";

    private static final int[] LEVEL_DESCRIPTIONS = {R.string.diff_level_desc_all, R.string.diff_level_desc_75, R.string.diff_level_desc_50, R.string.diff_level_desc_25, R.string.diff_level_desc_0};
    private static final int[] LEVEL_COLORS = {Color.BLACK, Color.GREEN, Color.rgb(255, 179, 0), Color.RED, Color.BLACK};
    private static final int MARK_COLOR = Color.rgb(81, 171, 240);

    private OnFragmentInteractionListener fragmentListener;
    private CompoundButton.OnCheckedChangeListener totalCheckboxChangeListener;

    private Map<String, Integer[]> categoriesSizes;
    private final Set<String> chosenCategories;
    private int actualLevel;
    private String preferredQuizSize;

    private TextView levelInfoLabel;
    private CheckBox totalCheckbox;
    private TextView totalCountLabel;
    private Button startButton;

    protected static SetupFragment newInstance(Map<String, Integer[]> categoriesSizes) {
        SetupFragment fragment = new SetupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALL_CATEGORIES_BUNDLE_KEY, new HashMap<>(categoriesSizes));
        fragment.setArguments(bundle);
        return fragment;
    }

    protected static SetupFragment newInstance(Map<String, Integer[]> categoriesSizes, Set<String> chosenCategories, int chosenLevel) {
        SetupFragment fragment = new SetupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALL_CATEGORIES_BUNDLE_KEY, new HashMap<>(categoriesSizes));
        bundle.putInt(CHOSEN_LEVEL_BUNDLE_KEY, chosenLevel);
        bundle.putStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
        fragment.setArguments(bundle);
        return fragment;
    }

    public SetupFragment() {
        chosenCategories = new HashSet<>();
        actualLevel = Constants.DIFFICULTY_LEVEL_ALL;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SetupFragment.OnFragmentInteractionListener");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferredQuizSize = sharedPreferences.getString(QUIZ_SIZE_PREFERENCE, null);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        totalCheckbox = (CheckBox) rootView.findViewById(R.id.totalCheckbox);
        totalCountLabel = (TextView) rootView.findViewById(R.id.totalCountLabel);
        SeekBar difficultBar = (SeekBar) rootView.findViewById(R.id.difficultBar);
        levelInfoLabel = (TextView) rootView.findViewById(R.id.difficultInfoLabel);
        startButton = (Button) rootView.findViewById(R.id.startQuizButton);

        if (savedInstanceState != null) {
            // handling configuration changes
            actualLevel = savedInstanceState.getInt(CHOSEN_LEVEL_BUNDLE_KEY);
            chosenCategories.addAll(savedInstanceState.getStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY));
        } else {
            if (getArguments().containsKey(CHOSEN_LEVEL_BUNDLE_KEY)) {
                actualLevel = getArguments().getInt(CHOSEN_LEVEL_BUNDLE_KEY);
            }
            if (getArguments().containsKey(CHOSEN_CATEGORIES_BUNDLE_KEY)) {
                chosenCategories.addAll(getArguments().getStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY));
            }
        }

        if (getArguments().containsKey(ALL_CATEGORIES_BUNDLE_KEY)) {
            // fill categories list
            categoriesSizes = (Map) getArguments().getSerializable(ALL_CATEGORIES_BUNDLE_KEY);
            List<String> categoriesNames = new ArrayList<>(categoriesSizes.keySet());
            Collections.sort(categoriesNames);
            setListAdapter(new CategoryListAdapter(inflater.getContext(), categoriesNames));
        } else {
            categoriesSizes = new HashMap<>();
            // TODO set some message in empty list view
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.onStartQuizButtonClicked(chosenCategories, actualLevel, preferredQuizSize);
            }
        });

        totalCheckboxChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chosenCategories.addAll(categoriesSizes.keySet());
                } else {
                    chosenCategories.clear();
                }
                // for each visible list item: set checkbox and text color
                ListView listView = getListView();
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View view = listView.getChildAt(i);
                    CheckBox checkbox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
                    TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
                    TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);

                    boolean chosen = chosenCategories.contains(nameLabel.getText().toString());
                    checkbox.setChecked(chosen);
                    nameLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
                    sizeLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
                }
                updateLayout(true);
            }
        };
        totalCheckbox.setOnCheckedChangeListener(totalCheckboxChangeListener);

        difficultBar.setProgress(actualLevel);
        difficultBar.setOnSeekBarChangeListener(this);

        updateLayout(false);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHOSEN_LEVEL_BUNDLE_KEY, actualLevel);
        outState.putStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
    }

    private void updateLayout(boolean updateList) {
        if (updateList) {
            // for each visible list item: set checkbox and text color
            ListView listView = getListView();
            for (int i = 0; i < listView.getChildCount(); i++) {
                View view = listView.getChildAt(i);
                CheckBox checkbox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
                TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
                TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);

                boolean chosen = chosenCategories.contains(nameLabel.getText().toString());
                checkbox.setChecked(chosen);
                nameLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
                sizeLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
            }
        }

        // count total number of questions chosen
        int totalQuestionsNumber = 0;
        for (String chosenCategory : chosenCategories) {
            totalQuestionsNumber += categoriesSizes.get(chosenCategory)[actualLevel];
        }

        totalCheckbox.setOnCheckedChangeListener(null);
        totalCheckbox.setChecked(chosenCategories.size() == categoriesSizes.keySet().size());
        totalCheckbox.setOnCheckedChangeListener(totalCheckboxChangeListener);

        totalCountLabel.setText(String.valueOf(totalQuestionsNumber));

        // update start button label and enable/disable it
        if (totalQuestionsNumber > 0) {
            if (preferredQuizSize.equals(getResources().getText(R.string.pref_quiz_size_value_all)) || Integer.parseInt(preferredQuizSize) >= totalQuestionsNumber) {
                startButton.setText(String.valueOf(getResources().getText(R.string.start_button_all_label))
                        .replace("#", String.valueOf(totalQuestionsNumber)));
            } else {
                startButton.setText(String.valueOf(getResources().getText(R.string.start_button_limited_label))
                        .replace("@", preferredQuizSize)
                        .replace("#", String.valueOf(totalQuestionsNumber)));
            }
            startButton.setEnabled(true);
        } else {
            startButton.setText(getResources().getText(R.string.start_button_empty_label));
            startButton.setEnabled(false);
        }

        // update level info label
        levelInfoLabel.setText(LEVEL_DESCRIPTIONS[actualLevel]);
        levelInfoLabel.setTextColor(LEVEL_COLORS[actualLevel]);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        actualLevel = progress;

        ListView listView = getListView();
        for (int i = 0; i < listView.getChildCount(); i++) {
            // update questions number for each category
            View view = listView.getChildAt(i);
            TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);
            sizeLabel.setText(String.valueOf((int) categoriesSizes.get(nameLabel.getText().toString())[actualLevel]));
        }
        // for each visible list item: set checkbox and text color
        ListView listView1 = getListView();
        for (int i = 0; i < listView1.getChildCount(); i++) {
            View view = listView1.getChildAt(i);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
            TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);

            boolean chosen = chosenCategories.contains(nameLabel.getText().toString());
            checkbox.setChecked(chosen);
            nameLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
            sizeLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
        }
        updateLayout(true);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
        TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);

        boolean newCheckedState = !checkbox.isChecked();
        checkbox.setChecked(newCheckedState);

        String categoryName = nameLabel.getText().toString();
        if (newCheckedState) {
            chosenCategories.add(categoryName);
        } else {
            chosenCategories.remove(categoryName);
        }

        // for each visible list item: set checkbox and text color
        ListView listView = getListView();
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view1 = listView.getChildAt(i);
            CheckBox checkbox1 = (CheckBox) view1.findViewById(R.id.categoryChosenCheckBox);
            TextView nameLabel1 = (TextView) view1.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView) view1.findViewById(R.id.categorySizeLabel);

            boolean chosen = chosenCategories.contains(nameLabel1.getText().toString());
            checkbox1.setChecked(chosen);
            nameLabel1.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
            sizeLabel.setTextColor(chosen ? MARK_COLOR : Color.BLACK);
        }
        updateLayout(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(QUIZ_SIZE_PREFERENCE)) {
            preferredQuizSize = sharedPreferences.getString(key, null);
            updateLayout(false);
        }
    }

    private class CategoryListAdapter extends ArrayAdapter<String> {

        public CategoryListAdapter(Context context, List<String> items) {
            super(context, R.layout.row_category, R.id.categoryNameLabel, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView nameLabel = (TextView)row.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView)row.findViewById(R.id.categorySizeLabel);
            CheckBox checkbox = (CheckBox) row.findViewById(R.id.categoryChosenCheckBox);

            String category = getItem(position);
            boolean checked = chosenCategories.contains(category);
            nameLabel.setTextColor(checked ? MARK_COLOR : Color.BLACK);
            sizeLabel.setText(String.valueOf((int) categoriesSizes.get(category)[actualLevel]));
            sizeLabel.setTextColor(checked ? MARK_COLOR : Color.BLACK);
            checkbox.setChecked(checked);
            return row;
        }

    }

    public interface OnFragmentInteractionListener {
        public void onStartQuizButtonClicked(Set<String> chosenCategories, int chosenLevel, String preferredQuizSize);
    }

}
