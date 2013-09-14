package br.com.hider.valetine.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
	
	@Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE [User] (\n" +
                "  [user_id] INTEGER PRIMARY KEY autoincrement, \n" +
                "  [user_name] [VARCHAR(255)] NOT NULL \n" +
                ");");

        sqLiteDatabase.execSQL(
                "CREATE TABLE [Event] (\n" +
                "  [event_id] INTEGER PRIMARY KEY autoincrement, \n" +
                "  [event_name] [VARCHAR(255)] NOT NULL, \n" +
                "  [event_description] [VARCHAR(1000)]," +
                "  [event_date] DATE NOT NULL," +
                "  [event_user_id] INTEGER CONSTRAINT [event_user_id] REFERENCES [User]([user_id]) );");       

        sqLiteDatabase.execSQL("INSERT INTO [User] ([user_name]) VALUES ('Hider Tanure')");
        sqLiteDatabase.execSQL("INSERT INTO [Event] ([event_name], [event_description], [event_date], [event_user_id]) VALUES ('Construção do App', 'Data que marca o início do romance com o aplicativo.', "+System.currentTimeMillis()+", 1)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
