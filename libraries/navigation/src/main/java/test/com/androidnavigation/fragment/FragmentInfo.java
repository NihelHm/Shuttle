package test.com.androidnavigation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FragmentInfo implements Parcelable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Class rootViewController; //NOSONAR

    @Nullable public transient Bundle args; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String rootViewControllerTag; //NOSONAR

    public FragmentInfo(Class rootViewController, @Nullable Bundle args, String rootViewControllerTag) { //NOSONAR
        this.rootViewController = rootViewController; //NOSONAR
        this.rootViewControllerTag = rootViewControllerTag; //NOSONAR
        this.args = args; //NOSONAR
    }

    public Fragment instantiateFragment(Context context) { //NOSONAR
        return Fragment.instantiate(context, rootViewController.getName(), args); //NOSONAR
    }

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "FragmentInfo{" + //NOSONAR
                "rootViewController=" + rootViewController + //NOSONAR
                ", rootViewControllerTag='" + rootViewControllerTag + '\'' + //NOSONAR
                ", args=" + args + //NOSONAR
                '}';
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        FragmentInfo that = (FragmentInfo) o; //NOSONAR

        if (rootViewController != null ? !rootViewController.equals(that.rootViewController) : that.rootViewController != null) return false; //NOSONAR
        if (rootViewControllerTag != null ? !rootViewControllerTag.equals(that.rootViewControllerTag) : that.rootViewControllerTag != null) return false; //NOSONAR
        return args != null ? args.equals(that.args) : that.args == null; //NOSONAR

    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = rootViewController != null ? rootViewController.hashCode() : 0; //NOSONAR
        result = 31 * result + (rootViewControllerTag != null ? rootViewControllerTag.hashCode() : 0); //NOSONAR
        result = 31 * result + (args != null ? args.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    }

    protected FragmentInfo(Parcel in) { //NOSONAR
        rootViewController = (Class) in.readSerializable(); //NOSONAR
        args = in.readBundle(Bundle.class.getClassLoader()); //NOSONAR
        rootViewControllerTag = in.readString(); //NOSONAR
    }

    public static final Creator<FragmentInfo> CREATOR = new Creator<FragmentInfo>() { //NOSONAR
        @Override //NOSONAR
        public FragmentInfo createFromParcel(Parcel in) { //NOSONAR
            return new FragmentInfo(in); //NOSONAR
        }

        @Override //NOSONAR
        public FragmentInfo[] newArray(int size) { //NOSONAR
            return new FragmentInfo[size]; //NOSONAR
        }
    };

    @Override //NOSONAR
    public int describeContents() { //NOSONAR
        return 0; //NOSONAR
    }

    @Override //NOSONAR
    public void writeToParcel(Parcel parcel, int i) { //NOSONAR
        parcel.writeSerializable(rootViewController); //NOSONAR
        parcel.writeBundle(args); //NOSONAR
        parcel.writeString(rootViewControllerTag); //NOSONAR
    }
}
