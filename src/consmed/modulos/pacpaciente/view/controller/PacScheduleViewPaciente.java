package consmed.modulos.pacpaciente.view.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import consmed.core.model.entities.PacPaciente;
import consmed.core.model.entities.ReserCita;
import consmed.modulos.authautorizacionlogin.view.controller.AuthBeanLogin;
import consmed.modulos.login.model.Login;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.resercita.model.ReserManagerCita;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class PacScheduleViewPaciente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ScheduleModel eventModel;

	private ScheduleModel lazyEventModel;
	private int idPaciente;
	private int idUsuario;
	private ScheduleEvent event = new DefaultScheduleEvent();

	@EJB
	private ReserManagerCita resMan;
	@EJB
	private PacManagerPaciente pacManagerPaciente;
	
	@Inject
	PacBeanPaciente beanPaciente;

	@PostConstruct
	public void init() {
		setIdPaciente(beanPaciente.getId_paciente());
		cargarCitasCalendario();		
		System.out.println("Init() pacSeche.. ");
		//System.out.println("idUsuario: "+login.getId_usuario());
		lazyEventModel = new LazyScheduleModel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			@Override
			public void loadEvents(Date start, Date end) {
				Date random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

				random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
			} **/
		};
	}

	public int searchIdPaciente(int idUsuario) {		
		PacPaciente pac=pacManagerPaciente.findPacienteByIdUsuario(idUsuario);
		System.out.println("IdPaciente: "+pac.getIdPaciente());
		setIdPaciente(pac.getIdPaciente());
		return getIdPaciente();
	}
	// Carga citas calendario
	public void cargarCitasCalendario() {
		try {
			//int idPaciente=searchIdPaciente(authBeanLogin.getLogin().getId_usuario());
			List<ReserCita> list = resMan.findCitaByPaciente(idPaciente);
			eventModel = new DefaultScheduleModel();
			for (ReserCita reserCita : list) {
				Date dateI= getFechaEvento(reserCita.getFechaReser(), reserCita.getHoraReser());
				Date dateF=getFechaEventoHasta(dateI,reserCita.getHoraReser() );
				eventModel.addEvent(new DefaultScheduleEvent(reserCita.getAsuntoReser(), dateI, dateF));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getFechaEvento(Date fecha, Time hora) {
		// Establecemos la fecha que deseamos en un Calendario
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + hora.getHours()-10);		
		return cal.getTime();
	}
	public Date getFechaEventoHasta(Date fecha,Time hora ) {
		// Establecemos la fecha que deseamos en un Calendario
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
		return cal.getTime();

	}

	public Date getRandomDate(Date base) {
		Calendar date = Calendar.getInstance();
		date.setTime(base);
		date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1); // set random day of month

		return date.getTime();
	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}

	private Calendar today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar;
	}

	private Date previousDay8Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
		t.set(Calendar.HOUR, 8);

		return t.getTime();
	}

	private Date previousDay11Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
		t.set(Calendar.HOUR, 11);

		return t.getTime();
	}

	


	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void addEvent() {
		if (event.getId() == null)
			eventModel.addEvent(event);
		else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	
	
}
