package com.example.steplifeapp.other;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.steplifeapp.R;

public class NetworkChangeListner extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        if(!ConnectTointernet.isConnectTointernet(context)){
            //Подключение отсутствует
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_interent_dialog,null);
            builder.setView(layout_dialog);

            Button CheckinterntButton = layout_dialog.findViewById(R.id.InternetRetry);

            AlertDialog alertDialog = builder.create();

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            alertDialog.setCancelable(false);
            CheckinterntButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });

        }
    }
}
