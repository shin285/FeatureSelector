package kr.co.shineware.feature.model;

public class LabeledFeature {
	private String label;
	private CountedFeature feature;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public CountedFeature getFeature() {
		return feature;
	}
	public void setFeature(CountedFeature feature) {
		this.feature = feature;
	}
}
