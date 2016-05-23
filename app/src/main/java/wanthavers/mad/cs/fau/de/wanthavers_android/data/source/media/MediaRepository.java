package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.support.annotation.NonNull;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Media;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaRepository implements MediaDataSource {
    private static MediaRepository INSTANCE = null;

    private final MediaDataSource mediaRemoteDataSource;

    private final MediaDataSource mediaLocalDataSource;

    private MediaRepository(@NonNull MediaDataSource mediaRemoteDataSource, @NonNull MediaDataSource mediaLocalDataSource) {
        this.mediaLocalDataSource = checkNotNull(mediaLocalDataSource);
        this.mediaRemoteDataSource = checkNotNull(mediaRemoteDataSource);
    }

    public static MediaRepository getInstance(MediaDataSource mediaRemoteDataSource, MediaDataSource mediaLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MediaRepository(mediaRemoteDataSource, mediaLocalDataSource);
        }
        return INSTANCE;
    }

    public void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getAllMedia(@NonNull final GetAllMediaCallback callback) {
        checkNotNull(callback);

        mediaLocalDataSource.getAllMedia(new GetAllMediaCallback() {
            @Override
            public void onAllMediaLoaded(List<Media> media) {
                callback.onAllMediaLoaded(media);
            }

            @Override
            public void onDataNotAvailable() {
                mediaRemoteDataSource.getAllMedia(new GetAllMediaCallback() {
                    @Override
                    public void onAllMediaLoaded(List<Media> media) {
                        callback.onAllMediaLoaded(media);
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
    public void getMedia(@NonNull final long mediaId, @NonNull final GetMediaCallback callback) {
        checkNotNull(mediaId);
        checkNotNull(callback);

        mediaLocalDataSource.getMedia(mediaId, new GetMediaCallback() {
            @Override
            public void onMediaLoaded(Media media) {
                callback.onMediaLoaded(media);
            }

            @Override
            public void onDataNotAvailable() {
                mediaRemoteDataSource.getMedia(mediaId, new GetMediaCallback() {
                    @Override
                    public void onMediaLoaded(Media media) {
                        callback.onMediaLoaded(media);
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
    public void createMedia(@NonNull InputStream inputStream, @NonNull FormDataContentDisposition contentDispositionHeader, @NonNull CreateMediaCallback callback) {
        checkNotNull(inputStream);
        checkNotNull(contentDispositionHeader);
        checkNotNull(callback);

        mediaLocalDataSource.createMedia(inputStream, contentDispositionHeader, callback);
        mediaRemoteDataSource.createMedia(inputStream, contentDispositionHeader, callback);
    }
}
