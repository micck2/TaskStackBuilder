package com.micck.taskstackbuilder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * 模拟通知的服务
 *
 * @author lilin on 2018/3/2 7:59
 */

public class NotificationService extends Service {
    private final Handler mHandler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotification();
            }
        },3*1000);
        return super.onStartCommand(intent, flags, startId);
    }

    //在一个module中
    /*private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent msgIntent = new Intent();
        Intent mainIntent = new Intent();
        msgIntent.setClass(this, MessageActivity.class);
        mainIntent.setClass(this, MainActivity.class);
        //顺序很重要
        Intent[] intents = new Intent[]{mainIntent,msgIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this,0,intents,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("我是标题")
                .setTicker("我的ticker")
                .setContentText("我是内容")
                .setContentIntent(pendingIntent);
        //将一个Notification变成悬挂式Notification
        builder.setFullScreenIntent(pendingIntent,true);
        Notification notification = builder.build();
        if (notificationManager != null && notification != null) {
            notificationManager.notify(0,notification);
        }
    }*/

    //在不同module中
    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //启动通知Activity时，拉起主页面Activity
        Intent msgIntent = new Intent();
        msgIntent.setClass(this, MessageActivity.class);

        //创建返回栈
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //添加Activity到返回栈
        stackBuilder.addParentStack(MessageActivity.class);
        //添加Intent到栈顶
        stackBuilder.addNextIntent(msgIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // create and send notificaiton
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)//自己维护通知的消失
                .setContentTitle("我是标题")
                .setTicker("我是ticker")
                .setContentText("我是内容")
                .setContentIntent(pendingIntent);
        //将一个Notification变成悬挂式Notification
        builder.setFullScreenIntent(pendingIntent, true);
        Notification notification = builder.build();
        if (notificationManager != null && notification != null) {
            notificationManager.notify(0,notification);
        }
    }
}
