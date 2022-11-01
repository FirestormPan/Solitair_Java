import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Stack;


public class Main {

	public static final int NumberOfGameStacks = 8;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		ArrayList<GameStack> solitairGameStacks = ScanToGameStacks("sample1.txt");//args[0] ειναι η παράμεττρος που δινεται στο command line κατα την εκτέλεση του jar αρχείου
		
		Tablau myTable = new Tablau( new ArrayList<Card>(), solitairGameStacks, new ArrayList<FoundationStack>() );//παιρνω την δομή που εφτιαξα, σκαναρωντας το αρχειο, και την βαζω στο ταμπλο
		myTable.printTablau();
		
		
		 TreeUninformedSearch tree =new TreeUninformedSearch(myTable);

		 tree.search(tree.getRootNode());
		 
		
	}
	
	
	
	
	public static ArrayList<GameStack> ScanToGameStacks(String fileName){
//Παίρνει τα στοιχεία του αρχείου και φτιάχνει αντικειμενα Card τα οποια τα βαζει σε μια λιστα απο στοίβες.
//Λειτουργεί χωρις αλλαγές στον κωδικα, ανεξαρτήτως του μέγιστου χαρτιου (Ν)
//Επέλεξα αυτην την δομή διοτι αντιπροσωπεύει κάλιστα το ταμπλό(στην αρχική κατασταση)
		ArrayList<GameStack> GameStacks =  new ArrayList<GameStack>();
		for(int i=0;i<NumberOfGameStacks;i++)
			GameStacks.add(new GameStack());
		
		Scanner myReader= null;
		try {	    
			File myFile = new File(fileName);
			
		      myReader = new Scanner(myFile).useDelimiter("");
				
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		  int GameStackNumber=0; 
		  String nextEntry = "";
	      while (myReader.hasNext()) {
	    	 String data = myReader.next();
	    	 if(data.equals("\n")) {
	    		 GameStacks.get(GameStackNumber).push(new Card(nextEntry));
	    		 GameStackNumber++;
	    		 nextEntry="";
	    	 }else if(data.equals(" ")) {
	    		 GameStacks.get(GameStackNumber).push(new Card(nextEntry));
	    		 nextEntry="";
		      }
	    	 else if(!data.equals("\r")) {
	    		 nextEntry+=data;
	    	 } 

	     }
	    	
	      myReader.close();  

		return  GameStacks;
	} 
	
	
	private static void performFunctionalityTests(Tablau myTable) {

		//game to game
		myTable.makeMove(myTable.getGameStacksAll(), 4, myTable.getGameStacksAll().get(2));
		// game to game BAD
		myTable.makeMove(myTable.getGameStacksAll(), 0, myTable.getGameStacksAll().get(1));
		//Game to found
		myTable.makeMove(myTable.getGameStacksAll(), 0, myTable.getFoundationStacksAll().get(2));
		// Game to foud BAD
		myTable.makeMove(myTable.getGameStacksAll(), 4, myTable.getFoundationStacksAll().get(2));
		//Game to freecell
		myTable.makeMove(myTable.getGameStacksAll(), 3, myTable.getFreeCells());
		//Game to freecell BAD (last one is BAD)
		myTable.makeMove(myTable.getGameStacksAll(), 1, myTable.getFreeCells());
		myTable.makeMove(myTable.getGameStacksAll(), 1, myTable.getFreeCells());
		myTable.makeMove(myTable.getGameStacksAll(), 1, myTable.getFreeCells());
		myTable.makeMove(myTable.getGameStacksAll(), 1, myTable.getFreeCells());
		
	}
	
	

}
