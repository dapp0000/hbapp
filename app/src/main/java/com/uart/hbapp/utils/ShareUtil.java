package com.uart.hbapp.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public class ShareUtil {


    public static Bitmap screenShotWholeScreen(Activity activity) {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();
        return bitmap;
    }
}
