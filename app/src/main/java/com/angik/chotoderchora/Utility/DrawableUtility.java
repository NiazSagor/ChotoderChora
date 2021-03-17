package com.angik.chotoderchora.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.angik.chotoderchora.R;

public class DrawableUtility {

    private static final int[] CATEGORY_DRAWABLES = {
            R.drawable.itol_bitol,
            R.drawable.bangla_alphabet_bg,
            R.drawable.benjonborno_bg,
            R.drawable.bangla_number_bg
    };

    public static Drawable getCategoryDrawable(Context context, int position){
        return ContextCompat.getDrawable(context, CATEGORY_DRAWABLES[position]);
    }

}
