package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.support.annotation.NonNull;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;

/**
 * Created by Nico on 22.05.2016.
 */
public interface MediaDataSource {

    interface GetAllMediaCallback {

        void onAllMediaLoaded(List<Media> media);

        void onDataNotAvailable();

    }

    interface GetMediaCallback {

        void onMediaLoaded(Media media);

        void onDataNotAvailable();

    }

    interface CreateMediaCallback {

        void onMediaCreated(Media media);

        void onCreateFailed();

    }

    void getAllMedia(@NonNull GetAllMediaCallback callback);

    void getMedia(@NonNull long mediaId, @NonNull GetMediaCallback callback);

    void createMedia(@NonNull File image, @NonNull CreateMediaCallback callback);

}
