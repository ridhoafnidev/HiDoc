package com.ismealdi.hidoc.utils.components.slider

/**
 * Created by Al
 * on 06/04/19 | 22:21
 */
class AmImage (var image: String = "", var title: String = "", var subTitle: String = "") {
    companion object {
        const val tableName = "ads"

        object Fields {
            const val image = "image"
            const val title = "title"
            const val subTitle = "subTitle"
        }
    }
}