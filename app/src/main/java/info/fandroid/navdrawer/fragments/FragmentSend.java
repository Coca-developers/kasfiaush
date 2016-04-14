package info.fandroid.navdrawer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import com.kosalgeek.android.photoutil.ImageBase64;
import java.io.IOException;
import android.graphics.Bitmap;
import info.fandroid.navdrawer.R;
import info.fandroid.navdrawer.RegisterRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class FragmentSend extends Fragment implements  View.OnClickListener{

    Button bRegister;
    EditText etAge;
    EditText etName;
    EditText etUsername;
    EditText etPassword;

    private Button buttonChoose;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText editTextName;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        etAge = (EditText) getActivity().findViewById(R.id.etAge);
        etName = (EditText) getActivity().findViewById(R.id.etName);
        etUsername = (EditText) getActivity().findViewById(R.id.etUsername);
        etPassword = (EditText) getActivity().findViewById(R.id.etPassword);
        buttonChoose = (Button) getActivity().findViewById(R.id.buttonChoose);
        bRegister = (Button) getActivity().findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        imageView = (ImageView) getActivity().findViewById(R.id.imageViewP);
        editTextName = (EditText) getActivity().findViewById(R.id.editText);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v == buttonChoose) {
                    showFileChooser();
                }
            }
        });

    }
    @Override
    public void onClick(View view){
        final String name = etName.getText().toString();
        final String username = etUsername.getText().toString();
        final int age = Integer.parseInt(etAge.getText().toString());
        final String password = etPassword.getText().toString();
        final String image = getStringImage(bitmap);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getActivity(), "succes!",
                                Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getActivity(), "an error has occuaved!",
                                Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name, username, age, password, image, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(registerRequest);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                Map<String,String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, image);
                return params;
            }


    private void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
