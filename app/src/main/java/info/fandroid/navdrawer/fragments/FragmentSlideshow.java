package info.fandroid.navdrawer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
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

import com.facebook.Profile;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import android.graphics.Bitmap;
import info.fandroid.navdrawer.R;
import info.fandroid.navdrawer.RegisterRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class FragmentSlideshow extends Fragment implements  View.OnClickListener{

    Button bRegister;
    EditText etDescription;
    EditText etName;
    EditText etProducer;
    EditText etYear;
    ImageView imageViewP;
    private Button buttonChoose;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slideshow, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        etDescription = (EditText) getActivity().findViewById(R.id.etAge);
        etName = (EditText) getActivity().findViewById(R.id.etName);
        etProducer = (EditText) getActivity().findViewById(R.id.etUsername);
        etYear = (EditText) getActivity().findViewById(R.id.etPassword);
        imageViewP = (ImageView) getActivity().findViewById(R.id.imageViewP);
        buttonChoose = (Button) getActivity().findViewById(R.id.buttonChoose);
        bRegister = (Button) getActivity().findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
//        TextView = (TextView) getActivity().findViewById(R.id.imageViewP);
        //editTextName = (EditText) getActivity().findViewById(R.id.editText);
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
        final String producer = etProducer.getText().toString();
        final String description = etDescription.getText().toString();
        final String ProducYear = etYear.getText().toString();
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

                        Toast.makeText(getActivity(), "an error has occured!",
                                Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Profile profile = Profile.getCurrentProfile();
        String CurrentUser = profile.getId();

        RegisterRequest registerRequest = new RegisterRequest(name, producer, description, ProducYear, image, CurrentUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(registerRequest);

    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
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
                imageViewP.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
