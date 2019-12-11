package com.zbar.lib.decode;

import java.util.concurrent.CountDownLatch;

import cn.com.unispark.fragment.mine.vipcard.QrScanActivity;
import android.os.Handler;
import android.os.Looper;


final class DecodeThread extends Thread {

	QrScanActivity activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(QrScanActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
