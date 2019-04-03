package List_View


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import Object.*
import android.content.ContentUris

import java.util.ArrayList

class dbAdapter{


    val DB_VERSION:Int = 1
    val DB_NAME:String = "scores.db"

    val TABLE_SCORE:String = "TableScore"
    val COL_JOUEUR:String = "nomJoueur"
    val COL_DATE:String = "Date"
    val COL_SCORE:String = "Score"
    val COL_ID:String= "ID"

    val CREATE_DB:String = "CREATE TABLE "+TABLE_SCORE+" ("+
            COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_JOUEUR+" TEXT NOT NULL,"+
            COL_DATE+" TEXT NOT NULL,"+
            COL_SCORE+" INTEGER NOT NULL);"

    private var mDB:SQLiteDatabase?=null
    private var mOpenHelper:MyOpenHelper?= null

    constructor(context: Context){
        mOpenHelper = MyOpenHelper(context,DB_NAME,null,DB_VERSION)
    }

    /**
     * Fonctions d'ouverture et de fermeture de la BD
     * => OK
     */
    fun open(){
        mDB = mOpenHelper?.writableDatabase
        if(mDB?.isOpen as Boolean) println("----------- BASE DE DONNEES : OPEN  -------------")
    }
    fun close(){
        mDB?.close()
    }

    /**
     * Récupère l'entrée n° id dans le tableau des scores
     */
    fun getScore(id:Long): Score? {
        var score:Score?=null

        val strArray:Array<String> = arrayOf(COL_ID,COL_JOUEUR,COL_SCORE,COL_DATE)

        val c:Cursor = mDB?.query(TABLE_SCORE,strArray,"$COL_ID =  $id",null,
                null,null,null) as Cursor

        if (c.count > 0){
            c.moveToFirst()
            score= Score(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3))
        }

        c.close()

        return score
    }

    /**
     * Récupère une ArrayList contenant tout les scores stockées dans la BD
     */
    fun getAllScore():ArrayList<Score>{
        val scores:ArrayList<Score> = ArrayList()

        /*
        Selectionne toutes les entrées dans la BD et les tris par ordre décroissant
         */
        val c:Cursor = mDB?.rawQuery("SELECT * FROM $TABLE_SCORE ORDER BY $COL_SCORE DESC",null)  as Cursor

        println("-- Recupération des scores --")

        if(!c.isBeforeFirst) c.move(-1)
        while(c.moveToNext()){

            /*
            Ajout d'un nouveau score au tableau
                Format : id + nomJoueur + Score + Date
             */

            scores.add(Score(id = c.getLong(0),nom = c.getString(1),
                    score = c.getInt(3),date = c.getString(2)))

            println("Récupération :  ${c.getString(1)} ${c.getString(2)} ${c.getString(3)} ")
        }
        c.close()
        return scores
    }

    /**
     * Insert un nouveau Score dans le tableau
     * => OK
     */
    fun insertScore(nom:String,date:String,score:Int):Long?{
        val values = ContentValues()
        values.put(COL_JOUEUR,nom)
        values.put(COL_DATE,date)
        values.put(COL_SCORE,score)

        println("Insertion dans la table : ${values.get(COL_JOUEUR)}")

        return mDB?.insert(TABLE_SCORE,null,values)
    }

    /**
     * Supprime une entrée du tableau des scores
     * => OK
     */
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
            db.execSQL("DROP TABLE IF EXISTS"+ TABLE_SCORE +";")
            onCreate(db)
        }
    }

}