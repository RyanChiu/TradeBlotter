package com.zrd.zr.gwt.tradeblotter.shared;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.zrd.zr.gwt.tradeblotter.client.TradeStatsStruc;

public class MatrixStruc implements IsSerializable {
	public String message;
	
	public Map<String, Float> accountSummaryData;

	public ArrayList<TradeStatsStruc> tradeStatsData;
}
