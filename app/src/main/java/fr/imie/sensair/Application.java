package fr.imie.sensair;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by mirouf on 16/05/17.
 */

public class Application extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
