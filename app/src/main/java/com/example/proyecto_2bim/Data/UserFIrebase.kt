package com.example.proyecto_2bim.Data

class UserFIrebase(
    val uid:String,
    val email:String,
    val nickname:String
) {

    override fun toString(): String {
        return "id: ${uid}; email:${email}; nickname: ${nickname}"
    }
}