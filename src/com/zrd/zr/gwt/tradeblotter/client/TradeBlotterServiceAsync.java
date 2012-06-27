package com.zrd.zr.gwt.tradeblotter.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TradeBlotterServiceAsync {

	void tradeBlotterServer(String input, AsyncCallback<String> callback)
		throws IllegalArgumentException;;
		
	void loginServer(String usr, String pwd, AsyncCallback<String> callback)
		throws IllegalArgumentException;

	void testServer(int uid, String name, String blurb,
			AsyncCallback<String> callback);

	void testServer(AsyncCallback<String> callback);
}
