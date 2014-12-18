package pl.jahu.quizzy;

import android.app.Application;
import dagger.ObjectGraph;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
public class QuizzyApplication extends Application {

    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(new AppModule(this));
    }

    public void inject(Object object) {
        graph.inject(object);
    }

}
