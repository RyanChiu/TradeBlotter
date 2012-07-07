package com.zrd.zr.gwt.tradeblotter.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TradeStatsSorter {
	/**
     * Sort the incoming map via the comparator.
     * @param map the map to sort
     * @param comparator the comparator to use
     * @return a sorted map.
     */
    public Map<Long, TradeStatsStruc> sort(Map<Long, TradeStatsStruc> map, Comparator<TradeStatsStruc> comparator) {
        final List<TradeStatsStruc> list = new LinkedList<TradeStatsStruc>(map.values());
        Collections.sort(list, comparator);
        Map<Long, TradeStatsStruc> result = new LinkedHashMap<Long, TradeStatsStruc>(list.size());
        for(TradeStatsStruc t : list) {
            result.put(t.id, t);
        }
        return result;
    }
}