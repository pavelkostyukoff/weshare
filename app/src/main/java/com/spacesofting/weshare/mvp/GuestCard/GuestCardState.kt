package com.digitalhorizon.eve.mvp.model.guestcard

import com.digitalhorizon.eve.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class GuestCardState(val strResId: Int) : Serializable {
    @SerializedName("draft")
    DRAFT(R.string.guest_card_state_draft),
    @SerializedName("saved")
    SAVED(R.string.guest_card_state_saved),
    @SerializedName("sendForApproval")
    SENDFORAPPROVAL(R.string.guest_card_state_send_for_approval),
    @SerializedName("agreed")
    AGREED(R.string.guest_card_state_agreed),
    @SerializedName("refused")
    REFUSED(R.string.guest_card_state_refused),
    @SerializedName("deleted")
    DELETED(R.string.guest_card_state_deleted),
    @SerializedName("changeRequest")
    CHANGEREQUEST(R.string.guest_card_state_change_request)
}