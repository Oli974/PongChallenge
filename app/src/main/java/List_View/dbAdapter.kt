package List_View


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import Object.*

import java.util.ArrayList

class dbAdapter{


    val DB_VERSION:Int = 1
    val DB_NAME:String = "score_donnees.db"

    val TABLE_SCORE:String = "Table_Score"
    val COL_JOUEUR:String = "Nom_joueur"
    val COL_DATE:String = "Date"
    val COL_SCORE:String = "Score"
    val COL_ID:String= "ID"

    val CREATE_DB:String = "create table "+TABLE_SCORE+" ("+
            COL_ID+" integer primary key autoincrement, "+
            COL_JOUEUR+" text not null,"+
            COL_DATE+" text not null,"+
            COL_SCORE+" integer not null );"

    private var mDB:SQLiteDatabase?=null
    private var mOpenHelper:MyOpenHelper?= null

    constructor(context: Context){
        mOpenHelper = MyOpenHelper(context,DB_NAME,null,DB_VERSION)
    }

    fun open(){
        mDB = mOpenHelper?.writableDatabase
    }

    fun close(){
        mDB?.close()
    }

    fun getScore(id:String): Score? {
        var score:Score?=null

        val strArray:Array<String> = arrayOf(COL_JOUEUR,COL_SCORE,COL_DATE)

        val c:Cursor = mDB?.query(TABLE_SCORE,strArray,COL_JOUEUR+" = "+id,null,
                null,null,null) as Cursor

        if (c.count > 0){
            c.moveToFirst()
            score= Score(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3))
        }

        c.close()

        return score
    }

    fun getAllScore():ArrayList<Score>{
        var scores:ArrayList<Score> = ArrayList()

        val strArray:Array<String> = arrayOf(COL_JOUEUR,COL_SCORE,COL_DATE)

        val c:Cursor = mDB?.query(TABLE_SCORE,strArray,null,null,
                null,null,COL_SCORE) as Cursor

        c.moveToFirst()
        while(!c.isAfterLast){
            scores.add(Score(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3)))
            print(c.getLong(0))
            c.moveToNext()
        }
        c.close()
        return scores
    }


    fun insertScore(nom:String,date:String,score:Int):Long?{
        val values : ContentValues = ContentValues()
        values.put(COL_JOUEUR,nom)
        values.put(COL_DATE,date)
        values.put(COL_SCORE,score)

        print(COL_JOUEUR+" "+ nom)

        return mDB?.insert(TABLE_SCORE,null,values)
    }

    fun removeScore(id: Long): Int?{
        return mDB?.delete(TABLE_SCORE, "$COL_ID = $id", null)
    }

    /**
     * Private class MyOpenHelper : Classe interne à dbAdapter
     */


    private inner class MyOpenHelper(context: Context, name: String,
                           cursorFactory: SQLiteDatabase.CursorFactory?, version: Int)
                        : SQLiteOpenHelper(context, name, cursorFactory, version) {


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("drop table "+ TABLE_SCORE +";")
            onCreate(db)
        }
    }

}