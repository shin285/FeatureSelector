package kr.co.shineware.feature.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountedFeature {
	private Map<String,Integer> featureMap;
	
	public CountedFeature(){
		this.init();
	}

	private void init() {
		this.featureMap = new HashMap<String, Integer>();
	}
	public int getFreq(String key){
		Integer tf = featureMap.get(key);
		if(tf == null){
			return 0;
		}
		return tf;
	}
	public Set<String> getTerms(){
		return featureMap.keySet();
	}

	public void addFeature(String key,int value){
		this.featureMap.put(key, value);
	}
	
	public void incFeature(String key, int inc){
		Integer tf = featureMap.get(key);
		if(tf == null){
			tf = 0;
		}
		this.featureMap.put(key, tf+inc);
	}
}
