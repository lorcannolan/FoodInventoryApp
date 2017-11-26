package dit.ie.foodstuff;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter
{
    private LayoutInflater cursorInflater;
    private Context mContext;
    DatabaseOutline myDatabase = new DatabaseOutline(mContext);

    //Constructor
    public MyCursorAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
        mContext = context;
        cursorInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor)
    {
        String imgUrl = cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_IMG_URL));
        if (imgUrl.equals(""))
        {
            ImageView img = (ImageView)v.findViewById(R.id.displayFood);
            img.setImageResource(R.drawable.not_found);
        }
        else
        {
            new ImageLoadTask(imgUrl, (ImageView)v.findViewById(R.id.displayFood)).execute();
        }
        TextView title = (TextView)v.findViewById(R.id.displayTitle);
        title.setText(cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_NAME)));
        TextView qty = (TextView)v.findViewById(R.id.displayQty);
        qty.setText(cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_QUANTITY)));
        TextView exp = (TextView)v.findViewById(R.id.displayEx);
        exp.setText(cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_EX_DATE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        // R.layout.row is xml layout for each row
        View v = cursorInflater.inflate(R.layout.row, parent, false);
        return v;
    }

}
