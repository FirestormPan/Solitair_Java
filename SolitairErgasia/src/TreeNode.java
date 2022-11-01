import java.util.Arrays;
import java.util.List;


public class TreeNode{
		private Tablau table;
		protected TreeNode parent;
		protected int sourceIterator;
		protected int destinationIterator;
		private List<Card> shortCode = Arrays.asList(new Card[12]);  //ενα array με τα στοιχεία [freeCells(0-3), GameStacks(4-11), Foundation(12-15)]
		
		public TreeNode(Tablau a_table,TreeNode a_parent,int _sourceIterator,int _destinationIterator) {
			table = a_table;
			parent = a_parent;
			sourceIterator = _sourceIterator;
			destinationIterator = _destinationIterator;

		}
		
		public Tablau getTable() {
			return table;
		}
		public void setTable(Tablau table) {
			this.table = table;
		}
		public TreeNode getParent() {
			return parent;
		}
		public void setParent(TreeNode parent) {
			this.parent = parent;
		}
		public int getSourceIterator() {
			return sourceIterator;
		}
		public void setSourceIterator(int sourceIterator) {
			this.sourceIterator = sourceIterator;
		}
		public int getDestinationIterator() {
			return destinationIterator;
		}
		public void setDestinationIterator(int destinationIterator) {
			this.destinationIterator = destinationIterator;
		}
		public  List<Card> getShortCode() {
			return shortCode;
		}
		public void setShortCode() {
			
			int i;
			for( i=0; i<table.getFreeCells().size(); i++) 
				shortCode.set(i, table.getFreeCells().get(i));
					

			for(i=0; i<8; i++) 
				if(table.getGameStacksAll().get(i).isEmpty())
					shortCode.set(i+4, null);
				else
				shortCode.set(i+4, table.getGameStacksAll().get(i).peek()); 				 
		
		}
		
				
		public boolean isSolution() {
			for(int k=0; k<this.shortCode.size() ; k++)
				if(this.shortCode.get(k) != null) 
					return false;
			//αν ειναι αδεια ολα τα freecells και τα GameStacks, τοτε εχω λυση
			return true;			
		}
		
		
		

		
	}
	