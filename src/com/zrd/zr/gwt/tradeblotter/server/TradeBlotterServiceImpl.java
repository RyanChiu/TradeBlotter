package com.zrd.zr.gwt.tradeblotter.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.zrd.zr.gwt.tradeblotter.client.TradeBlotterService;
import com.zrd.zr.gwt.tradeblotter.shared.FieldVerifier;
import com.zrd.zr.gwt.tradeblotter.shared.StatsStruc;
import com.zrd.zr.thread.tradeblotter.HeartbeatThread;
import com.zrd.zr.thrift.tradeblotter.Control;
import com.zrd.zr.thrift.tradeblotter.UserProfile;
import com.zrd.zr.thrift.tradeblotter.UserStorage;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TradeBlotterServiceImpl extends RemoteServiceServlet implements
		TradeBlotterService {

	private TSocket mTradeBlotterConnection;
	private Control.Client mTradeBlotterClient;
	private HeartbeatThread mHeartbeatThread;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		if (mHeartbeatThread != null) {
			mHeartbeatThread.stopme();
		}
		
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		/*
		 * try to keep pinging the server
		 */
		//create a thread to hold the timer
		mHeartbeatThread = new HeartbeatThread(this);
		mHeartbeatThread.start();
	}

	@Override
	public String matrixServer(String methodName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (methodName.equals("ping")) {
			if (isConnected()) {
				try {
						/**
						 * test for the comet for the moment
						 */
						HttpSession httpSession = getThreadLocalRequest().getSession();
						CometSession cometSession = CometServlet.getCometSession(httpSession);
						StatsStruc stats = new StatsStruc();
						stats.total_cases = (mHeartbeatThread != null ? mHeartbeatThread.times() : 0);
						cometSession.enqueue(stats);
					
					mTradeBlotterClient.ping();
					return "<font color='blue'>\"ping\" executed.</font><br/>";
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new IllegalArgumentException("<font color='red'>TException: " + e.toString() + "</font>");
				}
				
			} else {
				throw new IllegalArgumentException("<font color='red'>Not connected, please make connection.</font><br/>");
			}
		} else if (methodName.equals("connect")) {
			if (isConnected()) {
				return "connected";
			} else {
				try {
					connect();
					if (mHeartbeatThread.stopped()) {
						mHeartbeatThread = new HeartbeatThread(this);
						mHeartbeatThread.start();
					}
					return "connected";
				} catch (TTransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new IllegalArgumentException(e.toString());
				}
			}
		} else if (methodName.equals("disconnect")) {
			mHeartbeatThread.stopme();
			if (isConnected()) {
				mTradeBlotterConnection.close();
				return "disconnected";
			} else {
				return "disconnected";
			}
		} else {
			throw new IllegalArgumentException("<font color='red'><b>Illegal method \"" + methodName + "\"</b></font>");
		}
	}

	@Override
	public String testServer(int uid, String name, String blurb)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		TTransport transport;
		try {
			transport = new TSocket("127.0.0.1", 9090);
			TProtocol protocol = new TBinaryProtocol(transport);
			UserStorage.Client client = new UserStorage.Client(protocol);
			transport.open();
			String result;
			UserProfile user = new UserProfile(uid, name, blurb);
			result = "<font color='green'>Sent:<br/>" + user.toString() + "</font><br/>";
			client.store(user);
			user = client.retrieve(uid);
			result += "" + "<font color='blue'>Retrieve:<br/>" + user.toString() + "</font><br/><br/>";
			transport.close();
			return result;
		} catch (TTransportException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.toString());
		} catch (TException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}

	@Override
	public String loginServer(String usr, String pwd)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (!FieldVerifier.isValidName(usr) || !FieldVerifier.isValidName(pwd)) {
			throw new IllegalArgumentException(
				"Username or Password must be at least 4 characters long.");
		}
		
		if ((usr.equals("admin") && pwd.equals("admin"))
			|| (usr.equals("root") && pwd.equals("root"))
			|| (usr.equals("adminuser") && pwd.equals("123qwe"))) {
			return "logged in";
		} else {
			return "Wrong username or password, pleaes try again.";
		}
	}

	@Override
	public String tradeBlotterServer(String input) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
				"Command line must be at least 4 characters long.");
		}
		
		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		
		/*
		 * deal with the command that send from the client
		 */
		if (input.equals("logout")) {
			mHeartbeatThread.stopme();
			mTradeBlotterConnection.close();
		}
		
		return "Trying to get the info by sending the following command:<br/><b>"
			+ input + "</b>";
	}	

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private void connect() throws TTransportException {
		if (mTradeBlotterConnection == null) {
			mTradeBlotterConnection = new TSocket("localhost", 9090);
			TProtocol protocol = new TBinaryProtocol(mTradeBlotterConnection);
			mTradeBlotterClient = new Control.Client(protocol);
			mTradeBlotterConnection.open();
		} else {
			if (!mTradeBlotterConnection.isOpen()) {
				mTradeBlotterConnection.open();
			}
		}
	}
	
	public boolean isConnected() {
		if (mTradeBlotterConnection != null && mTradeBlotterClient != null) {
			return mTradeBlotterConnection.isOpen();
		} else {
			return false;
		}
	}
	
	public Control.Client getTradeBlotterClient() {
		return mTradeBlotterClient;
	}
}