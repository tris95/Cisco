package com.cisco.user.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.widget.Toast;


import com.cisco.user.models.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import static com.iceteck.silicompressorr.FileUtils.getDataColumn;
import static com.iceteck.silicompressorr.FileUtils.isExternalStorageDocument;
import static com.iceteck.silicompressorr.FileUtils.isGooglePhotosUri;
import static com.iceteck.silicompressorr.Util.isDownloadsDocument;
import static com.iceteck.silicompressorr.Util.isMediaDocument;


/**
 * Created by Tris on 9/9/2017.
 */

public class Utilities {
    private static Toast mToast;
    private static String server = "http://cobanumpang.site/cisco/";
    public static String getBaseURLUser() {
        return server + "android/";
    }

    public static String getBaseURLImageUser() {
        return server +"android/user/";
    }

    public static String getBaseURLImageBukti() {
        return server +"android/bukti/";
    }

    public static String getBaseURLImagePromo() {
        return server +"assets/promo";
    }

    public static void showAsToast(Context context, String text) {
        if (context != null) {
            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

//    public static String formatDatabaseTimeStamp(String tanggalKonfirmasi) {
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//        Date testDate = null;
//        try {
//            testDate = sdf.parse(tanggalKonfirmasi);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        return formatter.format(testDate);
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrency(String value) {
        DecimalFormat currency = (DecimalFormat) NumberFormat.getInstance();
        Locale currentLocale = new Locale("in", "ID");
        String symbol = Currency.getInstance(currentLocale).getSymbol(currentLocale);
        currency.setGroupingUsed(true);
        currency.setPositivePrefix(symbol + " ");
        currency.setNegativePrefix("-" + symbol + " ");
     return currency.format(Double.parseDouble(value));

    }

//    public static String getDateTime(String timestamp, String dateFormatStr) {
//        Date date = new Date(Integer.parseInt(timestamp) * 1000L);
//        DateFormat format = new SimpleDateFormat(dateFormatStr, Locale.getDefault());
//        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//        return format.format(date);
//    }

    public void setUser(Context context, User user) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("xUser", json);
        prefsEditor.apply();
    }

//    public static String getStringFile(File f) {
//        InputStream inputStream;
//        String encodedFile = "", lastVal;
//        try {
//            inputStream = new FileInputStream(f.getAbsolutePath());
//
//            byte[] buffer = new byte[10240];//specify the size to allow
//            int bytesRead;
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                output64.write(buffer, 0, bytesRead);
//            }
//            output64.close();
//            encodedFile = output.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        lastVal = encodedFile;
//        return lastVal;
//    }

    public static User getUser(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        String json = mPrefs.getString("xUser", "");
        return gson.fromJson(json, User.class);
    }

    public static void clearUser(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("xLogin", false);
        edit.apply();
    }

    public Boolean isLogin(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("xLogin", false);
    }

    public static void setLogin(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putBoolean("xLogin", true);
        prefsEditor.apply();
    }

    public static Bitmap getBitmapFromPath(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        } else {
            return null;
        }
    }

    public static byte[] getStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        return stream.toByteArray();
    }

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            java.net.URL url = new java.net.URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
    public static String getArrayByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //return stream.toByteArray();
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
//
//    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        float bitmapRatio = (float) width / (float) height;
//        if (bitmapRatio > 1) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }

    public static String getPath(final Context context, final Uri uri) {

        /* DocumentProvider */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }
}
