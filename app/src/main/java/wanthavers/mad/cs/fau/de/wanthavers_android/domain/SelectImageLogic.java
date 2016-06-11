package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class SelectImageLogic {

    Context mContext;
    Activity mAct;

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
                if (options[item].equals(mContext.getString(R.string.take_photo)))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    mAct.startActivityForResult(intent, 1);
                }
                else if (options[item].equals(mContext.getString(R.string.choose_image_gallery)))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mAct.startActivityForResult(intent, 2);

                }
                else if (options[item].equals(mContext.getString(R.string.add_image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

}
