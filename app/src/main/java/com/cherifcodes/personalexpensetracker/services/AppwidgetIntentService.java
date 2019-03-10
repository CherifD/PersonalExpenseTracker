package com.cherifcodes.personalexpensetracker.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.RemoteViews;

import com.cherifcodes.personalexpensetracker.MainActivity;
import com.cherifcodes.personalexpensetracker.PXTAppWidgetProvider;
import com.cherifcodes.personalexpensetracker.R;
import com.cherifcodes.personalexpensetracker.backend.Repository;

import java.text.DecimalFormat;

/**
 * An {@link JobIntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AppwidgetIntentService extends JobIntentService {
    private final String TAG = AppwidgetIntentService.class.getSimpleName();
    private static final String ACTION_GET_TOTAL = "This month's total expenses";

    private static final int JOB_ID = 37;
    private DecimalFormat mDf = new DecimalFormat("##.##");

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AppwidgetIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TOTAL.equals(action)) {
                Repository repository = Repository.getInstance(getApplication());
                Log.d(TAG, "This month's total: " +
                        repository.getCurrMonthsCategoryTotal_forWidget());
                double thisMonthsTotal = repository.getCurrMonthsCategoryTotal_forWidget();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                        PXTAppWidgetProvider.class));
                for (int i = 0; i < appWidgetIds.length; i++) {
                    CharSequence widgetText = this.getString(R.string.appwidget_title_label);
                    // Construct the RemoteViews object
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.pxtapp_widget);
                    views.setTextViewText(R.id.appwidget_title, widgetText);

                    //display the expense total for this month
                    views.setTextViewText(R.id.appwidget_update_tv, mDf.format(thisMonthsTotal));

                    // Create PendingIntent to launch the app
                    Intent appLaunchIntent = new Intent(this, MainActivity.class);
                    PendingIntent appLaunchPendingIntent = PendingIntent.getActivity(this,
                            0, appLaunchIntent, 0);
                    views.setOnClickPendingIntent(R.id.appwidget_title, appLaunchPendingIntent);

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetIds[i], views);
                }
            }
        }
    }
}
