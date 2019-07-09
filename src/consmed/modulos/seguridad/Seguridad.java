package consmed.modulos.seguridad;

import org.apache.commons.codec.digest.DigestUtils;

public class Seguridad {
	
	public static String encriptar(String passsword) {
		String contrasenia=DigestUtils.sha256Hex(passsword);
		return contrasenia;
	}
	
public static void main(String args[]) {

	encriptar("123");
	System.out.println(""+encriptar("123"));
	
	
}
}
