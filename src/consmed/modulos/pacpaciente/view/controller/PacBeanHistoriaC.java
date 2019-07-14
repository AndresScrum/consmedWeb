package consmed.modulos.pacpaciente.view.controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import consmed.core.model.entities.PacCabeceraHc;
import consmed.core.model.entities.PacPaciente;
import consmed.modulos.pacpaciente.model.PacManagerPaciente;
import consmed.modulos.resercita.view.controller.ReserBeanCita;
import consmed.modulos.view.util.JSFUtil;

@Named
@SessionScoped
public class PacBeanHistoriaC implements Serializable {
	private static final long serialVersionUID=1L;
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
	
	@EJB
	private PacManagerPaciente pacManagerPaciente;
	
	
	
	@PostConstruct
	public void init() {
		System.out.println("Init() HC");
		System.out.println(paciente);
		if(paciente!=null) {
			getCabeceraHc();
		}
	}
	
	
	
	public String actionHistoriaClinica(PacPaciente pacienteC) {
		paciente=pacienteC;
	//	setPaciente(paciente);
		System.out.println("Id paciente sele: "+paciente.getIdPaciente());
		if(paciente!=null) {
			getCabeceraHc();
		}
		return "historiaClinica?faces-redirect=true";
	}
	
	public String actionGuardarCabeceraHc() {
		//PacPaciente paciente=pacManagerPaciente.findPacienteById(17);
		//System.out.println(beanCita.getIdPaciente());
		System.out.println(fechaNacimiento);
		System.err.println(peso);
		System.err.println(altura);
		System.err.println(genero);
		System.err.println(ocupacion);
		try {
			pacManagerPaciente.ingresarPacCabeceraHc(paciente, fechaNacimiento, peso, altura, genero, ocupacion);
			JSFUtil.crearMensajeInfo("Ingreso correcto!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Ingreso cabecera hc");
		getCabeceraHc();
		return "";
	}
	
	public void getCabeceraHc() {
		pacCabeceraHc=pacManagerPaciente.getPacienteCabeceraHc(paciente.getIdPaciente());
		System.out.println(pacCabeceraHc);
		if(pacCabeceraHc!=null) {
			fechaNacimiento=pacCabeceraHc.getFechaNacimiento();
			peso=Double.parseDouble(pacCabeceraHc.getPeso().toString());
			altura=Double.parseDouble(pacCabeceraHc.getAltura().toString());
			genero=pacCabeceraHc.getGenero();
			ocupacion=pacCabeceraHc.getOcupacion();
			idCabecera=pacCabeceraHc.getIdCabecera();
			existeCab=true;
		}else {
			existeCab=false;
		}
		
	}
	
	public String actionUpdateCabeceraHc() {
		PacCabeceraHc cabeceraCargar=new PacCabeceraHc();
		cabeceraCargar.setIdCabecera(idCabecera);
		cabeceraCargar.setPacPaciente(paciente);
		cabeceraCargar.setFechaNacimiento(fechaNacimiento);
		cabeceraCargar.setPeso(new BigDecimal(peso));
		cabeceraCargar.setAltura(new BigDecimal(altura));
		cabeceraCargar.setGenero(genero);
		cabeceraCargar.setOcupacion(ocupacion);
		try {
			pacManagerPaciente.editarPacCabeceraHc(cabeceraCargar);
			JSFUtil.crearMensajeInfo("Actualización correcta!");
		} catch (Exception e) {
			JSFUtil.crearMensajeError("Error al actualizar");
		}
		
		return "";
	}
	
	public void actionNuevaHistoriaC() {
		System.out.println("Ac nueva HC: "+newHistoriaC);
		if(newHistoriaC) {
			newHistoriaC=false;
		}else {
			newHistoriaC=true;
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
	
	
	

}
