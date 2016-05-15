package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.Message;

public class WantHaversApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        //Register parse model
        ParseObject.registerSubclass(Message.class);

        //initialise Parse Server for Chat  - done here to ensure its only done once in the application
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wanthavers") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://faui21f.informatik.uni-erlangen.de:8080/parse/").build()); //TODO - should be https should it not?
    }

    protected void  attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
