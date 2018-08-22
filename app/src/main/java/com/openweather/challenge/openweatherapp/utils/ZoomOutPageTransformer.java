/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
  private static final float MIN_SCALE = 0.90f;
  private static final float MIN_ALPHA = 0.5f;

  public void transformPage(@NonNull View view, float position) {
    int pageWidth = view.getWidth();
    int pageHeight = view.getHeight();

    if (position < -1) { // [-Infinity,-1)
      // This page is way off-screen to the left.
      view.setAlpha(0);
    } else if (position <= 1) { // [-1,1]
      // Modify the default slide transition to shrink the page as well
      float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
      float vertMargin = pageHeight * (1 - scaleFactor) / 2;
      float horzMargin = pageWidth * (1 - scaleFactor) / 2;
      if (position < 0) {
        view.setTranslationX(horzMargin - vertMargin / 2);
      } else {
        view.setTranslationX(-horzMargin + vertMargin / 2);
      }

      // Scale the page down (between MIN_SCALE and 1)
      view.setScaleX(scaleFactor);
      view.setScaleY(scaleFactor);

      // Fade the page relative to its size.
      view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    } else { // (1,+Infinity]
      // This page is way off-screen to the right.
      view.setAlpha(0);
    }
  }
}
