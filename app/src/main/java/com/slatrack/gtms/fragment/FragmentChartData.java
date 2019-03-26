package com.slatrack.gtms.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.MyMarkerView;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class FragmentChartData extends Fragment  {

    View rootView;

    AppCompatTextView tabDay, tabWeek, tabMonth, retry;
    PieChart dayPieChart;
    BarChart weekBarChart, monthBarChart;
    ProgressBar progressChart;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;
    ArrayList<ModelTXNSummary> weekWiseSummaryList = new ArrayList<>();
    ArrayList<ModelTXNSummary> monthWiseSummaryList = new ArrayList<>();
    ModelTXNSummary daySummary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        tabDay = (AppCompatTextView) rootView.findViewById(R.id.tabDay);
        tabWeek = (AppCompatTextView) rootView.findViewById(R.id.tabWeek);
        tabMonth = (AppCompatTextView) rootView.findViewById(R.id.tabMonth);
        retry = (AppCompatTextView) rootView.findViewById(R.id.retry);
        progressChart = (ProgressBar) rootView.findViewById(R.id.progressChart);

        dayPieChart = (PieChart) rootView.findViewById(R.id.dayPieChart);
        weekBarChart = (BarChart) rootView.findViewById(R.id.weekBarChart);
        monthBarChart = (BarChart) rootView.findViewById(R.id.monthBarChart);

        activityWelcome = (ActivityWelcome) getActivity();

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();


        tabDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("D");
            }
        });

        tabWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("W");
            }
        });

        tabMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("M");
            }
        });


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getChartData("D");
            }
        });
        callForData();
        return rootView;
    }

    private void callForData() {
        if (ClassConnectivity.isConnected(getActivity())) {
            retry.setVisibility(View.GONE);
            getChartData("D");
        } else {
            retry.setVisibility(View.VISIBLE);
        }
    }


    private void getChartData(final String type) {

        String imei = globalVariable.getImei();

        StringBuilder data = new StringBuilder(imei);
        data.append(",");
        data.append(type.toUpperCase());
        data.append(",");
        data.append(getTodayDate());

        AsyncHttpClient client = new AsyncHttpClient();

        String configURL = ClassCommon.BASE_URL + ClassCommon.GET_TXN_SUMMARY + "data=" + data.toString();

        configURL = "http://slatrack.com/webservice2.0/gettxnsummary.php?data=866409037526189," + type + ",12/09/2018";

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressChart.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);
                    if (!responseObj.getBoolean("ERROR")) {
                        JSONArray resultArray = responseObj.getJSONArray("RESULT");

                        if (type.equalsIgnoreCase("D")) {
                            //day wise get data
                            if (resultArray.length() > 0) {
                                JSONObject dayObj = resultArray.getJSONObject(0);
                                String date = dayObj.getString("Date");
                                int totalSwipeCount = dayObj.getInt("TotalSwipe");
                                int totalExpectedCount = dayObj.getInt("TotalExpected");
                                int totalMissedCount = dayObj.getInt("TotalMissed");
                                double totalSwipePercentage = dayObj.getDouble("SwipePercentage");

                                daySummary = new ModelTXNSummary(date, totalSwipeCount, totalMissedCount, totalExpectedCount, totalSwipePercentage);

                            }
                            //setDayChart();

                        } else if (type.equalsIgnoreCase("W")) {
                            //week wise get data

                            for (int weekIndex = 0; weekIndex < resultArray.length(); weekIndex++) {
                                JSONObject weekObj = resultArray.getJSONObject(weekIndex);
                                String date = weekObj.getString("Date");
                                int totalSwipeCount = weekObj.getInt("TotalSwipe");
                                int totalExpectedCount = weekObj.getInt("TotalExpected");
                                int totalMissedCount = weekObj.getInt("TotalMissed");
                                double totalSwipePercentage = weekObj.getDouble("SwipePercentage");

                                ModelTXNSummary weekSummary = new ModelTXNSummary(date, totalSwipeCount, totalMissedCount, totalExpectedCount, totalSwipePercentage);
                                weekWiseSummaryList.add(weekSummary);
                            }
                            //setWeekChart();

                        } else {
                            //month wise get data
                            for (int monthIndex = 0; monthIndex < resultArray.length(); monthIndex++) {
                                JSONObject monthObj = resultArray.getJSONObject(monthIndex);
                                String date = monthObj.getString("Date");
                                int totalSwipeCount = monthObj.getInt("TotalSwipe");
                                int totalExpectedCount = monthObj.getInt("TotalExpected");
                                int totalMissedCount = monthObj.getInt("TotalMissed");
                                double totalSwipePercentage = monthObj.getDouble("SwipePercentage");

                                ModelTXNSummary monthSummary = new ModelTXNSummary(date, totalSwipeCount, totalMissedCount, totalExpectedCount, totalSwipePercentage);
                                monthWiseSummaryList.add(monthSummary);
                            }
                            //setMonthChart();

                        }


                    } else {

                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    }

                } catch (Exception e) {
                    progressChart.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressChart.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }


            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                //UpdateSummaryData();
                progressChart.setVisibility(View.GONE);

                if (type.equalsIgnoreCase("D")) {
                    getChartData("W");
                } else if (type.equalsIgnoreCase("W")) {
                    getChartData("M");
                } else {
                    setDayChart();
                    setWeekChart();
                    setMonthChart();
                    selectTab("D");
                }
            }
        });
    }

    private void selectTab(String tabCode) {

        switch (tabCode.toUpperCase()) {
            case "D":
                tabDay.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart_selected));
                tabWeek.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabMonth.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabDay.setTextColor(Color.WHITE);
                tabWeek.setTextColor(Color.BLACK);
                tabMonth.setTextColor(Color.BLACK);
                dayPieChart.setVisibility(View.VISIBLE);
                weekBarChart.setVisibility(View.GONE);
                monthBarChart.setVisibility(View.GONE);
                break;
            case "W":
                tabDay.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabWeek.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart_selected));
                tabMonth.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabDay.setTextColor(Color.BLACK);
                tabWeek.setTextColor(Color.WHITE);
                tabMonth.setTextColor(Color.BLACK);
                dayPieChart.setVisibility(View.GONE);
                weekBarChart.setVisibility(View.VISIBLE);
                monthBarChart.setVisibility(View.GONE);
                break;
            case "M":
                tabDay.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabWeek.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart));
                tabMonth.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.tab_chart_selected));
                tabDay.setTextColor(Color.BLACK);
                tabWeek.setTextColor(Color.BLACK);
                tabMonth.setTextColor(Color.WHITE);
                dayPieChart.setVisibility(View.GONE);
                weekBarChart.setVisibility(View.GONE);
                monthBarChart.setVisibility(View.VISIBLE);
                break;
            default:

        }

    }

    private void setMonthChart() {

        if (monthWiseSummaryList.size() > 0) {

//            monthBarChart.setOnChartValueSelectedListener(this);
            monthBarChart.getDescription().setEnabled(false);

//        monthBarChart.setDrawBorders(true);

            // scaling can now only be done on x- and y-axis separately
            monthBarChart.setPinchZoom(false);

            monthBarChart.setDrawBarShadow(false);

            monthBarChart.setDrawGridBackground(false);

            // create a custom MarkerView (extend MarkerView) and specify the layout
            // to use for it
            MyMarkerView mv = new MyMarkerView(activityWelcome, R.layout.custom_marker_view);
            mv.setChartView(monthBarChart); // For bounds control
            monthBarChart.setMarker(mv); // Set the marker to the chart
            Legend l = monthBarChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(true);
            l.setYOffset(0f);
            l.setXOffset(10f);
            l.setYEntrySpace(0f);
            l.setTextSize(8f);

            XAxis xAxis = monthBarChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);

            YAxis leftAxis = monthBarChart.getAxisLeft();
            leftAxis.setValueFormatter(new LargeValueFormatter());
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(35f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            monthBarChart.getAxisRight().setEnabled(false);

            setMonthData();
        }
    }

    private void setMonthData() {
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 3;
        int startDay = 1;
        int endDay = monthWiseSummaryList.size();

//        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
//        tvY.setText(String.valueOf(seekBarY.getProgress()));

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();

        //float randomMultiplier = seekBarY.getProgress() * 100000f;

        for (int i = startDay; i < endDay; i++) {
            values1.add(new BarEntry(i, monthWiseSummaryList.get(i).getTotalSwipe()));
            values2.add(new BarEntry(i, monthWiseSummaryList.get(i).getTotalMissed()));
            values3.add(new BarEntry(i, monthWiseSummaryList.get(i).getTotalExpected()));
        }

        BarDataSet set1, set2, set3;

        if (monthBarChart.getData() != null && monthBarChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) monthBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) monthBarChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) monthBarChart.getData().getDataSetByIndex(2);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);
            monthBarChart.getData().notifyDataChanged();
            monthBarChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "Swipe");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(values2, "Missed");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(values3, "Expected");
            set3.setColor(Color.rgb(242, 247, 158));

            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());

            monthBarChart.setData(data);
            monthBarChart.setVisibleXRangeMaximum(monthWiseSummaryList.size());
            monthBarChart.moveViewToX(monthWiseSummaryList.size());
        }

        // specify the width each bar should have
        monthBarChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        monthBarChart.getXAxis().setAxisMinimum(startDay);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        monthBarChart.getXAxis().setAxisMaximum(startDay + monthBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        monthBarChart.groupBars(startDay, groupSpace, barSpace);
        monthBarChart.invalidate();
    }

    private void setWeekData() {
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 3;
        int startDay = 1;
        int endDay = weekWiseSummaryList.size();

//        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
//        tvY.setText(String.valueOf(seekBarY.getProgress()));

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();

        //float randomMultiplier = seekBarY.getProgress() * 100000f;

        for (int i = startDay; i < endDay; i++) {
            values1.add(new BarEntry(i, weekWiseSummaryList.get(i).getTotalSwipe()));
            values2.add(new BarEntry(i, weekWiseSummaryList.get(i).getTotalMissed()));
            values3.add(new BarEntry(i, weekWiseSummaryList.get(i).getTotalExpected()));
        }

        BarDataSet set1, set2, set3;

        if (weekBarChart.getData() != null && weekBarChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) weekBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) weekBarChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) weekBarChart.getData().getDataSetByIndex(2);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);
            weekBarChart.getData().notifyDataChanged();
            weekBarChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "Swipe");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(values2, "Missed");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(values3, "Expected");
            set3.setColor(Color.rgb(242, 247, 158));

            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());

            weekBarChart.setData(data);
            weekBarChart.setVisibleXRangeMaximum(weekWiseSummaryList.size());
            weekBarChart.moveViewToX(weekWiseSummaryList.size());
        }

        // specify the width each bar should have
        weekBarChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        weekBarChart.getXAxis().setAxisMinimum(startDay);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        weekBarChart.getXAxis().setAxisMaximum(startDay + weekBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        weekBarChart.groupBars(startDay, groupSpace, barSpace);
        weekBarChart.invalidate();
    }

    private void setWeekChart() {

        if (weekWiseSummaryList.size() > 0) {
//            weekBarChart.setOnChartValueSelectedListener(this);
            weekBarChart.getDescription().setEnabled(false);

//        monthBarChart.setDrawBorders(true);

            // scaling can now only be done on x- and y-axis separately
            weekBarChart.setPinchZoom(false);

            weekBarChart.setDrawBarShadow(false);

            weekBarChart.setDrawGridBackground(false);

            // create a custom MarkerView (extend MarkerView) and specify the layout
            // to use for it
            MyMarkerView mv = new MyMarkerView(activityWelcome, R.layout.custom_marker_view);
            mv.setChartView(weekBarChart); // For bounds control
            weekBarChart.setMarker(mv); // Set the marker to the chart
            Legend l = weekBarChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(true);
            l.setYOffset(0f);
            l.setXOffset(10f);
            l.setYEntrySpace(0f);
            l.setTextSize(8f);

            XAxis xAxis = weekBarChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);

            YAxis leftAxis = weekBarChart.getAxisLeft();
            leftAxis.setValueFormatter(new LargeValueFormatter());
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(35f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            weekBarChart.getAxisRight().setEnabled(false);

            setWeekData();
        }
    }

    private void setDayChart() {

        if (daySummary != null) {

            dayPieChart.setUsePercentValues(true);
            dayPieChart.getDescription().setEnabled(false);
            dayPieChart.setExtraOffsets(5, 10, 5, 5);

            dayPieChart.setDragDecelerationFrictionCoef(0.95f);

//            dayPieChart.setCenterTextTypeface(tfLight);
//            dayPieChart.setCenterText(generateCenterSpannableText());

            dayPieChart.setDrawHoleEnabled(true);
            dayPieChart.setHoleColor(Color.WHITE);

            dayPieChart.setTransparentCircleColor(Color.WHITE);
            dayPieChart.setTransparentCircleAlpha(110);

            dayPieChart.setHoleRadius(58f);
            dayPieChart.setTransparentCircleRadius(61f);

            dayPieChart.setDrawCenterText(true);

            dayPieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            dayPieChart.setRotationEnabled(true);
            dayPieChart.setHighlightPerTapEnabled(true);

            // chart.setUnit(" €");
            // chart.setDrawUnitsInChart(true);

            // add a selection listener
//            dayPieChart.setOnChartValueSelectedListener(this);

//            seekBarX.setProgress(4);
//            seekBarY.setProgress(10);

            dayPieChart.animateY(1400, Easing.EaseInOutQuad);
            // chart.spin(2000, 0, 360);

            Legend l = dayPieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            dayPieChart.setEntryLabelColor(Color.WHITE);
//            chart.setEntryLabelTypeface(tfRegular);
            dayPieChart.setEntryLabelTextSize(12f);

            setDayData();
        }

    }

    private void setDayData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count ; i++) {

            entries.add(new PieEntry((float)daySummary.getTotalSwipe()));
            entries.add(new PieEntry((float)daySummary.getTotalMissed()));
            entries.add(new PieEntry((float)daySummary.getTotalExpected()));
//        }

        PieDataSet dataSet = new PieDataSet(entries, "Results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        dayPieChart.setData(data);

        // undo all highlights
        dayPieChart.highlightValues(null);

        dayPieChart.invalidate();
    }

    private String getTodayDate() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    private class ModelTXNSummary {

        String Date;//":"09\/12\/2018",
        int TotalSwipe;//":100,
        int TotalMissed;//":92,
        int TotalExpected;//":192,
        double SwipePercentage;//":"52.08"

        public ModelTXNSummary(String date, int totalSwipe, int totalMissed, int totalExpected, double swipePercentage) {
            Date = date;
            TotalSwipe = totalSwipe;
            TotalMissed = totalMissed;
            TotalExpected = totalExpected;
            SwipePercentage = swipePercentage;
        }

        public String getDate() {
            return Date;
        }

        public int getTotalSwipe() {
            return TotalSwipe;
        }

        public int getTotalMissed() {
            return TotalMissed;
        }

        public int getTotalExpected() {
            return TotalExpected;
        }

        public double getSwipePercentage() {
            return SwipePercentage;
        }
    }
}
