package net.dtkanov.lac.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Card {
	public Card(String s1, String s2) {
		side1 = s1;
		side2 = s2;
	}
	public Card() {
		this("", "");
	}
	public void swap() {
		String t = String.valueOf(side1);
		side1 = String.valueOf(side2);
		side2 = String.valueOf(t);
	}
	public String getSide1() {
		return side1;
	}
	public String getSide2() {
		return side2;
	}
	public String toString() {
		return "["+side1+"] - ["+side2+"]";
	}
	
	@XmlElement(name="side1")
	private String side1;
	@XmlElement(name="side2")
	private String side2;
}
