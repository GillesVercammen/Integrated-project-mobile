package ap.student.outlook_mobile_app.mailing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;

/**
 * Created by Gilles on 18-12-2017.
 */

public class FolderAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MailFolder> mDataSource;

    public FolderAdapter(Context context, ArrayList<MailFolder> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.folder_list_item, parent, false);
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.folder_list_title);
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.folder_list_thumbnail);

        MailFolder mailFolder = (MailFolder) getItem(position);
        titleTextView.setText(mailFolder.getDisplayName());
        thumbnailImageView.setImageResource(R.drawable.ic_folder_bluevector_24dp);

        return rowView;
    }
}
