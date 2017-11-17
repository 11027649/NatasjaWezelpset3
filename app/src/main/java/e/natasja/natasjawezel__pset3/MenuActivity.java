package e.natasja.natasjawezel__pset3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");

        // find listview dishesList and the TextView
        ListView dishesListView = (ListView) findViewById(R.id.dishes);

        final List<String> categoriesArray = new ArrayList<String>();
        final ArrayAdapter dishesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, categoriesArray);
        dishesListView.setAdapter(dishesListAdapter);

        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setBackgroundColor(Color.GRAY);

        if (category.equals("")) {
            Toast.makeText(MenuActivity.this, "Please select a course.", Toast.LENGTH_SHORT).show();
            Intent noChoice = new Intent(this, mainActivity.class);
            startActivity(noChoice);
            finish();

        } else {

            // Instantiate the RequestQueue.
            String url = "https://resto.mprog.nl/menu";

            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray group = response.getJSONArray("items");

                                // loop over JsonArray group and put the words in categoriesArray
                                for (int i = 0; i < group.length(); i++) {

                                    JSONObject object = group.getJSONObject(i);

                                    String appetizerOrEntree = object.getString("category");

                                    // if in the right category
                                    if (appetizerOrEntree.equals(category)) {
                                        categoriesArray.add(object.getString("name"));
                                    }
                                }

                                // make sure the list changes
                                dishesListAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {

                                // show error (in case of an error)
                                Toast.makeText(MenuActivity.this, "JSONObject to JSONArray didn't go right", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // show error (in case of error)
                            Toast.makeText(MenuActivity.this, "No response on Request.", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(jsObjRequest);

        }

        // if listItem is clicked, start intent to go to menu
        dishesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // show toast with what category is picked
                String cuisineCoursePicked = "You selected " +
                        String.valueOf(adapterView.getItemAtPosition(position));

                Toast.makeText(MenuActivity.this, cuisineCoursePicked, Toast.LENGTH_SHORT).show();

                // start intent to go to menu where that category is showed
                Intent menuItemSelected = new Intent(MenuActivity.this, DetailsActivity.class);
                menuItemSelected.putExtra("name", String.valueOf(adapterView.getItemAtPosition(position)));
                startActivity(menuItemSelected);
            }
        });
    }


    public void toMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("category", "");
        startActivity(intent);
        finish();
    }

    public void toOrder (View view) {
        Intent intent = new Intent(this, YourOrderActivity.class);
        intent.putExtra("name", "");
        startActivity(intent);
        finish();
    }
}
