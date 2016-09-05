package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirecreateFragBinding;

public class DesireCreateActionHandler {

    private DesireCreateContract.Presenter mListener;


    public DesireCreateActionHandler(DesireCreateContract.Presenter listener) {
        mListener = listener;

    }

    public void buttonNextDesireStep() {
        mListener.createNextDesireCreateStep();
    }

    public void onPressImage() {
        mListener.selectImageFromDevice();

    }

    public void selectExpirationDate() {
        mListener.selectExpirationDate();
    }

    public void openCategoryList() {
        mListener.openCategorySelection();
    }

    public void openInfo() {
        mListener.showInfo();
    }

    public void toggleHoursRadioButton(){
        mListener.toggleHoursRadioButton();
    }

    public void toggleDaysRadioButton(){
        mListener.toggleDaysRadioButton();
    }

    public void toggleReversedBidding() {
        mListener.toggleReversedBidding();
    }



}
