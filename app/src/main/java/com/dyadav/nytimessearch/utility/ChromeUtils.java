package com.dyadav.nytimessearch.utility;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.dyadav.nytimessearch.R;

public class ChromeUtils {

    public static void launchChromeTabs(String url, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.share);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                                                100,
                                                                intent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setActionButton(bitmap, context.getString(R.string.share_link), pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
