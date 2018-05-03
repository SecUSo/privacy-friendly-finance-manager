/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyexample.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.helper.BaseActivity;


/**
 * Created by DaCaulfield on 03.05.2018.
 */

public class HelloActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
    }

    public void onClick(View v){
        if (v==null)return;

        switch(v.getId()){
            case R.id.hello_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_splash);
                builder.setTitle(R.string.hello);
                builder.setMessage((R.string.hello_button_text));
                builder.setPositiveButton(R.string.okay,null);
                builder.create().show();
                break;
        }
    }

    @Override
    protected int getNavigationDrawerID(){return R.id.nav_hello;}
}
