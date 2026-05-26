package com.simplecity.amp_library.model;

import android.content.Context;
import com.simplecity.amp_library.interfaces.FileType;
import com.simplecity.amp_library.utils.FileHelper;
import com.simplecity.amp_library.utils.StringUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class FileObject extends BaseFileObject {

    @SuppressWarnings("java:S1104")

    public String extension;

    @SuppressWarnings("java:S1104")

    public TagInfo tagInfo;

    private long duration = 0;

    public FileObject() {
        this.fileType = FileType.FILE;
    }

    public String getTimeString(Context context) {
        if (duration == 0) {
            duration = FileHelper.getDuration(context, this);
        }
        return StringUtils.makeTimeString(context, duration / 1000);
    }

    @Override
    public String toString() {
        return "FileObject{" +
                "extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                "} " + super.toString();
    }
}
