package consmed.modulos.pacpaciente.view.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import consmed.core.model.entities.FactHospital;
import consmed.core.model.entities.MedMedico;
import consmed.core.model.entities.PacCabeceraHc;
import consmed.core.model.entities.PacHistoriaClinica;
import consmed.core.model.entities.PacPaciente;
import consmed.core.model.entities.ReserCita;
import consmed.modulos.authautorizacionlogin.view.controller.AuthBeanLogin;
import consmed.modulos.factfacturacion.model.FactManagerFacturacion;
import consmed.modulos.medmedico.model.MedManagerMedico;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.resercita.model.ReserManagerCita;
import consmed.modulos.resercita.view.controller.ReserBeanCita;
import consmed.modulos.segauditoria.model.SegManagerAuditoria;
import consmed.modulos.view.util.JSFUtil;

@Named
@SessionScoped
public class PacBeanHistoriaC implements Serializable {
	private static final long serialVersionUID = 1L;
	private PacPaciente paciente;
	private int idCabecera;
	private String fechaNacimiento;
	private double peso;
	private double altura;
	private String genero;
	private String ocupacion;
	private PacCabeceraHc pacCabeceraHc;
	private boolean existeCab;
	private boolean newHistoriaC;
	private Date fechaAtencion;
	private Time horaAtencion;
	private MedMedico medico;
	private int idMedico;
	private String cama;
	private String motivoConsulta;
	private String enfermedadActual;
	private String diagnostico;
	private String evolucionMedica;
	private String plan;
	private String tratamiento;
	private String estudios;
	private String cuidados;
	private List<PacHistoriaClinica> listHistoriasC;
	private PacHistoriaClinica historiaSelect;
	private ReserCita citaSelect;

	@EJB
	private PacManagerPaciente pacManagerPaciente;
	@EJB
	private MedManagerMedico medManagerMedico;
	@EJB
	private SegManagerAuditoria segManagerAuditoria;
	@EJB
	private ReserManagerCita reserManagerCita;
	@EJB
	private FactManagerFacturacion factManagerFacturacion;

	@Inject
	private ReserBeanCita reserBeanCita;
	@Inject
	private AuthBeanLogin authBeanLogin;

	@PostConstruct
	public void init() {
		System.out.println("Init() HC");
		System.out.println(paciente);
			
		
	}

	public String actionHistoriaClinica(ReserCita cita) {
		paciente = cita.getPacPaciente();
		citaSelect=cita;
		// setPaciente(paciente);
		System.out.println("Id paciente sele: " + paciente.getIdPaciente());
		if (paciente != null) {
			getCabeceraHc();
		}else {
			fechaNacimiento = "";
			peso = 0;
			altura = 0;
			genero = "";
			ocupacion = "";
			idCabecera = 0;
			existeCab = false;
		}
		return "historiaClinica?faces-redirect=true";
	}

