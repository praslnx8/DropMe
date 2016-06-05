package com.prasilabs.dropme.services.imageUpload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by prasi on 5/6/16.
 */
public class ImageUploadManager
{
    public static  final int TAKE_PHOTO_CODE = 123;

    public static void selectImage(final Activity activity)
    {
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Take Photo"))
                {
                    takePhoto(activity);
                }
                else if (items[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    activity.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void takePhoto(Activity activity)
    {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(activity)));
        activity.startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    public static File getTempFile(Context context)
    {
        //it will return /sdcard/image.tmp
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "image.jpg");
    }

    private void handleActivityResult(final Context context, int requestCode, Intent data, int uploadType)
    {
        String picturePath = "";
        String pictureName = "";

        File pictureFile = null;

        if (requestCode == 1)
        {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
            if(c != null && c.getCount() > 0)
            {
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                List<String> items = Arrays.asList(picturePath.split("/"));
                pictureName = items.get(items.size() - 1);

                pictureFile = new File(picturePath);
            }
        }
        else if (requestCode == TAKE_PHOTO_CODE)
        {
            pictureFile = getTempFile(context);

            pictureName = pictureFile.getName();
        }



        AsyncTaskFileUpload.uploadImage(context, pictureFile, pictureName, new AsyncTaskFileUpload.AsyncImageUploadCallBack() {
            @Override
            public void uploaded(boolean status, String message)
            {

            }
        });
    }
}
