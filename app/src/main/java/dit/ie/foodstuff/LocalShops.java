package dit.ie.foodstuff;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class LocalShops extends Fragment
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
        View view = inflater.inflate(R.layout.local_shops, container, false);

        ImageButton sValu, tesco, dStores, lidl, aldi;

        sValu = (ImageButton)view.findViewById(R.id.superValu);
        sValu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "SuperValu selected", Toast.LENGTH_SHORT).show();
            }
        });

        tesco = (ImageButton)view.findViewById(R.id.tesco);
        tesco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Tesco selected", Toast.LENGTH_SHORT).show();
            }
        });

        dStores = (ImageButton)view.findViewById(R.id.dunnes);
        dStores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Dunnes Stores selected", Toast.LENGTH_SHORT).show();
            }
        });

        lidl = (ImageButton)view.findViewById(R.id.lidl);
        lidl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Lidl selected", Toast.LENGTH_SHORT).show();
            }
        });

        aldi = (ImageButton)view.findViewById(R.id.aldi);
        aldi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Aldi selected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}