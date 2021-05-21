package com.example.proyecto_2bim

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.proyecto_2bim.DTO.UserDTO
import com.example.proyecto_2bim.Data.AuthUser
import com.example.proyecto_2bim.Data.UserFIrebase
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Authentication : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    val CODIGO_SESION=102

    override fun onCreate(savedInstanceState: Bundle?) {
        val instaciaAuth=FirebaseAuth.getInstance()
        if(instaciaAuth.currentUser!=null){
            setFirebaseUser()
            Toast.makeText(this, "Bienvenido ${instaciaAuth.currentUser.email}", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            Toast.makeText(this, "Ingrese para usar el Aplicativo", Toast.LENGTH_SHORT).show()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        val btnIngresar=findViewById<Button>(R.id.btn_ingresar)
        btnIngresar.setOnClickListener {
            ingresarApp()
        }


    }


    @SuppressLint("ResourceType")
    fun ingresarApp(

    ){
        val providers= arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.ThemeOverlay_AppCompat_Dark)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            CODIGO_SESION
        )
    }

    fun salirApp(){
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Cerrado de sesioin", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    when(requestCode){
            CODIGO_SESION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val User = IdpResponse.fromResultIntent(data)
                    if(User?.email!=null){
                        registerUser(User.email.toString())
                    }
                    finish()
                    Toast.makeText(this, "Bienvenido ${User?.email}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Ingreso no Realizado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setFirebaseUser(){
        val instanciaAuth=FirebaseAuth.getInstance()
        val userLocal=instanciaAuth.currentUser
        Firebase.firestore.collection("users").document(userLocal.email).get()
            .addOnSuccessListener {
                Log.i("fb-a","USER: ${it.data}")
                val userInfo=it.toObject(UserDTO::class.java)

                if(userLocal!=null &&userInfo!=null){
                    if(userLocal.email!=null){
                        val userFirebase=UserFIrebase(
                            userLocal.uid,
                            userLocal.email,
                            userInfo.nickname
                            )
                        AuthUser.user=userFirebase
                    }
                }
            }
            .addOnFailureListener{

            }


    }

    fun registerUser(emailStr:String){
        val userReg= hashMapOf<String,Any?>(
            "email" to emailStr,
            "nickname" to "_"
        )
        val db=Firebase.firestore
        db.collection("users")
            .document(emailStr)
            .set(userReg)
            .addOnSuccessListener {
                Toast.makeText(this, "New User Registered", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "New User Not Registered", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener {
                setFirebaseUser()
            }
    }

    }
