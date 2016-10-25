package com.seewo.note.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author silei
 * @brief
 * @date 2016/10/18
 */

public final class SaveFileManager {
    public static boolean SaveToPng(Bitmap bitmap, String fileName) {

        File file = new File(Constants.PNGDIR + File.separator + fileName + ".png");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
