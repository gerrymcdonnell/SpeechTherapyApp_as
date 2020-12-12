package com.example.speechtherapyapp.business;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.example.speechtherapyapp.xml.XmlSaxHandler;

/*use this rather than an arraylist to store data about the current word exercise session
 * 
 */


public class WordRecordList {

	//store words here
	private ArrayList<WordRecord> wordList;	
	
	//value to store which word index were on
	private int iCurrentWordNum=0;
	
	
	
	public int getiCurrentWordNum() {
		return iCurrentWordNum;
	}

	public void setiCurrentWordNum(int iCurrentWordNum) {
		this.iCurrentWordNum = iCurrentWordNum;
	}



	//array of boolean vlaues that indicte correct tor wrong answers
	private ArrayList<Boolean> wordMarkings;
	
	
	public WordRecordList(){
		 wordList=new ArrayList<WordRecord>();
		 wordMarkings=new ArrayList<Boolean>();
	}
	
	
	public int size(){
		return wordList.size();
	}
	
	
	public void clear(){
		iCurrentWordNum=0;
		wordList.clear();
	}
	
	
	//get the next word
	public WordRecord getNextWord(){
		
		Log.v("nextword"," = "+iCurrentWordNum);
		Log.v("wordList.size()"," = "+wordList.size());
		
		if(iCurrentWordNum+1<=wordList.size()){
			iCurrentWordNum++;
			return wordList.get(iCurrentWordNum-1);
		}
		else{
			//end of word list, restart it
			iCurrentWordNum=0;
			return wordList.get(iCurrentWordNum-1);
		}
	}
	
	
	public WordRecord getRandomWord(){
		Random r = new Random();
		int n=0;
		
		n= randInt(0,wordList.size()-1);
		
		return wordList.get(n);
	}
	
	
	int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
	//get the word at index i
	public WordRecord getWord(int i){
		iCurrentWordNum=1;
		return wordList.get(i);
	}
	
	
	
	public int getTotalWords()	{
		return wordList.size();
	}
	
	//add word to arralist of words
	public void addWord(WordRecord w)
	{
		wordList.add(w);
	}
	
	//copy the contents of readin arraylist tothis arraylist
	public void setupWordList(ArrayList<WordRecord> mywordList)	{	
		
		this.wordList=mywordList;			
		//initialise the wordlist and word markings list
	}
	
		
	//getters and setters
	public void setCorrectAnswer(int i){
		wordMarkings.set(i, true);
	}
	
	public void setWrongAnswer(int i){
		wordMarkings.set(i, false);
	}
	
	
	
	//reads in an xml file of words for the exercise from the assets folder
	public void ReadAssetsXML(Context ctx,String sFile) {
		try {

				AssetManager assetManager = ctx.getAssets();
				InputStream is = assetManager.open(sFile);				
				
				//reads from the device external location
				//FileInputStream is = new FileInputStream(sFile);					

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				XmlSaxHandler myXMLHandler = new XmlSaxHandler();
				xr.setContentHandler(myXMLHandler);
				InputSource inStream = new InputSource(is);
				xr.parse(inStream);

				//set the arraylist	
				wordList.clear();			
				
				wordList = (ArrayList<WordRecord>) myXMLHandler.getList();			
				
				//process input
				int x=0;
				for (WordRecord word : wordList) {		
					x++;
					Log.v("xml read", word.getsWord());
				}						

				//Toast.makeText(this, "XML File Loaded with " + x +" words", Toast.LENGTH_SHORT).show();
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.v("error", "xml file");
			}
		}
	
	
	//reads in an xml file of words for the exercise from the assets folder
	public void ReadXMLFromWeb(Context ctx,String sFile) {
		try {

				//AssetManager assetManager = ctx.getAssets();
				//InputStream is = assetManager.open(sFile);				
				
				//reads from the device external location
				FileInputStream is = new FileInputStream(sFile);					

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				XmlSaxHandler myXMLHandler = new XmlSaxHandler();
				xr.setContentHandler(myXMLHandler);
				InputSource inStream = new InputSource(is);
				xr.parse(inStream);

				//set the arraylist	
				wordList.clear();			
				
				wordList = (ArrayList<WordRecord>) myXMLHandler.getList();			
				
				//process input
				int x=0;
				for (WordRecord word : wordList) {		
					x++;
					Log.v("xml read", word.getsWord());
				}						

				//Toast.makeText(this, "XML File Loaded with " + x +" words", Toast.LENGTH_SHORT).show();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				Log.v("errorxml from web", e.getMessage());
			}
		}

	
	
}
