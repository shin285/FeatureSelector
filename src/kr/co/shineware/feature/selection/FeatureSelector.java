package kr.co.shineware.feature.selection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.feature.model.CountMap;
import kr.co.shineware.feature.model.CountedFeature;
import kr.co.shineware.feature.model.LabeledFeature;
import kr.co.shineware.util.common.collection.MapUtil;
import kr.co.shineware.util.common.file.FileUtil;

public class FeatureSelector {
	private CountMap tfMap; //term freq.
	private CountMap dfMap; //document freq.
	private CountMap cfMap; //category freq.
	private CountMap cofMap; //co-occurrence freq.

	public FeatureSelector(){
		this.tfMap = new CountMap();
		this.dfMap = new CountMap();
		this.cfMap = new CountMap();
		this.cofMap = new CountMap();
	}

	public void addFeature(LabeledFeature data){
		cfMap.inc(data.getLabel()); //increment category frequency
		CountedFeature features = data.getFeature();
		Set<String> terms = features.getTerms();
		for (String term : terms) {
			int freq = features.getFreq(term);
			tfMap.inc(term,freq); //increment term frequency
			dfMap.inc(term); //increment document frequency
			cofMap.inc(this.convertCooccurrenceKey(term,data.getLabel()));
		}
	}

	private String convertCooccurrenceKey(String term,String label){
		return String.format("%s_%s", term,label);
	}

	public void chisquare(){

		Set<String> labels = cfMap.getKeys();
		Set<String> terms = dfMap.getKeys();
		int n = this.getTotalDocument(); //N
		int count = 0;
		for (String label : labels) {
			
			int categoryFreq = this.getCategoryFreq(label); //A+C
			Map<String,Double> selectedTermScoreMap = new HashMap<>();

			for (String term : terms) {
				int df = this.getDf(term); //A+B
				int cooccurFreq = this.getCooccurrenceFreq(this.convertCooccurrenceKey(term, label)); //A
				if(cooccurFreq < 1){
					continue;
				}
				double score = this.getChisqrScore(n,df,categoryFreq,cooccurFreq);
				selectedTermScoreMap.put(term, score);
			}
			selectedTermScoreMap = MapUtil.sortByValue(selectedTermScoreMap, MapUtil.DESCENDING_ORDER);
			FileUtil.writeMap(selectedTermScoreMap, "features"+File.separator+label.replace("/", "_")+".txt");
			count++;
			System.out.println("Label Done "+count+"/"+labels.size()+" : "+label);
		}
	}

	public String getLowestTerm(
			Map<String, Double> selectedTermScoreMap) {
		Set<Entry<String,Double>> entrySet = selectedTermScoreMap.entrySet();
		String lowestTerm = null;
		double lowestScore = Double.MAX_VALUE;
		for (Entry<String, Double> entry : entrySet) {
			if(entry.getValue() < lowestScore){
				lowestTerm = entry.getKey();
				lowestScore = entry.getValue();
			}
		}
		return lowestTerm;
	}

	private double getChisqrScore(int n, int df, int categoryFreq, int cooccurFreq) {
		double normalizer = (double)categoryFreq*(n-categoryFreq)*df*(n-df);
		int a = cooccurFreq;
		int b = df-a;
		int c = categoryFreq-a;
		int d = n-(a+b+c);
		if(a < 0){
			System.out.println("a : "+a);
		}
		if(b < 0){
			System.out.println("b : "+b);
		}
		if(c < 0){
			System.out.println("c : "+c);
		}
		if(d < 0){
			System.out.println("d : "+d);
		}
		if(normalizer < 0){
			System.out.println("nor : "+normalizer);
			System.out.println(categoryFreq);
			System.out.println(n-categoryFreq);
			System.out.println(df);
			System.out.println(n-df);
		}
		double chi = n*Math.pow((double)a*d-(double)c*b, 2);
		
		return chi/normalizer;
	}

	private int getCooccurrenceFreq(String key) {
		return this.cofMap.getFreq(key);
	}

	private int getDf(String term) {
		return this.dfMap.getFreq(term);
	}

	private int getCategoryFreq(String label) {
		return this.cfMap.getFreq(label);
	}

	private int getTotalDocument() {
		Set<String> labels = cfMap.getKeys();
		int totalCount = 0;
		for (String label : labels) {
			totalCount += cfMap.getFreq(label);
		}
		return totalCount;
	}
}
