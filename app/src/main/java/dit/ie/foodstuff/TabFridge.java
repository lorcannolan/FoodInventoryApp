package dit.ie.foodstuff;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TabFridge extends Fragment
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
        View view = inflater.inflate(R.layout.show_food_fridge, container, false);

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
        String emptydbCheck = myDatabase.check("Fridge Freezer");

        TextView empty = (TextView)view.findViewById(R.id.empty);
        ListView list = (ListView)view.findViewById(R.id.listFridge);

        if (emptydbCheck == null)
        {
            setTextView = "No fridge food to show!!!";
            empty.setText(setTextView);
            list.setEmptyView(empty);
        }
        else
        {
            Cursor c = myDatabase.getCategoryFood("Fridge Freezer");
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
                    ShowFood showFood = new ShowFood();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, showFood);
                    fragmentTransaction.commit();
                }
            });
        }

        return view;
    }
}
