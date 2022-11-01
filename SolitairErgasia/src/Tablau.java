import java.util.*;

public class Tablau {
	
	private ArrayList<GameStack> GameStacksAll = new ArrayList<GameStack>();
	private ArrayList<FoundationStack> FoundationStacksAll= new ArrayList<FoundationStack>();
	private ArrayList<Card> freeCells;

	
	public Tablau(ArrayList<Card> initialFreeCells, ArrayList<GameStack> initialGameStacks, ArrayList<FoundationStack> initialFoundationStacks) {
		
		//initializing the freecells		
		freeCells = new FreeCells();
		for(Card card : initialFreeCells) //ειναι ενταξει τωρα
			freeCells.add(card);
		

		//initialize Game Stacks(την παρτιδα οπως εκκινειται, 8 στοιβες με τις καρτες μεσα)
		int index=0;
		for(GameStack gamestack : initialGameStacks) {
			gamestack =  (GameStack) initialGameStacks.get(index).clone();
			GameStacksAll.add(gamestack) ;
			index++;
		}


		//initialize foundation Stacks
		if(initialFoundationStacks.isEmpty()) {
			//initialize foundation Stacks, φτιαχνω 4 Fοundation stacks με ονοματα για ολα τα σετ
			char[] setNames = {'C','D','H','S'};
			for (char set : setNames) {
				FoundationStacksAll.add(new FoundationStack(set));
			}
		}else {
			int index2=0;
			for(FoundationStack stack : initialFoundationStacks) {
				stack = (FoundationStack) initialFoundationStacks.get(index2).clone();
				FoundationStacksAll.add(stack) ;
				index2++;
			}
			
		}
			
		

	}

	
	public ArrayList<Card> getFreeCells() {
		return freeCells;
	}

	public ArrayList<GameStack> getGameStacksAll() {
		return GameStacksAll;
	}

	public ArrayList<FoundationStack> getFoundationStacksAll() {
		return FoundationStacksAll;
	}

	
	
	public Card raiseCard(ArrayList<?> source,int sourceIndex) {
		//αν προσπαθώ να σηκώσω καρτα απο στοιβα ή foundation, επιστρεφει την πρωτη(pop), απο την  στοιβα αριμου index (α/α).
		//αν προσπαθώ να σηκώσω καρτα απο freecel, επιστρεφει την κάρτα θεσης index.
		//ΠΡΟΣΟΧΗ!! σε καθε περιπτωση μπορει να επιστραφεί null, δηλαδη ηταν αδειο το σημειο απο οπου προσπάθησα να σηκωσω καρτα
		//θεωρω δεδομένο οτι δεν θα δωθει τιμη sourceIndex  που θα με πεταξε ιεξω απο τα ορια της δομής μου	
		Card raisedCard=null;


		 if( source.get(0) instanceof FoundationStack && !FoundationStacksAll.get(sourceIndex).isEmpty() ) 
			 raisedCard=FoundationStacksAll.get(sourceIndex).pop(); 	
		 else if( source.get(0) instanceof GameStack && !(GameStacksAll.get(sourceIndex).isEmpty()) )
			 raisedCard = GameStacksAll.get(sourceIndex).pop();
		 else if( source.get(0) instanceof Card ) { //δεν ελεγχω για null διοτι null επιστρεφω οταν ειναι αδεια η θεση
			 raisedCard=freeCells.get(sourceIndex); 
			 freeCells.set(sourceIndex, null);
		 }
		
		return raisedCard;
				
	}
	
	public Card raiseCard2(String typeOfPile, int index) { //useless

		Card raisedCard=null;
		
		switch(typeOfPile) {
		case "foundation":
			if(!FoundationStacksAll.get(index).isEmpty())
				raisedCard=FoundationStacksAll.get(index).pop();
			break;
		case "gamestack":
			if(!GameStacksAll.get(index).isEmpty())
				raisedCard=GameStacksAll.get(index).pop();
			break;
		case "freecell": //δεν ελεγχω για null διοτι null επιστρεφω οταν ειναι αδεια η θεση
			raisedCard=freeCells.get(index);
			break;
		default:
			System.out.println("The accepted types of Piles are: 'foundation' , 'gamestack' or 'freecell'. Try again please ");
		}
		
		return raisedCard;
	}
	
	public boolean placeCard(Card cardToPlace, Collection<?> destination) {
		boolean managedToPlace=false;
		
		if(cardToPlace == null)
			return true;
		
		if(destination instanceof GameStack)
			managedToPlace = null!=((GameStack) destination).pushMove(cardToPlace);
		else if(destination instanceof FoundationStack)
			managedToPlace = placeInFoundations(cardToPlace);
		else if(destination instanceof FreeCells )
			managedToPlace = ((FreeCells)destination).add(cardToPlace);
		
		return managedToPlace;
	}
	
	public boolean placeInFoundations(Card newEntry) {
		//παιρνει ενα χαρτι και προσπαθεί να το βάλει σε ενα απο τα Foundation Stacks. Αν τα καταφερει επιστρεφει true
		if(newEntry==null)
			return true;
		
		boolean success=false;
		int i=0;
		while(i<FoundationStacksAll.size() && !success ) {
			success = FoundationStacksAll.get(i).push(newEntry) !=null;
			i++;
		}
		return success;
	}
	
	
	public boolean makeMove(ArrayList<?> source, int sourceIndex, Collection<Card> placementDestination ) {
//Σηκώνει μια κάρτα απο την δομή source και την θέση sourceIndex, και την τοποθετεί στον προορισμό, εάν μπορει! Αλλιως την επανατοποθετεί εκει απο οπου την πήρε(χωρίς ελεγχους εγγυρότητας)
//Επιστρέφει true ανν εχει μετακινηθεί η κάρτα που σήκωσα στον προορισμό
		Card raisedCard=raiseCard(source, sourceIndex);
		boolean complete = placeCard(raisedCard, placementDestination);		
		if(!complete) {  
			complete=placeCard(raisedCard, source);
			
			if(!complete)//η μόνη περίπτωση να μην εχει ολοκληρωθεί ως εδω ειναι αν το σηκωσα απο Gamestack στο οποιο δεν μπορω να επανατοποθετήσω την σηκωμενη καρτα(λογω ελεγχων εγγυρότητας)
				GameStacksAll.get(sourceIndex).push(raisedCard);
		}
 
		
		return complete;
	}
	

	
	
	public void printTablau() { //helper function, prints the tablau
		System.out.print("FreeCells: ");
		for(Card card: freeCells)
			if(card!=null)
				card.printCardInfo();
		System.out.println();
		System.out.print("FoundationStacks: ");
		for(FoundationStack foundation : FoundationStacksAll ) {
			if(foundation.isEmpty())
				System.out.print(foundation.getSetName() + " ");
			else {
				foundation.peek().printCardInfo();
			}
		}	
		System.out.println();
		System.out.println("Game Stacks: ");	
		for(GameStack gameStack : GameStacksAll ) {
			for(Card card : gameStack) {
				card.printCardInfo();
			}
			System.out.println();
		}
	}
	

	
}
