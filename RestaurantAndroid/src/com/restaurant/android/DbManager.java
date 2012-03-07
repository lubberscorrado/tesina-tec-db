package com.restaurant.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DbManager extends SQLiteOpenHelper {
	

	static final String DB_NAME="restaurant.db";
	static final int DB_VERSION=1;
	
	Context context;
	
	public DbManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.d("DbHelper", "Creating DB helper");
		this.context = context;
	}
	
	/* Metodo chiamato se il DB_NAME non esiste */
	@Override
	public void onCreate(SQLiteDatabase database) {
	}
	
	public void dropTables() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("drop table if exists categoria");
		database.execSQL("drop table if exists vocemenu");
		database.execSQL("drop table if exists variazione");
		database.close();
	}
	
	public void createTables() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("create table categoria (idCategoria int, idCategoriaPadre int, nome string, descrizione string)");
		database.execSQL("create table vocemenu (idVoceMenu int, idCategoria int, nome string, descrizione string, prezzo string)");
		database.execSQL("create table variazione (idVariazione int, idCategoria int,  nome string, descrizione string, prezzo string, checked int)");
		database.close();
	}
	
	public void createTablesComande() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("create table if not exists comanda (idComanda integer primary key autoincrement, idRemotoComanda int, idVoceMenu int, idTavolo int, quantita int, note string, stato string)");
		database.execSQL("create table if not exists variazionecomanda (idVariazioneComanda integer primary key autoincrement, idComanda int, idVariazione, unique(idComanda, idVariazione))");
		database.close();
	}
	
	public void dropTablesComande() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("drop table if exists comanda");
		database.execSQL("drop table if exists variazionecomanda");
		database.close();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	
	public void BackUpDbToSD() {
		
		/************************************************************
		 * Back up su scheda SD del database (entrambe le tabelle)
		 ************************************************************/
		
	    try {
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();

	        if (sd.canWrite()) {
	            String currentDBPath = "\\data\\com.restaurant.android\\databases\\restaurant.db";
	            String backupDBPath = "restaurantbackup.db";
	            File currentDB = new File(data, currentDBPath);
	            File backupDB = new File(sd, backupDBPath);

	            if (currentDB.exists()) {
	                FileChannel src = new FileInputStream(currentDB).getChannel();
	                FileChannel dst = new FileOutputStream(backupDB).getChannel();
	                dst.transferFrom(src, 0, src.size());
	                src.close();
	                dst.close();
	            }
	        }
	    } catch (Exception e) {
	    	Log.e("UpdataDatabaseService", "Errore copia DB su SD: " + e.toString());
	    }
	  
	}
	

}
