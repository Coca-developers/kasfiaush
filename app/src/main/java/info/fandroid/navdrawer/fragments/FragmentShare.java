package info.fandroid.navdrawer.fragments;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.fandroid.navdrawer.R;
public class FragmentShare extends Fragment implements View.OnClickListener {
    private static final String JSON_ARRAY = "result";
    private static final String ID = "user_id";
    private static final String USERNAME = "name";
    private static final String PASSWORD = "username";
    private JSONArray user = null;

    private int TRACK = 0;
    private String temp;

    private EditText editTextId;
    private EditText editTextUserName;
    private EditText editTextPassword;

    Button btnPrev;
    Button btnNext;
    private static final String JSON_URL = "http://yupimedia.com/android_connect/Login.php";

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        editTextId = (EditText) getActivity().findViewById(R.id.editTextID);
        editTextUserName = (EditText) getActivity().findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);
        btnPrev = (Button) getActivity().findViewById(R.id.buttonPrev);
        btnNext = (Button) getActivity().findViewById(R.id.buttonNext);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        getJSON(JSON_URL);
        //extractJSON();


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


    private void moveNext() {
        if (TRACK < user.length()) {
            TRACK++;
        }
        showData();
    }

    private void movePrev() {
        if (TRACK > 0) {
            TRACK--;
        }
        showData();
    }

    private void showData() {
        try {
            JSONObject jsonObject = user.getJSONObject(TRACK);

            editTextId.setText(jsonObject.getString(ID));
            editTextUserName.setText(jsonObject.getString(USERNAME));
            editTextPassword.setText(jsonObject.getString(PASSWORD));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }
        if (v == btnPrev) {
            movePrev();
        }
    }

}