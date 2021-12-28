package sp.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DetailForm extends AppCompatActivity {
    private RatingBar foodRating;
    private EditText foodName;
    private Button buttonSave;
    private Button buttonAdd;
    private EditText foodDescription;
    private ImageView foodImage;
    private FoodHelper helper = null;
    private String foodID = "";
    private static final int CHOOSE_IMAGE = 123;
    private byte[] byteImage;
    private EditText foodCalories;
    private RadioGroup foodTimes;

    /*private TextView location =null;
    private GPSTracker gpsTracker;
    private double latitude =0.0d;
    private double longitude =0.0d;
    private double myLatitude =0.0d;
    private double myLongitude =0.0d;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailform);

        foodRating = findViewById(R.id.food_rating);
        foodName = findViewById(R.id.food_name);
        foodDescription = findViewById(R.id.food_description);
        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(onAdd);
        foodImage = findViewById(R.id.foodImage);
        foodCalories=findViewById(R.id.food_calories);
        foodTimes =findViewById(R.id.food_times);

        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(onSave);
        helper = new FoodHelper(this);


        /*location =findViewById(R.id.location);
        gpsTracker = new GPSTracker(DetailForm.this);*/

        foodID = getIntent().getStringExtra("ID");
        if (foodID != null) {
            load();
        }


    }

    private View.OnClickListener onAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent photos = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(photos, CHOOSE_IMAGE);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE){
            Uri chosenImage = data.getData();
            foodImage.setImageURI(chosenImage);

            //Convert Image to bitmap:
            BitmapDrawable drawable = (BitmapDrawable) foodImage.getDrawable();
            Bitmap ChosenImg = drawable.getBitmap();


            foodImage.setImageBitmap(ChosenImg);
            Bitmap img = ChosenImg;
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, b);
            byteImage = b.toByteArray();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        /*gpsTracker.stopUsingGPS();*/

    }

    private void load() {
        Cursor c = helper.getById(foodID);
        c.moveToFirst();
        foodRating.getRating();
        foodName.setText(helper.getFoodName(c));
        foodDescription.setText(helper.getFoodDescription(c));
        foodCalories.setText(helper.getFoodCalories(c));
        if (helper.getFoodTimes(c).equals("Breakfast")){
            foodTimes.check(R.id.breakfast);
        }else if (helper.getFoodTimes(c).equals("Lunch")){
            foodTimes.check(R.id.lunch);
        }else if (helper.getFoodTimes(c).equals("Dinner")){
            foodTimes.check(R.id.dinner);
        }
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float rating = foodRating.getRating();
            String nameStr = foodName.getText().toString();
            String descStr = foodDescription.getText().toString();
            String caloStr = foodCalories.getText().toString();

            String fooTime = "";
            switch(foodTimes.getCheckedRadioButtonId()){
                case R.id.breakfast:
                    fooTime ="Breakfast";
                    break;
                case R.id.lunch:
                    fooTime ="Lunch";
                    break;
                case R.id.dinner:
                    fooTime ="Dinner";
                    break;
            }

            if (byteImage != null) {
                helper.insert(rating, nameStr, descStr, byteImage, caloStr,fooTime);
            }else if (byteImage == null){
                helper.insertNoImage(rating, nameStr, descStr,caloStr,fooTime);
            }
            Intent intent = new Intent(DetailForm.this, FoodList.class);
            startActivity(intent);
        }
    };


}