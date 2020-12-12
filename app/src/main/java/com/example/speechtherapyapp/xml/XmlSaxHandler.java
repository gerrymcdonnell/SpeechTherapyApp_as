package com.example.speechtherapyapp.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.speechtherapyapp.business.WordRecord;

public class XmlSaxHandler extends DefaultHandler{


	boolean currentElement = false;
	String currentValue = "";

	
	WordRecord wordrecord;
	ArrayList<WordRecord> wordList;

	
	public List<WordRecord> getList() {
		return wordList;
	}

	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (qName.equals("words")) {
			wordList = new ArrayList<WordRecord>();
		} else if (qName.equals("word")) {
			wordrecord = new WordRecord();
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (qName.equalsIgnoreCase("wordtitle"))
			wordrecord.setsWord(currentValue.trim());
		
		//syllables
		//files assocaited with word
		else if (qName.equalsIgnoreCase("syllables"))
			wordrecord.setsSyllables(currentValue.trim());
		
		//files assocaited with word
		else if (qName.equalsIgnoreCase("picturepath"))
			wordrecord.setsImagePath(currentValue.trim());
		
		else if (qName.equalsIgnoreCase("audiofile"))
			wordrecord.setsAudioFile(currentValue.trim());
		
		else if (qName.equalsIgnoreCase("word"))
			   wordList.add(wordrecord);
		
		currentValue = "";
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}

}



