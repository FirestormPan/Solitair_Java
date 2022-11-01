import java.util.Stack;

public class GameStack extends Stack<Card>{
	private static final long serialVersionUID = 2L;
	
	public GameStack(){
		super();
	}
	

	public Card pushMove(Card newEntry ) {	
		//� ������ ��������� ������ ���� ������ ���� ���� ����������� �������� �����������, ������ ��������� null
			if(this.isEmpty() ) {
				return push(newEntry);		
			}
			else if(!newEntry.isSameColourWith(this.peek()) && ((int)newEntry.getValue()==((int)this.peek().getValue() - 1) ) ){
				return push(newEntry);
			}
		return null;	
	}

	


}
