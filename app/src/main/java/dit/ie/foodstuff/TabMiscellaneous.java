package dit.ie.foodstuff;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.database.Cursor.FIELD_TYPE_INTEGER;

public class TabMiscellaneous extends Fragment
{

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.show_food_miscellaneous, container, false);

        FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.insert);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, new AddNewItem()).commit();
            }
        });

        DatabaseOutline myDatabase = new DatabaseOutline(getContext());

        String setTextView = "";

        myDatabase.open();
        String emptydbCheck = myDatabase.check("Miscellaneous");
        if (emptydbCheck == null)
        {
            setTextView = "No miscellaneous food to show!!!";
            Toast.makeText(getContext(), setTextView, Toast.LENGTH_LONG).show();
        }
        else
        {
            Cursor c = myDatabase.getCategoryFood("Miscellaneous");
            myDatabase.close();

            ListView list = (ListView)view.findViewById(R.id.list);
            ListAdapter adapter = new MyCursorAdapter(getContext(), c, 0);
            list.setAdapter(adapter);
            /*
            c.moveToFirst();
            if (c != null)
            {
                do
                {
                    for (int i = 0; i < c.getColumnCount(); i++)
                    {
                        setTextView += c.getString(i) + " ";
                    }
                } while (c.moveToNext());
            }*/
        }
        //textView.setText(setTextView);

        return view;
    }

    public class MyCursorAdapter extends CursorAdapter
    {
        private LayoutInflater cursorInflater;
        private Context mContext;
        DatabaseOutline myDatabase = new DatabaseOutline(getContext());

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
            cursor.moveToFirst();
            do
            {
                TextView title = (TextView)v.findViewById(R.id.displayTitle);
                title.setText(cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_NAME)));
                TextView qty = (TextView)v.findViewById(R.id.displayQty);
                qty.setText(cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_QUANTITY)));
            } while (cursor.moveToNext());
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            // R.layout.row is xml layout for each row
            View v = cursorInflater.inflate(R.layout.row, parent, false);
            return v;
        }
    }
}
