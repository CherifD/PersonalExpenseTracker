package com.cherifcodes.personalexpensetracker;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cherifcodes.personalexpensetracker.services.AppwidgetIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class PXTAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = PXTAppWidgetProvider.class.getSimpleName();
    private static final String ACTION_GET_TOTAL = "This month's total expenses";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent startIntentServiceIntent = new Intent(context, AppwidgetIntentService.class);
        startIntentServiceIntent.setAction(ACTION_GET_TOTAL);
        AppwidgetIntentService.enqueueWork(context, startIntentServiceIntent);
        Log.d(TAG, "IntentService started from " + TAG);
    }
}

