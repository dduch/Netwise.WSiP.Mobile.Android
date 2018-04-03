package com.netwise.wsip.domain.crm;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.mindorks.paracamera.Camera;

/**
 * Created by dawido on 31.03.2018.
 */

public class CameraHelper {

    private static Camera camera;

    public static Camera getCamera() {
        return camera;
    }

    public static Camera getCamera(Activity act) {
        if(camera != null){
            camera.deleteImage();
        }
        initCamera(act);
        return camera;
    }

    private static void initCamera(Activity act) {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(100)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(act);
    }
}
