package com.ismealdi.hidoc.utils.components.slider

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by Al
 * on 06/04/19 | 23:02
 */
class AmSliderTransformation : ViewPager.PageTransformer {
	override fun transformPage(page: View, position: Float) {
		page.translationX = -position*page.width
		page.alpha = 1-Math.abs(position)
	}
}