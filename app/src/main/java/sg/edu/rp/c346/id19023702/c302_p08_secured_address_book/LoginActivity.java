package sg.edu.rp.c346.id19023702.c302_p08_secured_address_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private AsyncHttpClient client;
    private String authenticate, apikey;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO (1) When Login button is clicked, call doLogin.php web service to check if the user is able to log in
                // What is the web service URL?
                // What is the HTTP method?
                // What parameters need to be provided?
                client = new AsyncHttpClient();
                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/doLogin.php";
                RequestParams params = new RequestParams();
                params.add("username", etUsername.getText().toString());
                params.add("password", etPassword.getText().toString());

                client.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            id = response.getInt("id");
                            apikey = response.getString("apikey");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("loginId", id);
                            i.putExtra("apikey", apikey);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });
    }

    // TODO (2) Using AsyncHttpClient, check if the user has been authenticated successfully
    // If the user can log in, extract the id and API Key from the JSON object, set them into Intent and start MainActivity Intent.
    // If the user cannot log in, display a toast to inform user that login has failed.


}