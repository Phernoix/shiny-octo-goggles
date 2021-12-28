package sp.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void EmailButton(View view ){
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:eklaseboer@gmail.com")));
    }
    public void telegramMsg(View view){
        try {
            Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
            telegramIntent.setData(Uri.parse("http://telegram.me/Phernoix"));
            startActivity(telegramIntent);

        } catch (Exception e) {

        }
    }
    public void phoneMsg(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+6594760130"));
        startActivity(intent);
    }
}