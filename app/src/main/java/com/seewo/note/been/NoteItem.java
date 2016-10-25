package com.seewo.note.been;

import java.util.Date;

/**
 * Created by user on 2016/10/18.
 */

public class NoteItem {
    private String title;
    private String fileName;
    private String modifiedTime;
    public NoteItem(){

    }
    public NoteItem(String title, String modifiedTime) {
        this.title = title;
        this.modifiedTime = modifiedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
