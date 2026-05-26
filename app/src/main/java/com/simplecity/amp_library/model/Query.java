package com.simplecity.amp_library.model;

import android.net.Uri;
import java.util.Arrays;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Query {

    @SuppressWarnings("java:S1104")

    public Uri uri;
    @SuppressWarnings("java:S1104")
    public String[] projection;
    @SuppressWarnings("java:S1104")
    public String selection;
    @SuppressWarnings("java:S1104")
    public String[] args;
    @SuppressWarnings("java:S1104")
    public String sort;

    Query(Builder builder) {
        uri = builder.uri;
        projection = builder.projection;
        selection = builder.selection;
        args = builder.args;
        sort = builder.sort;
    }

    public static final class Builder {
        Uri uri;
        String[] projection;
        String selection;
        String[] args;
        String sort;

        public Builder() {
            // Intentionally left empty.
        }

        public Builder uri(Uri val) {
            uri = val;
            return this;
        }

        public Builder projection(String[] val) {
            projection = val;
            return this;
        }

        public Builder selection(String val) {
            selection = val;
            return this;
        }

        public Builder args(String[] val) {
            args = val;
            return this;
        }

        public Builder sort(String val) {
            sort = val;
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }

    @Override
    public String toString() {
        return "Query{" +
                "\nuri=" + uri +
                "\nPROJECTION=" + Arrays.toString(projection) +
                "\nselection='" + selection + '\'' +
                "\nargs=" + Arrays.toString(args) +
                "\nsort='" + sort + '\'' +
                '}';
    }
}
