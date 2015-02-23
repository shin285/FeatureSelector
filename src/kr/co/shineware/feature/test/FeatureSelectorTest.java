package kr.co.shineware.feature.test;

import java.io.File;
import java.util.List;

import kr.co.shineware.feature.model.CountedFeature;
import kr.co.shineware.feature.model.LabeledFeature;
import kr.co.shineware.feature.selection.FeatureSelector;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class FeatureSelectorTest {

	public static void main(String[] args) throws Exception {
		Komoran komoran = new Komoran("models-light");
		FeatureSelector selector = new FeatureSelector();
		HKIBParser parser = new HKIBParser();
		parser.setDoc(new File("HKIB20000/HKIB-20000_005.txt"));
		HKIBDoc doc = null;
		while((doc = parser.next()) != null){
			LabeledFeature lf = new LabeledFeature();
			lf.setLabel(doc.getCategory());
			CountedFeature cf = new CountedFeature();
			String text = doc.getText().replaceAll("\n", " ");
			
			@SuppressWarnings("unchecked")
			List<List<Pair<String,String>>> results = komoran.analyze(text);
			for (List<Pair<String, String>> result : results) {
				for (Pair<String, String> pair : result) {
					if(pair.getSecond().startsWith("N")){
						cf.incFeature(pair.getFirst()+"/"+pair.getSecond(), 1);
					}
				}
			}

			lf.setFeature(cf);
			selector.addFeature(lf);
		}
		System.out.println("done to load");
		selector.chisquare();
	}
}
