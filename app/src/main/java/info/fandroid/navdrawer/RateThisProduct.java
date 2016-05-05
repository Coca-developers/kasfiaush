package info.fandroid.navdrawer;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 03.05.2016.
 */
public class RateThisProduct extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://itiscriu.eu/RateThisProduct.php";
    private Map<String, String> params;

    public RateThisProduct(String userID, String ProductId, int grade, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
       // params.put("age", age + "");
        params.put("ProductId", ProductId );
        params.put("grade", grade + "");


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}