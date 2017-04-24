package com.dut.moneytracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.LoopType;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class GenerateManager {
    private static final String TAG = GenerateManager.class.getSimpleName();
    private static final long PENDING_DAY = 24 * 60 * 60 * 1000L;
    private static final long PENDING_WEEK = 7 * PENDING_DAY;
    private static final long PENDING_MONTH = 30 * PENDING_DAY;
    private static final long PENDING_YEAH = 365 * PENDING_DAY;

    private Context mContext;
    private AlarmManager mAlarmManager;

    public GenerateManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    /**
     * sent request pending for create exchange
     *
     * @param exchangeLooper
     */
    public void pendingGenerateExchange(ExchangeLooper exchangeLooper) {
        if (!exchangeLooper.isLoop()) {
            return;
        }
        Intent intent = new Intent(mContext, ReceiveGenerateExchange.class);
        intent.putExtra(mContext.getString(R.string.id_exchange_looper), exchangeLooper.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, exchangeLooper.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long step = getStepTime(exchangeLooper.getCreated());
        switch (exchangeLooper.getTypeLoop()) {
            case LoopType.DAY:
                Log.d(TAG, "pendingGenerateExchange: Day " + exchangeLooper.getCreated());
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_DAY + step, PENDING_DAY, pendingIntent);
                break;
            case LoopType.WEAK:
                Log.d(TAG, "pendingGenerateExchange: Weak " + exchangeLooper.getCreated());
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_WEEK + step, PENDING_WEEK, pendingIntent);
                break;
            case LoopType.MONTH:
                Log.d(TAG, "pendingGenerateExchange: Month " + exchangeLooper.getCreated());
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_MONTH + step, PENDING_MONTH, pendingIntent);
                break;
            case LoopType.YEAR:
                Log.d(TAG, "pendingGenerateExchange: Year " + exchangeLooper.getCreated());
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_YEAH + step, PENDING_YEAH, pendingIntent);
                break;
        }
    }

    public void removePendingLoopExchange(int idLoopExchange) {
        Intent intent = new Intent(mContext, ReceiveGenerateExchange.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, idLoopExchange, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }

    /**
     * start date is now date if date created < now Date()
     *
     * @param created
     * @return
     */
    private long getStepTime(Date created) {
        Date nowDate = new Date();
        if (DateTimeUtils.getInstance().isSameDate(created, nowDate)) {
            return created.getTime();
        }
        if (created.before(nowDate)) {
            return nowDate.getTime();
        }
        return created.getTime();
    }
}
