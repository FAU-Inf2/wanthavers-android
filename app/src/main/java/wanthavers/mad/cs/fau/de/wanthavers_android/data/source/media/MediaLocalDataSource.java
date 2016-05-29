package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.InputStream;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.DatabaseHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaLocalDataSource implements MediaDataSource {
    private static MediaLocalDataSource INSTANCE;

    private RuntimeExceptionDao<Media, Long> mediaDao;

    private MediaLocalDataSource(Context context) {
        checkNotNull(context);
        mediaDao = DatabaseHelper.getInstance(context).getMediaRuntimeDao();
    }

    public static MediaLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MediaLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllMedia(@NonNull GetAllMediaCallback callback) {
        //TODO: alter this method when we decide to store media locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getMedia(@NonNull long mediaId, @NonNull GetMediaCallback callback) {
        //TODO: alter this method when we decide to store media locally
        callback.onDataNotAvailable();
    }

    @Override
    public void createMedia(@NonNull File image, @NonNull CreateMediaCallback callback) {
        //TODO: alter this method when we decide to store media locally
        callback.onCreateFailed();
    }
}
