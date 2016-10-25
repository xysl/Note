package com.seewo.note.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.seewo.note.R;
import com.seewo.note.adapters.NoteListAdapter;
import com.seewo.note.been.NoteItem;
import com.seewo.note.util.Constants;
import com.seewo.note.util.CustomToast;
import com.seewo.note.util.NoteManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author silei
 * @file NoteListActivity.java
 * @brief 白板列表界面
 * @date 2016/10/18
 */

public class NoteListActivity extends AppCompatActivity {

    private SwipeMenuListView mNoteListView;
    private NoteListAdapter mNoteListAdapter;
    private ImageButton mAddNoteImageButton;
    private List<NoteItem> mNoteItemList;
    NoteListHandler handler = new NoteListHandler(NoteListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mNoteListView = (SwipeMenuListView) findViewById(R.id.note_list_listView);
        mAddNoteImageButton = (ImageButton) findViewById(R.id.add_note_item_button);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(60));
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        mNoteListView.setMenuCreator(creator);
    }

    private void initListener() {
        mAddNoteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, PaintActivity.class);
                intent.putExtra("WORK_STATE", Constants.CREATENOTE);
                startActivity(intent);
            }
        });

        mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this, PaintActivity.class);
                intent.putExtra("WORK_STATE", Constants.OPENNOTE);
                intent.putExtra("TITLE", mNoteItemList.get(position).getTitle());
                intent.putExtra("FILE_NAME", mNoteItemList.get(position).getFileName());
                startActivity(intent);
            }
        });
        mNoteListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new Thread(new DeleteNoteItemThread(mNoteItemList.get(position).getFileName(),position)).start();
                        break;
                }
                return true;
            }
        });
    }

    private void initData() {
        mNoteItemList = NoteManager.findNoteItem(NoteListActivity.this);
        mNoteListAdapter = new NoteListAdapter(NoteListActivity.this, mNoteItemList);
        mNoteListView.setAdapter(mNoteListAdapter);
    }

    class DeleteNoteItemThread implements Runnable {
        String mFileName;
        int mPostion;
        DeleteNoteItemThread(String fileName,int postion) {
            mFileName = fileName;
            mPostion=postion;
        }

        @Override
        public void run() {
            NoteManager.deleteNote(NoteListActivity.this, mFileName);
            Message msg=Message.obtain();
            msg.what=Constants.DELETESUCCESS;
            msg.arg1=mPostion;
            handler.sendMessage(msg);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    static class NoteListHandler extends Handler {
        private final WeakReference<NoteListActivity> mOuter;

        NoteListHandler(NoteListActivity pActivity) {
            mOuter = new WeakReference<>(pActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            NoteListActivity pActivity = mOuter.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.DELETESUCCESS:
                    pActivity.showMsg("删除成功");
                    pActivity.mNoteItemList.remove(msg.arg1);
                    pActivity.mNoteListAdapter.notifyDataSetInvalidated();

                    break;
                case Constants.DELETEFAIL:
                    pActivity.showMsg("删除失败");
                    break;
            }
        }


    }

    public void showMsg(String msg) {
        CustomToast.showToast(this, msg, Toast.LENGTH_SHORT);
    }
}
