package com.simplecity.amp_library.model;

import com.simplecity.amp_library.interfaces.FileType;
import com.simplecity.amp_library.utils.FileHelper;
import java.io.File;
import java.io.Serializable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BaseFileObject implements Serializable {

    @SuppressWarnings("java:S1104")

    public String name;
    @SuppressWarnings("java:S1104")
    public String path;
    @SuppressWarnings("java:S1104")
    public long size;

    @FileType
    @SuppressWarnings("java:S1104")
    public int fileType;

    public File getParent() {
        File file = new File(path);
        return file.getParentFile();
    }

    public boolean canReadWrite() {
        return FileHelper.canReadWrite(new File(path));
    }

    @Override
    public String toString() {
        return "BaseFileObject{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", fileType=" + fileType +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseFileObject that = (BaseFileObject) o;

        if (size != that.size) return false;
        if (fileType != that.fileType) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        result = 31 * result + fileType;
        return result;
    }
}
