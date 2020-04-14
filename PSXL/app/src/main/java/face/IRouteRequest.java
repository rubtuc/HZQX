package face;

import com.bignerdrach.android.psxl.Route;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRouteRequest {
    @GET("/findRoute")
    Call<Route> getRouteData();
}
