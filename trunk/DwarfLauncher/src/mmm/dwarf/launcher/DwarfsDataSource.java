package mmm.dwarf.launcher;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;
import android.util.Log;

//Requetes Ajout/insert/suppression
public class DwarfsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_LATITUDE, MySQLiteHelper.COLUMN_LONGITUDE };

	public DwarfsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context, MySQLiteHelper.TABLE_DWARFS, null, MySQLiteHelper.DATABASE_VERSION);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public int createDwarf(Dwarf dwarf) {
		//Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ� � une cl� (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(MySQLiteHelper.COLUMN_LATITUDE, dwarf.getLatitude());
		values.put(MySQLiteHelper.COLUMN_LONGITUDE, dwarf.getLongitude());
		//on ins�re l'objet dans la BDD via le ContentValues
		return (int) database.insert(MySQLiteHelper.TABLE_DWARFS, null, values);
	}

	public long updateDwarf(Dwarf dwarf){
		ContentValues args = new ContentValues();
		args.put(MySQLiteHelper.COLUMN_LATITUDE, dwarf.getLatitude());
		args.put(MySQLiteHelper.COLUMN_LONGITUDE, dwarf.getLongitude());
		//on ins�re l'objet dans la BDD via le ContentValues
		return database.update(MySQLiteHelper.TABLE_DWARFS, args, "_id="+dwarf.getId(), null);
	}

	public void deleteDwarf(Dwarf dwarf) {
		long id = dwarf.getId();
		System.out.println("Dwarf deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_DWARFS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public Dwarf getDwarf(long id){
		   Cursor c=database.rawQuery("SELECT * FROM "+MySQLiteHelper.TABLE_DWARFS+" WHERE "+MySQLiteHelper.COLUMN_ID+"=?", new String []{id+""});
		   c.moveToFirst();
		   return cursorToDwarf(c);  
	}

	public List<Dwarf> getAllDwarfs() {
		List<Dwarf> dwarfs = new ArrayList<Dwarf>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DWARFS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Dwarf dwarf = cursorToDwarf(cursor);
			dwarfs.add(dwarf);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return dwarfs;
	}

	private Dwarf cursorToDwarf(Cursor cursor) {
		Dwarf dwarf = new Dwarf();
		dwarf.setId(cursor.getLong(0));
		dwarf.setLatitude(cursor.getDouble(1));
		dwarf.setLongitude(cursor.getDouble(2));
		Log.v("Chris", "dwarf: "+dwarf);
		return dwarf;
	}
}
