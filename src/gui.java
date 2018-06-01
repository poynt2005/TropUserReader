import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*; 
import java.util.regex.*;

public class gui extends JFrame {
	private static final long serialVersionUID = 1L;	
	
	/* paths String
	 * picFiles containes trophies' pic
	 * datPath : .dat file absolute path
	 * xmlPath : .sfm file absulute path
	 * dirPath : trophy directory absulute path
	 */
	private String[] picFiles;
	private String datPath;
	private String xmlPath;
	private String dirPath;
	
	
	private datParser datFile;
	private readList trophyInfo;
	
	//check if there is a trophy dirctory has loaded (init to false)
	private Boolean hasContent; 
	
	//map the trophy id and it's checkbox
	private Map<Integer, JCheckBox> checkBoxMap = new HashMap<Integer, JCheckBox>();
	
	
	public gui(){
		super("TrophyUsr Parse");
		getContentPane().setBackground(Color.WHITE);
		
		//there is no data loaded
		hasContent = false;
		
		//select all button (if there is no data set to invisible)
		JButton selectAllBtn = new JButton("Select All");
		selectAllBtn.setVisible(false);
		
		//select directory button
		JButton selectFile = new JButton("Select Directory");
		
		//trophy infomation Panel
		JPanel trophyPanel = new JPanel();
		trophyPanel.setLayout(new GridBagLayout());
		trophyPanel.setMinimumSize(new Dimension(600,500));
		
		//let trophy infomation Panel scrollable
		JScrollPane scrPane = new JScrollPane(trophyPanel);	
		
		
		selectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	//file chooser 
            	JFileChooser fc = new JFileChooser();
            	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            	fc.setDialogTitle("Select trophy directory");
            	fc.showOpenDialog(null);
            	
            	
            	try{
            		
            		processFile(fc.getSelectedFile());
            		
            		//load file data to "datParser" and "readList" instance
            		fileInit();
            		
            		//clear old data if there is a new trophy directory selected(checkBox mapping , trophy panel , trophy panel scrollbar)
            		checkBoxMap.clear();
            		trophyPanel.removeAll();
            		if(hasContent)
            			getContentPane().remove(scrPane);
            		
            		//vision the select all button
            		selectAllBtn.setVisible(true);
            		
            		//set new title to game infomation
            		String newTitle = new String(String.format("%s(%s); Rate: %.2f%%" , trophyInfo.getGameName() , trophyInfo.getGameId() , ((float)datFile.getUnlockedTrophies() / (float)trophyInfo.getTrophyCounts())*100));
            		setTitle(newTitle);
            		
            		//fileInit() has load the files
            		hasContent = true;
            		/**/
            		
            		final int trophyTotalCounts = trophyInfo.getTrophyCounts();
            		
            		for(int i = 0 ; i< trophyTotalCounts; i++){

            			//row panel unit contains "trophy pic" , "trophy name and unlocked/locked" , "checkbox" 
            			JPanel eachTrophyPanel = new JPanel(new BorderLayout());	
            			
            			//get images from png files and scale them to smaller size
            			ImageIcon trophyImg = new ImageIcon(picFiles[i]);
            			Image tmpImg = trophyImg.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);; 
            			trophyImg = new ImageIcon(tmpImg);
            			/**/
            			
            			//set border to trophy picture 
            			JLabel trophyPic = new JLabel(trophyImg);
            			trophyPic.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
            			/**/
            			
            			String trophyStatus = "";
            			if(datFile.getTrophySituation()[i] == true) trophyStatus="Unlocked";
            			else trophyStatus = "Locked";
            			
            			//trophy name and locked/unlocked
            			JLabel trophyName = new JLabel("<html>Trophy Name is : " + trophyInfo.gettrophyNames()[i] +"<br>This trophy is : " + trophyStatus + "</html>");
            			trophyName.setMinimumSize(new Dimension(200,30));
            			trophyName.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
            			
            			
            			//select checkbox to unlock
            			JCheckBox checkUnlocked = new JCheckBox("check");
            			checkBoxMap.put(i, checkUnlocked);
            			
            			
            			//add to row panel unit
            			eachTrophyPanel.add(trophyPic , BorderLayout.WEST);
            			eachTrophyPanel.add(trophyName , BorderLayout.CENTER);
            			eachTrophyPanel.add(checkUnlocked , BorderLayout.EAST);
            			eachTrophyPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            			/**/
            			
            			GridBagConstraints gbc = new GridBagConstraints();
            			gbc.gridx = 0;
            			gbc.gridy = i;
            			gbc.fill = gbc.HORIZONTAL;
            			trophyPanel.add(eachTrophyPanel, gbc);
            		}
            		
            		//update when new trophy directory is selected
            		trophyPanel.revalidate();
            		scrPane.revalidate();
            		add(scrPane , BorderLayout.CENTER);
            		revalidate();
            		
            	}
            	catch(IOException ioerror){
            		JOptionPane.showMessageDialog(null , "Open directory Failed!\nProbably it's not a trophy directory...");
            	}
            }
        });
		
		
		JButton unlockBtn = new JButton("Unlock Selected");
		unlockBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
            		final int totalTrophyCounts = trophyInfo.getTrophyCounts();
            		
            		//get ids of trophy perfer to unlock
            		ArrayList<Integer> checkToUnlockList = new ArrayList<>();            	
            		for(int i = 0 ; i<totalTrophyCounts ; i++){
            			if(checkBoxMap.get(i).isSelected())
            				checkToUnlockList.add(i);
            		}
            		
            		//convert to checkToUnlockList interger array
            		int[] checkToUnlock = new int[checkToUnlockList.size()];
            		for(int i = 0 ; i<checkToUnlockList.size() ; i++)
            			checkToUnlock[i] = checkToUnlockList.get(i);
            		/**/
            		
            		
            		if(totalTrophyCounts == checkToUnlockList.size())
            			datFile.unlockTrophy(checkToUnlock , true , dirPath);
            		else
            			datFile.unlockTrophy(checkToUnlock , false , dirPath);    
            		
            		JOptionPane.showMessageDialog(null , "\"TROPUSR.DAT_Mod\" has created");
            	}
            	catch(Exception EE){
            		JOptionPane.showMessageDialog(null , "Select trophy failed , maybe you didn't select any trophy...");
            	}
            }
        });
		
		//select all checkbox
		selectAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	for(int i = 0 ; i < trophyInfo.getTrophyCounts(); i++)
            		checkBoxMap.get(i).setSelected(true);
            }
        });
		
		
		//add button to the button panel at the south od the JFrame
		JPanel selectFilePanel = new JPanel();	
		selectFilePanel.add(selectFile);
		selectFilePanel.add(unlockBtn);
		selectFilePanel.add(selectAllBtn);   
		add(selectFilePanel , BorderLayout.SOUTH);
	}
	
	private void processFile(File f) throws IOException{
		
		datPath = xmlPath = "";
		File[] directoryFiles = f.listFiles();
		
		
		// trophy pic files is always 3 less than all file (files except game cover , .sfm , .dat) 
		if(directoryFiles.length-3 <= 0)
			throw new IOException("It's not a vailed trophy directory");
		picFiles = new String[directoryFiles.length-3];
		
		
		String trophyXML = new String("");
		String trophyDat = new String("");
		for(int i = 0 ; i<directoryFiles.length ; i++)
			if(directoryFiles[i].isFile()){
				
				//get trophy id from pictures' file name
				String fileName = directoryFiles[i].getName();
				Pattern p = Pattern.compile("[0-9]{3}");
			    Matcher m = p.matcher(fileName);
			    if(m.find()){
			    	try{
			    		picFiles[Integer.parseInt(m.group(0))] = directoryFiles[i].getAbsolutePath();
			    	}
			    	catch(Exception ee){
			    	}
			    }
			    
			    //get .dat and .sfm file absolute path
			    if(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase().equals("DAT"))
			    	trophyDat = directoryFiles[i].getAbsolutePath();
			    else if(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase().equals("SFM"))
			    	trophyXML = directoryFiles[i].getAbsolutePath();
			}
		datPath = trophyDat;
		xmlPath = trophyXML;
		dirPath = f.getAbsolutePath();
		
		//a vaild trophy directory always contains these two files
		if(datPath.isEmpty() || xmlPath.isEmpty())
			throw new IOException("It's not a vailed trophy directory");
		return;
	}
	
	private void fileInit(){
		readDat rd = new readDat(datPath);
		trophyInfo = new readList(xmlPath);
		datFile = new datParser(rd , trophyInfo);
	}
}
