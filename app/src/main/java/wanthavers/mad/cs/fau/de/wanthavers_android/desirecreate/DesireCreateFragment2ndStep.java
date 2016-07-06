package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.categorylist.CategoryListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate2ndFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.GpsLocationTrackerLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
//import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.CategoryAdapter;
import wanthavers.mad.cs.fau.de.wanthavers_android.locationlist.LocationListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;


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
    //private CategoryAdapter mCategoryListAdapter;
    private Category mCategory;
    private String mLocation;
    private double mLat;
    private double mLng;


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

        GpsLocationTrackerLogic gpsLocationTracker = new GpsLocationTrackerLogic(getContext(), 49.573759d , 11.027389d);
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

        //mCategory.setId(-1L);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {
        if(showEmptyEditTextError()){
            startMap();
        }

    }

    private void getDataForDesireAndFinish(){
        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);

        intent.putExtra("desireTitle", title);
        intent.putExtra("desireDescription", description);
        intent.putExtra("desirePrice", mDesirePrice.getText().toString());

        DesireLogic dsl = new DesireLogic(getContext());
        String currency = dsl.getIsoCurrency(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
        intent.putExtra("desireCurrency", currency);
        intent.putExtra("desireImage", image);
        intent.putExtra("desireCategory", mCategory);

        intent.putExtra("desireLocation", mLocation);
        intent.putExtra("desireLocationLat", Double.toString(mLat));
        intent.putExtra("desireLocationLng", Double.toString(mLng));

        startActivity(intent);
    }



    private void startMap(){

        Intent intent = new Intent(getContext(), LocationListActivity.class);
        Location location = null;
        intent.putExtra("filterlocation", location);
        intent.putExtra("calledAct", "0"); //for distinguishing which activity started the map
        startActivityForResult(intent, 20);
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
        }else if (grantResults[0]== PackageManager.PERMISSION_DENIED){
            showMessage(getString(R.string.declined_memory_runtime_permission));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if(requestCode == 10) { //CategoryList

            if (data.hasExtra("selectedCategory")) {
                Category category = (Category) data.getSerializableExtra("selectedCategory");
                showCategory(category);
                return;
            }

        }else if (/*requestCode == 20 &&*/ resultCode == 0){ //LocationList

            Location location = (Location) data.getSerializableExtra("locationObject");

            if(location == null){ //onBackPressed
                return;
            }
            mLocation = location.getFullAddress();
            mLat = location.getLat();
            mLng = location.getLon();
            getDataForDesireAndFinish();
            return;

        }else{

            mImageView = mViewDataBinding.imageCamera;
            if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                if (requestCode == REQUEST_GALLERY) {
                    galleryResult(data);
                } else if (requestCode == REQUEST_CAMERA) {
                    cameraResult(data);
                }
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

        //resizing high resolution images
        SelectImageLogic imageLogic = mPresenter.getImageLogic();
        image = imageLogic.scaleDown(image, imageLogic.getMaxImageSize(), orientation);
        mImageView.setImageURI(image);

       /* switch(orientation) {
            case 90:
                mImageView.setRotation(90);
                break;
        }*/

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }


    private void cameraResult(Intent intent){
        image = intent.getData();

        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

        Cursor cur = getContext().getContentResolver().query(image, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }

        SelectImageLogic imageLogic = mPresenter.getImageLogic();
        image = imageLogic.scaleDown(image, imageLogic.getMaxImageSize(), orientation);
        mImageView.setImageURI(image);

        /*switch(orientation) {
            case 90:
                mImageView.setRotation(90);
                break;
        }*/
    }

    /*private void cameraResult(Intent data){
        SelectImageLogic imageLogic = mPresenter.getImageLogic();
        image = imageLogic.getImageFromCamera(data);
        mImageView.setImageURI(image);
    }*/

    private boolean showEmptyEditTextError(){
        if(mDesirePrice.getText().toString().isEmpty() ){
            showMessage( getString(R.string.empty_price));
            return false;
        }

        if (mCategory == null){
            showMessage(getString(R.string.empty_category));
            return false;
        }

        return true;
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

    @Override
    public void showCategorySelection() {
        Intent intent = new Intent(getContext(), CategoryListActivity.class);
        startActivityForResult(intent, 10);
    }

    @Override
    public void showCategory(Category category) {

        mCategory = category;
        mViewDataBinding.setCategory(category);

        Media media = category.getImage();

        if (media != null) {
            final ImageView profileView = mViewDataBinding.selectedImageCategory;
            Picasso.with(mViewDataBinding.getRoot().getContext()).load(media.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mViewDataBinding.selectedImageCategory;
            profileView.setImageResource(R.drawable.no_pic);
        }

        mViewDataBinding.selectedImageCategory.setVisibility(View.VISIBLE);
        mViewDataBinding.selectedCategoryName.setVisibility(View.VISIBLE);
        mViewDataBinding.noCategorySelected.setVisibility(View.GONE);
    }

    @Override
    public void showGetCategoriesError() {
        showMessage(getString(R.string.get_categories_error));
    }

    @Override
    public void showCategories(List<Category> categories) {
        //mCategoryListAdapter.replaceData(categories);
        //mCategoryListAdapter.getFilter().filter(null);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ImageView iV = mViewDataBinding.selectedImageCategory;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            iV.setImageURI(image);
            if(mCategory!= null){
                showCategory(mCategory);
            }
        } else {
            iV.setImageURI(image);
            if(mCategory!= null){
                showCategory(mCategory);
            }
        }
    }

}
