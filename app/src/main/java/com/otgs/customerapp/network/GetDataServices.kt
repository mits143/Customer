package com.otgs.customerapp.network

import com.otgs.customerapp.Constants.Companion.FORGOT_PASSWORD_NEW
import com.otgs.customerapp.Constants.Companion.GET_ALLFAQ
import com.otgs.customerapp.Constants.Companion.GET_ALLMONTHS
import com.otgs.customerapp.Constants.Companion.GET_ALLYEARS
import com.otgs.customerapp.Constants.Companion.GET_ALL_STATES
import com.otgs.customerapp.Constants.Companion.GET_CITIES
import com.otgs.customerapp.Constants.Companion.GET_PINCODES
import com.otgs.customerapp.Constants.Companion.LOGIN_AUTO
import com.otgs.customerapp.Constants.Companion.RESET_PASSWORD
import com.otgs.customerapp.Constants.Companion.SEND_OTP
import com.otgs.customerapp.Constants.Companion.SUBMIT_QUERY
import com.otgs.customerapp.model.request.*
import com.otgs.customerapp.model.response.*
import com.google.gson.JsonObject
import com.otgs.customerapp.Constants.Companion.ADD_CUSTOMERVEHICLE_DETAILS
import com.otgs.customerapp.Constants.Companion.CUSTOMER_BOOKINGDETAILS
import com.otgs.customerapp.Constants.Companion.EDIT_CUSTOMERPROFILE
import com.otgs.customerapp.Constants.Companion.GET_ALLCATEGORIES
import com.otgs.customerapp.Constants.Companion.GET_ALLRTORULES
import com.otgs.customerapp.Constants.Companion.GET_ALLVEHICLETYPES
import com.otgs.customerapp.Constants.Companion.GET_ALL_SERVICE_MASTERLIST
import com.otgs.customerapp.Constants.Companion.GET_CUSTBRAND
import com.otgs.customerapp.Constants.Companion.GET_CUSTOMERLIST
import com.otgs.customerapp.Constants.Companion.GET_CUSTVEHICLELIST
import com.otgs.customerapp.Constants.Companion.GET_RATING_REVIEW
import com.otgs.customerapp.Constants.Companion.GET_SERVICE_PROVIDERDETAILS
import com.otgs.customerapp.Constants.Companion.GET_SERVICE_PROVIDERLIST
import com.otgs.customerapp.Constants.Companion.GET_SERVICINGLIST
import com.otgs.customerapp.Constants.Companion.GET_SOSDETAILS
import com.otgs.customerapp.Constants.Companion.GET_VEHICLEBRANDS
import com.otgs.customerapp.Constants.Companion.GET_VEHICLEDETAILS
import com.otgs.customerapp.Constants.Companion.GET_VEHICLESUBTYPES
import com.otgs.customerapp.Constants.Companion.REG_CUSTOMER
import com.otgs.customerapp.Constants.Companion.SUBMIT_RATING_REVIEW
import com.otgs.customerapp.model.response.GarageDetail.GarageDetailResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface GetDataServices {

    @POST(LOGIN_AUTO)
    fun login(@Body jsonObject: LoginRequest): Observable<Response<LoginResponse>>

    @Multipart
    @POST(REG_CUSTOMER)
    fun register(
        @Part profile: MultipartBody.Part,
        @Part("address") address: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("email_id") email_id: RequestBody,
        @Part("emergency_contact1") emergency_contact1: RequestBody,
        @Part("emergency_contact2") emergency_contact2: RequestBody,
        @Part("emergency_contact3") emergency_contact3: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("mobile_no") mobile_no: RequestBody,
        @Part("pincode") pincode: RequestBody,
        @Part("relation1") relation1: RequestBody,
        @Part("relation2") relation2: RequestBody,
        @Part("relation3") relation3: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("device_id") device_id: RequestBody
    ): Observable<Response<LoginResponse>>

    @POST(FORGOT_PASSWORD_NEW)
    fun forgotPwd(@Body jsonObject: ForgotPwdRequest): Observable<Response<JsonObject>>

    @POST(RESET_PASSWORD)
    fun resetPassword(@Body jsonObject: ChangePwdRequest): Observable<Response<JsonObject>>

    @GET(GET_ALL_STATES)
    fun getAllStates(): Observable<Response<StateResponse>>

    @GET(GET_CITIES)
    fun getCities(@Query("state_id") state_id: String): Observable<Response<CityReponse>>

    @GET(GET_PINCODES)
    fun getPincodes(@Query("city_id") city_id: String): Observable<Response<PincodeResponse>>

    @GET(GET_ALLYEARS)
    fun getYears(): Observable<Response<YearResponse>>

    @GET(GET_ALLMONTHS)
    fun getMonths(): Observable<Response<MonthResponse>>

    @POST(SEND_OTP)
    fun SendOTP(@Body jsonObject: JsonObject): Observable<Response<JsonObject>>

    @POST(SUBMIT_QUERY)
    fun submitQuery(@Body jsonObject: JsonObject): Observable<Response<JsonObject>>

    @GET(GET_ALLFAQ)
    fun getAllFaq(@Query("user_type") user_type: String): Observable<Response<FAQResponse>>

    @GET(GET_ALLCATEGORIES)
    fun getAllCategories(): Observable<Response<ContactUsResponse>>

    @GET(GET_CUSTOMERLIST)
    fun getCustomerList(
        @Header("token") token: String,
        @Query("customer_id") customer_id: String
    ): Observable<Response<ProfileDetailResponse>>

    @Multipart
    @POST(EDIT_CUSTOMERPROFILE)
    fun editCustomerProfile(
        @Header("token") token: String,
        @Part profile: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("address") address: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("email_id") email_id: RequestBody,
        @Part("emergency_contact1") emergency_contact1: RequestBody,
        @Part("emergency_contact2") emergency_contact2: RequestBody,
        @Part("emergency_contact3") emergency_contact3: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("mobile_no") mobile_no: RequestBody,
        @Part("pincode") pincode: RequestBody,
        @Part("relation1") relation1: RequestBody,
        @Part("relation2") relation2: RequestBody,
        @Part("relation3") relation3: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("city_id") city_id: RequestBody
    ): Observable<Response<JsonObject>>

    @POST(GET_ALL_SERVICE_MASTERLIST)
    fun getAllServiceMasterList(): Observable<Response<AutoHubResponse>>

    @POST(GET_SOSDETAILS)
    fun getSosDetails(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Observable<Response<SOSResponse>>

    @POST(GET_SERVICE_PROVIDERLIST)
    fun getGarageList(
        @Header("token") token: String,
        @Body jsonObject: GoogleMapDataRequest
    ): Observable<Response<GarageResponse>>

    @GET(GET_SERVICE_PROVIDERDETAILS)
    fun getServiceProviderDetails(
        @Header("token") token: String,
        @Query("service_provider_id") service_provider_id: String,
        @Query("cust_id") cust_id: String
    ): Observable<Response<GarageDetailResponse>>

    @GET(GET_ALLRTORULES)
    fun getAllRtoRules(
        @Header("token") token: String,
        @Query("state_id") state_id: String
    ): Observable<Response<RTOResponse>>

    @POST(GET_SERVICINGLIST)
    fun getServicingList(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Observable<Response<ServicingResponse>>

    @POST(CUSTOMER_BOOKINGDETAILS)
    fun getcustomerBookingDetails(
        @Header("token") token: String,
        @Body jsonObject: BookingRequest
    ): Observable<Response<BookingResponse>>

    @POST(GET_CUSTBRAND)
    fun getBrand(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Observable<Response<BrandResponse>>

    @Multipart
    @POST(ADD_CUSTOMERVEHICLE_DETAILS)
    fun addVehicleDetails(
        @Header("token") token: String,
        @Part("user_id") user_id: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("vehicle_type") vehicle_type: RequestBody,
        @Part("vehicle_number") vehicle_number: RequestBody,
        @Part("vehicle_sub_type") vehicle_sub_type: RequestBody,
        @Part("vehicle_brand") vehicle_brand: RequestBody,
        @Part("vehicle_details_id") vehicle_details_id: RequestBody,
        @Part driving_licence: MultipartBody.Part,
        @Part puc: MultipartBody.Part,
        @Part registration_paper: ArrayList<MultipartBody.Part>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part other: ArrayList<MultipartBody.Part>
    ): Observable<Response<JsonObject>>

    @POST(GET_RATING_REVIEW)
    fun getRatingReview(
        @Header("token") token: String,
        @Body jsonObject: RatingListRequest
    ): Observable<Response<RatingListResponse>>

    @POST(SUBMIT_RATING_REVIEW)
    fun submitRatingReview(
        @Header("token") token: String,
        @Body jsonObject: AddRatingRequest
    ): Observable<Response<JsonObject>>

    @GET(GET_ALLVEHICLETYPES)
    fun getAllVehicleTypes(
        @Header("token") token: String
    ): Observable<Response<VehicleTypeResponse>>

    @GET(GET_VEHICLESUBTYPES)
    fun getVehicleSubTypes(
        @Header("token") token: String,
        @Query("vehicle_type_id") vehicle_type_id: String
    ): Observable<Response<VehicleSubTypeResponse>>

    @GET(GET_VEHICLEBRANDS)
    fun getVehicleBrands(
        @Header("token") token: String,
        @Query("vehicle_subtype_id") vehicle_subtype_id: String
    ): Observable<Response<VehicleBrandResponse>>

    @POST(GET_CUSTVEHICLELIST)
    fun getCustVehicleList(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Observable<Response<VehicleListResponse>>

    @POST(GET_VEHICLEDETAILS)
    fun getVehicleDetails(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Observable<Response<VehicleDetailResponse>>
}