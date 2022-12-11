package com.nwf.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Environment
import android.util.Log
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

object CompressJAVA {

    fun compress(context: Context, actualImageFile: File,listener: CompressListener?)
    {
            GlobalScope.launch {
                var uploadFile:File?=null
                var thumbFile:File?=null

                val isNotOverLength=actualImageFile.length() <  1000 * 1000
                val bitmap=BitmapFactory.decodeFile(actualImageFile.path);
                val isNotOverSize=bitmap.width<3000 && bitmap.height<3000
                //限制1M
                uploadFile = if (isNotOverLength && isNotOverSize) {
                    actualImageFile
                } else {
                    Compressor.compress(context, actualImageFile) {
                        destination(    actualImageFile)
                        resolution(3000, 3000)
                        quality(90)
                        format(Bitmap.CompressFormat.JPEG)
                        size(1_097_152) // 1 MB
                    }
                }

//                thumbFile = Compressor.compress(context, actualImageFile) {
//                    destination(     File(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES).absolutePath+"/"+System.currentTimeMillis()+".thunmb"))
//                    resolution(point.x, point.y)
//                    quality(80)
//                    format(Bitmap.CompressFormat.JPEG)
//                    size(2_097_152) // 2 MB
//                }

                listener?.compressDone(uploadFile,uploadFile,isNotOverLength && isNotOverSize)
            }

    }

    interface CompressListener
    {
        fun compressDone(uploadFile: File,thumbFile:File,isOverLength: Boolean )
    }
}