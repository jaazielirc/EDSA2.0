package com.jaax.edsa.controlador

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jaax.edsa.R
import com.jaax.edsa.modelo.Cuenta
import com.jaax.edsa.modelo.DBHelper
import com.jaax.edsa.vista.VerCuentas

class DeleteCuentaFragment(
    private val ID: String,
    private val user: String,
    private val type: String ): DialogFragment() {

    private lateinit var db: DBHelper
    private lateinit var toast: Toast
    private lateinit var cuenta: Cuenta

    @SuppressLint("ShowToast")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        db = DBHelper(activity!!.applicationContext, DBHelper.nombreDB, null, DBHelper.version)
        toast = Toast.makeText(activity!!.applicationContext, "txt", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
        cuenta = Cuenta(this.ID, this.user, "", this.type)

        val builder = AlertDialog.Builder(activity)
        builder
            .setMessage("¿Eliminar ${cuenta.usuario}?")
            .setIcon(R.drawable.baseline_delete_black_18dp)
            .setPositiveButton("Eliminar") { _, _ ->
                val del = eliminarCuenta(cuenta)
                if( del != 0 ){
                    toast.setText("Cuenta eliminada")
                    toast.show()
                } else {
                    toast.setText("Error al eliminar\nIntenta nuevamente")
                    toast.show()
                }
            }
            .setNegativeButton("Cancelar") {_, _ -> dismiss() }

        return builder.create()
    }

    private fun eliminarCuenta(delCuenta: Cuenta): Int {
        val cursor = db.getDatosCuentaById(this.ID, this.type) //debe haber sólo 1 email si existe
        var eliminar = 0
        try {
            if(cursor.count>0){
                if( cursor.count>0 ){
                    eliminar = db.delCuenta(delCuenta.ID, delCuenta.tipo)
                    return eliminar
                } else {
                    toast.setText("Esa cuenta no existe (?")
                    toast.show()
                }
            }
        }catch(sql: SQLiteException){sql.printStackTrace()}
        return eliminar //== 0 -> no se pudo eliminar
    }

    override fun onDestroy() {
        super.onDestroy()
        VerCuentas(this.ID).show(
            this@DeleteCuentaFragment.activity!!.supportFragmentManager, "delCuenta"
        )
    }
}