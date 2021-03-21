package com.ismealdi.hidoc.struct

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class Specialist(
	var name: String = ""
) {

	companion object {
		const val tableName = "specialists"

		object Fields {
			const val name = "name"
		}
	}

}