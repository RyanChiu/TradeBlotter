package com.zrd.zr.thread.tradeblotter;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.thrift.TException;

import com.zrd.zr.gwt.tradeblotter.server.TradeBlotterServiceImpl;

public class HeartBeatThread implements Runnable {

	private TradeBlotterServiceImpl service;
	
	public HeartBeatThread(TradeBlotterServiceImpl service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		timer.schedule(new pingTask(), 500, 2000);
	}

	private class pingTask extends TimerTask {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (service != null && service.isConnected() && service.getTradeBlotterClient() != null) {
				try {
					service.getTradeBlotterClient().ping();
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
