package wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.PollingTask;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.WantHaverScheduledExecutor;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.FilterDatabaseHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.LocationDatabaseHelper;

public class WantHaversApplication extends MultiDexApplication {

    private static boolean mNewMessages = false;
    WantHaverScheduledExecutor mWantHaverScheduledExecutor;
    private static final int BACKUP_POLLING_DELAY= 30;
    private static boolean mGpsActive = true;
    private static final double DEFAULT_RADIUS = 100.0;

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


        startNotificationBackupThread();

        DesireFilter startupFilter = new DesireFilter();

        Location location = getLocation(getApplicationContext());

        if(location == null) {
            location = new Location();
            location.setCityName("Erlangen");
            location.setFullAddress("Martensstraße 1, 91058 Erlangen");
            location.setLon(11.027172803878784);
            location.setLat(49.573829301806924);
            setLocation(location,getApplicationContext());
        }

        //startupFilter.setLocation(location);

        startupFilter.setDescription("default");
        startupFilter.setCityName(location.getCityName());
        startupFilter.setFullAddress(location.getFullAddress());
        startupFilter.setLon(location.getLon());
        startupFilter.setLat(location.getLat());
        startupFilter.setRadius(DEFAULT_RADIUS);

        setDesireFilter(startupFilter, getApplicationContext());
    }

    @Override
    public void onTerminate(){
        super.onTerminate();

        mWantHaverScheduledExecutor.shutdownNow();
    }


    private void startNotificationBackupThread(){

        mWantHaverScheduledExecutor = new WantHaverScheduledExecutor(1);

        PollingTask pollingTask = new PollingTask();
        pollingTask.setAppContext(getApplicationContext());

        mWantHaverScheduledExecutor.scheduleWithFixedDelay(pollingTask,BACKUP_POLLING_DELAY, BACKUP_POLLING_DELAY, TimeUnit.SECONDS);
    }

    public static DesireFilter getDesireFilter(Context context){

        DesireFilter desireFilter = new DesireFilter();
        FilterDatabaseHelper filtDataHelper = FilterDatabaseHelper.getInstance(context);

        try {
            desireFilter = filtDataHelper.getById(0);
        }catch (Exception e){
            return new DesireFilter();
            //do nothing as no default filter is specified
        }

        return desireFilter;

    }


    public static void setDesireFilter(DesireFilter desireFilter, Context context){
        desireFilter.setId(0);

        FilterDatabaseHelper filtDataHelper = FilterDatabaseHelper.getInstance(context);
        filtDataHelper.createOrUpdate(desireFilter);
    }


    public static Location getLocation(Context context){

        Location location = new Location();
        LocationDatabaseHelper locDatabaseHelper = LocationDatabaseHelper.getInstance(context);

        try {
            location = locDatabaseHelper.getById(0);
        }catch (Exception e){
            return new Location();
            //do nothing as no default filter is specified
        }

        return location;
    }


    public static void setLocation(Location location, Context context){
        location.setId(0);

        LocationDatabaseHelper filtDataHelper = LocationDatabaseHelper.getInstance(context);
        filtDataHelper.createOrUpdate(location);

        //update filter on update location
        DesireFilter curDesireFilter = getDesireFilter(context);
        if(curDesireFilter == null){
            curDesireFilter = new DesireFilter();
        }

        curDesireFilter.setCityName(location.getCityName());
        curDesireFilter.setFullAddress(location.getFullAddress());
        curDesireFilter.setDescription(location.getDescription());
        curDesireFilter.setLat(location.getLat());
        curDesireFilter.setLon(location.getLon());
        setDesireFilter(curDesireFilter, context);
    }




    public static void setNewMessages(boolean newMessages){
        mNewMessages = newMessages;
    }

    public static boolean getNewMessages(){
        return mNewMessages;
    }

    public static void setGpsActive(boolean gpsActive){
        mGpsActive = gpsActive;
    }

    public static boolean getGpsActive(){
        return mGpsActive;
    }

    public static double getDefaultRadius() {
        return DEFAULT_RADIUS;
    }
}
