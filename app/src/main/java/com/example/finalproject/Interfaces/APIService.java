package com.example.finalproject.Interfaces;



import com.example.finalproject.Notifications.MyResponse;
import com.example.finalproject.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA7KKU4vY:APA91bGBHgZ-UQCW3n8rfkd7Stb-I9sOx-lMVPpo3QlO4-oM48F-MLvo1JYpEV8TUyzbFRH6SF1w78d8OdMOtA7EvynIdtoBmSiaZlazc2eMjNHDD4vr3Laju1WdV53beNAVWr9dHydH"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
