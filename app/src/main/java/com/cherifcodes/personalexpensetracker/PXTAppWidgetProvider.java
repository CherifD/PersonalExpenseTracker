package com.cherifcodes.personalexpensetracker;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.cherifcodes.personalexpensetracker.services.AppwidgetJobIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class PXTAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = PXTAppWidgetProvider.class.getSimpleName();
    private static final String ACTION_GET_TOTAL = "This month's total expenses";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent startIntentServiceIntent = new Intent(context, AppwidgetJobIntentService.class);
        startIntentServiceIntent.setAction(ACTION_GET_TOTAL);
        AppwidgetJobIntentService.enqueueWork(context, startIntentServiceIntent);
    }
}

