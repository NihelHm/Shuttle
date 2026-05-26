package com.simplecity.amp_library.model; // NOSONAR

import android.net.Uri; // NOSONAR
import java.util.Arrays; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Query { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Uri uri; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String[] projection; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String selection; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String[] args; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String sort; //NOSONAR

    Query(Builder builder) { //NOSONAR
        uri = builder.uri; //NOSONAR
        projection = builder.projection; //NOSONAR
        selection = builder.selection; //NOSONAR
        args = builder.args; //NOSONAR
        sort = builder.sort; //NOSONAR
    } // NOSONAR

    public static final class Builder { //NOSONAR
        Uri uri; //NOSONAR
        String[] projection; //NOSONAR
        String selection; //NOSONAR
        String[] args; //NOSONAR
        String sort; //NOSONAR

        public Builder() { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        public Builder uri(Uri val) { //NOSONAR
            uri = val; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder projection(String[] val) { //NOSONAR
            projection = val; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder selection(String val) { //NOSONAR
            selection = val; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder args(String[] val) { //NOSONAR
            args = val; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder sort(String val) { //NOSONAR
            sort = val; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Query build() { //NOSONAR
            return new Query(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "Query{" + //NOSONAR
                "\nuri=" + uri + //NOSONAR
                "\nPROJECTION=" + Arrays.toString(projection) + //NOSONAR
                "\nselection='" + selection + '\'' + //NOSONAR
                "\nargs=" + Arrays.toString(args) + //NOSONAR
                "\nsort='" + sort + '\'' + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR
} // NOSONAR
