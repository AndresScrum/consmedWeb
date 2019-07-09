package consmed.modulos.authautorizacion.view.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import consmed.core.model.entities.AuthRol;
import consmed.core.model.entities.AuthUsuario;
import consmed.modulos.authautorizacion.model.AuthManagerAutorizacion;
import consmed.modulos.view.util.JSFUtil;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AuthBeanAutorizacion implements Serializable {

	private static final long serialVersionUID = 1L;
	//Med Medico
	
	//Usuario
	private int id_usuario;
	private String correo_usuario;
	private String contrasenia;
	private int id_rol_fk;
	private AuthUsuario usuario;
	List<AuthUsuario>listaUsuarios;
	//Rol 
	private int id_rol;
	private String nombre_rol;
	private boolean activo_rol;
	private List<AuthRol>listaRoles;
private 	AuthRol rol;
@EJB
private AuthManagerAutorizacion authManagerAutorizacion;

@PostConstruct
public void init() {
	
	
	
	try {

		listaUsuarios=authManagerAutorizacion.findAllUsuariosAdmin("Administrador");
	} catch (Exception e) {
		
	}
}

public void actionListenerEncriptarAllPassword() {
	try {
		listaUsuarios=authManagerAutorizacion.findAllUsuarios();
		authManagerAutorizacion.encriptarAllPassword(listaUsuarios);
	} catch (Exception e) {
		
	}
}


public void actionListenerCargarUsuario(AuthUsuario user) {
	try {
    usuario=user;
	} catch (Exception e) {
		JSFUtil.crearMensajeError("" + e.getMessage());
		e.printStackTrace();
	}
}
public void actionListenerEliminarUsuario(int idUser) {
	try {
   authManagerAutorizacion.eliminarUsuario(idUser);
	listaUsuarios=authManagerAutorizacion.findAllUsuariosAdmin("Administrador");
   JSFUtil.crearMensajeInfo("El usuario ha sido eliminado correctamente!");
	} catch (Exception e) {
		JSFUtil.crearMensajeError("" + e.getMessage());
		e.printStackTrace();
	}
}
public void actionListenerIngresarUsuario() {
	try {
		contrasenia=correo_usuario;
		id_rol_fk=authManagerAutorizacion.findRolByNombre("Administrador");
	    if (id_rol_fk==0) {
	    	System.out.println("4 Pasa el  if"+id_rol_fk);
			authManagerAutorizacion.ingresarAuthRol("Administrador", true);
			id_rol_fk=authManagerAutorizacion.findRolByNombre("Administrador");
	    }
	    authManagerAutorizacion.ingresarAuthUsuario(correo_usuario, contrasenia, id_rol_fk);
		listaUsuarios=authManagerAutorizacion.findAllUsuariosAdmin("Administrador");
	    JSFUtil.crearMensajeInfo("El Administrador se ha creado correctamente!");
	    JSFUtil.crearMensajeInfo("El administrador que ingrese por primera vez ingresará "
	    		+ "con su correo como contraseña");
	} catch (Exception e) {
		JSFUtil.crearMensajeError("" + e.getMessage());
		e.printStackTrace();
	}
}
public void actionListenerEditarUsuario() {
	try {
		id_rol_fk=authManagerAutorizacion.findRolByNombre("Administrador");
	    if (id_rol_fk==0) {
	    	System.out.println("4 Pasa el  if"+id_rol_fk);
			authManagerAutorizacion.ingresarAuthRol("Administrador", true);
			id_rol_fk=authManagerAutorizacion.findRolByNombre("Administrador");
	    }
	    authManagerAutorizacion.editarUsuario(usuario, id_rol_fk);
	    JSFUtil.crearMensajeInfo("El Administrador se ha editado correctamente!");
	} catch (Exception e) {
		JSFUtil.crearMensajeError("" + e.getMessage());
		e.printStackTrace();
	}
}



public int getId_rol() {
	return id_rol;
}

public void setId_rol(int id_rol) {
	this.id_rol = id_rol;
}

public String getNombre_rol() {
	return nombre_rol;
}

public void setNombre_rol(String nombre_rol) {
	this.nombre_rol = nombre_rol;
}

public boolean isActivo_rol() {
	return activo_rol;
}

public void setActivo_rol(boolean activo_rol) {
	this.activo_rol = activo_rol;
}

public List<AuthRol> getListaRoles() {
	return listaRoles;
}

public void setListaRoles(List<AuthRol> listaRoles) {
	this.listaRoles = listaRoles;
}

public AuthRol getRol() {
	return rol;
}

public void setRol(AuthRol rol) {
	this.rol = rol;
}


public List<AuthUsuario> getListaUsuarios() {
	return listaUsuarios;
}


public void setListaUsuarios(List<AuthUsuario> listaUsuarios) {
	this.listaUsuarios = listaUsuarios;
}



public int getId_usuario() {
	return id_usuario;
}



public void setId_usuario(int id_usuario) {
	this.id_usuario = id_usuario;
}



public String getCorreo_usuario() {
	return correo_usuario;
}



public void setCorreo_usuario(String correo_usuario) {
	this.correo_usuario = correo_usuario;
}





public int getId_rol_fk() {
	return id_rol_fk;
}



public void setId_rol_fk(int id_rol_fk) {
	this.id_rol_fk = id_rol_fk;
}



public AuthUsuario getUsuario() {
	return usuario;
}



public void setUsuario(AuthUsuario usuario) {
	this.usuario = usuario;
}



public String getContrasenia() {
	return contrasenia;
}



public void setContrasenia(String contrasenia) {
	this.contrasenia = contrasenia;
}
	
	
	
}






