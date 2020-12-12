package com.example.speechtherapyapp.business;

public class WordRecord {
	
	private String sWord="";
	private String  sSyllables="";
	
	/* FILES ACCOSIATED WITH THIS WORD
	 */
	//are images local? inc with app or downloaded or ???
	private String sImagePath="";
	
	//audio file associated with this word
	private String sAudioFile="";
	
	/*
	 * ADD EXTRA IDEA FIELDS HERE
	 */
	
	private int iDiffculty=0;

	
	public WordRecord() {
	}
	
	
	public WordRecord(String sWord) {
		super();
		this.sWord = sWord;
	}
	
	
	public WordRecord(String sWord,String sSyllables) {
		super();		
		this.sWord = sWord;
		this.sSyllables=sSyllables;		
	}
	
	
	public WordRecord(String sWord, String sSyllables,String sImagePath,String sAudioFile) {
		super();
		this.sWord = sWord;
		this.sSyllables=sSyllables;	
		
		this.sImagePath = sImagePath;		
		this.sAudioFile=sAudioFile;
	}


	
	
	
	public String getsSyllables() {
		return sSyllables;
	}


	public void setsSyllables(String sSyllables) {
		this.sSyllables = sSyllables;
	}


	public String getsAudioFile() {
		return sAudioFile;
	}


	public void setsAudioFile(String sAudioFile) {
		this.sAudioFile = sAudioFile;
	}


	//getters and setters	
	public String getsWord() {
		return sWord;
	}


	public void setsWord(String sWord) {
		this.sWord = sWord;
	}


	public String getsImagePath() {
		return sImagePath;
	}


	public void setsImagePath(String sImagePath) {
		this.sImagePath = sImagePath;
	}
	
	
	
	
	

}
