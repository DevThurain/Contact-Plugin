package com.thurainx.contacts

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.provider.ContactsContract

class ContactsManager() {

    private var activity: Activity? = null

    fun initActivity(myActivity: Activity?) {
        activity = myActivity
    }

    suspend fun fetchContacts() : Map<String,String>{
        val contactMap = emptyMap<String,String>().toMutableMap()
        val contentResolver: ContentResolver = activity!!.contentResolver

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0",
                null,
                "display_name ASC"
            )

        if (cursor != null && cursor.count > 0) {
            var lastHeader: String = ""
            while (cursor.moveToNext()) {
                val id =
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val person =
                    ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        id.toLong()
                    )
                val ring =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val mobileNumber =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val header = name.first().toString()
                contactMap[name.toString()] = mobileNumber
//                if (header != lastHeader) {
//                    lastHeader = header
//                    list.add(
//                        ContactItemViewState(
//                            header,
//                            View.GONE,
//                            View.VISIBLE,
//                            "",
//                            name,
//                            "",
//                            "",
//                            null
//                        )
//                    )
//                }
//
//                list.add(
//                    ContactItemViewState(
//                        header,
//                        View.VISIBLE,
//                        View.GONE,
//                        id,
//                        name,
//                        mobileNumber,
//                        title,
//                        person
//                    )
//                )
            }
            cursor.close()
        }
        return contactMap
    }
}