package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateImage extends UseCase<CreateImage.RequestValues, CreateImage.ResponseValue> {

    private final MediaRepository mMediaRepository;

    public CreateImage(@NonNull MediaRepository mediaRepository) {
        mMediaRepository = checkNotNull(mediaRepository, "mediaRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mMediaRepository.createMedia(values.getImage(), new MediaDataSource.CreateMediaCallback() {
            @Override
            public void onMediaCreated(Media media) {
                ResponseValue responseValue = new ResponseValue(media);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreateFailed() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final File mImage;

        public RequestValues(File image) {
            mImage = image;
        }

        public File getImage(){
            return mImage;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Media mMedia;

        public ResponseValue(@NonNull Media media) {
            mMedia = checkNotNull(media, "media cannot be null!");
        }

        public Media getMedia() {
            return mMedia;
        }
    }

}
