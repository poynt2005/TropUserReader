import java.io.*;
import java.nio.file.*;

public class datParser {
	
	private readDat datFile; //contain .dat binary buffer
	private readList trophyList; //contain trophy list and trophy count
	
	private Boolean[] trophySituation; //record the trophy is unlocked or not
	private int[] trophyLockPos; //record the position of .dat binary buffer
	
	public datParser(){
		System.out.println("Need both dat file and xml file!!");
	}
	public datParser(readDat rd , readList rl){
		this.datFile = rd;
		this.trophyList = rl;
		this.parseByte(); 
	}
	
	private void parseByte(){
		byte[] inputData = datFile.getFileData();
		int trophyTotalNum = trophyList.getTrophyCounts(); //total count of trophy
		
		trophySituation = new Boolean[trophyTotalNum];
		trophyLockPos = new int[trophyTotalNum];
		
		for(int i = 0 ; i<inputData.length ; i++){
			int trophyCheckPosition = (i+1) % 16; //get the column position
			if(trophyCheckPosition == 4) //if data is located at the 4th column
				if(inputData[i] == 6) //data equal to (Hex)06 
					if(inputData[i + 4] == 96) // if data at the 8th column in the same row equal to (Hex)60 indicates that the row below this row probably is the trophy id and locked/unlocked located at 
						if(inputData[i + 16] >= 0 && inputData[i + 16] < trophyTotalNum){ //check if the trophy is in the range of total counts of trophies
							if(inputData[i + 20] == 0){ 
								trophySituation[inputData[i + 16]] = false; //set to false if not unlocked
							}
							else if(inputData[i + 20] == 1){
								trophySituation[inputData[i + 16]] = true;
							}
							else
								continue; 
							trophyLockPos[inputData[i + 16]] = i + 20; //record the trophy position
						}	
		}
	}
	
	public void unlockTrophy(int[] trophyId , Boolean unLockAll) throws IOException{ //unlock specific trophy
		byte[] outputData = datFile.getFileData();
		
		if(!unLockAll)
			for(int i = 0 ; i<trophyId.length ; i++)
				outputData[trophyLockPos[trophyId[i]]] = 1;
		else
			for(int i = 0 ; i<trophyLockPos.length ; i++)
				outputData[trophyLockPos[i]] = 1;
		
		Files.write(Paths.get("TROPUSR.DAT_Mod"), outputData); //write new .dat file
	}
	
	public void unlockTrophy(int[] trophyId , Boolean unLockAll , String dirPath) throws IOException{ //unlock specific trophy (add directory path param)
		byte[] outputData = datFile.getFileData();
		
		if(!unLockAll)
			for(int i = 0 ; i<trophyId.length ; i++)
				outputData[trophyLockPos[trophyId[i]]] = 1;
		else
			for(int i = 0 ; i<trophyLockPos.length ; i++)
				outputData[trophyLockPos[i]] = 1;
		
		Files.write(Paths.get(dirPath , "/TROPUSR.DAT_Mod"), outputData); //write new .dat file
	}
	
	public Boolean[] getTrophySituation(){
		return trophySituation;
	}
	public int[] gettrophyLockPos(){
		return trophyLockPos;
	}
	public int getUnlockedTrophies(){ //return how many trophoes have unlocked
		int count = 0;
		for(int i = 0 ; i<trophySituation.length; i++)
			if(trophySituation[i] == true)
				count++;
		return count;
	}
}
