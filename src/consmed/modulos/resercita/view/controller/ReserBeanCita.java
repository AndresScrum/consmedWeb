package consmed.modulos.resercita.view.controller;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import consmed.core.model.entities.MedMedico;
import consmed.core.model.entities.PacPaciente;
import consmed.core.model.entities.ReserCita;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.resercita.model.ReserManagerCita;
import consmed.modulos.segauditoria.model.SegManagerAuditoria;
import consmed.modulos.view.util.JSFUtil;

@Named
@ViewScoped
public class ReserBeanCita implements Serializable {
	private static final long serialVersionUID=1L;
	
	private int idMedico;
	private MedMedico medMedico;
	private int idUsuario;
	private Date fecha;
	private Date minFecha;
	private String horaSelect;
	private String asunto;
	private String sintoma;
	private ReserCita cita;
	private List<ReserCita> listCitasMedico;
	private List<ReserCita> listCitasPagado;	
	private List<ReserCita> listCitasNoPagado;
	private Map<String,String> listHorasDispo = new HashMap<String, String>();
	private List<HorasDisponibles> listHoras=new ArrayList<HorasDisponibles>();
	
	private boolean showDatosCita;
	
	@EJB
	private ReserManagerCita reserManagerCita;
	@EJB
	private PacManagerPaciente pacManagerPaciente;
	@EJB
	private SegManagerAuditoria segManagerAuditoria;
	
	@PostConstruct
	public void init() {
		System.out.println("Init Reserv()");
		minFecha=new Date();
		setShowDatosCita(true);
		 listHorasDispo= new HashMap<String, String>();
		 
			
		
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
		this.listCitasMedico=reserManagerCita.findCitaByDocFecha(idMedico, fecha);
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

	
	
	
}
