package dit.ie.foodstuff;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        DatabaseOutline myDatabase = new DatabaseOutline(getContext());

        myDatabase.open();
        String emptydbCheck = myDatabase.check("Fridge Freezer");
        if (emptydbCheck == null)
        {
            setTextView = "No fridge food to show!!!";
            Snackbar.make(view, setTextView, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
        else
        {
            Cursor c = myDatabase.getCategoryFood("Fridge Freezer");
            myDatabase.close();

            ListView list = (ListView)view.findViewById(R.id.listFridge);
            ListAdapter adapter = new MyCursorAdapter(getContext(), c, 0);
            list.setAdapter(adapter);
        }

        return view;
    }
}
