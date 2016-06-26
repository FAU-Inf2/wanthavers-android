package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate2ndFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.GpsLocationTracker;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;


public class DesireCreateFragment2ndStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate2ndFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private DesireCreateActionHandler mDesireCreateActionHandler;
    private Uri image;
    private Spinner spinner;
    private int REQUEST_CAMERA = 0;
    private int REQUEST_GALLERY = 1;
    private ImageView mImageView;
    private EditText mDesirePrice;

    public DesireCreateFragment2ndStep(){
        //Requires empty public constructor
    }
    public static DesireCreateFragment2ndStep newInstance(){ return new DesireCreateFragment2ndStep();}

    @Override
    public void setPresenter(@NonNull DesireCreateContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }
    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();

        GpsLocationTracker gpsLocationTracker = new GpsLocationTracker(getContext());
        if (!gpsLocationTracker.isNetworkAvailable()){
            showMessage(getString(R.string.network_failure));
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setRetainInstance(true);


        mViewDataBinding = Desirecreate2ndFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

        spinner = mViewDataBinding.spinnerCurrency;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt(getString(R.string.currency_header));
        spinner.setAdapter(adapter);

        mDesireCreateActionHandler = new DesireCreateActionHandler(mPresenter);
        mViewDataBinding.setActionHandler(mDesireCreateActionHandler);

        mDesirePrice = mViewDataBinding.createDesirePrice;

        mViewDataBinding.getRoot().setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter,  mDesireCreateActionHandler));

        CustomTextWatcher myWatcher = new CustomTextWatcher(mDesirePrice);
        mDesirePrice.addTextChangedListener(myWatcher);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {
        //final EditText desirePrice   = mViewDataBinding.createDesirePrice;
        //final EditText desireReward   = mViewDataBinding.createDesireReward;

        if(mDesirePrice.getText().toString().isEmpty() ){
            showMessage( getString(R.string.createDesire_Empty_Text));
            return;
        }


        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        //Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);
        Intent intent = new Intent(getContext(), MapActivity.class);


        intent.putExtra("desireTitle", title);
        intent.putExtra("desireDescription", description);
        intent.putExtra("desirePrice", mDesirePrice.getText().toString());
        //intent.putExtra("desireReward", desireReward.getText().toString());

        DesireLogic dsl = new DesireLogic(getContext());
        String currency = dsl.getIsoCurrency(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
        intent.putExtra("desireCurrency", currency);
        //intent.putExtra("desireCurrency", spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
        intent.putExtra("desireImage", image);
        intent.putExtra("calledAct", "0"); //for distinguishing which activity started the map
        startActivity(intent);
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);//
        //locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            SelectImageLogic imageLogic = mPresenter.getImageLogic();
            imageLogic.selectImageForDesire();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageView = mViewDataBinding.imageCamera;
        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            if(requestCode == REQUEST_GALLERY) {
                galleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                cameraResult(data);
            }
        }

    }

    private void galleryResult(Intent data){
        image = data.getData();

        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

        Cursor cur = getContext().getContentResolver().query(image, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }

        mImageView.setImageURI(image);

        switch(orientation) {
            case 90:
                mImageView.setRotation(90);
                break;
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }


    private void cameraResult(Intent data){
        SelectImageLogic imageLogic = mPresenter.getImageLogic();
        image = imageLogic.getImageFromCamera(data);
        mImageView.setImageURI(image);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showMedia(Desire m){
        //no Medias in this Step
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.create_desire_step_one, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next_step:
                mPresenter.createNextDesireCreateStep();
        }

        return true;
    }
}
