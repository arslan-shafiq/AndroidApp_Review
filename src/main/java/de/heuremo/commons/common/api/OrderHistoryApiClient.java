package de.heuremo.commons.common.api;

import java.util.ArrayList;

import de.heuremo.commons.common.model.file_storage.FileStorageDTO;
import de.heuremo.commons.common.model.order.OrderDTO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface OrderHistoryApiClient {

    @GET("external-orders/tms/order")
    Call<ArrayList<OrderDTO>> getAllOrders(
            @Header("Authorization") String auth, @Query("limit") String limit);

    @POST("external-orders/tms/order")
    Call<OrderDTO> postOrder(@Body OrderDTO OrderDTO);

    @Multipart
    @POST("file-management/file-storage/create")
    Call<FileStorageDTO> uploadNote(@Header("Authorization") String auth, @Part MultipartBody.Part file, @Part("filePath") RequestBody filePath);

}
