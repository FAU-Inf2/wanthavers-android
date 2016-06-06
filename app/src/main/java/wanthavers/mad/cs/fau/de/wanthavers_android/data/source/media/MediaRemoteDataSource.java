package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.MediaClient;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaRemoteDataSource implements MediaDataSource {
    private static MediaRemoteDataSource INSTANCE;

    private MediaClient mediaClient;
    private Context context;

    private MediaRemoteDataSource(Context context) {
        mediaClient = MediaClient.getInstance(context);
        this.context = context;
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
            List<Media> media = mediaClient.getAllMedia();
            callback.onAllMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMedia(@NonNull long mediaId, @NonNull GetMediaCallback callback) {
        try {
            Media media = mediaClient.get(mediaId);
            callback.onMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createMedia(@NonNull File image, @NonNull CreateMediaCallback callback) {
        String base64 = null;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(image);
            base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            callback.onCreateFailed();
            return;
        }

        if(base64 == null) {
            callback.onCreateFailed();
            return;
        }

        try {
            Media ret = mediaClient.createMedia(base64, image.getName());
            callback.onMediaCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }
}