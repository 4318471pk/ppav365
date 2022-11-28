package com.tencent.demo.camera.gltexture;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.tencent.demo.camera.glrender.DirectRenderer;
import com.tencent.demo.gles.EglCore;
import com.tencent.demo.gles.WindowSurface;
import com.tencent.demo.log.LogUtils;
import com.tencent.demo.utils.GlUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 包含GL环境的textureView
 * 主要用于捏脸数据
 */

public class NoCameraGLTextureView extends TextureView {

    private String TAG = NoCameraGLTextureView.class.getName();

    private GLTextureProcessListener glTextureProcessListener = null;
    private SurfaceTextureListenerImp surfaceTextureListenerImp = null;

    private static final int FPS = 30;
    private volatile AvatarTextureViewState avatarTextureViewState = AvatarTextureViewState.INIT;

    private ExecutorService executorService = null;
    private Task lastTask = null;

    enum AvatarTextureViewState {
        INIT, STARTED, PAUSED, RELEASE
    }

    public NoCameraGLTextureView(Context context) {
        super(context);
        this.init();
    }

    public NoCameraGLTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public NoCameraGLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }


    public void setGlTextureProcessListener(GLTextureProcessListener glTextureProcessListener) {
        this.glTextureProcessListener = glTextureProcessListener;
    }

    private void init() {
        executorService = Executors.newSingleThreadExecutor();
        surfaceTextureListenerImp = new SurfaceTextureListenerImp(new GLTextureProcessListener() {
            @Override
            public int onTextureCustomProcess(int textureId, int width, int height) {
                if (glTextureProcessListener != null) {
                    return glTextureProcessListener.onTextureCustomProcess(textureId, width, height);
                }
                return textureId;
            }

            @Override
            public void onTextureCreate() {
                if (glTextureProcessListener != null) {
                    glTextureProcessListener.onTextureCreate();
                }
            }

            @Override
            public void onTextureDestroyed() {
                if (glTextureProcessListener != null) {
                    glTextureProcessListener.onTextureDestroyed();
                }
            }
        });

        this.setSurfaceTextureListener(surfaceTextureListenerImp);

    }

    class Task implements Runnable {
        private boolean isCanceled = false;

        @Override
        public void run() {
            int time = 1000 / FPS;
            while (!isCanceled && avatarTextureViewState == AvatarTextureViewState.STARTED) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isCanceled && avatarTextureViewState == AvatarTextureViewState.STARTED && surfaceTextureListenerImp != null) {
                    surfaceTextureListenerImp.process();
                }
            }
            if (!isCanceled && avatarTextureViewState == AvatarTextureViewState.PAUSED) {
                if (surfaceTextureListenerImp != null) {
                    surfaceTextureListenerImp.pause();
                }
            }
        }
    }




    public void start() {
        if (avatarTextureViewState == AvatarTextureViewState.RELEASE || avatarTextureViewState == AvatarTextureViewState.STARTED) {
            return;
        }
        avatarTextureViewState = AvatarTextureViewState.STARTED;
        if (lastTask != null) {
            lastTask.isCanceled = true;
        }
        lastTask = new Task();
        executorService.submit(lastTask);
    }

    public void pause() {
        avatarTextureViewState = AvatarTextureViewState.PAUSED;
    }

    public void release() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        avatarTextureViewState = AvatarTextureViewState.RELEASE;
        if (surfaceTextureListenerImp != null) {
            surfaceTextureListenerImp.release();
            surfaceTextureListenerImp = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (surfaceTextureListenerImp != null) {
            surfaceTextureListenerImp.release();
        }
    }

    public interface GLTextureProcessListener {
        int onTextureCustomProcess(int textureId, int width, int height);

        void onTextureCreate();

        void onTextureDestroyed();
    }


    private static class GLHandlerThread extends HandlerThread {
        //消息类型。初始化gl环境类型
        private static final int INIT = 0;
        //处理纹理类型
        private static final int PROCESS = 1;
        //销毁环境类型
        private static final int RELEASE = 2;
        private String TAG = GLHandlerThread.class.getName();

        private Handler handler = null;
        private SurfaceTexture mSurfaceTexture;
        private EglCore mEglCore;
        private DirectRenderer mScreenRenderer;
        private WindowSurface windowSurface;
        private int surfaceTextureId = -1;
        private SurfaceTextureListenerImp surfaceTextureListenerImp = null;

        public GLHandlerThread(String name, SurfaceTexture surface, SurfaceTextureListenerImp surfaceTextureListenerImp) {
            super(name);
            this.mSurfaceTexture = surface;
            this.surfaceTextureListenerImp = surfaceTextureListenerImp;
        }


        @Override
        public synchronized void start() {
            super.start();
            handler = new Handler(getLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case INIT:
                            onInitGl();
                            break;
                        case PROCESS:
                            onProcess();
                            break;
                        case RELEASE:
                            onRelease();
                            break;
                    }
                }
            };
            initGl();
        }

        private void onInitGl() {
            mEglCore = new EglCore(null, 0);
            windowSurface = new WindowSurface(mEglCore, mSurfaceTexture);
            windowSurface.makeCurrent();

            mScreenRenderer = new DirectRenderer();
            mScreenRenderer.init();
            synchronized (surfaceTextureListenerImp.lock) {
                while (surfaceTextureListenerImp.isReleaseIng) {
                    try {
                        surfaceTextureListenerImp.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.surfaceTextureListenerImp.glTextureProcessListener != null) {
                LogUtils.d(TAG, "onInitGl   onTextureCreate" + Thread.currentThread().getName());
                this.surfaceTextureListenerImp.glTextureProcessListener.onTextureCreate();
            }
        }


        private void onProcess() {
            if (windowSurface == null) {
                return;
            }
            int width = windowSurface.getWidth();
            int height = windowSurface.getHeight();
            if (surfaceTextureId == -1) {
                surfaceTextureId = GlUtil.createTexture(width, height, GLES20.GL_RGBA);
            }
            int processedId = surfaceTextureId;
            if (this.surfaceTextureListenerImp.glTextureProcessListener != null) {
                processedId = this.surfaceTextureListenerImp.glTextureProcessListener.onTextureCustomProcess(surfaceTextureId, width, height);
            }
            // 纹理上屏
            mScreenRenderer.doRender(processedId, -1, width, height, null, null, null);
            // If the SurfaceTexture has been destroyed, this will throw an exception.
            windowSurface.swapBuffers();

        }


        private void onRelease() {
            LogUtils.d(TAG, "onRelease " + Thread.currentThread().getName());
            if (this.surfaceTextureListenerImp.glTextureProcessListener != null) {
                this.surfaceTextureListenerImp.glTextureProcessListener.onTextureDestroyed();
            }
            if (windowSurface != null) {
                windowSurface.release();
                windowSurface = null;
            }
            if (mScreenRenderer != null) {
                mScreenRenderer.release();
                mScreenRenderer = null;
            }
            if (mEglCore != null) {
                mEglCore.release();
                mEglCore = null;
            }
            if (mSurfaceTexture != null) {
                mSurfaceTexture.release();
                mSurfaceTexture = null;
            }
            if (handler != null) {
                handler.getLooper().quit();
                handler = null;
            }
            synchronized (surfaceTextureListenerImp.lock) {
                this.surfaceTextureListenerImp.isReleaseIng = false;
                this.surfaceTextureListenerImp.lock.notifyAll();
            }
        }


        public void initGl() {
            if (handler != null) {
                handler.sendEmptyMessage(INIT);
            }
        }


        public void pause() {
            if (handler != null) {
                handler.removeMessages(PROCESS);
            }
        }

        public void process() {
            if (handler != null) {
                //有时消息处理的时间可能大于下个消息过来的时间，这样就导致队列中的消息会不断的拥挤，所以在添加后一个消息的时候，移除队列中未被处理的消息
                handler.removeMessages(PROCESS);
                handler.sendEmptyMessage(PROCESS);
            }
        }


        public void release() {
            LogUtils.d(TAG, "release " + Thread.currentThread().getName());
            if (handler != null) {
                //在发送销毁消息的时候先将处理任务的消息全部移除
                handler.removeMessages(PROCESS);
                handler.sendEmptyMessage(RELEASE);
            }
        }
    }


    private static class SurfaceTextureListenerImp implements TextureView.SurfaceTextureListener {


        private String TAG = SurfaceTextureListenerImp.class.getName();


        private volatile GLHandlerThread glHandlerThread;
        private GLTextureProcessListener glTextureProcessListener;

        private final Object lock = new Object();
        private boolean isReleaseIng = false;

        public SurfaceTextureListenerImp(GLTextureProcessListener glTextureProcessListener) {
            this.glTextureProcessListener = glTextureProcessListener;
        }


        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            LogUtils.d(TAG, "onSurfaceTextureAvailable " + Thread.currentThread().getName() + "  " + width + "  " + height + "   " + surface.hashCode());
            glHandlerThread = new GLHandlerThread("gl_thread", surface, this);
            glHandlerThread.start();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            LogUtils.d(TAG, "onSurfaceTextureSizeChanged " + Thread.currentThread().getName() + "  " + width + "  " + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            LogUtils.d(TAG, "onSurfaceTextureDestroyed " + Thread.currentThread().getName() + "     " + surface.hashCode());
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            LogUtils.d(TAG, "onSurfaceTextureUpdated " + Thread.currentThread().getName());
        }


        public void pause() {
            if (glHandlerThread != null) {
                glHandlerThread.pause();
            }
        }

        public void process() {
            if (glHandlerThread != null) {
                glHandlerThread.process();
            }
        }

        public void release() {
            LogUtils.d(TAG, "release " + Thread.currentThread().getName());
            if (glHandlerThread != null) {
                isReleaseIng = true;
                glHandlerThread.release();
                glHandlerThread = null;
            }
        }
    }


}
