package kr.co.shineware.feature.test;

import java.io.File;

import kr.co.shineware.feature.model.CountedFeature;
import kr.co.shineware.feature.model.LabeledFeature;
import kr.co.shineware.feature.selection.FeatureSelector;

public class FeatureSelectorTest {

	public static void main(String[] args) throws Exception {
		FeatureSelector selector = new FeatureSelector();
//		LabeledFeature feature = convert();
		HKIBParser parser = new HKIBParser();
		parser.setDoc(new File("HKIB20000/HKIB-20000_005.txt"));
		HKIBDoc doc = null;
		while((doc = parser.next()) != null){
			LabeledFeature lf = new LabeledFeature();
			lf.setLabel(doc.getCategory());
			CountedFeature cf = new CountedFeature();
			String text = doc.getText();
			String[] tokens = text.split("\\s");
			for (String token : tokens) {
				cf.incFeature(token, 1);
			}
			lf.setFeature(cf);
			selector.addFeature(lf);
		}
		System.out.println("done to load");
		selector.chisquare();
	}
}
