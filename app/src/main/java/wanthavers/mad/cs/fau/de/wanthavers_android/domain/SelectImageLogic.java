package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class SelectImageLogic {

    Context mContext;
    Activity mAct;
    private int REQUEST_CAMERA = 0;
    private int REQUEST_GALLERY = 1;

    public SelectImageLogic(Context context){
        mContext = context;
        mAct = (Activity) context;
    }



    public void selectImageForDesire() {

        final CharSequence[] options = { mContext.getString(R.string.take_photo)
                , mContext.getString(R.string.choose_image_gallery), mContext.getString(R.string.add_image_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.add_image);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(mContext.getString(R.string.take_photo))) {

                    selectImageFromCamera();
                }
                else if (options[item].equals(mContext.getString(R.string.choose_image_gallery))) {

                    selectImageFromGallery();
                }
                else if (options[item].equals(mContext.getString(R.string.add_image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        mAct.startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void selectImageFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mAct.startActivityForResult(intent,REQUEST_CAMERA);
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(mAct, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission: ","Permission is granted");
                return true;
            } else {

                Log.d("Permission: ","Permission is revoked");
                ActivityCompat.requestPermissions(mAct, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted because sdk<23
            Log.v("Permission: ","Permission is granted");
            return true;
        }

    }

    public Uri getImageFromCamera(Intent data){
        String title = Long.toString(System.currentTimeMillis());
        Bitmap image = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        createNewFolder();

        File destination = new File(Environment.getExternalStorageDirectory() + "/WantHaver", title + ".jpg");
        //File destination = new File(Environment.getExternalStorageDirectory(), title + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapToUri(image, title);
    }



    //creates new folder if needed
    private boolean createNewFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/WantHaver");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

    private Uri bitmapToUri (Bitmap bitmap, String title){
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, title, null);
        return Uri.parse(path);
    }

}
