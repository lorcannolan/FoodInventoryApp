package dit.ie.foodstuff;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        }

        return view;
    }
}
