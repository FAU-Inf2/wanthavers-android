package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.rest.api.MediaResource;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaClient extends RestClient {
    private static MediaClient INSTANCE;

    private MediaResource mediaEndpoint;

    private MediaClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        mediaEndpoint = null;
        mediaEndpoint = WebResourceFactory.newResource(MediaResource.class, target);
    }

    public static MediaClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MediaClient(context);
        }
        return INSTANCE;
    }

    public List<Media> getAllMedia() {
        return mediaEndpoint.getAll();
    }

    public Media get(long mediaId) {
        return mediaEndpoint.get(mediaId);
    }

    public Media createMedia(String base64, String filename) {
        return mediaEndpoint.createMedia(null, base64, filename);
    }
}
