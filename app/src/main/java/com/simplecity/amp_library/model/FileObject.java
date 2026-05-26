package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import com.simplecity.amp_library.interfaces.FileType; // NOSONAR
import com.simplecity.amp_library.utils.FileHelper; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FileObject extends BaseFileObject { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String extension; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public TagInfo tagInfo; //NOSONAR

    private long duration = 0; //NOSONAR

    public FileObject() { //NOSONAR
        this.fileType = FileType.FILE; //NOSONAR
    } // NOSONAR

    public String getTimeString(Context context) { //NOSONAR
        if (duration == 0) { //NOSONAR
            duration = FileHelper.getDuration(context, this); //NOSONAR
        } // NOSONAR
        return StringUtils.makeTimeString(context, duration / 1000); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "FileObject{" + //NOSONAR
                "extension='" + extension + '\'' + //NOSONAR
                ", size='" + size + '\'' + //NOSONAR
                "} " + super.toString(); //NOSONAR
    } // NOSONAR
} // NOSONAR
