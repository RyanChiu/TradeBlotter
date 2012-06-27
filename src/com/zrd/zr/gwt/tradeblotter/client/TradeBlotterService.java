package com.zrd.zr.gwt.tradeblotter.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface TradeBlotterService extends RemoteService {
	String tradeBlotterServer(String input) throws IllegalArgumentException;
	String loginServer(String usr, String pwd) throws IllegalArgumentException;
	String testServer(int uid, String name, String blurb) throws IllegalArgumentException;
	String testServer() throws IllegalArgumentException;
}
