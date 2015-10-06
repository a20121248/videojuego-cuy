package controlador;

public class InterpreteComandos {
	public int interpretarMovimiento(String comando) {
		if (comando.isEmpty())
			return -1;
		
		char c = comando.charAt(0);
		
		if (c == 'W' || c == 'w')
			return 0;
		else if (c == 'A' || c == 'a')
			return 1;
		else if (c == 'S' || c == 's')
			return 2;
		else if (c == 'D' || c == 'd')
			return 3;
		else if (c == 'I' || c == 'i')
			return 4;
		else if (c == 'J' || c == 'j')
			return 5;
		else if (c == 'K' || c == 'k')
			return 6;
		else if (c == 'L' || c == 'l')
			return 7;
		else
			return -1;
	}

	public boolean interpretarEspecial(String comando, char comandoIdeal) {
		comando = comando.toUpperCase();
		if(comando.charAt(0) == comandoIdeal) return true;
		else return false;
	}
}