package dit.ie.foodstuff;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.database.Cursor.FIELD_TYPE_INTEGER;

public class TabMiscellaneous extends Fragment
{
    TextView textView;

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

        textView = (TextView)view.findViewById(R.id.misc);

        String setTextView = "";

        DatabaseOutline myDatabase = new DatabaseOutline(getContext());

        myDatabase.open();
        String emptydbCheck = myDatabase.check("Miscellaneous");
        if (emptydbCheck == null)
        {
            setTextView = "No miscellaneous food to show!!!";
        }
        else
        {
            Cursor c = myDatabase.getCategoryFood("Miscellaneous");
            myDatabase.close();

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
            }
        }
        textView.setText(setTextView);

        return view;
    }


}
