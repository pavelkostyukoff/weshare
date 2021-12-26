package com.spacesofting.weshare.presentation.mvp.model.guestcard
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GuestCardPriority : Serializable {
	 @SerializedName("guest_priority_id")
	 var id: Int = 0

	 @SerializedName("guest_priority_name")
	 var name: String = ""

	override fun equals(other: Any?): Boolean {
		if (other is GuestCardPriority) {
			if (id == other.id && name == other.name) {
				return true
			}
		}
		return false
	}

	override fun hashCode(): Int {
		val prime = 31
		var result = id
		result = prime * result + name.hashCode()

		return result
	}
}
