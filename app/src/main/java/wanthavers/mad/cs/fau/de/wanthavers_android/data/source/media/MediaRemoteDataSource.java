package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.content.Context;
import android.support.annotation.NonNull;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.MediaClient;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaRemoteDataSource implements MediaDataSource {
    private static MediaRemoteDataSource INSTANCE;

    private MediaClient mediaEndpoint;

    private MediaRemoteDataSource(Context context) {
        mediaEndpoint = MediaClient.getInstance(context);
    }

    public static MediaRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MediaRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllMedia(@NonNull GetAllMediaCallback callback) {
        try {
            List<Media> media = mediaEndpoint.getAllMedia();
            callback.onAllMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMedia(@NonNull long mediaId, @NonNull GetMediaCallback callback) {
        try {
            Media media = mediaEndpoint.get(mediaId);
            callback.onMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createMedia(@NonNull InputStream inputStream, @NonNull FormDataContentDisposition contentDispositionHeader, @NonNull CreateMediaCallback callback) {
        try {
            Media media = mediaEndpoint.createMedia(inputStream, contentDispositionHeader);
            callback.onMediaCreated(media);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }
}
