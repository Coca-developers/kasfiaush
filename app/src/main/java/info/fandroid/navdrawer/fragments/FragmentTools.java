package info.fandroid.navdrawer.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import info.fandroid.navdrawer.DeleteProduct;
import info.fandroid.navdrawer.EditProduct;
import info.fandroid.navdrawer.R;
import info.fandroid.navdrawer.RegisterRequestN;
import info.fandroid.navdrawer.SingleProduct;

public class FragmentTools extends Fragment {

    private static final String JSON_ARRAY = "result";
    private static final String ID = "product_id";
    private static final String ProductName = "ProductName";
    private static final String DESCRIPTION = "description";
    private static final String PRODUCER = "producatorul";
    private static final String ProductImage = "image";
    private static final String ProductYear = "anulProducerii";
    private static final String AddedAt = "AddedAt";

    private JSONArray user = null;
    private int TRACK = 0;
    LinearLayout ParentLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParentLayout = (LinearLayout)getActivity().findViewById(R.id.MyProucts);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);
                    user = j.getJSONArray(JSON_ARRAY);
                    showData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Profile profile = Profile.getCurrentProfile();
        String customID = profile.getId();
        RegisterRequestN registerRequest = new RegisterRequestN(customID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(registerRequest);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void showData() {
        try {
            for (TRACK = 0; TRACK < user.length(); TRACK++) {
                JSONObject jsonObject = user.getJSONObject(TRACK);
              final  LinearLayout frL = new LinearLayout(getActivity()) ;
                TextView name = new TextView(getActivity());
                TextView despript = new TextView(getActivity());
                TextView producer = new TextView(getActivity());
                TextView  productYearV = new TextView(getActivity());
                TextView  addedAtV = new TextView(getActivity());

                Button  delleteProduct = new Button(getActivity());
                Button EditThisProduct = new Button(getActivity());
                delleteProduct.setText("delete this item");
                EditThisProduct.setText("edit this item");
                name.setId(TRACK + 10);
                despript.setId(TRACK + 30);
                producer.setId(TRACK + 40);
                frL.setId(TRACK);

                final  String nameProduct = jsonObject.getString(ProductName);
                final String ProductDescription= jsonObject.getString(DESCRIPTION);
                final String IdProducT = jsonObject.getString(ID);
                final String Producer = jsonObject.getString(PRODUCER);
                final String encodedImage =  jsonObject.getString(ProductImage);
                final String ProductYearT = jsonObject.getString(ProductYear);
                final String AddedAtT =jsonObject.getString(AddedAt);

                ImageView productImageFinal = new ImageView(getActivity());
                EditThisProduct.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getActivity(), EditProduct.class);
                        intent.putExtra("nameProduct", nameProduct);
                        intent.putExtra("descriptionProduct", ProductDescription);
                        intent.putExtra("producer", Producer);
                        intent.putExtra("IdProducT", IdProducT);
                        intent.putExtra("DataAdaugarii", AddedAtT);
                        intent.putExtra("anulProducerii", ProductYearT);

                        intent.putExtra("ImageCurent", encodedImage);

                        startActivity(intent);
                    }

                });
                delleteProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Are you sure you want to delete this Products?");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseDelete = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            ParentLayout.removeView(frL);
                                            boolean success = jsonResponse.getBoolean("success");
                                            /*if (success ) {
                                                Toast.makeText(getActivity(), " a fost sters!",
                                                        Toast.LENGTH_LONG).show();

                                            } else {


                                            }*/
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeleteProduct deleteProduct = new DeleteProduct(IdProducT, responseDelete);
                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                queue.add(deleteProduct);
                            }
                        });
                        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Context context = getActivity().getApplicationContext();
                                Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.show();
                    }
                });
                frL.setPadding(30, 30, 30, 30);
                frL.setOrientation(LinearLayout.VERTICAL);
                ParentLayout.addView(frL);
                despript.setText(jsonObject.getString(DESCRIPTION));
                name.setText(jsonObject.getString(ProductName));
                producer.setText(Producer);
                productYearV.setText(ProductYearT);
                addedAtV.setText(AddedAtT);
                frL.addView(name);
                frL.addView(despript);
                frL.addView(producer);
                frL.addView(addedAtV);
                Bitmap my = decodeThumbnail(encodedImage);
                productImageFinal.setImageBitmap(my);
                frL.addView(productImageFinal);
                frL.addView(delleteProduct);
                frL.addView(EditThisProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
   private Bitmap decodeThumbnail(String thumbData) {
        byte[] bytes = Base64.decode(thumbData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
