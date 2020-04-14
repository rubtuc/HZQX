package face;

import com.bignerdrach.android.psxl.Route;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISearchRoute {
    @FormUrlEncoded
    @POST("/searchRoute")
    Call<Route> getSearchRoute(@Field("route_description") String route_description);
}
