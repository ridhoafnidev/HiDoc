package com.ismealdi.hidoc.utils.extensions

import android.support.design.widget.BottomNavigationView

/**
 * Created by Al
 * on 06/04/19 | 03:33
 */

infix fun BottomNavigationView.selectedListener(listener: BottomNavigationView.OnNavigationItemSelectedListener) {
	setOnNavigationItemSelectedListener(listener)
}