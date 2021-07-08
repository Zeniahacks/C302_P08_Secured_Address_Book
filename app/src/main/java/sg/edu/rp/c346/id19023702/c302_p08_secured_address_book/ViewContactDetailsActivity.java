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

public class ViewContactDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnUpdate, btnDelete;
    private int contactId;
    private AsyncHttpClient client;
    String loginId, apikey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        client = new AsyncHttpClient();
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);

        Intent intent1 = getIntent();
        etFirstName.setText(intent1.getStringExtra("FirstName"));
        etLastName.setText(intent1.getStringExtra("LastName"));
        etMobile.setText(intent1.getStringExtra("Mobile"));


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                Intent intent = getIntent();
                String loginId = intent.getStringExtra("loginId");
                String apikey = intent.getStringExtra("apikey");
                params.add("id", String.valueOf(contactId));
                params.add("FirstName", etFirstName.getText().toString());
                params.add("LastName", etLastName.getText().toString());
                params.add("Mobile", etMobile.getText().toString());
                params.add("loginId", loginId);
                params.add("apikey", apikey);
                client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/updateContact.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());
                            Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String loginId = intent.getStringExtra("loginId");
                String apikey = intent.getStringExtra("apikey");
                RequestParams params = new RequestParams();
                params.add("id", String.valueOf(contactId));
                params.add("loginId", loginId);
                params.add("apikey", apikey);

                client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/deleteContact.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            Toast.makeText(ViewContactDetailsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Code for step 1 start
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);
        String loginId = intent.getStringExtra("loginId");
        String apikey = intent.getStringExtra("apikey");
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(contactId));
        params.add("loginId", loginId);
        params.add("apikey", apikey);
        client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getContactDetails.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    etFirstName.setText(response.getString("firstname"));
                    etLastName.setText(response.getString("lastname"));
                    etMobile.setText(response.getString("mobile"));
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}