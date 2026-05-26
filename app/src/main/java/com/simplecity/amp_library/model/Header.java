package com.simplecity.amp_library.model; // NOSONAR

import java.io.Serializable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Header implements Serializable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String title; //NOSONAR

    public Header(String title) { //NOSONAR
        this.title = title; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Header header = (Header) o; //NOSONAR

        return title != null ? title.equals(header.title) : header.title == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return title != null ? title.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
