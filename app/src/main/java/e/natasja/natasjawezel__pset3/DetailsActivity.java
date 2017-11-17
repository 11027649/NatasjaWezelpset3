package e.natasja.natasjawezel__pset3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailsActivity extends AppCompatActivity {
    ImageView imageDish;
    RequestQueue queue;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent details = getIntent();
        final String name = details.getStringExtra("name");
        final String[] imageUrl = new String[1];


        // find TextViews and ImageView
        final TextView selectedDish = (TextView) findViewById(R.id.selectedDish);
        final TextView priceDish = (TextView) findViewById(R.id.priceDish);
        final TextView descriptionDish = (TextView) findViewById(R.id.descriptionDish);
        final TextView categoryDish = (TextView) findViewById(R.id.categoryDish);
        imageDish = (ImageView) findViewById(R.id.imageDish);

        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setBackgroundColor(Color.GRAY);

        // Instantiate the RequestQueue.
        String url = "https://resto.mprog.nl/menu";

        queue = Volley.newRequestQueue(this);

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

                                String whichDish = object.getString("name");

                                // if in the right category
                                if (whichDish.equals(name)) {
                                    title = object.getString("name");
                                    selectedDish.setText(title);

                                    String price = "Price: $" + object.getString("price")+ " ";
                                    priceDish.setText(price);

                                    String category = "Category: " + object.getString("category");
                                    categoryDish.setText(category);

                                    String description = "Description: " + object.getString("description");
                                    descriptionDish.setText(description);

                                    // with the imageURL, you need to make a new request to the server
                                    imageUrl[0] = object.getString("image_url");

                                    Log.d("imageUrl", imageUrl[0]);
                                    imageRequestFunction(imageUrl[0]);

                                }
                            }

                        } catch (JSONException e) {

                            // show error (in case of an error)
                            Toast.makeText(DetailsActivity.this, "JSONObject to JSONArray didn't go right", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // show error (in case of error)
                        Toast.makeText(DetailsActivity.this, "No response on Request.", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);


    }

    public void imageRequestFunction(String imageUrl) {
        // bron: https://www.programcreek.com/javi-api-examples/index.php?api=com.android.volley.toolbox.ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageDish.setImageBitmap(bitmap);
            }

        },
                0,
                0,
                null,
                Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailsActivity.this, "Loading image failed", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(imageRequest);
    }

    // if menu button is clicked, go to menu
    public void toMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("category", "");
        startActivity(intent);
    }

    // if order button is clicked, go to order
    public void toOrder (View view) {
        Intent intent = new Intent(this, YourOrderActivity.class);
        intent.putExtra("name", "");
        startActivity(intent);
    }

    public void ordered(View view) {
        Intent intent = new Intent(this, YourOrderActivity.class);
        intent.putExtra("name", title);
        startActivity(intent);

    }
}
