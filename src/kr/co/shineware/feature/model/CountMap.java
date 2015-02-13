package kr.co.shineware.feature.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountMap {
private Map<String,Integer> keyFreqMap;
	
	public CountMap(){
		this.init();
	}

	private void init() {
		this.keyFreqMap = new HashMap<String, Integer>();
	}
	public int getFreq(String key){
		Integer tf = keyFreqMap.get(key);
		if(tf == null){
			return 0;
		}
		return tf;
	}
	public Set<String> getKeys(){
		return keyFreqMap.keySet();
	}

	public void addKey(String key,int value){
		this.keyFreqMap.put(key, value);
	}
	public void inc(String key){
		this.inc(key, 1);
	}
	
	public void inc(String key, int inc){
		Integer tf = keyFreqMap.get(key);
		if(tf == null){
			tf = 0;
		}
		this.keyFreqMap.put(key, tf+inc);
	}
}
