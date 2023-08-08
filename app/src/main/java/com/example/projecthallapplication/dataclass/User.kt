package com.example.projecthallapplication.dataclass

import android.os.Parcel
import android.os.Parcelable


/*
* Define Dataclass store value via dataclass
* */
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val email: String,
    val password: String,
    val receiveEmailSms: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(mobileNumber)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeInt(if (receiveEmailSms) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
