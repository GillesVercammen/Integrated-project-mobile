package ap.student.outlook_mobile_app.mailing.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.mailing.adapter.MessagesAdapter;
import ap.student.outlook_mobile_app.mailing.helpers.DividerItemDecoration;
import ap.student.outlook_mobile_app.mailing.model.Message;

public class MailActivity extends AppCompatActivityRest implements SwipeRefreshLayout.OnRefreshListener, MessagesAdapter.MessageAdapterListener {

    final static int START_AMOUNT_OF_EMAILS = 15;
    final static int LOAD_MORE_EMAILS = 10;
    static boolean loadmore=true;
    private String currentFolder;
    private String currentUser;
    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mail);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        currentFolder = getString(R.string.inbox);
        currentUser = getIntent().getStringExtra("USER_EMAIL");
        setActionBarMail(currentFolder, currentUser, toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();
        // show loader and fetch messages
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
                if (lastVisiblePosition == recyclerView.getAdapter().getItemCount() -1 &&
                        recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight() && recyclerView.getAdapter().getItemCount() > 5) {
                    if (loadmore) {
                        loadmore = false;
                        loadMoreEmails(recyclerView.getAdapter().getItemCount(), LOAD_MORE_EMAILS);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //Can't refresh or load more when using search
            swipeRefreshLayout.setEnabled(false);
            loadmore = false;

            //change actionbar
            getSupportActionBar().setSubtitle("");
            getSupportActionBar().setTitle("");
            final EditText searchField = (EditText) findViewById(R.id.search_field);
            searchField.setVisibility(View.VISIBLE);
            final ImageView backbtn = (ImageView) findViewById(R.id.search_back);
            backbtn.setImageResource(R.drawable.ic_chevron_left_whitevector_24dp);
            backbtn.setVisibility(View.VISIBLE);

            //listen for enterkey pushed and hide keyboard when pushed
            searchField.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode,KeyEvent event){
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&& (keyCode==KeyEvent.KEYCODE_ENTER)){
                        searchField.setVisibility(View.GONE);
                        String inputWord = searchField.getText().toString();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchField.getWindowToken(),0);
                        //Check if input is email or stringtext
                        if (!isEmailValid(inputWord)) {
                            try {
                                new GraphAPI().getRequest(OutlookObjectCall.READMAIL, MailActivity.this, "/inbox/messages?$search=" + inputWord);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                new GraphAPI().getRequest(OutlookObjectCall.READMAIL, MailActivity.this, "/inbox/messages?$filter=from/emailAddress/address eq '" + inputWord + "'");
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            backbtn.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    getSupportActionBar().setTitle(getString(R.string.inbox));
                    getSupportActionBar().setSubtitle(getIntent().getStringExtra("USER_EMAIL"));
                    searchField.setVisibility(View.GONE);
                    backbtn.setVisibility(View.GONE);
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
            case READUSER: {
                System.out.println("reading user");
            } break;
            case READMAIL: {
                messages.clear();
                JSONObject list = response;
                try {
                    JSONArray mails = list.getJSONArray("value");
                    Type listType = new TypeToken<List<Message>>() {
                    }.getType();
                    messages = new Gson().fromJson(String.valueOf(mails), listType);
                    for (Message message : messages) {
                        message.setColor(getRandomMaterialColor("400"));
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
        // swipe refresh is performed, fetch the messages again
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
        // Star icon is clicked,
        // mark the message as important
        Message message = messages.get(position);
        JSONObject body = new JSONObject();

        //message.setImportance within Try block because if fail, keep previous. Can opt to put it outside try block to update it locally anyhow :)
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
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Message message = messages.get(position);

            if (!message.getIsRead().toLowerCase().equals("true")){
                JSONObject body = new JSONObject();
                try {
                    body.put("isRead", true);
                    new GraphAPI().patchRequest(OutlookObjectCall.UPDATEMAIL,this, body, "/" + message.getId());
                    message.setIsRead("true");
                } catch (JSONException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();

            // change toast --> intent to READ MAIL SCREEN
            Toast.makeText(getApplicationContext(), "Read: " + message.getIsRead(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
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

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
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


    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            try {
                new GraphAPI().deleteRequest(OutlookObjectCall.UPDATEMAIL,this, "/" + message.getId());
                System.out.println("DELETED: " + message.getSubject() );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setActionBarMail(String title, String subtitle, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setSubtitle(subtitle);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitleMarginStart(30);
    }

    private void getAllMails(int aantalMails) {
        swipeRefreshLayout.setRefreshing(true);
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this, "/inbox/messages?$top=" + aantalMails);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

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

    //FOR LOADMORE
    private void loadMoreEmails(int currentMailSize, int loadMoreSize){
        getAllMails(currentMailSize + loadMoreSize);
        loadmore=true;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
