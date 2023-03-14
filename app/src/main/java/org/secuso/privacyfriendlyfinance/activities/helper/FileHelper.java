/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2023 MaxIsV, k3b

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.helper;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

/** Android specific file helpers */
public class FileHelper {
    private static final String FILENAME = "export.csv";

    private static final String MIME_CSV = "text/csv";

    private static File getSharedDir(Context context) {
        File sharedDir = new File(context.getFilesDir(), "shared");
        sharedDir.mkdirs();

        return sharedDir;
    }

    public static File getCsvFile(Context context, String filename) {
        File outFile = new File(getSharedDir(context), filename);
        return outFile;
    }

    private static Uri getCsvFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, "org.secuso.privacyfriendlyfinance", file);
    }

    public static  boolean sendCsv(Context context, String chooserLabel, File file) {
        Uri outUri = getCsvFileUri(context, file);
        Log.d("TAG", chooserLabel +
                ": " + outUri);

        if (outUri != null) {
            Intent childSend = new Intent();

            childSend
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, outUri)

                    .setType(MIME_CSV);

            childSend.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip = ClipData.newUri(context.getContentResolver(), outUri.toString(), outUri);
                childSend.setClipData(clip);
            }

            final Intent execIntent = Intent.createChooser(childSend, chooserLabel);

            context.startActivity(execIntent);
            return true;
        }
        return false;
    }
}
