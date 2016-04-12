package info.fandroid.navdrawer.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import info.fandroid.navdrawer.R;
import info.fandroid.navdrawer.RegisterRequest;


public class FragmentSend extends Fragment implements  View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private String mParam1;
    private String mParam2;
    Button bRegister;
    EditText etAge;
    EditText etName;
    EditText etUsername;
    EditText etPassword;

    private OnFragmentInteractionListener mListener;
    private Button buttonChoose;
    private int PICK_IMAGE_REQUEST = 1;
    public static FragmentSend newInstance(String param1, String param2) {
        FragmentSend fragment = new FragmentSend();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSend() {
        // Required empty public constructor
    }


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
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == buttonChoose){
                    showFileChooser();
                }
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onClick(View view){
        final String name = etName.getText().toString();
        final String username = etUsername.getText().toString();
        final int age = Integer.parseInt(etAge.getText().toString());
        final String password = etPassword.getText().toString();

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

        RegisterRequest registerRequest = new RegisterRequest(name, username, age, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(registerRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
