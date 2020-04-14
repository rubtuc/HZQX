package face;

import com.bignerdrach.android.psxl.Action;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IActionRequest {
    @GET("/findAction")
    Call<Action> getActionInfo();
}
