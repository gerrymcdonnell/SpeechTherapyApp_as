package com.example.speechtherapyapp;
/*basic speech therapy app setup feb 2015
 */




import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.example.speechtherapyapp.business.WordRecord;
import com.example.speechtherapyapp.business.WordRecordList;
import com.example.speechtherapyapp.settings.MyPrefs;
import com.example.speechtherapyapp.xml.XmlSaxHandler;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements OnInitListener{

	/*
	 * UI OBJECTS
	 */
	
	private ImageView imgView;
	private TextView txtViewLarge;
	private TextView txtViewSmall;

	private Button btnNewWord;
	
	private ProgressBar progressBar;
	
	private TextToSpeech tts;
	
	//create instanct of class which holds the words
	private WordRecordList wordList=new WordRecordList();
	
	// for when acivity settings is finshed we can reload the options
	private static final int RESULT_SETTINGS = 1;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//setup
		init_UI_Objects();
		
		//setup button handlers
		init_button_Handlers();
		
		//app settings
		load_Prefs();
		
		
		//open xml file
		//***************************************************************
		wordList.ReadAssetsXML(this,"testwords1.xml");
		//***************************************************************
		
		//text to speech
		tts = new TextToSpeech(this, this);
		
		
	}

	private void init_button_Handlers() {		
		
		//new word button
        btnNewWord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { 
            	
            	//open xml file
        		//ReadXML("testwords1.xml",wordList);
        		
        		//display word
            	displayWord();
            }
            
        });
        
		
	}

	/*create objets that exist on on screen*/
	private void init_UI_Objects() {
		imgView = (ImageView) findViewById(R.id.imgvPicture);
		txtViewLarge=(TextView)findViewById(R.id.txtWordLarge);
		txtViewSmall=(TextView)findViewById(R.id.txtWordSmall);
		
		btnNewWord = (Button) findViewById(R.id.btnNewWord);
		
		progressBar=(ProgressBar) findViewById(R.id.prgbProgress);
	}

	
	
	//display a word on screen
	void displayWord()
	{
		int n=0;
		WordRecord w=new WordRecord();
		
		try {
			
			//random for demo purposes
			//*****************************************************************************************
			w=wordList.getRandomWord();
			//*****************************************************************************************
			
			//w=wordList.getWord(n);
			
			//next word cycles back to start
			//w=wordList.getNextWord();
			
			//display on screen
			//*****************************************************************************************
			txtViewLarge.setText(w.getsWord());
			
			//show syllable hint
			if(MyPrefs.bDisplayWordSyllables==true)
				txtViewSmall.setText(w.getsSyllables());
			else
				txtViewSmall.setText("");
			
			
			//display Image
			if(MyPrefs.bDisplayImage==true)
				displayImage(w.getsImagePath());
			else
				imgView.setVisibility(View.INVISIBLE);
			
			//play sound file
			if(MyPrefs.bEnableAudioFilePlayback==true)
				playSoundFile(w.getsAudioFile());	
			
			
			//TTS
			if(MyPrefs.bEnableTTS==true)
				textToSpeech(w.getsWord());			
			
			
			//progress bar NOT WORKING
			//*****************************************************************************************			
			//setup progress bar
			setupProgressBar(wordList.size());
			
			//update progress bar
			setProgressBar(wordList.getiCurrentWordNum());
			//*****************************************************************************************	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	
	
	/*
	 * https://xjaphx.wordpress.com/2011/10/02/store-and-use-files-in-assets/
	 */
	void displayImage(String sFile){
		
		// get input stream
        InputStream ims;
        
        imgView.setVisibility(View.VISIBLE);
        
		try {
			//eg "images/yellow-submarine.jpg"
			ims = getAssets().open(sFile);			
	        // load image as Drawable
	        Drawable d = Drawable.createFromStream(ims, null);
	        // set image to ImageView
	        imgView.setImageDrawable(d);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*http://stackoverflow.com/questions/3289038/play-audio-file-from-the-assets-directory
	 * 
	 */
	//plays sound accociated with word
	void playSoundFile(String sFile){

	    try {
	    	
			AssetFileDescriptor afd = getAssets().openFd(sFile);
		    MediaPlayer mp = new MediaPlayer();
		    
	        mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
	        mp.prepare();
	        mp.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	
	
	
	//PROGRESS BAR
	void setupProgressBar(int iMax)	{
		progressBar.setMax(iMax);
	}
	
	
	//set the quiz word exercise progess i.e 2/10
	void setProgressBar(int n)	{
		progressBar.setProgress(n);
	}
	
	
	
	
	
	
	
	
	
	
	/*
	Android inherited methods
	 */	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id){
		
			case R.id.action_settings:
				Intent i = new Intent(this, UserSettingsActivity.class);			
				startActivityForResult(i, RESULT_SETTINGS);				
				break;
				
			case R.id.action_loadXML:
				//open xml file
				wordList.ReadAssetsXML(this,"testwords1.xml");
				break;
				
			case R.id.action_loadXMLFromWeb:
				//open xml file from web
				wordList.ReadXMLFromWeb(this,"http://www.irishbloke.net/tmp/testwords1.xml");
				break;
				
			
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	// when settings are done,reload the value
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case RESULT_SETTINGS:
				load_Prefs();
				break;
			}
	}
	
	
	
	//app settings
	private void load_Prefs() {
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		
		// most important one
		MyPrefs.bEnableAudioFilePlayback = getPrefs.getBoolean("enableAudioFilePlayBack", true);
		MyPrefs.bEnableTTS = getPrefs.getBoolean("enableTTS", false);
		
		//optional not importat
		MyPrefs.bDisplayImage=getPrefs.getBoolean("enableDisplayPictures", true);
		
		MyPrefs.bDisplayWordSyllables=getPrefs.getBoolean("enableWordSyllables", true);
		
	}
	
		
	
	
	
	
	//TTS setup
	@Override
	public void onInit(int code) {
		if (code == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.getDefault());
		} else {
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.",Toast.LENGTH_SHORT).show();
		}
	}
	
	//when app shut close tts
	@Override
	protected void onDestroy() {
		if (tts!=null) {
			tts.stop();
	    	  tts.shutdown();
		}
		super.onDestroy();
	}
	
	
	// handle screen orientation change
	@Override
	public void onConfigurationChanged(Configuration newConfig) {		
		super.onConfigurationChanged(newConfig);
		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	//speak the word
	private void textToSpeech(String s){
		if (tts!=null&&s!=null)     	                 
	    	  if (!tts.isSpeaking()) 
	    		  tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);	    	  		
	}	
	
	
	
}
