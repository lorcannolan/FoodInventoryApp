package dit.ie.foodstuff;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowItem extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item_info);

        String url;

        ImageView img = (ImageView)findViewById(R.id.itemImg);
        TextView bCode = (TextView)findViewById(R.id.showBarcode);
        TextView name = (TextView)findViewById(R.id.showName);
        TextView category = (TextView)findViewById(R.id.showCategory);
        TextView expiry = (TextView)findViewById(R.id.showExpriation);
        TextView quantity = (TextView)findViewById(R.id.showQuantity);

        Intent i = getIntent();

        String barcode = i.getStringExtra("barcode");

        DatabaseOutline myDatabase = new DatabaseOutline(this);
        myDatabase.open();
        Cursor mCursor = myDatabase.getAll(barcode);

        bCode.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_BARCODE)));
        name.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_NAME)));
        category.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_CATEGORY)));
        expiry.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_EX_DATE)));
        quantity.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_QUANTITY)));
        url = mCursor.getString(mCursor.getColumnIndexOrThrow(myDatabase.ITEMS_IMG_URL));

        if (url.equals(""))
        {
            img.setImageResource(R.drawable.not_found);
        }
        else
        {
            new ImageLoadTask(url, img).execute();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
    }
}
