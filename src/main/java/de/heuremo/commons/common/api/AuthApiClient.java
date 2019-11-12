package de.heuremo.commons.common.api;

import de.heuremo.commons.common.model.AuthLoginDTO;
import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiClient {

    @POST("secure/auth/token/create")
    Call<AuthLoginDTO> createToken(
            @Query("username") String username, @Query("password") String password);

    @POST("secure/user-management/foreign-user/registration")
    Call<userRegistrationDTO> registration(@Body userRegistrationDTO userRegistrationDTO);

    @POST("/foreign-user/change-password")
    Call<AuthLoginDTO> changePassword(
            @Query("userName") String username, @Query("newPassword") String newPassword, @Query("oldPassword") String oldPassword);


}
