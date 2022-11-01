import java.util.*;

public class FreeCells extends ArrayList<Card>{
	private static final long serialVersionUID = 1L;
	

	@Override
	public boolean add(Card newEntry){
	if(this.size()<4)
		return super.add(newEntry);
	else 
		return false;
	}
	
}