	public String actionGuardarCabeceraHc() {
		// PacPaciente paciente=pacManagerPaciente.findPacienteById(17);
		// System.out.println(beanCita.getIdPaciente());
		System.out.println(fechaNacimiento);
		System.err.println(peso);
		System.err.println(altura);
		System.err.println(genero);
		System.err.println(ocupacion);
		try {
			pacManagerPaciente.ingresarPacCabeceraHc(paciente, fechaNacimiento, peso, altura, genero, ocupacion);
			segManagerAuditoria.ingresarBitacora(authBeanLogin.getLogin().getId_usuario(), "ingresarPacCabeceraHc",
					"Médico crea cabecera historia clínica");
			JSFUtil.crearMensajeInfo("Ingreso correcto!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}finally {
			existeCab = true;
			getCabeceraHc();
		}
		System.out.println("Ingreso cabecera hc");
		return "";
	}

	public void getCabeceraHc() {
		pacCabeceraHc = pacManagerPaciente.getPacienteCabeceraHc(paciente.getIdPaciente());
		System.out.println(pacCabeceraHc);
		if (pacCabeceraHc != null) {
			fechaNacimiento = pacCabeceraHc.getFechaNacimiento();
			peso = Double.parseDouble(pacCabeceraHc.getPeso().toString());
			altura = Double.parseDouble(pacCabeceraHc.getAltura().toString());
			genero = pacCabeceraHc.getGenero();
			ocupacion = pacCabeceraHc.getOcupacion();
			idCabecera = pacCabeceraHc.getIdCabecera();
			existeCab = true;
		} else {
			existeCab = false;
		}

	}

	public String actionUpdateCabeceraHc() {
		PacCabeceraHc cabeceraCargar = new PacCabeceraHc();
		cabeceraCargar.setIdCabecera(idCabecera);
		cabeceraCargar.setPacPaciente(paciente);
		cabeceraCargar.setFechaNacimiento(fechaNacimiento);
		cabeceraCargar.setPeso(new BigDecimal(peso));
		cabeceraCargar.setAltura(new BigDecimal(altura));
		cabeceraCargar.setGenero(genero);
		cabeceraCargar.setOcupacion(ocupacion);
		try {
			pacManagerPaciente.editarPacCabeceraHc(cabeceraCargar);
			segManagerAuditoria.ingresarBitacora(authBeanLogin.getLogin().getId_usuario(), "editarPacCabeceraHc",
					"Médico editó cabecera historia clínica");
			JSFUtil.crearMensajeInfo("Actualización correcta!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError("Error al actualizar");
		}finally {
			cama="";
			motivoConsulta="";
			enfermedadActual="";
			diagnostico="";
			evolucionMedica="";
			plan="";
			tratamiento="";
			estudios="";
			cuidados="";
		}

		return "";
	}

	public String actionGuardarHistoriC() {
		System.out.println("actionGuardarHistoriaC()");
		fechaAtencion = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaAtencion);
		@SuppressWarnings("deprecation")
		Time ti = new Time(fechaAtencion.getTime());
		horaAtencion = ti;
		try {
			PacCabeceraHc cabecera;
			cabecera = pacManagerPaciente.findCabeceraHcById(idCabecera);
			idMedico = reserBeanCita.getIdMedico();
			System.out.println("Id médico: " + idMedico);
			MedMedico medico;
			medico = medManagerMedico.findMedicoById(idMedico);
			pacManagerPaciente.ingresarPacHistoriaC(cabecera, fechaAtencion, horaAtencion, medico, cama, motivoConsulta,
					enfermedadActual, diagnostico, evolucionMedica, plan, tratamiento, estudios, cuidados);
			segManagerAuditoria.ingresarBitacora(authBeanLogin.getLogin().getId_usuario(), "ingresarPacHistoriaC",
					"Médico crea historia clínica");
			System.out.println("Cita asunto: "+citaSelect.getAsuntoReser());			
			facturar(citaSelect.getPacPaciente(),authBeanLogin.getLogin().getId_usuario(), motivoConsulta);
			actualizarCitaEstado(citaSelect, authBeanLogin.getLogin().getId_usuario());
			JSFUtil.crearMensajeInfo("Se creo historia clínica");
			newHistoriaC = true;
			listHistoriasC = pacManagerPaciente.findPacHistoriaClinicaByPaciente(paciente.getIdPaciente());
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
		}finally {
			cama="";
			motivoConsulta="";
			enfermedadActual="";
			diagnostico="";
			evolucionMedica="";
			plan="";
			tratamiento="";
			estudios="";
			cuidados="";
		}

		return "";
	}
	
	public void actualizarCitaEstado(ReserCita cita, int idUsuario) {
		try {
			cita.setActivoReser(false);
			cita.setPagoReser(true);
			reserManagerCita.actualizarCita(cita);
			segManagerAuditoria.ingresarBitacora(idUsuario, "actualizarCita", "Actualizar cita después de guardar historia clínica");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			JSFUtil.crearMensajeError("Erro actualizar estado cita");
		}
	}
	
	public void facturar(PacPaciente pacPaciente, int idUsuario, String detalle) {
		FactHospital factHospital= factManagerFacturacion.findFacHospitalById(1);
		if(factHospital!=null && pacPaciente!=null) {
			factManagerFacturacion.ingresarFactFactura(true,detalle , new BigDecimal(2.4), new BigDecimal(17.6), new BigDecimal(20), 
					factHospital, pacPaciente);
			segManagerAuditoria.ingresarBitacora(idUsuario, "ingresarFactFactura", "Crea factura");
		}
	}

	public void actionGetHistoriasClinicas() {
		System.out.println("Ac nueva HC: " + newHistoriaC);
		if (newHistoriaC) {
			newHistoriaC = false;
		} else {
			newHistoriaC = true;
			try {
				System.out.println("getHistoriasC idPaciente: " + paciente.getIdPaciente());
				listHistoriasC = pacManagerPaciente.findPacHistoriaClinicaByPaciente(paciente.getIdPaciente());

			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

	}

	public PacPaciente getPaciente() {
		return paciente;
	}

	public void setPaciente(PacPaciente paciente) {
		this.paciente = paciente;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public PacCabeceraHc getPacCabeceraHc() {
		return pacCabeceraHc;
	}

	public void setPacCabeceraHc(PacCabeceraHc pacCabeceraHc) {
		this.pacCabeceraHc = pacCabeceraHc;
	}

	public boolean isExisteCab() {
		return existeCab;
	}

	public void setExisteCab(boolean existeCab) {
		this.existeCab = existeCab;
	}

	public boolean isNewHistoriaC() {
		return newHistoriaC;
	}

	public void setNewHistoriaC(boolean newHistoriaC) {
		this.newHistoriaC = newHistoriaC;
	}

	public int getIdCabecera() {
		return idCabecera;
	}

	public void setIdCabecera(int idCabecera) {
		this.idCabecera = idCabecera;
	}

	public Date getFechaAtencion() {
		return fechaAtencion;
	}

	public void setFechaAtencion(Date fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
	}

	public Time getHoraAtencion() {
		return horaAtencion;
	}

	public void setHoraAtencion(Time horaAtencion) {
		this.horaAtencion = horaAtencion;
	}

	public MedMedico getMedico() {
		return medico;
	}

	public void setMedico(MedMedico medico) {
		this.medico = medico;
	}

	public int getIdMedico() {
		return idMedico;
	}

	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}

	public String getCama() {
		return cama;
	}

	public void setCama(String cama) {
		this.cama = cama;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public String getEnfermedadActual() {
		return enfermedadActual;
	}

	public void setEnfermedadActual(String enfermedadActual) {
		this.enfermedadActual = enfermedadActual;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getEvolucionMedica() {
		return evolucionMedica;
	}

	public void setEvolucionMedica(String evolucionMedica) {
		this.evolucionMedica = evolucionMedica;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public String getEstudios() {
		return estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public String getCuidados() {
		return cuidados;
	}

	public void setCuidados(String cuidados) {
		this.cuidados = cuidados;
	}

	public List<PacHistoriaClinica> getListHistoriasC() {
		return listHistoriasC;
	}

	public void setListHistoriasC(List<PacHistoriaClinica> listHistoriasC) {
		this.listHistoriasC = listHistoriasC;
	}

	public PacHistoriaClinica getHistoriaSelect() {
		return historiaSelect;
	}

	public void setHistoriaSelect(PacHistoriaClinica historiaSelect) {
		this.historiaSelect = historiaSelect;
	}

	public ReserCita getCitaSelect() {
		return citaSelect;
	}

	public void setCitaSelect(ReserCita citaSelect) {
		this.citaSelect = citaSelect;
	}
	
	

}
