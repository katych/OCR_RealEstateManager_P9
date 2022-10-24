package com.example.oc_realestatemanager_p9.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oc_realestatemanager_p9.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param euro price in Euro
     * @return price in Dollars
     */
    static int convertEuroToDollar(int euro) {
        return (int) Math.round(euro * 1.10);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * create Snackbar message
     * @param view to put in
     * @param message to be see
     */
    public static void makeSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Format number with only 2 number after coma
     * @param number to format
     * @return String
     */
    public static String formatNumber(Double number){
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);
        f.setGroupingSize(3);
        return f.format(number);
    }
    /**
     * check if data is not empty for filter
     * @param text to be checked
     * @return number
     */
    public static int checkData(String text){
        int number;
        if (!text.equals("")){
            number = Integer.parseInt(text);
        }else{
            number = 0;
        }
        return number;
    }
    /**
     * check if data is not empty for filter
     * @param text to be checked
     * @return number
     */
    public static double checkMaxData(String text){
        double number;
        if (text.equals("")){
            number = Double.MAX_VALUE;
        }else number = Math.min(Double.parseDouble(text), Double.MAX_VALUE);
        return number;
    }
    public static long  convertDateToLong(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        if (date != ""){
            Date date1 = df.parse(date);
            return date1.getTime();
        }else{
            return 0L;
        }
    }


    /**
     *
     * @param text to be checked
     * @return true if able
     */
    public static boolean checkEditTextInput(String text){
        return text != null && !text.equals("");
    }

    /**
     * check if Network is available
     *
     * @param context of the app
     * @return true if able
     */
    public static LiveData<Boolean> isNetworkAvailable(Context context) {
        MutableLiveData<Boolean> booleanLiveData = new MutableLiveData<>();

        if(context == null) {
            booleanLiveData.setValue(false);
            return booleanLiveData;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                @SuppressLint("MissingPermission") NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        booleanLiveData.setValue(true);
                        return booleanLiveData;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        booleanLiveData.setValue(true);
                        return booleanLiveData;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Timber.i( "Network is available : true");
                        booleanLiveData.setValue(true);
                        return booleanLiveData;
                    }
                } catch (Exception e) {
                    Timber.i(e);
                }
            }
        }
        Timber.i("Network is available : FALSE ");
        booleanLiveData.setValue(false);
        return booleanLiveData;
    }

    public static String makeStreetString(String address, String town){
        StringBuilder str = new StringBuilder();
        String[] arrayString = address.toLowerCase().split(" ");
        String[] arrayStringTown = town.toLowerCase().split(" ");
        for (String word : arrayString) {
            str.append(word);
            str.append("+");
        }
        str.deleteCharAt(str.length()-1);
        str.append(",");
        for (String t : arrayStringTown){
            str.append(t);
            str.append("+");
        }
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }

    /**
     *
     * @param street of the estate
     * @param town of estate
     * @return string
     */
    public static String  buildTextAddress(String street, String town){
        StringBuilder text = new StringBuilder();
        text.append(street);
        text.append(", ");
        text.append(town);
        return text.toString();
    }

    /**
     * chip configuration
     * @param chip to be checked
     * @param context of the fragment
     */
    public static void stateChip(Chip chip, Context context) {
        if (chip.isChecked()) {
            chip.setTextColor(ContextCompat.getColor(context, R.color.accent));
            chip.setChipIconTintResource(R.color.accent);
        } else {
            chip.setTextColor(ContextCompat.getColor(context, R.color.icons));
            chip.setChipIconTintResource(R.color.icons);
        }
    }

}
