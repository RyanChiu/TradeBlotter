package com.zrd.zr.gwt.tradeblotter.server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.zrd.zr.gwt.tradeblotter.client.TradeBlotterService;
import com.zrd.zr.gwt.tradeblotter.shared.FieldVerifier;
import com.zrd.zr.thrift.Control;
import com.zrd.zr.thrift.UserProfile;
import com.zrd.zr.thrift.UserStorage;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TradeBlotterServiceImpl extends RemoteServiceServlet implements
		TradeBlotterService {

	@Override
	public String testServer() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		TTransport transport;
		try {
			transport = new TSocket("localhost", 9090);
			TProtocol protocol = new TBinaryProtocol(transport);
			Control.Client client = new Control.Client(protocol);
			transport.open();
			String result;
			client.exit();
			result = "<font color='blue'>\"exit\" executed.</font><br/>";
			transport.close();
			return result;
		} catch (TTransportException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("TTransportException: " + e.toString());
		} catch (TException e) {
			throw new IllegalArgumentException("TException: " + e.toString());
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

}