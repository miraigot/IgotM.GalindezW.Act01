package com.example.db1;

import java.util.List;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;


public class copyFromPhone extends Activity {
	
	String[][] Contact;
	private int i = 0;
	
	Cursor cursor = getName();
    Cursor phones = getNumber(); 
    Cursor emails = getEmail();
    
    String name;
    String number;
    String email;
    
public copyFromPhone(){
	
	while (cursor.moveToNext() && phones.moveToNext() && emails.moveToNext()) {

	      String name = cursor.getString(cursor
	          .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
	      String number = phones.getString(phones
	          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	      String email = emails.getString(emails
		          .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));      
	      
	  }
}
	
	private Cursor getName() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
            + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

        
        return managedQuery(uri, projection, selection, selectionArgs,
            sortOrder);
    }
    
    @SuppressWarnings("deprecation")
   	private Cursor getNumber() {
           // Run query
           Uri uri =  ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
           String[] projection = new String[] { ContactsContract.Contacts._ID,
        		   ContactsContract.CommonDataKinds.Phone.NUMBER};
           String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
               + ("1") + "'";
           String[] selectionArgs = null;
           String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
           
           return managedQuery(uri, projection, selection, selectionArgs,
               sortOrder);
    }
    
    @SuppressWarnings("deprecation")
   	private Cursor getEmail() {
           // Run query
           Uri uri =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
           String[] projection = new String[] { ContactsContract.Contacts._ID,
        		   ContactsContract.CommonDataKinds.Email.DATA};
           String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
               + ("1") + "'";
           String[] selectionArgs = null;
           String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
           
           return managedQuery(uri, projection, selection, selectionArgs,
               sortOrder);
    }    



}
