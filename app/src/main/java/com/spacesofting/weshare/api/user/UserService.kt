package com.spacesofting.weshare.api.user

import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.ResponceProfile
import com.spacesofting.weshare.mvp.UpdateProfile
import com.spacesofting.weshare.mvp.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST


interface UserService {

    @POST("/auth/register")  //todo //Подтвердить получение задачи
    fun register(@Body updateProfile: Profile): Observable<ResponceProfile>

    //todo  Get user profile
    @GET("users/me")
    fun getAccount(): Observable<User>

    //todo  Update user profile
    @PATCH("users/me")
    fun updateProfile(@Body updProfile: UpdateProfile): Observable<User>

    //todo    Update user avatar
   // @PATCH("users/me")



   /* @GET("event/pass")
    fun getPass(): Observable<Pass>

    @GET("event/guestcard")
    fun getAccount(): Observable<GuestCard>

    @GET("http://server/service")
    fun getSomething(@Query("array") array: String): Observable<Void>

    @GET("event/guestcards")
    fun getApproveCards(@Query("limit") limit: Int,
                      @Query("offset") offset: Int,
                      @Query("states") guestCardStates: JSONArray? = null,
                      @Query("priority_ids") guestCardPriority: JSONArray? = null,
                      @Query("search") searchStr: String? = null): Observable<ApproveList>

    //TODO refactoring
    @GET("event/guestcards")
    fun getGuestCards(@Query("limit") limit: Int,
                      @Query("offset") offset: Int,
                      @Query("states") guestCardStates: JSONArray? = null,
                      @Query("priority_ids") guestCardPriority: JSONArray? = null,
                      @Query("search") searchStr: String? = null): Observable<CardsList>

    @GET("event/guestcards/list")
    fun getGuestCardsList(@Query("limit") limit: Int,
                          @Query("offset") offset: Int,
                          @Query("assigned") assignedUserId: Int,
                          @Query("purpose") purposeEnum: PurposeEnum,
                          @Query("states") guestCardStates: ArrayList<GuestCardState>? = null,
                          @Query("priorities") guestCardPriority: ArrayList<GuestCardPriority>? = null,
                          @Query("structures") structures: ArrayList<StructureUnit>? = null,
                          @Query("search") searchStr: String? = null): Observable<CardsList>

    @POST("event/guestcards/pass")
    fun getGuestCard(@Body pass: Pass): Observable<GuestCard>

    @PUT("event/pass/accept")
    fun passGuest(@Body pass: Pass): Observable<Response<Void>>

    @PUT("event/pass/deny")
    fun denyGuest(@Body pass: Pass): Observable<Response<Void>>

    @GET("event/pass/deny")
    fun getDenyGuest(count: Int?): Observable<CountDeny>

    @POST("event/guestcards/{id}/approval")
    fun approveGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?, @Body guestPriority: GuestCardPriority): Observable<GuestCard>

    @DELETE("event/guestcards/{id}/approval")
    fun rejectGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?): Observable<GuestCard>

    @POST("event/guestcards/{id}/sendforapproval")
    fun sendForApprovalGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?): Observable<GuestCard>

    @PUT("event/guestcards/{id}/change_request")
    fun changeRequestGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?, @Body description: Description): Observable<GuestCard>

    @PUT("event/guestcards/{id}/delete")
    fun deleteGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?): Observable<GuestCard>

    @PUT("event/guestcards/{id}/refused")
    fun rejectGuestCard(@Path("id") guestCardId: Int, @Header("RowVersion") rowVersion: String?, @Body description: Description): Observable<GuestCard>

    @GET("event/guestcardspriorities")
    fun getGuestCardsPriorities(): Observable<ArrayList<GuestCardPriority>>

    @GET("event/pass/deny/{guestCardId}")
    fun getDenyGuest(@Path("guestCardId") count: Int): Observable<CountDeny>

    @GET("/api/v1/event/structures/{structureId}/guest_responsible")
    fun getResponsible(@Path("structureId") structureId: Int): Observable<ResponsibleList>

    @GET("/api/v1/event/companies")
    fun getCompanies(@Query("companyName") companyName: String? = null,
                     @Query("limit") limit: Int,
                     @Query("offset") offset: Int,
                     @Query("orderField") orderField: String? = null,
                     @Query("sortOrder") sortOrder: String? = null): Observable<CompaniesList>

    @GET("/api/v1/event/structure_units")
    fun getStructureUnits(@Query("limit") limit: Int,
                          @Query("offset") offset: Int,
                          @Query("orderField") orderField: String? = null,
                          @Query("sortOrder") sortOrder: String? = null): Observable<StructureUnitList>

    @PUT("/api/v1/event/guestcards/{guestCardId}")
    fun sendGuestCard(@Path("guestCardId") guestCardId: Int, @Body guestCardRequest: GuestCardRequest?, @Header("RowVersion") rowVersion: String?): Observable<GuestCard>*/
}