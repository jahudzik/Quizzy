package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.os.Bundle;
import pl.jahu.quizzy.QuizzyApplication;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((QuizzyApplication) getApplication()).inject(this);
    }
}
