package dit.ie.foodstuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOutline
{
    private static final String DATABASE_NAME 	= "Foods";

    private static final String TABLE_ITEMS 	= "Items";
    public static final String ITEMS_BARCODE 	    = "_id";
    public static final String ITEMS_NAME = "prod_name";
    public static final String ITEMS_CATEGORY = "prod_category";
    public static final String ITEMS_EX_DATE = "prod_ex_date";
    public static final String ITEMS_QUANTITY = "prod_qty";
    public static final String ITEMS_IMG_URL = "prod_img_url";

    private static final int DATABASE_VERSION 	= 1; // since it is the first version of the dB

    // SQL statement to create the database
    private static final String DATABASE_CREATE =
            "create table " + TABLE_ITEMS + " (_id varchar2 primary key, " +
                    "prod_name text not null, " + "prod_category text not null, " +
                    "prod_ex_date text not null, " + "prod_qty integer not null, " +
                    "prod_img_url text);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    // Constructor
    public DatabaseOutline(Context ctx)
    {
        this.context 	= ctx;
        DBHelper 		= new DatabaseHelper(context);
    }

    public SQLiteDatabase open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return db;
    }

    // nested dB helper class
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        //
        public void onCreate(SQLiteDatabase db)
        {
            // Execute SQL to create your tables (call the execSQL method of the SQLLiteDatabase class, passing in your create table(s) SQL)
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        //
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            // dB structure change..

        }
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertItem(String barcode, String foodName, String foodCategory, String foodExDate, int foodQty, String foodImgUrl)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ITEMS_BARCODE, barcode);
        initialValues.put(ITEMS_NAME, foodName);
        initialValues.put(ITEMS_CATEGORY, foodCategory);
        initialValues.put(ITEMS_EX_DATE, foodExDate);
        initialValues.put(ITEMS_QUANTITY, foodQty);
        initialValues.put(ITEMS_IMG_URL, foodImgUrl);

        return db.insert(TABLE_ITEMS, null, initialValues);
    }

    public String check(String category) throws  SQLException
    {
        String testDB = null;
        Cursor mCursor =   db.query(true, TABLE_ITEMS, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                ITEMS_BARCODE,
                                ITEMS_NAME,
                                ITEMS_CATEGORY,
                                ITEMS_EX_DATE,
                                ITEMS_QUANTITY,
                                ITEMS_IMG_URL
                        },
                ITEMS_CATEGORY + "='" + category + "'",  null, null, null, null, null);

        if (mCursor.moveToFirst())
        {
            testDB = mCursor.getString(mCursor.getColumnIndex(ITEMS_BARCODE));
        }
        mCursor.close();
        return testDB;
    }

    public Cursor getCategoryFood(String category) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, TABLE_ITEMS, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                ITEMS_BARCODE,
                                ITEMS_NAME,
                                ITEMS_CATEGORY,
                                ITEMS_EX_DATE,
                                ITEMS_QUANTITY,
                                ITEMS_IMG_URL
                        },
                ITEMS_CATEGORY + "='" + category + "'",  null, null, null, null, null); //QUOTE WHEN LOOKING FOR STRINGS

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAll(String barcode) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, TABLE_ITEMS, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                ITEMS_BARCODE,
                                ITEMS_NAME,
                                ITEMS_CATEGORY,
                                ITEMS_EX_DATE,
                                ITEMS_QUANTITY,
                                ITEMS_IMG_URL
                        },
                ITEMS_BARCODE + "='" + barcode + "'",  null, null, null, null, null); //QUOTE WHEN LOOKING FOR STRINGS

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void updateQuantity(int qty, String barcode) throws SQLException
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("prod_qty", qty);
        db.update("Items", contentValues, "_id=" + barcode, null);
    }

    public void deleteItem(String barcode)
    {
        db.delete("Items", "_id" + "=" + barcode, null);
    }
}
