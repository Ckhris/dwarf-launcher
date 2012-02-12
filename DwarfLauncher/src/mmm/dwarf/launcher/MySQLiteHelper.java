package mmm.dwarf.launcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_DWARFS = "dwarfs";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE= "longitude";
	
	private static final String DATABASE_NAME = "dwarfLauncher.db";
	public static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_DWARFS + "( " 
			+ COLUMN_ID +" integer primary key autoincrement, "
			+ COLUMN_LATITUDE + " double not null, "
			+ COLUMN_LONGITUDE + " double not null);";

	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {	
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.v("Chris", "onCreate: "+DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v("Chris", "DROP TABLE");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DWARFS);
		onCreate(db);
	}

}