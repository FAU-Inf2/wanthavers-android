package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.LocationClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 11.06.2016.
 */
public class LocationRemoteDataSource implements LocationDataSource {
    private static LocationRemoteDataSource INSTANCE;

    private LocationClient locationClient;

    private LocationRemoteDataSource(Context context) {
        locationClient = LocationClient.getInstance(checkNotNull(context));
    }

    public static LocationRemoteDataSource getInstance(Context context) {
        checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new LocationRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getReverseGeocoding(@NonNull double lat, @NonNull double lon, @NonNull GetReverseGeocodingCallback callback) {
        try {
            Location ret = locationClient.getReverseGeocoding(lat, lon);
            callback.onReverseGeocodingLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createLocation(@NonNull Location location, @NonNull CreateLocationCallback callback) {
        try {
            Location ret = locationClient.createLocation(location);
            callback.onLocationCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void updateLocation(@NonNull long locationId, @NonNull Location location, @NonNull UpdateLocationCallback callback) {
        try {
            Location ret = locationClient.updateLocation(locationId, location);
            callback.onLocationUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }
}
