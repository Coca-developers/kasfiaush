package info.fandroid.navdrawer;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 20.04.2016.
 */
public class RegisterEditProduct extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://itiscriu.eu/update_product.php";
    private Map<String, String> params;

    public RegisterEditProduct(String name, String CurrentUser , String Pdescript, String PAge, String PProducer, String image, String id_product ,  Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("CurrentUser", CurrentUser);
        params.put("description", Pdescript);
        params.put("age", PAge);
        params.put("producer", PProducer);
        params.put("CurentImage", image);
        params.put("id_product", id_product);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
