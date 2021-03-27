package com.otgs.customerapp.network

import com.otgs.customerapp.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.otgs.customerapp.model.request.*
import com.otgs.customerapp.model.response.*
import com.otgs.customerapp.model.response.GarageDetail.GarageDetailResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Part
import java.util.concurrent.TimeUnit


class ApiClient {

    private val myAppService: GetDataServices

    companion object {
        private var apiClient: ApiClient? = null

        /**
         * Gets my app client.
         *
         * @return the my app client
         */
        val instance: ApiClient
            get() {
                if (apiClient == null) {
                    apiClient =
                        ApiClient()
                }
                return apiClient as ApiClient
            }
    }

    init {
        val gson = GsonBuilder().setLenient().create()
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Basic", "secure@puneit")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Basic secure@puneit")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        myAppService = retrofit.create(GetDataServices::class.java)
    }

    fun postLogin(
        username: String,
        password: String,
        device_token: String,
        device_type: String,
        user_type: String,
        device_id: String
    ): Observable<Response<LoginResponse>> {
        val jsonObject = LoginRequest(
            username,
            password,
            device_token,
            device_type,
            user_type,
            device_id
        )
        return myAppService.login(jsonObject)
    }

    fun postRegister(
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
    ): Observable<Response<LoginResponse>> {
        return myAppService.register(
            profile,
            address,
            dob,
            email_id,
            emergency_contact1,
            emergency_contact2,
            emergency_contact3,
            first_name,
            last_name,
            gender,
            mobile_no,
            pincode,
            relation1,
            relation2,
            relation3,
            state_id,
            city_id,
            device_id
        )
    }

    fun getAllStates(): Observable<Response<StateResponse>> {
        return myAppService.getAllStates()
    }

    fun getCities(state_id: String): Observable<Response<CityReponse>> {
        return myAppService.getCities(state_id)
    }

    fun getPincodes(city_id: String): Observable<Response<PincodeResponse>> {
        return myAppService.getPincodes(city_id)
    }

    fun postForgotPwd(
        username: String,
        user_type: String
    ): Observable<Response<JsonObject>> {
        val jsonObject = ForgotPwdRequest(
            username,
            user_type
        )
        return myAppService.forgotPwd(jsonObject)
    }


    fun postResetPwd(
        new_password: String,
        old_password: String,
        user_id: Int,
        user_type: Int
    ): Observable<Response<JsonObject>> {
        val jsonObject = ChangePwdRequest(
            new_password,
            old_password,
            user_id,
            user_type
        )
        return myAppService.resetPassword(jsonObject)
    }

    fun getAllYears(): Observable<Response<YearResponse>> {
        return myAppService.getYears()
    }

    fun getAllMonths(): Observable<Response<MonthResponse>> {
        return myAppService.getMonths()
    }

    fun senOTP(mobile_number: String): Observable<Response<JsonObject>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("mobile_no", mobile_number)
        return myAppService.SendOTP(jsonObject)
    }

    fun postSubmitQuery(
        user_id: String,
        user_type: String,
        query: String,
        category_id: String
    ): Observable<Response<JsonObject>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        jsonObject.addProperty("query", query)
        jsonObject.addProperty("category_id", category_id)
        return myAppService.submitQuery(jsonObject)
    }

    fun getAllFaq(user_type: String): Observable<Response<FAQResponse>> {
        return myAppService.getAllFaq(user_type)
    }

    fun getAllCategories(): Observable<Response<ContactUsResponse>> {
        return myAppService.getAllCategories()
    }

    fun getCustomerList(
        token: String,
        customer_id: String
    ): Observable<Response<ProfileDetailResponse>> {
        return myAppService.getCustomerList(token, customer_id)
    }

    fun editCustomerProfile(
        token: String,
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
    ): Observable<Response<JsonObject>> {
        return myAppService.editCustomerProfile(
            token,
            profile,
            user_id,
            user_type,
            address,
            dob,
            email_id,
            emergency_contact1,
            emergency_contact2,
            emergency_contact3,
            first_name,
            last_name,
            gender,
            mobile_no,
            pincode,
            relation1,
            relation2,
            relation3,
            state_id,
            city_id
        )
    }

    fun getAllServiceMasterList(): Observable<Response<AutoHubResponse>> {
        return myAppService.getAllServiceMasterList()
    }

    fun getSOSDetail(
        token: String,
        user_id: String,
        user_type: String
    ): Observable<Response<SOSResponse>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        return myAppService.getSosDetails(token, jsonObject)
    }

    fun getGarageList(
        token: String,
        latitude: Double,
        longitude: Double,
        pickup_flag: Int,
        radius: String,
        service_id: String,
        user_id: String,
        user_type: String,
        doorstep_service_id: Int
    ): Observable<Response<GarageResponse>> {
        val jsonObject = GoogleMapDataRequest(
            latitude,
            longitude,
            pickup_flag,
            radius,
            service_id,
            user_id,
            user_type,
            doorstep_service_id
        )
        return myAppService.getGarageList(token, jsonObject)
    }

    fun getServiceProviderDetails(
        token: String,
        service_provider_id: String,
        cust_id: String
    ): Observable<Response<GarageDetailResponse>> {
        return myAppService.getServiceProviderDetails(token, service_provider_id, cust_id)
    }

    fun getServiceProviderDetails(
        token: String,
        state_id: String
    ): Observable<Response<RTOResponse>> {
        return myAppService.getAllRtoRules(token, state_id)
    }

    fun getServicingList(
        token: String,
        user_id: String,
        user_type: String
    ): Observable<Response<ServicingResponse>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        return myAppService.getServicingList(token, jsonObject)
    }

    fun getcustomerBookingDetails(
        token: String,
        end_date: String,
        start_date: String,
        type: Int,
        user_id: Int,
        user_type: Int,
        vehicle_type: Int,
        brand: Int,
        status: Int
    ): Observable<Response<BookingResponse>> {
        val jsonObject = BookingRequest(
            end_date,
            start_date,
            type,
            user_id,
            user_type,
            vehicle_type,
            brand,
            status
        )
        return myAppService.getcustomerBookingDetails(token, jsonObject)
    }

    fun getBrand(
        token: String,
        user_id: Int,
        user_type: Int
    ): Observable<Response<BrandResponse>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        return myAppService.getBrand(token, jsonObject)
    }

    fun postAaddVehicleDetails(
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
    ): Observable<Response<JsonObject>> {
        return myAppService.addVehicleDetails(
            token,
            user_id,
            user_type,
            vehicle_type,
            vehicle_number,
            vehicle_sub_type,
            vehicle_brand,
            vehicle_details_id,
            driving_licence,
            puc,
            registration_paper,
            insurance,
            other
        )
    }

    fun postAaddVehicleDetails(
        token: String,
        end_date: String,
        page_number: Int,
        start_date: String,
        user_id: Int,
        user_type: Int
    ): Observable<Response<RatingListResponse>> {
        val jsonObject = RatingListRequest(
            end_date,
            page_number,
            start_date,
            user_id,
            user_type
        )
        return myAppService.getRatingReview(token, jsonObject)
    }

    fun submitRatingReview(
        token: String,
        date: String,
        order_id: Int,
        rating: Int,
        review: String,
        sp_id: Int,
        user_id: Int,
        user_type: Int
    ): Observable<Response<JsonObject>> {
        val jsonObject = AddRatingRequest(
            date,
            order_id,
            rating,
            review,
            sp_id,
            user_id,
            user_type
        )
        return myAppService.submitRatingReview(token, jsonObject)
    }

    fun getAllVehicleTypes(
        token: String
    ): Observable<Response<VehicleTypeResponse>> {
        return myAppService.getAllVehicleTypes(token)
    }

    fun getVehicleSubTypes(
        token: String,
        vehicle_type_id: String
    ): Observable<Response<VehicleSubTypeResponse>> {
        return myAppService.getVehicleSubTypes(token, vehicle_type_id)
    }

    fun getVehicleBrands(
        token: String,
        vehicle_subtype_id: String
    ): Observable<Response<VehicleBrandResponse>> {
        return myAppService.getVehicleBrands(token, vehicle_subtype_id)
    }

    fun getCustVehicleList(
        token: String,
        user_id: String,
        user_type: String
    ): Observable<Response<VehicleListResponse>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        return myAppService.getCustVehicleList(token, jsonObject)
    }

    fun getVehicleDetails(
        token: String,
        user_id: String,
        user_type: String,
        vehicle_details_id: String
    ): Observable<Response<VehicleDetailResponse>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("user_type", user_type)
        jsonObject.addProperty("vehicle_details_id", vehicle_details_id)
        return myAppService.getVehicleDetails(token, jsonObject)
    }
}