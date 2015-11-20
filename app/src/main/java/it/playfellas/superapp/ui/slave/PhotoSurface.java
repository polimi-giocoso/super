package it.playfellas.superapp.ui.slave;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import lombok.Getter;

/**
 * Created by Stefano Cappa on 06/09/15.
 */
public class PhotoSurface implements SurfaceHolder.Callback {
    private static final String TAG = PhotoSurface.class.getSimpleName();

    @Getter
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public PhotoSurface(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the front camera
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (RuntimeException e) {
            // check for exceptions
            Log.e(TAG, "RuntimeException while opening camera", e);
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameters
        //param.setPreviewSize(352, 288);
        //set modifiedparameters
        camera.setParameters(param);

        Log.d(TAG, "Device for Camera Surface View: " + PhotoUtils.getDeviceName());

        camera.setDisplayOrientation(PhotoUtils.getFixedOrientationDegree());
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            // check for exceptions
            Log.e(TAG, "IOException while displaying the preview", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        camera.stopPreview();

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "IOException while displaying the refreshed preview", e);
        }
    }
}
