package info.fandroid.navdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class EditProduct extends AppCompatActivity implements View.OnClickListener {
    EditText editName;
    EditText editUsername;
    EditText editPass;
    EditText editAge;
    ImageView ViewImageCurent;
    Button buttonRegister;
    private Button buttonChoose;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap ;
    private String KEY_IMAGE = "image";
    String nume_product;
    String id_product;
    String Password;
    String CurentImage;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editName = (EditText) findViewById(R.id.editName);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPass = (EditText) findViewById(R.id.editPass);
        editAge = (EditText) findViewById(R.id.editAge);
        ViewImageCurent = (ImageView)findViewById(R.id.ViewImageCurent);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonChoose = (Button) findViewById(R.id.EditImage);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v == buttonChoose) {
                    showFileChooser();
                }
            }
        });

        Intent intent = getIntent();

         nume_product = intent.getStringExtra("nameProduct");
          id_product = intent.getStringExtra("IdProducT");
         Password = intent.getStringExtra("Password");
          CurentImage = intent.getStringExtra("ImageCurent");
        Bitmap productImage = decodeThumbnail(CurentImage);
        ViewImageCurent.setImageBitmap(productImage);
        editName.setText(nume_product);
        editPass.setText(Password);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        buttonRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){

        final String name = editName.getText().toString();
        final String username = editUsername.getText().toString();
        final int age = Integer.parseInt(editAge.getText().toString());
        final String password = editPass.getText().toString();
        //final String image = getStringImage(bitmap);

        Response.Listener<String> responseEdit = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText( getApplicationContext(), "modificarile au fost salvate!",
                                Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "o eroare a aparut!",
                                Toast.LENGTH_LONG).show();
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        if (bitmap != null){
             image = getStringImage(bitmap);

        }else{
            image = CurentImage;
        }
        //final String image = getStringImage(bitmap);

        RegisterEditProduct registerEditProduct = new RegisterEditProduct(name, username, age, password, image, id_product,  responseEdit);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(registerEditProduct);


    }
    private Bitmap decodeThumbnail(String thumbData) {
        byte[] bytes = Base64.decode(thumbData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
    private void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // String ter = null;
       // SingePrImage.setImageDrawable(null);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                ViewImageCurent.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected Map<String, String> getParams() throws AuthFailureError {
        //Converting Bitmap to String
        String image = getStringImage(bitmap);
        Map<String,String> params = new Hashtable<String, String>();
        params.put(KEY_IMAGE, image);
        return params;
    }



}
