package com.ismealdi.hidoc.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Al for Female Daily Network
 * on 22/11/18 | 17:58
 */
fun String.toDate(): Date {
    return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this)
}

fun Date.toFormat(): String {
    return SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(this)
}

fun Date.toInputFormat(): String {
    return SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(this)
}

fun Date.toTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
}
