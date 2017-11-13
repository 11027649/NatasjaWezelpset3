package e.natasja.natasjawezel__pset3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView dishesListView = (ListView) findViewById(R.id.dishes);

        final TextView mTextView = (TextView) findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/categories";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);




        String[] dishesArray = {"Starters", "Main dishes", "Desserts", "Side dishes",
                "Hot/cold beverages", "Alcoholic beverages"};

        ListAdapter dishesListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, dishesArray);

        dishesListView.setAdapter(dishesListAdapter);

        dishesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String cuisineCoursePicked = "You selected " +
                        String.valueOf(adapterView.getItemAtPosition(position));

                Toast.makeText(mainActivity.this, cuisineCoursePicked, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void toOrder (View view) {
        Intent intent = new Intent(this, YourOrderActivity.class);
        startActivity(intent);
    }
}
