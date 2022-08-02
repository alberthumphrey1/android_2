package com.example.morningfirebasedatabaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var editTextName:EditText ?= null
    var editTextEmail:EditText ?= null
    var editTextIdNumber:EditText ?= null
    var buttonSave:Button ?= null
    var buttonView:Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName = findViewById(R.id.mEditName)
        editTextEmail = findViewById(R.id.mEditEmail)
        editTextIdNumber = findViewById(R.id.mEditIdNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)
        buttonSave!!.setOnClickListener {
            val userName = editTextName!!.text.toString().trim()
            val userEmail = editTextEmail!!.text.toString().trim()
            val userIdNumber = editTextIdNumber!!.text.toString().trim()
            val id = System.currentTimeMillis().toString()
            //Check if the user has submitted empty fields
            if (userName.isEmpty()){
                editTextName!!.setError("Please fill this field")
                editTextName!!.requestFocus()
            }else if (userEmail.isEmpty()){
                editTextEmail!!.setError("Please fill this field")
                editTextEmail!!.requestFocus()
            }else if (userIdNumber.isEmpty()){
                editTextIdNumber!!.setError("Please fill this field")
                editTextIdNumber!!.requestFocus()
            }else{
                //Proceed to save data
                //Start by creating the user object
                val  userData = User(userName, userEmail, userIdNumber,id)
                //Create a reference to the database to store data
                val reference = FirebaseDatabase.getInstance().
                                                getReference().child("User/$id")
                //Start saving userData
                reference.setValue(userData).addOnCompleteListener{
                        task->
                    if (task.isSuccessful){
                        Toast.makeText(applicationContext, "Data saved successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext, "Data saving failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        buttonView!!.setOnClickListener {
            val goToUsers = Intent(applicationContext,UsersActivity::class.java)
            startActivity(goToUsers)
        }
    }
}