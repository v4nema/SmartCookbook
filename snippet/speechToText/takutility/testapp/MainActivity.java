package takutility.testapp;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        /*
        findViewById(R.id.btnSphinx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SphinxActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnStandard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StandardActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnCont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContinuusServiceActivity.class);
                startActivity(intent);
            }
        });
        */

        Intent intent = new Intent(MainActivity.this, StandardActivity.class);
        startActivity(intent);
    }
}
