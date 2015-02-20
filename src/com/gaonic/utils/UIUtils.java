package com.gaonic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.TextView;

public class UIUtils {

	public static Bitmap getBitmapWithTransparentBG(Bitmap srcBitmap, int bgColor) {
	    Bitmap result = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
	    int nWidth = result.getWidth();
	    int nHeight = result.getHeight();
	    for (int y = 0; y < nHeight; ++y)
	      for (int x = 0; x < nWidth; ++x) {
	    	  int nPixelColor = result.getPixel(x, y);
	    	  if (nPixelColor == bgColor)
	    		  result.setPixel(x, y, Color.TRANSPARENT);
	      }
	    return result;
	}
	
	/**
	 * @param context
	 *            the current application context
	 * @param dp
	 *            the dip value to convert
	 * @return the px value corresponding to the given dip
	 */
	public static int getPxFromDp(final Context context, final int sizeDp) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return ((int) ((sizeDp * scale) + 0.5f));
	}
	public static void expand(final View v) {
		expand(v, null, LayoutParams.WRAP_CONTENT);
	}

	public static void expand(final View v, AnimationListener al,
			final int initialHeight) {
		v.measure(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1
						? initialHeight
						: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if (al != null)
			a.setAnimationListener(al);
		// 1dp/ms
		a.setDuration((int) (targtetHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}
	public static void collapse(final View v) {
		collapse(v, null);

	}

	public static void collapse(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if (al != null)
			a.setAnimationListener(al);
		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}
}
