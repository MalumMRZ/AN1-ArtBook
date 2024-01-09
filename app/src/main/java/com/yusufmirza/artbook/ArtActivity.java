package com.yusufmirza.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yusufmirza.artbook.databinding.ActivityArtBinding;
import com.yusufmirza.artbook.databinding.ActivityMainBinding;

public class ArtActivity extends AppCompatActivity {
private ActivityArtBinding binding;
ActivityResultLauncher<Intent> activityResultLauncher;
ActivityResultLauncher<String> permissionLauncher;

Bitmap selectedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= ActivityArtBinding.inflate(getLayoutInflater());
       View view = binding.getRoot();
       setContentView(view);
       registerLauncher();


    }

    public void save(View view) {
System.out.println("I love you");
    }
    public void setImage(View view) {

        if(Build.VERSION.SDK_INT>=33){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"İzin gerekli efenim", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                } else {
                    //request
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                }
            }  else {
                //gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);

            }
        }  else {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "İzin gerekli efenim", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    //request
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                }
            } else {
                //gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);

            }


        }
    }

    public void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
           if(result){ //İzin verildi
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               activityResultLauncher.launch(intent);
           } else { //İzin verilmedi
               Toast.makeText(ArtActivity.this,"Access needed",Toast.LENGTH_LONG);
           }
            }
        });

        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK) {
                Intent intentfromresult = result.getData();
                if(intentfromresult != null){
                    Uri imageData= intentfromresult.getData();
                    //binding.imageView.setImageURI(imageData);

                    try {
                        if(Build.VERSION.SDK_INT>=28) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageData);
                            selectedView = ImageDecoder.decodeBitmap(source);
                            binding.imageView.setImageBitmap(selectedView);

                        } else {
                            selectedView= MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                            binding.imageView.setImageBitmap(selectedView);
                        }



                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }
            }
            }
        });


    }

}
