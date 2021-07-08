package sg.edu.rp.c346.id19023702.c302_p08_secured_address_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ArrayList<Contact> alContact;
    private ArrayAdapter<Contact> aaContact;
    private String apikey;
    private int loginId;
    private AsyncHttpClient client;

    // TODO (3) Declare loginId and apikey

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new AsyncHttpClient();

        lvContact = (ListView) findViewById(R.id.listViewContact);
        alContact = new ArrayList<Contact>();

        aaContact = new ContactAdapter(this, R.layout.contact_row, alContact);
        lvContact.setAdapter(aaContact);

        // TODO (4) Get loginId and apikey from the previous Intent
        Intent i = getIntent();
        loginId = i.getIntExtra("loginId", 0);
        apikey = i.getStringExtra("apikey");



        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey

                Contact selectedContact = alContact.get(position);
                Intent i = new Intent(getBaseContext(), ViewContactDetailsActivity.class);
                i.putExtra("contact_id", selectedContact.getContactId());
                i.putExtra("loginId", loginId);
                i.putExtra("apikey", apikey);
                i.putExtra("FirstName", selectedContact.getFirstName());
                i.putExtra("LastName", selectedContact.getLastName());
                i.putExtra("Mobile", selectedContact.getMobile());
                startActivity(i);

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

        alContact.clear();


        // TODO (5) Refresh the main activity with the latest list of contacts by calling getListOfContacts.php
        // What is the web service URL?
        // What is the HTTP method?
        //  What parameters need to be provided?
        RequestParams params = new RequestParams();
        String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getListOfContacts.php";
        params.add("loginId", String.valueOf(loginId));
        params.add("apikey", apikey);
        client.post(url ,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    Log.i("JSON Results: ", response.toString());
                    for(int i = 0; i < response.length(); i++){
                        JSONObject jsonObj = response.getJSONObject(i);
                        int contactId = jsonObj.getInt("id");
                        String firstName = jsonObj.getString("firstname");
                        String lastName = jsonObj.getString("lastname");
                        String mobile = jsonObj.getString("mobile");

                        Contact contact = new Contact(contactId, firstName, lastName, mobile);
                        alContact.add(contact);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                aaContact = new ContactAdapter(getApplicationContext(), R.layout.contact_row, alContact);
                lvContact.setAdapter(aaContact);

            }
        });

    }

    // TODO (6) Using AsyncHttpClient for getListOfContacts.php, get all contacts from the results and show in the list


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey
            Intent intent = new Intent(MainActivity.this, CreateContactActivity.class);
            intent.putExtra("loginId", loginId);
            intent.putExtra("apikey", apikey);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
