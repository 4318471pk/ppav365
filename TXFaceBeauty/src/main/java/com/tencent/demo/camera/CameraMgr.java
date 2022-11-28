package com.tencent.demo.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;

public class CameraMgr {

    private static final String TAG = CameraMgr.class.getSimpleName();

    private Camera mCamera;
    private int mCameraDisplayOrientation = 0;

    public void startPreview(SurfaceTexture surfaceTexture) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewTexture(surfaceTexture);
            } else {
                Log.e(TAG, "setPreviewTexture: mCamera is NULL!!!");
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        if (mCamera != null) {
            mCamera.startPreview();
        } else {
            Log.e(TAG, "startPreview: mCamera is NULL!!!");
        }
    }

    /**
     * Opens a camera, and attempts to establish preview mode at the specified width and height.
     * <p>
     * Sets mCameraPreviewWidth and mCameraPreviewHeight to the actual width/height of the preview.
     */
    public Camera.Size openCamera(Activity activity, int desiredWidth, int desiredHeight) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized");
        }

        boolean useBackCamera = activity.getSharedPreferences("demo_settings", Context.MODE_PRIVATE)
                .getBoolean("USE_BACK_CAMERA", false);

        CameraInfo info = new CameraInfo();

        // Try to find a front-facing camera (e.g. for videoconferencing).
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (useBackCamera) {
                if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = Camera.open(i);
                    break;
                }
            } else {
                if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    mCamera = Camera.open(i);
                    break;
                }
            }
        }

        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters parms = mCamera.getParameters();

        CameraUtils.choosePreviewSize(parms, desiredWidth, desiredHeight);

        // Give the camera a hint that we're recording video.  This can have a big
        // impact on frame rate.
        parms.setRecordingHint(true);

        if (useBackCamera) {
            parms.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        // leave the frame rate set to default
        mCamera.setParameters(parms);

        int[] fpsRange = new int[2];
        Camera.Size mCameraPreviewSize = parms.getPreviewSize();
        parms.getPreviewFpsRange(fpsRange);
        String previewFacts = mCameraPreviewSize.width + "x" + mCameraPreviewSize.height;
        if (fpsRange[0] == fpsRange[1]) {
            previewFacts += " @" + (fpsRange[0] / 1000.0) + "fps";
        } else {
            previewFacts += " @[" + (fpsRange[0] / 1000.0) +
                    " - " + (fpsRange[1] / 1000.0) + "] fps";
        }
        Log.d(TAG, "openCamera: " + previewFacts);

        mCameraDisplayOrientation = calcDisplayOrientation(activity, info);
        mCamera.setDisplayOrientation(mCameraDisplayOrientation);

        return mCameraPreviewSize;
    }

    private int calcDisplayOrientation(Activity activity, CameraInfo info) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        int result = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public int getCameraDisplayOrientation() {
        return mCameraDisplayOrientation;
    }

    /**
     * Stops camera preview, and releases the camera to the system.
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.d(TAG, "releaseCamera -- done");
        }
    }
}
