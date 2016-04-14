package info.fandroid.navdrawer;

/**
 * Created by User on 06.04.2016.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yupimedia.com/android_connect/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, int age, String image, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("age", age + "");
        params.put("username", username);
        params.put("password", password);
        params.put("image_name", image);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}