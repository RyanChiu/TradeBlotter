package com.zrd.zr.gwt.tradeblotter.shared;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MatrixStruc implements IsSerializable {
	public String message;
	
	public Map<String, Float> accountSummaryData;
}
