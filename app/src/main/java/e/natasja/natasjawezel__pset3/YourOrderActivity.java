package e.natasja.natasjawezel__pset3;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class YourOrderActivity extends AppCompatActivity {
    int spaghettis, soups, pizzas, sandwiches, pesto_linguinis, salads;
    long selected;
    Button editButton;
    Button submitButton;
    TextView title;
    ListView dishesListView;
    List<String> ordersArray;
    ArrayAdapter dishesListAdapter;
    boolean submitted;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        loadFromSharedPrefs();

        submitted = false;
        editButton = findViewById(R.id.editButton);
        submitButton = findViewById(R.id.submitButton);
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setBackgroundColor(Color.GRAY);
        title = findViewById(R.id.title);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Are you sure you want to continue? You'll lose the information about this order.").setTitle("Continue?");

        // Add the buttons
        builder.setPositiveButton("Yes, continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                ordersArray.clear();
                soups = 0;
                salads = 0;
                pesto_linguinis = 0;
                pizzas = 0;
                sandwiches = 0;
                spaghettis = 0;
                editList();

                Intent toStart = new Intent(YourOrderActivity.this, mainActivity.class);
                startActivity(toStart);
                finish();

            }
        }).setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        dialog = builder.create();

        if (name.equals("Spaghetti and Meatballs")) {
            spaghettis += 1;
        }

        if (name.equals("Chicken Noodle Soup")) {
            soups += 1;
        }

        if (name.equals("Margherita Pizza")) {
            pizzas += 1;
        }

        if (name.equals("Grilled Steelhead Trout Sandwich")) {
            sandwiches += 1;
        }

        if (name.equals("Pesto Linguini")) {
            pesto_linguinis += 1;
        }

        if (name.equals("Italian Salad")) {
            salads += 1;
        }
        else {
        }

        dishesListView = (ListView) findViewById(R.id.dishes);
        ordersArray = new ArrayList<String>();
        dishesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ordersArray);
        dishesListView.setAdapter(dishesListAdapter);
        dishesListView.setOnItemLongClickListener(new ClickSomeLong());

        editList();
    }

    private class ClickSomeLong implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (submitted == false) {
                editButton = findViewById(R.id.editButton);
                submitButton = findViewById(R.id.submitButton);

                editButton.setText("Delete this!");
                submitButton.setText("Add one of this!");

                title.setVisibility(View.INVISIBLE);

                selected = id;
            }

            return true;
        }
    }

    public void editList() {
        ordersArray.clear();

        if (spaghettis > 0) {
            ordersArray.add(spaghettis + "x Spaghetti and Meatballs");
        }

        if (soups > 0) {
            ordersArray.add(soups + "x Chicken Noodle Soup");
        }

        if (pizzas > 0) {
            ordersArray.add(pizzas + "x Margherita Pizza");
        }

        if (sandwiches > 0) {
            ordersArray.add(sandwiches + "x Grilled Steelhead Trout Sandwich");
        }

        if (pesto_linguinis > 0) {
            ordersArray.add(pesto_linguinis + "x Pesto Linguini");
        }

        if (salads > 0) {
            ordersArray.add(salads + "x Italian Salad");
        }

        dishesListAdapter.notifyDataSetChanged();
        saveToSharedPrefs();

    }

    public void saveToSharedPrefs() {

        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("orderList1", spaghettis);
        editor.putInt("orderList2", soups);
        editor.putInt("orderList3", pizzas);
        editor.putInt("orderList4", sandwiches);
        editor.putInt("orderList5", pesto_linguinis);
        editor.putInt("orderList6", salads);

        editor.commit();
    }

    public void loadFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        spaghettis = prefs.getInt("orderList1", spaghettis);
        soups = prefs.getInt("orderList2", soups);
        pizzas = prefs.getInt("orderList3", pizzas);
        sandwiches = prefs.getInt("orderList4", sandwiches);
        pesto_linguinis = prefs.getInt("orderList5", pesto_linguinis);
        salads = prefs.getInt("orderList6", salads);

    }

    public void edit(View view) {

        if (editButton.getText().equals("Edit") ){
            Toast.makeText(YourOrderActivity.this, "Long click on the dish you want to edit!", Toast.LENGTH_SHORT).show();
        }
        else {
            if ((ordersArray.get((int) selected)).endsWith("Spaghetti and Meatballs")) {
                spaghettis -= 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Chicken Noodle Soup")) {
                soups -= 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Margherita Pizza")) {
                pizzas -= 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Grilled Steelhead Trout Sandwich")) {
                sandwiches -= 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Pesto Linguini")) {
                pesto_linguinis -= 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Italian Salad")) {
                salads -= 1;
            }

            editList();

            editButton.setText("Edit");
            submitButton.setText("Submit");
            title.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        // do something on back.
        dialog.show();
        return;
    }

    public void submit(View view) {

        if (submitButton.getText().equals("Submit") ){
            // Instantiate the RequestQueue.
            String url = "https://resto.mprog.nl/order";
            Log.d("Submit", "Submitbutton text equals submit");
            submitted = true;
            //ordersArray.clear();
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                TextView waitingTime = findViewById(R.id.waitingTime);
                                waitingTime.setText("The expected waiting time is: " + response.getString("preparation_time") + " minutes");
                                submitButton.setVisibility(View.INVISIBLE);
                                editButton.setVisibility(View.INVISIBLE);
                                title.setText("Here's what you've ordered");

                                Button orderButton = findViewById(R.id.orderButton);
                                Button menuButton = findViewById(R.id.menuButton);

                                orderButton.setVisibility(View.INVISIBLE);
                                menuButton.setVisibility(View.INVISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // show error (in case of error)
                            Toast.makeText(YourOrderActivity.this, "No response on Request.", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(jsObjRequest);

            Toast.makeText(YourOrderActivity.this, "You've submitted your order!", Toast.LENGTH_SHORT).show();

            int keyCode = 15;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
            }

        }

        else {
            Log.d("Submit", "Submitbutton text doesn't equal submit");

            if ((ordersArray.get((int) selected)).endsWith("Spaghetti and Meatballs")) {
                spaghettis += 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Chicken Noodle Soup")) {
                soups += 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Margherita Pizza")) {
                pizzas += 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Grilled Steelhead Trout Sandwich")) {
                sandwiches += 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Pesto Linguini")) {
                pesto_linguinis += 1;
            }

            if ((ordersArray.get((int) selected)).endsWith("Italian Salad")) {
                salads += 1;
            }

            editList();

            submitButton.setText("Submit");
            editButton.setText("Edit");
            title.setVisibility(View.VISIBLE);
        }
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
