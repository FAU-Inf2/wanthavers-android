package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.fau.cs.mad.wanthavers.common.rest.api.CloudMessageTokenResource;

/**
 * Created by Nico on 11.06.2016.
 */
public class CloudMessageTokenClient extends RestClient {
    private static CloudMessageTokenClient INSTANCE;

    private CloudMessageTokenResource cloudMessageTokenEndpoint;

    private CloudMessageTokenClient(Context context) {
        super(context);
    }

    public static CloudMessageTokenClient getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new CloudMessageTokenClient(context);
        }
        return INSTANCE;
    }

    @Override
    protected void buildNewEndpoint() {
        cloudMessageTokenEndpoint = null;
        cloudMessageTokenEndpoint = WebResourceFactory.newResource(CloudMessageTokenResource.class, target);
    }


}
