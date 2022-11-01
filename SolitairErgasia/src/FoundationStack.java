import java.util.Stack;

public class FoundationStack extends Stack<Card>{
	private static final long serialVersionUID = 1L;

	private char setName;

	FoundationStack(char foundationSet){
		super(); //constructor
		setName = foundationSet;
	}
	
	public char getSetName() {
		return setName;
	}
	
	@Override
	public Card push(Card newEntry ) {
		//η κίνηση εισαγωγής καρτας στην στοιβα μετα τους καταλληλους ελεγχους εγκυροτητας, αλλιως επιστροφη null
		if((newEntry.getSet() == setName)) {

			if(this.isEmpty()) {
				if(newEntry.getValue()==0 ) {
					return super.push(newEntry);
				}
			}
			else if( (int)newEntry.getValue() == (int)this.peek().getValue() +1){
				return super.push(newEntry);
			}
		}
		return null;
	}	
	
	

	
	

}


