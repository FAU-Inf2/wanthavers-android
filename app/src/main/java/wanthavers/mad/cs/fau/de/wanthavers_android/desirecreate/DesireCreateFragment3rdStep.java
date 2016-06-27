package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.File;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate3rdFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;


public class DesireCreateFragment3rdStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate3rdFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private Desire desire = new Desire();
    private ProgressDialog mLoadingDialog;
    private final int DESIRE_COLOR_NUMBER = 4;

    public DesireCreateFragment3rdStep() {
        //Requires empty public constructor
    }

    public static DesireCreateFragment3rdStep newInstance() {
        return new DesireCreateFragment3rdStep();
    }

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
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mViewDataBinding = Desirecreate3rdFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);


        //final TextView desireDropzone = mViewDataBinding.createDesireDropzone;
        //desireDropzone.setText(getActivity().getIntent().getExtras().getString("desireLocation"));
        //just for testing

        showLoadingProgress();
        //waitForUpload(mViewDataBinding.getRoot());

        mPresenter.createNextDesireCreateStep();
        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {
        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        String price = getActivity().getIntent().getExtras().getString("desirePrice");
        //String reward = getActivity().getIntent().getExtras().getString("desireReward");
        String currency = getActivity().getIntent().getExtras().getString("desireCurrency");
        Uri image = getActivity().getIntent().getExtras().getParcelable("desireImage");
        String location = getActivity().getIntent().getExtras().getString("desireLocation");
        double lat = Double.parseDouble(getActivity().getIntent().getExtras().getString("desireLocationLat"));
        double lng = Double.parseDouble(getActivity().getIntent().getExtras().getString("desireLocationLng"));
        Category cat = (Category) getActivity().getIntent().getSerializableExtra("desireCategory");


        setDataForDesire(title, description, Integer.parseInt(price),
                location, currency, image, lat, lng, cat);
        //includes publishDesire()

        Log.d("DesireTitle:", desire.getTitle());
        Log.d("DesireDesciption:", desire.getDescription());
        Log.d("DesirePrice:", Double.toString(desire.getPrice()));
        Log.d("DesireReward:", Double.toString(desire.getReward()));
        Log.d("DesireDropzone:", desire.getDropzone_string());
        Log.d("DesireCurrency:", desire.getCurrency());
        Log.d("DesireColor:", Integer.toString(desire.getColorIndex()));
        Log.d("DesireCategoryId:", Long.toString(desire.getCategoryId()));

        /*Intent intent = new Intent(getContext(), DesireListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        //getActivity().finish();

    }


    public void setDataForDesire(String title, String description, int price, String dropzone,
                                 String currency, Uri image, double lat, double lng, Category cat) {
        desire.setTitle(title);
        desire.setDescription(description);
        desire.setPrice(price);
        //desire.setReward(reward);
        desire.setDropzone_string(dropzone);
        desire.setDropzone_lat(lat);
        desire.setDropzone_long(lng);
        desire.setCurrency(currency);

        int colorNumber = (int) (Math.random() * DESIRE_COLOR_NUMBER);
        desire.setColorIndex(colorNumber);
        desire.setCreation_time(new Date());

        desire.setCategoryId(cat.getId());

        //will be set by the server
        desire.setCreator(null);
        desire.setId(0);

        if (image != null) {
            Log.d("Image", image.getPath());
            File file = new File(PathHelper.getRealPathFromURI(this.getContext().getApplicationContext(), image));
            Log.d("Image", file.getPath());

            mPresenter.setImage(file, desire);
            return; // calls own publishDesire()
        }

        desire.setImage(cat.getImage());
        publishDesire(desire);

    }

    @Override
    public void showMedia(Desire d){
        publishDesire(d);
    }

    @Override
    public void showMessage(String message) {
        //no Messages to show here
    }

    public void publishDesire(Desire desire) {
        mPresenter.setDesire(desire);

        endLoadingProgress();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next_step:
                mPresenter.createNextDesireCreateStep();
        }

        return true;
    }


    public void showLoadingProgress() {
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setTitle(getString(R.string.createDesire_loadingProgress_title));
        mLoadingDialog.setMessage(getString(R.string.createDesire_loadingProgress_message));
        mLoadingDialog.show();
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }


    public void endLoadingProgress() {
        mLoadingDialog.dismiss();

        Intent intent = new Intent(getContext(), DesireListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void showCategorySelection() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategory(Category category) {
        //no Category Selection in this Step
    }

    @Override
    public void showGetCategoriesError() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategories(List<Category> categories) {
        //no Category Selection in this Step
    }
}
