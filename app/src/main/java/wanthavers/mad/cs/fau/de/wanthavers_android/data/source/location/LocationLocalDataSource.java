package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Location;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 11.06.2016.
 */
public class LocationLocalDataSource implements LocationDataSource {
    private static LocationLocalDataSource INSTANCE;

    private Context context;

    private LocationLocalDataSource(Context context) {
        this.context = checkNotNull(context);
    }

    public static LocationLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocationLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getReverseGeocoding(@NonNull double lat, @NonNull double lon, @NonNull GetReverseGeocodingCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }

    @Override
    public void createLocation(@NonNull Location location, @NonNull CreateLocationCallback callback) {
        //TODO
        callback.onCreateFailed();
    }

    @Override
    public void updateLocation(@NonNull long locationId, @NonNull Location location, @NonNull UpdateLocationCallback callback) {
        //TODO
        callback.onUpdateFailed();
    }

    @Override
    public void getSavedLocationsForLoggedInUser(@NonNull GetSavedLocationsForLoggedInUserCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }
}
