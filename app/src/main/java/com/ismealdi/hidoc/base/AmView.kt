package com.ismealdi.hidoc.base

import com.ismealdi.hidoc.utils.interfaces.AmNetworkInterface

/**
 * Created by Al
 * on 06/04/19 | 21:01
 */
interface AmView<T> : AmNetworkInterface {

    var presenter : T?

}