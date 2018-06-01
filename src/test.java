import java.io.IOException;

import javax.swing.JFrame;

public class test {
	public static void main(String args[]) throws Exception{
		/*readDat rd1 = new readDat("TROPUSR.DAT");
		readList rL1 = new readList("TROPCONF.SFM");
		
		datParser dP1 = new datParser(rd1 ,rL1);
		int tropyUnlockedCount = 0;
		
		for(int i = 0 ; i < dP1.getTrophySituation().length ; i++){
			String n;
			if(dP1.getTrophySituation()[i] == true) {n="Unlocked";tropyUnlockedCount++;}
			else n = "Locked";
			
			System.out.printf("Trophy id is : %d , name is : %s and Thropy is %s \n" , i , rL1.gettrophyNames()[i] , n);
		}
		
		System.out.printf("Total trophies : %d , You Unlocked : %d , Unlock Rate is : %.2f%%" , rL1.getTrophyCounts() , tropyUnlockedCount , (float)tropyUnlockedCount/(float)rL1.getTrophyCounts()*100);
		
		int[] a = new int[]{1,2,3};
		try{
			dP1.unlockTrophy(a ,true);
		}
		catch(IOException e){
			
		}*/
		
		gui g1 = new gui();
		g1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g1.setSize(500,800);
		g1.setResizable(false);
		g1.setVisible(true);
		
	} //end main
}
