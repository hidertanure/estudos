package br.com.hider.onlyone.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidertanure on 03/06/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE [Author] (\n" +
                "  [author_id] INTEGER PRIMARY KEY autoincrement, \n" +
                "  [author_displayName] [VARCHAR(255)], \n" +
                "  [author_externalId] [VARCHAR(255)], \n" +
                "  [author_author_id] INTEGER CONSTRAINT [author_author_id] REFERENCES [Author]([author_id]), \n" +
                "  [author_icon] BINARY);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE [Chat] (\n" +
                "  [chat_id] INTEGER PRIMARY KEY autoincrement, \n" +
                "  [chat_icon] BINARY, \n" +
                "  [chat_apptype_id] INTEGER NOT NULL," +
                "  [chat_title] [VARCHAR(255)]);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE [Message] (\n" +
                "  [message_id] INTEGER PRIMARY KEY autoincrement, \n" +
                "  [message_chat_id] INTEGER NOT NULL CONSTRAINT [message_chat_id] REFERENCES [Chat]([chat_id]), \n" +
                "  [message_author_id] INTEGER NOT NULL, \n" +
                "  [message_timestamp] DATE NOT NULL, \n" +
                "  [message_content] NTEXT);");

        sqLiteDatabase.execSQL("INSERT INTO [Chat] ([chat_apptype_id], [chat_title]) VALUES (1, 'Teste Chat Whatsapp')");
        sqLiteDatabase.execSQL("INSERT INTO [Author] ([author_displayName], [author_externalId]) VALUES ('Hider', '+552799799289')");
        sqLiteDatabase.execSQL("INSERT INTO [Message] ([message_chat_id], [message_author_id], [message_timestamp], [message_content]) VALUES (1, 1, "+System.currentTimeMillis()+",'First Message')");
        sqLiteDatabase.execSQL("INSERT INTO [Message] ([message_chat_id], [message_author_id], [message_timestamp], [message_content]) VALUES (1, 1, "+(System.currentTimeMillis()+1)+",'Second Message')");

        sqLiteDatabase.execSQL("INSERT INTO [Chat] ([chat_apptype_id], [chat_title]) VALUES (2, 'Teste Chat Hangouts')");
        sqLiteDatabase.execSQL("INSERT INTO [Author] ([author_displayName], [author_externalId]) VALUES ('Hider Tanure', 'hidertanure@gmail.com')");
        sqLiteDatabase.execSQL("INSERT INTO [Message] ([message_chat_id], [message_author_id], [message_timestamp], [message_content]) VALUES (2, 2, "+System.currentTimeMillis()+",'First Message')");
        sqLiteDatabase.execSQL("INSERT INTO [Message] ([message_chat_id], [message_author_id], [message_timestamp], [message_content]) VALUES (2, 2, "+(System.currentTimeMillis()+1)+",'Second Message com muitos caracteres e fim.')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
