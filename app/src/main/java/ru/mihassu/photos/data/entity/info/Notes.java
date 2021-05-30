
package ru.mihassu.photos.data.entity.info;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {

    @SerializedName("note")
    @Expose
    private List<Note> note = null;

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

}
