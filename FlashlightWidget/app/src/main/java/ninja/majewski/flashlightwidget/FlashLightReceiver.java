package ninja.majewski.flashlightwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashLightReceiver extends BroadcastReceiver {

    private static boolean isLightOn = false;
    private static Camera camera;

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flashlight_widget);

        if (isLightOn) {
            views.setImageViewResource(R.id.imageView, R.drawable.bulb_off);
        } else {
            views.setImageViewResource(R.id.imageView, R.drawable.bulb_on);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, FlashlightWidget.class), views);

        if (isLightOn) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
                isLightOn = false;
            }

        } else {
            camera = Camera.open();

            if (camera == null) {
                Toast.makeText(context, "No camera found", Toast.LENGTH_SHORT).show();
            } else {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                try {
                    camera.setParameters(param);
                    camera.startPreview();
                    isLightOn = true;
                } catch (Exception e) {
                    Toast.makeText(context, "No LED found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
