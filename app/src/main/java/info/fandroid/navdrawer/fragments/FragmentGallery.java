package info.fandroid.navdrawer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.fandroid.navdrawer.R;
import info.fandroid.navdrawer.SingleProduct;

public class FragmentGallery extends Fragment implements View.OnClickListener{

    private static final String JSON_ARRAY = "result";
    private static final String ID = "product_id";
    private static final String ProductName = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRODUCER = "producatorul";
    private static final String ProductImage = "image";
    private static final String ProductYear = "anulProducerii";
    private static final String AddedAt = "AddedAt";
    private JSONArray user = null;
    Button readMore;
    private int TRACK = 0;
    private static final String JSON_URL = "http://itiscriu.eu/AllProducts.php";
    LinearLayout ll;

    private OnFragmentInteractionListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, null);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll = (LinearLayout)getActivity().findViewById(R.id.parentL);

        getJSON(JSON_URL);
        //extractJSON();
    }

    private Bitmap decodeThumbnail(String thumbData) {
        byte[] bytes = Base64.decode(thumbData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");

                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s.toString());
                    user = jsonObject.getJSONArray(JSON_ARRAY);

                    showData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
    private void showData() {
        try {


            for (TRACK = 0; TRACK < user.length(); TRACK++) {
                JSONObject jsonObject = user.getJSONObject(TRACK);
                LinearLayout frL = new LinearLayout(getActivity()) ;
                TextView name = new TextView(getActivity());
                TextView Producator = new TextView(getActivity());
                TextView year = new TextView(getActivity());
                readMore = new Button(getActivity());
                readMore.setText("read more");
                name.setId(TRACK + 10);
                Producator.setId(TRACK + 30);
                year.setId(TRACK + 40);
                frL.setId(TRACK + 20);

                final  String nameProduct = jsonObject.getString(ProductName);
                final String descriptionProduct= jsonObject.getString(DESCRIPTION);
                final String IdProducT = jsonObject.getString(ID);
                final String producer = jsonObject.getString(PRODUCER);
                final String anulProducerii= jsonObject.getString(ProductYear);
                final String DataAdaugarii = jsonObject.getString(AddedAt);
                final String encodedImage =  jsonObject.getString(ProductImage);
                ImageView productImageFinal = new ImageView(getActivity());
                //productImageFinal.setId(TRACK);
                //  readMore.setId("butonulmeu");

                readMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SingleProduct.class);
                        intent.putExtra("nameProduct", nameProduct);
                        intent.putExtra("descriptionProduct", descriptionProduct);
                        intent.putExtra("producer", producer);
                        intent.putExtra("IdProducT", IdProducT);
                        intent.putExtra("DataAdaugarii", DataAdaugarii);
                        intent.putExtra("anulProducerii", anulProducerii);

                        intent.putExtra("ImageCurent", encodedImage);
                        startActivity(intent);
                    }
                });
                frL.setPadding(30, 30, 30, 30);
                frL.setOrientation(LinearLayout.VERTICAL);
                ll.addView(frL);
                Producator.setText(jsonObject.getString(PRODUCER));
                name.setText(jsonObject.getString(ProductName));
                year.setText(jsonObject.getString(ProductYear));
                frL.addView(name);
                frL.addView(Producator);
                frL.addView(year);


                Bitmap my = decodeThumbnail(encodedImage);
                productImageFinal.setImageBitmap(my);

                frL.addView(productImageFinal);
                frL.addView(readMore);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

    }
}