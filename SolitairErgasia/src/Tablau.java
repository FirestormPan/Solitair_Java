import java.util.*;

public class Tablau {
	
	private ArrayList<GameStack> GameStacksAll = new ArrayList<GameStack>();
	private ArrayList<FoundationStack> FoundationStacksAll= new ArrayList<FoundationStack>();
	private ArrayList<Card> freeCells;

	
	public Tablau(ArrayList<Card> initialFreeCells, ArrayList<GameStack> initialGameStacks, ArrayList<FoundationStack> initialFoundationStacks) {
		
		//initializing the freecells		
		freeCells = new FreeCells();
		for(Card card : initialFreeCells) //����� ������� ����
			freeCells.add(card);
		

		//initialize Game Stacks(��� ������� ���� ����������, 8 ������� �� ��� ������ ����)
		int index=0;
		for(GameStack gamestack : initialGameStacks) {
			gamestack =  (GameStack) initialGameStacks.get(index).clone();
			GameStacksAll.add(gamestack) ;
			index++;
		}


		//initialize foundation Stacks
		if(initialFoundationStacks.isEmpty()) {
			//initialize foundation Stacks, ������� 4 F�undation stacks �� ������� ��� ��� �� ���
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
		//�� �������� �� ������ ����� ��� ������ � foundation, ���������� ��� �����(pop), ��� ���  ������ ������ index (�/�).
		//�� �������� �� ������ ����� ��� freecel, ���������� ��� ����� ����� index.
		//�������!! �� ���� ��������� ������ �� ���������� null, ������ ���� ����� �� ������ ��� ���� ���������� �� ������ �����
		//����� �������� ��� ��� �� ����� ���� sourceIndex  ��� �� �� ������ ���� ��� �� ���� ��� ����� ���	
		Card raisedCard=null;


		 if( source.get(0) instanceof FoundationStack && !FoundationStacksAll.get(sourceIndex).isEmpty() ) 
			 raisedCard=FoundationStacksAll.get(sourceIndex).pop(); 	
		 else if( source.get(0) instanceof GameStack && !(GameStacksAll.get(sourceIndex).isEmpty()) )
			 raisedCard = GameStacksAll.get(sourceIndex).pop();
		 else if( source.get(0) instanceof Card ) { //��� ������ ��� null ����� null ��������� ���� ����� ����� � ����
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
		case "freecell": //��� ������ ��� null ����� null ��������� ���� ����� ����� � ����
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
		//������� ��� ����� ��� ��������� �� �� ����� �� ��� ��� �� Foundation Stacks. �� �� ��������� ���������� true
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
//������� ��� ����� ��� ��� ���� source ��� ��� ���� sourceIndex, ��� ��� ��������� ���� ���������, ��� ������! ������ ��� �������������� ���� ��� ���� ��� ����(����� �������� �����������)
//���������� true ��� ���� ����������� � ����� ��� ������ ���� ���������
		Card raisedCard=raiseCard(source, sourceIndex);
		boolean complete = placeCard(raisedCard, placementDestination);		
		if(!complete) {  
			complete=placeCard(raisedCard, source);
			
			if(!complete)//� ���� ��������� �� ��� ���� ����������� �� ��� ����� �� �� ������ ��� Gamestack ��� ����� ��� ����� �� ��������������� ��� �������� �����(���� ������� �����������)
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
