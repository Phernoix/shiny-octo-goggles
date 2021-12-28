package sp.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodList extends AppCompatActivity {
    private Cursor model = null;
    private FoodAdapter adapter = null;
    private ListView list;
    private FoodHelper helper = null;
    private TextView empty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        empty = findViewById(R.id.empty);
        helper = new FoodHelper(this);
        list = findViewById(R.id.list);
        model = helper.getAll();
        adapter = new FoodAdapter(this, model, 0);
        list.setOnItemClickListener(onListClick);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model != null) {
            model.close();
        }
        model = helper.getAll();
        if (model.getCount() > 0) {
            empty.setVisibility(View.INVISIBLE);
        }
        adapter.swapCursor(model);
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        invalidateOptionsMenu();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.add):
                Intent intent;
                intent = new Intent(FoodList.this, DetailForm.class);
                startActivity(intent);
                break;
            case (R.id.about):
                intent = new Intent(FoodList.this, About.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            model.moveToPosition(position);
            String recordID = helper.getID(model);
            Intent intent;
            intent = new Intent(FoodList.this, DetailForm.class);
            intent.putExtra("ID", recordID);
            startActivity(intent);
        }
    };

    static class FoodHolder {
        private TextView foodName ;
        private TextView foodDesc ;
        private ImageView foodImage;
        private TextView foodRating;

        FoodHolder(View row) {
            foodRating = row.findViewById(R.id.rating);
            foodName = row.findViewById(R.id.foodName);
            foodDesc = row.findViewById(R.id.foodDesc);
            foodImage = row.findViewById(R.id.imageview);
            foodRating.setText("Test");
        }

        void populateFrom(Cursor c, FoodHelper helper) {
            foodRating.setText("Rating: " + helper.getFoodRating(c) + "stars");
            foodName.setText("Name: " + helper.getFoodName(c));
            foodDesc.setText("Description: " + helper.getFoodDescription(c));

            byte[] ImageByte = helper.getFoodImage(c);
            if (ImageByte != null) {
                Bitmap ImgBitmap = BitmapFactory.decodeByteArray(ImageByte, 0, ImageByte.length);
                foodImage.setImageBitmap(ImgBitmap);
            } else {
                foodImage.setImageResource(R.drawable.no_img);
            }
        }
    }

    class FoodAdapter extends CursorAdapter {
        FoodAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            FoodHolder holder = (FoodHolder) view.getTag();
            holder.populateFrom(cursor, helper);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            FoodHolder holder = new FoodHolder(row);
            row.setTag(holder);
            return (row);
        }
    }
}