package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import com.google.repacked.antlr.v4.runtime.misc.NotNull;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetImage extends UseCase<SetImage.RequestValues, SetImage.ResponseValue> {

    private final MediaRepository mMediaRepository;


    public SetImage(@NonNull MediaRepository mediaRepository) {
        mMediaRepository = checkNotNull(mediaRepository, "mediaRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {
        mMediaRepository.createMedia(values.getImage(), new MediaDataSource.CreateMediaCallback() {
            @Override
            public void onMediaCreated(Media media,Desire desire) {
                ResponseValue responseValue = new ResponseValue(media, desire);
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
        private Desire mDesire;

        public ResponseValue(@NonNull Media media, @NotNull Desire desire) {
            mMedia = checkNotNull(media, "media cannot be null!");
            mDesire = checkNotNull(desire, "desire cannot be null!");
        }

        public Desire getDesire() {
            mDesire.setImage(mMedia);
            return mDesire;
        }
    }

}
