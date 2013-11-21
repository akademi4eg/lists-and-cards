package net.dtkanov.lac.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import net.dtkanov.lac.gui.MainWindow;

public class AppCore {
	private AppCore() {
		try {
			strings = ResourceBundle.getBundle("i18n.Strings", curLocale);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			strings = ResourceBundle.getBundle("i18n.Strings", new Locale("en", "US"));
		}
		currList = CardsList.load(new File("list.lcl"));
		langs = new HashMap<String, String>();
		readFileToHashMap(LANGS_LIST_FILENAME, langs);
		lists = new ArrayList<CardsList>();
		reloadLists();
	}
	
	/**
	 * Launches app.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			curLocale = new Locale(args[0], args[1]);
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mw = new MainWindow();
				mw.setVisible(true);
			}
		});
	}
	
	public void reloadLists() {
		lists.clear();
		File[] files = new File(LISTS_LOCATION).listFiles();
		addLists(files);
	}
	
	public void addLists(File[] files) {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            addLists(file.listFiles());
	        } else {
	        	if (file.getName().endsWith(".lcl"))
	        		lists.add(CardsList.load(file));
	        }
	    }
	}
	
	public static AppCore getInstance() {
		if (instance == null)
			instance = new AppCore();
		return instance;
	}
	
	public CardsList getCurrentList() {
		return currList;
	}
	
	public void setCurrentList(File f) {
		setCurrentList(CardsList.load(f));
	}
	
	public void setCurrentList(int idx) {
		if (idx >= 0 && idx < lists.size())
			setCurrentList(lists.get(idx));
	}
	
	public void setCurrentList(CardsList cl) {
		currList = cl;
	}
	
	public ArrayList<CardsList> getLists() {
		return lists;
	}
	
	public String getLangFromCode(String code) {
		if (langs.containsKey(code))
			return langs.get(code);
		
		return code;
	}
	
	public String getCodeForLang(String lang) {
		for (Map.Entry<String, String> entry : langs.entrySet()) {
	        if (lang.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		
		return lang;
	}
	
	public Vector<String> getLangs() {
		return new Vector<String>(langs.values());
	}
	
	public String getString(String key) {
		try {
			return new String(strings.getString(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return strings.getString(key);
		}
	}
	
	public static void readFileToHashMap(String filename, HashMap<String, String> map) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
	        String line = "";
	        while ((line = in.readLine()) != null) {
	            String parts[] = line.split("\\|");
	            map.put(parts[0], parts[1]);
	        }
	        in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final String VERSION = "0.5.0";
	private static AppCore instance = null;
	private CardsList currList;
	private ArrayList<CardsList> lists;
	private HashMap<String, String> langs;
	private ResourceBundle strings;
	public static final String LANGS_LIST_FILENAME = "langs.lst";
	public static final String LISTS_LOCATION = "lists";
	public static Locale curLocale = Locale.getDefault();
}
