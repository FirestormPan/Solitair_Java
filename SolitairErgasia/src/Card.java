
public class Card {
	
	private char set;
	private int value;

	
	public Card(String cardInputRaw) {
	//παιρνει το string με τα στοιχεια της καρτας και φτιαχνει το αντικειμενο καρτα	
		set = cardInputRaw.charAt(0);
		
		String tempValue = "";
		for(int i=1;i<cardInputRaw.length();i++) {
			tempValue+=	cardInputRaw.charAt(i);
		}	
		value = Integer.parseInt(tempValue);
	}
	
	public int getValue() {
		return value;
	}

	public char getSet() {
		return set;
	}
	
	
	public String assingColour() {
		String colour="";
		if(this.set=='C' || this.set=='S') {
			colour="black";
		}
		else if(this.set=='H' || this.set=='D') {
			colour="red";
		}
		return colour;
	}
	
	public boolean isSameColourWith(Card otherCard) {
	return	this.assingColour().equals(otherCard.assingColour()); 
	
	}
	
	public void printCardInfo() {
		System.out.print( getSet() + "" + getValue() + " " );
	}
	
	
}
