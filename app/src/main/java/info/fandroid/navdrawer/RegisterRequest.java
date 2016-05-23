package info.fandroid.navdrawer;

/**
 * Created by User on 06.04.2016.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://itiscriu.eu/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String producer, String description, String ProducYear, String image, String CurrentUser, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("producer", producer);
        params.put("ProducYear", ProducYear);
        params.put("image_name", image);
        params.put("CurrentUser" , CurrentUser);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}