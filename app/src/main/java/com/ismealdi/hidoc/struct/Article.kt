package com.ismealdi.hidoc.struct

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class Article(
		var title: String = "",
		var image: String = "",
		var writer: String = "",
		var contents: String = "",
		var createdOn: Timestamp = Timestamp.now()
) : Parcelable {

	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readParcelable(Timestamp::class.java.classLoader)
	) {
	}

	companion object CREATOR : Parcelable.Creator<Article> {
		override fun createFromParcel(parcel: Parcel): Article {
			return Article(parcel)
		}

		override fun newArray(size: Int): Array<Article?> {
			return arrayOfNulls(size)
		}

		const val tableName = "articles"

		object Fields {
			const val title = "title"
			const val image = "image"
			const val writer = "writer"
			const val contents = "contents"
			const val createdOn = "createdOn"
		}
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(title)
		parcel.writeString(image)
		parcel.writeString(writer)
		parcel.writeString(contents)
		parcel.writeParcelable(createdOn, flags)
	}

	override fun describeContents(): Int {
		return 0
	}
}