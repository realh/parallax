/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */


package uk.co.realh.parallax;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import org.parallax3d.android.AndroidImage;

public class ImageAssetLoader
{
    private static final String TAG = "Parallax";

    static public AndroidImage createImageFromAsset(AssetManager assets,
                                                 String filename)
    throws IOException
    {
        InputStream strm = assets.open(filename);
        Bitmap bmp = BitmapFactory.decodeStream(strm);
        strm.close();
        if (null == bmp) {
            throw new RuntimeException("Unable to create bitmap from asset '" +
                    filename + "'");
        }
        if (null == bmp.getConfig()) {
            Log.d(TAG, "Bitmap has unsupported format, converting");
            Bitmap orig = bmp;
            bmp = bmp.copy(Bitmap.Config.RGB_565, true);
            orig.recycle();
        }
        return new AndroidImage(bmp);
    }
}
