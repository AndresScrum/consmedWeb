package consmed.modulos.pacpaciente.view.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import consmed.core.model.entities.PacPaciente;
import consmed.modulos.authautorizacion.model.AuthManagerAutorizacion;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.view.util.JSFUtil;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PacBeanPaciente implements Serializable {
//Pac Paciente
	private static final long serialVersionUID = 1L;
	private int id_paciente;
	private String nombres_pac;
	private String apellidos_pac;
	private String identificacion;
	private String correo_pac;
	private String telefono_pac;
	private int id_usuariofk;
	private String direccion_pac;
	private boolean activo_pac;
	private String foto_pac;
	private PacPaciente paciente;
	@EJB
	private PacManagerPaciente pacManagerPaciente;
	@EJB
	private AuthManagerAutorizacion authManagerAutorizacion;
	private List<PacPaciente> listaPacientes;
//AuthUsuario
	private int id_usuario;
	private String correo_usua;
	private String contrasenia_usua;
	private int id_rol_fk;

	@PostConstruct
	public void init() {
		try {
			listaPacientes = pacManagerPaciente.findAllPacPacientes();
			
		} catch (Exception e) {

		}
	}

	public void actionListenerIngresarPaciente() {
		try {
			contrasenia_usua = identificacion;
			int rol = authManagerAutorizacion.findRolByNombre("Paciente");
			if (rol == 0) {
				authManagerAutorizacion.ingresarAuthRol("Paciente", true);
				rol = authManagerAutorizacion.findRolByNombre("Paciente");
			}
			pacManagerPaciente.ingresarPacPaciente(nombres_pac, apellidos_pac, identificacion, correo_pac,
					contrasenia_usua, rol, telefono_pac, direccion_pac, true, foto_pac);
			listaPacientes = pacManagerPaciente.findAllPacPacientes();
			JSFUtil.crearMensajeInfo("El paciente ha sido creado correctamente!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError("" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCargarPaciente(PacPaciente pacienteCargado) {
		paciente = pacienteCargado;
	}

	public void actionListenerEditarPacienteByAdministrador() {
try {
	pacManagerPaciente.editarPacPaciente(paciente);
	JSFUtil.crearMensajeInfo("El paciente ha sido editado correctamente!");
} catch (Exception e) {
	JSFUtil.crearMensajeError(e.getMessage());

	e.printStackTrace();
}	
}

	public void actionListenerEliminarPaciente(int id_paciente) {
		try {
			pacManagerPaciente.eliminarPaciente(id_paciente);
			listaPacientes = pacManagerPaciente.findAllPacPacientes();
			JSFUtil.crearMensajeInfo("El paciente ha sido eliminado correctamente!");
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeError(e.getMessage());
		}

	}

	public void actionListenerListaPacientes() {

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

	public List<PacPaciente> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<PacPaciente> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public PacPaciente getPaciente() {
		return paciente;
	}

	public void setPaciente(PacPaciente paciente) {
		this.paciente = paciente;
	}

}
