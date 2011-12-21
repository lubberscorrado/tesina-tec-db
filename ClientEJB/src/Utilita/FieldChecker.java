package Utilita;

public class FieldChecker {
	
	/**
	 * Effettua il parsing di una stringa ritornando un booleano
	 * @param in
	 * @return
	 */
	public static boolean checkValue(String in){
		if(in == null){
			return false;
		}else if( in.equals("on") || in.equals("true") ){
			return true;
		}else{
			return false;
		}
	}
	
}

