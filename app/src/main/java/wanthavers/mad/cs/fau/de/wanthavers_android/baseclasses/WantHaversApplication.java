package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class WantHaversApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        //built.setIndicatorsEnabled(true);
        //built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }



    protected void  attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
