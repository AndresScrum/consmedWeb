package consmed.modulos.resercita.view.controller;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import consmed.core.model.entities.MedMedico;
import consmed.core.model.entities.PacPaciente;
import consmed.core.model.entities.ReserCita;
import consmed.modulos.authautorizacionlogin.view.controller.AuthBeanLogin;
import consmed.modulos.medmedico.model.MedManagerMedico;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.pacpaciente.view.controller.PacBeanPaciente;
import consmed.modulos.pacpaciente.view.controller.PacScheduleViewPaciente;
import consmed.modulos.resercita.model.ReserManagerCita;
import consmed.modulos.segauditoria.model.SegManagerAuditoria;
import consmed.modulos.view.util.JSFUtil;

@Named
@SessionScoped
public class ReserBeanCita implements Serializable {
	private static final long serialVersionUID=1L;
	
	private int idMedico;
	private int idMedicoSelect;
	private MedMedico medMedico;
	private PacPaciente paciente;
	private int idUsuario;
	private int idPaciente;
	private Date fecha;
	private Date minFecha;
	private String horaSelect;
	private String asunto;
	private String sintoma;
	private ReserCita selectedCita;
	private List<ReserCita> listCitasMedico;
	private List<ReserCita> listCitasPagado;	
	private List<ReserCita> listCitasNoPagado;
	private List<ReserCita> listCitasDocFecha;
	private Map<String,String> listHorasDispo = new HashMap<String, String>();
	private List<HorasDisponibles> listHoras=new ArrayList<HorasDisponibles>();
	private List<MedMedico> listMedicos;
	
	private boolean showDatosCita;
	
	@EJB
	private ReserManagerCita reserManagerCita;
	@EJB
	private PacManagerPaciente pacManagerPaciente;
	@EJB
	private MedManagerMedico medManagerMedico;
	@EJB
	private SegManagerAuditoria segManagerAuditoria;
	
	@Inject
	private PacBeanPaciente beanPaciente;
	@Inject AuthBeanLogin authBeanLogin;
	@PostConstruct
	public void init() {
		System.out.println("Init Reserv()");
		
		minFecha=new Date();
		if(authBeanLogin.getLogin().getNombre_rol().equals("Médico")) {
			getIdMedico(authBeanLogin.getLogin().getId_usuario());
			//Hoy
			fecha=new Date();
			try {
				listMedicos=medManagerMedico.findAllMedMedicos();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(minFecha); // Configuramos la fecha que se recibe
				calendar.add(Calendar.DAY_OF_YEAR, -1);  // numero de días a añadir, o restar en caso de días<0
				minFecha=calendar.getTime();
				listCitasDocFecha=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
				for (ReserCita reserCita : listCitasDocFecha) {
					System.out.println(reserCita.getAsuntoReser());
				}
			} catch (ParseException e) {
				e.getMessage();
			}
		}else if(authBeanLogin.getLogin().getNombre_rol().equals("Paciente")) {
			setIdPaciente(beanPaciente.getId_paciente());
			setShowDatosCita(true);
			 listHorasDispo= new HashMap<String, String>();
			 try {
				listCitasNoPagado=reserManagerCita.findCitaNoPagadoByPaciente(idPaciente);
			} catch (ParseException e) {
				e.getMessage();
			}
		}
		
			
		
	}
	
	public String irMenuReservas(int idUsuario) {
		setIdUsuario(idUsuario);
		return "reservaMenu";
	}
	public String actionListerSetIdMedico(MedMedico medMedico) {
		setMedMedico(medMedico);
		setIdMedico(medMedico.getIdMedico());
		System.out.println("idMedico: "+medMedico.getIdMedico());
		return "";
	}
	
