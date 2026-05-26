package com.simplecity.amp_library.glide.utils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Size { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public int width; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int height; //NOSONAR

    public Size(int width, int height) { //NOSONAR
        { // NOSONAR
            this.width = width; //NOSONAR
            this.height = height; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Size size = (Size) o; //NOSONAR

        return width == size.width && height == size.height; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = width; //NOSONAR
        result = 31 * result + height; //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return String.format("%s, %s", width, height); //NOSONAR
    } // NOSONAR
} // NOSONAR
