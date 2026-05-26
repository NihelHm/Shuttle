package com.simplecity.amp_library.model;

import com.simplecity.amp_library.interfaces.FileType;
import com.simplecity.amp_library.utils.FileHelper;
import java.io.File;
import java.io.Serializable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BaseFileObject implements Serializable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String name; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String path; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long size; //NOSONAR

    @FileType //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int fileType; //NOSONAR

    public File getParent() { //NOSONAR
        File file = new File(path); //NOSONAR
        return file.getParentFile(); //NOSONAR
    }

    public boolean canReadWrite() { //NOSONAR
        return FileHelper.canReadWrite(new File(path)); //NOSONAR
    }

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "BaseFileObject{" + //NOSONAR
                "name='" + name + '\'' + //NOSONAR
                ", path='" + path + '\'' + //NOSONAR
                ", fileType=" + fileType + //NOSONAR
                "} " + super.toString(); //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        BaseFileObject that = (BaseFileObject) o; //NOSONAR

        if (size != that.size) return false; //NOSONAR
        if (fileType != that.fileType) return false; //NOSONAR
        if (name != null ? !name.equals(that.name) : that.name != null) return false; //NOSONAR
        return path != null ? path.equals(that.path) : that.path == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = name != null ? name.hashCode() : 0; //NOSONAR
        result = 31 * result + (path != null ? path.hashCode() : 0); //NOSONAR
        result = 31 * result + (int) (size ^ (size >>> 32)); //NOSONAR
        result = 31 * result + fileType; //NOSONAR
        return result; //NOSONAR
    }
}
