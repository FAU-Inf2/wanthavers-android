package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.support.annotation.NonNull;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;

public class DesireCreatePresenter implements DesireCreateContract.Presenter {
    private final DesireCreateContract.View mDesireCreateView;
    private final UseCaseHandler mUseCaseHandler;
    private final SetDesire mSetDesire;
    private final SetImage mSetImage;
    private final DesireCreateActivity3rdStep mDesireCreateActivity3rdStep;
    //private Media mMedia;
    private Desire mDesire;

    public DesireCreatePresenter(@NonNull UseCaseHandler ucHandler, @NonNull DesireCreateContract.View view, @NonNull DesireCreateActivity3rdStep desireCreateActivity3rdStep,
                                 @NonNull SetDesire setDesire, @NonNull SetImage setImage) {

        mUseCaseHandler = ucHandler;
        mDesireCreateView = view;
        mSetDesire = setDesire;
        mSetImage = setImage;
        mDesireCreateActivity3rdStep = desireCreateActivity3rdStep;

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
                        mDesireCreateView.showMessage(mDesireCreateActivity3rdStep.getResources().getString(R.string.desire_create_failed));
                    }
                }
        );
    }

    public void setImage(File file, Desire desire){

        SetImage.RequestValues requestValue = new SetImage.RequestValues(file, desire);

        mUseCaseHandler.execute(mSetImage, requestValue,
                new UseCase.UseCaseCallback<SetImage.ResponseValue>() {
                @Override
                public void onSuccess(SetImage.ResponseValue response) {

                    mDesire = response.getDesire();
                    mDesireCreateView.showMedia(mDesire);

                }

            @Override
            public void onError() {
                mDesireCreateView.showMessage(mDesireCreateActivity3rdStep.getResources().getString(R.string.desire_image_upload_failed));
            }

        });

    }




}

