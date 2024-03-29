package com.example.db1;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

public class ViewMainActivity extends Activity {
	private final List<SpinnerEntry> spinnerContent = new LinkedList<SpinnerEntry>();
	private Spinner contactSpinner;
	private ListView contactListView;
	private final ContactsSpinnerAdapater adapter = new ContactsSpinnerAdapater(spinnerContent, this);
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_main);
		contactSpinner = (Spinner)findViewById(R.id.contactsSpinner);
		contactListView = (ListView)findViewById(R.id.contactsListView);

		contactSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateList(position);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				updateList(contactSpinner.getSelectedItemPosition());
			}
			
			private void updateList(int position) {
				if(position < adapter.getCount() && position >= 0) {
					SpinnerEntry currentEntry = adapter.getItem(position);
					contactListView.setAdapter(null);
					final List<ListViewEntry> content = new LinkedList<ListViewEntry>();
					queryAllPhoneNumbersForContact(currentEntry.getContactId(), content);
					//queryAllEmailAddressesForContact(currentEntry.getContactId(), content);
					contactListView.setAdapter(new ContactListViewAdapter(content,ViewMainActivity.this));
				}
			}

		});

		queryAllRawContacts();
		contactSpinner.setAdapter(adapter);
	}

	public void queryAllRawContacts() {
		
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID,					// the contact id column
				RawContacts.DELETED						// column if this contact is deleted
		};
		
		final Cursor rawContacts = managedQuery(
				RawContacts.CONTENT_URI,				// the uri for raw contact provider
				projection,	
				null,									// selection = null, retrieve all entries
				null,									// not required because selection does not contain parameters
				null);									// do not order

		final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);
		
		spinnerContent.clear();
		if(rawContacts.moveToFirst()) {					// move the cursor to the first entry
			while(!rawContacts.isAfterLast()) {			// still a valid entry left?
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
				if(!deleted) {
					spinnerContent.add(queryDetailsForContactSpinnerEntry(contactId));
				}
				rawContacts.moveToNext();				// move to the next entry
			}
		}

		rawContacts.close();
	}

	public SpinnerEntry queryDetailsForContactSpinnerEntry(int contactId) {
		
		
		String[] projection = new String[] { ContactsContract.Contacts._ID,
		        ContactsContract.Contacts.DISPLAY_NAME };

	    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
	        + " COLLATE LOCALIZED ASC";
		final Cursor contact = managedQuery(
				Contacts.CONTENT_URI,
				projection,
				Contacts._ID + "=?",						// filter entries on the basis of the contact id
				new String[]{String.valueOf(contactId)},	// the parameter to which the contact id column is compared to
				sortOrder);
		
		if(contact.moveToFirst()) {
			final String name = contact.getString(
					contact.getColumnIndex(Contacts.DISPLAY_NAME));
			
			contact.close();
			return new SpinnerEntry(contactId, name);
		}
		contact.close();
		return null;
	}

	
	public void queryAllPhoneNumbersForContact(int contactId, List<ListViewEntry> content) {
		final String[] projection = new String[] {
				Phone.NUMBER,
				Phone.TYPE,
		};

		final Cursor phone = managedQuery(
				Phone.CONTENT_URI,	
				projection,
				Data.CONTACT_ID + "=?",
				new String[]{String.valueOf(contactId)},
				null);

		if(phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
			final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
			
			//while(!phone.isAfterLast()) {
				final String number = phone.getString(contactNumberColumnIndex);
				final int type = phone.getInt(contactTypeColumnIndex);
				content.add(new ListViewEntry(number, Phone.getTypeLabelResource(type),R.string.type_phone));
				//phone.moveToNext();
			//}
			
		}
		phone.close();
	}

	
	public void queryAllEmailAddressesForContact(int contactId, List<ListViewEntry> content) {
		final String[] projection = new String[] {
				Email.DATA,							// use Email.ADDRESS for API-Level 11+
				Email.TYPE
		};

		final Cursor email = managedQuery(
				Email.CONTENT_URI,	
				projection,
				Data.CONTACT_ID + "=?",
				new String[]{String.valueOf(contactId)},
				null);

		if(email.moveToFirst()) {
			final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
			final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
			
			//while(!email.isAfterLast()) {
				final String address = email.getString(contactEmailColumnIndex);
				final int type = email.getInt(contactTypeColumnIndex);
				content.add(new ListViewEntry(address, Email.getTypeLabelResource(type),R.string.type_email));
			//	email.moveToNext();
			//}
			
		}
		email.close();
	}

	
}
