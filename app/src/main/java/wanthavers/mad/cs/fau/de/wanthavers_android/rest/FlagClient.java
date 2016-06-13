package wanthavers.mad.cs.fau.de.wanthavers_android.rest;


import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.rest.api.FlagResource;

public class FlagClient extends RestClient {
    private static FlagClient INSTANCE;

    private FlagResource flagEndpoint;

    private FlagClient(Context context) {
        super(context);
    }

    public static FlagClient getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new FlagClient(context);
        }
        return INSTANCE;
    }

    @Override
    protected void buildNewEndpoint() {
        flagEndpoint = null;
        flagEndpoint = WebResourceFactory.newResource(FlagResource.class, target);
    }

    public List<DesireFlag> getDesireFlags(long id) {
        return flagEndpoint.getDesireFlags(null, id);
    }

    public DesireFlag flagDesire(long id, DesireFlag desireFlag) {
        return flagEndpoint.flagDesire(null, id, desireFlag);
    }

    public void unflagDesire(long id, long flagId) {
        flagEndpoint.unflagDesire(null, id, flagId);
    }

}
