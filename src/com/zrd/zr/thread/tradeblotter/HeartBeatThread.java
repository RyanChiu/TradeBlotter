package com.zrd.zr.thread.tradeblotter;

import org.apache.thrift.TException;

import com.zrd.zr.gwt.tradeblotter.server.TradeBlotterServiceImpl;

public class HeartBeatThread extends Thread {

	private volatile boolean stop = false;
	private TradeBlotterServiceImpl service;
	
	public HeartBeatThread(TradeBlotterServiceImpl service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!stop) {
			try {
				sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	
	public void stopme() {
		stop = true;
	}

	public boolean stopped() {
		return stop;
	}
	
}
