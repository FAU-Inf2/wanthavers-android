package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;

/**
 * Created by Nico on 11.06.2016.
 */
public interface LocationDataSource {

    interface GetReverseGeocodingCallback {

        void onReverseGeocodingLoaded(Location location);

        void onDataNotAvailable();

    }

    interface CreateLocationCallback {

        void onLocationCreated(Location location);

        void onCreateFailed();

    }

    interface UpdateLocationCallback {

        void onLocationUpdated(Location location);

        void onUpdateFailed();

    }

    interface GetSavedLocationsForLoggedInUserCallback {

        void onSavedLocationsForLoggedInUserLoaded(List<Location> locations);

        void onDataNotAvailable();

    }

    void getReverseGeocoding(@NonNull double lat, @NonNull double lon, @NonNull GetReverseGeocodingCallback callback);

    void createLocation(@NonNull Location location, @NonNull CreateLocationCallback callback);

    void updateLocation(@NonNull long locationId, @NonNull Location location, @NonNull UpdateLocationCallback callback);

    void getSavedLocationsForLoggedInUser(@NonNull GetSavedLocationsForLoggedInUserCallback callback);

}
