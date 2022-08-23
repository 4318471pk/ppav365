package com.live.fox.dialog;

import static com.live.fox.svga.ShowBigGiftFragment.DEFAULT_GIFT_DOWNLOAD_DIR;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.entity.Gift;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Svg 预览
 */
public class SvgaPreview extends DialogFragment {

    public static final String SVGA_GIFT_KEY = "svga gift key";
    private Dialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setDimAmount(0f);
        window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.black_20));
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_svga_play, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        try {
            SVGAImageView svagImg = view.findViewById(R.id.svga_preview_admission);
            view.findViewById(R.id.svga_preview_close).setOnClickListener(view1 -> dismiss());
            if (getArguments() != null) {
                Gift gift = (Gift) getArguments().getSerializable(SVGA_GIFT_KEY);
                SVGAParser parser = new SVGAParser(requireActivity());
                String path = DEFAULT_GIFT_DOWNLOAD_DIR + ".tx/" + gift.getGid() + "/" + gift.getGid();
                File file = new File(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                parser.decodeFromInputStream(bufferedInputStream, file.getAbsolutePath(),
                        new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                                svagImg.setImageDrawable(drawable);
                                svagImg.startAnimation();
                                svagImg.stepToFrame(0, true);
                            }

                            @Override
                            public void onError() {
                                Log.e("playSvg", "onError: ");
                            }
                        }, true, null, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
