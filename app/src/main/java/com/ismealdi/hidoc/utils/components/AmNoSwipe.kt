package com.ismealdi.hidoc.utils.components

import android.support.design.widget.BaseTransientBottomBar
import android.view.View

internal class AmNoSwipe : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View):Boolean {
        return false
    }
}