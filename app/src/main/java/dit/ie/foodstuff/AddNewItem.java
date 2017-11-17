package dit.ie.foodstuff;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

public class AddNewItem extends Fragment
{
    private TextView name;
    private TableLayout tableLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.add_new_item, container, false);

        tableLayout = (TableLayout)container.findViewById(R.id.tableLayout);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
