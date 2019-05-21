package com.ministerio.android.captura;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MagicalPermissions magicalPermissions;
    private final static int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;
    private MagicalCamera magicalCamera;
    private ImageView imageViewFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos la foto

        findViewById(R.id.btnHuella).setOnClickListener(this);
        imageViewFoto = findViewById(R.id.imgHuella);

        String[] permission =new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };


        magicalPermissions = new MagicalPermissions(this,permission);

        magicalCamera = new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);


    }




    @Override
    public void onClick(View v) { magicalCamera.takePhoto(); }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        Map<String,Boolean> map = magicalPermissions.permissionResult(requestCode, permissions,grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + "was: " + map.get(permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode==RESULT_OK){

            magicalCamera.resultPhoto(requestCode, resultCode, data);

            imageViewFoto.setImageBitmap(magicalCamera.getPhoto());

            String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"Foto","Foto",MagicalCamera.JPEG,true);


            if(path !=null){
                Toast.makeText(MainActivity.this,"La foto se guardo en el dispositivo"+path, Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(MainActivity.this,"La imagen no se guardo en el dispositivo", Toast.LENGTH_SHORT).show();
            }
        }




    }
}
