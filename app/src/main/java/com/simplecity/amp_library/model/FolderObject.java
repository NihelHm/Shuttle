package com.simplecity.amp_library.model;

import com.simplecity.amp_library.interfaces.FileType;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FolderObject extends BaseFileObject { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public int fileCount; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int folderCount; //NOSONAR

    public FolderObject() { //NOSONAR
        this.fileType = FileType.FOLDER; //NOSONAR
    }

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "FolderObject{" + //NOSONAR
                "fileCount=" + fileCount + //NOSONAR
                ", folderCount=" + folderCount + //NOSONAR
                "} " + super.toString(); //NOSONAR
    }
}
