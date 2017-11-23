package dit.ie.foodstuff;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CameraPreview;

public class AddNewItem extends Fragment
{
    public EditText barcodeInput;
    public String barcode;

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

        Spinner spinner = (Spinner)view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        FloatingActionButton fab, plus, minus;

        fab = (FloatingActionButton)view.findViewById(R.id.done);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Details Entered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        Following code taken from https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
         */
        final Calendar myCalendar = Calendar.getInstance();

        final EditText enterDate = (EditText)view.findViewById(R.id.enterDate);

        enterDate.setOnClickListener(new View.OnClickListener()
        {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                    enterDate.setText(sdf.format(myCalendar.getTime()));
                }

            };

            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText qty = (EditText)view.findViewById(R.id.enterQty);
        qty.setText("0");

        plus = (FloatingActionButton)view.findViewById(R.id.add);
        plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qtyString = qty.getText().toString();
                int val = Integer.parseInt(qtyString);
                val++;
                qtyString = Integer.toString(val);
                qty.setText(qtyString);
            }
        });

        minus = (FloatingActionButton)view.findViewById(R.id.remove);
        minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qtyString = qty.getText().toString();
                int val = Integer.parseInt(qtyString);
                if(val != 0)
                {
                    val--;
                    qtyString = Integer.toString(val);
                    qty.setText(qtyString);
                }
            }
        });

        //Ensures permissions are set for camera
        final int MY_CAMERA_REQUEST_CODE = 100;

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        barcodeInput = (EditText)view.findViewById(R.id.enterBarcode);

        Button scan = (Button)view.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    scanFromFragment();
                }
            }
        });

        return view;
    }

    public void scanFromFragment()
    {
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan Something").initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "Scanned " + result.getContents(), Toast.LENGTH_SHORT).show();
        if(result != null)
        {
            if(result.getContents() == null)
            {
                barcodeInput.setText("0");
            }
            else
            {
                barcodeInput.setText(result.getContents());
            }
        }
    }
}