package app.akbank.com.akbankapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import app.akbank.com.akbankapi.R;
import app.akbank.com.akbankapi.adapter.ATMAdapter;
import app.akbank.com.akbankapi.app.EndPoints;
import app.akbank.com.akbankapi.app.MyApplication;
import app.akbank.com.akbankapi.helper.SimpleDividerItemDecoration;
import app.akbank.com.akbankapi.model.ATM;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<ATM> atmArrayList;
    private ATMAdapter atmAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewATM);
        atmArrayList=new ArrayList<>();
        atmAdapter=new ATMAdapter(this,atmArrayList);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.addOnItemTouchListener(new ATMAdapter.RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ATMAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ATM atm=atmArrayList.get(position);
                Snackbar.make(view, "latitude:"+atm.getLatitude()+
                                     " longitude:"+atm.getLongitude() , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(atmAdapter);
        try {
            findAtm();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(atmAdapter.getItemCount()));

    }

    private void findAtm() throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", "40.995460");
        params.put("longitude", "28.978359");
        params.put("radius", "1000000");
        params.put("city", null);
        params.put("district", null);
        params.put("searchText", null);
        JSONObject parameters = new JSONObject(params);
        //     final JSONObject jsonBody = new JSONObject("{  \"latitude\": 41.008238,  \"longitude\": 28.978359,  \"radius\": 1000,  \"city\": null,  \"district\": null,  \"searchText\": null}");
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,EndPoints.FIND_ATM, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG+":erdinc:",response.toString());

                try {
                    if(response.getString("returnCode").equals("")&&response.getString("returnMsg").equals("")){
                        Log.d(TAG,"Basarılı");
                        List<String> allNames = new ArrayList<>();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0; i<data.length(); i++) {
                            ATM atm=new ATM();
                            JSONObject temp = data.getJSONObject(i);
                            String address = temp.getString("address");
                          //  allNames.add(name);
                            Log.d(TAG,address);

                            atm.setAddress(temp.getString("address"));
                            atm.setCity(temp.getString("city"));
                            atm.setDeviceType(temp.getString("deviceType"));
                            atm.setDistrict(temp.getString("district"));
                            atm.setExchangeAvailable(temp.getString("exchangeAvailable"));
                            atm.setLatitude(temp.getString("latitude"));
                            atm.setLongitude(temp.getString("longitude"));
                            atm.setName(temp.getString("name"));

                            atmArrayList.add(atm);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                atmAdapter.notifyDataSetChanged();
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG+":",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", "41.008238");
                params.put("longitude", "28.978359");
                params.put("radius", "1000");
                params.put("city", null);
                params.put("district", null);
                params.put("searchText", null);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("apikey", "l7xxdd95497a30fa403b99bd37a3ebaa0052");

                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
