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
		//������� ���� ����� ��� ������� �� ���������� �� ����� �� ������
		Tablau nodeTable = 	new Tablau(currentNode.getTable().getFreeCells(), currentNode.getTable().getGameStacksAll(), currentNode.getTable().getFoundationStacksAll()) ;
				
		boolean	solved = false;
		
		//������� ��� ������� ��� �� ������ ��� ��� ���� ������
		int i = currentNode.getSourceIterator();
		int j = currentNode.getDestinationIterator();
	
		ArrayList<?> raisedCardSource = null;
		int raisedCardIndex=0;// �� �� ���� index �� ����������� � �����(������� �� �� ����� ��� source
		
		
		boolean complete=false;	// ����� ������������ ������;	
		while(!solved) {	
			//�� �� i ��� ��� ��� ��� ������ ��� �� ������ �� ����� �� ���� ������. �� �����, ��� ������ ��� ��� ������ ��� ������. �� ��� ��� �� ������� i

			//����� �������� �� ���� source ��� index ���������� ��� FreeCell
			if(i<4) {
				if(nodeTable.getFreeCells().size() <= i) { //�� �� freeCells ������ �������� ��� i ��������, ���� ��������
					i++;
					continue;
				}
				raisedCardSource =  nodeTable.getFreeCells();
				raisedCardIndex = i;
			}
			//������ ���������� ��� GameStack			
			else if(i<NumberOfGameStacks+4) {
				if(nodeTable.getGameStacksAll().get(i-4).isEmpty()) {
					i++;
					continue;	
				}
				raisedCardSource = nodeTable.getGameStacksAll();
				raisedCardIndex = i-4;				
			}	
			//����� ���������� ��� Foundation Stack
			else if(i<16 ) {
				if(nodeTable.getFoundationStacksAll().get(i-(NumberOfGameStacks+4)).isEmpty()) {
					i++;
					continue;	
				}
				raisedCardSource = nodeTable.getFoundationStacksAll();
				raisedCardIndex = i-(NumberOfGameStacks+4);			
			}
			else {//�� ����� ��� ������� ��� �� ����� �� ������ ����� ����� , �������� ������ ������, ��� ����� �� ��������
				//�� ��� �� ��������� ��� ������� ����� ���� ���������� �� j , ����� ��� ����� ��� 9 ����� ���� ��� ������� i
				int newParentDestinationIterator = currentNode.getParent().getDestinationIterator() + 1;
				if(newParentDestinationIterator>9) {				
					currentNode.getParent().setSourceIterator(currentNode.getParent().getSourceIterator() +1);
					currentNode.getParent().setDestinationIterator(0);	
				}else {
					currentNode.getParent().setDestinationIterator(newParentDestinationIterator);
				}	
				
				return search(currentNode.getParent());
			}

			
			
			//���� ��� ����������
			if(j>9) { //������ ��� ��� ��� ������ j>9 ���� ������������, ������ �� ��� 0 ��� ��� ��� �� ������� ���� ������� ����� 
				j=0;
				i++;
				continue;
			}

			//check if it can go at Foundations
			if(j==0) {
				complete=nodeTable.makeMove(raisedCardSource, raisedCardIndex, new FoundationStack('t')); 
				j++;
			}				
			//check if it can go at Gamestacks- ���� OXI ��� ������ ��� ���� ��� ����				
			while(j<NumberOfGameStacks+1 && !complete) { //��� ���� ����� complete ��� Foundation, �� �� ���� ���� ���� ������
				if(j==i+3) {
					j++;
					continue;//OXI ��� ������ ��� ���� ��� ����
				}
					
				complete = nodeTable.makeMove(raisedCardSource, raisedCardIndex, nodeTable.getGameStacksAll().get(j-1));
				j++;
			}
			//check if it can go at freecells
			if(j==9 && !complete) {
				if(raisedCardSource instanceof FreeCells) {  //��� ���� ���� �� �� ���� ��� freecells ��� �� �� ���� ��� freeecells
					complete = nodeTable.makeMove(raisedCardSource, raisedCardIndex, nodeTable.getFreeCells());					
				}				
				j++;
			}
				
			if(complete) { //��� �� ���������� 	
				TreeNode newborn = new TreeNode(nodeTable, currentNode, 0, 0); //������� ��� ��� ����� ��� ������
				newborn.setShortCode();
				
				if(performTreeInsertionChecks(newborn)) {//��� ������ ��� ������ ��� �����������
					
					//������� ��� �� ����� ����
					solved = newborn.isSolution();
					if(solved) { 	//print solution
						System.out.println("solution");
						return newborn;
					}
					else { //�� ��� ����� ���� , ��� ���������
						System.out.println( newborn.getShortCode() );
						return	search(newborn);
					}
			
				}else {
					j++;
					continue;
				}				
					
			}
			//��� ��� ���� ���� ���, �������� ��� ��� ����� �� ���� ������ ������ ������ �� �� ������������ i, ����� ��� ��� ������� i
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
