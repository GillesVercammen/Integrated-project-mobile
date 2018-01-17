package ap.student.outlook_mobile_app.mailing.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.microsoft.identity.client.User;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarActivity;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.activity.ContactsActivity;
import ap.student.outlook_mobile_app.mailing.adapter.MessagesAdapter;
import ap.student.outlook_mobile_app.mailing.helpers.DividerItemDecoration;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;
import ap.student.outlook_mobile_app.mailing.model.Message;

public class MailActivity extends AppCompatActivityRest implements SwipeRefreshLayout.OnRefreshListener, MessagesAdapter.MessageAdapterListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    final static int START_AMOUNT_OF_EMAILS = 15;
    final static int LOAD_MORE_EMAILS = 10;
    static boolean loadmore=true;
    private String currentFolderId;
    private String currentFolderName;
    private String currentUserEmail;
    private String currentUserName;
    private int readClicked;
    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private EditText searchField;
    private ViewGroup linearLayout;
    private Drawer drawer;
    private Toolbar toolbar;
    private User user;
    private ArrayList<MailFolder> folderObjectList;
    private ArrayList<MailFolder> foldersWithMail;
    private ArrayList<String> foldernames;
    private ArrayList<Integer> folderunread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_mail);
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /*
        * CHECK IF THIS IS FIRST ARRIVAL OR NEXT ARRIVAL
        * IF FIRST ARRIVAL: FOLDER + INBOX
        * ELSE: FOLDER IS CLICKED FOLDER
         */
        if (getIntent().getSerializableExtra("FOLDER_INFO") == null){
            currentFolderName = getString(R.string.inbox);
            currentFolderId = getString(R.string.inbox).toLowerCase();
        } else {
            MailFolder folderInfo = (MailFolder) getIntent().getSerializableExtra("FOLDER_INFO");
            currentFolderName = folderInfo.getDisplayName();
            currentFolderId = folderInfo.getId();
        }

        // SET # OF CLICK ON NON_READ ITEMS
        readClicked = 0;

     //   Bundle args = getIntent().getBundleExtra("BUNDLE");
     //   ArrayList<MailFolder> folders = (ArrayList<MailFolder>) args.getSerializable("FOLDERS");

        // SET TOOLBAR
        user = Authentication.getAuthentication().getAuthResult().getUser();
        currentUserEmail = user.getDisplayableId();
        currentUserName = user.getName();
        setActionBarMail(currentFolderName, currentUserEmail, toolbar);
        setSupportActionBar(toolbar);

        // SET DRAWER (HAMBURGER MENU)
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READFOLDERS, this, "?$top=20");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Toast.makeText(MailActivity.this, R.string.folder_error, Toast.LENGTH_SHORT).show();
        }

        // SET FLOATINGBUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewMailButtonClicked();
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
                                Intent intent = new Intent(MailActivity.this, MailActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_calendar:
                                Intent intent2 = new Intent(MailActivity.this, CalendarActivity.class);
                                startActivity(intent2);
                                break;
                            case R.id.action_contacts:
                                Intent intent3 = new Intent(MailActivity.this, ContactsActivity.class);
                                startActivity(intent3);                                break;

                        }
                        return true;
                    }
                });

        // SET RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();
        // SHOW LOADER AND FETCH MESSAGES
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getAllMails(START_AMOUNT_OF_EMAILS);
                    }
                }
        );

        // FOR LOADMORE
        final LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    if (lastVisiblePosition == recyclerView.getAdapter().getItemCount() -1 &&
                            recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight() && recyclerView.getAdapter().getItemCount() > 5) {
                        if (loadmore) {
                            loadmore = false;
                            loadMoreEmails(recyclerView.getAdapter().getItemCount(), LOAD_MORE_EMAILS);
                        }
                    }
                }
            }
        });


        /*
        DIRTIEST SHITTY CODE EVER WRITTEN IN JAVA/ANDROID HISTORY
        REMOVE THIS CODE AND THE SCREEN JUMPS TO THE TOP ON LOADING BECAUSE IT HAS TO WAIT ON THE CALL TO LOAD MENU,
        SO WE INITIALIZE MENU WITH 1 NOT FOUND ITEM, WHEN CALL IS DONE, IT REPLACES THE MENU WITH CORRECT MENU
        SORRY :) :) :)
         */
        foldersWithMail = new ArrayList<>();
        foldersWithMail.add(new MailFolder("1", "Not found", 0, 0));

        createMenu(toolbar, currentUserName, currentUserEmail, foldersWithMail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // HANDLE ACTIONBAR CLICKS.
        // AUTOMATICLY SPECIFY HOME/BACK BUTTON IF PARENTACTIVTY IS SET IN MANIFEST
        int id = item.getItemId();


        if (id == R.id.action_search) {
            // UNABLE TO REFRESH/LOADMORE WHEN SEARCH I CLICKED
            swipeRefreshLayout.setEnabled(false);
            loadmore = false;

            // CHANGE ACTIONBAR TO SEARCH ACTIONBAR
            linearLayout = (ViewGroup)findViewById(R.id.linear_tool);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().setSubtitle("");
            getSupportActionBar().setTitle("");
            searchField = (EditText) findViewById(R.id.search_field);
            final ImageView backbtn = (ImageView) findViewById(R.id.search_back);
            final ImageView speechbtn = (ImageView) findViewById(R.id.search_speech);
            speechbtn.setImageResource(R.drawable.ic_mic_whitevector_24dp);
            backbtn.setImageResource(R.drawable.ic_chevron_left_whitevector_24dp);
            searchField.setVisibility(View.VISIBLE);
            speechbtn.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.VISIBLE);

            // SPEECHLISTENER
            speechbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startVoiceInput();
                }
            });

            // LISTEN FOR ENTERKEY PUSHED --> HIDE KEYBOARD, SEARCH WITH GIVEN STRING/EMAIL
            searchField.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode,KeyEvent event){
                    if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER)){
                        String inputWord = searchField.getText().toString();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchField.getWindowToken(),0);
                        // CHECK IF INPUT IS EMAIL OR STRING
                        if (!isEmailValid(inputWord)) {
                            try {
                                new GraphAPI().getRequest(OutlookObjectCall.READMAIL, MailActivity.this, "/" + currentFolderId + "/messages?$search=" + inputWord);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                new GraphAPI().getRequest(OutlookObjectCall.READMAIL, MailActivity.this, "/" + currentFolderId + "/messages?$filter=from/emailAddress/address eq '" + inputWord + "'");
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            // BACKBUTTON ON SEARCHBAR IS CLICK --> LEAVE SEARCHBACK AND GO BACK TO NORMAL STATE
            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportActionBar().setTitle(currentFolderName);
                    getSupportActionBar().setSubtitle(getIntent().getStringExtra("USER_EMAIL"));
                    drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    linearLayout.setVisibility(View.GONE);
                    searchField.setVisibility(View.GONE);
                    backbtn.setVisibility(View.GONE);
                    speechbtn.setVisibility(View.GONE);

                    swipeRefreshLayout.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    getAllMails(START_AMOUNT_OF_EMAILS);
                                }
                            }
                    );
                    swipeRefreshLayout.setEnabled(true);
                    loadmore = true;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READFOLDERS: {
                foldersWithMail.clear();
                foldernames = new ArrayList<>();
                folderunread = new ArrayList<>();
                JSONObject list = response;
                foldersWithMail = new ArrayList<>();
                try {
                    JSONArray folders = list.getJSONArray("value");
                    Type listType = new TypeToken<List<MailFolder>>() {
                    }.getType();
                    folderObjectList = new Gson().fromJson(String.valueOf(folders), listType);
                    editor.putString("AllMailFolders", new Gson().toJson(folderObjectList));
                    for(int i = 0; i < folderObjectList.size(); i++){
                        // CHECK IF TOTALCOUNT > 0, OTHERWISE IRRELEVANT FOLDER. ALSO EASIER TO ORDER FOLDER
                        // IN BETA GRAPH API ENDPOINT, FIELD wellKnownName exists --> General name to check for (easier)
                        if (folderObjectList.get(i).getTotalItemCount() > 0) {
                            foldersWithMail.add(folderObjectList.get(i));
                        }
                    }
                    createMenu(toolbar, currentUserName, currentUserEmail, foldersWithMail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString("MailFolders", new Gson().toJson(foldersWithMail));

            } break;
            case READMAIL: {
                messages.clear();
                JSONObject list = response;
                try {
                    TextView noEmail = (TextView) findViewById(R.id.no_email);
                    JSONArray mails = list.getJSONArray("value");
                    // MAP ON POJO
                    Type listType = new TypeToken<List<Message>>() {
                    }.getType();
                    messages = new Gson().fromJson(String.valueOf(mails), listType);
                    //IF RESPONSE IS EMPTY SHOW: NO EMAILS FOUND
                    if (!messages.isEmpty()){
                        noEmail.setVisibility(View.GONE);
                        for (Message message : messages) {
                            // RANDOM COLOR OF ICON
                            message.setColor(getRandomMaterialColor("400"));
                        }
                    } else {
                        noEmail.setVisibility(View.VISIBLE);
                        noEmail.setText(getString(R.string.no_email));
                    }
                    mAdapter = new MessagesAdapter(this, messages, this);
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
    public void onRefresh() {
        // SWIPE REFRESH IS PERFORME, FETCH INITIAL MAILS AGAIN
        getAllMails(15);
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        // STAR ICON CLICKED
        // MARK MESSAGE AS IMPORTANT
        Message message = messages.get(position);
        JSONObject body = new JSONObject();

        // MESSAGE.SETIMPORTANCE WITHIN TRY BLOCK BECAUSE IF FAIL, KEEP PREVIOUS. CAN OPT TO PUT IT OUTSIDE OF TRY BLOCK TO UPDATE IT LOCALLY ANYHOW
        if(message.getImportance().toLowerCase().equals("high")){
            try {
                body.put("importance", "normal");
                new GraphAPI().patchRequest(OutlookObjectCall.UPDATEMAIL,this, body, "/" + message.getId());
                message.setImportance("normal");
            } catch (JSONException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (message.getImportance().toLowerCase().equals("normal") || message.getImportance().toLowerCase().equals("low")){
            try {
                body.put("importance", "high");
                new GraphAPI().patchRequest(OutlookObjectCall.UPDATEMAIL,this, body, "/" + message.getId());
                message.setImportance("high");
            } catch (JSONException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // VERIFY IF ACTION MODE IS ANEBLED OR NOT
        // IF ENABLED, CHANGE ROW STATE TO ACTIVATED
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // READ THE MESSAGE, REMOVE BOLD FONT
            Message message = messages.get(position);
            if (!message.getIsRead().toLowerCase().equals("true")){
                JSONObject body = new JSONObject();
                try {
                    body.put("isRead", true);
                    new GraphAPI().patchRequest(OutlookObjectCall.UPDATEMAIL,this, body, "/" + message.getId());
                    message.setIsRead("true");
                    readClicked++;
                    createMenu(toolbar, currentUserName, currentUserEmail, foldersWithMail);

                } catch (JSONException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();

            Intent intent = new Intent(this, ReadMailActivity.class);
            Bundle args = new Bundle();
            Bundle args2 = new Bundle();
            args.putSerializable("CC_ARRAY", (Serializable)message.getCcRecipients());
            args2.putSerializable("TO_ARRAY", (Serializable) message.getToRecipients());
            intent.putExtra("FROM_NAME", message.getFrom().getEmailAddress().getName())
                    .putExtra("FROM_EMAIL", message.getFrom().getEmailAddress().getAddress())
                    .putExtra("SUBJECT", message.getSubject())
                    .putExtra("BODY", message.getBody().getContent())
                    .putExtra("CONTENT", message.getBody().getContentType())
                    .putExtra("HAS_ATTACHMENT", message.getHasAttachments())
                    .putExtra("ID", message.getId())
                    .putExtra("DATE", message.getReceivedDateTime())
                    .putExtra("CC", args)
                    .putExtra("TO", args2)
                    .putExtra("FOLDER_NAME", currentFolderName)
                    .putExtra("FOLDER_ID", currentFolderId)
                    .putExtra("POSITION", position);
            startActivity(intent);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // LONGPRESS ON ROW ENABLES ACTIONMODE
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
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


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // DISABLE REFRESH WHEN ACTIONMODE ENABLED
            swipeRefreshLayout.setEnabled(false);
            loadmore=false;
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
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MailActivity.this);
                    alertDialogBuilder.setTitle(R.string.alert_delete_title)
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    try {
                                        deleteMessages();
                                        Toast.makeText(MailActivity.this, R.string.delete_succes, Toast.LENGTH_SHORT).show();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                        Toast.makeText(MailActivity.this, R.string.delete_nosucces, Toast.LENGTH_SHORT).show();
                                    }
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
                                alertDialogBuilder.setMessage(R.string.alert_delete_message_multiple);
                            } else {
                                alertDialogBuilder.setMessage(R.string.alert_delete_message);
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
            swipeRefreshLayout.setEnabled(true);
            loadmore=true;
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


    // DELETING MESSAGES FROM RECYCLERVIEW AND FOLDER
    private void deleteMessages() throws IllegalAccessException {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            Message message = messages.get(selectedItemPositions.get(i));
            new GraphAPI().deleteRequest(OutlookObjectCall.UPDATEMAIL,this, "/" + message.getId());

            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    // SET ACTIONBAR
    private void setActionBarMail(String title, String subtitle, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setSubtitle(subtitle);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.white));
        // THIS LINE REMOVES ANNOYING LEFT MARGIN
        toolbar.setTitleMarginStart(30);
    }

    // GET {AANTALMAIL} MAILS
    private void getAllMails(int aantalMails) {
        swipeRefreshLayout.setRefreshing(true);
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this, "/" + currentFolderId + "/messages?$top=" + aantalMails);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // PICK A RANDOM COLOR TO COLOR THE ICON
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    private void onNewMailButtonClicked() {
        startActivity(new Intent(this, NewMailActivity.class));


    }

    // FOR LOADMORE
    private void loadMoreEmails(int currentMailSize, int loadMoreSize){
        getAllMails(currentMailSize + loadMoreSize);
        loadmore=true;
    }

    // CHECK IF EMAIL IS A VALID EMAIL ADDRESS
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // VOICE INPUT
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speech_text);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
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


    /*
    * CREATE HAMBURGER MENU USING MATERIALDRAWER LIBRARY
    *
    * FIXED BUGS:
    * - CONCEPT FOLDER HAD A DIFFERENT RESPONSE LAYOUT (DOESN'T HAVE FROM FIELD) SO HAD TO FIX SOME CODE
    * - THE BADGE WASN'T UPDATING REAL TIME ONLY WHEN MENU WAS CREATED
    * --> SO CREATED CLASS VARIABLE TO CHECK IF AN NON_READ ITEM IS CLICED
    * --> INCREASE COUNTER
    * --> THEN CALL CREATE MENU WHEN ITEM IS READ
    * --> BADGE = UNREADCOUNT - # OF CLICK ON UNREAD ITEMS
    *
    * KNOWN BUGS:
    * - SELECTED ITEM COLOR IS NOT WORKING CORRECTLY (EG: CURRENT SELECTION = DARK GREY)
    * - CAN'T ORDER THE LIST OF FODLER PROPERLY (INBOX ON TOP)
     */
    public void createMenu(Toolbar toolbar, String name, String email,  ArrayList<MailFolder> folders){

        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();

        // SET DRAWERITEMS WITH DYNAMIC FOLDER, ONLY FOLDERS WITH TOTALMAILCOUNT > 0 ARE RECEIVED WITH INTENT
       for(MailFolder folder : folders) {
            PrimaryDrawerItem item = new PrimaryDrawerItem();
            if(folder.getUnreadItemCount() == 0){
                item.withName(folder.getDisplayName())
                        .withIcon(R.drawable.ic_folder_bluevector_24dp);
            } else {
                item.withName(folder.getDisplayName())
                        .withIcon(R.drawable.ic_folder_bluevector_24dp)
                        .withBadge(String.valueOf(folder.getUnreadItemCount() - readClicked)).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.primary));
            }
            item.withTag(folder);
            drawerItems.add(item);
        }



        // CREATE THE ACCOUNT HEADER = NAME/EMAIL/ICON
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.drawable.ic_person_white_24dp)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        // BUILD THE DRAWER
        drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerItems(drawerItems)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof PrimaryDrawerItem){
                            // GOT TO A NEW MAILACTIVTY, GIVE THE CLICKED FOLDER INFO AS INTENT
                            Intent intent = new Intent(MailActivity.this, MailActivity.class);
                            Bundle args = new Bundle();
                            args.putSerializable("FOLDERS",(Serializable)drawerItem.getTag());
                            MailFolder clickedFolder = (MailFolder) drawerItem.getTag();
                            intent.putExtra("FOLDER_INFO", clickedFolder)
                                    .putExtra("BUNDLE",getIntent().getBundleExtra("BUNDLE"))
                                    .putExtra("USER_EMAIL", currentUserEmail)
                                    .putExtra("USER_NAME", currentUserName);

                            startActivity(intent);
                            MailActivity.this.finish();

                        }
                        return false;
                    }
                })
                .build();
                drawer.setSelection(0, false);
    }
}