	public String formatFecha(Date fecha) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String fechaString = dateFormat.format(fecha); 
        return fechaString;
	}
	public void setHorasDisponibles() {
		try {
			String hoy=formatFecha(minFecha);
			String fechaSel=formatFecha(fecha);
			if(hoy.equals(fechaSel)) {
				System.out.println("Se seleccionó la fecha de hoy");
			}else {
				System.out.println("No se seleccionó la fecha de hoy");
			}
			listHorasDispo.clear();
				listHoras.clear();

				System.out.println("idMedicoSelect: "+idMedicoSelect);
				if(idMedicoSelect!=0) {
					this.listCitasMedico=reserManagerCita.findCitaByDocFecha((idMedicoSelect), fecha);
				}else {
					this.listCitasMedico=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
				}
		
		System.out.println(listCitasMedico);
		System.out.println(idMedico);
		
		
        
		int cont=8;
		for(int i=0; i<8; i++) {
			if(cont==13) {
				cont++;
			}
			int nextHora=cont+1;
			String hora="";
			String aux="";
			if(cont<10) {
				hora="0"+cont+":00 - "+nextHora+":00";
				aux="0"+cont+":00:00";
			} else {
				hora=cont+":00 - "+nextHora+":00";
				aux=cont+":00:00";
			}
			if(!existeHora(aux))	{
				HorasDisponibles hd=new HorasDisponibles(hora,String.valueOf(cont));
				System.out.println("add");
				listHoras.add(hd);
				//listHorasDispo.put(hora, String.valueOf(cont));
			}
			cont++;
		}
		}catch(Exception e){
			
		}
		
		if(listHoras.size()>0) {			
			System.out.println("Muestra datos");
			setShowDatosCita(false);
		}else {
			setShowDatosCita(true);
		}
	}
	
	public void changeSelectedValue(HorasDisponibles hd) {
	     setHoraSelect(hd.getTime());
	 }
	
	public boolean existeHora(String time) {
		boolean res=false;
		
		for (ReserCita rc : listCitasMedico) {
			String noDispo=rc.getHoraReser().toString();
			if(noDispo.equals(time)) {
				System.out.println("Existe cita");
				return true;					
			}
		}
		return res;
	}
	
	public String actionReservar(int idUsuario) {
		try {
			
		System.out.println("actionReservar()");
		System.out.println("IdUsuario: "+idUsuario);
		PacPaciente pacPaciente=getIdPaciente(idUsuario);
		System.out.println(fecha);
		System.out.println(horaSelect);
		//HoraSelect convertir a time
		int hora=Integer.parseInt(horaSelect);
		@SuppressWarnings("deprecation")
		Time ti=new Time(hora, 0, 0);
		reserManagerCita.ingresarReserCita(pacPaciente, medMedico, fecha, ti, asunto, sintoma);		
		segManagerAuditoria.ingresarBitacora(idUsuario, "ingresarReserCita","Paciente crea una reserva de la cita");
		JSFUtil.crearMensajeInfo("Reserva exitosa");

		}catch(Exception e) {
			JSFUtil.crearMensajeError("Hubo un error");
		}
		return "reservaMenu";
	}
	
	public PacPaciente getIdPaciente(int idUsuario) {
		PacPaciente pac=pacManagerPaciente.findPacienteByIdUsuario(idUsuario);
		System.out.println("IdPaciente: "+pac.getIdPaciente());
		return pac;
	}
	
	/**
	 * Obtiene id de medico
	 * @param idUsuario
	 * @return
	 */
	public void getIdMedico(int idUsuario) {
		MedMedico med=medManagerMedico.findMedMedicoByUsuario(idUsuario);
		System.out.println("IdMedico: "+med.getIdMedico());
		setIdMedico(med.getIdMedico());
	}

	
	public Date getMinFecha() {
		return minFecha;
	}
	

	public String actionGetCitasNoPagadosPac(int idUsuario) {
		PacPaciente pac=getIdPaciente(idUsuario);
		try {
			listCitasNoPagado=reserManagerCita.findCitaNoPagadoByPaciente(pac.getIdPaciente());
			System.out.println("ListcitasnoPagado: "+listCitasNoPagado.size());
		} catch (ParseException e) {
			JSFUtil.crearMensajeError("Hubo un error");
		}
		return "reservaMenu";
	}

	public String actionCancelarCita() {
		ReserCita cita=selectedCita;
		idUsuario=authBeanLogin.getLogin().getId_usuario();
		reserManagerCita.actualizarEstadoCita(cita, false);
		System.out.println("Usuario auditoria: "+idUsuario);
		segManagerAuditoria.ingresarBitacora(idUsuario, "actionCancelarCita","Paciente canceló cita");
		try {
			listCitasNoPagado=reserManagerCita.findCitaNoPagadoByPaciente(idPaciente);
			JSFUtil.crearMensajeInfo("Se canceló cita");
		} catch (ParseException e) {
			e.getMessage();
		}
		return "reservaMenu";
	}
	
	/**
	 * Obtiene citas del presente día
	 */
	public String actionGetCitasDocHoy() {
		Date fecha=new Date();
		try {
			this.listCitasDocFecha=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
		} catch (ParseException e) {
			e.getMessage();
		}
		return "";
	}
	
	/**
	 * Obtiene citas por fecha
	 */
	public String actionGetCitasDocFecha() {
		try {
			System.out.println("getCitasMed");
			this.listCitasDocFecha=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
		} catch (ParseException e) {
			e.getMessage();
		}
		
		return "";
	}
	
	/**
	 * Reagendar cita
	 * @return
	 */
	public String actionReagendar() {
		System.out.println("Reagendar cita");
		ReserCita cita=selectedCita;
		try {
			MedMedico medico=medManagerMedico.findMedicoById((idMedicoSelect));
			int hora=Integer.parseInt(horaSelect);
			@SuppressWarnings("deprecation")
			Time ti=new Time(hora, 0, 0);
			cita.setMedMedico(medico);
			cita.setFechaReser(fecha);
			cita.setHoraReser(ti);
			reserManagerCita.actualizarCita(cita);
			JSFUtil.crearMensajeInfo("Se reagendó");	
			fecha=new Date();
			this.listCitasDocFecha=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			JSFUtil.crearMensajeError("No se pudo reagendar");
		}
		return "";
	}

	
	

	

	public List<ReserCita> getListCitasMedico() {
		return listCitasMedico;
	}

	public void setListCitasMedico(List<ReserCita> listCitasMedico) {
		this.listCitasMedico = listCitasMedico;
	}

	public int getIdMedico() {
		return idMedico;
	}


	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}


	public Date getFecha() {
		return fecha;
	}
	

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isShowDatosCita() {
		return showDatosCita;
	}

	public void setShowDatosCita(boolean showDatosCita) {
		this.showDatosCita = showDatosCita;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public MedMedico getMedMedico() {
		return medMedico;
	}

	public void setMedMedico(MedMedico medMedico) {
		this.medMedico = medMedico;
	}

	public String getHoraSelect() {
		return horaSelect;
	}

	public void setHoraSelect(String hora) {
		this.horaSelect = hora;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getSintoma() {
		return sintoma;
	}

	public void setSintoma(String sintoma) {
		this.sintoma = sintoma;
	}

	public Map<String, String> getListHorasDispo() {
		return listHorasDispo;
	}

	public void setListHorasDispo(Map<String, String> listHorasDispo) {
		this.listHorasDispo = listHorasDispo;
	}

	public List<HorasDisponibles> getListHoras() {
		return listHoras;
	}

	public void setListHoras(List<HorasDisponibles> listHoras) {
		this.listHoras = listHoras;
	}

	public List<ReserCita> getListCitasPagado() {
		return listCitasPagado;
	}

	public void setListCitasPagado(List<ReserCita> listCitasPagado) {
		this.listCitasPagado = listCitasPagado;
	}

	public List<ReserCita> getListCitasNoPagado() {
		return listCitasNoPagado;
	}

	public void setListCitasNoPagado(List<ReserCita> listCitasNoPagado) {
		this.listCitasNoPagado = listCitasNoPagado;
	}
	public int getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}
	public ReserCita getSelectedCita() {
		return selectedCita;
	}
	public void setSelectedCita(ReserCita selectedCita) {
		this.selectedCita = selectedCita;
	}
	public List<ReserCita> getListCitasDocFecha() {
		return listCitasDocFecha;
	}
	public void setListCitasDocFecha(List<ReserCita> listCitasDocFecha) {
		this.listCitasDocFecha = listCitasDocFecha;
	}

	public PacPaciente getPaciente() {
		return paciente;
	}

	public void setPaciente(PacPaciente paciente) {
		this.paciente = paciente;
	}

	public int getIdMedicoSelect() {
		return idMedicoSelect;
	}

	public void setIdMedicoSelect(int idMedicoSelect) {
		this.idMedicoSelect = idMedicoSelect;
	}

	public List<MedMedico> getListMedicos() {
		return listMedicos;
	}

	public void setListMedicos(List<MedMedico> listMedicos) {
		this.listMedicos = listMedicos;
	}

	
	
	
	
}
