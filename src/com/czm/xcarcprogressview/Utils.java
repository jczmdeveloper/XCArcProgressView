package com.czm.xcarcprogressview;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {
    /**
     * 从Resources中获取图片资源，转化为Bitmap格式返回
     * @param c
     * @param res
     * @return
     */
    public static Bitmap decodeCustomRes(Context c, int res) {
        InputStream is = c.getResources().openRawResource(res);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;//原尺寸加载图片
        Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
        return bmp;
    }
    /**
     * // 获取屏幕分辨率-宽
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }
    /**
     * // 获取屏幕分辨率-高
     * @param context
     * @return
     */
    public static int getWindowHeigh(Context context) {
        
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }
}
