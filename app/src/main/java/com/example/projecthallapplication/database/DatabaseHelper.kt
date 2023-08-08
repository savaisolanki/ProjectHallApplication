package com.example.projecthallapplication.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.projecthallapplication.dataclass.User

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_MOBILE_NUMBER = "mobileNumber"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_RECEIVE_EMAIL_SMS = "receive_email_sms"


    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            ("CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_FIRST_NAME TEXT, " +
                    "$COLUMN_LAST_NAME TEXT, " +
                    "$COLUMN_MOBILE_NUMBER TEXT, " +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_PASSWORD TEXT, " +
                    "$COLUMN_RECEIVE_EMAIL_SMS INTEGER)")
        db.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_MOBILE_NUMBER TEXT")
        }
    }


    fun loginUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }


    fun insertSignUpUser(
        firstName: String,
        lastName: String,
        mobileNumber: String,
        email: String,
        password: String,
        receiveEmailSms: Boolean
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_MOBILE_NUMBER, mobileNumber)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_RECEIVE_EMAIL_SMS, receiveEmailSms)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun isMobileNumberExists(mobileNumber: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_MOBILE_NUMBER = ?"
        val cursor = db.rawQuery(query, arrayOf(mobileNumber))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val query = "SELECT * FROM $TABLE_USERS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME))
            val mobileNumber = cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val receiveEmailSms =
                cursor.getInt(cursor.getColumnIndex(COLUMN_RECEIVE_EMAIL_SMS)) == 1

            val user = User(id, firstName, lastName, mobileNumber, email, password, receiveEmailSms)
            userList.add(user)
        }

        cursor.close()

        return userList
    }

    fun deleteUserById(userId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(userId.toString()))
    }







    @SuppressLint("Range")
    fun getCurrentUser(): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_RECEIVE_EMAIL_SMS = ? LIMIT 1"
        val cursor = db.rawQuery(query, arrayOf(1.toString()))

        var currentUser: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME))
            val mobileNumber = cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val receiveEmailSms =
                cursor.getInt(cursor.getColumnIndex(COLUMN_RECEIVE_EMAIL_SMS)) == 1

            currentUser =
                User(id, firstName, lastName, mobileNumber, email, password, receiveEmailSms)
        }

        cursor.close()
        return currentUser
    }


}
