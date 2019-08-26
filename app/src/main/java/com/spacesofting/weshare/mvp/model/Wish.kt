package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.ui.adapter.ListWishElement
import java.io.Serializable
import java.util.*


class Wish(): Serializable, Cloneable, ListWishElement {


    constructor(templateId: Int?, listId: Int?) : this() {
        this.templateId = templateId
        /*this.listId = listId*/
    }

    constructor(template: Template, listId: Int?) : this() {
        this.templateId = template.id
        this.name = template.name
        this.images = template.images
      //  this.price = template.price
        this.status = template.status
        /*this.listId = listId*/
    }

    enum class PrivacyLevel {
        @SerializedName("public")
        PUBLIC,
        @SerializedName("friendsOnly")
        FRIENDS_ONLY,
        @SerializedName("private")
        PRIVATE;
    }

    @SerializedName("template_id")
    var templateId: Int? = 0

    @SerializedName("compilation_id")
    var compilationId: Int = 0

    @SerializedName("parent_id")
    override var parentId: Int? = null

    /*@SerializedName("wish_list_id")
    var listId: Int? = null*/

    @SerializedName("wish_id")
    override var id: Int = 0

    @SerializedName("wish_name")
    override var name: String? = ""

    @SerializedName("url")
    override var url: String? = null

    @SerializedName("description")
    override var description: String? = null

    @SerializedName("privacy_level")
    var privacy: PrivacyLevel? = PrivacyLevel.PUBLIC

 //   @SerializedName("price")
 //   override var price: Money? = null

    @SerializedName("pictures")
    var images: Array<Image>? = null

    @SerializedName("status")
    override var status: ListWishElement.Status = ListWishElement.Status.ACTIVE

    @SerializedName("is_complain")
    override var isComplain: Boolean = false

    @SerializedName("is_blocked")
    override var isBlocked: Boolean = false

    @SerializedName("skip_warnings")
    var skipWarnings: Boolean = true

    @SerializedName("profile")
    override var profile: Profile? = null

    @SerializedName("additions_count")
    override var additionsCount: Int? = 0

    @Transient
    var isDeleting = false

    @Transient
    override val type = "wish"

    override fun getImage(): Image? {
        images?.let {
            if (it.isNotEmpty()) {
                return it[0]
            }
        }
        return null
    }

    override fun isLoading() = isDeleting

    override fun setLoading(value: Boolean) {
        isDeleting = value
    }

    override public fun clone(): Any = super.clone()

    fun isNew(): Boolean = id == 0

    fun isMy(): Boolean {
        var isMy = false

        val myProfile = ApplicationWrapper.INSTANCE.profile
        if(myProfile != null && profile != null){
            isMy = profile!!.phone == myProfile.phone
        }

        return isMy
    }

    override fun isClosed(): Boolean = status == ListWishElement.Status.CLOSE

    override fun equals(other: Any?): Boolean {
        if (other is Wish &&
                other.name == name
                && other.privacy == privacy
                && other.description == description
               // && other.price == price
                && id == other.id
                && other.status == status
                && other.url == url) {

            val thisImages = images
            val otherImages = other.images

           /* if(other.profile != null && profile != null) {
                if (other.profile != profile){
                    return false
                }
            }*/

            if (otherImages == null && thisImages == null){
                return true
            } else if(otherImages == null || thisImages == null){
                return false
            } else if (!otherImages.contentEquals(images!!)) {
                return false
            }
        }

        return false
    }

    override fun hashCode(): Int {
        var result = templateId ?: 0
        result = 31 * result + (id ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (privacy?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
     //   result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (images?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + isDeleting.hashCode()
       // result = 31 * result + (profile?.hashCode() ?: 0)
        return result
    }
}


