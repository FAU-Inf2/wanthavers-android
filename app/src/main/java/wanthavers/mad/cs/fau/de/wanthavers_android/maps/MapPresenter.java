package wanthavers.mad.cs.fau.de.wanthavers_android.maps;


public class MapPresenter implements MapContract.Presenter {
    private MapContract.View mDesireCreateView;

    public MapPresenter(MapContract.View view){
        mDesireCreateView = view;
    }

    public void createMoveToCurrentGpsPosition(){
        mDesireCreateView.moveToCurrentGpsPosition();
    }

    public void createFinishDesireCreate(){
        mDesireCreateView.showFinishDesireCreate();
    }

    public void createEditAddress(){
        //editing the Adress is not available for DesireDetail
        if (!(mDesireCreateView.forDesireDetail())) {
            mDesireCreateView.editAddress();
        }

    }

    @Override
    public void start() {

    }
}
