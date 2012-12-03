package com.example.db1;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;


@SuppressLint("ShowToast")
public class MainActivity extends Activity {
/** Called when the activity is first created. */
public String data_name;
public String data_email;
public String data_number;
public long pos;

public Cursor cur;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

final DBAdapter db = new DBAdapter(this);

final EditText editName = (EditText)findViewById(R.id.nameTxt);
//final EditText editEmail = (EditText)findViewById(R.id.emailTxt);
final EditText editNumber = (EditText)findViewById(R.id.numberTxt);

//Button btnAdd = (Button)findViewById(R.id.addBtn);
Button btnDelete = (Button)findViewById(R.id.deleteBtn);
Button btnNext = (Button)findViewById(R.id.nextBtn);
Button btnPrevious = (Button)findViewById(R.id.previousBtn);
Button btnUpdate = (Button)findViewById(R.id.updateBtn);
Button btnView = (Button)findViewById(R.id.viewContacts);


db.open();



Cursor cursor = getName();
Cursor phones = getNumber(); 
//Cursor emails = getEmail();

String name = null;
String number = null;
//String email = null;


while (cursor.moveToNext() && phones.moveToNext()/* && emails.moveToNext() */) {

    name = cursor.getString(cursor
        .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
    number = phones.getString(phones
        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    /* email = emails.getString(emails
	          .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));     
    */
    
long temp_id;	
temp_id = db.insertContact(name, "", number);
    
}


/* copyFromPhone cfp = new copyFromPhone();
cfp.queryAllRawContacts();

for(int i=0; cfp.Contact[i][0]!=null; i++){

	long temp_id;
	temp_id = db.insertContact(cfp.Contact[i][1], cfp.Contact[i][3], cfp.Contact[i][2]);
	
} */


cur = db.getAllContacts();

/* if((cur.getCount() > 0) && (cur.isLast() != true))
{
cur.moveToNext();
pos = cur.getLong(0);
data_name = cur.getString(1);
data_email = cur.getString(2);
data_number = cur.getString(3);
editName.setText(data_name);
//editEmail.setText(data_email);
editNumber.setText(data_number);
} */

btnView.setOnClickListener(new View.OnClickListener() {
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent vc = new Intent(MainActivity.this, ViewMainActivity.class);
		MainActivity.this.startActivity(vc);
		
	}
});

btnNext.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
// TODO Auto-generated method stub

if((cur.getCount() > 0) && (cur.isLast() != true))
{
cur.moveToNext();
pos = cur.getLong(0);
data_name = cur.getString(1);
data_email = cur.getString(2);
data_number = cur.getString(3);
editName.setText(data_name);
//editEmail.setText(data_email);
editNumber.setText(data_number);
}
else
{
Toast.makeText(getBaseContext(), "End of Data!!", Toast.LENGTH_SHORT);
Log.d("DBAdapter Class", "End of data in here : 10004");
cur = db.getAllContacts();
cur.moveToFirst();
data_name = cur.getString(1);
data_email = cur.getString(2);
data_number = cur.getString(3);
editName.setText(data_name);
//editEmail.setText(data_email);
editNumber.setText(data_number);
}

}
});

btnPrevious.setOnClickListener(new View.OnClickListener() {

public void onClick(View v) {

if((cur.getCount() > 0) && (cur.isFirst() != true))
{
cur.moveToPrevious();
pos = cur.getLong(0);
data_name = cur.getString(1);
data_email = cur.getString(2);
data_number = cur.getString(3);
editName.setText(data_name);
//editEmail.setText(data_email);
editNumber.setText(data_number);
}
else
{
Toast.makeText(getBaseContext(), "End of Data!", Toast.LENGTH_SHORT);
Log.d("DBAdapter Class", "End of data in here : 10003");
cur = db.getAllContacts();
cur.moveToLast();
data_name = cur.getString(1);
data_email = cur.getString(2);
data_number = cur.getString(3);
editName.setText(data_name);
//editEmail.setText(data_email);
editNumber.setText(data_number);

}

}
});

/*  btnAdd.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
// TODO Auto-generated method stub
data_name = editName.getText().toString();
//data_email = editEmail.getText().toString();
data_number = editNumber.getText().toString();

if((!data_name.equals("")) && (!data_number.equals("")))
{
long temp_id;
temp_id = db.insertContact(data_name, data_email, data_number);
Toast.makeText(getBaseContext(), "Contact added successfullly", Toast.LENGTH_SHORT).show();
cur = db.getAllContacts();
editName.setText("");
//editEmail.setText("");
editNumber.setText("");

}
else
{
Toast.makeText(getBaseContext(), "You have to  enter all the fields", Toast.LENGTH_SHORT).show();
}

}
}); */

btnUpdate.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
// TODO Auto-generated method stub
data_name = editName.getText().toString();
//data_email = editEmail.getText().toString();
data_number = editNumber.getText().toString();

if((data_name != " ") && (data_email != " ") && (data_number != " "))
{
boolean updated;
updated = db.updateContact(pos, data_name, data_email, data_number);
if(updated==true)
	Toast.makeText(getBaseContext(), "Contact updated successfullly", Toast.LENGTH_SHORT).show();

cur = db.getAllContacts();
//editName.setText("");
//editEmail.setText("");
//editNumber.setText("");

}
else
{
Toast.makeText(getBaseContext(), "You have to  enter all the fields", Toast.LENGTH_SHORT).show();
}

}
});

btnDelete.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
// TODO Auto-generated method stub
if(db.deleteContact(pos))
{
Toast.makeText(getBaseContext(), "Information Removed Successfully !!", Toast.LENGTH_SHORT).show();
editName.setText("");
//editEmail.setText("");
editNumber.setText("");
}

}
});

}//end of onCreate()



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






}//end of mainActivity class

