import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class readList {

	private String fileName;
	private int trophyCounts;
	private String[] trophyNames;
	private String gameTitle;
	private String gameId;
	
	public readList() {
		System.out.println("No input files!");
	}

	public readList(String fileName) {
		this.fileName = fileName;
		try {
			this.openFile();
		} catch (Exception e) {
			System.out.println("Open File failed!");
		}
	}

	private void openFile() throws Exception {

		File F = new File(this.fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //get the DOM parser's factory instance
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //get DOM parser
		Document doc = dBuilder.parse(F); 
		doc.getDocumentElement().normalize();

		NodeList trophyList = doc.getElementsByTagName("trophy"); //get trophy node nodelist

		trophyCounts = trophyList.getLength(); //total counts of trophies
		trophyNames = new String[trophyCounts]; 

		for (int i = 0; i < trophyList.getLength(); i++) {
			NodeList childNodes = trophyList.item(i).getChildNodes(); //get child node nodelist
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node trophyNameNode = childNodes.item(j); 
				if (trophyNameNode.getNodeName() == "name") //if child node is "name"
					trophyNames[i] = trophyNameNode.getTextContent(); //record the trophy name
			}
		}
		
		//get game title and game id
		
		NodeList gameName = doc.getElementsByTagName("title-name");
		
		for (int i = 0; i < gameName.getLength(); i++) {
			Node currentNode = gameName.item(i);
			gameTitle = currentNode.getTextContent();
		}
		
		NodeList npcommid = doc.getElementsByTagName("npcommid");
		
		for (int i = 0; i < npcommid.getLength(); i++) {
			Node currentNode = npcommid.item(i);
			gameId = currentNode.getTextContent();
		}
		
	}

	public int getTrophyCounts() {
		return this.trophyCounts;
	}

	public String[] gettrophyNames() {
		return this.trophyNames;
	}
	
	public String getGameName(){
		return this.gameTitle;
	}
	
	public String getGameId(){
		return this.gameId;
	}
	

}
