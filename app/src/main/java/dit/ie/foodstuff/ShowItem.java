package dit.ie.foodstuff;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowItem extends AppCompatActivity
{
    TextView bCode, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item_info);

        String url;

        ImageView img = (ImageView)findViewById(R.id.itemImg);
        bCode = (TextView)findViewById(R.id.showBarcode);
        TextView name = (TextView)findViewById(R.id.showName);
        TextView category = (TextView)findViewById(R.id.showCategory);
        TextView expiry = (TextView)findViewById(R.id.showExpriation);
        quantity = (TextView)findViewById(R.id.showQuantity);

        FloatingActionButton remove = (FloatingActionButton)findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qty = quantity.getText().toString();
                int qtyInt = Integer.parseInt(qty);
                if (qtyInt != 0)
                {
                    qtyInt--;
                    qty = Integer.toString(qtyInt);
                    quantity.setText(qty);
                }
            }
        });

        FloatingActionButton add = (FloatingActionButton)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qty = quantity.getText().toString();
                int qtyInt = Integer.parseInt(qty);
                qtyInt++;
                qty = Integer.toString(qtyInt);
                quantity.setText(qty);
            }
        });

        Intent i = getIntent();

        String barcode = i.getStringExtra("barcode");

        DatabaseOutline myDatabase = new DatabaseOutline(this);
        myDatabase.open();
        Cursor mCursor = myDatabase.getAll(barcode);
        myDatabase.close();

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
        DatabaseOutline myDatabase = new DatabaseOutline(this);
        myDatabase.open();
        myDatabase.updateQuantity(Integer.parseInt(quantity.getText().toString()), bCode.getText().toString());
        if (Integer.parseInt(quantity.getText().toString()) == 0)
        {
            myDatabase.deleteItem(bCode.getText().toString());
        }
        myDatabase.close();
        super.onBackPressed();
        this.finish();
    }
}
