package com.jaax.edsa.Modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    nombre: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
):
    SQLiteOpenHelper(context, nombre, factory, version) {

    private val db: SQLiteDatabase
    private val cv: ContentValues
    private var resultInsertar = 0L
    private var resultActualizar = 0
    //DB y TABLAS
    private val nombreBD = "EDSA.db"
    private val tablaUsuario = "USUARIOS"
    private val tablaEmail = "EMAILS"
    private val tablaCuenta = "CUENTAS"

    //columnas de c/tabla
    private val usuarioColumna1 = "NOMBRE"
    private val usuarioColumna2 = "PSSWRD"

    private val emailColumna1 = "ID"
    private val emailColumna2 = "NOMBRE"
    private val emailColumna3 = "PSSWRD"

    private val cuentaColumna1 = "ID"
    private val cuentaColumna2 = "NOMBRE"
    private val cuentaColumna3 = "PSSWRD"
    private val cuentaColumna4 = "TIPO"

    init {
        db = this.writableDatabase
        cv = ContentValues()
    }

    override fun onCreate(sqldb: SQLiteDatabase?) {
        sqldb!!.execSQL(
            "CREATE TABLE $tablaUsuario " +
                    "($usuarioColumna1 TEXT NOT NULL PRIMARY KEY" +
                    " $usuarioColumna2 TEXT NOT NULL)"
        )

        sqldb.execSQL(
            "CREATE TABLE $tablaEmail " +
                    "($emailColumna1 TEXT NOT NULL PRIMARY KEY" +
                    " $emailColumna2 TEXT NOT NULL," +
                    " $emailColumna3 TEXT NOT NULL)"
        )

        sqldb.execSQL(
            "CREATE TABLE $tablaEmail " +
                    "($cuentaColumna1 TEXT NOT NULL PRIMARY KEY" +
                    " $cuentaColumna2 TEXT," +
                    " $cuentaColumna3 TEXT," +
                    " $cuentaColumna4 TEXT NOT NULL)"
        )
    }
    override fun onUpgrade(sqldb: SQLiteDatabase?, p1: Int, p2: Int) {
        sqldb!!.execSQL("DROP TABLE IF EXISTS $tablaUsuario")
        sqldb.execSQL("DROP TABLE IF EXISTS $tablaEmail")
        sqldb.execSQL("DROP TABLE IF EXISTS $tablaCuenta")
    }

    //--------- INSERTAR ---------//
    fun insertarUsuario(nombre: String, psswrd: String): Boolean {
        cv.put(usuarioColumna1, nombre) //ID
        cv.put(usuarioColumna2, psswrd)

        resultInsertar = db.insert(tablaUsuario, null, cv)
        db.close()
        return resultInsertar != -1L
    }

    fun insertarEmail(id: String, nombre: String, psswrd: String): Boolean {
        cv.put(emailColumna1, id) //debe ser el nombre de usuario
        cv.put(emailColumna2, nombre)
        cv.put(emailColumna3, psswrd)

        resultInsertar = db.insert(tablaEmail, null, cv)
        db.close()
        return resultInsertar != -1L
    }

    fun insertarCuenta(id: String, nombre: String, psswrd: String, tipo: String): Boolean {
        cv.put(cuentaColumna1, id)
        cv.put(cuentaColumna2, nombre)
        cv.put(cuentaColumna3, psswrd)
        cv.put(cuentaColumna4, tipo)

        resultInsertar = db.insert(tablaCuenta, null, cv)
        db.close()
        return resultInsertar != -1L
    }

    //--------- LEER ---------//
    fun getAllUsuarios(): Cursor {
        return db.rawQuery("SELECT * FROM $tablaUsuario", null)
    }

    fun getEmailsByID(ID: String): Cursor {
        return db.rawQuery("SELECT * FROM $tablaEmail WHERE $emailColumna1 =?", arrayOf(ID))
    }
    fun getDatosEmailById(nombre: String): Cursor {
        return db.rawQuery("SELECT * FROM $tablaEmail WHERE $emailColumna2 =?", arrayOf(nombre))
    }

    fun getCuentasById(ID: String): Cursor {
        return db.rawQuery("SELECT * FROM $tablaCuenta WHERE $cuentaColumna1 =?", arrayOf(ID))
    }
    fun getDatosSingleCuentaById(idEmail: String, idCuenta: String): Cursor {
        return db.rawQuery(
            "SELECT * FROM $tablaCuenta WHERE $cuentaColumna1 =? AND $cuentaColumna2 =?", arrayOf(idEmail, idCuenta)
        )
    }

    //--------- ACTUALIZAR ---------//
    fun updtDatosUsuario(
        nombreActual: String,
        nombreNuevo: String,
        psswrdActual: String,
        psswrdNuevo: String ): Boolean {
        cv.put(usuarioColumna1, nombreNuevo)
        cv.put(usuarioColumna2, psswrdNuevo)

        resultActualizar = db.update(
            tablaUsuario, cv, "$usuarioColumna1 = $nombreActual AND $usuarioColumna2 = $psswrdActual", null
        )
        return resultActualizar > 0
    }

    fun updtDatosEmail(
        id: String,
        nombreActual: String,
        nombreNuevo: String,
        psswrdActual: String,
        psswrdNuevo: String
    ): Boolean {
        cv.put(emailColumna2, nombreNuevo)
        cv.put(emailColumna3, psswrdNuevo)

        resultActualizar = db.update(
            tablaEmail, cv,
            "$emailColumna1 = $id AND $emailColumna2 = $nombreActual AND $emailColumna3 = $psswrdActual",
            null
        )
        return resultActualizar > 0
    }

    fun updtDatosCuenta(
        id: String,
        nombreActual: String,
        nombreNuevo: String,
        psswrdActual: String,
        psswrdNuevo: String,
        tipoActual: String,
        tipoNuevo: String
    ): Boolean {
        cv.put(cuentaColumna2, nombreNuevo)
        cv.put(cuentaColumna3, psswrdNuevo)
        cv.put(cuentaColumna4, tipoNuevo)

        resultActualizar = db.update(
            tablaCuenta, cv,
            "$cuentaColumna1 = $id AND $cuentaColumna2 = $nombreActual AND $cuentaColumna3 = $psswrdActual AND $cuentaColumna4 = $tipoActual",
            null
        )
        return resultActualizar > 0
    }

    //--------- ELIMINAR ---------//
    fun delUsuario(nombre: String, psswrd: String): Int{
        return db.delete(tablaUsuario, "$usuarioColumna1 = $nombre AND $usuarioColumna2 = $psswrd", null)
    }
    fun delEmail(id: String, nombre: String): Int{
        return db.delete(tablaEmail, "$emailColumna1 = $id AND $emailColumna2 = $nombre", null)
    }
    fun delCuenta( id: String, nombre: String ): Int{
        return db.delete(tablaCuenta, "$cuentaColumna1 = $id AND $cuentaColumna2 = $nombre", null)
    }
}