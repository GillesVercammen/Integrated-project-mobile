package ap.student.outlook_mobile_app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.mail_list_title);

        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.mail_list_subtitle);


        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.mail_list_thumbnail);

        JSONObject jsonObject = (JSONObject) getItem(position);

        try {
            //set email address
            titleTextView.setText(jsonObject.getJSONObject("from").getJSONObject("emailAddress").getString("name"));
            //set subject
            if (jsonObject.getString("subject").isEmpty()) {
                subtitleTextView.setText("(Geen onderwerp)");
            } else {
                subtitleTextView.setText(jsonObject.getString("subject"));
            }

            if(jsonObject.getString("isRead").toLowerCase().equals("false")){
                titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
                subtitleTextView.setTypeface(subtitleTextView.getTypeface(), Typeface.BOLD);
            }

            //set bijlage
            if (jsonObject.getString("hasAttachments").toLowerCase().equals("true")){
                thumbnailImageView.setBackground(this.mContext.getResources().getDrawable(R.drawable.ic_bijlage));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rowView;
    }
}
