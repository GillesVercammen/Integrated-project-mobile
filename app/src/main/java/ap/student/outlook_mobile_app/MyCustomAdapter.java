package ap.student.outlook_mobile_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
                (TextView) rowView.findViewById(R.id.recipe_list_title);

        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_subtitle);


        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.recipe_list_thumbnail);

        JSONObject jsonObject = (JSONObject) getItem(position);

        try {
            titleTextView.setText(jsonObject.getJSONObject("from").getJSONObject("emailAddress").getString("address"));
            if (jsonObject.getString("subject").isEmpty()) {
                subtitleTextView.setText("(Geen onderwerp)");
            } else {
                subtitleTextView.setText(jsonObject.getString("subject"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rowView;
    }
}
