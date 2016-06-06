package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.support.annotation.NonNull;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;

public class DesireCreatePresenter implements DesireCreateContract.Presenter {
    private final DesireCreateContract.View mDesireCreateView;
    private final UseCaseHandler mUseCaseHandler;
    private final SetDesire mSetDesire;
    private final SetImage mSetImage;
    //private Media mMedia;
    private Desire mDesire;

    public DesireCreatePresenter(@NonNull UseCaseHandler ucHandler, @NonNull DesireCreateContract.View view,  @NonNull SetDesire setDesire, @NonNull SetImage setImage) {

        mUseCaseHandler = ucHandler;
        mDesireCreateView = view;
        mSetDesire = setDesire;
        mSetImage = setImage;

        mDesireCreateView.setPresenter(this);

    }

    public void start() { //TODO;
    }

    @Override
    public void createNextDesireCreateStep() {
        mDesireCreateView.showNextDesireCreateStep();
    }

    @Override
    public void selectImageFromDevice(){
        if(mDesireCreateView.isStoragePermissionGranted()){
            mDesireCreateView.selectImageForDesire();
        }
    }

    public void setDesire(Desire desire){


        SetDesire.RequestValues requestValue = new SetDesire.RequestValues(desire);

        mUseCaseHandler.execute(mSetDesire, requestValue,
                new UseCase.UseCaseCallback<SetDesire.ResponseValue>() {
                    @Override
                    public void onSuccess(SetDesire.ResponseValue response) {

                        Desire desire = response.getDesire();

                    }

                    @Override
                    public void onError() {
                        //TODO
                    }
                }
        );
    }

    public void setImage(File file){

        SetImage.RequestValues requestValue = new SetImage.RequestValues(file);

        mUseCaseHandler.execute(mSetImage, requestValue,
                new UseCase.UseCaseCallback<SetImage.ResponseValue>() {
                @Override
                public void onSuccess(SetImage.ResponseValue response) {

                //Media media = response.getMedia();
                    //mMedia = response.getMedia();
                    mDesire = response.getDesire();

                    //mDesireCreateView.showMedia(mMedia);
                    mDesireCreateView.showMedia(mDesire);

                }

            @Override
            public void onError() {
                //TODO
            }

        });

    }




}

