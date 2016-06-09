package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.FilterDatabaseHelper;

public class WantHaversApplication extends Application {


    private static DesireFilter mDesireFilter;

    @Override
    public void onCreate() {
        super.onCreate();


        //load picasso
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

    public DesireFilter getCurDesireFilter(){

        DesireFilter tmpFilter = new DesireFilter();

        if(mDesireFilter == null){
            FilterDatabaseHelper filtDataHelper = FilterDatabaseHelper.getInstance(getApplicationContext());

            try {
               tmpFilter = filtDataHelper.getById(0);
            }catch (Exception e){
                //do nothing as no default filter is specified
            }

        }

        return mDesireFilter;
    }


    public void setCurDesireFilter(DesireFilter desireFilter){
        mDesireFilter = desireFilter;
    }

    public void setDefaultFilter(DesireFilter desireFilter){

        //ensure id of filter set to "0"
        if(desireFilter.getId != 0){
            System.err.println("Default filter has to have the id 0\n, filter was not set");
            return;
        }

        mDesireFilter = desireFilter;
        FilterDatabaseHelper filtDataHelper = FilterDatabaseHelper.getInstance(getApplicationContext());
        filtDataHelper.createOrUpdate(desireFilter);

    }


}
