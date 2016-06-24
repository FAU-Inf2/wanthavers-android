package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.rest.api.LocationResource;

/**
 * Created by Nico on 11.06.2016.
 */
public class LocationClient extends RestClient {
    private static LocationClient INSTANCE;

    private LocationResource locationEndpoint;

    private LocationClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        locationEndpoint = null;
        locationEndpoint = WebResourceFactory.newResource(LocationResource.class, target);
    }

    public static LocationClient getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new LocationClient(context);
        }
        return INSTANCE;
    }

    public Location getReverseGeocoding(double lat, double lon) {
        return locationEndpoint.reverse(null, lat, lon);
    }

    public Location createLocation(Location location) {
        return locationEndpoint.createLocation(null, location);
    }

    public Location updateLocation(long locationId, Location location) {
        return locationEndpoint.updateLocation(null, locationId, location);
    }

    public void deleteLocation(long locationId) {
        locationEndpoint.deleteLocation(null, locationId);
    }

    public Location getLocation(long locationId) {
        return locationEndpoint.get(null, locationId);
    }
}
