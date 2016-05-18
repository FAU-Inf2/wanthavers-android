package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

public class WantHaversApplication extends Application {



    protected void  attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
