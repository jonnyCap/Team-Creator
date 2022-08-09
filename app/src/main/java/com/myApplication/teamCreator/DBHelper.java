package com.myApplication.teamCreator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "TCData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Games(ID INTEGER PRIMARY KEY AUTOINCREMENT,game TEXT)");
        DB.execSQL("create Table Players(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, defaultStrength INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Games");
        DB.execSQL("drop Table if exists Players");
    }

    //Probleme:
    //Diese Funktionen sind redundant und können für games und player die gleichen sein, es muss nur ein parameter für database und table hinzugefügt werden

    //------------------Generell Methods for Database-------------------
    public void addData(String table, String column, String name){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, name);
        DB.insert(table, column, contentValues);
    }
    public void addData(String table, String column,String column2, String name,  int defaultStrength ){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, name);
        contentValues.put(column2, defaultStrength);
        DB.insert(table, null, contentValues);
        assignDefaultStrengthForNewPlayer(name);
    }
    public void deleteData(String table, String column, String name){
        SQLiteDatabase DB = this.getWritableDatabase();
        String sqlString = "SELECT * FROM " + table + " where " + column +  "=?";
        Cursor cursor = DB.rawQuery(sqlString, new String[]{name});
        if(cursor.getCount() > 0){
            DB.delete(table, column + "=?", new String[]{name});
        }
        cursor.close();
    }
    //---------------Player Methods---------------------------------------

    public ArrayList<Player> fetchAllPlayerData(){
        ArrayList<Player> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * FROM Players", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Player(cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return arrayList;
    }
    public ArrayList<Player> DeleteAndFetchAllPlayerData(String table, String column, String name){
        ArrayList<Player> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        deleteData(table, column, name);
        Cursor cursor = DB.rawQuery("Select * FROM Players", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Player(cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return arrayList;
    }
    public ArrayList<Player> AddAndFetchAllPlayerData(String table, String column, String column2, String name, int defaultStrength){
        ArrayList<Player> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        addData(table, column, column2, name, defaultStrength);
        Cursor cursor = DB.rawQuery("Select * FROM Players", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Player(cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return arrayList;
    }

    public void insertColumn(String database, String columnName){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("ALTER TABLE " + database + " ADD " + columnName + " INTEGER DEFAULT 0");
        Cursor cursor = DB.rawQuery("Select * FROM " + database, null, null);
        while(cursor.moveToNext()){
             ContentValues CV = new ContentValues();
             CV.put(columnName, cursor.getInt(2));
             String ID = String.valueOf(cursor.getInt(0));
             DB.update(database, CV, "ID = ?", new String[]{ID});
        }
        cursor.close();
    }

    public void assignDefaultStrengthForNewPlayer(String newPlayer){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * FROM Players WHERE name = '" + newPlayer + "'", null, null);
        String []columnNames = cursor.getColumnNames();
        cursor.moveToFirst();
        ContentValues CV = new ContentValues();
        for(int i = 3;i < columnNames.length; i++) {
            String value = String.valueOf(cursor.getInt(2));
            CV.put(columnNames[i], value);
            DB.update("Players" , CV, "name = ?", new String[]{newPlayer});
        }
        cursor.close();
    }
    //soll dann im Spinner angewendet werden
    public void updateColumn(String columnName, int strength, String playerName){
        String actualColumnName = columnName;
        if(actualColumnName.equals("Default Strength")){
            actualColumnName = "defaultStrength";
        }
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(actualColumnName, strength);
        DB.update("Players", CV, "name = ?", new String[]{playerName});
    }

    public void deleteColumn(String columnName){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("CREATE TABLE Players_backup(ID INTEGER PRIMARY KEY AUTOINCREMENT)");
        Cursor oldCursor = DB.rawQuery("Select * FROM Players", null, null);
        String[] oldColumnNames = oldCursor.getColumnNames();

        //Inserts Columns
        for(int i = 1; i < oldColumnNames.length; i++) {
            if (!oldColumnNames[i].equals(columnName)) {
                insertColumn("Players_backup", oldColumnNames[i]);
            }
        }

        //inserts new Data into Columns
        while(oldCursor.moveToNext()) {
            ContentValues backUpCV = new ContentValues();
            for(int i = 1; i < oldColumnNames.length; i++) {
                if (!oldColumnNames[i].equals(columnName)) {
                    if(oldColumnNames[i].equals("name")){
                        backUpCV.put(oldColumnNames[i], oldCursor.getString(i));
                    }else {
                        backUpCV.put(oldColumnNames[i], oldCursor.getInt(i));
                    }
                }
            }
            DB.insert("Players_backup", null, backUpCV);
        }
        oldCursor.close();

        //Drop old Players Table
        DB.execSQL("DROP TABLE Players");

        //Recreate Table with an identical Copie of the bakcup
        DB.execSQL("CREATE TABLE Players(ID INTEGER PRIMARY KEY AUTOINCREMENT)");
        Cursor newCursor = DB.rawQuery("Select * FROM Players_backup", null);
        String[] newColumnNames = newCursor.getColumnNames();

        for(int i = 1; i < newColumnNames.length; i++) {
                insertColumn("Players", newColumnNames[i]);
        }
        while(newCursor.moveToNext()) {
            ContentValues newCV = new ContentValues();
            for(int i = 1; i < newColumnNames.length; i++) {
                if(newColumnNames[i].equals("name")){
                    newCV.put(newColumnNames[i], newCursor.getString(i));
                }else {
                    newCV.put(newColumnNames[i], newCursor.getInt(i));
                }
            }
            DB.insert("Players", null, newCV);
        }
        newCursor.close();
        //Drop Backup Table
        DB.execSQL("DROP TABLE Players_backup");
    }
    //---------------------------Methods for Game----------------------

    public ArrayList<Game> fetchAllData(){
        ArrayList<Game> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * FROM Games", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Game(cursor.getString(1)));
        }
        cursor.close();
        return arrayList;
    }
    public ArrayList<String> fetchAllDataString(){
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * FROM Games", null, null);
        while(cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }
        cursor.close();
        return arrayList;
    }
    public int returnSpecificStrength(String game, String player){
        String actualGame = game;
        if(actualGame.equals("Default Strength")){
            actualGame = "defaultStrength";
        }
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select " + actualGame + " FROM Players WHERE name=? ", new String[] {player});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    public ArrayList<Game> deleteAndFetchAllData(String table, String column, String game){
        ArrayList<Game> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        deleteData(table, column, game);
        Cursor cursor = DB.rawQuery("Select * FROM Games", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Game(cursor.getString(1)));
        }
        cursor.close();
        deleteColumn(game);
        return arrayList;
    }
    public ArrayList<Game> addAndFetchAllData(String table, String column, String game){
        ArrayList<Game> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        addData(table, column, game);
        Cursor cursor = DB.rawQuery("Select * FROM Games", null, null);
        while(cursor.moveToNext()){
            arrayList.add(new Game(cursor.getString(1)));
        }
        cursor.close();
        insertColumn("Players" , game);
        return arrayList;
    }
}
