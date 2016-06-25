package wanthavers.mad.cs.fau.de.wanthavers_android.maps;

public class MapActionHandler {

    private MapContract.Presenter mListener;


    public MapActionHandler(MapContract.Presenter listener) {
        mListener = listener;

    }


    public void buttonMyLocation(){
        mListener.createMoveToCurrentGpsPosition();
    }

    public void buttonSetLocation(){
        mListener.createFinishDesireCreate();
    }

    public void onPressedAddress(){
        mListener.createEditAddress();
    }
}
