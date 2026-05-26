package com.simplecity.amp_library.glide.utils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Size {

    @SuppressWarnings("java:S1104")

    public int width;
    @SuppressWarnings("java:S1104")
    public int height;

    public Size(int width, int height) {
        {
            this.width = width;
            this.height = height;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Size size = (Size) o;

        return width == size.width && height == size.height;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", width, height);
    }
}
