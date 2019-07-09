package consmed.modulos.medmedico.view.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import consmed.core.model.entities.AuthRol;
import consmed.core.model.entities.MedEspecialidad;
import consmed.core.model.entities.MedMedico;
import consmed.modulos.authautorizacion.model.AuthManagerAutorizacion;
import consmed.modulos.authautorizacionlogin.view.controller.AuthBeanLogin;
import consmed.modulos.medmedico.model.MedManagerMedico;
import consmed.modulos.view.util.JSFUtil;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class MedBeanMedico implements Serializable {

	private static final long serialVersionUID = 1L;
	//Med Medico
	private int id_medico;
	private String nombres_med;
	private String apellidos_med;
	private String identificacion_med;
	private String correo_med;
	private String telefono_med;
	private boolean activo_med;
	private int id_usuario_fk;
	private int id_especialidad_fk;
	private String foto_med;
private MedMedico medMedico;
private MedMedico editarmed;
//MedEspecialidad
	private int id_especialidad;
	private String nombre_esp;
	private boolean activo_esp;
	private MedEspecialidad medEspecialidad;
	@EJB
	private AuthManagerAutorizacion authManagerAutorizacion;
	@EJB
	private MedManagerMedico medManagerMedico;
	private List<MedMedico>listaMedicos;
	private List<MedEspecialidad>listaEspecialidades;
	@Inject
	private AuthBeanLogin authBeanLogin;
//AuthUsuario
private int	id_usuario;
private String correo_usua;
private String contrasenia_usua;
private int id_rol_fk;
//rol
private List<AuthRol>listaRoles;
private int id_rol;
private String nombre_rol;
private boolean activo_rol;
private AuthRol rol;
@PostConstruct
public void init() {
	try {
		System.out.println("init() m�dico");
		listaEspecialidades=medManagerMedico.findAllMedEspecialidades();
		listaMedicos=medManagerMedico.findAllMedMedicos();
		listaRoles=authManagerAutorizacion.findAllRoles();
		System.out.println("AQUI "+listaRoles);
	} catch (Exception e) {
		
	}
}
	
	public void actionListenerIngresarMedico() {
		try {
			medManagerMedico.ingresarMedMedico(nombres_med, apellidos_med, 
					identificacion_med, correo_med, identificacion_med, telefono_med, 
					activo_med, id_especialidad_fk, foto_med,id_rol_fk);
			listaMedicos=medManagerMedico.findAllMedMedicos();
			JSFUtil.crearMensajeInfo("El médico ha sido creado correctamente! \n "
					+ "El médico puede ingresar con su correo y su identificación");
			
		} catch (Exception e) {
			JSFUtil.crearMensajeError(""+e.getMessage());
			e.printStackTrace();
		}
	}
	public void actionListenerIngresarEspecialidad()  {
		try {
			medManagerMedico.ingresarMedEspecialidad(nombre_esp,activo_esp);
			listaEspecialidades=medManagerMedico.findAllMedEspecialidades();
			JSFUtil.crearMensajeInfo("Especialidad creada correctamente.!");
			nombre_esp="";
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}		
	}

	public void actionListenerCargarMedEspecialidad(MedEspecialidad especialidad) {
			medEspecialidad=especialidad;	
	}
	public String actionCargarMedicoPerfil() {
try {
	System.out.println("ID: "+authBeanLogin.getLogin().getId_usuario());
			
			editarmed=medManagerMedico.findMedMedicoByUsuario(authBeanLogin.getLogin().getId_usuario());
		if (editarmed==null) {
			JSFUtil.crearMensajeError("ERROR AL CARGAR");
			return "";
		}
			System.out.println("pasa");
			authBeanLogin.setEditarmed(editarmed);
			System.out.println("pasa");
		
		System.out.println("N"+editarmed.getCorreoMed());
		JSFUtil.crearMensajeInfo("Bienvenido Médico ");
		return"editarPerfil?faces-redirect=true";
		} catch (Exception e) {
			JSFUtil.crearMensajeError("" + e.getMessage());
			e.printStackTrace();
			return"";
		}
	}
	public void editarMedicoPerfil() {
		try {
			editarmed=authBeanLogin.getEditarmed();
			medManagerMedico.editarMedicoPerfil(editarmed, id_especialidad_fk, "Médico");
			JSFUtil.crearMensajeInfo("Sus datos han sido correctamente editados!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError("" + e.getMessage());
			e.printStackTrace();
		}
	}
	public void actionListenerCargarAuthRol(AuthRol rolCargado) {
			rol=rolCargado;
}
		
		public void actionListenerEditarMedEspecialidad(){
			
			try {
				medManagerMedico.editarEspecialidad(medEspecialidad);
				JSFUtil.crearMensajeInfo("La especialidad ha sido editado correctamente!");
			} catch (Exception e) {
				JSFUtil.crearMensajeError(e.getMessage());
			}
			
		}
		public void actionListenerIngresarRol() {
			try {
				medManagerMedico.ingresarRol(nombre_rol, activo_rol);
				listaRoles=authManagerAutorizacion.findAllRoles();
				JSFUtil.crearMensajeInfo("El rol ha sido creado correctamente! ");
				
			} catch (Exception e) {
				JSFUtil.crearMensajeError(""+e.getMessage());
				e.printStackTrace();
			}
		}
		
	
	public void actionListenerCargarMedMedico(MedMedico medico) {
			medMedico=medico;	
	}
	public void actionListenerEditarAuthRol() {
		try {
			medManagerMedico.editarRol(rol);
			JSFUtil.crearMensajeInfo("El rol ha sido editado correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	public void actionListenerEditarMedMedico() {
		try {
			medManagerMedico.editarMedico(medMedico,id_especialidad_fk,"Médico");
			listaMedicos=medManagerMedico.findAllMedMedicos();
			JSFUtil.crearMensajeInfo("EL médico ha sido editado correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}


	public void actionListenerEliminarMedico(int id_medico) {
		try {
			medManagerMedico.eliminarMedMedico(id_medico);
			listaMedicos=medManagerMedico.findAllMedMedicos();
			JSFUtil.crearMensajeInfo("El médicoha sidoeliminadocorrectamente!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	public void actionListenerEliminarEspecialidad(int id_especialidad) {
		try {
			medManagerMedico.eliminarMedEspecialidad(id_especialidad);
			listaEspecialidades=medManagerMedico.findAllMedEspecialidades();
			JSFUtil.crearMensajeInfo("La especialidad ha sido eliminada correctamente!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	public void actionListenerEliminarRol(int id_rol) {
		try {
			medManagerMedico.eliminarAuthRol(id_rol);
			listaRoles=authManagerAutorizacion.findAllRoles();
			JSFUtil.crearMensajeInfo("El rol ha sido eliminado correctamente!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	public void actionListenerListaPacientes() {
		
	}

	public int getId_medico() {
		return id_medico;
	}

	public void setId_medico(int id_medico) {
		this.id_medico = id_medico;
	}

	public String getNombres_med() {
		return nombres_med;
	}

	public void setNombres_med(String nombres_med) {
		this.nombres_med = nombres_med;
	}

	public String getApellidos_med() {
		return apellidos_med;
	}

	public void setApellidos_med(String apellidos_med) {
		this.apellidos_med = apellidos_med;
	}

	public String getIdentificacion_med() {
		return identificacion_med;
	}

	public void setIdentificacion_med(String identificacion_med) {
		this.identificacion_med = identificacion_med;
	}

	public String getCorreo_med() {
		return correo_med;
	}

	public void setCorreo_med(String correo_med) {
		this.correo_med = correo_med;
	}

	public String getTelefono_med() {
		return telefono_med;
	}

	public void setTelefono_med(String telefono_med) {
		this.telefono_med = telefono_med;
	}

	public boolean isActivo_med() {
		return activo_med;
	}

	public void setActivo_med(boolean activo_med) {
		this.activo_med = activo_med;
	}

	public int getId_usuario_fk() {
		return id_usuario_fk;
	}

	public void setId_usuario_fk(int id_usuario_fk) {
		this.id_usuario_fk = id_usuario_fk;
	}

	public int getId_especialidad_fk() {
		return id_especialidad_fk;
	}

	public void setId_especialidad_fk(int id_especialidad_fk) {
		this.id_especialidad_fk = id_especialidad_fk;
	}

	public String getFoto_med() {
		return foto_med;
	}

	public void setFoto_med(String foto_med) {
		this.foto_med = foto_med;
	}

	public int getId_especialidad() {
		return id_especialidad;
	}

	public void setId_especialidad(int id_especialidad) {
		this.id_especialidad = id_especialidad;
	}

	public String getNombre_esp() {
		return nombre_esp;
	}

	public void setNombre_esp(String nombre_esp) {
		this.nombre_esp = nombre_esp;
	}

	public boolean isActivo_esp() {
		return activo_esp;
	}

	public void setActivo_esp(boolean activo_esp) {
		this.activo_esp = activo_esp;
	}

	public MedManagerMedico getMedManagerMedico() {
		return medManagerMedico;
	}

	public void setMedManagerMedico(MedManagerMedico medManagerMedico) {
		this.medManagerMedico = medManagerMedico;
	}

	public List<MedMedico> getListaMedicos() {
		return listaMedicos;
	}

	public void setListaMedicos(List<MedMedico> listaMedicos) {
		this.listaMedicos = listaMedicos;
	}

	public List<MedEspecialidad> getListaEspecialidades() {
		return listaEspecialidades;
	}

	public void setListaEspecialidades(List<MedEspecialidad> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
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

	public List<AuthRol> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<AuthRol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	public MedMedico getMedMedico() {
		return medMedico;
	}

	public void setMedMedico(MedMedico medMedico) {
		this.medMedico = medMedico;
	}

	public MedEspecialidad getMedEspecialidad() {
		return medEspecialidad;
	}

	public void setMedEspecialidad(MedEspecialidad medEspecialidad) {
		this.medEspecialidad = medEspecialidad;
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

	public AuthRol getRol() {
		return rol;
	}

	public void setRol(AuthRol rol) {
		this.rol = rol;
	}

	public MedMedico getEditarmed() {
		return editarmed;
	}

	public void setEditarmed(MedMedico editarmed) {
		this.editarmed = editarmed;
	}
	
	
	
	
	
	
	
	
	
}
