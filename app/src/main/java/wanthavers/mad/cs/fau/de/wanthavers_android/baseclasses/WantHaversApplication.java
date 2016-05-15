package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

public class WantHaversApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO test if parse setup works in activity

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wanthavers") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://faui21f.informatik.uni-erlangen.de:8080/parse/").build()); //TODO - should be https should it not?
    }
}
