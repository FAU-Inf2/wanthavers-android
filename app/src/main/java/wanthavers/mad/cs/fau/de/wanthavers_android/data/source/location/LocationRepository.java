package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 11.06.2016.
 */
public class LocationRepository implements LocationDataSource {
    private static LocationRepository INSTANCE;

    private final LocationDataSource locationLocalDataSource;

    private final LocationDataSource locationRemoteDataSource;

    private LocationRepository(@NonNull LocationDataSource locationRemoteDataSource, @NonNull LocationDataSource locationLocalDataSource) {
        this.locationRemoteDataSource = checkNotNull(locationRemoteDataSource);
        this.locationLocalDataSource = checkNotNull(locationLocalDataSource);
    }

    public static LocationRepository getInstance(@NonNull LocationDataSource locationRemoteDataSource, @NonNull LocationDataSource locationLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new LocationRepository(checkNotNull(locationRemoteDataSource), checkNotNull(locationRemoteDataSource));
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getReverseGeocoding(@NonNull final double lat, @NonNull final double lon, @NonNull final GetReverseGeocodingCallback callback) {
        checkNotNull(lat);
        checkNotNull(lon);
        checkNotNull(callback);

        locationLocalDataSource.getReverseGeocoding(lat, lon, new GetReverseGeocodingCallback() {
            @Override
            public void onReverseGeocodingLoaded(Location location) {
                callback.onReverseGeocodingLoaded(location);
            }

            @Override
            public void onDataNotAvailable() {
                locationRemoteDataSource.getReverseGeocoding(lat, lon, new GetReverseGeocodingCallback() {
                    @Override
                    public void onReverseGeocodingLoaded(Location location) {
                        callback.onReverseGeocodingLoaded(location);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void createLocation(@NonNull Location location, @NonNull final CreateLocationCallback callback) {
        checkNotNull(location);
        checkNotNull(callback);

        locationRemoteDataSource.createLocation(location, new CreateLocationCallback() {
            @Override
            public void onLocationCreated(Location location) {
                callback.onLocationCreated(location);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void updateLocation(@NonNull long locationId, @NonNull Location location, @NonNull final UpdateLocationCallback callback) {
        checkNotNull(locationId);
        checkNotNull(location);
        checkNotNull(callback);

        locationRemoteDataSource.updateLocation(locationId, location, new UpdateLocationCallback() {
            @Override
            public void onLocationUpdated(Location location) {
                callback.onLocationUpdated(location);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void getSavedLocationsForLoggedInUser(@NonNull final GetSavedLocationsForLoggedInUserCallback callback) {
        checkNotNull(callback);

        locationRemoteDataSource.getSavedLocationsForLoggedInUser(new GetSavedLocationsForLoggedInUserCallback() {
            @Override
            public void onSavedLocationsForLoggedInUserLoaded(List<Location> locations) {
                callback.onSavedLocationsForLoggedInUserLoaded(locations);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
