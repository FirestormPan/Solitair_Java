import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class TreeUninformedSearch {
	
	public static final int NumberOfGameStacks = 8;
	

	private TreeNode rootNode;
	
	public TreeNode getRootNode() {
		return rootNode;
	}
	
	public TreeUninformedSearch (Tablau startingTable) {	
		rootNode = new TreeNode(startingTable,null,0,0);
		rootNode.setShortCode()	;		
	}
	

	public TreeNode search(TreeNode currentNode) { 
		//παιρνει εναν κομβο και αρχίζει να επεκτείνει σε βάθος το δένδρο
		Tablau nodeTable = 	new Tablau(currentNode.getTable().getFreeCells(), currentNode.getTable().getGameStacksAll(), currentNode.getTable().getFoundationStacksAll()) ;
				
		boolean	solved = false;
		
		//ξεκινάω την αζήτηση απο το σημείο που την είχα αφησει
		int i = currentNode.getSourceIterator();
		int j = currentNode.getDestinationIterator();
	
		ArrayList<?> raisedCardSource = null;
		int raisedCardIndex=0;// το σε ποιο index θα τοποθετηθεί η καρτα(αναλογα με το ειδος του source
		
		
		boolean complete=false;	// εγινε ολοκληρομένη κινηση;	
		while(!solved) {	
			//Με το i που εχω απο την εισοδο παω να ελεγξω αν μπορώ να κανω κινηση. αν μπορω, την ελεγχω και την περνάω στο δενδρο. αν οχι παω σε επομενο i

			//πρωτα προσπαθώ να δωσω source και index σηκώνοντας απο FreeCell
			if(i<4) {
				if(nodeTable.getFreeCells().size() <= i) { //αν τα freeCells εχουνε λιγοτερα απο i στοιχεία, παμε παρακατω
					i++;
					continue;
				}
				raisedCardSource =  nodeTable.getFreeCells();
				raisedCardIndex = i;
			}
			//επειτα σηκώνοντας απο GameStack			
			else if(i<NumberOfGameStacks+4) {
				if(nodeTable.getGameStacksAll().get(i-4).isEmpty()) {
					i++;
					continue;	
				}
				raisedCardSource = nodeTable.getGameStacksAll();
				raisedCardIndex = i-4;				
			}	
			//τελος σηκώνοντας απο Foundation Stack
			else if(i<16 ) {
				if(nodeTable.getFoundationStacksAll().get(i-(NumberOfGameStacks+4)).isEmpty()) {
					i++;
					continue;	
				}
				raisedCardSource = nodeTable.getFoundationStacksAll();
				raisedCardIndex = i-(NumberOfGameStacks+4);			
			}
			else {//αν φτασω εδω σμαινει οτι δε μπορώ να σηκώσω καμια καρτα , κανοντας εγγυρη κινηση, αρα ειμαι σε αδιέξοδο
				//θα παω να επεκτείνω τον πατρικό κομβο αλλα αυξάνοντας το j , εκτως εαν ειναι ηδη 9 οποτε παμε στο επομενο i
				int newParentDestinationIterator = currentNode.getParent().getDestinationIterator() + 1;
				if(newParentDestinationIterator>9) {				
					currentNode.getParent().setSourceIterator(currentNode.getParent().getSourceIterator() +1);
					currentNode.getParent().setDestinationIterator(0);	
				}else {
					currentNode.getParent().setDestinationIterator(newParentDestinationIterator);
				}	
				
				return search(currentNode.getParent());
			}

			
			
			//παμε για τοποθέτηση
			if(j>9) { //ελεγχω οτι δεν μου δωθηκε j>9 στην αρχικοποιηση, αλλιως το παω 0 και του λεω να σηκώσει στην επόμενη καρτα 
				j=0;
				i++;
				continue;
			}

			//check if it can go at Foundations
			if(j==0) {
				complete=nodeTable.makeMove(raisedCardSource, raisedCardIndex, new FoundationStack('t')); 
				j++;
			}				
			//check if it can go at Gamestacks- αλλα OXI στο σημειο απο οπου την πήρα				
			while(j<NumberOfGameStacks+1 && !complete) { //εαν ειχε γίνει complete στο Foundation, δε θα μπει ποτε στον βρογχο
				if(j==i+3) {
					j++;
					continue;//OXI στο σημειο απο οπου την πήρα
				}
					
				complete = nodeTable.makeMove(raisedCardSource, raisedCardIndex, nodeTable.getGameStacksAll().get(j-1));
				j++;
			}
			//check if it can go at freecells
			if(j==9 && !complete) {
				if(raisedCardSource instanceof FreeCells) {  //δεν εχει νοηα να το παρω απο freecells και να το βαλω στα freeecells
					complete = nodeTable.makeMove(raisedCardSource, raisedCardIndex, nodeTable.getFreeCells());					
				}				
				j++;
			}
				
			if(complete) { //εαν το τοποθετησα 	
				TreeNode newborn = new TreeNode(nodeTable, currentNode, 0, 0); //φτιαχνω ενα νεο κόμβο στο δενδρο
				newborn.setShortCode();
				
				if(performTreeInsertionChecks(newborn)) {//και περασε τον ελεγχο για επαναληψεις
					
					//ελεγχος για αν είναι λυση
					solved = newborn.isSolution();
					if(solved) { 	//print solution
						System.out.println("solution");
						return newborn;
					}
					else { //αν δεν ειναι λυση , τον επεκτείνω
						System.out.println( newborn.getShortCode() );
						return	search(newborn);
					}
			
				}else {
					j++;
					continue;
				}				
					
			}
			//εαν δεν μπει ουτε εδω, σημαίνει οτι δεν μπορώ να κάνω καποια εγγυρη κίνηση με το συγκεκριμένο i, οποτε παω στο επόμενο i
			i++;
			j=0;			
		}
		
		System.out.println("process finished! good step~!  " + solved);
		return null;
	}
	
	
	public boolean performTreeInsertionChecks(TreeNode currentCheckingNode) {
		
		while(currentCheckingNode.getParent() != null) {
			if(currentCheckingNode.getShortCode().equals(currentCheckingNode.getParent().getShortCode()))
				return false;	
			else
				currentCheckingNode = currentCheckingNode.getParent();
		}
		
		return true;	
	}
	
	
	public void depthPrintSolution(TreeNode solution) { //incomplete
		if(!solution.isSolution()) {
			System.out.println(solution + " is not a solution Node");
			return;
		}
		
		
		TreeNode currentCheckingNode = solution;
		Stack<String> eggrafesLyshs = new Stack<String>(); 
		int k = 0;
		
		while(currentCheckingNode.getParent() != null) {
			k++;
			currentCheckingNode = currentCheckingNode.getParent();
		}
		
		
	}
	
	
	public void printparentalTree(TreeNode currentCheckingNode) { //useless
		while(currentCheckingNode.getParent() != null) {
			currentCheckingNode.getTable().printTablau();
			currentCheckingNode = currentCheckingNode.getParent();
		}
		
	}
	
	
	
}
