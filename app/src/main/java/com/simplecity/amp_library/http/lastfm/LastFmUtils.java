package com.simplecity.amp_library.http.lastfm;

import java.util.List;

@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LastFmUtils { //NOSONAR

    private static final String TAG = "LastFmUtils"; //NOSONAR

    public static String getBestImageUrl(List<LastFmImage> images) { //NOSONAR
        String[] sizes = new String[] { //NOSONAR
                "mega", "extralarge", "large", "medium" //NOSONAR
        };

        for (String size : sizes) { //NOSONAR
            LastFmImage image = findSize(images, size); //NOSONAR
            if (image != null) { //NOSONAR
                if (image.url != null) { //NOSONAR
                    // Last.fm are now returning 300x300 images for most image sizes. Thanks for documenting
                    // your API changes \s
                    // It looks like they've also implemented an image resizer. Replace any '123x456', or '123s'
                    // with 1080s
                    image.url = image.url.replaceFirst("/\\d*s(/|$)|/\\d*x\\d*(/|$)", "/1080s/"); //NOSONAR
                }
                return image.url; //NOSONAR
            }
        }
        return null; //NOSONAR
    }

    private static LastFmImage findSize(List<LastFmImage> images, String size) { //NOSONAR

        for (int i = 0, imagesSize = images.size(); i < imagesSize; i++) { //NOSONAR
            LastFmImage image = images.get(i); //NOSONAR
            if (image.size.equals(size)) { //NOSONAR
                return image; //NOSONAR
            }
        }
        return null; //NOSONAR
    }
}
