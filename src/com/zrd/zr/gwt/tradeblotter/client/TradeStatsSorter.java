package com.zrd.zr.gwt.tradeblotter.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    
    /**
     * {@link Comparator} for sorting by {@link TradeStatsStruc} ID
     */
    public final static class IdComparator implements Comparator<TradeStatsStruc> {
 
        private final boolean ascending;
 
        public IdComparator(boolean ascending) { this.ascending = ascending; }
 
        @Override
        public int compare(TradeStatsStruc t1, TradeStatsStruc t2) {
            final Long id1 = t1.id; 
            final Long id2 = t2.id; 
            if(ascending) { 
                return id1.compareTo(id2);
            } else { 
                return id2.compareTo(id1);
            }
        }
    }
    
    /**
	 * {@link Comparator} for sorting by {@link Message} TEXT
	 */
	public final static class TextComparator implements Comparator<TradeStatsStruc> {
				
		private final boolean ascending;
		
		public TextComparator(boolean ascending) { 
			this.ascending = ascending; 
		}
		
		@Override
		public int compare(TradeStatsStruc m1, TradeStatsStruc m2) {
			final String s1 = m1.itemName;// = m1.getText();
			final String s2 = m2.itemName;// = m2.getText();
			if(ascending) { 
				return s1.compareTo(s2);
			} else { 
				return s2.compareTo(s1);
			}
		}
	}
	
	/**
	 * {@link Comparator} for sorting by {@link Message} DATE
	 */
	public final static class DateComparator implements Comparator<TradeStatsStruc> {
		
		private final boolean ascending;
			
		public DateComparator(boolean ascending) { 
			this.ascending = ascending; 
		}

		@Override
		public int compare(TradeStatsStruc m1, TradeStatsStruc m2) {
			final Date d1 = new Date();// = m1.getDate();
			final Date d2 = new Date();// = m2.getDate();
		
			if(ascending) { 
				return d1.compareTo(d2);
			} else { 
				return d2.compareTo(d1);
			}
		}
	}
}