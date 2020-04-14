package face;

import com.bignerdrach.android.psxl.Action;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IActionUpdate {
    @FormUrlEncoded
    @POST("/updateAction")
    Call<Action> getUpdateAction(@Field("publish_id") String publish_id, @Field("publish_shared") String publish_shared, @Field("publish_loved") String publish_loved,
                                 @Field("publish_discuss") String publish_discuss);
}
