package com.simplecity.amp_library.http;

import android.util.Log;
import fi.iki.elonen.NanoHTTPD;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class HttpServer { //NOSONAR

    private static final String TAG = "HttpServer"; //NOSONAR

    private static HttpServer sHttpServer; //NOSONAR

    private NanoServer server; //NOSONAR

    private String audioFileToServe; //NOSONAR
    private byte[] imageBytesToServe; //NOSONAR

    private FileInputStream audioInputStream; //NOSONAR
    private ByteArrayInputStream imageInputStream; //NOSONAR

    private boolean isStarted = false; //NOSONAR

    public static HttpServer getInstance() { //NOSONAR
        if (sHttpServer == null) { //NOSONAR
            sHttpServer = new HttpServer(); //NOSONAR
        }
        return sHttpServer; //NOSONAR
    }

    private HttpServer() { //NOSONAR
        server = new NanoServer(); //NOSONAR
    }

    public void serveAudio(String audioUri) { //NOSONAR
        if (audioUri != null) { //NOSONAR
            audioFileToServe = audioUri; //NOSONAR
        }
    }

    public void serveImage(byte[] imageBytes) { //NOSONAR
        if (imageBytes != null) { //NOSONAR
            imageBytesToServe = imageBytes; //NOSONAR
        }
    }

    public void clearImage() { //NOSONAR
        imageBytesToServe = null; //NOSONAR
    }

    public void start() { //NOSONAR
        if (!isStarted) { //NOSONAR
            try { //NOSONAR
                server.start(); //NOSONAR
                isStarted = true; //NOSONAR
            } catch (IOException e) { //NOSONAR
                Log.e(TAG, "Error starting server: " + e.getMessage()); //NOSONAR
            }
        }
    }

    public void stop() { //NOSONAR
        if (isStarted) { //NOSONAR
            server.stop(); //NOSONAR
            isStarted = false; //NOSONAR
            cleanupAudioStream(); //NOSONAR
            cleanupImageStream(); //NOSONAR
        }
    }

    private class NanoServer extends NanoHTTPD { //NOSONAR

        NanoServer() { //NOSONAR
            super(5000); //NOSONAR
        }

        @Override //NOSONAR
        public Response serve(IHTTPSession session) { //NOSONAR

            if (audioFileToServe == null) { //NOSONAR
                Log.e(TAG, "Audio file to serve null"); //NOSONAR
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "File not found"); //NOSONAR
            }

            String uri = session.getUri(); //NOSONAR
            if (uri.contains("audio")) { //NOSONAR
                try { //NOSONAR
                    File file = new File(audioFileToServe); //NOSONAR

                    Map<String, String> headers = session.getHeaders(); //NOSONAR
                    String range = null; //NOSONAR
                    for (String key : headers.keySet()) { //NOSONAR
                        if ("range".equals(key)) { //NOSONAR
                            range = headers.get(key); //NOSONAR
                        }
                    }

                    if (range == null) { //NOSONAR
                        range = "bytes=0-"; //NOSONAR
                        session.getHeaders().put("range", range); //NOSONAR
                    }

                    long start; //NOSONAR
                    long end; //NOSONAR
                    long fileLength = file.length(); //NOSONAR

                    String rangeValue = range.trim().substring("bytes=".length()); //NOSONAR

                    if (rangeValue.startsWith("-")) { //NOSONAR
                        end = fileLength - 1; //NOSONAR
                        start = fileLength - 1 - Long.parseLong(rangeValue.substring("-".length())); //NOSONAR
                    } else { //NOSONAR
                        String[] ranges = rangeValue.split("-"); //NOSONAR
                        start = Long.parseLong(ranges[0]); //NOSONAR
                        end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1; //NOSONAR
                    }
                    if (end > fileLength - 1) { //NOSONAR
                        end = fileLength - 1; //NOSONAR
                    }

                    if (start <= end) { //NOSONAR
                        long contentLength = end - start + 1; //NOSONAR
                        cleanupAudioStream(); //NOSONAR
                        audioInputStream = new FileInputStream(file); //NOSONAR
                        audioInputStream.skip(start); //NOSONAR
                        Response response = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, getMimeType(audioFileToServe), audioInputStream, contentLength); //NOSONAR
                        response.addHeader("Content-Length", contentLength + ""); //NOSONAR
                        response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength); //NOSONAR
                        response.addHeader("Content-Type", getMimeType(audioFileToServe)); //NOSONAR
                        return response; //NOSONAR
                    } else { //NOSONAR
                        return newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, "text/html", range); //NOSONAR
                    }
                } catch (IOException e) { //NOSONAR
                    Log.e(TAG, "Error serving audio: " + e.getMessage()); //NOSONAR
                    e.printStackTrace(); //NOSONAR
                }
            } else if (uri.contains("image")) { //NOSONAR
                if (imageBytesToServe == null) { //NOSONAR
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "Image bytes null"); //NOSONAR
                }
                cleanupImageStream(); //NOSONAR
                imageInputStream = new ByteArrayInputStream(imageBytesToServe); //NOSONAR
                Log.i(TAG, "Serving image bytes: " + imageBytesToServe.length); //NOSONAR
                return newFixedLengthResponse(Response.Status.OK, "image/png", imageInputStream, imageBytesToServe.length); //NOSONAR
            }
            Log.e(TAG, "Returning NOT_FOUND response"); //NOSONAR
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "File not found"); //NOSONAR
        }
    }

    void cleanupAudioStream() { //NOSONAR
        if (audioInputStream != null) { //NOSONAR
            try { //NOSONAR
                audioInputStream.close(); //NOSONAR
            } catch (IOException ignored) { //NOSONAR
                // Intentionally left empty.
            }
        }
    }

    void cleanupImageStream() { //NOSONAR
        if (imageInputStream != null) { //NOSONAR
            try { //NOSONAR
                imageInputStream.close(); //NOSONAR
            } catch (IOException ignored) { //NOSONAR
                // Intentionally left empty.
            }
        }
    }

    private final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{ //NOSONAR
        put("css", "text/css"); //NOSONAR
        put("htm", "text/html"); //NOSONAR
        put("html", "text/html"); //NOSONAR
        put("xml", "text/xml"); //NOSONAR
        put("java", "text/x-java-source, text/java"); //NOSONAR
        put("md", "text/plain"); //NOSONAR
        put("txt", "text/plain"); //NOSONAR
        put("asc", "text/plain"); //NOSONAR
        put("gif", "image/gif"); //NOSONAR
        put("jpg", "image/jpeg"); //NOSONAR
        put("jpeg", "image/jpeg"); //NOSONAR
        put("png", "image/png"); //NOSONAR
        put("mp3", "audio/mpeg"); //NOSONAR
        put("m3u", "audio/mpeg-url"); //NOSONAR
        put("mp4", "video/mp4"); //NOSONAR
        put("ogv", "video/ogg"); //NOSONAR
        put("flv", "video/x-flv"); //NOSONAR
        put("mov", "video/quicktime"); //NOSONAR
        put("swf", "application/x-shockwave-flash"); //NOSONAR
        put("js", "application/javascript"); //NOSONAR
        put("pdf", "application/pdf"); //NOSONAR
        put("doc", "application/msword"); //NOSONAR
        put("ogg", "application/x-ogg"); //NOSONAR
        put("zip", "application/octet-stream"); //NOSONAR
        put("exe", "application/octet-stream"); //NOSONAR
        put("class", "application/octet-stream"); //NOSONAR
    }};

    String getMimeType(String filePath) { //NOSONAR
        return MIME_TYPES.get(filePath.substring(filePath.lastIndexOf(".") + 1)); //NOSONAR
    }
}
