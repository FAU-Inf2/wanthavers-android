package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate2ndFragBinding;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DesireCreateFragment2ndStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate2ndFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private static final int SELECT_PICTURE = 1;
    private Bitmap image = null;


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
        View view = inflater.inflate(R.layout.desirecreate2nd_frag, container, false);
        mViewDataBinding = Desirecreate2ndFragBinding.bind(view);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        //TODO View for Fragment
        final EditText desirePrice   = (EditText) view.findViewById(R.id.create_desire_price);
        final EditText desireReward   = (EditText) view.findViewById(R.id.create_desire_reward);

        Button uploadImage = (Button) view.findViewById(R.id.button_select_Image);
        uploadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if(isStoragePermissionGranted()){
                    selectImageForDesire();
                }

            }
        });


        Button button = (Button) view.findViewById(R.id.button_2nd_step);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //getActivity().finish();

                if (desirePrice.getText().toString().isEmpty() || desireReward.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), R.string.createDesire_Empty_Text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String[] input = {desirePrice.getText().toString(), desireReward.getText().toString()};
                    mPresenter.createNextDesireCreateStep(input);
                }

                       }
        });

        return view;
    }


    @Override
    public void showNextDesireCreateStep(String[] input) {
        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);
        intent.putExtra("desireTitle", title);
        intent.putExtra("desireDescription", description);
        intent.putExtra("desirePrice", input[0]);
        intent.putExtra("desireReward", input[1]);
        intent.putExtra("desireImage", image);
        startActivity(intent);
        //getActivity().finish();
    }

    private void selectImageForDesire() {

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

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                ImageView imageView = (ImageView) getView().findViewById(R.id.image_camera);
                imageView.setImageBitmap(bitmap);
                image = bitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }//TODO

    }


}
