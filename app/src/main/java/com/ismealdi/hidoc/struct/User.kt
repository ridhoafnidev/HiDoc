package com.ismealdi.hidoc.struct

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.ismealdi.hidoc.utils.commons.Constants
import java.util.*

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class User(
	var uid: String = "",
	var fullName: String = "",
	var specialist: String = "",
	var dateOfBirth: Date = Date(),
	var photoUrl: String = "",
	var email: String = "",
	var blood: String = "",
	var sex: String = "",
	var phoneNumber: String = "",
	var rating: Float = 0.0f,
	var emailVerified: Boolean = false,
	var category: Int = Constants.USER.PATIENT,
	var location: GeoPoint = GeoPoint(0.0,0.0),
	var pushId: String = "",
	var onlineUser: Boolean = false,
	var lastUpdated: Timestamp = Timestamp.now()
) : Parcelable {

	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		Date(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readFloat(),
		parcel.readByte() != 0.toByte(),
		parcel.readInt(),
		GeoPoint(0.0, 0.0),
		parcel.readString(),
		parcel.readByte() != 0.toByte(),
		parcel.readParcelable(Timestamp::class.java.classLoader)
	) {
	}

	companion object CREATOR : Parcelable.Creator<User> {
		override fun createFromParcel(parcel: Parcel): User {
			return User(parcel)
		}

		override fun newArray(size: Int): Array<User?> {
			return arrayOfNulls(size)
		}

		const val tableName = "users"

		object Fields {
			const val uid = "uid"
			const val fullName = "fullName"
			const val specialist = "specialist"
			const val dateOfBirth = "dateOfBirth"
			const val photoUrl = "photoUrl"
			const val email = "email"
			const val blood = "blood"
			const val sex = "sex"
			const val phoneNumber = "phoneNumber"
			const val rating = "rating"
			const val emailVerified = "emailVerified"
			const val category = "category"
			const val location = "location"
			const val pushId = "pushId"
			const val onlineUser = "onlineUser"
			const val lastUpdated = "lastUpdated"
		}
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(uid)
		parcel.writeString(fullName)
		parcel.writeString(specialist)
		parcel.writeString(photoUrl)
		parcel.writeString(email)
		parcel.writeString(blood)
		parcel.writeString(sex)
		parcel.writeString(phoneNumber)
		parcel.writeFloat(rating)
		parcel.writeByte(if (emailVerified) 1 else 0)
		parcel.writeInt(category)
		parcel.writeString(pushId)
		parcel.writeByte(if (onlineUser) 1 else 0)
		parcel.writeParcelable(lastUpdated, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

}