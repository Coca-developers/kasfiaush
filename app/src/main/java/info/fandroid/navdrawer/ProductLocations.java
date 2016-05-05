package info.fandroid.navdrawer;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 29.04.2016.
 */

public class ProductLocations extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://itiscriu.eu/getLocation.php";
    private Map<String, String> params;

    public ProductLocations(String id_product, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("ProductId", id_product);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}