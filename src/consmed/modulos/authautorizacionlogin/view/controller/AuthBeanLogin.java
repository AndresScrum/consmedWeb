package consmed.modulos.authautorizacionlogin.view.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import consmed.core.model.entities.AuthRol;
import consmed.core.model.entities.PacPaciente;
import consmed.modulos.authautorizacion.model.AuthManagerAutorizacion;
import consmed.modulos.login.model.AuthManagerLogin;
import consmed.modulos.login.model.Login;
import consmed.modulos.view.util.JSFUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class AuthBeanLogin implements Serializable {
	private static final long serialVersionUID = 1L;
	//Med Medico
	private int id_paciente;
	private String nombres_pac;
	private String apellidos_pac;
	private String identificacion;
	private String correo_pac;
	private String contrasenia_pac;
	private String telefono_pac;
	private int id_usuariofk;
	private String direccion_pac;
 private boolean activo_pac;
	private String foto_pac;
	private PacPaciente paciente;
	List<PacPaciente >listaPacientes;
	//
	private int id_rol;
	private String nombre_rol;
	private String activo_rol;
	private AuthRol rol;
	private int id_usuario;
	private String correo_usua;
	private String contrasenia_usua;
	private int id_rol_fk;
	private Login login;
	private boolean activoLogin;
	@EJB
	private AuthManagerAutorizacion authManagerAutorizacion;
	@EJB
	private AuthManagerLogin authManagerLogin;
	
	@PostConstruct
	public void inicializar() {
		HttpServletRequest req=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public String actionIngresarPaciente() {
		try {
			authManagerAutorizacion.ingresarPacPaciente(nombres_pac, apellidos_pac, identificacion,
					correo_pac, contrasenia_pac, telefono_pac, direccion_pac, true);
		JSFUtil.crearMensajeInfo("El paciente ha sido creado correctamente!");
		return "pacpaciente/menu?faces-redirect=true";
		} catch (Exception e) {

			JSFUtil.crearMensajeError(""+e.getMessage());
			e.printStackTrace();
			
		}
		return "";
	}
	//Administrador ingresar Médico 
	public String actionIngresarMedico() {
		try {
			authManagerAutorizacion.ingresarPacPaciente(nombres_pac, apellidos_pac, identificacion,
					correo_pac, contrasenia_pac, telefono_pac, direccion_pac, true);
		JSFUtil.crearMensajeInfo("El paciente ha sido creado correctamente!");
		return "pacpaciente/menu?faces-redirect=true";
		} catch (Exception e) {

			JSFUtil.crearMensajeError(""+e.getMessage());
			e.printStackTrace();
			
		}
		return "";
	}
	public String actionLogin() {
		try {
			login=authManagerLogin.comprobarCredenciales(correo_usua, contrasenia_usua);
			if (login==null) {
				System.out.println("ESTA VACIO");	
			}else {
				System.out.println("si"+login.getNombres());
			}
			
			JSFUtil.crearMensajeInfo("Bienvenido!"+login.getCorreo());
			System.out.println("ROL: "+login.getCorreo());
			if (login.getNombre_rol().equals("Paciente")) {
				
				System.out.println("ddddddd "+login.getNombres());
				return"pacpacientes/menu?faces-redirect=true";
			}else {
				if (login.getNombre_rol().equals("Médico")) {
					return"medico/menu?faces-redirect=true";
				}else {
					return"administrador/menu?faces-redirect=true";
				}
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		JSFUtil.crearMensajeError(e.getMessage());
		}
		
		return"";
	}
	
	
	public String  actionCerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();		
		return "/login?faces-redirect=true";
	}
	
	public void actionComprobarSessionLogin(){
    	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    	try {
    	String path=ec.getRequestPathInfo();
    	System.out.println("getRequestContextPath(): "+ec.getRequestContextPath());
    	System.out.println("getRequestPathInfo(): "+path);
    	
    	if(path.equals("/login.xhtml"))
    	return;
    	if(login==null)
    	ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
    	else
    	activoLogin=true;
    	
    	if(!activoLogin){
    	ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
    	}else{
    	//si hizo login, verificamos que acceda a paginas permitidas:
    	if(login.equals("Administrador")){
    	if(!path.contains("/administrador/"))
    	ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
    	else
    	return;
    	}
    	//si hizo login, verificamos que acceda a paginas permitidas:
    	if(login.equals("Médico")){
    	if(!path.contains("/medico/"))
    	ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
    	else
    	return;
    	}
    	
    	if(!path.contains("/paciente/"))
    	ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
    	}
    	} catch (IOException e) {
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

	public String getActivo_rol() {
		return activo_rol;
	}

	public void setActivo_rol(String activo_rol) {
		this.activo_rol = activo_rol;
	}

	public AuthRol getRol() {
		return rol;
	}

	public void setRol(AuthRol rol) {
		this.rol = rol;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getCorreo_usua() {
		return correo_usua;
	}

	public void setCorreo_usua(String correo_usua) {
		this.correo_usua = correo_usua;
	}

	public String getContrasenia_usua() {
		return contrasenia_usua;
	}

	public void setContrasenia_usua(String contrasenia_usua) {
		this.contrasenia_usua = contrasenia_usua;
	}

	public int getId_rol_fk() {
		return id_rol_fk;
	}

	public void setId_rol_fk(int id_rol_fk) {
		this.id_rol_fk = id_rol_fk;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public boolean isActivoLogin() {
		return activoLogin;
	}

	public void setActivoLogin(boolean activoLogin) {
		this.activoLogin = activoLogin;
	}

	public int getId_paciente() {
		return id_paciente;
	}

	public void setId_paciente(int id_paciente) {
		this.id_paciente = id_paciente;
	}

	public String getNombres_pac() {
		return nombres_pac;
	}

	public void setNombres_pac(String nombres_pac) {
		this.nombres_pac = nombres_pac;
	}

	public String getApellidos_pac() {
		return apellidos_pac;
	}

	public void setApellidos_pac(String apellidos_pac) {
		this.apellidos_pac = apellidos_pac;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getCorreo_pac() {
		return correo_pac;
	}

	public void setCorreo_pac(String correo_pac) {
		this.correo_pac = correo_pac;
	}

	public String getContrasenia_pac() {
		return contrasenia_pac;
	}

	public void setContrasenia_pac(String contrasenia_pac) {
		this.contrasenia_pac = contrasenia_pac;
	}

	public String getTelefono_pac() {
		return telefono_pac;
	}

	public void setTelefono_pac(String telefono_pac) {
		this.telefono_pac = telefono_pac;
	}

	public int getId_usuariofk() {
		return id_usuariofk;
	}

	public void setId_usuariofk(int id_usuariofk) {
		this.id_usuariofk = id_usuariofk;
	}

	public String getDireccion_pac() {
		return direccion_pac;
	}

	public void setDireccion_pac(String direccion_pac) {
		this.direccion_pac = direccion_pac;
	}

	public boolean isActivo_pac() {
		return activo_pac;
	}

	public void setActivo_pac(boolean activo_pac) {
		this.activo_pac = activo_pac;
	}

	public String getFoto_pac() {
		return foto_pac;
	}

	public void setFoto_pac(String foto_pac) {
		this.foto_pac = foto_pac;
	}

	public PacPaciente getPaciente() {
		return paciente;
	}

	public void setPaciente(PacPaciente paciente) {
		this.paciente = paciente;
	}

	public List<PacPaciente> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<PacPaciente> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}
	

}
