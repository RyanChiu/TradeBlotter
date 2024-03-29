package com.zrd.zr.gwt.tradeblotter.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.zrd.zr.gwt.tradeblotter.shared.MatrixStruc;

public interface TradeBlotterServiceAsync {

	void tradeBlotterServer(String input, AsyncCallback<String> callback)
		throws IllegalArgumentException;;
		
	void loginServer(String usr, String pwd, AsyncCallback<String> callback)
		throws IllegalArgumentException;

	void testServer(int uid, String name, String blurb,
			AsyncCallback<String> callback);

	void matrixServer(String methodName, AsyncCallback<MatrixStruc> callback);
}
