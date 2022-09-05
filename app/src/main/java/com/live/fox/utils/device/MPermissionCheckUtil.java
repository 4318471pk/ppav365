package com.live.fox.utils.device;

import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * User: xyp
 * Date: 2017/6/1
 * Time: 20:14
 */

public class MPermissionCheckUtil {

    //*****************暂时发现魅族6.0与HTC6.0动态权限无法获取正确的权限状态****************//

    /**
     * 动态权限之前判断是否有摄像头权限
     *
     * @return
     */
    public static boolean isCameraPermission() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            return false;
        }finally {
            if(mCamera != null){
                mCamera.release();
                mCamera = null;
            }
        }
        return true;
    }

    /**
     * 针对htc、vivo、oppo、api<23的手机判断是否有录音权限
     */
    public static boolean isVoicePermission() {
        AudioRecord record = null;
        try {
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState = record.getRecordingState();
            if (recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (record != null)
                record.release();
        }
        return true;
    }
}
