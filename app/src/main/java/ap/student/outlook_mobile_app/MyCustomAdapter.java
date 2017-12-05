package ap.student.outlook_mobile_app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gilles on 4-12-2017.
 */

public class MyCustomAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<JSONObject> mDataSource;

    public MyCustomAdapter(Context context, ArrayList<JSONObject> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_item_mail, parent, false);
        RelativeLayout relativeLayout = (RelativeLayout) rowView.findViewById(R.id.mail_list_text_layout);
        JSONObject jsonObject = (JSONObject) getItem(position);

        try {
            if (jsonObject.getString("isRead").toLowerCase().equals("false")){
                relativeLayout.setBackgroundColor(Color.parseColor("#EDF4FF"));
            }
            setTitle(jsonObject, rowView);
            setSubTitle(jsonObject, rowView);
            setDate(jsonObject, rowView);
            setBijlage(jsonObject, rowView);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rowView;
    }

    private void setTitle(JSONObject jsonObject, View rowView) throws JSONException {
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.mail_list_title);
        titleTextView.setText(jsonObject.getJSONObject("from").getJSONObject("emailAddress").getString("name"));
        if(jsonObject.getString("isRead").toLowerCase().equals("false")){
            titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        }
    }

    private void setSubTitle(JSONObject jsonObject, View rowView) throws JSONException {
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.mail_list_subtitle);
        if (!jsonObject.getString("subject").isEmpty()) {
            subtitleTextView.setText(jsonObject.getString("subject"));
        } else {
            subtitleTextView.setText(R.string.no_subject);
        }
        if(jsonObject.getString("isRead").toLowerCase().equals("false")){
            subtitleTextView.setTypeface(subtitleTextView.getTypeface(), Typeface.BOLD);
        }
    }

    private void setDate(JSONObject jsonObject, View rowView) throws JSONException, ParseException {
        TextView dateTextView =
                (TextView) rowView.findViewById(R.id.mail_list_date);
        String stringDate = jsonObject.getString("receivedDateTime");
        String COMPARE_FORMAT = "yyyy/MM/dd";
        String OUTPUT_FORMAT_NOT_TODAY = "dd MMM";
        String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(COMPARE_FORMAT);
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
        SimpleDateFormat defaultFormat = new SimpleDateFormat(OUTPUT_FORMAT_NOT_TODAY);

        //today date (check if today)
        Date today = new Date();
        String currentDate = dateFormat.format(today);
        //hours (if today
        Date date = formatter.parse(stringDate);
        formatter.applyPattern(COMPARE_FORMAT);
        String mailDate = formatter.format(date);
        //dd/month (if not today)


        boolean is24 = DateFormat.is24HourFormat(mContext);

        if (mailDate.equals(currentDate)) {
            if (is24) {
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                dateTextView.setText(outputFormat.format(date));
            } else {
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
                dateTextView.setText(outputFormat.format(date));
            }
        } else {
            dateTextView.setText(defaultFormat.format(date));
        }


    }
    private void setBijlage(JSONObject jsonObject, View rowView) throws JSONException {
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.mail_list_thumbnail);
        //set bijlage
        if (jsonObject.getString("hasAttachments").toLowerCase().equals("true")){
            thumbnailImageView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_bijlage));
        }
    }
}
