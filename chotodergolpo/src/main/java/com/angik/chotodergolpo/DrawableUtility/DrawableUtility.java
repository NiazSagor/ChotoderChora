package com.angik.chotodergolpo.DrawableUtility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.angik.chotodergolpo.R;

public class DrawableUtility {

    private static final int[] CATEGORY_DRAWABLES = {
            R.drawable.ic_story_bg_1,
            R.drawable.ic_story_bg_2,
            R.drawable.ic_story_bg_3,
            R.drawable.ic_story_bg_4
    };

    public static Drawable getCategoryDrawable(Context context, int position){
        return ContextCompat.getDrawable(context, CATEGORY_DRAWABLES[position]);
    }

}
