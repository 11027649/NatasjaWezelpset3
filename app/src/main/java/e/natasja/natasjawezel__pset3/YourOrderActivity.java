package e.natasja.natasjawezel__pset3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class YourOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);
    }

    public void toMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void toOrder (View view) {
        Intent intent = new Intent(this, YourOrderActivity.class);
        startActivity(intent);
        finish();
    }
}
