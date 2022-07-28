package com.example.morningsqlitedatabaseapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var editTextName:EditText ?= null
    var editTextEmail:EditText ?= null
    var editTextIdNumber:EditText ?= null
    var buttonSave:Button ?= null
    var buttonView:Button ?= null
    var buttonDelete:Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName = findViewById(R.id.mEditName)
        editTextEmail = findViewById(R.id.mEditEmail)
        editTextIdNumber = findViewById(R.id.mEditIdNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)
        buttonDelete = findViewById(R.id.mBtnDelete)

        //create a database
        var db = openOrCreateDatabase("votersDB",Context.MODE_PRIVATE,null)
        //Create atable called users inside the votersDB
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        //set a listener to
        buttonSave!!.setOnClickListener {
            //Receive the data from the user
            var userName = editTextName!!.text.toString()
            var userEmail = editTextEmail!!.text.toString()
            var userIdNumber = editTextIdNumber!!.text.toString()

            //Check if the user is submitting empty fields
            if (userName.isEmpty()||userEmail.isEmpty()||userIdNumber.isEmpty()){
                displayMessage("EMPTY FIELD!!","Please fill all inputs")
            }else{
                //Proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+userName+"', '"+userEmail+"', '"+userIdNumber+"')")
                displayMessage("SUCCESS!!","Data saved successfully")
            }
        }
        buttonView!!.setOnClickListener {
            //use cursor to select all the data from database
            var cursor = db.rawQuery("SELECT * FROM users", null)

            //Check if there is no record in the db
            if (cursor.count == 0){
                displayMessage("NO DATA!!","Sorry the db is empty!!")
            }else{
                //use a string buffer to append all the records for display
                var  buffer = StringBuffer()
                //use a loop to display data per row
                while (cursor.moveToNext()) {
                    buffer.append(cursor.getString(0) + "\n")//column 0 is for Name
                    buffer.append(cursor.getString(1) + "\n")//column 1 is for Email
                    buffer.append(cursor.getString(2) + "\n")//column 2 is for ID No
                }
                displayMessage("DB RECORDS",buffer.toString())
                
            }
            buttonDelete!!.setOnClickListener {
                //Receive the ID number from the user
                var idNumber = editTextIdNumber!!.text.toString().trim()
                //Check if the user is submitting on an empty field
                if(idNumber.isEmpty()){
                    displayMessage("EMPTY FIELD!!","Please Enter ID No!!")
                }else{
                    //use cursor to select user with provided ID
                    var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho = '"+idNumber+"'",null)
                    //Check if there's a user with the provided id
                    if (cursor.count == 0) {
                        displayMessage("NO RECORD!!", "Sorry, no user found!!")
                    }else{
                        db.execSQL("DELETE FROM users WHERE kitambulisho = '"+idNumber+"'")
                        displayMessage("SUCCESS!!","Record deleted successfully!!")
                        clear()
                    }
                }

                }

        }

    }
    fun displayMessage(kichwa:String,ujumbe:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(kichwa)
        alertDialog.setMessage(ujumbe)
        alertDialog.create().show()
    }
    fun clear(){
        editTextName!!.setText("")
        editTextEmail!!.setText("")
        editTextIdNumber!!.setText("")
    }
}