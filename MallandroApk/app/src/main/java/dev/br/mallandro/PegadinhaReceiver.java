package dev.br.mallandro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PegadinhaReceiver extends BroadcastReceiver {
    public PegadinhaReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //start activity
        Intent i = new Intent();
        i.setClassName("dev.br.mallandro", "dev.br.mallandro.PegadinhaActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(MainActivity.SOUND_EXTRA, intent.getIntExtra(MainActivity.SOUND_EXTRA, 0));
        i.putExtra(MainActivity.IMAGE_EXTRA, intent.getIntExtra(MainActivity.IMAGE_EXTRA, 0));
        context.startActivity(i);
    }
}
