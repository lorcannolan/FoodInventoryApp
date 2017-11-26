package dit.ie.foodstuff;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class TabFruitVeg extends Fragment
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
        View view = inflater.inflate(R.layout.show_food_fruit_veg, container, false);

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

        String setTextView = "";

        final DatabaseOutline myDatabase = new DatabaseOutline(getContext());

        myDatabase.open();
        String emptydbCheck = myDatabase.check("Fruit & Veg");

        TextView empty = (TextView)view.findViewById(R.id.empty);
        ListView list = (ListView)view.findViewById(R.id.listFandF);

        if (emptydbCheck == null)
        {
            setTextView = "No fruit and veg food to show!!!";
            empty.setText(setTextView);
            list.setEmptyView(empty);
        }
        else
        {
            Cursor c = myDatabase.getCategoryFood("Fruit & Veg");
            myDatabase.close();

            ListAdapter adapter = new MyCursorAdapter(getContext(), c, 0);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                    Intent i = new Intent(getContext(), ShowItem.class);
                    i.putExtra("barcode", cursor.getString(cursor.getColumnIndexOrThrow(myDatabase.ITEMS_BARCODE)));
                    startActivity(i);
                }
            });
        }

        return view;
    }
}
