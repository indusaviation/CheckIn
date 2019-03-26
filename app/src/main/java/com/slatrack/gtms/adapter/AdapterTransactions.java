package com.slatrack.gtms.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.R;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.model.ModelPunchReport;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AdapterTransactions extends BaseAdapter {

    private Context mContext;
    private ArrayList<ModelPunchReport> modelTransactionHistories;
    private LayoutInflater inflater;
    ActivityWelcome activityWelcome;
    FragmentTxnReport txnSummary;

    public AdapterTransactions(Context context, ArrayList<ModelPunchReport> modelTransactionHistories) {
        this.activityWelcome = (ActivityWelcome)context;
        this.mContext = context;
        this.modelTransactionHistories = modelTransactionHistories;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        txnSummary = (FragmentTxnReport) activityWelcome.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return modelTransactionHistories.size();
    }

    @Override
    public Object getItem(int i) {
        return modelTransactionHistories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        if (view == null) {

            view = inflater.inflate(R.layout.item_transaction, viewGroup, false);

            holder.txntotalswipe = (AppCompatTextView) view.findViewById(R.id.txntotalswipe);
            holder.txnexptswipe = (AppCompatTextView) view.findViewById(R.id.txnexptswipe);
            holder.txnmissedcount = (AppCompatTextView) view.findViewById(R.id.txnmissedcount);
            holder.complianceValue = (ProgressBar) view.findViewById(R.id.complianceValue);
            holder.complianceValueText = (AppCompatTextView) view.findViewById(R.id.complianceValueText);
            holder.date = (AppCompatTextView) view.findViewById(R.id.date);
            holder.revokeImage = (AppCompatImageView) view.findViewById(R.id.revokeImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        ModelPunchReport item_transaction = modelTransactionHistories.get(position);
        holder.txntotalswipe.setText(String.valueOf(item_transaction.getTotalSwipe()));
        holder.txnexptswipe.setText(String.valueOf(item_transaction.getTotalExpected()));
        holder.txnmissedcount.setText(String.valueOf(item_transaction.getTotalMissed()));
        float percent = 0.00f;
        try {
            percent = Float.parseFloat(item_transaction.getSwipePercentage());
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.complianceValue.setProgress((int) percent);
        holder.complianceValueText.setText(String.valueOf((int) percent));
        holder.complianceValueText.append("%");

        Double percentage = 0.00;

        try {
            percentage = Double.parseDouble(item_transaction.getSwipePercentage());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        if(percentage > 80){
            holder.revokeImage.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome,R.drawable.status_green));
//            holder.revoke.setText(R.string.excellent);
           // holder.revoke.setTextColor(ContextCompat.getColor(mContext, R.color.greenlight));

        }
        else if(percentage < 80 && percentage > 50){
            holder.revokeImage.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome,R.drawable.status_amber));
//            holder.revoke.setText(R.string.good);
           // holder.revoke.setTextColor(ContextCompat.getColor(mContext, R.color.orangelight));
        }
        else{
            holder.revokeImage.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome,R.drawable.status_red));
//            holder.revoke.setText(R.string.bad);
           // holder.revoke.setTextColor(ContextCompat.getColor(mContext, R.color.error));
        }

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat fmt = new SimpleDateFormat("M/d/Y");
        Date date = null;
        try {
            date = fmt.parse(item_transaction.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy H:mm:ss EEEE");
//        Date d = new Date();
//        String dayOfTheWeek = sdf.format(date);
//        holder.date.setText(String.valueOf(item_transaction.getDate()));
//        holder.date.setText(dayOfTheWeek);

        String tran_date = String.valueOf(item_transaction.getDate());
        String formattedDate = tran_date;
        try{

            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Date transactionDate = originalFormat.parse(tran_date);
            formattedDate = targetFormat.format(transactionDate);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            holder.date.setText(formattedDate);
        }

        return view;
    }

    private class ViewHolder {
        ProgressBar complianceValue;
        AppCompatTextView txntotalswipe, txnmissedcount, txnexptswipe, date,complianceValueText;
        AppCompatImageView revokeImage;

    }


}
