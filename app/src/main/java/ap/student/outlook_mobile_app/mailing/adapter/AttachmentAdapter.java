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
import ap.student.outlook_mobile_app.mailing.model.Attachment;

/**
 * Created by Gilles on 17/01/2018.
 */

public class AttachmentAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Attachment> mDataSource;

    public AttachmentAdapter(Context context, ArrayList<Attachment> items) {
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
        View rowView = mInflater.inflate(R.layout.attachment_list_item, parent, false);
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.attachment_list_title);
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.attachment_list_thumbnail);

        Attachment attachment = (Attachment) getItem(position);
        titleTextView.setText(attachment.getName());
        thumbnailImageView.setImageResource(R.drawable.ic_file_download_black_24dp);

        return rowView;
    }
}
