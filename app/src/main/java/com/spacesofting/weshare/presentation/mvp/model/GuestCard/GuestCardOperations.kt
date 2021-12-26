package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GuestCardOperations : Serializable {
    @SerializedName("is_escalate")
    var isEscalate = true

    @SerializedName("is_edit")
    var isEdit: Boolean = true

    @SerializedName("is_delete")
    var isDelete: Boolean = true

    @SerializedName("is_send_for_approval")
    var isSendForApproval: Boolean = true

    @SerializedName("is_approval")
    var isApproval: Boolean = true

    @SerializedName("is_refused")
    var isRefused: Boolean = true

    @SerializedName("is_change_request")
    var isChangeRequest = true

    @SerializedName("is_cancel_last_operation")
    var isCancelLastOperation = false

    override fun equals(other: Any?): Boolean {
        if (other is GuestCardOperations) {
            if (isEdit == other.isEdit &&
                isDelete == other.isDelete &&
                isSendForApproval == other.isSendForApproval &&
                isApproval == other.isApproval &&
                isRefused == other.isRefused &&
                isChangeRequest == other.isChangeRequest &&
                isCancelLastOperation == other.isCancelLastOperation &&
                isEscalate == other.isEscalate
            ) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = isEdit.hashCode()
        result = prime * result + isDelete.hashCode()
        result = prime * result + isSendForApproval.hashCode()
        result = prime * result + isApproval.hashCode()
        result = prime * result + isRefused.hashCode()
        result = prime * result + isChangeRequest.hashCode()
        result = prime * result + isCancelLastOperation.hashCode()
        result = prime * result + isEscalate.hashCode()

        return result
    }
}