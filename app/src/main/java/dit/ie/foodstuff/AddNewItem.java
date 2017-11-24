package dit.ie.foodstuff;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CameraPreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNewItem extends Fragment
{
    private static final String DEBUG_TAG = "HttpExample";
    public EditText barcodeInput, nameInput, dateInput, qtyInput;
    public String barcode;
    public String insertBarcode, insertName, insertCategory, insertDate;
    public int insertQty;
    private String url = "https://world.openfoodfacts.org/api/v0/product/";

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

        //insertBarcode = insertName = insertCategory = insertDate = insertQty = "";

        final DatabaseOutline myDatabase = new DatabaseOutline(getContext());

        final Spinner spinner = (Spinner)view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        FloatingActionButton fab, plus, minus, check;

        fab = (FloatingActionButton)view.findViewById(R.id.done);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Details Entered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                insertBarcode = barcodeInput.getText().toString();
                insertName = nameInput.getText().toString();
                insertCategory = spinner.getSelectedItem().toString();
                insertDate = dateInput.getText().toString();
                insertQty = Integer.parseInt(qtyInput.getText().toString());
                if (insertBarcode.equals("") || insertName.equals("") || insertCategory.equals("")
                        || insertDate.equals("") || insertQty == 0)
                {
                    Snackbar.make(view, "Enter ALL Details", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                {
                    myDatabase.open();
                    long num = myDatabase.insertItem(insertBarcode, insertName, insertCategory, insertDate, insertQty);
                    myDatabase.close();
                    if (num == -1)
                    {
                        Snackbar.make(view, "Insert ERROR", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else
                    {
                        Snackbar.make(view, "Insert SUCCESS", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });

        check = (FloatingActionButton)view.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Details Entered", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    myDatabase.open();
                    Cursor c = myDatabase.getMiscellaneous("Miscellaneous");
                    myDatabase.close();
            }
        });

        /*
        Following code taken from https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
         */
        final Calendar myCalendar = Calendar.getInstance();

        dateInput = (EditText)view.findViewById(R.id.enterDate);

        dateInput.setOnClickListener(new View.OnClickListener()
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

                    dateInput.setText(sdf.format(myCalendar.getTime()));
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

        qtyInput = (EditText)view.findViewById(R.id.enterQty);
        qtyInput.setText("0");

        plus = (FloatingActionButton)view.findViewById(R.id.add);
        plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qtyString = qtyInput.getText().toString();
                int val = Integer.parseInt(qtyString);
                val++;
                qtyString = Integer.toString(val);
                qtyInput.setText(qtyString);
            }
        });

        minus = (FloatingActionButton)view.findViewById(R.id.remove);
        minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String qtyString = qtyInput.getText().toString();
                int val = Integer.parseInt(qtyString);
                if(val != 0)
                {
                    val--;
                    qtyString = Integer.toString(val);
                    qtyInput.setText(qtyString);
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
        nameInput = (EditText)view.findViewById(R.id.enterName);

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
                barcode = result.getContents();
                barcodeInput.setText(barcode);
                jsonConnect();
            }
        }
    }

    public void jsonConnect()
    {
        String stringUrl = url + barcode + ".json";
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadWebpageTask().execute(stringUrl);
        }
        else
        {
            Toast.makeText(getActivity(), "Manually input details", Toast.LENGTH_SHORT).show();
        }
    }

    /* Use AsyncTask to create a task away from the main UI thread. This task takes a URL string and uses
    it to create an HttpUrlConnection. Once the connection has been established, the AsyncTask downloads
    the contents of the webpage as an InputStream. Finally, the InputStream is converted into a string,
    which is displayed in the UI by the AsyncTask's onPostExecute method */
    private class DownloadWebpageTask extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        protected void onPostExecute(String result)
        {
            String name = "";
            String status = "";
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status_verbose");
                if (status.equals("product found"))
                {
                    JSONObject productInfo = (JSONObject) jsonObject.get("product");
                    name = productInfo.getString("product_name");
                }
                else
                {
                    Toast.makeText(getActivity(), "Manually Input Details", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }
            nameInput.setText(name);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.

    private String downloadUrl(String myurl) throws IOException
    {
        InputStream is = null;

        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);

            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally
        {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    // Reads an InputStream and converts it to a String.
    private String readIt(InputStream is)
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append('\n');
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}