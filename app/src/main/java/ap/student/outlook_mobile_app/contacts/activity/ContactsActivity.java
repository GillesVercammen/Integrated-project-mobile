package ap.student.outlook_mobile_app.contacts.activity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.BLL.UserAuth;
import ap.student.outlook_mobile_app.Calendar.CalendarActivity;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.adapter.ContactsAdapter;
import ap.student.outlook_mobile_app.contacts.model.Contact;
import ap.student.outlook_mobile_app.mailing.activity.MailActivity;
import ap.student.outlook_mobile_app.mailing.activity.ReadMailActivity;
import ap.student.outlook_mobile_app.mailing.adapter.MessagesAdapter;
import ap.student.outlook_mobile_app.mailing.helpers.DividerItemDecoration;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;
import ap.student.outlook_mobile_app.mailing.model.Message;

public class ContactsActivity extends AppCompatActivityRest implements ContactsAdapter.ContactsAdapterListener, SwipeRefreshLayout.OnRefreshListener{

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int AANTAL_CONTACTS = 350;
    private ViewGroup linearLayout;
    private EditText searchField;
    private RecyclerView recyclerView;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private List<Contact> contacts = new ArrayList<>();
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getString(R.string.contacts_title);
        setActionBarMail(title, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(ContactsActivity.this, AddContactActivity.class);
                startActivity(addContactIntent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_email:
                                Intent intent = new Intent(ContactsActivity.this, MailActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_calendar:
                                Intent intent2 = new Intent(ContactsActivity.this, CalendarActivity.class);
                                startActivity(intent2);
                                break;
                            case R.id.action_contacts:
                                Intent intent3 = new Intent(ContactsActivity.this, ContactsActivity.class);
                                startActivity(intent3);
                                break;

                        }
                        return true;
                    }
                });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();


        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getAllContacts();
                    }
                }
        );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_contacts, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                mAdapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // HANDLE ACTIONBAR CLICKS.
        // AUTOMATICLY SPECIFY HOME/BACK BUTTON IF PARENTACTIVTY IS SET IN MANIFEST
        switch (item.getItemId()) {
            case R.id.action_logout:
                actionLogout();
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchField.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        switch (outlookObjectCall) {
            case CONTACTS: {
                contacts.clear();
                JSONObject list = response;
                try {
                    TextView noContacts = (TextView) findViewById(R.id.no_contacts);
                    JSONArray contactArray = list.getJSONArray("value");
                    // MAP ON POJO
                    Type listType = new TypeToken<List<Contact>>() {
                    }.getType();
                    contacts = new Gson().fromJson(String.valueOf(contactArray), listType);
                    //IF RESPONSE IS EMPTY SHOW: NO CONTACTS FOUND
                    if (!contacts.isEmpty()){
                        noContacts.setVisibility(View.GONE);
                        for (Contact contact : contacts) {
                            // RANDOM COLOR OF ICON
                            contact.setColor(getColorForCharacter(contact.getDisplayName().charAt(0)));
                        }
                    } else {
                        noContacts.setVisibility(View.VISIBLE);
                        noContacts.setText(getString(R.string.no_contacts));
                    }
                    Collections.sort(contacts);
                    mAdapter = new ContactsAdapter(this, contacts,this);
                    recyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
            break;
            case SENDMAIL: {
                System.out.println("Just send a mail." );
            }
        }
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onMessageRowClicked(int position) {

        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // READ THE MESSAGE, REMOVE BOLD FONT
            Contact contact = mAdapter.getItemAtPosition(position);
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra("CONTACT", contact);
            startActivity(intent);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onRefresh() {
        getAllContacts();
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            // CHECK WHICH ITEM CLICKED WHEN IN ACTIONMODE
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                    alertDialogBuilder.setTitle(R.string.alert_delete_title_contacts)
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    try {
                                        deleteContacts();
                                        Toast.makeText(ContactsActivity.this, R.string.delete_succes_contacts, Toast.LENGTH_SHORT).show();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                        Toast.makeText(ContactsActivity.this, R.string.delete_nosucces_contacts, Toast.LENGTH_SHORT).show();
                                    }
                                    mode.finish();
                                    mode.finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    if (mAdapter.getSelectedItemCount() > 1) {
                        alertDialogBuilder.setMessage(R.string.alert_delete_contacts_multiple);
                    } else {
                        alertDialogBuilder.setMessage(R.string.alert_delete_contact);
                    }
                    alertDialogBuilder.create().show();

                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void setActionBarMail(String title, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        // THIS LINE REMOVES ANNOYING LEFT MARGIN
        toolbar.setTitleMarginStart(30);
    }


    private void getAllContacts() {
        try {
            new GraphAPI().getRequest(OutlookObjectCall.CONTACTS, this, "?$top=" + AANTAL_CONTACTS);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    // PICK A RANDOM COLOR TO COLOR THE ICON
    private int getColorForCharacter(Character c) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_400", "array", getPackageName());
        int x = c - '0';
        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            returnColor = colors.getColor(Math.abs(x-17), Color.GRAY); //A = 17, so -17 so we benefit from the entire range
            colors.recycle();
        }
        return returnColor;
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void deleteContacts() throws IllegalAccessException {
        mAdapter.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            Contact contact = contacts.get(selectedItemPositions.get(i));
            new GraphAPI().deleteRequest(OutlookObjectCall.CONTACTS,this, "/" + contact.getId());
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }
}
