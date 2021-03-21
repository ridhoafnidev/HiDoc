package com.ismealdi.hidoc.struct

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class
Location(
	var location: GeoPoint = GeoPoint(0.0,0.0),
	var uid: String = "",
	var createdOn: Timestamp = Timestamp.now()
) {

	companion object {
		const val tableName = "locations"

		object Fields {
			const val location = "location"
			const val uid = "uid"
			const val createdOn = "createdOn"
		}
	}

}