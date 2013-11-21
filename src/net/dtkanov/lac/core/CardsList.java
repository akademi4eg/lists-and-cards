package net.dtkanov.lac.core;

import java.io.File;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardsList {
	public static CardsList load(File f) {
		CardsList cl = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(CardsList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			cl = (CardsList) jaxbUnmarshaller.unmarshal(f);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cl;
	}
	public static void save(CardsList cl, File f) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(CardsList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(cl, f);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void swap() {
		String t = String.valueOf(lang1);
		lang1 = String.valueOf(lang2);
		lang2 = String.valueOf(t);
		for (Card c : cards)
			c.swap();
	}
	public String getLang1() {
		return lang1;
	}
	public String getLang2() {
		return lang2;
	}
	public void randomize() {
		Collections.shuffle(cards);
	}
	public int getSize() {
		return cards.size();
	}
	public Card getCard(int num) {
		return cards.get(num);
	}
	public long getID() {
		return id;
	}
	public Card first() {
		currCard = 0;
		return getCard(0);
	}
	public Card last() {
		currCard = getSize()-1;
		return getCard(getSize()-1);
	}
	public Card next() throws NoCardException {
		currCard++;
		if (currCard >= getSize() || currCard < 0)
			throw new NoCardException();
		
		return getCard(currCard);
	}
	public Card prev() throws NoCardException {
		currCard--;
		if (currCard >= getSize() || currCard < 0)
			throw new NoCardException();
		
		return getCard(currCard);
	}
	public Card setCurrCard(int idx) throws NoCardException {
		currCard = idx;
		if (currCard >= getSize() || currCard < 0)
			throw new NoCardException();
		
		return getCard(currCard);
	}
	public int getCurrCardIdx() {
		return currCard;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return "["+getLang1()+"-"+getLang2()+"] "+getName()+", карточек: "+getSize();
	}
	public void setName(String n) {
		name = n;
	}
	public void setLang1(String code) {
		lang1 = code;
	}
	public void setLang2(String code) {
		lang2 = code;
	}
	public void addCard(Card c) {
		cards.add(c);
	}
	public void addCard(String s1, String s2) {
		cards.add(new Card(s1, s2));
	}
	public void setID(int id) {
		this.id = id;
	}
	public long generateID() {
		id = (long)Math.random()*Long.MAX_VALUE;
		return id;
	}
	public List<Card> getCards() {
		return cards;
	}
	
	@XmlAttribute(name="lang1")
	private String lang1;
	@XmlAttribute(name="lang2")
	private String lang2;
	@XmlAttribute(name="id")
	private long id;
	@XmlElement(name="name")
	private String name;
	private int currCard;
	
	@XmlElement(name="card")
	private List<Card> cards = new ArrayList<Card>();
}
