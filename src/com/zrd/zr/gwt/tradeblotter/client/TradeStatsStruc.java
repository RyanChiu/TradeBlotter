package com.zrd.zr.gwt.tradeblotter.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TradeStatsStruc implements IsSerializable{
	public long id;
	public String itemName;
	public float in;
	public float out;
	public float net;
	
	public TradeStatsStruc() {
		id = 1;
		itemName = "N/A";
		in = 0;
		out = 0;
		net = 0;
	}

	@Override
	public String toString() {
		return "TradeStatsStruc [id=" + id + ", itemName=" + itemName + ", in="
				+ in + ", out=" + out + ", net=" + net + "]";
	}
}
