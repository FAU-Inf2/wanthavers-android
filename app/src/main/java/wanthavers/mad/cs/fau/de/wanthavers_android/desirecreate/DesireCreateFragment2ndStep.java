package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate2ndFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DesireCreateFragment2ndStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate2ndFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private Uri image;
    private Spinner spinner;

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
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setRetainInstance(true);


        mViewDataBinding = Desirecreate2ndFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

        spinner = (Spinner) mViewDataBinding.getRoot().findViewById(R.id.spinner_currency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt(getString(R.string.currency_header));
        spinner.setAdapter(adapter);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {
        final EditText desirePrice   = (EditText) getView().findViewById(R.id.create_desire_price);
        final EditText desireReward   = (EditText) getView().findViewById(R.id.create_desire_reward);

        if(desirePrice.getText().toString().isEmpty() || desireReward.getText().toString().isEmpty() ){
            showMessage( getString(R.string.createDesire_Empty_Text));
            return;
        }


        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);
        intent.putExtra("desireTitle", title);
        intent.putExtra("desireDescription", description);
        intent.putExtra("desirePrice", desirePrice.getText().toString());
        intent.putExtra("desireReward", desireReward.getText().toString());

        DesireLogic ds = new DesireLogic(getContext());
        String currency = ds.getIsoCurrency(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
        intent.putExtra("desireCurrency", currency);
        //intent.putExtra("desireCurrency", spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
        intent.putExtra("desireImage", image);
        startActivity(intent);
    }

    public void selectImageForDesire() {

        final CharSequence[] options = { getString(R.string.take_photo) , getString(R.string.choose_image_gallery), getString(R.string.add_image_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_image);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo)))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals(getString(R.string.choose_image_gallery)))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals(getString(R.string.add_image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission: ","Permission is granted");
                return true;
            } else {

                Log.d("Permission: ","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted because sdk<23
            Log.v("Permission: ","Permission is granted");
            return true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            selectImageForDesire();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            image = data.getData();
            ImageView imageView = (ImageView) getView().findViewById(R.id.image_camera);
            imageView.setImageURI(image);

        }//TODO

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMedia(Desire m){
        //no Medias in this Step
    }


}
