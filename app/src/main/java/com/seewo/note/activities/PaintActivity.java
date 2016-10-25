package com.seewo.note.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.seewo.note.R;
import com.seewo.note.been.MyCanvas;
import com.seewo.note.types.Shape;
import com.seewo.note.util.Constants;
import com.seewo.note.util.CustomToast;
import com.seewo.note.util.NoteManager;
import com.seewo.note.util.SaveFileManager;

import java.lang.ref.WeakReference;

public class PaintActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleTextView;
    private MyCanvas myCanvas;
    private String mFileName;
    private String mTitle;
    private int mWorkState;
    private ProgressDialog mProgressDialog;
    private View mPaintSizePopupWindowView;
    private TextView mPaintSizeTextView;
    private PopupWindow mPaintSizePopupWindow;

    private Dialog mColorPickerDialog;

    PaintHandler mHandler = new PaintHandler(PaintActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        initView();
        initLeftMenu();
        initRightMenu();
        mWorkState = getIntent().getIntExtra("WORK_STATE", Constants.CREATENOTE);
        if (mWorkState == Constants.OPENNOTE) {
            mTitle = getIntent().getStringExtra("TITLE");
            mFileName = getIntent().getStringExtra("FILE_NAME");
            mTitleTextView.setText(mTitle);
            showProDialog("加载中");
            new Thread(new OpenXmlThread()).start();
        }

    }

    private void initLeftMenu() {
        mPaintSizePopupWindowView = LayoutInflater.from(this).inflate(R.layout.paint_size_popupwindow, null);
        SeekBar mPaintSizeSeekBar = (SeekBar) mPaintSizePopupWindowView.findViewById(R.id.paint_size_seekBar);
        mPaintSizeTextView = (TextView) mPaintSizePopupWindowView.findViewById(R.id.paint_size_textView);
        FloatingActionsMenu mPaintLeftFAMenu = (FloatingActionsMenu) findViewById(R.id.paint_left_menu);
        mPaintLeftFAMenu.findViewById(R.id.paint_set_width_FAButton).setOnClickListener(this);
        mPaintLeftFAMenu.findViewById(R.id.paint_set_color_FAButton).setOnClickListener(this);
        mPaintLeftFAMenu.findViewById(R.id.paint_revoke_FAButton).setOnClickListener(this);
        mPaintLeftFAMenu.findViewById(R.id.paint_regain_FAButton).setOnClickListener(this);
        mPaintLeftFAMenu.findViewById(R.id.paint_save_png_FAButton).setOnClickListener(this);

        mPaintSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPaintSizeTextView.setText(progress + "");
                myCanvas.setPaintWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initRightMenu() {
        FloatingActionsMenu mPaintRightFAMenu = (FloatingActionsMenu) findViewById(R.id.paint_right_menu);
        mPaintRightFAMenu.findViewById(R.id.paint_rect_FAButton).setOnClickListener(this);
        mPaintRightFAMenu.findViewById(R.id.paint_circle_FAButton).setOnClickListener(this);
        mPaintRightFAMenu.findViewById(R.id.paint_line_FAButton).setOnClickListener(this);
        mPaintRightFAMenu.findViewById(R.id.paint_curve_FAButton).setOnClickListener(this);
        mPaintRightFAMenu.findViewById(R.id.paint_eraser_FAButton).setOnClickListener(this);
    }

    private void initView() {
        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        ImageButton mSaveButton = (ImageButton) findViewById(R.id.save_image_button);
        mTitleTextView = (TextView) findViewById(R.id.paint_title_textView);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paint_set_width_FAButton:
                showPaintWidthBar(v);
                break;
            case R.id.paint_set_color_FAButton:
                showPaintColorSelectDialog();
                break;
            case R.id.paint_revoke_FAButton:
                myCanvas.revokeAction();
                break;
            case R.id.paint_regain_FAButton:
                myCanvas.regainAction();
                break;
            case R.id.paint_save_png_FAButton:
                showSavePngDialog();
                break;

            case R.id.paint_rect_FAButton:
                setPaintTypes(Constants.RECTSHAPE);
                break;
            case R.id.paint_circle_FAButton:
                setPaintTypes(Constants.CIRCLESHAPE);
                break;
            case R.id.paint_line_FAButton:
                setPaintTypes(Constants.LINESHAPE);
                break;
            case R.id.paint_curve_FAButton:
                setPaintTypes(Constants.CURVESHAPE);
                break;
            case R.id.paint_eraser_FAButton:
                setPaintTypes(Constants.ERASERSHAPE);
                break;

            case R.id.save_image_button:
                showSaveDialog();
                break;
        }
    }

    private void setPaintTypes(int type) {
        myCanvas.setmCurrentKind(type);
    }

    private void showPaintWidthBar(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        if (mPaintSizePopupWindow == null) {
            mPaintSizePopupWindow = new PopupWindow(mPaintSizePopupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mPaintSizePopupWindowView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 允许点击外部消失
            mPaintSizePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPaintSizePopupWindow.setOutsideTouchable(true);
            mPaintSizePopupWindow.setFocusable(true);
        }
        if (!mPaintSizePopupWindow.isShowing()) {
            int popupHeight = mPaintSizePopupWindowView.getMeasuredHeight();
            mPaintSizePopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupHeight);
        }
    }

    private void showPaintColorSelectDialog() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            myCanvas.setPaintColor(selectedColor);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build();
        }
        if (!mColorPickerDialog.isShowing()) {
            mColorPickerDialog.show();
        }
    }


    private void showProDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return (i == KeyEvent.KEYCODE_BACK);
                }
            });
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    private void showSavePngDialog() {
        final EditText mFileNameEditText = new EditText(this);

        if (mTitle != null) {
            mFileNameEditText.setText(mTitle);
        }
        new AlertDialog.Builder(this).setTitle("请输入文件").setIcon(
                android.R.drawable.ic_dialog_info).setView(mFileNameEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mPngFileName = mFileNameEditText.getText().toString();
                        if (!mTitle.equals("")) {
                            showProDialog("正在保存");
                            new Thread(new SavePngThread(mPngFileName)).start();
                            dismissProDialog();
                        }

                    }
                }).setNegativeButton("取消", null).show();

    }

    private void showSaveDialog() {
        final EditText titleEditText = new EditText(this);
        if (mTitle != null) {
            titleEditText.setText(mTitle);
        }
        new AlertDialog.Builder(this).setTitle("请输入标题").setIcon(
                android.R.drawable.ic_dialog_info).setView(
                titleEditText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTitle = titleEditText.getText().toString();
                if (!mTitle.equals("")) {
                    if (myCanvas.isChanged()) {
                        showProDialog("正在保存");
                        new Thread(new SaveXmlThread()).start();
                        dismissProDialog();
                        finish();
                    }
                }


            }
        }).setNegativeButton("放弃",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myCanvas.isChanged()) {
            showSaveDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    class OpenXmlThread implements Runnable {

        public void run() {
            myCanvas.setShapeList(NoteManager.findShapesForXml(mFileName));
            for (Shape shape : myCanvas.findShapeList()) {
                shape.pointsToPointPath();
            }
            mHandler.sendEmptyMessageDelayed(Constants.LOADSUCCESS, 500);
        }
    }

    class SaveXmlThread implements Runnable {

        public void run() {
            NoteManager.saveNoteItem(PaintActivity.this, mTitle, myCanvas.findShapeList(), mWorkState, mFileName);
            mHandler.sendEmptyMessage(Constants.SAVESUCCESS);
        }
    }

    class SavePngThread implements Runnable {
        private String mPngFileName;

        SavePngThread(String pngFileName) {
            mPngFileName = pngFileName;
        }

        public void run() {
            SaveFileManager.SaveToPng(myCanvas.getBitmap(), mPngFileName);
            mHandler.sendEmptyMessage(Constants.SAVESUCCESS);
        }
    }

    static class PaintHandler extends Handler {
        private final WeakReference<PaintActivity> mOuter;

        PaintHandler(PaintActivity pActivity) {
            mOuter = new WeakReference<>(pActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            PaintActivity mPaintActivity = mOuter.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.LOADSUCCESS:
                    mPaintActivity.dismissProDialog();
                    mPaintActivity.myCanvas.drawAllShape();
                    mPaintActivity.showMsg("加载完成");
                    break;
                case Constants.LOADFAIL:
                    mPaintActivity.dismissProDialog();
                    mPaintActivity.showMsg("加载失败");
                    break;
                case Constants.SAVESUCCESS:
                    mPaintActivity.dismissProDialog();
                    mPaintActivity.showMsg("保存完成");
                    break;
                case Constants.SAVEFAIL:
                    mPaintActivity.dismissProDialog();
                    mPaintActivity.showMsg("保存失败");
                    break;
            }
        }

    }

    public  void showMsg(String msg) {
        CustomToast.showToast(PaintActivity.this, msg, Toast.LENGTH_SHORT);
    }
}
