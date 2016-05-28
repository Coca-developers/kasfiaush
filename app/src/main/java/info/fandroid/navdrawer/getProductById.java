package info.fandroid.navdrawer;

        import com.android.volley.Response;
        import com.android.volley.toolbox.StringRequest;

        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by User on 19.04.2016.
 */
public class getProductById extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://itiscriu.eu/CurrentUserProducts.php";
    private Map<String, String> params;

    public getProductById(String ProductId, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("customID", ProductId);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}