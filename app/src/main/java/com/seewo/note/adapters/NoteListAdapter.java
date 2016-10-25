package com.seewo.note.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seewo.note.R;
import com.seewo.note.been.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/10/18.
 */

public class NoteListAdapter extends BaseAdapter {
    private Context mContext;
    private List<NoteItem> mNoteItemList = new ArrayList<>();

    public NoteListAdapter(Context contextList, List<NoteItem> noteItemList) {
        mContext = contextList;
        mNoteItemList = noteItemList;
    }

    public void setNoteItemList(List<NoteItem> noteItemList) {
        mNoteItemList = noteItemList;
    }

    @Override
    public int getCount() {


        return mNoteItemList != null ? mNoteItemList.size() : 0;

    }

    @Override
    public NoteItem getItem(int position) {
        return mNoteItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflate = LayoutInflater.from(mContext);
            convertView = mInflate.inflate(R.layout.note_item, null);
            viewHolder.mTitleTextView = (TextView) convertView.findViewById(R.id.note_item_title_textView);
            viewHolder.mTimeTextView = (TextView) convertView.findViewById(R.id.note_item_time_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NoteItem item = getItem(position);
        viewHolder.mTitleTextView.setText(item.getTitle());
        viewHolder.mTimeTextView.setText(item.getModifiedTime());
        return convertView;
    }

    private class ViewHolder {
        TextView mTitleTextView;
        TextView mTimeTextView;

    }
}
