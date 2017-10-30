package com.qualitas.portal.commons.util.reports.pdf.impl;

import static com.qualitas.portal.commons.ApplicationConstants.AGEN_ESP_OCULTA_PRIMAS;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.qualitas.portal.commons.model.LabelValueBean;
import com.qualitas.portal.commons.model.pdf.CoberturasPdfBean;
import com.qualitas.portal.commons.model.pdf.PdfPolizaBean;
import com.qualitas.portal.commons.util.FormatDecimal;
import com.qualitas.portal.commons.util.reports.pdf.renovaciones.BasePdfGeneratePolicy;
import com.qualitas.portal.commons.util.reports.pdf.tools.CreatePdf;
import com.qualitas.portal.commons.util.reports.pdf.tools.DateFormat;
import com.qualitas.portal.commons.util.reports.pdf.tools.Numtotext;
import com.qualitas.reports.service.AltoRiesgoService;

public class PdfGeneratePolicy_MX_Impl extends BasePdfGeneratePolicy {
	
	private AltoRiesgoService altoRiesgoService;
	
	private static String DEDUCIBLE_MINIMOP1="El deducible mínimo a pagar en las coberturas Daños Materiales y Robo Total para Vehículos Fronterizos y ";
	private static String DEDUCIBLE_MINIMOP2="Regularizados es de $1,000 y $2,000 (pesos 00/100 M.N.) respectivamente";
	
	private static String SEG_OBL_PART_1="La cobertura de Responsabilidad Civil tiene el carácter de seguro obligatorio, por lo que no podrá cesar en sus efectos,";
	private static String SEG_OBL_PART_2=" rescindirse ni darse por terminada con anterioridad a la fecha de terminación de su vigencia y el pago deberá ";
	private static String SEG_OBL_PART_3="efectuarse de contado.";
	
	private static String SEG_OBL_PUB_1="Las coberturas de Responsabilidad Civil y Responsabilidad Civil Pasajero tienen el carácter de seguro obligatorio,";
	private static String SEG_OBL_PUB_2="por lo que no podrán cesar en sus efectos, rescindirse ni darse por terminadas con anterioridad a la fecha de";
	private static String SEG_OBL_PUB_3="terminación de su vigencia y el pago deberá efectuarse de contado.";

	private static final Integer ANIO_ALTO_RIESGO_RT = Calendar.getInstance().get(Calendar.YEAR) - 2;
	private static final String ROBO_TOTAL_ID = "2";
	
	private static final Logger log = Logger.getLogger(PdfGeneratePolicy_MX_Impl.class);
	
	public PdfGeneratePolicy_MX_Impl(Properties properties) {
		super(properties);
	}
	
	public void creaPdf (List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(0);
	
		if (polizaVO.getAmis().trim().equals("7")||polizaVO.getAmis().trim().equals("8")||polizaVO.getAmis().trim().equals("9")||
			polizaVO.getAmis().trim().equals("71")||polizaVO.getAmis().trim().equals("72")||polizaVO.getAmis().trim().equals("74")||
			polizaVO.getAmis().trim().equals("79")
		   ){
			 //creaPdfTur( arrPolizas, salida, membretado);
			 creaPdfTurActual( arrPolizas, salida, membretado);
		}
		else {

//			java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
			int anioFormato = 0;
			int mesFormato = 0;  
			int diaFormato = 0;  
			
			anioFormato = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
			mesFormato = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
			diaFormato = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
			
			if(anioFormato==2016){				
				if (mesFormato>11) {
					creaPdfNormal_cambios_conducef_une( arrPolizas, salida, membretado);
				}else if(mesFormato==11){
					if (diaFormato>=4){
						creaPdfNormal_cambios_conducef_une( arrPolizas, salida, membretado);
					}
					else{
						creaPdfNormal_cambios_conducef( arrPolizas, salida, membretado);
					}
				}else if(mesFormato<11){
					creaPdfNormal_cambios_conducef( arrPolizas, salida, membretado);
				}
				else{
					creaPdfNormal_cambios_conducef( arrPolizas, salida, membretado);
				}
				//creaPdfNormal_cambios_conducef_bis( arrPolizas, salida, membretado);
				//creaPdfNormal_cambios_conducef_bis_prueba( arrPolizas, salida, membretado);
			}else if(anioFormato<2016){
				creaPdfNormal( arrPolizas, salida, membretado);
			}else if (anioFormato>2016) {
				creaPdfNormal_cambios_conducef_une( arrPolizas, salida, membretado);
			}
	
//			if (anioFormato>=2016) {
//				creaPdfNormal_cambios_conducef_une( arrPolizas, salida, membretado);
//			}
//			if(anioFormato>=2016){
//				creaPdfNormal_cambios_conducef( arrPolizas, salida, membretado);
//				//creaPdfNormal_cambios_conducef_bis( arrPolizas, salida, membretado);
//				//creaPdfNormal_cambios_conducef_bis_prueba( arrPolizas, salida, membretado);
//			}else
//			{
//				creaPdfNormal( arrPolizas, salida, membretado);
//			}
			
			
			
			
			 //creaPdfNormal( arrPolizas, salida, membretado);
			//creaPdfNormal_cambios_conducef( arrPolizas, salida, membretado);
//			creaPdfNormal_08_oct_2015( arrPolizas, salida, membretado);
		}
		
//		if (polizaVO.getNumPoliza().substring(2,polizaVO.getNumPoliza().length()-6).equals("0840068173")){
//			 creaPdfTur( arrPolizas, salida, membretado);
//		}
//		else {
//			 creaPdfNormal( arrPolizas, salida, membretado);
//		}
	}

	
	/**
	 * PdfPolizaSeguro
	 * ===============
	 * Método que crea el pdf de poliza de seguro.
	 * 
	 * @param arrPolizas .- contiene las polizas que se pintan en el pdf.
	 * @param salida .- es el tipo de salida en el que se va a generar el pdf.
	 */
	//ANDRES-MEM
	//public void creaPdf(ArrayList arrPolizas,OutputStream salida){
	public void creaPdfNormal_cambios_conducef(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;

				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;

				for(int page=0;page<numpages;page++){//número de paginas				
					//document.newPage();//5-ene-2016

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();

						//ANDRES-MEM
//						if (membretado==null||membretado.equals("S")){
//							cb=pdf.addRectAngColorGreenWater(cb,35,718,535,30);
//							cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
//							//document=pdf.addImage(document,35,725,130,50,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/logoQpoliza.jpg");
//							//recibo.get(page).getDirImagen()+"logoQpoliza.jpg"
//							document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
//						}
						
						if(page==0){
							
//							cb=pdf.addRectAng(cb,23,717,562,46);//seccion 1
//							cb=pdf.addRectAng(cb,348,707,237,24);//seccion 1
//							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 0/1",true,1);
//							
//							cb=pdf.addLabel(cb,60,695,10,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//							cb=pdf.addLabel(cb,355,695,10,"POLIZA",true,1);
//							cb=pdf.addLabel(cb,435,695,10,"ENDOSO",true,1);
//							cb=pdf.addLabel(cb,525,695,10,"INCISO",true,1);
//							
//							if(polizaVO != null){
//								String inciso="000";
//								String incisoAux;
//								if(polizaVO.getNumPoliza()!=null){
//									sizeNumPoliza=polizaVO.getNumPoliza().length();
//									cb=pdf.addLabel(cb,355,685,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//									cb=pdf.addLabel(cb,435,685,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//								}
//								if(polizaVO.getInciso()!=null){
//									inciso = inciso+polizaVO.getInciso();								
//									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//									cb=pdf.addLabel(cb,525,685,10,incisoAux,false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,669,562,77);//seccion 2
//							cb=pdf.addRectAng(cb,401,657,184,14);//seccion 2
//							cb=pdf.addRectAng(cb,401,618,184,26);//seccion 2
//							cb=pdf.addLabel(cb,27,658,10,"DATOS DEL CONTRATANTE:",true,1);
//							cb=pdf.addLabel(cb,250,658,10,"RFC:",true,1);
//							cb=pdf.addLabel(cb,401,658,10,"RENUEVA A:",false,1);
//							cb=pdf.addLabel(cb,435,646,10,"VIGENCIA:",true,1);
//							cb=pdf.addLabel(cb,27,630,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,401,633,10,"Desde las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,401,621,10,"Hasta las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,27,616,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,220,616,10,"Estado:",false,1);
//							cb=pdf.addLabel(cb,27,602,10,"Beneficiario Preferente:",false,1);
//							cb=pdf.addLabel(cb,410,606,10,"Fecha de vencimiento del pago",false,1);
//							if(polizaVO.getPolizaAnterior()!=null){
//								cb=pdf.addLabel(cb,472,658,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}
//							cb=pdf.addLabel(cb,472,658,10,"RENUEVAxxx",false,1);	
//							if(polizaVO != null){		
//								if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,511,633,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//								}
//								if(polizaVO.getFchFin()!=null){
//									cb=pdf.addLabel(cb,511,621,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//								}
//								if(polizaVO.getFechaLimPago()!=null){																												
//									cb=pdf.addLabel(cb,450,594,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//							}
//							
//							
//							if(polizaVO != null){
//								String nombre=""; 
//								if(polizaVO.getNombre()!=null){
//									nombre=polizaVO.getNombre()+" ";}
//								if(polizaVO.getApePate()!=null){
//									nombre=nombre+polizaVO.getApePate()+" ";}
//								if(polizaVO.getApeMate()!=null){
//									nombre=nombre+polizaVO.getApeMate()+" ";}
//
//								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//									nombre=nombre+" Y/O "+polizaVO.getConductor();
//								}	
//								cb=pdf.addLabel(cb,27,646,10,nombre,false,1);
//								if(datosCliente){										
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,75,630,10,calle,false,1);}
//
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,55,616,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,260,616,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,280,658,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getBeneficiario()!=null){
//										cb=pdf.addLabel(cb,135,602,10,polizaVO.getBeneficiario(),false,1);}
//									cb=pdf.addLabel(cb,135,602,10,"PRUEBA BENEFICIARIO PREFERENTE XXX",false,1);
//
//
//								}						
//
//							}				
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,590,562,62);//seccion 3
//							cb=pdf.addRectAng(cb,23,590,562,13);//seccion 3
//							cb=pdf.addLabel(cb,215,580,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,1);
//							cb=pdf.addLabel(cb,27,567,10,"Clave y Marca",false,1);
//							cb=pdf.addLabel(cb,27,556,10,"Tipo:",false,1);
//							cb=pdf.addLabel(cb,190,556,10,"Modelo:",false,1);
//							cb=pdf.addLabel(cb,320,556,10,"Color:",false,1);
//							cb=pdf.addLabel(cb,450,556,10,"Ocupantes:",false,1);
//							cb=pdf.addLabel(cb,27,544,10,"Serie:",false,1);
//							cb=pdf.addLabel(cb,190,544,10,"Motor:",false,1);
//							cb=pdf.addLabel(cb,390,544,10,"Placas:",false,1);
//
//							if(polizaVO != null){							
//								if(polizaVO.getAmis()!=null){
//									cb=pdf.addLabel(cb,97,567,10,polizaVO.getAmis(),false,1);}							
//								if(polizaVO.getDescVehi()!=null){
//									cb=pdf.addLabel(cb,127,567,10,polizaVO.getDescVehi(),false,1);}											
//								if(polizaVO.getTipo()!=null){
//									if(polizaVO.getTipo().length()>18){	
//										cb=pdf.addLabel(cb,57,556,10,polizaVO.getTipo().substring(0, 19),false,1);
//									}else{
//										cb=pdf.addLabel(cb,57,556,10,polizaVO.getTipo(),false,1); 
//									}	
//								}
//								if(polizaVO.getVehiAnio()!=null){												
//									cb=pdf.addLabel(cb,230,556,10,polizaVO.getVehiAnio(),false,1);}
//								if(polizaVO.getColor()!=null){
//
//									if(polizaVO.getColor().equals("SIN COLOR")){
//
//										cb=pdf.addLabel(cb,350,556,10,"",false,1);		
//									}else{
//										cb=pdf.addLabel(cb,350,556,10,polizaVO.getColor(),false,1);
//									}
//								}
//								//ANDRES-PASAJEROS
//								if (polizaVO.getNumPasajeros()!=null){
//									cb=pdf.addLabel(cb,507,556,10,polizaVO.getNumPasajeros(),false,1);
//								}
//								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,507,556,10,polizaVO.getNumOcupantes(),false,1);
//								}
//								if(polizaVO.getNumPlaca()!=null){												
//									cb=pdf.addLabel(cb,430,544,10,polizaVO.getNumPlaca(),false,1);}
//								if(polizaVO.getNumSerie()!=null){					
//									cb=pdf.addLabel(cb,60,544,10,polizaVO.getNumSerie(),false,1);}
//								if(polizaVO.getNumMotor()!=null){					
//									cb=pdf.addLabel(cb,224,544,10,polizaVO.getNumMotor(),false,1);}
//								
//								if (polizaVO.getCveServ().trim().equals("3")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,532,10,"Tipo de Carga",false,1);
//										cb=pdf.addLabel(cb,110,532,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//										cb=pdf.addLabel(cb,200,532,10,polizaVO.getTipoCarga()+" : ",true,2);}
//									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//										String descAux = "";
//										String valorRemolque="";
//										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//											descAux=polizaVO.getDescCarga();
//										}
//										
//										if(polizaVO.getDobleRemolque()!= null){
//											if(polizaVO.getDobleRemolque().equals("S")){
//												valorRemolque = "2° Remolque: AMPARADO";
//											}else{
//												valorRemolque = "2° Remolque: EXCLUIDO";
//											}
//										}								
//										if(descAux != "" || valorRemolque != ""){
//											cb=pdf.addLabel(cb,260,532,10,descAux+"  "+valorRemolque,false,1);
//										}
//										
//									}
//								}
//								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,532,10,"Tipo de Carga "+polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//									}
//								}
//								if(polizaVO.getNoEconomico()!=null){
//									cb=pdf.addLabel(cb,390,532,10,"No. Económico: "+polizaVO.getNoEconomico(),false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,526,562,342);//seccion 4
//							cb=pdf.addLabel(cb,27,516,10,"Estimado asegurado nos permitimos informarle que si durante la vigilancia de su seguro las Condiciones Generales",false,1);
//							cb=pdf.addLabel(cb,27,504,10,"presentaran alguna modificación estas serán publicadas en nuestra página web www.qualitas.com.mx para su consulta,",false,1);
//							cb=pdf.addLabel(cb,27,492,10,"descargar o impresión.",false,1);
//							cb=pdf.addLabel(cb,27,480,10,"",false,1);
//							cb=pdf.addLabel(cb,27,468,10,"Estimado Asegurado con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro",false,1);
//							cb=pdf.addLabel(cb,27,456,10,"de automóvil que acaba de adquirir, Quálitas, Compaña de Seguros, lo invita a que lea sus Condiciones Generales ",false,1);
//							cb=pdf.addLabel(cb,27,444,10,"mismas que se adjuntan a esta póliza, o bien, puede ustede ingresar a nuestra página Web, en la siguiente dirección",false,1);
//							cb=pdf.addLabel(cb,27,432,10,"electrónica:",false,1);
//							cb=pdf.addLabel(cb,82,432,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
//							cb=pdf.addlineH(cb,82,430,343);
//							cb=pdf.addLabel(cb,27,420,10,"",false,1);
//							cb=pdf.addLabel(cb,27,408,10,"Unidad Especializada de Atención a Usuario (UNE) Domicilio Avenida San Jerónimo número 478, piso 4°, Colonia Jardines",false,1);
//							cb=pdf.addLabel(cb,27,396,10,"del Pedregal, Delegación Alvaro Obregón, México, Distrito Federal, Código Postal 01900, horario de atención de Lunes a",false,1);
//							cb=pdf.addLabel(cb,27,384,10,"Viernes de 9:00 a.m. a 18:00 p.m., teléfono 01 (55) 15556067, correo electrónico uauf@quialitas.com.mx",false,1);
//							cb=pdf.addLabel(cb,27,372,10,"",false,1);
//							cb=pdf.addLabel(cb,27,360,10,"Comisión Nacional de la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
//							cb=pdf.addLabel(cb,27,348,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono (55) 5340 0999 y ",false,1);
//							cb=pdf.addLabel(cb,27,336,10,"(01 800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría.condusef.gob.mx",false,1);
//							cb=pdf.addLabel(cb,27,324,10,"",false,1);
//							cb=pdf.addLabel(cb,27,312,10,"Artículo 25 de la Ley sore el Contrato del Seguro. Si el contenido de la póliza o sus modificaciones no concordaren con",false,1);
//							cb=pdf.addLabel(cb,27,300,10,"la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan al día en que",false,1);
//							cb=pdf.addLabel(cb,27,288,10,"reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de sus modificaciones",false,1);
//							cb=pdf.addLabel(cb,27,276,10,"",false,1);
//							if(polizaVO.getDescConVig()!=null){		
//								cb=pdf.addLabel(cb,27,264,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables ("+ polizaVO.getDescConVig(),false,1);
//								cb=pdf.addLabel(cb,510,264,10,")misma que",false,1);
//							}
//							else{
//								cb=pdf.addLabel(cb,27,264,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables (__/__ ____-__)misma que",false,1);
//							}
//							
//							
//							
//							cb=pdf.addLabel(cb,27,252,10,"además puede consultar e imprimir en nuestra página www.qualitas.com.mx",false,1);
//							
//							
//							cb=pdf.addRectAng(cb,23,182,353,86);//seccion 5
//							cb=pdf.addRectAng(cb,23,182,353,12);//seccion 5
//							
//							cb=pdf.addLabel(cb,27,172,10,"OFICINA DE SERVICIO",true,1);
//							cb=pdf.addLabel(cb,27,158,10,"Agente:",false,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
//							cb=pdf.addLabel(cb,27,134,10,"Oficina:",false,1);
//							cb=pdf.addLabel(cb,27,122,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,300,122,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,27,110,10,"Colonia:",false,1);
//							cb=pdf.addLabel(cb,27,98,10,"Télefono:",false,1);
//							cb=pdf.addLabel(cb,194,98,10,"Fax:",false,1);
//							
//							
//							
//							
//							if(polizaVO!= null){
//								//la siguiente información va del nombre del agente a telefono nacional
//								String nombreAgente="";
//								if(polizaVO.getNombreAgente()!=null){
//									nombreAgente=polizaVO.getNombreAgente()+" ";}
//								if(polizaVO.getPateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
//								if(polizaVO.getMateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
//								if (polizaVO.getClavAgente().equals("52017")) {
//									nombreAgente = "";
//								}
//								cb=pdf.addLabel(cb,67,158,10,nombreAgente,false,1);
//								if(polizaVO.getClavAgente()!=null){
//									cb=pdf.addLabel(cb,67,146,10,polizaVO.getClavAgente(),false,1);}
//								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//									if(polizaVO.getTelComerAgente()!=null){
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelComerAgente(),false,1);
//									}
//									else{
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelPartAgente(),false,1);
//									}
//
//								}
//								cb=pdf.addlineH(cb,20,143,360);
//								if(polizaVO.getDescOficina()!=null){
//									cb=pdf.addLabel(cb,77,134,10,polizaVO.getDescOficina(),false,1);}
////								if(polizaVO.getPoblacionOficina()!=null){
////									cb=pdf.addLabel(cb,350,198,10,polizaVO.getPoblacionOficina(),false,2);}							
//								if(polizaVO.getCalleOficina()!=null){						
//									cb=pdf.addLabel(cb,77,122,10,polizaVO.getCalleOficina(),false,1);}
//								if(polizaVO.getCodPostalOficina()!=null){										
//									cb=pdf.addLabel(cb,330,122,10,polizaVO.getCodPostalOficina(),false,1);}
//								if(polizaVO.getColoniaOficina()!=null){										
//									cb=pdf.addLabel(cb,77,110,10,polizaVO.getColoniaOficina(),false,1);}
//								if(polizaVO.getTelOficina()!=null){
//									cb=pdf.addLabel(cb,77,98,10,polizaVO.getTelOficina(),false,1);}
//								if(polizaVO.getFaxOficina()!=null){					
//									cb=pdf.addLabel(cb,213,98,10,polizaVO.getFaxOficina(),false,1);}
//							}
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,386,182,199,26);//seccion 6
//							cb=pdf.addLabel(cb,388,172,10,"CONDICIONES VIGENTES",false,1);
//							cb=pdf.addLabel(cb,388,160,10,"TARIFA APLICADA",false,1);
//							
//							
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,605,172,10,polizaVO.getDescConVig(),false,2);
//							}
//							
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									cb=pdf.addLabel(cb,570,160,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									cb=pdf.addLabel(cb,570,160,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//							}	
//							
//							
//							
//							if(polizaVO != null){
//								String lugar="";
//								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//								cb=pdf.addLabel(cb,490,146,10,lugar,false,0);																
//								if(polizaVO.getFchEmi()!=null){
//									cb=pdf.addLabel(cb,490,134,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							}
//
//							if(polizaVO.getDirImagen()!=null){
//								document=pdf.addImage(document,438,105,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							}
//							cb=pdf.addLabel(cb,490,98,10,"FUNCIONARIO AUTORIZADO",false,0);	
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,94,562,71);//seccion 7
//							cb=pdf.addLabel(cb,27,84,10,"Quálitas Compañía de Seguros, S.A de C.V. (en lo sucesivo La Compañía), asegura de aucerdo a las Condiciones",false,1);
//							cb=pdf.addLabel(cb,27,72,10,"Generales y Especiales de esta Póliza, el vehiculo asegurado contra pérdidas o daños causados por cualquiera de los",false,1);
//							cb=pdf.addLabel(cb,27,60,10,"Riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							
							
							
							
							document.newPage();
							
							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
//								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 1/1",true,1);							
							

							
							
							
							
							cb=pdf.addLabel(cb,100,708,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",false,1);										
							cb=pdf.addLabel(cb,400,708,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,708,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,525,708,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,400,684,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,460,684,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,684,10,incisoAux,false,1);
								}
							}


							//**********CUERPO		
							//cb=pdf.addRectAngColor(cb,23,661,562,12);
							//cb=pdf.addRectAng(cb,23,648,562,43);
							cb=pdf.addRectAngColor(cb,23,684,562,12);
							cb=pdf.addRectAng(cb,23,671,562,73);


							//***********asegurado					
							cb=pdf.addLabel(cb,290,674,10,"INFORMACIÓN DEL ASEGURADO",true,0);		
//							cb=pdf.addLabel(cb,27,654,10,"Nombre:",false,1);
//							cb=pdf.addLabel(cb,27,639,10,"RFC:",false,1);
//							cb=pdf.addLabel(cb,27,624,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,27,609,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,167,609,10,"Estado:",false,1);
							cb=pdf.addLabel(cb,27,612,10,"Vigencia           Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,330,612,10,"Hasta las 12:00 P.M. del:",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,27,652,10,nombre,false,1);
								
												
									//la información siguiente va de descripción del vehiculo a tipo de carga							
									if(polizaVO.getAmis()!=null){
										cb=pdf.addLabel(cb,27,632,10,polizaVO.getAmis(),false,1);}							
									if(polizaVO.getDescVehi()!=null){
										cb=pdf.addLabel(cb,57,632,10,polizaVO.getDescVehi(),false,1);}	
									
									if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,222,612,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
									}
									if(polizaVO.getFchFin()!=null){
										cb=pdf.addLabel(cb,460,612,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

									}

//									if(polizaVO.getTipo()!=null){
//
//										if(polizaVO.getTipo().length()>18){
//											//System.out.println("TIPO: "+polizaVO.getTipo());	
//											cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo().substring(0, 19),false,1);
//										}else{
//											//System.out.println("TIPO: "+polizaVO.getTipo());	
//											cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo(),false,1); 
//										}	
//									}
								
								
								
//								if(datosCliente){									
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,72,639,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,72,624,10,calle,false,1);}
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,72,609,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,202,609,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//
//								}						

							}				
							

							
							cb=pdf.addRectAng(cb,23,590,562,408);
							cb=pdf.addRectAngColor(cb,23,590,562,13);

							cb=pdf.addLabel(cb,210,580,10,"INFORMACION IMPORTANTE",true,1);

							cb=pdf.addLabel(cb,27,550,10,"Estimado asegurado nos permitimos informarle que si durante la vigencia de su seguro las Condiciones Generales",false,1);
							cb=pdf.addLabel(cb,27,538,10,"presentaran alguna modificación estas serían publicadas en nuestra página web www.qualitas.com.mx para su consulta,",false,1);
							cb=pdf.addLabel(cb,27,526,10,"descarga o impresión. (Artículo 65 de la Ley sobre el Contrato de Seguro).",false,1);
							cb=pdf.addLabel(cb,27,514,10,"",false,1);
							cb=pdf.addLabel(cb,27,502,10,"Asimismo, con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro de",false,1);
							cb=pdf.addLabel(cb,27,490,10,"automóvil que acaba de adquirir, Quálitas Compañia de Seguros, lo invita a que lea sus Condiciones Generales ",false,1);
							cb=pdf.addLabel(cb,27,478,10,"mismas que se adjuntan a esta póliza, o bien, puede usted ingresar a nuestra página Web.",false,1);
							cb=pdf.addLabel(cb,27,466,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
							cb=pdf.addlineH(cb,27,464,343);
							cb=pdf.addLabel(cb,27,454,10,"",false,1);
							
							cb=pdf.addLabel(cb,27,442,10,"Usted puede consultar el folleto que contiene los Derechos de los Asegurados, Contratantes y Beneficiarios en nuestra",false,1);
							cb=pdf.addLabel(cb,27,430,10,"página de internet (www.qualitas.com.mx)",false,1);
							cb=pdf.addLabel(cb,27,418,10,"",false,1);
							
							cb=pdf.addLabel(cb,27,406,10,"Artículo 25 de la Ley sobre el Contrato de Seguro. Si el contenido de la póliza o sus modificaciones no concordaren",false,1);
							cb=pdf.addLabel(cb,27,394,10,"con la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan",false,1);
							cb=pdf.addLabel(cb,27,382,10,"al día en que reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de",false,1);
							cb=pdf.addLabel(cb,27,370,10,"sus modificaciones.",false,1);
							cb=pdf.addLabel(cb,27,358,10,"",false,1);
							cb=pdf.addLabel(cb,27,346,10,"Nuestra Unidad Especializada de Atención a Usuario (UNE) con siguiente domicilio en: Boulevard Adolfo López Mateos 2601",false,1);
							cb=pdf.addLabel(cb,27,334,10,"Colonia Progreso Tizapán, Delegación Alvaro Obregón, México, Distrito Federal C.P. 01080, horario de atención",false,1);
							cb=pdf.addLabel(cb,27,322,10,"de Lunes a Viernes de 9:00 a.m. a 6:00 p.m., teléfono 01 (55) 5481 8500, correo electrónico: uauf@quialitas.com.mx",false,1);
							cb=pdf.addLabel(cb,27,310,10,"",false,1);
							cb=pdf.addLabel(cb,27,298,10,"Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
							cb=pdf.addLabel(cb,27,286,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono 01 (55) 5340 0999 y ",false,1);
							cb=pdf.addLabel(cb,27,274,10,"01 (800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría.condusef.gob.mx",false,1);
							cb=pdf.addLabel(cb,27,262,10,"",false,1);
							cb=pdf.addLabel(cb,27,250,10,"Quálitas Compañía de Seguros, S.A. de C.V. (en lo sucesivo La Compañía), asegura de acuerdo a las Condiciones",false,1);
							cb=pdf.addLabel(cb,27,238,10,"Generales y Especiales de esta Póliza el vehículo contra pérdidas o daños causados por cualquiera de los",false,1);
							cb=pdf.addLabel(cb,27,226,10,"riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							
							
							cb=pdf.addRectAng(cb,23,182,562,86);//seccion 5
							cb=pdf.addRectAngColor(cb,23,182,562,12);//seccion 5
							
							cb=pdf.addLabel(cb,300,172,10,"OFICINA 1",true,1);
							cb=pdf.addLabel(cb,27,98,10,"Agente:",false,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
							cb=pdf.addLabel(cb,27,158,10,"Oficina:",false,1);
							cb=pdf.addLabel(cb,27,146,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,400,146,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,27,134,10,"Colonia:",false,1);
							cb=pdf.addLabel(cb,27,122,10,"Télefono:",false,1);
							cb=pdf.addLabel(cb,400,122,10,"Fax:",false,1);
							
							
							
							
							if(polizaVO!= null){
								//la siguiente información va del nombre del agente a telefono nacional
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								
								
								
								
								if (nombreAgente.length()>72){
								cb=pdf.addLabel(cb,130,98,10,nombreAgente.substring(0, 72),false,1);
								}else{
									cb=pdf.addLabel(cb,130,98,10,nombreAgente,false,1);
								}
								
								
								
								
								
								//cb=pdf.addLabel(cb,130,158,10,nombreAgente,false,1);
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,67,98,10,polizaVO.getClavAgente(),false,1);}
//								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//									if(polizaVO.getTelComerAgente()!=null){
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelComerAgente(),false,1);
//									}
//									else{
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelPartAgente(),false,1);
//									}
//
//								}
								cb=pdf.addlineH(cb,20,107,562);
								if(polizaVO.getDescOficina()!=null){
									cb=pdf.addLabel(cb,77,158,10,polizaVO.getDescOficina(),false,1);}
//								if(polizaVO.getPoblacionOficina()!=null){
//									cb=pdf.addLabel(cb,350,198,10,polizaVO.getPoblacionOficina(),false,2);}							
								if(polizaVO.getCalleOficina()!=null){						
									cb=pdf.addLabel(cb,77,146,10,polizaVO.getCalleOficina(),false,1);}
								if(polizaVO.getCodPostalOficina()!=null){										
									cb=pdf.addLabel(cb,440,146,10,polizaVO.getCodPostalOficina(),false,1);}
								if(polizaVO.getColoniaOficina()!=null){										
									cb=pdf.addLabel(cb,77,134,10,polizaVO.getColoniaOficina(),false,1);}
								if(polizaVO.getTelOficina()!=null){
									cb=pdf.addLabel(cb,77,122,10,polizaVO.getTelOficina(),false,1);}
								
								if(polizaVO.getFaxOficina()!=null){
									if(polizaVO.getFaxOficina().length() > 28){
										cb=pdf.addLabel(cb,440,122,10,polizaVO.getFaxOficina().substring(0, 27),false,1);
									}else{
										cb=pdf.addLabel(cb,440,122,10,polizaVO.getFaxOficina(),false,1);
									}								
								}
								
								
							}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
//							cb=pdf.addRectAng(cb,386,182,199,26);//seccion 6
//							cb=pdf.addLabel(cb,388,172,10,"CONDICIONES VIGENTES",false,1);
//							cb=pdf.addLabel(cb,388,160,10,"TARIFA APLICADA",false,1);
							
							
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,605,172,10,polizaVO.getDescConVig(),false,2);
//							}
							
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									cb=pdf.addLabel(cb,570,175,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									cb=pdf.addLabel(cb,570,175,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//							}	
							
							
							
//							if(polizaVO != null){
//								String lugar="";
//								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//								cb=pdf.addLabel(cb,490,161,10,lugar,false,0);																
//								if(polizaVO.getFchEmi()!=null){
//									cb=pdf.addLabel(cb,490,149,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							}
//
//							if(polizaVO.getDirImagen()!=null){
//								document=pdf.addImage(document,438,115,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							}
//							cb=pdf.addLabel(cb,490,100,10,"FUNCIONARIO AUTORIZADO",false,0);	
							
							
							
							
							
							cb=pdf.addRectAng(cb,23,94,562,65);//seccion 7
							cb=pdf.addLabel(cb,27,81,12,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,",false,1);
							cb=pdf.addLabel(cb,27,67,12,"la documentación contractual y la nota técnica que integran este producto de seguro, quedaron",false,1);
							cb=pdf.addLabel(cb,27,53,12,"registrados ante la Comisión Nacional de Seguros y Fianzas a partir del día ",false,1);
							cb=pdf.addLabel(cb,27,39,12,"20 de Julio de 2015 con el número CNSF-S0046-3135-1005",false,1);
							
							
							
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							}
							
							
							
							
							
							
//							document.newPage();
//
//							
//							
//
//							
//							
//							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 2/1",true,1);
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,717,562,46);//seccion 1
//							cb=pdf.addRectAng(cb,348,707,237,24);//seccion 1
//							
//							cb=pdf.addLabel(cb,60,695,10,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//							cb=pdf.addLabel(cb,355,695,10,"POLIZA",true,1);
//							cb=pdf.addLabel(cb,435,695,10,"ENDOSO",true,1);
//							cb=pdf.addLabel(cb,525,695,10,"INCISO",true,1);
//							
//							if(polizaVO != null){
//								String inciso="000";
//								String incisoAux;
//								if(polizaVO.getNumPoliza()!=null){
//									sizeNumPoliza=polizaVO.getNumPoliza().length();
//									cb=pdf.addLabel(cb,355,685,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//									cb=pdf.addLabel(cb,435,685,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//								}
//								if(polizaVO.getInciso()!=null){
//									inciso = inciso+polizaVO.getInciso();								
//									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//									cb=pdf.addLabel(cb,525,685,10,incisoAux,false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,669,562,77);//seccion 2
//							cb=pdf.addRectAng(cb,401,657,184,14);//seccion 2
//							cb=pdf.addRectAng(cb,401,618,184,26);//seccion 2
//							cb=pdf.addLabel(cb,27,658,10,"DATOS DEL CONTRATANTE:",true,1);
//							cb=pdf.addLabel(cb,250,658,10,"RFC:",true,1);
//							cb=pdf.addLabel(cb,401,658,10,"RENUEVA A:",false,1);
//							cb=pdf.addLabel(cb,435,646,10,"VIGENCIA:",true,1);
//							cb=pdf.addLabel(cb,27,630,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,401,633,10,"Desde las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,401,621,10,"Hasta las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,27,616,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,220,616,10,"Estado:",false,1);
//							cb=pdf.addLabel(cb,27,602,10,"Beneficiario Preferente:",false,1);
//							cb=pdf.addLabel(cb,410,606,10,"Fecha de vencimiento del pago",false,1);
//							if(polizaVO.getPolizaAnterior()!=null){
//								cb=pdf.addLabel(cb,472,658,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}
//							cb=pdf.addLabel(cb,472,658,10,"RENUEVAxxx",false,1);	
//							if(polizaVO != null){		
//								if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,511,633,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//								}
//								if(polizaVO.getFchFin()!=null){
//									cb=pdf.addLabel(cb,511,621,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//								}
//								if(polizaVO.getFechaLimPago()!=null){																												
//									cb=pdf.addLabel(cb,450,594,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//							}
//							
//							
//							if(polizaVO != null){
//								String nombre=""; 
//								if(polizaVO.getNombre()!=null){
//									nombre=polizaVO.getNombre()+" ";}
//								if(polizaVO.getApePate()!=null){
//									nombre=nombre+polizaVO.getApePate()+" ";}
//								if(polizaVO.getApeMate()!=null){
//									nombre=nombre+polizaVO.getApeMate()+" ";}
//
//								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//									nombre=nombre+" Y/O "+polizaVO.getConductor();
//								}	
//								cb=pdf.addLabel(cb,27,646,10,nombre,false,1);
//								if(datosCliente){										
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,75,630,10,calle,false,1);}
//
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,55,616,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,260,616,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,280,658,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getBeneficiario()!=null){
//										cb=pdf.addLabel(cb,135,602,10,polizaVO.getBeneficiario(),false,1);}
//									cb=pdf.addLabel(cb,135,602,10,"PRUEBA BENEFICIARIO PREFERENTE XXX",false,1);
//
////									for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
////										LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
////										if(campo.getLabel().length()>13)
////											cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
////										else
////											cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
////										j=j+50;
////										if(campo.getValue().length()>13)
////											cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
////										else
////											cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
////										j=j+50;
////									}
//								}						
//
//							}				
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,590,148,33);//seccion 3
//							cb=pdf.addRectAng(cb,172,590,237,33);//seccion 3
//							cb=pdf.addRectAng(cb,410,590,172,33);//seccion 3
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,556,562,62);//seccion 4
//							cb=pdf.addRectAng(cb,23,556,562,13);//seccion 4
//							cb=pdf.addLabel(cb,215,546,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,1);
//							cb=pdf.addLabel(cb,27,533,10,"Clave y Marca",false,1);
//							cb=pdf.addLabel(cb,27,522,10,"Tipo:",false,1);
//							cb=pdf.addLabel(cb,190,522,10,"Modelo:",false,1);
//							cb=pdf.addLabel(cb,320,522,10,"Color:",false,1);
//							cb=pdf.addLabel(cb,450,522,10,"Ocupantes:",false,1);
//							cb=pdf.addLabel(cb,27,510,10,"Serie:",false,1);
//							cb=pdf.addLabel(cb,190,510,10,"Motor:",false,1);
//							cb=pdf.addLabel(cb,390,510,10,"Placas:",false,1);
//
//							if(polizaVO != null){							
//								if(polizaVO.getAmis()!=null){
//									cb=pdf.addLabel(cb,97,533,10,polizaVO.getAmis(),false,1);}							
//								if(polizaVO.getDescVehi()!=null){
//									cb=pdf.addLabel(cb,127,533,10,polizaVO.getDescVehi(),false,1);}											
//								if(polizaVO.getTipo()!=null){
//									if(polizaVO.getTipo().length()>18){	
//										cb=pdf.addLabel(cb,57,522,10,polizaVO.getTipo().substring(0, 19),false,1);
//									}else{
//										cb=pdf.addLabel(cb,57,522,10,polizaVO.getTipo(),false,1); 
//									}	
//								}
//								if(polizaVO.getVehiAnio()!=null){												
//									cb=pdf.addLabel(cb,230,522,10,polizaVO.getVehiAnio(),false,1);}
//								if(polizaVO.getColor()!=null){
//
//									if(polizaVO.getColor().equals("SIN COLOR")){
//
//										cb=pdf.addLabel(cb,350,522,10,"",false,1);		
//									}else{
//										cb=pdf.addLabel(cb,350,522,10,polizaVO.getColor(),false,1);
//									}
//								}
//								//ANDRES-PASAJEROS
//								if (polizaVO.getNumPasajeros()!=null){
//									cb=pdf.addLabel(cb,507,522,10,polizaVO.getNumPasajeros(),false,1);
//								}
//								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,507,522,10,polizaVO.getNumOcupantes(),false,1);
//								}
//								if(polizaVO.getNumPlaca()!=null){												
//									cb=pdf.addLabel(cb,430,510,10,polizaVO.getNumPlaca(),false,1);}
//								if(polizaVO.getNumSerie()!=null){					
//									cb=pdf.addLabel(cb,60,510,10,polizaVO.getNumSerie(),false,1);}
//								if(polizaVO.getNumMotor()!=null){					
//									cb=pdf.addLabel(cb,224,510,10,polizaVO.getNumMotor(),false,1);}
//								
//								if (polizaVO.getCveServ().trim().equals("3")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,498,10,"Tipo de Carga",false,1);
//										cb=pdf.addLabel(cb,110,498,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//										cb=pdf.addLabel(cb,200,498,10,polizaVO.getTipoCarga()+" : ",true,2);}
//									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//										String descAux = "";
//										String valorRemolque="";
//										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//											descAux=polizaVO.getDescCarga();
//										}
//										
//										if(polizaVO.getDobleRemolque()!= null){
//											if(polizaVO.getDobleRemolque().equals("S")){
//												valorRemolque = "2° Remolque: AMPARADO";
//											}else{
//												valorRemolque = "2° Remolque: EXCLUIDO";
//											}
//										}								
//										if(descAux != "" || valorRemolque != ""){
//											cb=pdf.addLabel(cb,260,498,10,descAux+"  "+valorRemolque,false,1);
//										}
//										
//									}
//								}
//								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,498,10,"Tipo de Carga "+polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//									}
//								}
//								if(polizaVO.getNoEconomico()!=null){
//									cb=pdf.addLabel(cb,390,498,10,"No. Económico: "+polizaVO.getNoEconomico(),false,1);
//								}
//							}
//							
//						
							
					for (int l=0;l<=1;l++){								
							document.newPage();
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 3/1",true,1);		
							
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							
							
							
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
//								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
//								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
//							}
							
							
							if (polizaVO.getPlan()!=null){
								cb=pdf.addLabel(cb,390,725,10,"PLAN: "+polizaVO.getPlan(),false,1);
							}
							
							
							
							cb=pdf.addLabel(cb,100,705,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",true,1);										
							cb=pdf.addLabel(cb,400,705,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,705,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,525,705,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,400,695,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,460,695,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,695,10,incisoAux,false,1);
								}
							}


							//**********CUERPO			
							//cb=pdf.addRectAngColor(cb,23,684,562,12);
							//cb=pdf.addRectAng(cb,23,671,562,73);
							
							cb=pdf.addRectAngColor(cb,23,691,562,12);
							cb=pdf.addRectAng(cb,23,678,562,60);


							//***********asegurado					
							cb=pdf.addLabel(cb,290,681,10,"INFORMACIÓN DEL ASEGURADO",true,0);				
							cb=pdf.addLabel(cb,440,668,10,"RENUEVA A:",false,1);
							cb=pdf.addLabel(cb,40,656,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,40,644,10,"C.P.",false,1);
							cb=pdf.addLabel(cb,340,644,10,"RFC",false,1);
							//cb=pdf.addLabel(cb,40,632,10,"Beneficiario Preferente:",false,1);	
							if(polizaVO.getPolizaAnterior()!=null){
								cb=pdf.addLabel(cb,515,668,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,40,668,10,nombre,false,1);
								if(datosCliente){									
									cb=pdf.addLabel(cb,490,670,10,"  ",false,1);	

									
								if (l==0){
									if(polizaVO.getCalle()!=null){
										String calle= polizaVO.getCalle();
										if(polizaVO.getExterior()!= null){
											calle += " No. EXT. " + polizaVO.getExterior();
										}
										if(polizaVO.getInterior()!= null){
											calle += " No. INT. " + polizaVO.getInterior();
										}
										//ANDRES-prueba 
										//System.out.println("colonia:::"+polizaVO.getColonia());
											if(polizaVO.getColonia()!=null){
												calle += " COL. " + polizaVO.getColonia();
											}
											cb=pdf.addLabel(cb,93,656,10,calle,false,1);
											
											if(polizaVO.getCodPostal()!=null){
												cb=pdf.addLabel(cb,70,644,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
											if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
												cb=pdf.addLabel(cb,130,644,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
											if(polizaVO.getRfc()!=null){
												cb=pdf.addLabel(cb,390,644,10,polizaVO.getRfc(),false,1);}
											
											if(polizaVO.getBeneficiario()!=null){
												if(polizaVO.getBeneficiario().length()>1){
													cb=pdf.addLabel(cb,40,632,10,"BENEFICIARIO PREFERENTE "+polizaVO.getBeneficiario(),false,1);
												}
											}
											
										}
								}
										
										
//									for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
//										LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
//										if(campo.getLabel().length()>13)
//											cb=pdf.addLabel(cb,j,630,10,campo.getLabel().substring(0,13),false,1);
//										else
//											cb=pdf.addLabel(cb,j,630,10,campo.getLabel(),false,1);
//										j=j+50;
//										if(campo.getValue().length()>13)
//											cb=pdf.addLabel(cb,j,630,10,campo.getValue().substring(0,13),false,1);
//										else
//											cb=pdf.addLabel(cb,j,630,10,campo.getValue(),false,1);
//										j=j+50;
//									}
								}
//								if(polizaVO.getCveApoderado()!=null){
//									cb=pdf.addLabel(cb,40,630,10,"APODERADO",false,1);
//									cb=pdf.addLabel(cb,100,630,10,polizaVO.getCveApoderado(),false,1);
//								}							

							}				


							//*************Vehiculo
							cb=pdf.addRectAngColor(cb,23,616,562,12);
							cb=pdf.addRectAng(cb,23,616,562,65);

							cb=pdf.addLabel(cb,290,607,10,"DESCRIPCIÓN DEL VEHÍCULO ASEGURADO",true,0);
							cb=pdf.addLabel(cb,40,580,10,"Tipo:",false,1);
							cb=pdf.addLabel(cb,210,580,10,"Modelo:",false,1);
							cb=pdf.addLabel(cb,320,580,10,"Color:",false,1);
							cb=pdf.addLabel(cb,40,567,10,"Serie:",false,1);
							cb=pdf.addLabel(cb,210,567,10,"Motor:",false,1);
							cb=pdf.addLabel(cb,358,567,10,"REPUVE:",false,1);
							cb=pdf.addLabel(cb,490,567,10,"Placas:",false,1);
							if(polizaVO != null){
								//la información siguiente va de descripción del vehiculo a tipo de carga							
								if(polizaVO.getAmis()!=null){
									cb=pdf.addLabel(cb,40,595,10,polizaVO.getAmis(),false,1);}							
								if(polizaVO.getDescVehi()!=null){
									cb=pdf.addLabel(cb,70,595,10,polizaVO.getDescVehi(),false,1);}											

								if(polizaVO.getTipo()!=null){

									if(polizaVO.getTipo().length()>18){
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo().substring(0, 19),false,1);
									}else{
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo(),false,1); 
									}	
								}

								if(polizaVO.getVehiAnio()!=null){												
									cb=pdf.addLabel(cb,267,580,10,polizaVO.getVehiAnio(),false,1);}
								if(polizaVO.getColor()!=null){

									if(polizaVO.getColor().equals("SIN COLOR")){

										cb=pdf.addLabel(cb,358,580,10,"",false,1);		
									}else{
										cb=pdf.addLabel(cb,358,580,10,polizaVO.getColor(),false,1);
										//cb=pdf.addLabel(cb,428,580,10,polizaVO.getColor().length()>7?polizaVO.getColor().substring(0,7):polizaVO.getColor(),false,1);
										
									}
								}
								//ANDRES-PASAJEROS
								if (polizaVO.getNumPasajeros()!=null){
									cb=pdf.addLabel(cb,490,580,10,polizaVO.getNumPasajeros(),false,1);
								}
								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,490,580,10,"OCUP.",false,1);
									cb=pdf.addLabel(cb,490,580,10,"Ocupantes:",false,1);
									cb=pdf.addLabel(cb,550,580,10,polizaVO.getNumOcupantes(),false,1);
								}

								if(polizaVO.getNumPlaca()!=null){												
									cb=pdf.addLabel(cb,530,567,10,polizaVO.getNumPlaca(),false,1);}
								if(polizaVO.getNumSerie()!=null){					
									cb=pdf.addLabel(cb,73,567,10,polizaVO.getNumSerie(),false,1);}
								if(polizaVO.getNumMotor()!=null){					
									cb=pdf.addLabel(cb,250,567,10,polizaVO.getNumMotor(),false,1);}
								if(polizaVO.getRenave()!=null){					
									cb=pdf.addLabel(cb,398,567,10,polizaVO.getRenave(),false,1);}
								
								if (polizaVO.getCveServ().trim().equals("3")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,"'"+polizaVO.getClaveCarga()+"'",false,1);}
									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
										cb=pdf.addLabel(cb,200,554,10,polizaVO.getTipoCarga()+" : ",true,2);}
									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
										String descAux = "";
										String valorRemolque="";
										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
											descAux=polizaVO.getDescCarga();
										}
										
										if(polizaVO.getDobleRemolque()!= null){
											if(polizaVO.getDobleRemolque().equals("S")){
												valorRemolque = "2° Remolque: AMPARADO";
											}else{
												valorRemolque = "2° Remolque: EXCLUIDO";
											}
										}								
										if(descAux != "" || valorRemolque != ""){
											cb=pdf.addLabel(cb,210,554,10,descAux+"  "+valorRemolque,false,1);
										}
										
									}
								}
								
								
								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
									}
								}
								
								
								if(polizaVO.getNoEconomico()!=null){
									cb=pdf.addLabel(cb,40,557,10,"No.Económico: ",false,1);
									cb=pdf.addLabel(cb,115,557,10,polizaVO.getNoEconomico(),false,1);
								}
								
								
								
								
								
								
								
							}


							//**************vigencia
							//cb=pdf.addRectAng(cb,23,615,562,65);
							
							cb=pdf.addRectAng(cb,23,550,183,45);
							cb=pdf.addRectAng(cb,215,550,181,45);
							cb=pdf.addRectAng(cb,403,550,181,45);


							cb=pdf.addLabel(cb,33,538,10,"VIGENCIA",false,1);
							cb=pdf.addLabel(cb,33,526,10,"Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,33,510,10,"Hasta las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,217,538,10,"Fecha Vencimiento del pago:",false,1);
							cb=pdf.addLabel(cb,217,510,10,"Plazo de pago:",false,1);
							cb=pdf.addLabel(cb,410,538,10,"Uso:",false,1);
							cb=pdf.addLabel(cb,410,526,10,"Servicio:",false,1);
							cb=pdf.addLabel(cb,410,510,10,"Movimiento:",false,1);
							
							
							if(polizaVO != null){		
								//la información siguiente va desde la fecha de vigencia hasta servicio
								if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,147,526,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
								}
								if(polizaVO.getFchFin()!=null){
									cb=pdf.addLabel(cb,147,510,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

								}

								if(polizaVO.getFechaLimPago()!=null){																												
									cb=pdf.addLabel(cb,259,526,10,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
								if(polizaVO.getPlazoPago()!=null){
									cb=pdf.addLabel(cb,287,510,10, polizaVO.getPlazoPago()+" dias",false,1);}


								if(polizaVO.getMovimiento()!=null){
									cb=pdf.addLabel(cb,500,510,10,polizaVO.getMovimiento(),false,1);
								}

								if(polizaVO.getUso()!=null){
									ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      15 , 21*2 , 25*3
									if(uso!=null){
										if(uso.size()==1){
											cb=pdf.addLabel(cb,500,538,10,(String)uso.get(0),false,1);
										}
//										else if(uso.size()==2){
//											cb=pdf.addLabel(cb,415,515,10,(String)uso.get(0),false,0);
//											cb=pdf.addLabel(cb,415,509,10,(String)uso.get(1),false,0);
//										}	
//										else if(uso.size()==3){
//											cb=pdf.addLabel(cb,415,517,10,(String)uso.get(0),false,0);
//											cb=pdf.addLabel(cb,415,512,10,(String)uso.get(1),false,0);
//											cb=pdf.addLabel(cb,415,507,10,(String)uso.get(2),false,0);
//										}											
									}									
								}
								if(polizaVO.getServicio()!=null){
									ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      23 , 31*2 , 37*3
									if(servicio!=null){
										if(servicio.size()==1){
											cb=pdf.addLabel(cb,500,526,10,(String)servicio.get(0),false,1);
										}
//										else if(servicio.size()==2){
//											cb=pdf.addLabel(cb,515,515,10,(String)servicio.get(0),false,0);
//											cb=pdf.addLabel(cb,515,509,10,(String)servicio.get(1),false,0);
//										}
//										else if(servicio.size()==3){
//											cb=pdf.addLabel(cb,515,517,10,(String)servicio.get(0),false,0);
//											cb=pdf.addLabel(cb,515,512,10,(String)servicio.get(1),false,0);
//											cb=pdf.addLabel(cb,515,507,10,(String)servicio.get(2),false,0);
//										}
									}									
								}					
							}


							//*************Datos de Riesgos
							//cb=pdf.addRectAng(cb,23,615,562,65);
							cb=pdf.addRectAngColor(cb,23,503,562,14);	
							cb=pdf.addRectAng(cb,23,486,562,235);

							cb=pdf.addLabel(cb,33,492,10,"COBERTURAS CONTRATADAS",true,1);
							cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
							cb=pdf.addLabel(cb,430,492,10,"DEDUCIBLE",true,1);
							cb=pdf.addLabel(cb,520,492,10,"$     PRIMAS",true,1);

							//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
							//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
							//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
							//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
							double primaAux=0;
							double primaExe=0;
							double derechoAux=0;
							double recargoAux=0;
							double subtotalAux=0;
							double impuestoAux=0;
							boolean exDM=false;
							boolean exRT=false;
							boolean agenEsp1 = false;
							boolean agenEsp2 = false;
							boolean validaAltoRiesgo = false;					

							for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
								int temp = (Integer)polizaVO.getAgenteEsp().get(y);
								if(temp==1)
									agenEsp1=true;
								if(temp==AGEN_ESP_OCULTA_PRIMAS)
									agenEsp2=true;
							}
							
							boolean minimos=false;

							
							
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;

								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									if (coberturaVO.getDescrCobertura().trim().equals("CONSIDERACION5035")) {
									polizaVO.getCoberturasArr().remove(x);
									}
								}
							}
							
							
							
							
							
							
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;

								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									if(coberturaVO.getClaveCobertura().equals("12")){
										exDM=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}else if(coberturaVO.getClaveCobertura().equals("40")){
										exRT=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}	
								}
								
								String cveServ = polizaVO.getCveServ().trim();
								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{	
									boolean salto=false;
									if(x==toppage+27)
									{break;}

									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									String claveCobertura = coberturaVO.getClaveCobertura();

									int deducible = getNumber(coberturaVO.getDeducible());
									int anio = getNumber(polizaVO.getVehiAnio());
									if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
										validaAltoRiesgo = true;
									}

									//ANDRES-MINIMOS
									if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
										minimos=true;
									}


									log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
//									//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
//									//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
//									//}else{
//									if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
//										//cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
//										cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,"Gastos por Perdida de Uso en P T" ,false,1);
//									}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//										//cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
//										cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,"Exe. Ded. x PT, DM Y RT" ,false,1);
//										salto = true;
//									}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//										salto = true;
//									}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
//										//cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
//										cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,"Responsabilidad Civil Estandarizado" ,false,1);
//									}else{
//										//cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
//										cb=pdf.addLabel(cb,25, 467-(9*(x-toppage)),10,coberturaVO.getDescrCobertura().substring(1, 2)+ "|||"+coberturaVO.getDescrCobertura().substring(2).toLowerCase() ,false,1);
//									}
//									//}
									
									
									int diaEmi = 0;
									int mesEmi = 0;
									int anioEmi = 0;
									
									if (polizaVO.getFchEmi() != null && !polizaVO.getFchEmi().isEmpty()) {
										diaEmi = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
										mesEmi = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
										anioEmi = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
									}
									if ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 9) || (anioEmi >= 2015 && mesEmi >= 8 && diaEmi>=17)){//a partir del 17 agosot 2015 se quitan los numeros de cobertura
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T" ,false,1);//quitar clave cobe
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exe. Ded. x PT, DM Y RT" ,false,1);//quitar clave cobe
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estandarizado" ,false,1);//quitar clave cobe
										}else{
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												
												if (coberturaVO.getClaveCobertura().equals("1")){
														if(polizaVO.getPlan().contains("PLUS")||polizaVO.getPlan().contains("plus")){
															cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Solo Perdida Total" ,false,1);
														}else{
															cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños materiales" ,false,1);
														}													
														/*if (coberturaVO.getDescrCobertura().contains("MATERIALES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños materiales" ,false,1);}
														if (coberturaVO.getDescrCobertura().contains("TOTAL")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Solo Perdida Total" ,false,1);}*/
													}
												else if (coberturaVO.getClaveCobertura().equals("10")){
													if (coberturaVO.getDescrCobertura().contains("OCUPANTES")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Daños a Ocupantes" ,false,1);														
														}
													if (coberturaVO.getDescrCobertura().contains("EXT")){
														if (coberturaVO.getDescrCobertura().contains("OCUPANTES")){
															x++;
														}
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Extensión de coberturas" ,false,1);
														
														}
													}
//												else if (coberturaVO.getClaveCobertura().equals("11")){												
//													if (coberturaVO.getDescrCobertura().contains("DM")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible X PT DM" ,false,1);}
//													if (coberturaVO.getDescrCobertura().contains("RT")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT" ,false,1);}
//													}												
												else if (coberturaVO.getClaveCobertura().equals("11")){	
													if (coberturaVO.getDescrCobertura().contains("DM")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible X PT DM" ,false,1);
													}
													if (coberturaVO.getDescrCobertura().contains("RT")){
														//Si se pinta la cobertura en el PDF, se hace un salto de renglón para la siguiente cobertura
														if (coberturaVO.getDescrCobertura().contains("DM")){
															x++;
														}
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT" ,false,1);
													}
												}
												else if (coberturaVO.getClaveCobertura().equals("12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Pasajero" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo Parcial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Ajuste Automático" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Vial",false,1);
													cb=pdf.addLabel(cb,23,294 ,9,"Servicios de Asistencia Vial: D.F. y Area Metropolitana: 3300 4534 ; Interior de la República : 01 800 253 0553",false,1);
													}
												else if (coberturaVO.getClaveCobertura().equals("16.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA DM" ,false,1);}			
												else if (coberturaVO.getClaveCobertura().equals("16.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA RT" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Satelital" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("18")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Can. Ded por Colisión o vuelco" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("19")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AVC" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo total" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("20")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG EG" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("21")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG SM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("22")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Muerte del Conductor por Accidente Automovilístico" ,false,1);}
												
												
												else if (coberturaVO.getClaveCobertura().equals("24")){
													if (coberturaVO.getDescrCobertura().contains("RINES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Rines" ,false,1);}
													else {
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Neumáticos" ,false,1);
													}
													}
												else if (coberturaVO.getClaveCobertura().equals("3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.11")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Daños a Terceros EUA" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estand" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Cruzada" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.16")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Maniobras de carga y descarga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Arrastre de remolque" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Bienes" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.4")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños por carga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.5")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Ecologica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("4")){
													if(polizaVO.getCveServ().trim().equals("2")){
													//if(polizaVO.getPlan().contains("PUBLICO")||polizaVO.getPlan().contains("publico")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Médicos Conductor y Familiares" ,false,1);
													}else{
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Médicos Ocupantes" ,false,1);
													}													
												}
												else if (coberturaVO.getClaveCobertura().equals("40")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("41")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Avería Mecánica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("43")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Llantas",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("44")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Rines" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("51")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("52")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P P",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos de Transporte" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"GTP" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("7")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Legales" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("8")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Equipo Especial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Serv.Asist.AccidentePers.enViaje",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Adapt y/o Conversiones DM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AdapT y/o Conversiones RT" ,false,1);}
												
												
												if (polizaVO.getLeyendaRCEUA()!=null){
													cb=pdf.addLabel(cb,23,288 ,9,polizaVO.getLeyendaRCEUA(),false,1);
												}
												
												if (polizaVO.getLeyendaUMA()!=null){
													cb=pdf.addLabel(cb,23,280 ,9,polizaVO.getLeyendaUMA(),false,1);
												}
												


												
												
												
												
												
												
												
												
												
												//cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
																								
//												if ( ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 10) || (anioEmi >= 2015 && mesEmi >= 9 && diaEmi>=21)) && (polizaVO.getCvePlan().equals("34"))){
//													//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
	//
//												}else{
//													cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
//												}

																					
										}
									}
									else{//se muestra la numeracion de coberturas
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
											
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
											
										}else{
											//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
									
//												if ( polizaVO.getCvePlan().equals("34")){
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la pagina www.qualitas.com.mx para imprimir certificado", false, 1);
//												}
										}
									}
									
									
									
									
									
									
									
									

									if(!salto){
										//ANDRES-SUMASEG
										//suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}				
												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
											}else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
											} 

										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,coberturaVO.getPrima(),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
										//ANDRES-SUMASEG
										//}
										//										suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if(coberturaVO.isFlagSumaAsegurada()){	
												if (coberturaVO.getClaveCobertura().contains("6.2")){
													double dias=0;
													try {
														if (coberturaVO.getSumaAsegurada().contains(",")){
															int indice=0;
															indice=coberturaVO.getSumaAsegurada().indexOf(",");
															String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
															dias = Double.parseDouble(sumAseg)/500 ;
														}
														else{
															dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
														}
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
													} catch (Exception e) {
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
													}				
													//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
													//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
												}
												else if(coberturaVO.getClaveCobertura().equals("12")) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
													
												}
												else{
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}
										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if ("45".equals(coberturaVO.getClaveCobertura())){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"U$S 200",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,FormatDecimal.numDecimal(primaExe),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}		
								}
							}

							boolean altoRiesgo = false;
							if (validaAltoRiesgo) {
								log.debug("Se validara el alto riesgo");
								
								Integer tarifa = getNumber(polizaVO.getTarifa());
								
								// Tarifas enero 1990 - diciembre 2029
								boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
								
								if (tarifa < 1309 && formatoTarifaNormal) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								} else {
									List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
									if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
										Integer amis = getNumber(polizaVO.getAmis());
										int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
										altoRiesgo = claveAmisAltoRiesgo == 9999;
									}
								}
							}
							if (altoRiesgo) {
								cb = pdf.addLabel(cb, 23, 290, 10, "IMPORTANTE", false, 1);
								cb = pdf.addLabel(cb, 23, 280, 10, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", false, 1);
								cb = pdf.addLabel(cb, 23, 270, 10, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", false, 1);
							}
							
							int dif = 0;
							if (minimos){
								dif = 20; // Para que no choquen leyendas
								cb=pdf.addLabel(cb,23,286,10,DEDUCIBLE_MINIMOP1,false,1);
								cb=pdf.addLabel(cb,23,278,10,DEDUCIBLE_MINIMOP2,false,1);
							}
							
							String servicio = polizaVO.getServicio().trim();
							String tarifa = polizaVO.getTarifa().trim();
							log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
							if (servicio.equals("PUBLICO")) {
								//cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPub1,true,1);
								//cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPub2,true,1);
								cb=pdf.addLabel(cb,23,270+dif,10,SEG_OBL_PUB_1,false,1);
								cb=pdf.addLabel(cb,23,262+dif,10,SEG_OBL_PUB_2,false,1);
								cb=pdf.addLabel(cb,23,254+dif,10,SEG_OBL_PUB_3,false,1);
							} else if (servicio.equals("PARTICULAR")
									&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
//								cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPart1,true,1);
//								cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPart2,true,1);
								
								cb=pdf.addLabel(cb,23,270+dif,10,SEG_OBL_PART_1,false,1);
								cb=pdf.addLabel(cb,23,263+dif,10,SEG_OBL_PART_2,false,1);
								cb=pdf.addLabel(cb,23,256+dif,10,SEG_OBL_PART_3,false,1);
							}
							
							


							//************Agente
							//cb=pdf.addRectAngColor(cb,35,242,335,12);
							cb=pdf.addRectAng(cb,23,248,355,50);
							cb=pdf.addLabel(cb,40,238,10,"Textos:",false,1);
							cb=pdf.addRectAng(cb,23,196,355,30);
							//************forma de pago
							cb=pdf.addLabel(cb,40,183,10,"Forma de:",false,1);
							cb=pdf.addLabel(cb,40,173,10,"Pago:",false,1);
							if(polizaVO != null){
								if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
								}
								else {
										//cb=pdf.addLabel(cb,100,208,10,polizaVO.getClavAgente(),false,1);
									
									if(polizaVO.getDescrFormPago()!=null){
											cb=pdf.addLabel(cb,88,177,10,polizaVO.getDescrFormPago(),false,1);
									}
		
									//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
									cb=pdf.addLabel(cb,315,183,10,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
									if(polizaVO.getNumRecibos() != null && polizaVO.getNumRecibos() > 1){
										cb=pdf.addLabel(cb,185,183,10,"Primer pago",false,1);
										cb=pdf.addLabel(cb,185,173,10,"Pago(s) Subsecuente(s)",false,1);																						
										cb=pdf.addLabel(cb,315,173,10,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
									}else{
										cb=pdf.addLabel(cb,190,183,10,"Pago Unico",false,1);
									}
								}			
							}
							
							
							

//							cb=pdf.addLabel(cb,190,232,10,"OFICINA DE SERVICIO",true,0);
//							cb=pdf.addLabel(cb,40,218,10,"AGENTE",false,1);
//							cb=pdf.addLabel(cb,40,208,10,"NUMERO",false,1);
//							cb=pdf.addLabel(cb,190,208,10,"TELEFONO",false,1);
//							cb=pdf.addLabel(cb,40,198,10,"OFICINA",false,1);
//							cb=pdf.addLabel(cb,40,188,10,"DOMICILIO",false,1);
//							cb=pdf.addLabel(cb,310,188,10,"C.P.",false,1);
//							cb=pdf.addLabel(cb,40,178,10,"COL.",false,1);
//							cb=pdf.addLabel(cb,188,178,10,"TEL.",false,1);
//							cb=pdf.addLabel(cb,274,178,10,"FAX",false,1);
							//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
							//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
							//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
							//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
//							if(polizaVO!= null){
//								//la siguiente información va del nombre del agente a telefono nacional
//								String nombreAgente="";
//								if(polizaVO.getNombreAgente()!=null){
//									nombreAgente=polizaVO.getNombreAgente()+" ";}
//								if(polizaVO.getPateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
//								if(polizaVO.getMateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
//								if (polizaVO.getClavAgente().equals("52017")) {
//									nombreAgente = "";
//								}
//
//								cb=pdf.addLabel(cb,100,218,10,nombreAgente,false,1);
//
//								if(polizaVO.getClavAgente()!=null){
//									cb=pdf.addLabel(cb,100,208,10,polizaVO.getClavAgente(),false,1);}
//
//								//ANDRES-TELEFONO AGENTE
//								//System.out.println("telParti"+polizaVO.getTelPartAgente());
//								//System.out.println("telcomer"+polizaVO.getTelComerAgente());
//								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//									if(polizaVO.getTelComerAgente()!=null){
//										cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelComerAgente(),false,1);
//									}
//									else{
//										cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelPartAgente(),false,1);
//									}
//
//								}
//
//
//
//
//
//
//
//								cb=pdf.addlineH(cb,31,206,343);
//								if(polizaVO.getDescOficina()!=null){
//									cb=pdf.addLabel(cb,100,198,10,polizaVO.getDescOficina(),false,1);}
//								if(polizaVO.getPoblacionOficina()!=null){
//									cb=pdf.addLabel(cb,350,198,10,polizaVO.getPoblacionOficina(),false,2);}							
//								if(polizaVO.getCalleOficina()!=null){						
//									cb=pdf.addLabel(cb,100,188,10,polizaVO.getCalleOficina(),false,1);}
//								if(polizaVO.getCodPostalOficina()!=null){										
//									cb=pdf.addLabel(cb,330,188,10,polizaVO.getCodPostalOficina(),false,1);}
//								if(polizaVO.getColoniaOficina()!=null){										
//									//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
//									cb=pdf.addLabel(cb,60,178,10,polizaVO.getColoniaOficina(),false,1);}
//
//								//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
//								//cb=pdf.addlineH(cb,240,177,125);
//
//								if(polizaVO.getTelOficina()!=null){
//									//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
//									cb=pdf.addLabel(cb,208,178,10,polizaVO.getTelOficina(),false,1);}
//
//								//if(polizaVO.getTelLocal()!=null){
//								//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
//								//}
//
//								if(polizaVO.getFaxOficina()!=null){					
//									cb=pdf.addLabel(cb,293,178,10,polizaVO.getFaxOficina(),false,1);}
//
//								//if(polizaVO.getTelNacional()!=null){
//								//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
//								//}
//							}				

							cb=pdf.addRectAng(cb,23,164,355,47);
							cb=pdf.addLabel(cb,40,154,10,"Exclusivo para reporte de",true,1);
							cb=pdf.addLabel(cb,40,144,10,"Siniestros",true,1);
							cb=pdf.addLabel(cb,180,154,10,"01-800-288-6700",true,1);
							cb=pdf.addLabel(cb,180,144,10,"01-800-800-2880",true,1);
							cb=pdf.addlineH(cb, 23, 139, 355);
							//la siguiente información va del nombre del agente a telefono nacional
							cb=pdf.addLabel(cb,40,128,10,"Agente:",true,1);
							cb=pdf.addLabel(cb,40,118,10,"Clave:",true,1);
							cb=pdf.addLabel(cb,168,118,10,"Teléfono:",true,1);
							
							if (polizaVO!=null){
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
	
								if (nombreAgente.length()>42){
								cb=pdf.addLabel(cb,88,128,10,nombreAgente.substring(0, 42),true,1);
								}else{
									cb=pdf.addLabel(cb,88,128,10,nombreAgente,true,1);
								}
								
	
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,88,118,10,polizaVO.getClavAgente(),false,1);}
	
								//ANDRES-TELEFONO AGENTE
								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
									if(polizaVO.getTelComerAgente()!=null){
										//cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelComerAgente(),false,1);
										cb=pdf.addLabel(cb,218,118,10,polizaVO.getTelComerAgente(),true,1);
									}
									else{
										//cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelPartAgente(),false,1);
										cb=pdf.addLabel(cb,218,118,10,polizaVO.getTelPartAgente(),true,1);
									}
	
								}
						}
							
							
							
//							cb=pdf.addLabel(cb,40,153,10,"Servicio de Asistencia Vial         3300 4534",true,1);
//							cb=pdf.addLabel(cb,40,153,10,"Quálitas                            01-800-253-0553",true,1);
							
							
//							if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
//								cb=pdf.addLabel(cb,35,140,10,polizaVO.getAsistencia(),true,1);
//							} else {
//								cb=pdf.addLabel(cb,35,140,10,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
//								
//								if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//								}
//								else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//								}
//								else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
//								}
//								else{
//									cb=pdf.addLabel(cb,290,140,10,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
//								}
//							}
							
							



							cb=pdf.addRectAng(cb,23,115,355,60);
//							cb=pdf.addLabel(cb,43,117,10,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
//							cb=pdf.addLabel(cb,43,110,10,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
//							cb=pdf.addLabel(cb,43,103,10,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
//							cb=pdf.addLabel(cb,43,96,10,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);
//
////							cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
////							cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
////							cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
////							cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
//							
//							cb=pdf.addLabel(cb,43,81,10,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
//							cb=pdf.addLabel(cb,43,74,10,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
//							cb=pdf.addLabel(cb,43,67,10,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
//							cb=pdf.addLabel(cb,43,60,10,"del día",false,1);

							
//							//CHAVA-LEYENDA ARTICULO 25
//							cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
//							cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);



							//************importe
							cb=pdf.addRectAngColor(cb,390,247,195,13);
							cb=pdf.addLabel(cb,395,237,10,"MONEDA",true,1);
							if(polizaVO.getDescMoneda()!=null){
								cb=pdf.addLabel(cb,560,237,10,polizaVO.getDescMoneda(),true,2);}
							cb=pdf.addRectAng(cb,390,232,195,20);
																
							cb=pdf.addRectAng(cb,390,210,195,83);

//							cb=pdf.addLabel(cb,395,200,10,"PRIMA NETA",false,1);
//							cb=pdf.addLabel(cb,395,190,10,"TASA FINANCIAMIENTO POR PAGO",false,1);
//							cb=pdf.addLabel(cb,395,180,10,"FRACCIONADO",false,1);
//							cb=pdf.addLabel(cb,395,167,10,"GTOS. EXPEDICION POL.",false,1);
//							cb=pdf.addLabel(cb,395,147,10,"SUBTOTAL",false,1);
//							cb=pdf.addLabel(cb,395,135,10,"I.V.A.",false,1);
//							cb=pdf.addLabel(cb,395,116,10,"IMPORTE TOTAL",true,1);
//							cb=pdf.addLabel(cb,395,100,10,"CONDICIONES VIGENTES:",false,1);
//							cb=pdf.addLabel(cb,395,88,10,"TARIFA APLICADA:",false,1);
							
							
							cb=pdf.addLabel(cb,395,200,10,"Prima Neta",false,1);
							cb=pdf.addLabel(cb,395,190,10,"Tasa Financiamiento",false,1);
							//cb=pdf.addLabel(cb,395,180,10,"FRACCIONADO",false,1);
							//cb=pdf.addLabel(cb,395,167,10,"GTOS. EXPEDICION POL.",false,1);
							cb=pdf.addLabel(cb,395,180,10,"Gastos por Expedición.",false,1);
							cb=pdf.addLabel(cb,395,147,10,"Subtotal",false,1);
							cb=pdf.addLabel(cb,395,135,10,"I.V.A.   16%",false,1);
							cb=pdf.addLabel(cb,395,116,10,"IMPORTE TOTAL",true,1);
//							cb=pdf.addLabel(cb,395,100,10,"CONDICIONES VIGENTES:",false,1);
							cb=pdf.addLabel(cb,30,103,10,"Tarifa Aplicada:",false,1);
							

							if(polizaVO != null){
								//la información siguiente va de prima neta a tarifa aplicada
								if(polizaVO.getPrimaNeta()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(primaAux),false,2);
									}
									else
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
								}
								/*	if(polizaVO.getRecargo()!=null){ 
										    if(Double.parseDouble(polizaVO.getRecargo())>0){
										    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
										}*/
								if(polizaVO.getRecargo()!=null){
									if(Double.parseDouble(polizaVO.getRecargo())>0){
										if(Integer.parseInt(polizaVO.getNumIncisos())>1){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}
										else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}else{
										if(Integer.parseInt(polizaVO.getNumIncisos())<0){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}
								}
								if(polizaVO.getDerecho()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(derechoAux),false,2);

									}
									else{
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
								}
								
								
								if(polizaVO.getCesionComision()!=null){
									    cb=pdf.addLabel(cb,395,157,10,"DESCUENTOS",false,1);
										cb=pdf.addLabel(cb,560,157,10,polizaVO.getCesionComision(),false,2);
								}
								
								
								if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										subtotalAux = primaAux+derechoAux;				
									}
									else{
										subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

									cb=pdf.addLabel(cb,560,147,10,FormatDecimal.numDecimal(subtotalAux),false,2);
								}

								cb=pdf.addlineH(cb,460,143,115);
								if(polizaVO.getImpuesto()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
										if(polizaVO.getIva()!=null){
											impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
											cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(impuestoAux),false,2);
										}
									}
									else{
										cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
								}

								cb=pdf.addRectAng(cb,390,125,195,13);
								if(polizaVO.getPrimaTotal()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
									}
									else{
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
								}

								//***************************

//								if(polizaVO.getDescConVig()!=null){									
//									cb=pdf.addLabel(cb,555,100,10,polizaVO.getDescConVig(),false,2);
//								}

								//cb=pdf.addRectAng(cb,390,110,180,25);
								if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
									//String concatZonaId= "0000"+polizaVO.getClaveOfic();

									if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){

										String concatZonaId= "0000"+polizaVO.getTarifApDesc();
										//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}
									else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){

										String concatZonaId= "0000"+polizaVO.getTarifApCve();
										//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}

									//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}								

							}

							//****firma
							if(polizaVO != null){
								String lugar="";
								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
								cb=pdf.addLabel(cb,490,100,10,lugar,false,0);
								//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
								if(polizaVO.getFchEmi()!=null){
									cb=pdf.addLabel(cb,490,90,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
								/*if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
							}


							if(polizaVO.getDirImagen()!=null){
								document=pdf.addImage(document,450,50,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
								//cb=pdf.addLabel(cb,490,35,10,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
							}
//							cb=pdf.addLabel(cb,490,28,10,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
							cb=pdf.addLabel(cb,490,40,10,"Funcionario Autorizado",false,0);	


							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables "+polizaVO.getDescConVig()+ "mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							else{
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables                              mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}


//							if (polizaVO.getcNSF()!=null){
//								cb=pdf.addLabel(cb,43,53,10,polizaVO.getcNSF(),false,1);
//							}
							
							//ANDRES-MEM
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							}




							
							
						}//fin for para las dos hojas de la poliza
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
							
							

//							document.newPage();
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						}
						
						
						
						
						
						

//						cb=pdf.addLabel(cb,100,700,12,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//						cb=pdf.addLabel(cb,400,706,10,"POLIZA",false,1);
//						cb=pdf.addLabel(cb,460,706,10,"ENDOSO",false,1);
//						cb=pdf.addLabel(cb,525,706,10,"INCISO",false,1);
//						if(polizaVO != null){
//							//el orden de los datos siguientes va de poliza a inciso
//							String inciso="000";
//							String incisoAux;
//
//							if(polizaVO.getNumPoliza()!=null){
//								sizeNumPoliza=polizaVO.getNumPoliza().length();
//								cb=pdf.addLabel(cb,400,694,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//
//								cb=pdf.addLabel(cb,460,694,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//							}
//
//							if(polizaVO.getInciso()!=null){
//								inciso = inciso+polizaVO.getInciso();								
//								incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//								cb=pdf.addLabel(cb,525,694,9,incisoAux,false,1);
//							}
//						}
//
//
//						//**********CUERPO										
//						cb=pdf.addRectAngColor(cb,35,661,535,12);
//						cb=pdf.addRectAng(cb,35,648,535,43);


//						//***********asegurado					
//						cb=pdf.addLabel(cb,290,651,10,"INFORMACION DEL ASEGURADO",true,0);				
//						cb=pdf.addLabel(cb,440,640,8,"RENUEVA A:",false,1);
//						cb=pdf.addLabel(cb,40,630,8,"DOMICILIO",false,1);
//						cb=pdf.addLabel(cb,40,620,8,"C.P.",false,1);
//						cb=pdf.addLabel(cb,340,620,8,"RFC",false,1);
//						//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
//						if(polizaVO.getPolizaAnterior()!=null){
//							cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
//						if(polizaVO != null){
//							//el orden de los datos siguientes va del nombre del asegurado a beneficiario
//							String nombre=""; 
//							if(polizaVO.getNombre()!=null){
//								nombre=polizaVO.getNombre()+" ";}
//							if(polizaVO.getApePate()!=null){
//								nombre=nombre+polizaVO.getApePate()+" ";}
//							if(polizaVO.getApeMate()!=null){
//								nombre=nombre+polizaVO.getApeMate()+" ";}
//
//							if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//								nombre=nombre+" Y/O "+polizaVO.getConductor();
//							}	
//
//							cb=pdf.addLabel(cb,40,640,8,nombre,false,1);
//							if(datosCliente){									
//								cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	
//
//								if(polizaVO.getCalle()!=null){
//									String calle= polizaVO.getCalle();
//									if(polizaVO.getExterior()!= null){
//										calle += " No. EXT. " + polizaVO.getExterior();
//									}
//									if(polizaVO.getInterior()!= null){
//										calle += " No. INT. " + polizaVO.getInterior();
//									}
//									//ANDRES-prueba 
//									//System.out.println("colonia:::"+polizaVO.getColonia());
//									if(polizaVO.getColonia()!=null){
//										calle += " COL. " + polizaVO.getColonia();
//									}
//									cb=pdf.addLabel(cb,90,630,8,calle,false,1);}
//
//								if(polizaVO.getCodPostal()!=null){
//									cb=pdf.addLabel(cb,70,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//								if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//									cb=pdf.addLabel(cb,130,620,8,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//								if(polizaVO.getRfc()!=null){
//									cb=pdf.addLabel(cb,390,620,8,polizaVO.getRfc(),false,1);}
//								if(polizaVO.getBeneficiario()!=null){
//									cb=pdf.addLabel(cb,40,610,8,polizaVO.getBeneficiario(),false,1);}
//
//								for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
//									LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
//									if(campo.getLabel().length()>13)
//										cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
//									else
//										cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
//									j=j+50;
//									if(campo.getValue().length()>13)
//										cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
//									else
//										cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
//									j=j+50;
//								}
//							}
//							if(polizaVO.getCveApoderado()!=null){
//								cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);
//								cb=pdf.addLabel(cb,100,610,8,polizaVO.getCveApoderado(),false,1);
//							}							
//
//						}				


						//*************Vehiculo
//						cb=pdf.addRectAngColor(cb,35,603,535,12);
//						cb=pdf.addRectAng(cb,35,589,535,55);
//
//						cb=pdf.addLabel(cb,290,592,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,0);
//						cb=pdf.addLabel(cb,40,565,8,"TIPO",true,1);
//						cb=pdf.addLabel(cb,170,565,8,"MODELO",true,1);
//						cb=pdf.addLabel(cb,260,565,8,"COLOR",true,1);
//						cb=pdf.addLabel(cb,490,565,8,"PLACAS",true,1);
//						cb=pdf.addLabel(cb,40,552,8,"SERIE",true,1);
//						cb=pdf.addLabel(cb,259,552,8,"MOTOR",true,1);
//						cb=pdf.addLabel(cb,390,552,8,"REPUVE",true,1);
//						if(polizaVO != null){
//							//la información siguiente va de descripción del vehiculo a tipo de carga							
//							if(polizaVO.getAmis()!=null){
//								cb=pdf.addLabel(cb,40,580,8,polizaVO.getAmis(),true,1);}							
//							if(polizaVO.getDescVehi()!=null){
//								cb=pdf.addLabel(cb,70,580,8,polizaVO.getDescVehi(),false,1);}											
//
//							if(polizaVO.getTipo()!=null){
//
//								if(polizaVO.getTipo().length()>18){
//									//System.out.println("TIPO: "+polizaVO.getTipo());	
//									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo().substring(0, 19),false,1);
//								}else{
//									//System.out.println("TIPO: "+polizaVO.getTipo());	
//									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo(),false,1); 
//								}	
//							}
//
//							if(polizaVO.getVehiAnio()!=null){												
//								cb=pdf.addLabel(cb,210,565,8,polizaVO.getVehiAnio(),false,1);}
//							if(polizaVO.getColor()!=null){
//
//								if(polizaVO.getColor().equals("SIN COLOR")){
//
//									cb=pdf.addLabel(cb,300,565,8,"",false,1);		
//								}else{
//									cb=pdf.addLabel(cb,300,565,8,polizaVO.getColor(),false,1);
//								}
//							}
//							//ANDRES-PASAJEROS
//							if (polizaVO.getNumPasajeros()!=null){
//								cb=pdf.addLabel(cb,40,542,8,polizaVO.getNumPasajeros(),false,1);
//							}
//							else if(polizaVO.getNumOcupantes()!=null){
//								cb=pdf.addLabel(cb,390,565,8,"OCUP.",true,1);
//								cb=pdf.addLabel(cb,420,565,8,polizaVO.getNumOcupantes(),false,1);
//							}
//
//							if(polizaVO.getNumPlaca()!=null){												
//								cb=pdf.addLabel(cb,530,565,8,polizaVO.getNumPlaca(),false,1);}
//							if(polizaVO.getNumSerie()!=null){					
//								cb=pdf.addLabel(cb,70,552,8,polizaVO.getNumSerie(),false,1);}
//							if(polizaVO.getNumMotor()!=null){					
//								cb=pdf.addLabel(cb,300,552,8,polizaVO.getNumMotor(),false,1);}
//							if(polizaVO.getRenave()!=null){					
//								cb=pdf.addLabel(cb,440,552,8,polizaVO.getRenave(),false,1);}
//							
//							if (polizaVO.getCveServ().trim().equals("3")) {
//								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
//									cb=pdf.addLabel(cb,110,539,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//								if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//									cb=pdf.addLabel(cb,200,539,8,polizaVO.getTipoCarga()+" : ",true,2);}
//								//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//								if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//									String descAux = "";
//									String valorRemolque="";
//									if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//										descAux=polizaVO.getDescCarga();
//									}
//									
//									if(polizaVO.getDobleRemolque()!= null){
//										if(polizaVO.getDobleRemolque().equals("S")){
//											valorRemolque = "2° Remolque: AMPARADO";
//										}else{
//											valorRemolque = "2° Remolque: EXCLUIDO";
//										}
//									}								
//									if(descAux != "" || valorRemolque != ""){
//										cb=pdf.addLabel(cb,210,539,8,descAux+"  "+valorRemolque,false,1);
//									}
//									
//								}
//							}
//							
//							
//							if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
//									cb=pdf.addLabel(cb,110,539,8,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//								}
//							}
//							
//							
//							if(polizaVO.getNoEconomico()!=null){
//								cb=pdf.addLabel(cb,480,552,8,"NO.ECO.",true,1);
//								cb=pdf.addLabel(cb,515,552,8,polizaVO.getNoEconomico(),false,1);
//							}
//							
//							
//							
//							
//							
//							
//							
//						}
//
//
//						//**************vigencia
//						cb=pdf.addRectAng(cb,35,531,150,25);
//						cb=pdf.addRectAng(cb,193,531,50,25);
//						cb=pdf.addRectAng(cb,251,531,60,25);
//						cb=pdf.addRectAng(cb,319,531,50,25);						
//						cb=pdf.addRectAng(cb,377,531,192,25);
//
//						cb=pdf.addLabel(cb,33,526,5,"VIGENCIA:",false,1);
//						cb=pdf.addLabel(cb,33,518,7,"DESDE LAS 12 HORAS P.M. DEL  ",false,1);
//						cb=pdf.addLabel(cb,33,510,7,"HASTA LAS 12 HORAS P.M. DEL  ",false,1);
//						cb=pdf.addLabel(cb,218,522,8,"PLAZO PAGO",false,0);
//						cb=pdf.addLabel(cb,251,522,8,"F. LIMITE PAGO",false,1);
//						cb=pdf.addLabel(cb,344,522,8,"MOVIMIENTO",false,0);
//						cb=pdf.addLabel(cb,415,522,8,"USO",false,0);
//						cb=pdf.addLabel(cb,515,522,8,"SERVICIO",false,0);
//						if(polizaVO != null){		
//							//la información siguiente va desde la fecha de vigencia hasta servicio
//							if(polizaVO.getFchIni()!=null){
//								cb=pdf.addLabel(cb,143,518,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//							}
//							if(polizaVO.getFchFin()!=null){
//								cb=pdf.addLabel(cb,143,510,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//
//							}
//
//							if(polizaVO.getPlazoPago()!=null){
//								cb=pdf.addLabel(cb,218,512,8, polizaVO.getPlazoPago()+" dias",false,0);}
//
//							if(polizaVO.getFechaLimPago()!=null){																												
//								cb=pdf.addLabel(cb,259,512,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//
//
//							if(polizaVO.getMovimiento()!=null){
//								cb=pdf.addLabel(cb,344,512,8,polizaVO.getMovimiento(),false,0);
//							}
//
//							if(polizaVO.getUso()!=null){
//								ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
//								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
//								//caracteres      15 , 21*2 , 25*3
//								if(uso!=null){
//									if(uso.size()==1){
//										cb=pdf.addLabel(cb,415,512,8,(String)uso.get(0),false,0);
//									}
//									else if(uso.size()==2){
//										cb=pdf.addLabel(cb,415,515,6,(String)uso.get(0),false,0);
//										cb=pdf.addLabel(cb,415,509,6,(String)uso.get(1),false,0);
//									}	
//									else if(uso.size()==3){
//										cb=pdf.addLabel(cb,415,517,5,(String)uso.get(0),false,0);
//										cb=pdf.addLabel(cb,415,512,5,(String)uso.get(1),false,0);
//										cb=pdf.addLabel(cb,415,507,5,(String)uso.get(2),false,0);
//									}											
//								}									
//							}
//							if(polizaVO.getServicio()!=null){
//								ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
//								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
//								//caracteres      23 , 31*2 , 37*3
//								if(servicio!=null){
//									if(servicio.size()==1){
//										cb=pdf.addLabel(cb,515,512,8,(String)servicio.get(0),false,0);
//									}
//									else if(servicio.size()==2){
//										cb=pdf.addLabel(cb,515,515,6,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,509,6,(String)servicio.get(1),false,0);
//									}
//									else if(servicio.size()==3){
//										cb=pdf.addLabel(cb,515,517,5,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,512,5,(String)servicio.get(1),false,0);
//										cb=pdf.addLabel(cb,515,507,5,(String)servicio.get(2),false,0);
//									}
//								}									
//							}					
//						}
//
//
//						//*************Datos de Riesgos
//						cb=pdf.addRectAngColor(cb,35,502,535,12);	
//						cb=pdf.addRectAng(cb,35,482,535,235);
//
//						cb=pdf.addLabel(cb,60,492,10,"COBERTURAS CONTRATADAS",true,1);
//						cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
//						cb=pdf.addLabel(cb,420,492,10,"DEDUCIBLE",true,1);
//						cb=pdf.addLabel(cb,510,492,10,"$     PRIMAS",true,1);
//
//						//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
//						//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
//						//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
//						//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
//						double primaAux=0;
//						double primaExe=0;
//						double derechoAux=0;
//						double recargoAux=0;
//						double subtotalAux=0;
//						double impuestoAux=0;
//						boolean exDM=false;
//						boolean exRT=false;
//						boolean agenEsp1 = false;
//						boolean agenEsp2 = false;
//						boolean validaAltoRiesgo = false;					
//
//						for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
//							int temp = (Integer)polizaVO.getAgenteEsp().get(y);
//							if(temp==1)
//								agenEsp1=true;
//							if(temp==AGEN_ESP_OCULTA_PRIMAS)
//								agenEsp2=true;
//						}
//						
//						boolean minimos=false;
//
//						if(polizaVO.getCoberturasArr()!=null){
//							CoberturasPdfBean coberturaVO;
//
//							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
//							{
//								coberturaVO= new CoberturasPdfBean();
//								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
//								if(coberturaVO.getClaveCobertura().equals("12")){
//									exDM=true;
//									if(coberturaVO.isFlagPrima()){
//										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									}
//								}else if(coberturaVO.getClaveCobertura().equals("40")){
//									exRT=true;
//									if(coberturaVO.isFlagPrima()){
//										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									}
//								}	
//							}
//							
//							String cveServ = polizaVO.getCveServ().trim();
//							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
//							{	
//								boolean salto=false;
//								if(x==toppage+27)
//								{break;}
//
//								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
//								String claveCobertura = coberturaVO.getClaveCobertura();
//
//								int deducible = getNumber(coberturaVO.getDeducible());
//								int anio = getNumber(polizaVO.getVehiAnio());
//								if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
//									validaAltoRiesgo = true;
//								}
//
//								//ANDRES-MINIMOS
//								if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
//									minimos=true;
//								}
//
//
//								log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
//								//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
//								//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
//								//}else{
//								if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
//								}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
//									salto = true;
//								}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									salto = true;
//								}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
//									cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
//								}else{
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
//								}
//								//}
//
//								if(!salto){
//									//ANDRES-SUMASEG
//									//suma asegurada														
//									if(coberturaVO.isFlagSumaAsegurada()){	
//										if (coberturaVO.getClaveCobertura().contains("6.2")){
//											double dias=0;
//											try {
//												if (coberturaVO.getSumaAsegurada().contains(",")){
//													int indice=0;
//													indice=coberturaVO.getSumaAsegurada().indexOf(",");
//													String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
//													dias = Double.parseDouble(sumAseg)/500 ;
//												}
//												else{
//													dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
//												}
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
//											} catch (Exception e) {
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//											}				
//											//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
//											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
//										}
//										else{
//											cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//										}
//
//									}
//									//deducibles
//									if(coberturaVO.isFlagDeducible()){
//										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else{
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
//										}
//
//
//									}																					
//									//primas
//									if(coberturaVO.isFlagPrima()&& !agenEsp2){
//										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,coberturaVO.getPrima(),false,2);	
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}else if(coberturaVO.isFlagPrima()){
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}
//								}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
//									//ANDRES-SUMASEG
//									//}
//									//										suma asegurada														
//									if(coberturaVO.isFlagSumaAsegurada()){	
//										if(coberturaVO.isFlagSumaAsegurada()){	
//											if (coberturaVO.getClaveCobertura().contains("6.2")){
//												double dias=0;
//												try {
//													if (coberturaVO.getSumaAsegurada().contains(",")){
//														int indice=0;
//														indice=coberturaVO.getSumaAsegurada().indexOf(",");
//														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
//														dias = Double.parseDouble(sumAseg)/500 ;
//													}
//													else{
//														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
//													}
//													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
//												} catch (Exception e) {
//													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//												}				
//												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
//												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
//											}
//											else{
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//											}
//											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//										}
//									}
//									//deducibles
//									if(coberturaVO.isFlagDeducible()){
//										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if ("45".equals(coberturaVO.getClaveCobertura())){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"U$S 200",false,1);
//										}else{
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
//										}
//
//
//									}																					
//									//primas
//									if(coberturaVO.isFlagPrima()&& !agenEsp2){
//										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}else if(coberturaVO.isFlagPrima()){
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}
//								}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
//									primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									log.debug("este es el acumulado de prima "+primaAux);
//								}		
//							}
//						}
//
//						boolean altoRiesgo = false;
//						if (validaAltoRiesgo) {
//							log.debug("Se validara el alto riesgo");
//							
//							Integer tarifa = getNumber(polizaVO.getTarifa());
//							
//							// Tarifas enero 1990 - diciembre 2029
//							boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
//							
//							if (tarifa < 1309 && formatoTarifaNormal) {
//								Integer amis = getNumber(polizaVO.getAmis());
//								int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
//								altoRiesgo = claveAmisAltoRiesgo == 9999;
//							} else {
//								List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
//								if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
//									Integer amis = getNumber(polizaVO.getAmis());
//									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
//									altoRiesgo = claveAmisAltoRiesgo == 9999;
//								}
//							}
//						}
//						if (altoRiesgo) {
//							cb = pdf.addLabel(cb, 35, 290, 7, "IMPORTANTE", true, 1);
//							cb = pdf.addLabel(cb, 35, 280, 7, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
//							cb = pdf.addLabel(cb, 35, 270, 7, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
//						}
//						
//						int dif = 0;
//						if (minimos){
//							dif = 20; // Para que no choquen leyendas
//							cb=pdf.addLabel(cb,35,270,7,DEDUCIBLE_MINIMOP1,true,1);
//							cb=pdf.addLabel(cb,35,263,7,DEDUCIBLE_MINIMOP2,true,1);
//						}
//						
//						String servicio = polizaVO.getServicio().trim();
//						String tarifa = polizaVO.getTarifa().trim();
//						log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
//						if (servicio.equals("PUBLICO")) {
//							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPub1,true,1);
//							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPub2,true,1);
//						} else if (servicio.equals("PARTICULAR")
//								&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
//							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPart1,true,1);
//							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPart2,true,1);
//						}
//						
//						if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
//							cb=pdf.addLabel(cb,35,250,7,polizaVO.getAsistencia(),true,1);
//						} else {
//							cb=pdf.addLabel(cb,35,250,7,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
//							
//							if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//							}
//							else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//							}
//							else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
//							}
//							else{
//								cb=pdf.addLabel(cb,290,250,7,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
//							}
//						}
//
//
//						//************Agente
//						cb=pdf.addRectAngColor(cb,35,242,335,12);
//						cb=pdf.addRectAng(cb,35,228,335,53);
//
//						cb=pdf.addLabel(cb,190,232,10,"OFICINA DE SERVICIO",true,0);
//						cb=pdf.addLabel(cb,40,218,8,"AGENTE",false,1);
//						cb=pdf.addLabel(cb,40,208,8,"NUMERO",false,1);
//						cb=pdf.addLabel(cb,190,208,8,"TELEFONO",false,1);
//						cb=pdf.addLabel(cb,40,198,8,"OFICINA",false,1);
////						cb=pdf.addLabel(cb,40,188,8,"DOMICILIO",false,1);
////						cb=pdf.addLabel(cb,310,188,8,"C.P.",false,1);
////						cb=pdf.addLabel(cb,40,178,8,"COL.",false,1);
////						cb=pdf.addLabel(cb,188,178,8,"TEL.",false,1);
////						cb=pdf.addLabel(cb,274,178,8,"FAX",false,1);
////						//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
////						//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
////						//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
////						//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
//						if(polizaVO!= null){
//							//la siguiente información va del nombre del agente a telefono nacional
//							String nombreAgente="";
//							if(polizaVO.getNombreAgente()!=null){
//								nombreAgente=polizaVO.getNombreAgente()+" ";}
//							if(polizaVO.getPateAgente()!=null){
//								nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
//							if(polizaVO.getMateAgente()!=null){
//								nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
//							if (polizaVO.getClavAgente().equals("52017")) {
//								nombreAgente = "";
//							}
//
//							cb=pdf.addLabel(cb,100,218,8,nombreAgente,false,1);
//
//							if(polizaVO.getClavAgente()!=null){
//								cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);}
//
//							//ANDRES-TELEFONO AGENTE
//							//System.out.println("telParti"+polizaVO.getTelPartAgente());
//							//System.out.println("telcomer"+polizaVO.getTelComerAgente());
//							if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//								if(polizaVO.getTelComerAgente()!=null){
//									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelComerAgente(),false,1);
//								}
//								else{
//									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelPartAgente(),false,1);
//								}
//
//							}
//
//
//
//
//
//
//
//							cb=pdf.addlineH(cb,31,206,343);
//							if(polizaVO.getDescOficina()!=null){
//								cb=pdf.addLabel(cb,100,198,8,polizaVO.getDescOficina(),false,1);}
//							if(polizaVO.getPoblacionOficina()!=null){
//								cb=pdf.addLabel(cb,350,198,8,polizaVO.getPoblacionOficina(),false,2);}							
//							if(polizaVO.getCalleOficina()!=null){						
//								cb=pdf.addLabel(cb,100,188,8,polizaVO.getCalleOficina(),false,1);}
//							if(polizaVO.getCodPostalOficina()!=null){										
//								cb=pdf.addLabel(cb,330,188,8,polizaVO.getCodPostalOficina(),false,1);}
//							if(polizaVO.getColoniaOficina()!=null){										
//								//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
//								cb=pdf.addLabel(cb,60,178,8,polizaVO.getColoniaOficina(),false,1);}
//
//							//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
//							//cb=pdf.addlineH(cb,240,177,125);
//
//							if(polizaVO.getTelOficina()!=null){
//								//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
//								cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina(),false,1);}
//
//							//if(polizaVO.getTelLocal()!=null){
//							//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
//							//}
//
//							if(polizaVO.getFaxOficina()!=null){					
//								cb=pdf.addLabel(cb,293,178,8,polizaVO.getFaxOficina(),false,1);}
//
//							//if(polizaVO.getTelNacional()!=null){
//							//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
//							//}
//						}				
//
//						cb=pdf.addRectAng(cb,35,175,335,25);
//						cb=pdf.addLabel(cb,40,159,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
//						cb=pdf.addLabel(cb,220,164,8,"  (55) 5258-2880    01-800-288-6700",true,1);
//						//cb=pdf.addLabel(cb,218,153,8,"01-800-004-9600    01-800-800-2880",true,1);
//						cb=pdf.addLabel(cb,218,153,8,"                                01-800-800-2880",true,1);
//
//						//************forma de pago
//						cb=pdf.addRectAng(cb,35,150,335,20);
//						cb=pdf.addLabel(cb,40,137,8,"FORMA DE PAGO:",false,1);
//						if(polizaVO != null){
//							if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
//							}
//							else {
//									cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);
//								
//								if(polizaVO.getDescrFormPago()!=null){
//										cb=pdf.addLabel(cb,120,137,8,polizaVO.getDescrFormPago(),false,1);
//								}
//	
//								//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
//								cb=pdf.addLabel(cb,310,141,8,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
//								if(polizaVO.getNumRecibos() > 1){
//									cb=pdf.addLabel(cb,190,141,8,"PRIMER PAGO",false,1);
//									cb=pdf.addLabel(cb,190,132,8,"PAGO(S) SUBSECUENTE(S)",false,1);																						
//									cb=pdf.addLabel(cb,310,132,8,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
//								}else{
//									cb=pdf.addLabel(cb,190,141,8,"PAGO UNICO",false,1);
//								}
//							}			
//						}
//
//						cb=pdf.addRectAng(cb,35,127,335,75);
//						cb=pdf.addLabel(cb,43,117,7.5f,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
//						cb=pdf.addLabel(cb,43,110,7.5f,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
//						cb=pdf.addLabel(cb,43,103,7.5f,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
//						cb=pdf.addLabel(cb,43,96,7.5f,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);
//
////						cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
////						cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
////						cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
////						cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
//						
//						cb=pdf.addLabel(cb,43,81,7.5f,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
//						cb=pdf.addLabel(cb,43,74,7.5f,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
//						cb=pdf.addLabel(cb,43,67,7.5f,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
//						cb=pdf.addLabel(cb,43,60,7.5f,"del día",false,1);
//						if (polizaVO.getcNSF()!=null){
//							cb=pdf.addLabel(cb,43,53,7.5f,polizaVO.getcNSF(),false,1);
//						}
//						
//						//CHAVA-LEYENDA ARTICULO 25
//						cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
//						cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);
//
//
//
//						//************importe
//						cb=pdf.addRectAngColor(cb,390,242,180,13);
//						cb=pdf.addLabel(cb,395,232,10,"MONEDA",true,1);
//						if(polizaVO.getDescMoneda()!=null){
//							cb=pdf.addLabel(cb,560,232,10,polizaVO.getDescMoneda(),true,2);}
//
//						cb=pdf.addRectAng(cb,390,226,180,13);										
//						cb=pdf.addRectAng(cb,390,210,180,83);
//
//						cb=pdf.addLabel(cb,395,200,8,"PRIMA NETA",false,1);
//						cb=pdf.addLabel(cb,395,190,8,"TASA FINANCIAMIENTO POR PAGO",false,1);
//						cb=pdf.addLabel(cb,395,180,8,"FRACCIONADO",false,1);
//						cb=pdf.addLabel(cb,395,167,8,"GTOS. EXPEDICION POL.",false,1);
//						cb=pdf.addLabel(cb,395,147,8,"SUBTOTAL",false,1);
//						cb=pdf.addLabel(cb,395,135,8,"I.V.A.",false,1);
//						cb=pdf.addLabel(cb,395,116,8,"IMPORTE TOTAL",true,1);
//						cb=pdf.addLabel(cb,395,100,8,"CONDICIONES VIGENTES:",false,1);
//						cb=pdf.addLabel(cb,395,88,8,"TARIFA APLICADA:",false,1);
//
//						if(polizaVO != null){
//							//la información siguiente va de prima neta a tarifa aplicada
//							if(polizaVO.getPrimaNeta()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(primaAux),false,2);
//								}
//								else
//									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
//							}
//							/*	if(polizaVO.getRecargo()!=null){ 
//									    if(Double.parseDouble(polizaVO.getRecargo())>0){
//									    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
//									}*/
//							if(polizaVO.getRecargo()!=null){
//								if(Double.parseDouble(polizaVO.getRecargo())>0){
//									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//										recargoAux=Double.parseDouble(polizaVO.getRecargo());
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
//									}
//									else{
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
//									}
//								}else{
//									if(Integer.parseInt(polizaVO.getNumIncisos())<0){
//										recargoAux=Double.parseDouble(polizaVO.getRecargo());
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
//									}else{
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
//									}
//								}
//							}
//							if(polizaVO.getDerecho()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
//									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(derechoAux),false,2);
//
//								}
//								else{
//									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
//							}
//							
//							
//							if(polizaVO.getCesionComision()!=null){
//								    cb=pdf.addLabel(cb,395,157,8,"DESCUENTOS",false,1);
//									cb=pdf.addLabel(cb,560,157,8,polizaVO.getCesionComision(),false,2);
//							}
//							
//							
//							if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								
//
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									subtotalAux = primaAux+derechoAux;				
//								}
//								else{
//									subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}
//
//								cb=pdf.addLabel(cb,560,147,8,FormatDecimal.numDecimal(subtotalAux),false,2);
//							}
//
//							cb=pdf.addlineH(cb,460,143,115);
//							if(polizaVO.getImpuesto()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
//									if(polizaVO.getIva()!=null){
//										impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
//										cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(impuestoAux),false,2);
//									}
//								}
//								else{
//									cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
//							}
//
//							cb=pdf.addRectAng(cb,390,125,180,13);
//							if(polizaVO.getPrimaTotal()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
//								}
//								else{
//									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
//							}
//
//							//***************************
//
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,555,100,8,polizaVO.getDescConVig(),false,2);
//							}
//
//							cb=pdf.addRectAng(cb,390,110,180,25);
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								//String concatZonaId= "0000"+polizaVO.getClaveOfic();
//
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
//									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
//									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//
//								//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//							}								
//
//						}
//
//						//****firma
//						if(polizaVO != null){
//							String lugar="";
//							if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//							if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//							cb=pdf.addLabel(cb,490,75,8,lugar,false,0);
//							//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
//							if(polizaVO.getFchEmi()!=null){
//								cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							/*if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
//						}
//
//
//						if(polizaVO.getDirImagen()!=null){
//							document=pdf.addImage(document,450,35,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							cb=pdf.addLabel(cb,490,35,7,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
//						}
//						cb=pdf.addLabel(cb,490,28,7,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
//						cb=pdf.addLabel(cb,490,21,7,"AUTORIZADO",false,0);	
//
//
//						if(polizaVO.getDescConVig()!=null){									
//							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables "+polizaVO.getDescConVig(),true,0);
//							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
//						}
//						else{
//							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables",true,0);
//							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
//						}
//
//
//						//ANDRES-MEM
//						if (membretado==null||membretado.equals("S")){
//							cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//							cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//							//document=pdf.addImageWaterMark(document,610,660,140,-145,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//						}


					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;





				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}

	
	/**
	 * PdfPolizaSeguro
	 * ===============
	 * Método que crea el pdf de poliza de seguro.
	 * 
	 * @param arrPolizas .- contiene las polizas que se pintan en el pdf.
	 * @param salida .- es el tipo de salida en el que se va a generar el pdf.
	 */
	//ANDRES-MEM
	//public void creaPdf(ArrayList arrPolizas,OutputStream salida){
	public void creaPdfNormal_08_oct_2015(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");

		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					document.newPage();

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();

						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addRectAngColorGreenWater(cb,35,718,535,30);
							cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
							//document=pdf.addImage(document,35,725,130,50,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/logoQpoliza.jpg");
							//recibo.get(page).getDirImagen()+"logoQpoliza.jpg"
							document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
						}

						cb=pdf.addLabel(cb,100,700,12,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
						cb=pdf.addLabel(cb,400,706,10,"POLIZA",false,1);
						cb=pdf.addLabel(cb,460,706,10,"ENDOSO",false,1);
						cb=pdf.addLabel(cb,525,706,10,"INCISO",false,1);
						if(polizaVO != null){
							//el orden de los datos siguientes va de poliza a inciso
							String inciso="000";
							String incisoAux;

							if(polizaVO.getNumPoliza()!=null){
								sizeNumPoliza=polizaVO.getNumPoliza().length();
								cb=pdf.addLabel(cb,400,694,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

								cb=pdf.addLabel(cb,460,694,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
							}

							if(polizaVO.getInciso()!=null){
								inciso = inciso+polizaVO.getInciso();								
								incisoAux =inciso.substring(inciso.length()-4,inciso.length());
								cb=pdf.addLabel(cb,525,694,9,incisoAux,false,1);
							}
						}


						//**********CUERPO										
						cb=pdf.addRectAngColor(cb,35,661,535,12);
						cb=pdf.addRectAng(cb,35,648,535,43);


						//***********asegurado					
						cb=pdf.addLabel(cb,290,651,10,"INFORMACION DEL ASEGURADO",true,0);				
						cb=pdf.addLabel(cb,440,640,8,"RENUEVA A:",false,1);
						cb=pdf.addLabel(cb,40,630,8,"DOMICILIO",false,1);
						cb=pdf.addLabel(cb,40,620,8,"C.P.",false,1);
						cb=pdf.addLabel(cb,340,620,8,"RFC",false,1);
						//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
						if(polizaVO.getPolizaAnterior()!=null){
							cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
						if(polizaVO != null){
							//el orden de los datos siguientes va del nombre del asegurado a beneficiario
							String nombre=""; 
							if(polizaVO.getNombre()!=null){
								nombre=polizaVO.getNombre()+" ";}
							if(polizaVO.getApePate()!=null){
								nombre=nombre+polizaVO.getApePate()+" ";}
							if(polizaVO.getApeMate()!=null){
								nombre=nombre+polizaVO.getApeMate()+" ";}

							if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
								nombre=nombre+" Y/O "+polizaVO.getConductor();
							}	

							cb=pdf.addLabel(cb,40,640,8,nombre,false,1);
							if(datosCliente){									
								cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	

								if(polizaVO.getCalle()!=null){
									String calle= polizaVO.getCalle();
									if(polizaVO.getExterior()!= null){
										calle += " No. EXT. " + polizaVO.getExterior();
									}
									if(polizaVO.getInterior()!= null){
										calle += " No. INT. " + polizaVO.getInterior();
									}
									//ANDRES-prueba 
									//System.out.println("colonia:::"+polizaVO.getColonia());
									if(polizaVO.getColonia()!=null){
										calle += " COL. " + polizaVO.getColonia();
									}
									cb=pdf.addLabel(cb,90,630,8,calle,false,1);}

								if(polizaVO.getCodPostal()!=null){
									cb=pdf.addLabel(cb,70,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
								if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
									cb=pdf.addLabel(cb,130,620,8,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
								if(polizaVO.getRfc()!=null){
									cb=pdf.addLabel(cb,390,620,8,polizaVO.getRfc(),false,1);}
							
								if(polizaVO.getBeneficiario()!=null){
									if(polizaVO.getBeneficiario().length()>1){
										cb=pdf.addLabel(cb,40,610,8,"BENEFICIARIO PREFERENTE "+polizaVO.getBeneficiario(),false,1);
									}
								}
								
								

								for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
									LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
									if(campo.getLabel().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
									j=j+50;
									if(campo.getValue().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
									j=j+50;
								}
							}
							if(polizaVO.getCveApoderado()!=null){
								cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);
								cb=pdf.addLabel(cb,100,610,8,polizaVO.getCveApoderado(),false,1);
							}							

						}				


						//*************Vehiculo
						cb=pdf.addRectAngColor(cb,35,603,535,12);
						cb=pdf.addRectAng(cb,35,589,535,55);

						cb=pdf.addLabel(cb,290,592,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,0);
						cb=pdf.addLabel(cb,40,565,8,"TIPO",true,1);
						cb=pdf.addLabel(cb,170,565,8,"MODELO",true,1);
						cb=pdf.addLabel(cb,260,565,8,"COLOR",true,1);
						cb=pdf.addLabel(cb,490,565,8,"PLACAS",true,1);
						cb=pdf.addLabel(cb,40,552,8,"SERIE",true,1);
						cb=pdf.addLabel(cb,259,552,8,"MOTOR",true,1);
						cb=pdf.addLabel(cb,390,552,8,"REPUVE",true,1);
						if(polizaVO != null){
							//la información siguiente va de descripción del vehiculo a tipo de carga							
							if(polizaVO.getAmis()!=null){
								cb=pdf.addLabel(cb,40,580,8,polizaVO.getAmis(),true,1);}							
							if(polizaVO.getDescVehi()!=null){
								cb=pdf.addLabel(cb,70,580,8,polizaVO.getDescVehi(),false,1);}											

							if(polizaVO.getTipo()!=null){

								if(polizaVO.getTipo().length()>18){
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo().substring(0, 19),false,1);
								}else{
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo(),false,1); 
								}	
							}

							if(polizaVO.getVehiAnio()!=null){												
								cb=pdf.addLabel(cb,210,565,8,polizaVO.getVehiAnio(),false,1);}
							if(polizaVO.getColor()!=null){

								if(polizaVO.getColor().equals("SIN COLOR")){

									cb=pdf.addLabel(cb,300,565,8,"",false,1);		
								}else{
									cb=pdf.addLabel(cb,300,565,8,polizaVO.getColor(),false,1);
								}
							}
							//ANDRES-PASAJEROS
							if (polizaVO.getNumPasajeros()!=null){
								cb=pdf.addLabel(cb,40,542,8,polizaVO.getNumPasajeros(),false,1);
							}
							else if(polizaVO.getNumOcupantes()!=null){
								cb=pdf.addLabel(cb,390,565,8,"OCUP.",true,1);
								cb=pdf.addLabel(cb,420,565,8,polizaVO.getNumOcupantes(),false,1);
							}

							if(polizaVO.getNumPlaca()!=null){												
								cb=pdf.addLabel(cb,530,565,8,polizaVO.getNumPlaca(),false,1);}
							if(polizaVO.getNumSerie()!=null){					
								cb=pdf.addLabel(cb,70,552,8,polizaVO.getNumSerie(),false,1);}
							if(polizaVO.getNumMotor()!=null){					
								cb=pdf.addLabel(cb,300,552,8,polizaVO.getNumMotor(),false,1);}
							if(polizaVO.getRenave()!=null){					
								cb=pdf.addLabel(cb,440,552,8,polizaVO.getRenave(),false,1);}
							
							if (polizaVO.getCveServ().trim().equals("3")) {
								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
									cb=pdf.addLabel(cb,110,539,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
								if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
									cb=pdf.addLabel(cb,200,539,8,polizaVO.getTipoCarga()+" : ",true,2);}
								//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
								if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
									String descAux = "";
									String valorRemolque="";
									if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
										descAux=polizaVO.getDescCarga();
									}
									
									if(polizaVO.getDobleRemolque()!= null){
										if(polizaVO.getDobleRemolque().equals("S")){
											valorRemolque = "2° Remolque: AMPARADO";
										}else{
											valorRemolque = "2° Remolque: EXCLUIDO";
										}
									}								
									if(descAux != "" || valorRemolque != ""){
										cb=pdf.addLabel(cb,210,539,8,descAux+"  "+valorRemolque,false,1);
									}
									
								}
							}
							
							
							if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
									cb=pdf.addLabel(cb,110,539,8,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
								}
							}
							
							
							if(polizaVO.getNoEconomico()!=null){
								cb=pdf.addLabel(cb,480,552,8,"NO.ECO.",true,1);
								cb=pdf.addLabel(cb,515,552,8,polizaVO.getNoEconomico(),false,1);
							}
							
							
							
							
							
							
							
						}


						//**************vigencia
						cb=pdf.addRectAng(cb,35,531,150,25);
						cb=pdf.addRectAng(cb,193,531,50,25);
						cb=pdf.addRectAng(cb,251,531,60,25);
						cb=pdf.addRectAng(cb,319,531,50,25);						
						cb=pdf.addRectAng(cb,377,531,192,25);

						cb=pdf.addLabel(cb,33,526,5,"VIGENCIA:",false,1);
						cb=pdf.addLabel(cb,33,518,7,"DESDE LAS 12 HORAS P.M. DEL  ",false,1);
						cb=pdf.addLabel(cb,33,510,7,"HASTA LAS 12 HORAS P.M. DEL  ",false,1);
						cb=pdf.addLabel(cb,218,522,8,"PLAZO PAGO",false,0);
						cb=pdf.addLabel(cb,251,522,8,"F. LIMITE PAGO",false,1);
						cb=pdf.addLabel(cb,344,522,8,"MOVIMIENTO",false,0);
						cb=pdf.addLabel(cb,415,522,8,"USO",false,0);
						cb=pdf.addLabel(cb,515,522,8,"SERVICIO",false,0);
						if(polizaVO != null){		
							//la información siguiente va desde la fecha de vigencia hasta servicio
							if(polizaVO.getFchIni()!=null){
								cb=pdf.addLabel(cb,143,518,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
							}
							if(polizaVO.getFchFin()!=null){
								cb=pdf.addLabel(cb,143,510,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

							}

							if(polizaVO.getPlazoPago()!=null){
								cb=pdf.addLabel(cb,218,512,8, polizaVO.getPlazoPago()+" dias",false,0);}

							if(polizaVO.getFechaLimPago()!=null){																												
								cb=pdf.addLabel(cb,259,512,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}


							if(polizaVO.getMovimiento()!=null){
								cb=pdf.addLabel(cb,344,512,8,polizaVO.getMovimiento(),false,0);
							}

							if(polizaVO.getUso()!=null){
								ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      15 , 21*2 , 25*3
								if(uso!=null){
									if(uso.size()==1){
										cb=pdf.addLabel(cb,415,512,8,(String)uso.get(0),false,0);
									}
									else if(uso.size()==2){
										cb=pdf.addLabel(cb,415,515,6,(String)uso.get(0),false,0);
										cb=pdf.addLabel(cb,415,509,6,(String)uso.get(1),false,0);
									}	
									else if(uso.size()==3){
										cb=pdf.addLabel(cb,415,517,5,(String)uso.get(0),false,0);
										cb=pdf.addLabel(cb,415,512,5,(String)uso.get(1),false,0);
										cb=pdf.addLabel(cb,415,507,5,(String)uso.get(2),false,0);
									}											
								}									
							}
							if(polizaVO.getServicio()!=null){
								ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      23 , 31*2 , 37*3
								if(servicio!=null){
									if(servicio.size()==1){
										cb=pdf.addLabel(cb,515,512,8,(String)servicio.get(0),false,0);
									}
									else if(servicio.size()==2){
										cb=pdf.addLabel(cb,515,515,6,(String)servicio.get(0),false,0);
										cb=pdf.addLabel(cb,515,509,6,(String)servicio.get(1),false,0);
									}
									else if(servicio.size()==3){
										cb=pdf.addLabel(cb,515,517,5,(String)servicio.get(0),false,0);
										cb=pdf.addLabel(cb,515,512,5,(String)servicio.get(1),false,0);
										cb=pdf.addLabel(cb,515,507,5,(String)servicio.get(2),false,0);
									}
								}									
							}					
						}


						//*************Datos de Riesgos
						cb=pdf.addRectAngColor(cb,35,502,535,12);	
						cb=pdf.addRectAng(cb,35,482,535,235);

						cb=pdf.addLabel(cb,60,492,10,"COBERTURAS CONTRATADAS",true,1);
						cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
						cb=pdf.addLabel(cb,420,492,10,"DEDUCIBLE",true,1);
						cb=pdf.addLabel(cb,510,492,10,"$     PRIMAS",true,1);

						//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
						//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
						//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
						//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
						double primaAux=0;
						double primaExe=0;
						double derechoAux=0;
						double recargoAux=0;
						double subtotalAux=0;
						double impuestoAux=0;
						boolean exDM=false;
						boolean exRT=false;
						boolean agenEsp1 = false;
						boolean agenEsp2 = false;
						boolean validaAltoRiesgo = false;					

						for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
							int temp = (Integer)polizaVO.getAgenteEsp().get(y);
							if(temp==1)
								agenEsp1=true;
							if(temp==AGEN_ESP_OCULTA_PRIMAS)
								agenEsp2=true;
						}
						
						boolean minimos=false;

						if(polizaVO.getCoberturasArr()!=null){
							CoberturasPdfBean coberturaVO;

							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{
								coberturaVO= new CoberturasPdfBean();
								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								if(coberturaVO.getClaveCobertura().equals("12")){
									exDM=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}else if(coberturaVO.getClaveCobertura().equals("40")){
									exRT=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}	
							}
							
							String cveServ = polizaVO.getCveServ().trim();
							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{	
								boolean salto=false;
								if(x==toppage+27)
								{break;}

								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								String claveCobertura = coberturaVO.getClaveCobertura();

								int deducible = getNumber(coberturaVO.getDeducible());
								int anio = getNumber(polizaVO.getVehiAnio());
								if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
									validaAltoRiesgo = true;
								}

								//ANDRES-MINIMOS
								if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
									minimos=true;
								}


								log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
								//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
								//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
								//}else{
								if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
								}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
									salto = true;
								}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
									salto = true;
								}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
									cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
								}else{
									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
								}
								//}

								if(!salto){
									//ANDRES-SUMASEG
									//suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if (coberturaVO.getClaveCobertura().contains("6.2")){
											double dias=0;
											try {
												if (coberturaVO.getSumaAsegurada().contains(",")){
													int indice=0;
													indice=coberturaVO.getSumaAsegurada().indexOf(",");
													String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
													dias = Double.parseDouble(sumAseg)/500 ;
												}
												else{
													dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
												}
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
											} catch (Exception e) {
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}				
											//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
										}
										else if(coberturaVO.getClaveCobertura().equals("12")) {
											cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
											
										}
										else{
											cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}

									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,coberturaVO.getPrima(),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
									//ANDRES-SUMASEG
									//}
									//										suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
												}				
												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
											}
											else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}
									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if ("45".equals(coberturaVO.getClaveCobertura())){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"U$S 200",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
									primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									log.debug("este es el acumulado de prima "+primaAux);
								}		
							}
						}

						boolean altoRiesgo = false;
						if (validaAltoRiesgo) {
							log.debug("Se validara el alto riesgo");
							
							Integer tarifa = getNumber(polizaVO.getTarifa());
							
							// Tarifas enero 1990 - diciembre 2029
							boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
							
							if (tarifa < 1309 && formatoTarifaNormal) {
								Integer amis = getNumber(polizaVO.getAmis());
								int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
								altoRiesgo = claveAmisAltoRiesgo == 9999;
							} else {
								List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
								if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								}
							}
						}
						if (altoRiesgo) {
							cb = pdf.addLabel(cb, 35, 290, 7, "IMPORTANTE", true, 1);
							cb = pdf.addLabel(cb, 35, 280, 7, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
							cb = pdf.addLabel(cb, 35, 270, 7, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
						}
						
						int dif = 0;
						if (minimos){
							dif = 20; // Para que no choquen leyendas
							cb=pdf.addLabel(cb,35,270,7,DEDUCIBLE_MINIMOP1,true,1);
							cb=pdf.addLabel(cb,35,263,7,DEDUCIBLE_MINIMOP2,true,1);
						}
						
						String servicio = polizaVO.getServicio().trim();
						String tarifa = polizaVO.getTarifa().trim();
						log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
						if (servicio.equals("PUBLICO")) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPub1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPub2,true,1);
						} else if (servicio.equals("PARTICULAR")
								&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPart1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPart2,true,1);
						}
						
						if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
							cb=pdf.addLabel(cb,35,250,7,polizaVO.getAsistencia(),true,1);
						} else {
							cb=pdf.addLabel(cb,35,250,7,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
							
							if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
							}
							else{
								cb=pdf.addLabel(cb,290,250,7,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
							}
						}


						//************Agente
						cb=pdf.addRectAngColor(cb,35,242,335,12);
						cb=pdf.addRectAng(cb,35,228,335,53);

						cb=pdf.addLabel(cb,190,232,10,"OFICINA DE SERVICIO 2",true,0);
						cb=pdf.addLabel(cb,40,218,8,"AGENTE",false,1);
						cb=pdf.addLabel(cb,40,208,8,"NUMERO",false,1);
						cb=pdf.addLabel(cb,190,208,8,"TELEFONO",false,1);
						cb=pdf.addLabel(cb,40,198,8,"OFICINA",false,1);
						cb=pdf.addLabel(cb,40,188,8,"DOMICILIO",false,1);
						cb=pdf.addLabel(cb,310,188,8,"C.P.",false,1);
						cb=pdf.addLabel(cb,40,178,8,"COL.",false,1);
						cb=pdf.addLabel(cb,188,178,8,"TEL.",false,1);
						cb=pdf.addLabel(cb,274,178,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
						//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
						//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
						if(polizaVO!= null){
							//la siguiente información va del nombre del agente a telefono nacional
							String nombreAgente="";
							if(polizaVO.getNombreAgente()!=null){
								nombreAgente=polizaVO.getNombreAgente()+" ";}
							if(polizaVO.getPateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
							if(polizaVO.getMateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
							if (polizaVO.getClavAgente().equals("52017")) {
								nombreAgente = "";
							}

							cb=pdf.addLabel(cb,100,218,8,nombreAgente,false,1);

							if(polizaVO.getClavAgente()!=null){
								cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);}

							//ANDRES-TELEFONO AGENTE
							//System.out.println("telParti"+polizaVO.getTelPartAgente());
							//System.out.println("telcomer"+polizaVO.getTelComerAgente());
							if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
								if(polizaVO.getTelComerAgente()!=null){
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelComerAgente(),false,1);
								}
								else{
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelPartAgente(),false,1);
								}

							}







							cb=pdf.addlineH(cb,31,206,343);
							if(polizaVO.getDescOficina()!=null){
								cb=pdf.addLabel(cb,100,198,8,polizaVO.getDescOficina(),false,1);}
							if(polizaVO.getPoblacionOficina()!=null){
								cb=pdf.addLabel(cb,350,198,8,polizaVO.getPoblacionOficina(),false,2);}							
							if(polizaVO.getCalleOficina()!=null){						
								cb=pdf.addLabel(cb,100,188,8,polizaVO.getCalleOficina(),false,1);}
							if(polizaVO.getCodPostalOficina()!=null){										
								cb=pdf.addLabel(cb,330,188,8,polizaVO.getCodPostalOficina(),false,1);}
							if(polizaVO.getColoniaOficina()!=null){										
								//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
								cb=pdf.addLabel(cb,60,178,8,polizaVO.getColoniaOficina(),false,1);}

							//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
							//cb=pdf.addlineH(cb,240,177,125);

							if(polizaVO.getTelOficina()!=null){
								//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
								cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina(),false,1);}

							//if(polizaVO.getTelLocal()!=null){
							//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
							//}

							if(polizaVO.getFaxOficina()!=null){					
								cb=pdf.addLabel(cb,293,178,8,polizaVO.getFaxOficina(),false,1);}

							//if(polizaVO.getTelNacional()!=null){
							//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
							//}
						}				

						cb=pdf.addRectAng(cb,35,175,335,25);
						cb=pdf.addLabel(cb,40,159,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
						cb=pdf.addLabel(cb,220,164,8,"  (55) 5258-2880    01-800-288-6700",true,1);
						//cb=pdf.addLabel(cb,218,153,8,"01-800-004-9600    01-800-800-2880",true,1);
						cb=pdf.addLabel(cb,218,153,8,"                                01-800-800-2880",true,1);

						//************forma de pago
						cb=pdf.addRectAng(cb,35,150,335,20);
						cb=pdf.addLabel(cb,40,137,8,"FORMA DE PAGO:",false,1);
						if(polizaVO != null){
							if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
							}
							else {
									cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);
								
								if(polizaVO.getDescrFormPago()!=null){
										cb=pdf.addLabel(cb,120,137,8,polizaVO.getDescrFormPago(),false,1);
								}
	
								//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
								cb=pdf.addLabel(cb,310,141,8,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
								if(polizaVO.getNumRecibos() > 1){
									cb=pdf.addLabel(cb,190,141,8,"PRIMER PAGO",false,1);
									cb=pdf.addLabel(cb,190,132,8,"PAGO(S) SUBSECUENTE(S)",false,1);																						
									cb=pdf.addLabel(cb,310,132,8,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
								}else{
									cb=pdf.addLabel(cb,190,141,8,"PAGO UNICO",false,1);
								}
							}			
						}

						cb=pdf.addRectAng(cb,35,127,335,75);
						cb=pdf.addLabel(cb,43,117,7.5f,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
						cb=pdf.addLabel(cb,43,110,7.5f,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
						cb=pdf.addLabel(cb,43,103,7.5f,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
						cb=pdf.addLabel(cb,43,96,7.5f,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);

//						cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
//						cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
//						cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
//						cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
						
						cb=pdf.addLabel(cb,43,81,7.5f,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
						cb=pdf.addLabel(cb,43,74,7.5f,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
						cb=pdf.addLabel(cb,43,67,7.5f,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
						cb=pdf.addLabel(cb,43,60,7.5f,"del día",false,1);
						if (polizaVO.getcNSF()!=null){
							cb=pdf.addLabel(cb,43,53,7.5f,polizaVO.getcNSF(),false,1);
						}
						
						//CHAVA-LEYENDA ARTICULO 25
						cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
						cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);



						//************importe
						cb=pdf.addRectAngColor(cb,390,242,180,13);
						cb=pdf.addLabel(cb,395,232,10,"MONEDA",true,1);
						if(polizaVO.getDescMoneda()!=null){
							cb=pdf.addLabel(cb,560,232,10,polizaVO.getDescMoneda(),true,2);}

						cb=pdf.addRectAng(cb,390,226,180,13);										
						cb=pdf.addRectAng(cb,390,210,180,83);

						cb=pdf.addLabel(cb,395,200,8,"PRIMA NETA",false,1);
						cb=pdf.addLabel(cb,395,190,8,"TASA FINANCIAMIENTO POR PAGO",false,1);
						cb=pdf.addLabel(cb,395,180,8,"FRACCIONADO",false,1);
						cb=pdf.addLabel(cb,395,167,8,"GTOS. EXPEDICION POL.",false,1);
						cb=pdf.addLabel(cb,395,147,8,"SUBTOTAL",false,1);
						cb=pdf.addLabel(cb,395,135,8,"I.V.A.",false,1);
						cb=pdf.addLabel(cb,395,116,8,"IMPORTE TOTAL",true,1);
						cb=pdf.addLabel(cb,395,100,8,"CONDICIONES VIGENTES:",false,1);
						cb=pdf.addLabel(cb,395,88,8,"TARIFA APLICADA:",false,1);

						if(polizaVO != null){
							//la información siguiente va de prima neta a tarifa aplicada
							if(polizaVO.getPrimaNeta()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(primaAux),false,2);
								}
								else
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
							}
							/*	if(polizaVO.getRecargo()!=null){ 
									    if(Double.parseDouble(polizaVO.getRecargo())>0){
									    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
									}*/
							if(polizaVO.getRecargo()!=null){
								if(Double.parseDouble(polizaVO.getRecargo())>0){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}
									else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}else{
									if(Integer.parseInt(polizaVO.getNumIncisos())<0){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}
							}
							if(polizaVO.getDerecho()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(derechoAux),false,2);

								}
								else{
									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
							}
							
							
							if(polizaVO.getCesionComision()!=null){
								    cb=pdf.addLabel(cb,395,157,8,"DESCUENTOS",false,1);
									cb=pdf.addLabel(cb,560,157,8,polizaVO.getCesionComision(),false,2);
							}
							
							
							if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									subtotalAux = primaAux+derechoAux;				
								}
								else{
									subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

								cb=pdf.addLabel(cb,560,147,8,FormatDecimal.numDecimal(subtotalAux),false,2);
							}

							cb=pdf.addlineH(cb,460,143,115);
							if(polizaVO.getImpuesto()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
									if(polizaVO.getIva()!=null){
										impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
										cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(impuestoAux),false,2);
									}
								}
								else{
									cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
							}

							cb=pdf.addRectAng(cb,390,125,180,13);
							if(polizaVO.getPrimaTotal()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
								}
								else{
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
							}

							//***************************

							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,555,100,8,polizaVO.getDescConVig(),false,2);
							}

							cb=pdf.addRectAng(cb,390,110,180,25);
							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
								//String concatZonaId= "0000"+polizaVO.getClaveOfic();

								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}
								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApCve();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}

								//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
							}								

						}

						//****firma
						if(polizaVO != null){
							String lugar="";
							if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
							if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
							cb=pdf.addLabel(cb,490,75,8,lugar,false,0);
							//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
							if(polizaVO.getFchEmi()!=null){
								cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
							/*if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
						}


						if(polizaVO.getDirImagen()!=null){
							document=pdf.addImage(document,450,35,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
							cb=pdf.addLabel(cb,490,35,7,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
						}
						cb=pdf.addLabel(cb,490,28,7,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
						cb=pdf.addLabel(cb,490,21,7,"AUTORIZADO",false,0);	


						if(polizaVO.getDescConVig()!=null){									
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables "+polizaVO.getDescConVig(),true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}
						else{
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables",true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}


						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
							cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							//document=pdf.addImageWaterMark(document,610,660,140,-145,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/Qmembretado.jpg",cb=writer.getDirectContentUnder());
							document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
						}


					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;





				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}
	
	private int getNumber(String number) {
		int intNumber = 0;
		String numberAux = number.replaceAll("\\D+", "");
		if (StringUtils.isNotEmpty(numberAux)) {
			intNumber = Integer.parseInt(numberAux);
		}
		return intNumber;
	}

	public void setAltoRiesgoService(AltoRiesgoService altoRiesgoService) {
		this.altoRiesgoService = altoRiesgoService;
	}

	
	
	
	//agregando cambio(quitar la numeracion de coberturas , y agregar texto CNSF)
	public void creaPdfNormal(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");

		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					document.newPage();

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();

						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addRectAngColorGreenWater(cb,35,718,535,30);
							cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
							//document=pdf.addImage(document,35,725,130,50,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/logoQpoliza.jpg");
							//recibo.get(page).getDirImagen()+"logoQpoliza.jpg"
							document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
						}

						cb=pdf.addLabel(cb,100,700,12,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
						cb=pdf.addLabel(cb,400,706,10,"POLIZA",false,1);
						cb=pdf.addLabel(cb,460,706,10,"ENDOSO",false,1);
						cb=pdf.addLabel(cb,525,706,10,"INCISO",false,1);
						if(polizaVO != null){
							//el orden de los datos siguientes va de poliza a inciso
							String inciso="000";
							String incisoAux;

							if(polizaVO.getNumPoliza()!=null){
								sizeNumPoliza=polizaVO.getNumPoliza().length();
								cb=pdf.addLabel(cb,400,694,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

								cb=pdf.addLabel(cb,460,694,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
							}

							if(polizaVO.getInciso()!=null){
								inciso = inciso+polizaVO.getInciso();								
								incisoAux =inciso.substring(inciso.length()-4,inciso.length());
								cb=pdf.addLabel(cb,525,694,9,incisoAux,false,1);
							}
						}


						//**********CUERPO										
						cb=pdf.addRectAngColor(cb,35,661,535,12);
						cb=pdf.addRectAng(cb,35,648,535,43);


						//***********asegurado					
						cb=pdf.addLabel(cb,290,651,10,"INFORMACION DEL ASEGURADO",true,0);				
						cb=pdf.addLabel(cb,440,640,8,"RENUEVA A:",false,1);
						cb=pdf.addLabel(cb,40,630,8,"DOMICILIO",false,1);
						cb=pdf.addLabel(cb,40,620,8,"C.P.",false,1);
						cb=pdf.addLabel(cb,340,620,8,"RFC",false,1);
						//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
						if(polizaVO.getPolizaAnterior()!=null){
							cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
						if(polizaVO != null){
							//el orden de los datos siguientes va del nombre del asegurado a beneficiario
							String nombre=""; 
							if(polizaVO.getNombre()!=null){
								nombre=polizaVO.getNombre()+" ";}
							if(polizaVO.getApePate()!=null){
								nombre=nombre+polizaVO.getApePate()+" ";}
							if(polizaVO.getApeMate()!=null){
								nombre=nombre+polizaVO.getApeMate()+" ";}

							if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
								nombre=nombre+" Y/O "+polizaVO.getConductor();
							}	

							cb=pdf.addLabel(cb,40,640,8,nombre,false,1);
							if(datosCliente){									
								cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	

								if(polizaVO.getCalle()!=null){
									String calle= polizaVO.getCalle();
									if(polizaVO.getExterior()!= null){
										calle += " No. EXT. " + polizaVO.getExterior();
									}
									if(polizaVO.getInterior()!= null){
										calle += " No. INT. " + polizaVO.getInterior();
									}
									//ANDRES-prueba 
									//System.out.println("colonia:::"+polizaVO.getColonia());
									if(polizaVO.getColonia()!=null){
										calle += " COL. " + polizaVO.getColonia();
									}
									cb=pdf.addLabel(cb,90,630,8,calle,false,1);}

								if(polizaVO.getCodPostal()!=null){
									cb=pdf.addLabel(cb,70,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
								if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
									cb=pdf.addLabel(cb,130,620,8,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
								if(polizaVO.getRfc()!=null){
									cb=pdf.addLabel(cb,390,620,8,polizaVO.getRfc(),false,1);}
								if(polizaVO.getBeneficiario()!=null){
									if(polizaVO.getBeneficiario().length()>1){
										cb=pdf.addLabel(cb,40,610,8,"BENEFICIARIO PREFERENTE "+polizaVO.getBeneficiario(),false,1);
									}
								}

								for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
									LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
									if(campo.getLabel().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
									j=j+50;
									if(campo.getValue().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
									j=j+50;
								}
							}
							if(polizaVO.getCveApoderado()!=null){
								cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);
								cb=pdf.addLabel(cb,100,610,8,polizaVO.getCveApoderado(),false,1);
							}							

						}				


						//*************Vehiculo
						cb=pdf.addRectAngColor(cb,35,603,535,12);
						cb=pdf.addRectAng(cb,35,589,535,55);

						cb=pdf.addLabel(cb,290,592,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,0);
						cb=pdf.addLabel(cb,40,565,8,"TIPO",true,1);
						cb=pdf.addLabel(cb,170,565,8,"MODELO",true,1);
						cb=pdf.addLabel(cb,260,565,8,"COLOR",true,1);
						cb=pdf.addLabel(cb,490,565,8,"PLACAS",true,1);
						cb=pdf.addLabel(cb,40,552,8,"SERIE",true,1);
						cb=pdf.addLabel(cb,259,552,8,"MOTOR",true,1);
						cb=pdf.addLabel(cb,390,552,8,"REPUVE",true,1);
						if(polizaVO != null){
							//la información siguiente va de descripción del vehiculo a tipo de carga							
							if(polizaVO.getAmis()!=null){
								cb=pdf.addLabel(cb,40,580,8,polizaVO.getAmis(),true,1);}							
							if(polizaVO.getDescVehi()!=null){
								cb=pdf.addLabel(cb,70,580,8,polizaVO.getDescVehi(),false,1);}											

							if(polizaVO.getTipo()!=null){

								if(polizaVO.getTipo().length()>18){
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo().substring(0, 19),false,1);
								}else{
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo(),false,1); 
								}	
							}

							if(polizaVO.getVehiAnio()!=null){												
								cb=pdf.addLabel(cb,210,565,8,polizaVO.getVehiAnio(),false,1);}
							if(polizaVO.getColor()!=null){

								if(polizaVO.getColor().equals("SIN COLOR")){

									cb=pdf.addLabel(cb,300,565,8,"",false,1);		
								}else{
									cb=pdf.addLabel(cb,300,565,8,polizaVO.getColor(),false,1);
								}
							}
							//ANDRES-PASAJEROS
							if (polizaVO.getNumPasajeros()!=null){
								cb=pdf.addLabel(cb,40,542,8,polizaVO.getNumPasajeros(),false,1);
							}
							else if(polizaVO.getNumOcupantes()!=null){
								cb=pdf.addLabel(cb,390,565,8,"OCUP.",true,1);
								cb=pdf.addLabel(cb,420,565,8,polizaVO.getNumOcupantes(),false,1);
							}

							if(polizaVO.getNumPlaca()!=null){												
								cb=pdf.addLabel(cb,530,565,8,polizaVO.getNumPlaca(),false,1);}
							if(polizaVO.getNumSerie()!=null){					
								cb=pdf.addLabel(cb,70,552,8,polizaVO.getNumSerie(),false,1);}
							if(polizaVO.getNumMotor()!=null){					
								cb=pdf.addLabel(cb,295,552,8,polizaVO.getNumMotor(),false,1);}
							if(polizaVO.getRenave()!=null){					
								cb=pdf.addLabel(cb,440,552,8,polizaVO.getRenave(),false,1);}
							
							if (polizaVO.getCveServ().trim().equals("3")) {
								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
									cb=pdf.addLabel(cb,110,539,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
								if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
									cb=pdf.addLabel(cb,200,539,8,polizaVO.getTipoCarga()+" : ",true,2);}
								//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
								if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
									String descAux = "";
									String valorRemolque="";
									if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
										descAux=polizaVO.getDescCarga();
									}
									
									if(polizaVO.getDobleRemolque()!= null){
										if(polizaVO.getDobleRemolque().equals("S")){
											valorRemolque = "2° Remolque: AMPARADO";
										}else{
											valorRemolque = "2° Remolque: EXCLUIDO";
										}
									}								
									if(descAux != "" || valorRemolque != ""){
										cb=pdf.addLabel(cb,210,539,8,descAux+"  "+valorRemolque,false,1);
									}
									
								}
							}
							
							
							if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
									cb=pdf.addLabel(cb,110,539,8,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
								}
							}
							
							
							if(polizaVO.getNoEconomico()!=null){
								cb=pdf.addLabel(cb,480,552,8,"NO.ECO.",true,1);
								cb=pdf.addLabel(cb,515,552,8,polizaVO.getNoEconomico(),false,1);
							}
							
							
							
							
							
							
							
						}


						//**************vigencia
						cb=pdf.addRectAng(cb,35,531,150,25);
						cb=pdf.addRectAng(cb,193,531,50,25);
						cb=pdf.addRectAng(cb,251,531,60,25);
						cb=pdf.addRectAng(cb,319,531,50,25);						
						cb=pdf.addRectAng(cb,377,531,192,25);

						cb=pdf.addLabel(cb,33,526,5,"VIGENCIA:",false,1);
						cb=pdf.addLabel(cb,33,518,7,"DESDE LAS 12 HORAS P.M. DEL  ",false,1);
						cb=pdf.addLabel(cb,33,510,7,"HASTA LAS 12 HORAS P.M. DEL  ",false,1);
						cb=pdf.addLabel(cb,218,522,8,"PLAZO PAGO",false,0);
						cb=pdf.addLabel(cb,251,522,8,"F. LIMITE PAGO",false,1);
						cb=pdf.addLabel(cb,344,522,8,"MOVIMIENTO",false,0);
						cb=pdf.addLabel(cb,415,522,8,"USO",false,0);
						cb=pdf.addLabel(cb,515,522,8,"SERVICIO",false,0);
						if(polizaVO != null){		
							//la información siguiente va desde la fecha de vigencia hasta servicio
							if(polizaVO.getFchIni()!=null){
								cb=pdf.addLabel(cb,143,518,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
							}
							if(polizaVO.getFchFin()!=null){
								cb=pdf.addLabel(cb,143,510,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

							}

							if(polizaVO.getPlazoPago()!=null){
								cb=pdf.addLabel(cb,218,512,8, polizaVO.getPlazoPago()+" dias",false,0);}

							if(polizaVO.getFechaLimPago()!=null){																												
								cb=pdf.addLabel(cb,259,512,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}


							if(polizaVO.getMovimiento()!=null){
								cb=pdf.addLabel(cb,344,512,8,polizaVO.getMovimiento(),false,0);
							}

							if(polizaVO.getUso()!=null){
								ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      15 , 21*2 , 25*3
								if(uso!=null){
									if(uso.size()==1){
										cb=pdf.addLabel(cb,415,512,8,(String)uso.get(0),false,0);
									}
									else if(uso.size()==2){
										cb=pdf.addLabel(cb,415,515,6,(String)uso.get(0),false,0);
										cb=pdf.addLabel(cb,415,509,6,(String)uso.get(1),false,0);
									}	
									else if(uso.size()==3){
										cb=pdf.addLabel(cb,415,517,5,(String)uso.get(0),false,0);
										cb=pdf.addLabel(cb,415,512,5,(String)uso.get(1),false,0);
										cb=pdf.addLabel(cb,415,507,5,(String)uso.get(2),false,0);
									}											
								}									
							}
							if(polizaVO.getServicio()!=null){
								ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      23 , 31*2 , 37*3
								if(servicio!=null){
									if(servicio.size()==1){
										cb=pdf.addLabel(cb,515,512,8,(String)servicio.get(0),false,0);
									}
									else if(servicio.size()==2){
										cb=pdf.addLabel(cb,515,515,6,(String)servicio.get(0),false,0);
										cb=pdf.addLabel(cb,515,509,6,(String)servicio.get(1),false,0);
									}
									else if(servicio.size()==3){
										cb=pdf.addLabel(cb,515,517,5,(String)servicio.get(0),false,0);
										cb=pdf.addLabel(cb,515,512,5,(String)servicio.get(1),false,0);
										cb=pdf.addLabel(cb,515,507,5,(String)servicio.get(2),false,0);
									}
								}									
							}					
						}


						if (polizaVO.getLeyendaRCEUA()!=null){
							cb = pdf.addLabel(cb,35,260,7,polizaVO.getLeyendaRCEUA(), false, 1);
	
						}
						
						//*************Datos de Riesgos
						cb=pdf.addRectAngColor(cb,35,502,535,12);	
						cb=pdf.addRectAng(cb,35,482,535,235);

						cb=pdf.addLabel(cb,60,492,10,"COBERTURAS CONTRATADAS",true,1);
						cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
						cb=pdf.addLabel(cb,420,492,10,"DEDUCIBLE",true,1);
						cb=pdf.addLabel(cb,510,492,10,"$     PRIMAS",true,1);

						//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
						//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
						//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
						//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
						double primaAux=0;
						double primaExe=0;
						double derechoAux=0;
						double recargoAux=0;
						double subtotalAux=0;
						double impuestoAux=0;
						boolean exDM=false;
						boolean exRT=false;
						boolean agenEsp1 = false;
						boolean agenEsp2 = false;
						boolean validaAltoRiesgo = false;	
						

						for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
							int temp = (Integer)polizaVO.getAgenteEsp().get(y);
							if(temp==1)
								agenEsp1=true;
							if(temp==AGEN_ESP_OCULTA_PRIMAS)
								agenEsp2=true;
						}
						
						boolean minimos=false;

						if(polizaVO.getCoberturasArr()!=null){
							CoberturasPdfBean coberturaVO;
							
							
							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{	

								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								if( coberturaVO.getDescrCobertura().trim().equals("CONSIDERACION5035")){
									coberturaVO.setClaveCobertura("34");
									polizaVO.getCoberturasArr().remove(x);
								}
								
							}
							

							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{
								coberturaVO= new CoberturasPdfBean();
								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								if(coberturaVO.getClaveCobertura().equals("12")){
									exDM=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}else if(coberturaVO.getClaveCobertura().equals("40")){
									exRT=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}	
							}
							
							String cveServ = polizaVO.getCveServ().trim();
							

							
							
							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{	
								boolean salto=false;
								if(x==toppage+27)
								{break;}

								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								String claveCobertura = coberturaVO.getClaveCobertura();

								int deducible = getNumber(coberturaVO.getDeducible());
								int anio = getNumber(polizaVO.getVehiAnio());
								if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
									validaAltoRiesgo = true;
								}

								//ANDRES-MINIMOS
								if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
									minimos=true;
								}


								log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
								//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
								//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
								//}else{
								
								
								
								int diaEmi = 0;
								int mesEmi = 0;
								int anioEmi = 0;
								
								if (polizaVO.getFchEmi() != null && !polizaVO.getFchEmi().isEmpty()) {
									diaEmi = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
									mesEmi = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
									anioEmi = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
								}
								
								
								
								if ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 9) || (anioEmi >= 2015 && mesEmi >= 8 && diaEmi>=17)){//a partir del 17 agosot 2015 se quitan los numeros de cobertura
									if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
										cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T" ,false,1);//quitar clave cobe
									}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
										cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"Exe. Ded. x PT, DM Y RT" ,false,1);//quitar clave cobe
										salto = true;
									}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
										salto = true;
									}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
										cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estandarizado" ,false,1);//quitar clave cobe
									}else{
											int longitud= coberturaVO.getDescrCobertura().length();
											int ini=0;
											for (int ind=0;ind<longitud;ind++)
												{
													char car=coberturaVO.getDescrCobertura().charAt(ind);
													if (Character.isLetter(car)){
														ini=ind;
														break;
													}
												}
											;
											cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
																							
//											if ( ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 10) || (anioEmi >= 2015 && mesEmi >= 9 && diaEmi>=21)) && (polizaVO.getCvePlan().equals("34"))){
//												//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//												cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
//
//											}else{
//												cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//												cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
//											}

																				
									}
								}
								else{//se muestra la numeracion de coberturas
									if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
										cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
									}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
										cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
										
										salto = true;
									}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
										salto = true;
									}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
										cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
										
									}else{
										//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
											int longitud= coberturaVO.getDescrCobertura().length();
											int ini=0;
											for (int ind=0;ind<longitud;ind++)
												{
													char car=coberturaVO.getDescrCobertura().charAt(ind);
													if (Character.isLetter(car)){
														ini=ind;
														break;
													}
												}
											;
											cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
								
//											if ( polizaVO.getCvePlan().equals("34")){
//												cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la pagina www.qualitas.com.mx para imprimir certificado", false, 1);
//											}
									}
								}
								

								
								
								
								
								
								
								
								
								//}

								
								
								
								
								
								
								
								
								if(!salto){
									//ANDRES-SUMASEG
									//suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if (coberturaVO.getClaveCobertura().contains("6.2")){
											double dias=0;
											try {
												if (coberturaVO.getSumaAsegurada().contains(",")){
													int indice=0;
													indice=coberturaVO.getSumaAsegurada().indexOf(",");
													String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
													dias = Double.parseDouble(sumAseg)/500 ;
												}
												else{
													dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
												}
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
											} catch (Exception e) {
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}				
											//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
										}
										else if(coberturaVO.getClaveCobertura().equals("12")) {
											cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
											
										}
										else{
											cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}

									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,coberturaVO.getPrima(),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
									//ANDRES-SUMASEG
									//}
									//										suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
												}				
												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
											}
											else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}
									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
										}else if ("45".equals(coberturaVO.getClaveCobertura())){
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"U$S 200",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
									primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									log.debug("este es el acumulado de prima "+primaAux);
								}		
							}
						}

						boolean altoRiesgo = false;
						if (validaAltoRiesgo) {
							log.debug("Se validara el alto riesgo");
							
							Integer tarifa = getNumber(polizaVO.getTarifa());
							
							// Tarifas enero 1990 - diciembre 2029
							boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
							
							if (tarifa < 1309 && formatoTarifaNormal) {
								Integer amis = getNumber(polizaVO.getAmis());
								int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
								altoRiesgo = claveAmisAltoRiesgo == 9999;
							} else {
								List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
								if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								}
							}
						}
						if (altoRiesgo) {
							cb = pdf.addLabel(cb, 35, 290, 7, "IMPORTANTE", true, 1);
							cb = pdf.addLabel(cb, 35, 280, 7, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
							cb = pdf.addLabel(cb, 35, 270, 7, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
						}
						
						int dif = 0;
						if (minimos){
							dif = 20; // Para que no choquen leyendas
							cb=pdf.addLabel(cb,35,270,7,DEDUCIBLE_MINIMOP1,true,1);
							cb=pdf.addLabel(cb,35,263,7,DEDUCIBLE_MINIMOP2,true,1);
						}
						
						String servicio = polizaVO.getServicio().trim();
						String tarifa = polizaVO.getTarifa().trim();
						log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
						if (servicio.equals("PUBLICO")) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPub1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPub2,true,1);
						} else if (servicio.equals("PARTICULAR")
								&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPart1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPart2,true,1);
						}
						
						if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
							cb=pdf.addLabel(cb,35,250,7,polizaVO.getAsistencia(),true,1);
						} else {
							cb=pdf.addLabel(cb,35,250,7,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
							
							if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
							}
							else{
								cb=pdf.addLabel(cb,290,250,7,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
							}
						}




						
						//************Agente
						cb=pdf.addRectAngColor(cb,35,242,335,12);
						cb=pdf.addRectAng(cb,35,228,335,53);

						cb=pdf.addLabel(cb,190,232,10,"OFICINA DE SERVICIO 3",true,0);
						cb=pdf.addLabel(cb,40,218,8,"AGENTE",false,1);
						cb=pdf.addLabel(cb,40,208,8,"NUMERO",false,1);
						cb=pdf.addLabel(cb,190,208,8,"TELEFONO",false,1);
						cb=pdf.addLabel(cb,40,198,8,"OFICINA",false,1);
						cb=pdf.addLabel(cb,40,188,8,"DOMICILIO",false,1);
						cb=pdf.addLabel(cb,310,188,8,"C.P.",false,1);
						cb=pdf.addLabel(cb,40,178,8,"COL.",false,1);
						cb=pdf.addLabel(cb,188,178,8,"TEL.",false,1);
						cb=pdf.addLabel(cb,280,178,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
						//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
						//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
						if(polizaVO!= null){
							//la siguiente información va del nombre del agente a telefono nacional
							String nombreAgente="";
							if(polizaVO.getNombreAgente()!=null){
								nombreAgente=polizaVO.getNombreAgente()+" ";}
							if(polizaVO.getPateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
							if(polizaVO.getMateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
							if (polizaVO.getClavAgente().equals("52017")) {
								nombreAgente = "";
							}

							cb=pdf.addLabel(cb,100,218,8,nombreAgente,false,1);

							if(polizaVO.getClavAgente()!=null){
								cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);}

							//ANDRES-TELEFONO AGENTE
							//System.out.println("telParti"+polizaVO.getTelPartAgente());
							//System.out.println("telcomer"+polizaVO.getTelComerAgente());
							if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
								if(polizaVO.getTelComerAgente()!=null){
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelComerAgente(),false,1);
								}
								else{
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelPartAgente(),false,1);
								}

							}







							cb=pdf.addlineH(cb,31,206,343);
							if(polizaVO.getDescOficina()!=null){
								cb=pdf.addLabel(cb,100,198,8,polizaVO.getDescOficina(),false,1);}
							if(polizaVO.getPoblacionOficina()!=null){
								cb=pdf.addLabel(cb,350,198,8,polizaVO.getPoblacionOficina(),false,2);}							
							if(polizaVO.getCalleOficina()!=null){						
								cb=pdf.addLabel(cb,100,188,8,polizaVO.getCalleOficina(),false,1);}
							if(polizaVO.getCodPostalOficina()!=null){										
								cb=pdf.addLabel(cb,330,188,8,polizaVO.getCodPostalOficina(),false,1);}
							if(polizaVO.getColoniaOficina()!=null){										
								//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
								cb=pdf.addLabel(cb,60,178,8,polizaVO.getColoniaOficina(),false,1);}

							//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
							//cb=pdf.addlineH(cb,240,177,125);

							if(polizaVO.getTelOficina()!=null){
								//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
								
								if(polizaVO.getTelOficina().length() > 20){
									cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina().substring(0,19),false,1);
								}else{
									cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina(),false,1);
								}
							}

							//if(polizaVO.getTelLocal()!=null){
							//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
							//}

							if(polizaVO.getFaxOficina()!=null){
								if(polizaVO.getFaxOficina().length() > 20){
									cb=pdf.addLabel(cb,300,178,8,polizaVO.getFaxOficina().substring(0,19),false,1);
								}else{
									cb=pdf.addLabel(cb,300,178,8,polizaVO.getFaxOficina(),false,1);
								}								
							}

							//if(polizaVO.getTelNacional()!=null){
							//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
							//}
						}				

						cb=pdf.addRectAng(cb,35,175,335,25);
						cb=pdf.addLabel(cb,40,159,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
						cb=pdf.addLabel(cb,220,164,8,"  (55) 5258-2880    01-800-288-6700",true,1);
						//cb=pdf.addLabel(cb,218,153,8,"01-800-004-9600    01-800-800-2880",true,1);
						cb=pdf.addLabel(cb,218,153,8,"                                01-800-800-2880",true,1);

						//************forma de pago
						cb=pdf.addRectAng(cb,35,150,335,20);
						cb=pdf.addLabel(cb,40,137,8,"FORMA DE PAGO:",false,1);
						if(polizaVO != null){
							if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
							}
							else {
									cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);
								
								if(polizaVO.getDescrFormPago()!=null){
										cb=pdf.addLabel(cb,120,137,8,polizaVO.getDescrFormPago(),false,1);
								}
	
								//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
								cb=pdf.addLabel(cb,310,141,8,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
								if(polizaVO.getNumRecibos() != null && polizaVO.getNumRecibos() > 1){
									cb=pdf.addLabel(cb,190,141,8,"PRIMER PAGO",false,1);
									cb=pdf.addLabel(cb,190,132,8,"PAGO(S) SUBSECUENTE(S)",false,1);																						
									cb=pdf.addLabel(cb,310,132,8,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
								}else{
									cb=pdf.addLabel(cb,190,141,8,"PAGO UNICO",false,1);
								}
							}			
						}

						cb=pdf.addRectAng(cb,35,127,335,75);
						cb=pdf.addLabel(cb,43,117,7.5f,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
						cb=pdf.addLabel(cb,43,110,7.5f,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
						cb=pdf.addLabel(cb,43,103,7.5f,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
						cb=pdf.addLabel(cb,43,96,7.5f,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);

//						cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
//						cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
//						cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
//						cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
						
						cb=pdf.addLabel(cb,43,81,7.5f,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
						cb=pdf.addLabel(cb,43,74,7.5f,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
						cb=pdf.addLabel(cb,43,67,7.5f,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
						cb=pdf.addLabel(cb,43,60,7.5f,"del día",false,1);
						if (polizaVO.getcNSF()!=null){
							cb=pdf.addLabel(cb,43,53,7.5f,polizaVO.getcNSF(),false,1);
						}
						
						//CHAVA-LEYENDA ARTICULO 25
						cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
						cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);



						//************importe
						cb=pdf.addRectAngColor(cb,390,242,180,13);
						cb=pdf.addLabel(cb,395,232,10,"MONEDA",true,1);
						if(polizaVO.getDescMoneda()!=null){
							cb=pdf.addLabel(cb,560,232,10,polizaVO.getDescMoneda(),true,2);}

						cb=pdf.addRectAng(cb,390,226,180,13);										
						cb=pdf.addRectAng(cb,390,210,180,83);

						cb=pdf.addLabel(cb,395,200,8,"PRIMA NETA",false,1);
						cb=pdf.addLabel(cb,395,190,8,"TASA FINANCIAMIENTO POR PAGO",false,1);
						cb=pdf.addLabel(cb,395,180,8,"FRACCIONADO",false,1);
						cb=pdf.addLabel(cb,395,167,8,"GTOS. EXPEDICION POL.",false,1);
						cb=pdf.addLabel(cb,395,147,8,"SUBTOTAL",false,1);
						cb=pdf.addLabel(cb,395,135,8,"I.V.A.",false,1);
						cb=pdf.addLabel(cb,395,116,8,"IMPORTE TOTAL",true,1);
						cb=pdf.addLabel(cb,395,100,8,"CONDICIONES VIGENTES:",false,1);
						cb=pdf.addLabel(cb,395,88,8,"TARIFA APLICADA:",false,1);

						if(polizaVO != null){
							//la información siguiente va de prima neta a tarifa aplicada
							if(polizaVO.getPrimaNeta()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(primaAux),false,2);
								}
								else
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
							}
							/*	if(polizaVO.getRecargo()!=null){ 
									    if(Double.parseDouble(polizaVO.getRecargo())>0){
									    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
									}*/
							if(polizaVO.getRecargo()!=null){
								if(Double.parseDouble(polizaVO.getRecargo())>0){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}
									else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}else{
									if(Integer.parseInt(polizaVO.getNumIncisos())<0){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}
							}
							if(polizaVO.getDerecho()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(derechoAux),false,2);

								}
								else{
									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
							}
							
							
							if(polizaVO.getCesionComision()!=null){
								    cb=pdf.addLabel(cb,395,157,8,"DESCUENTOS",false,1);
									cb=pdf.addLabel(cb,560,157,8,polizaVO.getCesionComision(),false,2);
							}
							
							
							if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									subtotalAux = primaAux+derechoAux;				
								}
								else{
									subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

								cb=pdf.addLabel(cb,560,147,8,FormatDecimal.numDecimal(subtotalAux),false,2);
							}

							cb=pdf.addlineH(cb,460,143,115);
							if(polizaVO.getImpuesto()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
									if(polizaVO.getIva()!=null){
										impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
										cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(impuestoAux),false,2);
									}
								}
								else{
									cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
							}

							cb=pdf.addRectAng(cb,390,125,180,13);
							if(polizaVO.getPrimaTotal()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
								}
								else{
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
							}

							//***************************

							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,555,100,8,polizaVO.getDescConVig(),false,2);
							}

							cb=pdf.addRectAng(cb,390,110,180,25);
							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
								//String concatZonaId= "0000"+polizaVO.getClaveOfic();

								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}
								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApCve();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}

								//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
							}								

						}

						//****firma
						if(polizaVO != null){
							String lugar="";
							if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
							if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
							cb=pdf.addLabel(cb,490,75,8,lugar,false,0);
							//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
							if(polizaVO.getFchEmi()!=null){
								cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
							/*if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
						}


						if(polizaVO.getDirImagen()!=null){
							document=pdf.addImage(document,450,35,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
							cb=pdf.addLabel(cb,490,35,7,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
						}
						cb=pdf.addLabel(cb,490,28,7,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
						cb=pdf.addLabel(cb,490,21,7,"AUTORIZADO",false,0);	


						if(polizaVO.getDescConVig()!=null){									
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables "+polizaVO.getDescConVig(),true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}
						else{
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables",true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}


						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
							cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							//document=pdf.addImageWaterMark(document,610,660,140,-145,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/Qmembretado.jpg",cb=writer.getDirectContentUnder());
							document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
						}


					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;





				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}

	
	
	
	
	
	
	
	
	/**
	 * PdfPolizaSeguro
	 * ===============
	 * Método que crea el pdf de poliza de seguro.
	 * 
	 * @param arrPolizas .- contiene las polizas que se pintan en el pdf.
	 * @param salida .- es el tipo de salida en el que se va a generar el pdf.
	 */
	//ANDRES-MEM
	//public void creaPdf(ArrayList arrPolizas,OutputStream salida){
	public void creaPdfTur(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");

		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					document.newPage();

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();

						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addRectAngColorGreenWater(cb,35,720,535,32);
							cb=pdf.addRectAngColorPurple(cb,388,718,175,27);
							//document=pdf.addImage(document,35,725,130,50,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/logoQpoliza.jpg");
							//recibo.get(page).getDirImagen()+"logoQpoliza.jpg"
							document=pdf.addImage(document,35,727,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
						}

						cb=pdf.addLabel(cb,100,707,12,"POLIZA VEHICULOS TURISTAS",true,1);		
						cb=pdf.addLabeli(cb,120,697,11,"TOURIST VEHICLE POLICY",false,1);	
						cb=pdf.addLabel(cb,390,709,8,"POLIZA/",false,1);
						cb=pdf.addLabeli(cb,390,701,8,"POLICY",false,1);
						cb=pdf.addLabel(cb,450,709,8,"ENDOSO/",false,1);
						cb=pdf.addLabeli(cb,450,701,8,"ENDORSEMENT",false,1);
						cb=pdf.addLabel(cb,525,709,8,"INCISO/",false,1);
						cb=pdf.addLabeli(cb,525,701,8,"ITEM",false,1);
						if(polizaVO != null){
							//el orden de los datos siguientes va de poliza a inciso
							String inciso="000";
							String incisoAux;

							if(polizaVO.getNumPoliza()!=null){
								sizeNumPoliza=polizaVO.getNumPoliza().length();
								cb=pdf.addLabel(cb,390,692,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

								cb=pdf.addLabel(cb,450,692,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
							}

							if(polizaVO.getInciso()!=null){
								inciso = inciso+polizaVO.getInciso();								
								incisoAux =inciso.substring(inciso.length()-4,inciso.length());
								cb=pdf.addLabel(cb,525,692,9,incisoAux,false,1);
							}
						}

						
						cb=pdf.addLabel(cb,85,665,8,"MULTIENTRADA PLAN A/B:",false,0);
						if (polizaVO.isFlagMultientrada()){
							if(polizaVO.getMultientrada()!=null){
								if(polizaVO.getMultientrada().trim().toUpperCase().equals("A")){
									cb=pdf.addLabel(cb,290,665,8,polizaVO.getMultientrada()+": Baja California Norte y Sur, Sonora,Chihuahua,Coahuila,Nuevo León y Tamaulipas",false,0);
								}
								else if ((polizaVO.getMultientrada().trim().toUpperCase().equals("B"))){
									cb=pdf.addLabel(cb,290,665,8,polizaVO.getMultientrada()+": Resto de la República",false,0);
								}
							}
						}
						cb=pdf.addLabel(cb,498,665,8,"CLAVE DEL PLAN / RANGO:",false,0);
						if(polizaVO.getPlanRango()!=null){
							cb=pdf.addLabel(cb,558,665,8,polizaVO.getPlanRango(),false,0);
						}
						

						//**********CUERPO										
						cb=pdf.addRectAngColor(cb,35,661,535,12);
						cb=pdf.addRectAng(cb,35,648,535,43);


						
						
						
						//***********asegurado					
						cb=pdf.addLabel(cb,240,651,10,"INFORMACION DEL ASEGURADO /",true,0);			
						cb=pdf.addLabeli(cb,383,651,10,"NAMED AND ADDRESS",true,0);		
						cb=pdf.addLabel(cb,40,640,8,"NOMBRE DEL ASEGURADO/",false,1);
						cb=pdf.addLabeli(cb,150,640,8,"INSURED NAME:",false,1);
						cb=pdf.addLabel(cb,440,640,8,"RENUEVA A:",false,1);
						cb=pdf.addLabel(cb,40,630,8,"DOMICILIO/",false,1);
						cb=pdf.addLabeli(cb,84,630,8,"ADDRESS:",false,1);
						cb=pdf.addLabel(cb,40,620,8,"CIUDAD/",false,1);
						cb=pdf.addLabeli(cb,74,620,8,"CITY:",false,1);
						cb=pdf.addLabel(cb,470,620,8,"CP/",false,1);
						cb=pdf.addLabeli(cb,484,620,8,"ZIP",false,1);
						cb=pdf.addLabel(cb,430,630,8,"RFC",false,1);
						cb=pdf.addLabel(cb,40,610,8,"BENEFICIARIO PREFERENTE/",false,1);
						cb=pdf.addLabeli(cb,160,610,8,"PREFERRED BENEFICIARY",false,1);
						//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
						if(polizaVO.getPolizaAnterior()!=null){
							cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
						if(polizaVO != null){
							//el orden de los datos siguientes va del nombre del asegurado a beneficiario
							String nombre=""; 
							if(polizaVO.getNombre()!=null){
								nombre=polizaVO.getNombre()+" ";}
							if(polizaVO.getApePate()!=null){
								nombre=nombre+polizaVO.getApePate()+" ";}
							if(polizaVO.getApeMate()!=null){
								nombre=nombre+polizaVO.getApeMate()+" ";}

							if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
								nombre=nombre+" Y/O "+polizaVO.getConductor();
							}	

							cb=pdf.addLabel(cb,250,640,8,nombre,false,1);
							if(datosCliente){									
								cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	

								if(polizaVO.getCalle()!=null){
									String calle= polizaVO.getCalle();
									if(polizaVO.getExterior()!= null){
										calle += " No. EXT. " + polizaVO.getExterior();
									}
									if(polizaVO.getInterior()!= null){
										calle += " No. INT. " + polizaVO.getInterior();
									}
									//ANDRES-prueba 
									//System.out.println("colonia:::"+polizaVO.getColonia());
									if(polizaVO.getColonia()!=null){
										calle += " COL. " + polizaVO.getColonia();
									}
									cb=pdf.addLabel(cb,127,630,8,calle,false,1);}

								if(polizaVO.getCodPostal()!=null){
									cb=pdf.addLabel(cb,500,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
								if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
									cb=pdf.addLabel(cb,115,620,8,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
								if(polizaVO.getRfc()!=null){
									cb=pdf.addLabel(cb,450,630,8,polizaVO.getRfc(),false,1);}
								if(polizaVO.getBeneficiario()!=null){
									cb=pdf.addLabel(cb,40,610,8,polizaVO.getBeneficiario(),false,1);}

								for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
									LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
									if(campo.getLabel().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
									j=j+50;
									if(campo.getValue().length()>13)
										cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
									else
										cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
									j=j+50;
								}
							}
							if(polizaVO.getCveApoderado()!=null){
								cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);
								cb=pdf.addLabel(cb,100,610,8,polizaVO.getCveApoderado(),false,1);
							}							

						}				


						//*************Vehiculo
						cb=pdf.addRectAngColor(cb,35,603,535,12);
						cb=pdf.addRectAng(cb,35,589,535,55);

						cb=pdf.addLabel(cb,240,592,10,"DESCRIPCION DEL VEHICULO /",true,0);
						cb=pdf.addLabeli(cb,378,592,10,"VEHICLE DESCRIPTION",true,0);
						cb=pdf.addLabel(cb,40,565,8,"TIPO /",true,1);
						cb=pdf.addLabeli(cb,64,565,8,"TYPE",true,1);
						cb=pdf.addLabel(cb,210,565,8,"MODELO /",true,1);
						cb=pdf.addLabeli(cb,250,565,8,"YEAR",true,1);

						cb=pdf.addLabel(cb,381,565,8,"PLACAS/",true,1);
						cb=pdf.addLabeli(cb,418,565,8,"LICENCE PLATES",true,1);
						cb=pdf.addLabel(cb,40,552,8,"SERIE/",true,1);
						cb=pdf.addLabeli(cb,66,552,8,"V.I.N.",true,1);
						cb=pdf.addLabel(cb,259,552,8,"MOTOR",true,1);
						cb=pdf.addLabel(cb,390,552,8,"REPUVE",true,1);
						cb=pdf.addLabel(cb,490,552,8,"COLOR",true,1);
						if(polizaVO != null){
							//la información siguiente va de descripción del vehiculo a tipo de carga							
							if(polizaVO.getAmis()!=null){
								cb=pdf.addLabel(cb,40,580,8,polizaVO.getAmis(),true,1);}							
							if(polizaVO.getDescVehi()!=null){
								cb=pdf.addLabel(cb,70,580,8,polizaVO.getDescVehi(),false,1);}											

							if(polizaVO.getTipo()!=null){

								if(polizaVO.getTipo().length()>18){
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,90,565,8,polizaVO.getTipo().substring(0, 19),false,1);
								}else{
									//System.out.println("TIPO: "+polizaVO.getTipo());	
									cb=pdf.addLabel(cb,90,565,8,polizaVO.getTipo(),false,1); 
								}	
							}

							if(polizaVO.getVehiAnio()!=null){												
								cb=pdf.addLabel(cb,278,565,8,polizaVO.getVehiAnio(),false,1);}
							if(polizaVO.getColor()!=null){

								if(polizaVO.getColor().equals("SIN COLOR")){

									cb=pdf.addLabel(cb,525,552,8,"",false,1);		
								}else{
									cb=pdf.addLabel(cb,525,552,8,polizaVO.getColor(),false,1);
								}
							}
							//ANDRES-PASAJEROS
							if (polizaVO.getNumPasajeros()!=null){
								cb=pdf.addLabel(cb,40,542,8,polizaVO.getNumPasajeros(),false,1);
							}
							else if(polizaVO.getNumOcupantes()!=null){
								cb=pdf.addLabel(cb,315,565,8,"OCUP.",true,1);
								cb=pdf.addLabel(cb,350,565,8,polizaVO.getNumOcupantes(),false,1);
							}

							if(polizaVO.getNumPlaca()!=null){												
								cb=pdf.addLabel(cb,495,565,8,polizaVO.getNumPlaca(),false,1);}
							if(polizaVO.getNumSerie()!=null){					
								cb=pdf.addLabel(cb,100,552,8,polizaVO.getNumSerie(),false,1);}
							if(polizaVO.getNumMotor()!=null){					
								cb=pdf.addLabel(cb,300,552,8,polizaVO.getNumMotor(),false,1);}
							if(polizaVO.getRenave()!=null){					
								cb=pdf.addLabel(cb,440,552,8,polizaVO.getRenave(),false,1);}
							
							if (polizaVO.getCveServ().trim().equals("3")) {
								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
									cb=pdf.addLabel(cb,110,539,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
								if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
									cb=pdf.addLabel(cb,200,539,8,polizaVO.getTipoCarga()+" : ",true,2);}
								//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
								if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
									String descAux = "";
									String valorRemolque="";
									if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
										descAux=polizaVO.getDescCarga();
									}
									
									if(polizaVO.getDobleRemolque()!= null){
										if(polizaVO.getDobleRemolque().equals("S")){
											valorRemolque = "2° Remolque: AMPARADO";
										}else{
											valorRemolque = "2° Remolque: EXCLUIDO";
										}
									}								
									if(descAux != "" || valorRemolque != ""){
										cb=pdf.addLabel(cb,210,539,8,descAux+"  "+valorRemolque,false,1);
									}
									
								}
							}
						}


						//**************vigencia
						cb=pdf.addRectAng(cb,35,531,150,25);
						cb=pdf.addRectAng(cb,193,531,60,25);
						cb=pdf.addRectAng(cb,261,531,70,25);
						cb=pdf.addRectAng(cb,339,531,50,25);						
						cb=pdf.addRectAng(cb,397,531,172,25);

						cb=pdf.addLabel(cb,33,526,5,"VIGENCIA/",false,1);
						cb=pdf.addLabeli(cb,59,526,5,"POLICY TERM (M/D/Y)",false,1);
						
						
						if(polizaVO.getHoraEmision()!= null){
							cb=pdf.addLabel(cb,33,518,7,"DESDE LAS "+polizaVO.getHoraEmision(),false,1);
							cb=pdf.addLabel(cb,93,518,7,"HORAS  DEL  ",false,1);
						}
						else{
							cb=pdf.addLabel(cb,33,518,7,"DESDE LAS 12 HORAS P.M. DEL  ",false,1);
						}
						
						
						
						
						cb=pdf.addLabel(cb,33,510,7,"HASTA LAS 12 HORAS P.M. DEL  ",false,1);
						cb=pdf.addLabel(cb,222,524,8,"PLAZO PAGO/",false,0);
						cb=pdf.addLabeli(cb,222,516,7,"PAYMENT TERM",false,0);
						cb=pdf.addLabel(cb,274,524,8,"DURACION/",false,1);
						cb=pdf.addLabeli(cb,265,516,7,"DAYS COVERAGE",false,1);
						cb=pdf.addLabel(cb,361,524,8,"BLANKET",false,0);
						cb=pdf.addLabeli(cb,363,516,8,"SI/NO",false,0);
						cb=pdf.addLabel(cb,364,508,8,"NO",false,0);
						cb=pdf.addLabel(cb,447,524,8,"USO SERVICIO  /",false,0);
						cb=pdf.addLabeli(cb,510,524,7,"USE SERVICE",false,0);
						if(polizaVO != null){		
							//la información siguiente va desde la fecha de vigencia hasta servicio
							if(polizaVO.getFchIni()!=null){
								cb=pdf.addLabel(cb,143,518,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
							}
							if(polizaVO.getFchFin()!=null){
								cb=pdf.addLabel(cb,143,510,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

							}

							if(polizaVO.getPlazoPago()!=null){
								cb=pdf.addLabel(cb,218,506,8, polizaVO.getPlazoPago()+" dias",false,0);}

							//if(polizaVO.getFechaLimPago()!=null){																												
								//cb=pdf.addLabel(cb,259,512,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}


							/*if(polizaVO.getMovimiento()!=null){
								cb=pdf.addLabel(cb,344,512,8,polizaVO.getMovimiento(),false,0);
							}*/

							if(polizaVO.getUso()!=null){
								ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      15 , 21*2 , 25*3
								if(uso!=null){
									if(uso.size()==1){
										cb=pdf.addLabel(cb,447,512,6,(String)uso.get(0),false,0);
									}
									else if(uso.size()==2){
										cb=pdf.addLabel(cb,447,512,6,(String)uso.get(0),false,0);
										//cb=pdf.addLabel(cb,415,515,6,(String)uso.get(0),false,0);
										//cb=pdf.addLabel(cb,415,509,6,(String)uso.get(1),false,0);
									}	
									else if(uso.size()==3){
										cb=pdf.addLabel(cb,447,512,6,(String)uso.get(0),false,0);
										//cb=pdf.addLabel(cb,415,517,5,(String)uso.get(0),false,0);
										//cb=pdf.addLabel(cb,415,512,5,(String)uso.get(1),false,0);
										//cb=pdf.addLabel(cb,415,507,5,(String)uso.get(2),false,0);
									}											
								}									
							}
							if(polizaVO.getServicio()!=null){
								ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
								//caracteres      23 , 31*2 , 37*3
								if(servicio!=null){
									if(servicio.size()==1){
										cb=pdf.addLabel(cb,532,512,6,(String)servicio.get(0),false,0);
									}
									else if(servicio.size()==2){
										cb=pdf.addLabel(cb,532,512,6,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,515,6,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,509,6,(String)servicio.get(1),false,0);
									}
									else if(servicio.size()==3){
										cb=pdf.addLabel(cb,532,512,6,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,517,5,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,512,5,(String)servicio.get(1),false,0);
//										cb=pdf.addLabel(cb,515,507,5,(String)servicio.get(2),false,0);
									}
								}									
							}					
						}


						//*************Datos de Riesgos
						cb=pdf.addRectAngColor(cb,35,505,535,22);	
						cb=pdf.addRectAng(cb,35,482,535,235);

						cb=pdf.addLabel(cb,37,495,10,"COBERTURAS CONTRATADAS",true,1);
						cb=pdf.addLabeli(cb,67,485,10,"COVERAGES",true,1);
						cb=pdf.addLabel(cb,210,495,10,"LIMITES MAXIMOS DE RESPONSABILIDAD",true,1);
						cb=pdf.addLabeli(cb,228,485,10,"MAXIMUS LIMITS OF LIABILITY",true,1);
						cb=pdf.addLabel(cb,420,495,10,"DEDUCIBLE",true,1);
						cb=pdf.addLabeli(cb,420,485,10,"DEDUCTIBLE",true,1);
						cb=pdf.addLabel(cb,510,495,10,"$     PRIMAS",true,1);
						cb=pdf.addLabel(cb,510,485,10,"$ PREMIUMS",true,1);

						//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
						//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
						//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
						//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
						double primaAux=0;
						double primaExe=0;
						double derechoAux=0;
						double recargoAux=0;
						double subtotalAux=0;
						double impuestoAux=0;
						boolean exDM=false;
						boolean exRT=false;
						boolean agenEsp1 = false;
						boolean agenEsp2 = false;
						boolean validaAltoRiesgo = false;					

						for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
							int temp = (Integer)polizaVO.getAgenteEsp().get(y);
							if(temp==1)
								agenEsp1=true;
							if(temp==AGEN_ESP_OCULTA_PRIMAS)
								agenEsp2=true;
						}
						
						boolean minimos=false;
 
//						for(int x=toppage;x<(cotizacionVO.getCoberturasArr().size());x++)	
//						{
//							coberturaVO = new CoberturasPdfVO();
//							coberturaVO = (CoberturasPdfVO)cotizacionVO.getCoberturasArr().get(x);
//							if ( coberturaVO.getClaveCobertura().trim().equals("45") ){
//							coberturaVO.setFlagDeducible(true);
//								//coberturaVO.setFlagSumaAsegurada(false);
//							}
//						}
											
						if(polizaVO.getCoberturasArr()!=null){			
							CoberturasPdfBean coberturaVO;
						for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{
								coberturaVO= new CoberturasPdfBean();
								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);

							}
						}
						
						
						if(polizaVO.getCoberturasArr()!=null){
							CoberturasPdfBean coberturaVO;

							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{	
								
								coberturaVO= new CoberturasPdfBean();
								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								
								if(coberturaVO.getClaveCobertura().equals("45")||coberturaVO.getClaveCobertura().equals("9")){
									coberturaVO.setFlagDeducible(true);
								}
								
								if(coberturaVO.getClaveCobertura().equals("12")){
									exDM=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}else if(coberturaVO.getClaveCobertura().equals("40")){
									exRT=true;
									if(coberturaVO.isFlagPrima()){
										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									}
								}	
							}
							
							
							int y=toppage;
							String cveServ = polizaVO.getCveServ().trim();
							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
							{	
								boolean salto=false;
								if(x==toppage+27)
								{break;}

								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
								String claveCobertura = coberturaVO.getClaveCobertura();

								int deducible = getNumber(coberturaVO.getDeducible());
								int anio = getNumber(polizaVO.getVehiAnio());
								if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
									validaAltoRiesgo = true;
								}

								//ANDRES-MINIMOS
								if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
									minimos=true;
								}


								log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
								//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
								//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
								//}else{
//								if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
//								}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
//									salto = true;
//								}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									salto = true;
//								}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
//									cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
//								}else{
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
//								}
								//}

								if(!salto){
									//ANDRES-SUMASEG
									//suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if (coberturaVO.getClaveCobertura().contains("6.2")){
											double dias=0;
											try {
												if (coberturaVO.getSumaAsegurada().contains(",")){
													int indice=0;
													indice=coberturaVO.getSumaAsegurada().indexOf(",");
													String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
													dias = Double.parseDouble(sumAseg)/500 ;
												}
												else{
													dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
												}
												cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
											} catch (Exception e) {
												cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}				
											//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
										}
										else if(coberturaVO.getClaveCobertura().equals("12")) {
											cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
											
										}
										else if (coberturaVO.getClaveCobertura().trim().equals("45")||coberturaVO.getClaveCobertura().trim().equals("9")){
											cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,"INCLUIDO/INCLUDED",false,1);			
										}
										else if (coberturaVO.getClaveCobertura().trim().compareTo("1")==0||coberturaVO.getClaveCobertura().trim().compareTo("2")==0){
											String prueba=coberturaVO.getClaveCobertura().trim();
											cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,"VALOR COMERCIAL/MARKET VALUE",false,1);			
										}
//										else if (coberturaVO.getClaveCobertura().trim().equals("15")){
//											cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,"AMPARADA",false,1);			
//										}
										else{
											cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}

									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"5%",false,1);
										}else if ("45".equals(coberturaVO.getClaveCobertura())||"9".equals(coberturaVO.getClaveCobertura())){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"U$S 200",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(y-toppage)),9,coberturaVO.getPrima(),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
									//ANDRES-SUMASEG
									//}
									//										suma asegurada														
									if(coberturaVO.isFlagSumaAsegurada()){	
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
												}				
												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
											}
											else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,265, 467-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}
											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
										}
									}
									//deducibles
									if(coberturaVO.isFlagDeducible()){
										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"5%",false,1);
										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"5%",false,1);
										}else if ("45".equals(coberturaVO.getClaveCobertura())||"9".equals(coberturaVO.getClaveCobertura())){
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,"U$S 200",false,1);
										}else{
											cb=pdf.addLabel(cb,425, 467-(9*(y-toppage)),9,coberturaVO.getDeducible(),false,1);
										}


									}																					
									//primas
									if(coberturaVO.isFlagPrima()&& !agenEsp2){
										cb=pdf.addLabel(cb, 565, 467-(9*(y-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}else if(coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}
								}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
									primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
									log.debug("este es el acumulado de prima "+primaAux);
								}		
								
								
								
								
								
								
								
								
//								if(coberturaVO.getClaveCobertura().trim().equals("1")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Collision/Upset" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("2")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9, "Total Theft" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("3")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Civil Liability"  ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("4")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Medical Expenses for Passengers" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("6")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Transportation Expense" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("7")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Legal Expenses" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("EXT de RC GL GM y AV al Tit")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Driver's Liability Extension" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("BIS RC Daños a ocupantes")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Passengers Liability" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("15")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Road Side Assistance" ,false,1);
//							}else if(coberturaVO.getClaveCobertura().trim().equals("45")){
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" /" ,false,1);
//								y=y+1;
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
//							}
//							else{
//								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
//							}
								
								
							if (coberturaVO.getDescrCobertura().contains("/")){
								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().substring(0,coberturaVO.getDescrCobertura().indexOf("/")) ,false,1);
								y=y+1;
								cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getDescrCobertura().substring(coberturaVO.getDescrCobertura().indexOf("/"),coberturaVO.getDescrCobertura().length()),false,1);		

							}	
							else{
									if(coberturaVO.getClaveCobertura().trim().equals("1")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Collision/Upset" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("2")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9, "Total Theft" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("3")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Civil Liability"  ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("4")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Medical Expenses for Passengers" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("6")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Transportation Expense" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("7")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Legal Expenses" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("EXT R.C.,GL,GM,AV TIT.")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Driver's Liability Extension" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("BIS RC DANOS A OCUPANTES")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Passengers Liability" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("15")){
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Road Side Assistance" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("45")){
									//cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" /" ,false,1);
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
								}else if(coberturaVO.getClaveCobertura().trim().equals("")){
									//cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" /" ,false,1);
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
								}
								else{
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
								}
									//cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura() ,false,1);
							}
								//cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().substring(0,coberturaVO.getDescrCobertura().indexOf("/")) ,false,1);
								//y=y+1;
								//cb=pdf.addLabel(cb,50, 477-(9*(y-toppage))-10,9,coberturaVO.getDescrCobertura().substring(coberturaVO.getDescrCobertura().indexOf("/"),coberturaVO.getDescrCobertura().length()),false,1);		
							y++;								
							}
						}

						boolean altoRiesgo = false;
						if (validaAltoRiesgo) {
							log.debug("Se validara el alto riesgo");
							
							Integer tarifa = getNumber(polizaVO.getTarifa());
							
							// Tarifas enero 1990 - diciembre 2029
							boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
							
							if (tarifa < 1309 && formatoTarifaNormal) {
								Integer amis = getNumber(polizaVO.getAmis());
								int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
								altoRiesgo = claveAmisAltoRiesgo == 9999;
							} else {
								List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
								if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								}
							}
						}
						if (altoRiesgo) {
							cb = pdf.addLabel(cb, 35, 290, 7, "IMPORTANTE", true, 1);
							cb = pdf.addLabel(cb, 35, 280, 7, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
							cb = pdf.addLabel(cb, 35, 270, 7, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
						}
						
						int dif = 0;
						if (minimos){
							dif = 20; // Para que no choquen leyendas
							cb=pdf.addLabel(cb,35,270,7,DEDUCIBLE_MINIMOP1,true,1);
							cb=pdf.addLabel(cb,35,263,7,DEDUCIBLE_MINIMOP2,true,1);
						}
						
						String servicio = polizaVO.getServicio().trim();
						String tarifa = polizaVO.getTarifa().trim();
						log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
						if (servicio.equals("PUBLICO")) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPub1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPub2,true,1);
						} else if (servicio.equals("PARTICULAR")
								&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPart1,true,1);
							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPart2,true,1);
						}
						
						
					
						if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
							cb=pdf.addLabel(cb,35,280,7,"Deductible: Minimum $ 200 U.S. Cy Material Damage and $ 400 U.S. Cy Total Theft",true,1);
							cb=pdf.addLabel(cb,35,270,7,polizaVO.getAsistencia(),true,1);
							cb=pdf.addLabel(cb,35,260,7,"For Road side assistance within Mexico City dial 5480 3839 for service outside Mexico City dial 01 800 010 5300",true,1);
						} else {
							
							
							
							cb=pdf.addLabel(cb,35,280,7,"Deductible: Minimum $ 200 U.S. Cy Material Damage and $ 400 U.S. Cy Total Theft",true,1);
							
							if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,270,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
								cb=pdf.addLabel(cb,290,270,7,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
							}
							else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
								cb=pdf.addLabel(cb,290,270,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
							}
							else{
								cb=pdf.addLabel(cb,290,270,7,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
							}
							
							
							cb=pdf.addLabel(cb,35,260,7,"For Road side assistance within Mexico City dial 5480 3839 for service outside Mexico City dial 01 800 010 5300",true,1);	
							
						}


						//************Agente
						cb=pdf.addRectAngColor(cb,35,242,335,12);
						cb=pdf.addRectAng(cb,35,228,335,53);

						cb=pdf.addLabel(cb,140,232,10,"OFICINA DE SERVICIO/",true,0);
						cb=pdf.addLabeli(cb,237,232,10,"SERVICE OFFICE",true,0);
						cb=pdf.addLabel(cb,40,218,8,"AGENTE",false,1);
						cb=pdf.addLabel(cb,40,208,8,"AGENTE No/",false,1);
						cb=pdf.addLabel(cb,88,208,8,"CODE",false,1);
						cb=pdf.addLabel(cb,190,208,8,"TELEFONO",false,1);
						cb=pdf.addLabel(cb,40,198,8,"OFICINA",false,1);
						cb=pdf.addLabel(cb,40,188,8,"DOMICILIO",false,1);
						cb=pdf.addLabel(cb,290,188,8,"C.P./",false,1);
						cb=pdf.addLabeli(cb,310,188,8,"ZIP",false,1);
						cb=pdf.addLabel(cb,40,178,8,"COL.",false,1);
						cb=pdf.addLabel(cb,188,178,8,"TEL.",false,1);
						cb=pdf.addLabel(cb,274,178,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
						//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
						//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
						//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
						if(polizaVO!= null){
							//la siguiente información va del nombre del agente a telefono nacional
							String nombreAgente="";
							if(polizaVO.getNombreAgente()!=null){
								nombreAgente=polizaVO.getNombreAgente()+" ";}
							if(polizaVO.getPateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
							if(polizaVO.getMateAgente()!=null){
								nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
							if (polizaVO.getClavAgente().equals("52017")) {
								nombreAgente = "";
							}

							cb=pdf.addLabel(cb,100,218,8,nombreAgente,false,1);

							if(polizaVO.getClavAgente()!=null){
								cb=pdf.addLabel(cb,150,208,8,polizaVO.getClavAgente(),false,1);}

							//ANDRES-TELEFONO AGENTE
							//System.out.println("telParti"+polizaVO.getTelPartAgente());
							//System.out.println("telcomer"+polizaVO.getTelComerAgente());
							if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
								if(polizaVO.getTelComerAgente()!=null){
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelComerAgente(),false,1);
								}
								else{
									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelPartAgente(),false,1);
								}

							}







							cb=pdf.addlineH(cb,31,206,343);
							if(polizaVO.getDescOficina()!=null){
								cb=pdf.addLabel(cb,100,198,8,polizaVO.getDescOficina(),false,1);}
							if(polizaVO.getPoblacionOficina()!=null){
								cb=pdf.addLabel(cb,350,198,8,polizaVO.getPoblacionOficina(),false,2);}							
							if(polizaVO.getCalleOficina()!=null){						
								cb=pdf.addLabel(cb,100,188,8,polizaVO.getCalleOficina(),false,1);}
							if(polizaVO.getCodPostalOficina()!=null){										
								cb=pdf.addLabel(cb,348,188,8,polizaVO.getCodPostalOficina(),false,1);}
							if(polizaVO.getColoniaOficina()!=null){										
								//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
								cb=pdf.addLabel(cb,60,178,8,polizaVO.getColoniaOficina(),false,1);}

							//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
							//cb=pdf.addlineH(cb,240,177,125);

							if(polizaVO.getTelOficina()!=null){
								//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
								cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina(),false,1);}

							//if(polizaVO.getTelLocal()!=null){
							//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
							//}

							if(polizaVO.getFaxOficina()!=null){					
								cb=pdf.addLabel(cb,293,178,8,polizaVO.getFaxOficina(),false,1);}

							//if(polizaVO.getTelNacional()!=null){
							//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
							//}
							
						}				

						cb=pdf.addRectAng(cb,35,175,335,25);
						cb=pdf.addLabel(cb,40,159,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
						cb=pdf.addLabel(cb,220,164,8,"  (55) 5258-2880    01-800-288-6700",true,1);
						//cb=pdf.addLabel(cb,218,153,8,"01-800-004-9600    01-800-800-2880",true,1);
						cb=pdf.addLabel(cb,218,153,8,"                                01-800-800-2880",true,1);

						//************forma de pago
						cb=pdf.addRectAng(cb,35,150,335,20);
						cb=pdf.addLabel(cb,40,137,8,"FORMA DE PAGO/",false,1);
						cb=pdf.addLabeli(cb,112,137,8,"TYPE OF PAYMENT:",false,1);
						if(polizaVO != null){
							if(polizaVO.getDescrFormPago()!=null){
								cb=pdf.addLabel(cb,193,137,8,polizaVO.getDescrFormPago(),false,1);
							}

							//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
							cb=pdf.addLabel(cb,310,141,8,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
							if(polizaVO.getNumRecibos()!=null && polizaVO.getNumRecibos() > 1){
								cb=pdf.addLabel(cb,250,141,8,"PRIMER PAGO",false,1);
								cb=pdf.addLabel(cb,250,132,8,"PAGO(S) SUBSECUENTE(S)",false,1);																						
								cb=pdf.addLabel(cb,360,132,8,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
							}else{
								cb=pdf.addLabel(cb,250,141,8,"PAGO UNICO",false,1);
							}
						}

						//cb=pdf.addRectAng(cb,35,127,335,75);
						cb=pdf.addLabel(cb,43,117,7.5f,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
						cb=pdf.addLabel(cb,43,110,7.5f,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
						cb=pdf.addLabel(cb,43,103,7.5f,"contra  perdidas o  daños  causados en la pública Mexicana,por cualquiera de los riesgos",false,1);
						cb=pdf.addLabel(cb,43,96,7.5f,"que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañia",false,1);
						cb=pdf.addLabel(cb,43,89,7.5f,"firma la presente",false,1);

						cb=pdf.addLabel(cb,43,80,7.5f,"La documentación contractual y la nota técnica que integran este producto,están registrados",false,1);
						cb=pdf.addLabel(cb,43,73,7.5f,"ante la Comision Nacional de Seguros y Finanzas, de conformidad con lo dispuesto por los",false,1);
						cb=pdf.addLabel(cb,43,66,7.5f,"artículos 36, 36-A, 36-B y 36-D de la Ley General de Instituciones y Sociedades Mutualistas",false,1);
						cb=pdf.addLabel(cb,43,59,7.5f,"de Seguros, bajo el(los) registro(s) número: ",false,1);
						cb=pdf.addLabel(cb,103,52,7.5f,"CNSF-S0046-0365-2014 de fecha 12 de junio de 2014.",false,1);
						cb=pdf.addLabel(cb,43,45,7.5f,"Qualitas Cia de Seguros SA de CV, issues this policy, valid in Mexican Republic, in favor",false,1);
						cb=pdf.addLabel(cb,43,38,7.5f,"of the above mentioned insured, in acordance with the terms, conditions, herein",false,1);

						//CHAVA-LEYENDA ARTICULO 25
						//cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
						//cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);



						//************importe
						cb=pdf.addRectAngColor(cb,390,242,180,13);
						cb=pdf.addLabel(cb,390,232,10,"MONEDA/",true,1);
						cb=pdf.addLabeli(cb,437,232,9,"CURRENCY",true,1);
						if(polizaVO.getDescMoneda()!=null){
							cb=pdf.addLabel(cb,560,232,10,polizaVO.getDescMoneda(),true,2);}

						cb=pdf.addRectAng(cb,390,226,180,13);										
						cb=pdf.addRectAng(cb,390,210,180,83);

						cb=pdf.addLabel(cb,395,200,8,"PRIMA NETA/",false,1);
						cb=pdf.addLabeli(cb,449,200,8,"NET PREMIUM",false,1);
						
						cb=pdf.addLabel(cb,395,190,8,"TASA F.P.F/",false,1);
						cb=pdf.addLabeli(cb,442,190,8,"RATE FINANCING",false,1);
						cb=pdf.addLabeli(cb,395,180,8,"INSTALLMENT PAYMENT",false,1);
						cb=pdf.addLabel(cb,395,170,8,"GTOS. EXPEDICION POLIZA/",false,1);
						cb=pdf.addLabeli(cb,395,160,8,"POLICY FREE",false,1);
						cb=pdf.addLabel(cb,395,146,8,"SUBTOTAL/",false,1);
						cb=pdf.addLabeli(cb,440,146,8,"AMOUNT",false,1);
						cb=pdf.addLabel(cb,395,134,8,"I.V.A./",false,1);
						cb=pdf.addLabeli(cb,420,134,8,"TAX",false,1);
						cb=pdf.addLabel(cb,390,116,8,"IMPORTE TOTAL/",true,1);
						cb=pdf.addLabeli(cb,459,116,7,"TOTAL PREMIUM",true,1);
						cb=pdf.addLabel(cb,395,100,8,"CONDICIONES VIGENTES/",false,1);
						cb=pdf.addLabeli(cb,395,90,8,"CONDITIONS IN FORCE",false,1);
						cb=pdf.addLabel(cb,395,80,8,"TARIFA",false,1);

						if(polizaVO != null){
							//la información siguiente va de prima neta a tarifa aplicada
							if(polizaVO.getPrimaNeta()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(primaAux),false,2);
								}
								else
									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
							}
							/*	if(polizaVO.getRecargo()!=null){ 
									    if(Double.parseDouble(polizaVO.getRecargo())>0){
									    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
									}*/
							if(polizaVO.getRecargo()!=null){
								if(Double.parseDouble(polizaVO.getRecargo())>0){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}
									else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}else{
									if(Integer.parseInt(polizaVO.getNumIncisos())<0){
										recargoAux=Double.parseDouble(polizaVO.getRecargo());
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
									}else{
										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
									}
								}
							}
							if(polizaVO.getDerecho()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
									cb=pdf.addLabel(cb,560,170,8,FormatDecimal.numDecimal(derechoAux),false,2);

								}
								else{
									cb=pdf.addLabel(cb,560,170,8,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
							}
							if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									subtotalAux = primaAux+derechoAux;				
								}
								else{
									subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

								cb=pdf.addLabel(cb,560,147,8,FormatDecimal.numDecimal(subtotalAux),false,2);
							}

							cb=pdf.addlineH(cb,460,143,115);
							if(polizaVO.getImpuesto()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
									if(polizaVO.getIva()!=null){
										impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
										cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(impuestoAux),false,2);
									}
								}
								else{
									cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
							}

							cb=pdf.addRectAng(cb,390,125,180,13);
							if(polizaVO.getPrimaTotal()!=null){
								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
								}
								else{
									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
							}

							//***************************

							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,555,100,8,polizaVO.getDescConVig(),false,2);
							}

							cb=pdf.addRectAng(cb,390,110,180,35);
							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
								//String concatZonaId= "0000"+polizaVO.getClaveOfic();

								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,78,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}
								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){

									String concatZonaId= "0000"+polizaVO.getTarifApCve();
									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
									cb=pdf.addLabel(cb,555,78,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}

								//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
							}								

						}

						//****firma
						if(polizaVO != null){
							String lugar="";
							if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
							if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
							cb=pdf.addLabel(cb,490,65,8,lugar,false,0);
							//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
							if(polizaVO.getFchEmi()!=null){
								cb=pdf.addLabel(cb,490,55,8,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
							/*if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
						}


						if(polizaVO.getDirImagen()!=null){
							document=pdf.addImage(document,450,25,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
							cb=pdf.addLabel(cb,490,25,7,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
						}
						cb=pdf.addLabel(cb,490,18,7,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
						cb=pdf.addLabel(cb,490,11,7,"AUTORIZADO",false,0);	


						/*if(polizaVO.getDescConVig()!=null){									
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables "+polizaVO.getDescConVig(),true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}
						else{
							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables",true,0);
							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
						}*/


						//ANDRES-MEM
						if (membretado==null||membretado.equals("S")){
							cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
							cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							//document=pdf.addImageWaterMark(document,610,660,140,-145,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/Qmembretado.jpg",cb=writer.getDirectContentUnder());
							document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
						}


					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;





				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}
	
	
	public void creaPdfTurActual(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					//document.newPage();//5-ene-2016

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();
						
						
						if(page==0){
														
							document.newPage();
								
							cb=pdf.addLabel(cb,100,707,12,"POLIZA VEHICULOS TURISTAS",true,1);		
							cb=pdf.addLabeli(cb,120,697,11,"TOURIST VEHICLE POLICY",false,1);	
							cb=pdf.addLabel(cb,390,709,8,"POLIZA/",false,1);
							cb=pdf.addLabeli(cb,390,701,8,"POLICY",false,1);
							cb=pdf.addLabel(cb,450,709,8,"ENDOSO/",false,1);
							cb=pdf.addLabeli(cb,450,701,8,"ENDORSEMENT",false,1);
							cb=pdf.addLabel(cb,525,709,8,"INCISO/",false,1);
							cb=pdf.addLabeli(cb,525,701,8,"ITEM",false,1);							
							
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,390,692,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,450,692,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,692,9,incisoAux,false,1);
								}
							}

							//**********CUERPO		
							cb=pdf.addRectAngColor(cb,23,684,562,12);
							cb=pdf.addRectAng(cb,23,671,562,73);

							//***********asegurado					
							cb=pdf.addLabel(cb,290,674,10,"INFORMACIÓN DEL ASEGURADO / NAME AND ADDRESS",true,0);		
							cb=pdf.addLabel(cb,27,612,10,"Vigencia/Policy Term(M/D/Y):Desde/From las 12:00 P.M. de:",false,1);
							cb=pdf.addLabel(cb,380,612,10,"Hasta/To las 12:00 P.M. de:",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,27,652,10,nombre,false,1);
			
									//la información siguiente va de descripción del vehiculo a tipo de carga							
									if(polizaVO.getAmis()!=null){
										cb=pdf.addLabel(cb,27,632,10,polizaVO.getAmis(),false,1);}							
									if(polizaVO.getDescVehi()!=null){
										cb=pdf.addLabel(cb,57,632,10,polizaVO.getDescVehi(),false,1);}	
									
									if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,300,612,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
									}
									if(polizaVO.getFchFin()!=null){
										cb=pdf.addLabel(cb,508,612,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

									}						

							}				

							cb=pdf.addRectAng(cb,23,590,562,408);
							cb=pdf.addRectAngColor(cb,23,590,562,13);

							cb=pdf.addLabel(cb,295,580,10,"INFORMACIÒN IMPORTANTE / IMPORTANT INFORMATION Bernabé",true,0);
							cb=pdf.addLabel(cb,27,563,10,"Estimado Asegurado con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro de",false,1);
							cb=pdf.addLabel(cb,27,550,10,"automóvil que acaba de adquirir, Quálitas Compañia de Seguros, lo invita a que lea sus Condiciones Generales mismas que",false,1);
							cb=pdf.addLabel(cb,27,538,10,"se adjuntan a esta póliza, o bien, puede usted ingresar a nuestra página Web.",false,1);
							cb=pdf.addLabel(cb,27,526,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
							cb=pdf.addLabel(cb,27,514,10,"",false,1);
							cb=pdf.addLabel(cb,27,500,10,"Usted puede consultar el folleto que contiene los Derechos de los Asegurados, Contratantes y Beneficiarios en nuestra",false,1);
							cb=pdf.addLabel(cb,27,488,10,"página de internet (www.qualitas.com.mx).",false,1);
							cb=pdf.addLabel(cb,27,476,10,"",false,1);
							cb=pdf.addLabel(cb,27,464,10,"Artículo 25 de la Ley sobre el Contrato de Seguro. Si el contenido de la póliza o sus modificaciones no concordaren",false,1);
							cb=pdf.addLabel(cb,27,454,10,"con la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan al",false,1);
							cb=pdf.addLabel(cb,27,442,10,"día en que reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de",false,1);
							cb=pdf.addLabel(cb,27,430,10,"sus modificaciones.",false,1);
							cb=pdf.addLabel(cb,27,418,10,"",false,1);
							cb=pdf.addLabel(cb,27,406,10,"Nuestra Unidad Especializada de Atención a Usuario (UNE) con domicilio en: Boulevard Picacho Ajusco 236, Colonia",false,1);
							cb=pdf.addLabel(cb,27,394,10,"Jardines de la Montaña, Delegación Tlalpan, Ciudad de México, C.P. 14210, horario de atención de Lunes a Viernes",false,1);
							cb=pdf.addLabel(cb,27,382,10,"de 9:00 a.m. a 6:00 p.m., teléfono 01 (55) 5002 5500, correo electrónico: uauf@quialitas.com.mx",false,1);
							cb=pdf.addLabel(cb,27,370,10,"",false,1);
							cb=pdf.addLabel(cb,27,358,10,"Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
							cb=pdf.addLabel(cb,27,346,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono 01 (55) 5340 0999 ",false,1);
							cb=pdf.addLabel(cb,27,334,10,"y 01 (800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría@condusef.gob.mx",false,1);
							cb=pdf.addLabel(cb,27,322,10,"",false,1);
							cb=pdf.addLabel(cb,27,310,10,"Quálitas Compañía de Seguros, S.A. de C.V. (en lo sucesivo La Compañía), asegura de acuerdo a las Condiciones",false,1);
							cb=pdf.addLabel(cb,27,298,10,"Generales y Especiales de esta Póliza el vehículo contra pérdidas o daños causados por cualquiera de los riesgos",false,1);
							cb=pdf.addLabel(cb,27,286,10,"que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							cb=pdf.addLabel(cb,27,274,10,"",false,1);
							cb=pdf.addLabel(cb,27,262,10,"Póliza de Seguro Registrada en el RECAS con el número CONDUSEF-000055-02",false,1);
							cb=pdf.addLabel(cb,27,250,10,"",false,1);
							cb=pdf.addLabel(cb,27,238,10,"Consulta de Significado de Abreviaturas en nuestra página Web: www.qualitas.com.mx",false,1);
							
							cb=pdf.addRectAng(cb,23,182,562,93);//seccion 5
							cb=pdf.addRectAngColor(cb,23,182,562,12);//seccion 5
							cb=pdf.addLabel(cb,295,172,10,"OFICINA DE ATENCIÓN DE SERVICIO / SERVICE OFFICE",true,0);
							cb=pdf.addLabel(cb,27,158,10,"Oficina:",false,1);
							cb=pdf.addLabel(cb,27,147,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,400,147,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,27,136,10,"Colonia:",false,1);
							cb=pdf.addLabel(cb,27,125,10,"Télefono:",false,1);
							cb=pdf.addLabel(cb,400,125,10,"Fax:",false,1);
							cb=pdf.addLabel(cb,27,105,10,"Canal de Venta",false,1);
							cb=pdf.addLabel(cb,400,105,10,"Teléfono:",false,1);
							cb=pdf.addLabel(cb,27,94,10,"Agente:",false,1);
							if(polizaVO!= null){
								//la siguiente información va del nombre del agente a telefono nacional
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								cb=pdf.addLabel(cb,130,94,10,nombreAgente,false,1);
								if(polizaVO.getDescOficina()!=null){
									cb=pdf.addLabel(cb,77,158,10,polizaVO.getDescOficina(),false,1);}							
								if(polizaVO.getCalleOficina()!=null){						
									cb=pdf.addLabel(cb,77,147,10,polizaVO.getCalleOficina(),false,1);}
								if(polizaVO.getCodPostalOficina()!=null){										
									cb=pdf.addLabel(cb,440,147,10,polizaVO.getCodPostalOficina(),false,1);}
								if(polizaVO.getColoniaOficina()!=null){										
									cb=pdf.addLabel(cb,77,136,10,polizaVO.getColoniaOficina(),false,1);}
								if(polizaVO.getTelOficina()!=null){
									cb=pdf.addLabel(cb,77,125,10,polizaVO.getTelOficina(),false,1);}
								if(polizaVO.getFaxOficina()!=null){					
									cb=pdf.addLabel(cb,440,125,10,polizaVO.getFaxOficina(),false,1);}
								cb=pdf.addlineH(cb,20,115,562);
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,67,94,10,polizaVO.getClavAgente(),false,1);}
							}
							cb=pdf.addRectAng(cb,23,88,562,65);//seccion 7
							cb=pdf.addLabel(cb,27,71,12,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,",false,1);
							cb=pdf.addLabel(cb,27,57,12,"la documentación contractual y la nota técnica que integran este producto de seguro, quedaron",false,1);
							cb=pdf.addLabel(cb,27,43,12,"registrados ante la Comisión Nacional de Seguros y Fianzas a partir del día 18 de Diciembre de 2015",false,1);
							cb=pdf.addLabel(cb,27,29,12,"con el No. CNSF-S0046-0486-2015",false,1);

							
					for (int l=0;l<=1;l++){								
							document.newPage();		
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,35,720,535,32);
								cb=pdf.addRectAngColorPurple(cb,388,718,175,27);
								document=pdf.addImage(document,35,727,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}

							cb=pdf.addLabel(cb,100,707,12,"POLIZA VEHICULOS TURISTAS",true,1);		
							cb=pdf.addLabeli(cb,120,697,11,"TOURIST VEHICLE POLICY",false,1);	
							cb=pdf.addLabel(cb,390,709,8,"POLIZA/",false,1);
							cb=pdf.addLabeli(cb,390,701,8,"POLICY",false,1);
							cb=pdf.addLabel(cb,450,709,8,"ENDOSO/",false,1);
							cb=pdf.addLabeli(cb,450,701,8,"ENDORSEMENT",false,1);
							cb=pdf.addLabel(cb,525,709,8,"INCISO/",false,1);
							cb=pdf.addLabeli(cb,525,701,8,"ITEM",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,390,692,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,450,692,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,692,9,incisoAux,false,1);
								}
							}
							//**********CUERPO										
							cb=pdf.addRectAngColor(cb,35,661,535,12);
							cb=pdf.addRectAng(cb,35,648,535,43);
							//***********asegurado					
							cb=pdf.addLabel(cb,240,651,10,"INFORMACION DEL ASEGURADO /",true,0);			
							cb=pdf.addLabeli(cb,383,651,10,"NAMED AND ADDRESS",true,0);		
							cb=pdf.addLabel(cb,40,640,8,"NOMBRE DEL ASEGURADO/",true,1);
							cb=pdf.addLabeli(cb,150,640,8,"INSURED NAME:",true,1);
							cb=pdf.addLabel(cb,40,630,8,"DOMICILIO/",true,1);
							cb=pdf.addLabeli(cb,84,630,8,"ADDRESS:",true,1);
							cb=pdf.addLabel(cb,430,630,8,"RFC:",true,1);
							cb=pdf.addLabel(cb,40,620,8,"CP/",true,1);
							cb=pdf.addLabeli(cb,53,620,8,"ZIP:",true,1);
							cb=pdf.addLabel(cb,140,620,8,"MUNICIPIO/",true,1);
							cb=pdf.addLabeli(cb,184,620,8,"TOWN:",true,1);
							cb=pdf.addLabel(cb,325,620,8,"ESTADO/",true,1);
							cb=pdf.addLabeli(cb,360,620,8,"STATE:",true,1);
							
							//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
							if(polizaVO.getPolizaAnterior()!=null){
								cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,250,640,8,nombre,false,1);
								if(datosCliente){									
									cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	

									if(polizaVO.getCalle()!=null){
										String calle= polizaVO.getCalle();
										if(polizaVO.getExterior()!= null){
											calle += " No. EXT. " + polizaVO.getExterior();
										}
										if(polizaVO.getInterior()!= null){
											calle += " No. INT. " + polizaVO.getInterior();
										}
										if(polizaVO.getColonia()!=null){
											calle += " COL. " + polizaVO.getColonia();
										}
										cb=pdf.addLabel(cb,127,630,8,calle,false,1);}

									if(polizaVO.getCodPostal()!=null){
										cb=pdf.addLabel(cb,70,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
									if(polizaVO.getMunicipio()!=null){
										cb=pdf.addLabel(cb,215,620,8,polizaVO.getMunicipio(),false,1);}
									if(polizaVO.getEstado()!=null){
										cb=pdf.addLabel(cb,390,620,8,polizaVO.getEstado(),false,1);}
									if(polizaVO.getRfc()!=null){
										cb=pdf.addLabel(cb,450,630,8,polizaVO.getRfc(),false,1);}
								}						

							}				


							//*************Vehiculo
							cb=pdf.addRectAngColor(cb,35,603,535,12);
							cb=pdf.addRectAng(cb,35,589,535,55);

							cb=pdf.addLabel(cb,240,592,10,"DESCRIPCION DEL VEHICULO /",true,0);
							cb=pdf.addLabeli(cb,378,592,10,"VEHICLE DESCRIPTION",true,0);
							cb=pdf.addLabeli(cb,40,580,8,"CLAVE, MARCA, DESCRIPCIÓN:",true,1);
							cb=pdf.addLabel(cb,40,565,8,"TIPO /",true,1);
							cb=pdf.addLabeli(cb,64,565,8,"TYPE:",true,1);
							cb=pdf.addLabeli(cb,92,565,8,"TURISTA/TOURIST",false,1);
							cb=pdf.addLabel(cb,250,565,8,"MODELO /",true,1);
							cb=pdf.addLabeli(cb,290,565,8,"YEAR:",true,1);
							
							cb=pdf.addLabel(cb,40,552,8,"SERIE/",true,1);
							cb=pdf.addLabeli(cb,66,552,8,"V.I.N.:",true,1);
							cb=pdf.addLabel(cb,40,539,8,"PLACAS/",true,1);
							cb=pdf.addLabeli(cb,77,539,8,"LICENCE PLATES:",true,1);
							cb=pdf.addLabel(cb,250,552,8,"MOTOR:",true,1);
							cb=pdf.addLabel(cb,250,539,8,"TIPO /",true,1);
							cb=pdf.addLabeli(cb,274,539,8,"TYPE:",true,1);
							cb=pdf.addLabel(cb,420,565,8,"OCUPANTES/",true,1);
							cb=pdf.addLabeli(cb,475,565,8,"PASS:",true,1);
							cb=pdf.addLabel(cb,420,552,8,"COLOR:",true,1);
							cb=pdf.addLabeli(cb,420,539,8,"BLANKET(YES/NO): NO",true,1);
							if(polizaVO != null){
								//la información siguiente va de descripción del vehiculo a tipo de carga							
								if(polizaVO.getAmis()!=null){
									cb=pdf.addLabel(cb,140,580,8,polizaVO.getAmis(),true,1);}							
								if(polizaVO.getDescVehi()!=null){
									cb=pdf.addLabel(cb,170,580,8,polizaVO.getDescVehi(),false,1);}											

								if(polizaVO.getTipo()!=null){

									if(polizaVO.getTipo().length()>18){	
										cb=pdf.addLabel(cb,300,539,8,polizaVO.getTipo().substring(0, 19),false,1);
									}else{
										cb=pdf.addLabel(cb,300,539,8,polizaVO.getTipo(),false,1); 
									}	
								}

								if(polizaVO.getVehiAnio()!=null){												
									cb=pdf.addLabel(cb,320,565,8,polizaVO.getVehiAnio(),false,1);}
								if(polizaVO.getColor()!=null){

									if(polizaVO.getColor().equals("SIN COLOR")){

										cb=pdf.addLabel(cb,450,552,8,"",false,1);		
									}else{
										cb=pdf.addLabel(cb,450,552,8,polizaVO.getColor(),false,1);
									}
								}
								if (polizaVO.getNumPasajeros()!=null){
									cb=pdf.addLabel(cb,505,565,8,polizaVO.getNumPasajeros(),false,1);
								}
								else if(polizaVO.getNumOcupantes()!=null){
									//cb=pdf.addLabel(cb,500,565,8,"OCUP.",true,1);
									cb=pdf.addLabel(cb,505,565,8,polizaVO.getNumOcupantes(),false,1);
								}

								if(polizaVO.getNumPlaca()!=null){												
									cb=pdf.addLabel(cb,158,539,8,polizaVO.getNumPlaca(),false,1);}
								if(polizaVO.getNumSerie()!=null){					
									cb=pdf.addLabel(cb,95,552,8,polizaVO.getNumSerie(),false,1);}
								if(polizaVO.getNumMotor()!=null){					
									cb=pdf.addLabel(cb,275,552,8,polizaVO.getNumMotor(),false,1);}
					
							}
							
							cb=pdf.addRectAng(cb,35,531,180,34);
							cb=pdf.addRectAng(cb,224,531,137,34);
							cb=pdf.addRectAng(cb,370,531,200,34);
							
							cb=pdf.addLabel(cb,60,522,8,"VIGENCIA/",true,1);
							cb=pdf.addLabeli(cb,100,522,8,"POLICY TERM (M/D/Y)",true,1);
							
							if(polizaVO.getHoraEmision()!= null){
								cb=pdf.addLabel(cb,40,511,7,"DESDE/ FROM LAS "+polizaVO.getHoraEmision(),false,1);
								cb=pdf.addLabel(cb,40,500,7,"HASTA/ TO LAS",false,1);
							}
							else{
								cb=pdf.addLabel(cb,40,511,7,"DESDE/FROM LAS 12 HORAS P.M. DEL  ",false,1);
							}
							cb=pdf.addLabel(cb,250,522,8,"PLAZO PAGO",true,1);
							cb=pdf.addLabeli(cb,250,512,8,"PAYMENT TERM",true,1);
							cb=pdf.addLabel(cb,265,502,8,"DIAS/DAYS",false,1);
							cb=pdf.addLabel(cb,380,522,8,"USO/",true,1);
							cb=pdf.addLabeli(cb,400,522,8,"USE:",true,1);
							cb=pdf.addLabel(cb,380,512,8,"SERVICIO/",true,1);
							cb=pdf.addLabeli(cb,420,512,8,"SERVICE:",true,1);
							cb=pdf.addLabel(cb,380,502,8,"DURACION/",true,1);
							cb=pdf.addLabeli(cb,427,502,8,"DAYS COVERAGE:",true,1);
							
							//*************Datos de Riesgos
							cb=pdf.addRectAngColor(cb,35,495,535,22);	
							cb=pdf.addRectAng(cb,35,472,535,235);

							cb=pdf.addLabel(cb,40,485,10,"COBERTURAS CONTRATADAS",true,1);
							cb=pdf.addLabeli(cb,67,475,10,"COVERAGES",true,1);
							cb=pdf.addLabel(cb,225,485,10,"VALOR CONVENIDO",true,1);
							cb=pdf.addLabeli(cb,225,475,10,"AGREED VALUE",true,1);
							cb=pdf.addLabel(cb,394,485,10,"% DEDUCIBLES",true,1);
							cb=pdf.addLabeli(cb,394,475,10,"% DEDUCTIBLE",true,1);
							cb=pdf.addLabel(cb,497,485,10,"US$PRIMAS",true,1);
							cb=pdf.addLabel(cb,497,475,10,"US$PREMIUMS",true,1);
							
							//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
							//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
							//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
							//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
							double primaAux=0;
							double primaExe=0;
							double derechoAux=0;
							double recargoAux=0;
							double subtotalAux=0;
							double impuestoAux=0;
							boolean exDM=false;
							boolean exRT=false;
							boolean agenEsp1 = false;
							boolean agenEsp2 = false;
							boolean validaAltoRiesgo = false;					

							for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
								int temp = (Integer)polizaVO.getAgenteEsp().get(y);
								if(temp==1)
									agenEsp1=true;
								if(temp==AGEN_ESP_OCULTA_PRIMAS)
									agenEsp2=true;
							}
							
							boolean minimos=false;
												
							if(polizaVO.getCoberturasArr()!=null){			
								CoberturasPdfBean coberturaVO;
							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);

								}
							}
							
							
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;

								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{	
									
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									
									if(coberturaVO.getClaveCobertura().equals("45")||coberturaVO.getClaveCobertura().equals("9")){
										coberturaVO.setFlagDeducible(true);
									}
									
									if(coberturaVO.getClaveCobertura().equals("12")){
										exDM=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}else if(coberturaVO.getClaveCobertura().equals("40")){
										exRT=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}	
								}
								
								
								int y=toppage;
								String cveServ = polizaVO.getCveServ().trim();
								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{	
									boolean salto=false;
									if(x==toppage+27)
									{break;}
									
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									String claveCobertura = coberturaVO.getClaveCobertura();

									int deducible = getNumber(coberturaVO.getDeducible());
									int anio = getNumber(polizaVO.getVehiAnio());
									if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
										validaAltoRiesgo = true;
									}

									if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
										minimos=true;
									}


									if(!salto){													
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,218, 463-(9*(y-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,218, 463-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
												}				
												
											}
											else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,224, 463-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else if (coberturaVO.getClaveCobertura().trim().equals("45")||coberturaVO.getClaveCobertura().trim().equals("9")){
												cb=pdf.addLabel(cb,218, 463-(9*(y-toppage)),9,"INCLUIDO/INCLUDED",false,1);			
											}
											else if (coberturaVO.getClaveCobertura().trim().compareTo("1")==0||coberturaVO.getClaveCobertura().trim().compareTo("2")==0){
												String prueba=coberturaVO.getClaveCobertura().trim();
												cb=pdf.addLabel(cb,218, 463-(9*(y-toppage)),9,"VALOR COMERCIAL/MARKET VALUE",false,1);			
											}
											else{
												cb=pdf.addLabel(cb,218, 463-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}

										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,435, 463-(9*(y-toppage)),9,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,435, 463-(9*(y-toppage)),9,"5%",false,1);
											}else if ("45".equals(coberturaVO.getClaveCobertura())||"9".equals(coberturaVO.getClaveCobertura())){
												cb=pdf.addLabel(cb,431, 463-(9*(y-toppage)),9,"US$ 200",false,1);
											}else{
												cb=pdf.addLabel(cb,431, 463-(9*(y-toppage)),9,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 565, 463-(9*(y-toppage)),9,coberturaVO.getPrima(),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("11")){												
										if(coberturaVO.isFlagSumaAsegurada()){	
											if(coberturaVO.isFlagSumaAsegurada()){	
												if (coberturaVO.getClaveCobertura().contains("6.2")){
													double dias=0;
													try {
														if (coberturaVO.getSumaAsegurada().contains(",")){
															int indice=0;
															indice=coberturaVO.getSumaAsegurada().indexOf(",");
															String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
															dias = Double.parseDouble(sumAseg)/500 ;
														}
														else{
															dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
														}
														cb=pdf.addLabel(cb,265, 463-(9*(y-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
													} catch (Exception e) {
														cb=pdf.addLabel(cb,265, 463-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
													}				
													
												}
												else if(coberturaVO.getClaveCobertura().equals("12")) {
													cb=pdf.addLabel(cb,276, 463-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
													
												}
												else{
													cb=pdf.addLabel(cb,265, 463-(9*(y-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
												}
											}
										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,425, 463-(9*(y-toppage)),9,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,425, 463-(9*(y-toppage)),9,"5%",false,1);
											}else if ("45".equals(coberturaVO.getClaveCobertura())||"9".equals(coberturaVO.getClaveCobertura())){
												cb=pdf.addLabel(cb,421, 463-(9*(y-toppage)),9,"US$ 200",false,1);
											}else{
												cb=pdf.addLabel(cb,421, 463-(9*(y-toppage)),9,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 565, 463-(9*(y-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}								
									
								if (coberturaVO.getDescrCobertura().contains("/")){
									cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().substring(0,coberturaVO.getDescrCobertura().indexOf("/")) ,false,1);
									y=y+1;
									cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getDescrCobertura().substring(coberturaVO.getDescrCobertura().indexOf("/"),coberturaVO.getDescrCobertura().length()),false,1);		

								}	
								else{
										if(coberturaVO.getClaveCobertura().trim().equals("1")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Collision/Upset" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("2")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9, "Total Theft" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("3")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Civil Liability"  ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("4")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Medical Expenses for Passengers" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("6")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Transportation Expense" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("7")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Legal Expenses" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("EXT R.C.,GL,GM,AV TIT.")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / ",false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Driver's Liability Extension" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("10")&& coberturaVO.getDescrCobertura().trim().equals("BIS RC DANOS A OCUPANTES")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Passengers Liability" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("15")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+".-"+coberturaVO.getDescrCobertura().trim()+" / " ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Road Side Assistance" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("45")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
									}else if(coberturaVO.getClaveCobertura().trim().equals("")){
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
									}
									else{
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,coberturaVO.getClaveCobertura().trim()+"45.-Serv. Asist. Accidente Pers. en Viaje"+" /" ,false,1);
										y=y+1;
										cb=pdf.addLabel(cb,40, 474-(9*(y-toppage))-10,9,"Travel Accident Assistance" ,false,1);
									}
								}
								y++;								
								}
							}
							
							//************Texto
							cb=pdf.addRectAng(cb,35,235,335,44);
							cb=pdf.addLabel(cb,40,218,8,"TEXTOS:",true,1);
							cb=pdf.addLabel(cb,75,218,8,"336 DERECHOS DEL ASEGURADO.",false,1);
							cb=pdf.addRectAng(cb,35,189,335,20);
							cb=pdf.addLabel(cb,40,177,8,"FORMA DE PAGO/",true,1);
							cb=pdf.addLabeli(cb,108,177,8,"METHOD OF PAYMENT:",true,1);
							cb=pdf.addLabel(cb,210,177,8,"CONTADO/",false,1);
							cb=pdf.addLabeli(cb,252,177,8,"CASH",false,1);
							
							cb=pdf.addRectAng(cb,35,167,335,60);
							cb=pdf.addLabel(cb,270,157,8,"01-800-288-6700",true,1);
							cb=pdf.addLabel(cb,40,152,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
							cb=pdf.addLabel(cb,270,147,8,"01-800-800-2880",true,1);
							cb=pdf.addlineH(cb,33,137,340);
							cb=pdf.addLabel(cb,270,127,8,"01-800-062-0840",true,1);
							cb=pdf.addLabel(cb,40,127,8,"BILINGUAL ATTENTION",true,1);
							cb=pdf.addLabel(cb,195,127,8,"ENGLISH",true,1);
							cb=pdf.addLabel(cb,270,117,8,"01-800-062-0841",true,1);
							/*
							String valor =new String("日本の".getBytes("ISO-8859-1"),"UTF-8");
							String valorJ = new String(valor.getBytes("SHIFT-JIS"),"SHIFT-JIS");
							cb=pdf.addLabel(cb,40,117,8,valor,true,1);
							log.info("ValorJapones:: "+new String(valor.getBytes("SHIFT-JIS"),"SHIFT-JIS"));
							log.info("ValorJapones2:: "+new String(valorJ.getBytes("UTF-8"),"UTF-8"));*/
							
							//************forma de pago
							cb=pdf.addRectAng(cb,35,105,335,60);
							cb=pdf.addLabel(cb,40,99,8,"Tarifa Aplicada:",false,1);
							
							//************importe
							cb=pdf.addRectAngColor(cb,383,235,183,13);
							cb=pdf.addLabel(cb,390,225,10,"MONEDA/",true,1);
							cb=pdf.addLabeli(cb,437,225,9,"CURRENCY",true,1);
							if(polizaVO.getDescMoneda()!=null){
								cb=pdf.addLabel(cb,560,225,10,polizaVO.getDescMoneda(),true,2);}	
							
							cb=pdf.addRectAng(cb,383,219,183,83);
							cb=pdf.addLabel(cb,395,209,8,"PRIMA NETA/",false,1);
							cb=pdf.addLabeli(cb,449,209,8,"NET PREMIUM",false,1);
							cb=pdf.addLabel(cb,395,199,8,"TASA F.P.F/",false,1);
							cb=pdf.addLabeli(cb,442,199,8,"RATE FINANCING",false,1);
							cb=pdf.addLabeli(cb,395,189,8,"INSTALLMENT PAYMENT",false,1);
							cb=pdf.addLabel(cb,395,179,8,"GTOS. EXPEDICION POLIZA/",false,1);
							cb=pdf.addLabeli(cb,395,169,8,"POLICY FREE",false,1);
							cb=pdf.addLabel(cb,395,155,8,"SUBTOTAL/",false,1);
							cb=pdf.addLabeli(cb,440,155,8,"AMOUNT",false,1);
							cb=pdf.addLabel(cb,395,145,8,"I.V.A./",false,1);
							cb=pdf.addLabeli(cb,420,145,8,"TAX",false,1);
							
							cb=pdf.addRectAng(cb,383,134,183,20);
							cb=pdf.addLabel(cb,390,124,8,"IMPORTE TOTAL",true,1);
							//cb=pdf.addLabeli(cb,459,101,7,"TOTAL PREMIUM",true,1);
							
						 }//fin for para las dos hojas de la poliza					
						}
					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;
				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
		
	}

	
	public void creaPdfNormal_cambios_conducef_bis(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					//document.newPage();//5-ene-2016

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();
						
						
						if(page==0){
														
							document.newPage();
								
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 1/1",true,1);							
							
							cb=pdf.addLabel(cb,100,695,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",false,1);										
							cb=pdf.addLabel(cb,400,696,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,696,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,525,696,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,400,684,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,460,684,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,684,10,incisoAux,false,1);
								}
							}

							//**********CUERPO		
							cb=pdf.addRectAngColor(cb,23,684,562,12);
							cb=pdf.addRectAng(cb,23,671,562,73);

							//***********asegurado					
							cb=pdf.addLabel(cb,290,674,10,"INFORMACIÓN DEL ASEGURADO",true,0);		
							cb=pdf.addLabel(cb,27,612,10,"Vigencia           Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,330,612,10,"Hasta las 12:00 P.M. del:",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,27,652,10,nombre,false,1);
			
									//la información siguiente va de descripción del vehiculo a tipo de carga							
									if(polizaVO.getAmis()!=null){
										cb=pdf.addLabel(cb,27,632,10,polizaVO.getAmis(),false,1);}							
									if(polizaVO.getDescVehi()!=null){
										cb=pdf.addLabel(cb,57,632,10,polizaVO.getDescVehi(),false,1);}	
									
									if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,222,612,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
									}
									if(polizaVO.getFchFin()!=null){
										cb=pdf.addLabel(cb,460,612,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

									}						

							}				

							cb=pdf.addRectAng(cb,23,590,562,408);
							cb=pdf.addRectAngColor(cb,23,590,562,13);

							cb=pdf.addLabel(cb,210,580,10,"INFORMACION IMPORTANTE",true,1);
							cb=pdf.addLabel(cb,27,550,10,"Estimado asegurado nos permitimos informarle que si durante la vigencia de su seguro las Condiciones Generales",false,1);
							cb=pdf.addLabel(cb,27,538,10,"presentaran alguna modificación estas serían publicadas en nuestra página web www.qualitas.com.mx para su consulta,",false,1);
							cb=pdf.addLabel(cb,27,526,10,"descarga o impresión. (Artículo 65 de la Ley sobre el Contrato de Seguro).",false,1);
							cb=pdf.addLabel(cb,27,514,10,"",false,1);
							cb=pdf.addLabel(cb,27,502,10,"Asimismo, con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro de",false,1);
							cb=pdf.addLabel(cb,27,490,10,"automóvil que acaba de adquirir, Quálitas Compañia de Seguros, lo invita a que lea sus Condiciones Generales ",false,1);
							cb=pdf.addLabel(cb,27,478,10,"mismas que se adjuntan a esta póliza, o bien, puede usted ingresar a nuestra página Web.",false,1);
							cb=pdf.addLabel(cb,27,466,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
							cb=pdf.addlineH(cb,27,464,343);
							cb=pdf.addLabel(cb,27,454,10,"",false,1);							
							cb=pdf.addLabel(cb,27,442,10,"Usted puede consultar el folleto que contiene los Derechos de los Asegurados, Contratantes y Beneficiarios en nuestra",false,1);
							cb=pdf.addLabel(cb,27,430,10,"página de internet (www.qualitas.com.mx)",false,1);
							cb=pdf.addLabel(cb,27,418,10,"",false,1);						
							cb=pdf.addLabel(cb,27,406,10,"Artículo 25 de la Ley sobre el Contrato de Seguro. Si el contenido de la póliza o sus modificaciones no concordaren",false,1);
							cb=pdf.addLabel(cb,27,394,10,"con la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan",false,1);
							cb=pdf.addLabel(cb,27,382,10,"al día en que reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de",false,1);
							cb=pdf.addLabel(cb,27,370,10,"sus modificaciones.",false,1);
							cb=pdf.addLabel(cb,27,358,10,"",false,1);
							cb=pdf.addLabel(cb,27,346,10,"Nuestra Unidad Especializada de Atención a Usuario (UNE) con siguiente domicilio en: Boulevard Adolfo López Mateos 2601",false,1);
							cb=pdf.addLabel(cb,27,334,10,"Colonia Progreso Tizapán, Delegación Alvaro Obregón, México, Distrito Federal C.P. 01080, horario de atención",false,1);
							cb=pdf.addLabel(cb,27,322,10,"de Lunes a Viernes de 9:00 a.m. a 6:00 p.m., teléfono 01 (55) 5481 8500, correo electrónico: uauf@quialitas.com.mx",false,1);
							cb=pdf.addLabel(cb,27,310,10,"",false,1);
							cb=pdf.addLabel(cb,27,298,10,"Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
							cb=pdf.addLabel(cb,27,286,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono 01 (55) 5340 0999 y ",false,1);
							cb=pdf.addLabel(cb,27,274,10,"01 (800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría.condusef.gob.mx",false,1);
							cb=pdf.addLabel(cb,27,262,10,"",false,1);
							cb=pdf.addLabel(cb,27,250,10,"Quálitas Compañía de Seguros, S.A. de C.V. (en lo sucesivo La Compañía), asegura de acuerdo a las Condiciones",false,1);
							cb=pdf.addLabel(cb,27,238,10,"Generales y Especiales de esta Póliza el vehículo contra pérdidas o daños causados por cualquiera de los",false,1);
							cb=pdf.addLabel(cb,27,226,10,"riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							cb=pdf.addRectAng(cb,23,182,562,86);//seccion 5
							cb=pdf.addRectAngColor(cb,23,182,562,12);//seccion 5
							cb=pdf.addLabel(cb,27,158,10,"Agente:",false,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
							cb=pdf.addLabel(cb,27,134,10,"Oficina:",false,1);
							cb=pdf.addLabel(cb,27,122,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,400,122,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,27,110,10,"Colonia:",false,1);
							cb=pdf.addLabel(cb,27,98,10,"Télefono:",false,1);
							cb=pdf.addLabel(cb,400,98,10,"Fax:",false,1);
							if(polizaVO!= null){
								//la siguiente información va del nombre del agente a telefono nacional
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								cb=pdf.addLabel(cb,130,158,10,nombreAgente,false,1);
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,67,158,10,polizaVO.getClavAgente(),false,1);}
								cb=pdf.addlineH(cb,20,148,562);
								if(polizaVO.getDescOficina()!=null){
									cb=pdf.addLabel(cb,77,134,10,polizaVO.getDescOficina(),false,1);}							
								if(polizaVO.getCalleOficina()!=null){						
									cb=pdf.addLabel(cb,77,122,10,polizaVO.getCalleOficina(),false,1);}
								if(polizaVO.getCodPostalOficina()!=null){										
									cb=pdf.addLabel(cb,440,122,10,polizaVO.getCodPostalOficina(),false,1);}
								if(polizaVO.getColoniaOficina()!=null){										
									cb=pdf.addLabel(cb,77,110,10,polizaVO.getColoniaOficina(),false,1);}
								if(polizaVO.getTelOficina()!=null){
									cb=pdf.addLabel(cb,77,98,10,polizaVO.getTelOficina(),false,1);}
								if(polizaVO.getFaxOficina()!=null){					
									cb=pdf.addLabel(cb,440,98,10,polizaVO.getFaxOficina(),false,1);}
																
								
							}
							
							cb=pdf.addRectAng(cb,23,94,562,65);//seccion 7
							cb=pdf.addLabel(cb,27,81,12,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,",false,1);
							cb=pdf.addLabel(cb,27,67,12,"la documentación contractual y la nota técnica que integran este producto de seguro, quedaron",false,1);
							cb=pdf.addLabel(cb,27,53,12,"registrados ante la Comisión Nacional de Seguros y Fianzas a partir del día ",false,1);
							cb=pdf.addLabel(cb,27,39,12,"20 de Julio de 2015 con el número CNSF-S0046-3135-1005",false,1);

							
					for (int l=0;l<=1;l++){								
							document.newPage();
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 3/1",true,1);		
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							cb=pdf.addLabel(cb,100,705,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",true,1);										
							cb=pdf.addLabel(cb,400,705,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,705,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,525,705,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;
								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,400,695,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,460,695,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}
								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,525,695,10,incisoAux,false,1);
								}
							}
							//**********CUERPO			
							cb=pdf.addRectAngColor(cb,23,691,562,12);
							cb=pdf.addRectAng(cb,23,678,562,60);
							//***********asegurado					
							cb=pdf.addLabel(cb,290,681,10,"INFORMACIÓN DEL ASEGURADO",true,0);				
							cb=pdf.addLabel(cb,440,668,10,"RENUEVA A:",false,1);
							cb=pdf.addLabel(cb,40,656,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,40,644,10,"C.P.",false,1);
							cb=pdf.addLabel(cb,340,644,10,"RFC",false,1);
							cb=pdf.addLabel(cb,40,632,10,"BENEFICIARIO PREFERENTE:",false,1);	
							if(polizaVO.getPolizaAnterior()!=null){
								cb=pdf.addLabel(cb,515,668,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	
								cb=pdf.addLabel(cb,40,668,10,nombre,false,1);
								if(datosCliente){									
									cb=pdf.addLabel(cb,490,670,10,"  ",false,1);	
								if (l==0){
									if(polizaVO.getCalle()!=null){
										String calle= polizaVO.getCalle();
										if(polizaVO.getExterior()!= null){
											calle += " No. EXT. " + polizaVO.getExterior();
										}
										if(polizaVO.getInterior()!= null){
											calle += " No. INT. " + polizaVO.getInterior();
										}
										//ANDRES-prueba 
										//System.out.println("colonia:::"+polizaVO.getColonia());
											if(polizaVO.getColonia()!=null){
												calle += " COL. " + polizaVO.getColonia();
											}
											cb=pdf.addLabel(cb,93,656,10,calle,false,1);
											
											if(polizaVO.getCodPostal()!=null){
												cb=pdf.addLabel(cb,70,644,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
											if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
												cb=pdf.addLabel(cb,130,644,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
											if(polizaVO.getRfc()!=null){
												cb=pdf.addLabel(cb,390,644,10,polizaVO.getRfc(),false,1);}
											if(polizaVO.getBeneficiario()!=null){
												cb=pdf.addLabel(cb,40,632,10,polizaVO.getBeneficiario(),false,1);}
										}
								}
								}
							}				

							//*************Vehiculo
							cb=pdf.addRectAngColor(cb,23,616,562,12);
							cb=pdf.addRectAng(cb,23,616,562,65);
							cb=pdf.addLabel(cb,290,607,10,"DESCRIPCIÓN DEL VEHÍCULO ASEGURADO",true,0);
							cb=pdf.addLabel(cb,40,580,10,"Tipo:",false,1);
							cb=pdf.addLabel(cb,260,580,10,"Modelo:",false,1);
							cb=pdf.addLabel(cb,390,580,10,"Color:",false,1);
							cb=pdf.addLabel(cb,40,567,10,"Serie:",false,1);
							cb=pdf.addLabel(cb,259,567,10,"Motor:",false,1);
							cb=pdf.addLabel(cb,390,567,10,"REPUVE:",false,1);
							cb=pdf.addLabel(cb,490,567,10,"Placas:",false,1);
							if(polizaVO != null){
								//la información siguiente va de descripción del vehiculo a tipo de carga							
								if(polizaVO.getAmis()!=null){
									cb=pdf.addLabel(cb,40,595,10,polizaVO.getAmis(),false,1);}							
								if(polizaVO.getDescVehi()!=null){
									cb=pdf.addLabel(cb,70,595,10,polizaVO.getDescVehi(),false,1);}											

								if(polizaVO.getTipo()!=null){

									if(polizaVO.getTipo().length()>18){
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo().substring(0, 19),false,1);
									}else{
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo(),false,1); 
									}	
								}

								if(polizaVO.getVehiAnio()!=null){												
									cb=pdf.addLabel(cb,317,580,10,polizaVO.getVehiAnio(),false,1);}
								if(polizaVO.getColor()!=null){

									if(polizaVO.getColor().equals("SIN COLOR")){

										cb=pdf.addLabel(cb,428,580,10,"",false,1);		
									}else{
										cb=pdf.addLabel(cb,428,580,10,polizaVO.getColor(),false,1);
									}
								}
								//ANDRES-PASAJEROS
								if (polizaVO.getNumPasajeros()!=null){
									cb=pdf.addLabel(cb,40,557,10,polizaVO.getNumPasajeros(),false,1);
								}
								else if(polizaVO.getNumOcupantes()!=null){
									cb=pdf.addLabel(cb,490,580,10,"Ocupantes:",false,1);
									cb=pdf.addLabel(cb,550,580,10,polizaVO.getNumOcupantes(),false,1);
								}
								if(polizaVO.getNumPlaca()!=null){												
									cb=pdf.addLabel(cb,530,567,10,polizaVO.getNumPlaca(),false,1);}
								if(polizaVO.getNumSerie()!=null){					
									cb=pdf.addLabel(cb,73,567,10,polizaVO.getNumSerie(),false,1);}
								if(polizaVO.getNumMotor()!=null){					
									cb=pdf.addLabel(cb,300,567,10,polizaVO.getNumMotor(),false,1);}
								if(polizaVO.getRenave()!=null){					
									cb=pdf.addLabel(cb,440,567,10,polizaVO.getRenave(),false,1);}
								
								if (polizaVO.getCveServ().trim().equals("3")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,"'"+polizaVO.getClaveCarga()+"'",false,1);}
									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
										cb=pdf.addLabel(cb,200,554,10,polizaVO.getTipoCarga()+" : ",true,2);}
									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
										String descAux = "";
										String valorRemolque="";
										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
											descAux=polizaVO.getDescCarga();
										}
										
										if(polizaVO.getDobleRemolque()!= null){
											if(polizaVO.getDobleRemolque().equals("S")){
												valorRemolque = "2° Remolque: AMPARADO";
											}else{
												valorRemolque = "2° Remolque: EXCLUIDO";
											}
										}								
										if(descAux != "" || valorRemolque != ""){
											cb=pdf.addLabel(cb,210,554,10,descAux+"  "+valorRemolque,false,1);
										}
										
									}
								}
								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
									}
								}
								if(polizaVO.getNoEconomico()!=null){
									cb=pdf.addLabel(cb,480,567,10,"NO.ECO.",true,1);
									cb=pdf.addLabel(cb,515,567,10,polizaVO.getNoEconomico(),false,1);
								}		
							}
							//**************vigencia				
							cb=pdf.addRectAng(cb,23,550,183,45);
							cb=pdf.addRectAng(cb,215,550,181,45);
							cb=pdf.addRectAng(cb,403,550,181,45);
							cb=pdf.addLabel(cb,33,538,10,"VIGENCIA",false,1);
							cb=pdf.addLabel(cb,33,526,10,"Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,33,510,10,"Hasta las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,217,538,10,"Fecha Vencimiento del pago:",false,1);
							cb=pdf.addLabel(cb,217,510,10,"Plazo de pago:",false,1);
							cb=pdf.addLabel(cb,410,538,10,"Uso:",false,1);
							cb=pdf.addLabel(cb,410,526,10,"Servicio:",false,1);
							cb=pdf.addLabel(cb,410,510,10,"Movimiento:",false,1);
							if(polizaVO != null){		
								//la información siguiente va desde la fecha de vigencia hasta servicio
								if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,147,526,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
								}
								if(polizaVO.getFchFin()!=null){
									cb=pdf.addLabel(cb,147,510,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

								}

								if(polizaVO.getFechaLimPago()!=null){																												
									cb=pdf.addLabel(cb,259,526,10,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
								if(polizaVO.getPlazoPago()!=null){
									cb=pdf.addLabel(cb,287,510,10, polizaVO.getPlazoPago()+" dias",false,1);}


								if(polizaVO.getMovimiento()!=null){
									cb=pdf.addLabel(cb,500,510,10,polizaVO.getMovimiento(),false,1);
								}
								
								if(polizaVO.getUso()!=null){
									ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      15 , 21*2 , 25*3
									if(uso!=null){
										if(uso.size()==1){
											cb=pdf.addLabel(cb,500,538,10,(String)uso.get(0),false,1);
										}										
									}									
								}
								if(polizaVO.getServicio()!=null){
									ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      23 , 31*2 , 37*3
									if(servicio!=null){
										if(servicio.size()==1){
											cb=pdf.addLabel(cb,500,526,10,(String)servicio.get(0),false,1);
										}
									}									
								}					
							}
							//*************Datos de Riesgos
							//cb=pdf.addRectAng(cb,23,615,562,65);
							cb=pdf.addRectAngColor(cb,23,503,562,14);	
							cb=pdf.addRectAng(cb,23,486,562,235);
							cb=pdf.addLabel(cb,33,492,10,"COBERTURAS CONTRATADAS",true,1);
							cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
							cb=pdf.addLabel(cb,430,492,10,"DEDUCIBLE",true,1);
							cb=pdf.addLabel(cb,520,492,10,"$     PRIMAS",true,1);
							//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
							//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
							//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
							//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
							double primaAux=0;
							double primaExe=0;
							double derechoAux=0;
							double recargoAux=0;
							double subtotalAux=0;
							double impuestoAux=0;
							boolean exDM=false;
							boolean exRT=false;
							boolean agenEsp1 = false;
							boolean agenEsp2 = false;
							boolean validaAltoRiesgo = false;					
							for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
								int temp = (Integer)polizaVO.getAgenteEsp().get(y);
								if(temp==1)
									agenEsp1=true;
								if(temp==AGEN_ESP_OCULTA_PRIMAS)
									agenEsp2=true;
							}
							boolean minimos=false;
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;
								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									if(coberturaVO.getClaveCobertura().equals("12")){
										exDM=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}else if(coberturaVO.getClaveCobertura().equals("40")){
										exRT=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}	
								}
								String cveServ = polizaVO.getCveServ().trim();
								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{	
									boolean salto=false;
									if(x==toppage+27)
									{break;}

									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									String claveCobertura = coberturaVO.getClaveCobertura();

									int deducible = getNumber(coberturaVO.getDeducible());
									int anio = getNumber(polizaVO.getVehiAnio());
									if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
										validaAltoRiesgo = true;
									}
									//ANDRES-MINIMOS
									if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
										minimos=true;
									}
									log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
									int diaEmi = 0;
									int mesEmi = 0;
									int anioEmi = 0;									
									if (polizaVO.getFchEmi() != null && !polizaVO.getFchEmi().isEmpty()) {
										diaEmi = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
										mesEmi = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
										anioEmi = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
									}
									if ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 9) || (anioEmi >= 2015 && mesEmi >= 8 && diaEmi>=17)){//a partir del 17 agosot 2015 se quitan los numeros de cobertura
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T" ,false,1);//quitar clave cobe
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exe. Ded. x PT, DM Y RT" ,false,1);//quitar clave cobe
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estandarizado" ,false,1);//quitar clave cobe
										}else{
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												if (coberturaVO.getClaveCobertura().equals("1")){
													if (coberturaVO.getDescrCobertura().contains("MATERIALES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños materiales" ,false,1);}
													if (coberturaVO.getDescrCobertura().contains("TOTAL")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Solo Perdida Total" ,false,1);}
													}
												else if (coberturaVO.getClaveCobertura().equals("10")){
													if (coberturaVO.getDescrCobertura().contains("OCUPANTES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Daños a Ocupantes" ,false,1);}
													if (coberturaVO.getDescrCobertura().contains("EXT")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Extensión de coberturas" ,false,1);}
													}
												else if (coberturaVO.getClaveCobertura().equals("11")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible DM",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Pasajero" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo Parcial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Ajuste Automático" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Vial",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("16.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA DM" ,false,1);}			
												else if (coberturaVO.getClaveCobertura().equals("16.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA RT" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Satelital" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("18")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Can. Ded por Colisión o vuelco" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("19")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AVC" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo total" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("20")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG EG" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("21")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG SM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("22")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Muerte del Conductor por Accidente Automovilístico" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.11")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Daños a Terceros EUA" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estand" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Cruzada" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.16")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Maniobras de carga y descarga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Arrastre de remolque" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Bienes" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.4")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños por carga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.5")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Ecologica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("4")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Médicos Ocupantes" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("40")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("41")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Avería Mecánica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("43")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Llantas",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("44")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Rines" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("51")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("52")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P P",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos de Transporte" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"GTP" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("7")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Legales" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("8")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Equipo Especial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Serv.Asist.AccidentePers.enViaje",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Adapt y/o Conversiones DM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AdapT y/o Conversiones RT" ,false,1);}																					
										}
									}
									else{//se muestra la numeracion de coberturas
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
											
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
											
										}else{
											//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
//												if ( polizaVO.getCvePlan().equals("34")){
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la pagina www.qualitas.com.mx para imprimir certificado", false, 1);
//												}
										}
									}
									if(!salto){
										//ANDRES-SUMASEG
										//suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}				
											}
											else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
											} 

										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}
										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,coberturaVO.getPrima(),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
										//ANDRES-SUMASEG
										//}
										//										suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if(coberturaVO.isFlagSumaAsegurada()){	
												if (coberturaVO.getClaveCobertura().contains("6.2")){
													double dias=0;
													try {
														if (coberturaVO.getSumaAsegurada().contains(",")){
															int indice=0;
															indice=coberturaVO.getSumaAsegurada().indexOf(",");
															String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
															dias = Double.parseDouble(sumAseg)/500 ;
														}
														else{
															dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
														}
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
													} catch (Exception e) {
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
													}				
												}
												else if(coberturaVO.getClaveCobertura().equals("12")) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
													
												}
												else{
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}
											}
										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if ("45".equals(coberturaVO.getClaveCobertura())){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"U$S 200",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}
										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,FormatDecimal.numDecimal(primaExe),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}		
								}
							}

							boolean altoRiesgo = false;
							if (validaAltoRiesgo) {
								log.debug("Se validara el alto riesgo");
								Integer tarifa = getNumber(polizaVO.getTarifa());
								// Tarifas enero 1990 - diciembre 2029
								boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
								if (tarifa < 1309 && formatoTarifaNormal) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								} else {
									List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
									if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
										Integer amis = getNumber(polizaVO.getAmis());
										int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
										altoRiesgo = claveAmisAltoRiesgo == 9999;
									}
								}
							}
							if (altoRiesgo) {
								cb = pdf.addLabel(cb, 35, 290, 10, "IMPORTANTE", true, 1);
								cb = pdf.addLabel(cb, 35, 280, 10, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
								cb = pdf.addLabel(cb, 35, 270, 10, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
							}
							
							int dif = 0;
							if (minimos){
								dif = 20; // Para que no choquen leyendas
								cb=pdf.addLabel(cb,35,270,10,DEDUCIBLE_MINIMOP1,true,1);
								cb=pdf.addLabel(cb,35,263,10,DEDUCIBLE_MINIMOP2,true,1);
							}
							
							String servicio = polizaVO.getServicio().trim();
							String tarifa = polizaVO.getTarifa().trim();
							log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
							if (servicio.equals("PUBLICO")) {
								cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPub1,true,1);
								cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPub2,true,1);
							} else if (servicio.equals("PARTICULAR")
									&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
								cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPart1,true,1);
								cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPart2,true,1);
							}
							//************Agente
							//cb=pdf.addRectAngColor(cb,35,242,335,12);
							cb=pdf.addRectAng(cb,23,248,355,50);
							cb=pdf.addLabel(cb,40,238,10,"Textos:",false,1);
							cb=pdf.addRectAng(cb,23,196,355,30);
							//************forma de pago
							cb=pdf.addLabel(cb,40,183,10,"Forma de:",false,1);
							cb=pdf.addLabel(cb,40,173,10,"Pago:",false,1);
							if(polizaVO != null){
								if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
								}
								else {
										//cb=pdf.addLabel(cb,100,208,10,polizaVO.getClavAgente(),false,1);
									
									if(polizaVO.getDescrFormPago()!=null){
											cb=pdf.addLabel(cb,88,177,10,polizaVO.getDescrFormPago(),false,1);
									}
		
									//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
									cb=pdf.addLabel(cb,315,183,10,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
									if(polizaVO.getNumRecibos()!=null && polizaVO.getNumRecibos() > 1){
										cb=pdf.addLabel(cb,185,183,10,"Primer pago",false,1);
										cb=pdf.addLabel(cb,185,173,10,"Pago(s) Subsecuente(s)",false,1);																						
										cb=pdf.addLabel(cb,315,173,10,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
									}else{
										cb=pdf.addLabel(cb,190,183,10,"Pago Unico",false,1);
									}
								}			
							}
							cb=pdf.addRectAng(cb,23,164,355,47);
							cb=pdf.addLabel(cb,40,154,10,"Exclusivo para reporte de",true,1);
							cb=pdf.addLabel(cb,40,144,10,"Siniestros",true,1);
							cb=pdf.addLabel(cb,180,154,10,"01-800-288-6700",true,1);
							cb=pdf.addLabel(cb,180,144,10,"01-800-800-2880",true,1);
							cb=pdf.addlineH(cb, 23, 139, 355);
							//la siguiente información va del nombre del agente a telefono nacional
							cb=pdf.addLabel(cb,40,128,10,"Agente:",true,1);
							cb=pdf.addLabel(cb,40,118,10,"Teléfono:",true,1);
							
							if (polizaVO!=null){
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								cb=pdf.addLabel(cb,88,128,10,nombreAgente,true,1);
								//ANDRES-TELEFONO AGENTE
								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
									if(polizaVO.getTelComerAgente()!=null){
										cb=pdf.addLabel(cb,88,121,10,polizaVO.getTelComerAgente(),true,1);
									}
									else{
										cb=pdf.addLabel(cb,88,118,10,polizaVO.getTelPartAgente(),true,1);
									}
	
								}
						}
							cb=pdf.addRectAng(cb,23,115,355,60);
							//************importe
							cb=pdf.addRectAngColor(cb,390,247,195,13);
							cb=pdf.addLabel(cb,395,237,10,"MONEDA",true,1);
							if(polizaVO.getDescMoneda()!=null){
								cb=pdf.addLabel(cb,560,237,10,polizaVO.getDescMoneda(),true,2);}
							cb=pdf.addRectAng(cb,390,232,195,20);
																
							cb=pdf.addRectAng(cb,390,210,195,83);
							cb=pdf.addLabel(cb,395,200,10,"Prima Neta",false,1);
							cb=pdf.addLabel(cb,395,190,10,"Tasa Financiamiento",false,1);
							cb=pdf.addLabel(cb,395,180,10,"Gastos por Expedición.",false,1);
							cb=pdf.addLabel(cb,395,147,10,"Subtotal",false,1);
							cb=pdf.addLabel(cb,395,135,10,"I.V.A.   16%",false,1);
							cb=pdf.addLabel(cb,395,116,10,"IMPORTE TOTAL",true,1);
							cb=pdf.addLabel(cb,30,103,10,"Tarifa Aplicada:",false,1);
							
							if(polizaVO != null){
								//la información siguiente va de prima neta a tarifa aplicada
								if(polizaVO.getPrimaNeta()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(primaAux),false,2);
									}
									else
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
								}
								if(polizaVO.getRecargo()!=null){
									if(Double.parseDouble(polizaVO.getRecargo())>0){
										if(Integer.parseInt(polizaVO.getNumIncisos())>1){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}
										else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}else{
										if(Integer.parseInt(polizaVO.getNumIncisos())<0){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}
								}
								if(polizaVO.getDerecho()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(derechoAux),false,2);

									}
									else{
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
								}
								if(polizaVO.getCesionComision()!=null){
									    cb=pdf.addLabel(cb,395,157,10,"DESCUENTOS",false,1);
										cb=pdf.addLabel(cb,560,157,10,polizaVO.getCesionComision(),false,2);
								}
								if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										subtotalAux = primaAux+derechoAux;				
									}
									else{
										subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

									cb=pdf.addLabel(cb,560,147,10,FormatDecimal.numDecimal(subtotalAux),false,2);
								}
								cb=pdf.addlineH(cb,460,143,115);
								if(polizaVO.getImpuesto()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
										if(polizaVO.getIva()!=null){
											impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
											cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(impuestoAux),false,2);
										}
									}
									else{
										cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
								}
								cb=pdf.addRectAng(cb,390,125,195,13);
								if(polizaVO.getPrimaTotal()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
									}
									else{
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
								}
								if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
									if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
										String concatZonaId= "0000"+polizaVO.getTarifApDesc();
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}
									else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
										String concatZonaId= "0000"+polizaVO.getTarifApCve();
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}
								}								

							}
							//****firma
							if(polizaVO != null){
								String lugar="";
								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
								cb=pdf.addLabel(cb,490,100,10,lugar,false,0);
								//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
								if(polizaVO.getFchEmi()!=null){
									cb=pdf.addLabel(cb,490,90,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
							}
							if(polizaVO.getDirImagen()!=null){
								document=pdf.addImage(document,450,60,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
							}
							cb=pdf.addLabel(cb,490,58,10,"Funcionario Autorizado",false,0);	


							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables "+polizaVO.getDescConVig()+ "mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							else{
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables                              mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
							}
						}//fin for para las dos hojas de la poliza					
						}
					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;
				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}

	
	public void creaPdfNormal_cambios_conducef_bis_prueba(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					//document.newPage();//5-ene-2016

					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();
						
						
						if(page==0){
														
							document.newPage();
								

							//**********CUERPO		
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							
							
							cb=pdf.addRectAngColor(cb,23,684,562,12);
							cb=pdf.addRectAng(cb,23,671,562,73);

							//***********asegurado					
							cb=pdf.addLabel(cb,290,674,10,"INFORMACIÓN DEL ASEGURADO",true,0);		
							cb=pdf.addLabel(cb,27,612,10,"Vigencia           Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,330,612,10,"Hasta las 12:00 P.M. del:",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,27,652,10,nombre,false,1);
			
									//la información siguiente va de descripción del vehiculo a tipo de carga							
									if(polizaVO.getAmis()!=null){
										cb=pdf.addLabel(cb,27,632,10,polizaVO.getAmis(),false,1);}							
									if(polizaVO.getDescVehi()!=null){
										cb=pdf.addLabel(cb,57,632,10,polizaVO.getDescVehi(),false,1);}	
									
									if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,222,612,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
									}
									if(polizaVO.getFchFin()!=null){
										cb=pdf.addLabel(cb,460,612,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

									}						

							}				

							cb=pdf.addRectAng(cb,23,590,562,408);
							cb=pdf.addRectAngColor(cb,23,590,562,13);

							cb=pdf.addLabel(cb,210,580,10,"INFORMACION IMPORTANTE",true,1);
							cb=pdf.addLabel(cb,27,550,10,"Estimado asegurado nos permitimos informarle que si durante la vigencia de su seguro las Condiciones Generales",false,1);
							cb=pdf.addLabel(cb,27,538,10,"presentaran alguna modificación estas serían publicadas en nuestra página web www.qualitas.com.mx para su consulta,",false,1);
							cb=pdf.addLabel(cb,27,526,10,"descarga o impresión. (Artículo 65 de la Ley sobre el Contrato de Seguro).",false,1);
							cb=pdf.addLabel(cb,27,514,10,"",false,1);
							cb=pdf.addLabel(cb,27,502,10,"Asimismo, con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro de",false,1);
							cb=pdf.addLabel(cb,27,490,10,"automóvil que acaba de adquirir, Quálitas Compañia de Seguros, lo invita a que lea sus Condiciones Generales ",false,1);
							cb=pdf.addLabel(cb,27,478,10,"mismas que se adjuntan a esta póliza, o bien, puede usted ingresar a nuestra página Web.",false,1);
							cb=pdf.addLabel(cb,27,466,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
							cb=pdf.addlineH(cb,27,464,343);
							cb=pdf.addLabel(cb,27,454,10,"",false,1);							
							cb=pdf.addLabel(cb,27,442,10,"Usted puede consultar el folleto que contiene los Derechos de los Asegurados, Contratantes y Beneficiarios en nuestra",false,1);
							cb=pdf.addLabel(cb,27,430,10,"página de internet (www.qualitas.com.mx)",false,1);
							cb=pdf.addLabel(cb,27,418,10,"",false,1);						
							cb=pdf.addLabel(cb,27,406,10,"Artículo 25 de la Ley sobre el Contrato de Seguro. Si el contenido de la póliza o sus modificaciones no concordaren",false,1);
							cb=pdf.addLabel(cb,27,394,10,"con la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan",false,1);
							cb=pdf.addLabel(cb,27,382,10,"al día en que reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de",false,1);
							cb=pdf.addLabel(cb,27,370,10,"sus modificaciones.",false,1);
							cb=pdf.addLabel(cb,27,358,10,"",false,1);
							cb=pdf.addLabel(cb,27,346,10,"Nuestra Unidad Especializada de Atención a Usuario (UNE) con siguiente domicilio en: Boulevard Adolfo López Mateos 2601",false,1);
							cb=pdf.addLabel(cb,27,334,10,"Colonia Progreso Tizapán, Delegación Alvaro Obregón, México, Distrito Federal C.P. 01080, horario de atención",false,1);
							cb=pdf.addLabel(cb,27,322,10,"de Lunes a Viernes de 9:00 a.m. a 6:00 p.m., teléfono 01 (55) 5481 8500, correo electrónico: uauf@quialitas.com.mx",false,1);
							cb=pdf.addLabel(cb,27,310,10,"",false,1);
							cb=pdf.addLabel(cb,27,298,10,"Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
							cb=pdf.addLabel(cb,27,286,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono 01 (55) 5340 0999 y ",false,1);
							cb=pdf.addLabel(cb,27,274,10,"01 (800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría.condusef.gob.mx",false,1);
							cb=pdf.addLabel(cb,27,262,10,"",false,1);
							cb=pdf.addLabel(cb,27,250,10,"Quálitas Compañía de Seguros, S.A. de C.V. (en lo sucesivo La Compañía), asegura de acuerdo a las Condiciones",false,1);
							cb=pdf.addLabel(cb,27,238,10,"Generales y Especiales de esta Póliza el vehículo contra pérdidas o daños causados por cualquiera de los",false,1);
							cb=pdf.addLabel(cb,27,226,10,"riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							cb=pdf.addRectAng(cb,23,182,562,86);//seccion 5
							cb=pdf.addRectAngColor(cb,23,182,562,12);//seccion 5
							cb=pdf.addLabel(cb,27,158,10,"Agente:",false,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
							cb=pdf.addLabel(cb,27,134,10,"Oficina:",false,1);
							cb=pdf.addLabel(cb,27,122,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,400,122,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,27,110,10,"Colonia:",false,1);
							cb=pdf.addLabel(cb,27,98,10,"Télefono:",false,1);
							cb=pdf.addLabel(cb,400,98,10,"Fax:",false,1);
							if(polizaVO!= null){
								//la siguiente información va del nombre del agente a telefono nacional
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								cb=pdf.addLabel(cb,130,158,10,nombreAgente,false,1);
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,67,158,10,polizaVO.getClavAgente(),false,1);}
								cb=pdf.addlineH(cb,20,148,562);
								if(polizaVO.getDescOficina()!=null){
									cb=pdf.addLabel(cb,77,134,10,polizaVO.getDescOficina(),false,1);}							
								if(polizaVO.getCalleOficina()!=null){						
									cb=pdf.addLabel(cb,77,122,10,polizaVO.getCalleOficina(),false,1);}
								if(polizaVO.getCodPostalOficina()!=null){										
									cb=pdf.addLabel(cb,440,122,10,polizaVO.getCodPostalOficina(),false,1);}
								if(polizaVO.getColoniaOficina()!=null){										
									cb=pdf.addLabel(cb,77,110,10,polizaVO.getColoniaOficina(),false,1);}
								if(polizaVO.getTelOficina()!=null){
									cb=pdf.addLabel(cb,77,98,10,polizaVO.getTelOficina(),false,1);}
								if(polizaVO.getFaxOficina()!=null){					
									cb=pdf.addLabel(cb,440,98,10,polizaVO.getFaxOficina(),false,1);}
							}
							
							cb=pdf.addRectAng(cb,23,94,562,65);//seccion 7
							cb=pdf.addLabel(cb,27,81,12,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,",false,1);
							cb=pdf.addLabel(cb,27,67,12,"la documentación contractual y la nota técnica que integran este producto de seguro, quedaron",false,1);
							cb=pdf.addLabel(cb,27,53,12,"registrados ante la Comisión Nacional de Seguros y Fianzas a partir del día ",false,1);
							cb=pdf.addLabel(cb,27,39,12,"20 de Julio de 2015 con el número CNSF-S0046-3135-1005",false,1);

							
					for (int l=0;l<=1;l++){								
							document.newPage();
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 3/1",true,1);		
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							cb=pdf.addLabel(cb,100,705,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",true,1);										
							cb=pdf.addLabel(cb,400,705,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,705,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,525,705,10,"INCISO",false,1);
							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables "+polizaVO.getDescConVig()+ "mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							else{
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables                              mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							

						}//fin for para las dos hojas de la poliza					
						}
					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;
				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}
	
	public void creaPdfNormal_cambios_conducef_une(List<PdfPolizaBean> arrPolizas,OutputStream salida,String membretado){
		Document document= new Document(PageSize.LETTER,10,10,10,10);
		CreatePdf pdf= new CreatePdf();
//		PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(0);

		//variables para indicar el numero maximo de renglones por pagina (coberturas)
		//número para la medida del numero de poliza para obtener el número de endoso
		//número de paginas que se generan para el archivo (depende del numero de polizas de arrPolizas)
		//número de paginas que se generan para el archivo sin datos del cliente (depende del numero de polizas de arrPolizas)
		int toppage=0;		
		int sizeNumPoliza;
		int numpages=0;
		int pageAux=0;
		int ii = 0;
				
		java.text.DecimalFormat objeForm1= new java.text.DecimalFormat("##");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			document.open();

			//for que se utiliza para obtener cada una de las polizas de arrPolizas
			for(int polizas=0;polizas<arrPolizas.size();polizas++){

				PdfPolizaBean polizaVO=(PdfPolizaBean) arrPolizas.get(polizas);

				//variable que nos indicara si la poliza que se esta pintando llevara o no los datos del cliente.
				boolean datosCliente=true;


				//de acuerdo al los datos obtenidos de polizaVO se generan el numero de paginas para la póliza especifica.
				if(polizaVO.getNumCopiasCCliente()>0){
					numpages=polizaVO.getNumCopiasCCliente();
					if(polizaVO.getNumCopiasSCliente()>0){
						numpages=numpages+polizaVO.getNumCopiasSCliente();
						if(polizaVO.getNumCopiasCCliente()==1 && polizaVO.getNumCopiasSCliente()==1){
							pageAux=1;
						}
						else{							
							pageAux=(numpages-polizaVO.getNumCopiasSCliente())-1;
						}						
					}																
				}
				else
					numpages=1;



				for(int page=0;page<numpages;page++){//número de paginas				
					//document.newPage();//5-ene-2016
					
					if(pageAux>=(numpages-page)){datosCliente=false;}

					try{
						//****************ENCABEZADO
						PdfContentByte cb=writer.getDirectContent();

						//ANDRES-MEM
//						if (membretado==null||membretado.equals("S")){
//							cb=pdf.addRectAngColorGreenWater(cb,35,718,535,30);
//							cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
//							//document=pdf.addImage(document,35,725,130,50,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/logoQpoliza.jpg");
//							//recibo.get(page).getDirImagen()+"logoQpoliza.jpg"
//							document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
//						}
						
						
						
						if(page==0){
							
//							cb=pdf.addRectAng(cb,23,717,562,46);//seccion 1
//							cb=pdf.addRectAng(cb,348,707,237,24);//seccion 1
//							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 0/1",true,1);
//							
//							cb=pdf.addLabel(cb,60,695,10,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//							cb=pdf.addLabel(cb,355,695,10,"POLIZA",true,1);
//							cb=pdf.addLabel(cb,435,695,10,"ENDOSO",true,1);
//							cb=pdf.addLabel(cb,525,695,10,"INCISO",true,1);
//							
//							if(polizaVO != null){
//								String inciso="000";
//								String incisoAux;
//								if(polizaVO.getNumPoliza()!=null){
//									sizeNumPoliza=polizaVO.getNumPoliza().length();
//									cb=pdf.addLabel(cb,355,685,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//									cb=pdf.addLabel(cb,435,685,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//								}
//								if(polizaVO.getInciso()!=null){
//									inciso = inciso+polizaVO.getInciso();								
//									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//									cb=pdf.addLabel(cb,525,685,10,incisoAux,false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,669,562,77);//seccion 2
//							cb=pdf.addRectAng(cb,401,657,184,14);//seccion 2
//							cb=pdf.addRectAng(cb,401,618,184,26);//seccion 2
//							cb=pdf.addLabel(cb,27,658,10,"DATOS DEL CONTRATANTE:",true,1);
//							cb=pdf.addLabel(cb,250,658,10,"RFC:",true,1);
//							cb=pdf.addLabel(cb,401,658,10,"RENUEVA A:",false,1);
//							cb=pdf.addLabel(cb,435,646,10,"VIGENCIA:",true,1);
//							cb=pdf.addLabel(cb,27,630,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,401,633,10,"Desde las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,401,621,10,"Hasta las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,27,616,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,220,616,10,"Estado:",false,1);
//							cb=pdf.addLabel(cb,27,602,10,"Beneficiario Preferente:",false,1);
//							cb=pdf.addLabel(cb,410,606,10,"Fecha de vencimiento del pago",false,1);
//							if(polizaVO.getPolizaAnterior()!=null){
//								cb=pdf.addLabel(cb,472,658,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}
//							cb=pdf.addLabel(cb,472,658,10,"RENUEVAxxx",false,1);	
//							if(polizaVO != null){		
//								if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,511,633,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//								}
//								if(polizaVO.getFchFin()!=null){
//									cb=pdf.addLabel(cb,511,621,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//								}
//								if(polizaVO.getFechaLimPago()!=null){																												
//									cb=pdf.addLabel(cb,450,594,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//							}
//							
//							
//							if(polizaVO != null){
//								String nombre=""; 
//								if(polizaVO.getNombre()!=null){
//									nombre=polizaVO.getNombre()+" ";}
//								if(polizaVO.getApePate()!=null){
//									nombre=nombre+polizaVO.getApePate()+" ";}
//								if(polizaVO.getApeMate()!=null){
//									nombre=nombre+polizaVO.getApeMate()+" ";}
//
//								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//									nombre=nombre+" Y/O "+polizaVO.getConductor();
//								}	
//								cb=pdf.addLabel(cb,27,646,10,nombre,false,1);
//								if(datosCliente){										
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,75,630,10,calle,false,1);}
//
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,55,616,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,260,616,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,280,658,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getBeneficiario()!=null){
//										cb=pdf.addLabel(cb,135,602,10,polizaVO.getBeneficiario(),false,1);}
//									cb=pdf.addLabel(cb,135,602,10,"PRUEBA BENEFICIARIO PREFERENTE XXX",false,1);
//
//
//								}						
//
//							}				
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,590,562,62);//seccion 3
//							cb=pdf.addRectAng(cb,23,590,562,13);//seccion 3
//							cb=pdf.addLabel(cb,215,580,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,1);
//							cb=pdf.addLabel(cb,27,567,10,"Clave y Marca",false,1);
//							cb=pdf.addLabel(cb,27,556,10,"Tipo:",false,1);
//							cb=pdf.addLabel(cb,190,556,10,"Modelo:",false,1);
//							cb=pdf.addLabel(cb,320,556,10,"Color:",false,1);
//							cb=pdf.addLabel(cb,450,556,10,"Ocupantes:",false,1);
//							cb=pdf.addLabel(cb,27,544,10,"Serie:",false,1);
//							cb=pdf.addLabel(cb,190,544,10,"Motor:",false,1);
//							cb=pdf.addLabel(cb,390,544,10,"Placas:",false,1);
//
//							if(polizaVO != null){							
//								if(polizaVO.getAmis()!=null){
//									cb=pdf.addLabel(cb,97,567,10,polizaVO.getAmis(),false,1);}							
//								if(polizaVO.getDescVehi()!=null){
//									cb=pdf.addLabel(cb,127,567,10,polizaVO.getDescVehi(),false,1);}											
//								if(polizaVO.getTipo()!=null){
//									if(polizaVO.getTipo().length()>18){	
//										cb=pdf.addLabel(cb,57,556,10,polizaVO.getTipo().substring(0, 19),false,1);
//									}else{
//										cb=pdf.addLabel(cb,57,556,10,polizaVO.getTipo(),false,1); 
//									}	
//								}
//								if(polizaVO.getVehiAnio()!=null){												
//									cb=pdf.addLabel(cb,230,556,10,polizaVO.getVehiAnio(),false,1);}
//								if(polizaVO.getColor()!=null){
//
//									if(polizaVO.getColor().equals("SIN COLOR")){
//
//										cb=pdf.addLabel(cb,350,556,10,"",false,1);		
//									}else{
//										cb=pdf.addLabel(cb,350,556,10,polizaVO.getColor(),false,1);
//									}
//								}
//								//ANDRES-PASAJEROS
//								if (polizaVO.getNumPasajeros()!=null){
//									cb=pdf.addLabel(cb,507,556,10,polizaVO.getNumPasajeros(),false,1);
//								}
//								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,507,556,10,polizaVO.getNumOcupantes(),false,1);
//								}
//								if(polizaVO.getNumPlaca()!=null){												
//									cb=pdf.addLabel(cb,430,544,10,polizaVO.getNumPlaca(),false,1);}
//								if(polizaVO.getNumSerie()!=null){					
//									cb=pdf.addLabel(cb,60,544,10,polizaVO.getNumSerie(),false,1);}
//								if(polizaVO.getNumMotor()!=null){					
//									cb=pdf.addLabel(cb,224,544,10,polizaVO.getNumMotor(),false,1);}
//								
//								if (polizaVO.getCveServ().trim().equals("3")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,532,10,"Tipo de Carga",false,1);
//										cb=pdf.addLabel(cb,110,532,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//										cb=pdf.addLabel(cb,200,532,10,polizaVO.getTipoCarga()+" : ",true,2);}
//									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//										String descAux = "";
//										String valorRemolque="";
//										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//											descAux=polizaVO.getDescCarga();
//										}
//										
//										if(polizaVO.getDobleRemolque()!= null){
//											if(polizaVO.getDobleRemolque().equals("S")){
//												valorRemolque = "2° Remolque: AMPARADO";
//											}else{
//												valorRemolque = "2° Remolque: EXCLUIDO";
//											}
//										}								
//										if(descAux != "" || valorRemolque != ""){
//											cb=pdf.addLabel(cb,260,532,10,descAux+"  "+valorRemolque,false,1);
//										}
//										
//									}
//								}
//								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,532,10,"Tipo de Carga "+polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//									}
//								}
//								if(polizaVO.getNoEconomico()!=null){
//									cb=pdf.addLabel(cb,390,532,10,"No. Económico: "+polizaVO.getNoEconomico(),false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,526,562,342);//seccion 4
//							cb=pdf.addLabel(cb,27,516,10,"Estimado asegurado nos permitimos informarle que si durante la vigilancia de su seguro las Condiciones Generales",false,1);
//							cb=pdf.addLabel(cb,27,504,10,"presentaran alguna modificación estas serán publicadas en nuestra página web www.qualitas.com.mx para su consulta,",false,1);
//							cb=pdf.addLabel(cb,27,492,10,"descargar o impresión.",false,1);
//							cb=pdf.addLabel(cb,27,480,10,"",false,1);
//							cb=pdf.addLabel(cb,27,468,10,"Estimado Asegurado con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro",false,1);
//							cb=pdf.addLabel(cb,27,456,10,"de automóvil que acaba de adquirir, Quálitas, Compaña de Seguros, lo invita a que lea sus Condiciones Generales ",false,1);
//							cb=pdf.addLabel(cb,27,444,10,"mismas que se adjuntan a esta póliza, o bien, puede ustede ingresar a nuestra página Web, en la siguiente dirección",false,1);
//							cb=pdf.addLabel(cb,27,432,10,"electrónica:",false,1);
//							cb=pdf.addLabel(cb,82,432,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
//							cb=pdf.addlineH(cb,82,430,343);
//							cb=pdf.addLabel(cb,27,420,10,"",false,1);
//							cb=pdf.addLabel(cb,27,408,10,"Unidad Especializada de Atención a Usuario (UNE) Domicilio Avenida San Jerónimo número 478, piso 4°, Colonia Jardines",false,1);
//							cb=pdf.addLabel(cb,27,396,10,"del Pedregal, Delegación Alvaro Obregón, México, Distrito Federal, Código Postal 01900, horario de atención de Lunes a",false,1);
//							cb=pdf.addLabel(cb,27,384,10,"Viernes de 9:00 a.m. a 18:00 p.m., teléfono 01 (55) 15556067, correo electrónico uauf@quialitas.com.mx",false,1);
//							cb=pdf.addLabel(cb,27,372,10,"",false,1);
//							cb=pdf.addLabel(cb,27,360,10,"Comisión Nacional de la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF), Avenida",false,1);
//							cb=pdf.addLabel(cb,27,348,10,"Insurgentes Sur #762, Colonia del Valle, México, Distrito Federal, C.P. 03100. Teléfono (55) 5340 0999 y ",false,1);
//							cb=pdf.addLabel(cb,27,336,10,"(01 800) 999 80 80. Página Web www.condusef.gob.mx; correo electrónico asesoría.condusef.gob.mx",false,1);
//							cb=pdf.addLabel(cb,27,324,10,"",false,1);
//							cb=pdf.addLabel(cb,27,312,10,"Artículo 25 de la Ley sore el Contrato del Seguro. Si el contenido de la póliza o sus modificaciones no concordaren con",false,1);
//							cb=pdf.addLabel(cb,27,300,10,"la oferta, el Asegurado podrá pedir la rectificación correspondiente dentro de los treinta (30) días que sigan al día en que",false,1);
//							cb=pdf.addLabel(cb,27,288,10,"reciba su póliza, transcurrido ese plazo se considerán aceptadas las estipulaciones de la póliza o de sus modificaciones",false,1);
//							cb=pdf.addLabel(cb,27,276,10,"",false,1);
//							if(polizaVO.getDescConVig()!=null){		
//								cb=pdf.addLabel(cb,27,264,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables ("+ polizaVO.getDescConVig(),false,1);
//								cb=pdf.addLabel(cb,510,264,10,")misma que",false,1);
//							}
//							else{
//								cb=pdf.addLabel(cb,27,264,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables (__/__ ____-__)misma que",false,1);
//							}
//							
//							
//							
//							cb=pdf.addLabel(cb,27,252,10,"además puede consultar e imprimir en nuestra página www.qualitas.com.mx",false,1);
//							
//							
//							cb=pdf.addRectAng(cb,23,182,353,86);//seccion 5
//							cb=pdf.addRectAng(cb,23,182,353,12);//seccion 5
//							
//							cb=pdf.addLabel(cb,27,172,10,"OFICINA DE SERVICIO",true,1);
//							cb=pdf.addLabel(cb,27,158,10,"Agente:",false,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
//							cb=pdf.addLabel(cb,27,134,10,"Oficina:",false,1);
//							cb=pdf.addLabel(cb,27,122,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,300,122,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,27,110,10,"Colonia:",false,1);
//							cb=pdf.addLabel(cb,27,98,10,"Télefono:",false,1);
//							cb=pdf.addLabel(cb,194,98,10,"Fax:",false,1);
//							
//							
//							
//							
//							if(polizaVO!= null){
//								//la siguiente información va del nombre del agente a telefono nacional
//								String nombreAgente="";
//								if(polizaVO.getNombreAgente()!=null){
//									nombreAgente=polizaVO.getNombreAgente()+" ";}
//								if(polizaVO.getPateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
//								if(polizaVO.getMateAgente()!=null){
//									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
//								if (polizaVO.getClavAgente().equals("52017")) {
//									nombreAgente = "";
//								}
//								cb=pdf.addLabel(cb,67,158,10,nombreAgente,false,1);
//								if(polizaVO.getClavAgente()!=null){
//									cb=pdf.addLabel(cb,67,146,10,polizaVO.getClavAgente(),false,1);}
//								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//									if(polizaVO.getTelComerAgente()!=null){
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelComerAgente(),false,1);
//									}
//									else{
//										cb=pdf.addLabel(cb,260,146,10,polizaVO.getTelPartAgente(),false,1);
//									}
//
//								}
//								cb=pdf.addlineH(cb,20,143,360);
//								if(polizaVO.getDescOficina()!=null){
//									cb=pdf.addLabel(cb,77,134,10,polizaVO.getDescOficina(),false,1);}
////								if(polizaVO.getPoblacionOficina()!=null){
////									cb=pdf.addLabel(cb,350,198,10,polizaVO.getPoblacionOficina(),false,2);}							
//								if(polizaVO.getCalleOficina()!=null){						
//									cb=pdf.addLabel(cb,77,122,10,polizaVO.getCalleOficina(),false,1);}
//								if(polizaVO.getCodPostalOficina()!=null){										
//									cb=pdf.addLabel(cb,330,122,10,polizaVO.getCodPostalOficina(),false,1);}
//								if(polizaVO.getColoniaOficina()!=null){										
//									cb=pdf.addLabel(cb,77,110,10,polizaVO.getColoniaOficina(),false,1);}
//								if(polizaVO.getTelOficina()!=null){
//									cb=pdf.addLabel(cb,77,98,10,polizaVO.getTelOficina(),false,1);}
//								if(polizaVO.getFaxOficina()!=null){					
//									cb=pdf.addLabel(cb,213,98,10,polizaVO.getFaxOficina(),false,1);}
//							}
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,386,182,199,26);//seccion 6
//							cb=pdf.addLabel(cb,388,172,10,"CONDICIONES VIGENTES",false,1);
//							cb=pdf.addLabel(cb,388,160,10,"TARIFA APLICADA",false,1);
//							
//							
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,605,172,10,polizaVO.getDescConVig(),false,2);
//							}
//							
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									cb=pdf.addLabel(cb,570,160,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									cb=pdf.addLabel(cb,570,160,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//							}	
//							
//							
//							
//							if(polizaVO != null){
//								String lugar="";
//								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//								cb=pdf.addLabel(cb,490,146,10,lugar,false,0);																
//								if(polizaVO.getFchEmi()!=null){
//									cb=pdf.addLabel(cb,490,134,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							}
//
//							if(polizaVO.getDirImagen()!=null){
//								document=pdf.addImage(document,438,105,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							}
//							cb=pdf.addLabel(cb,490,98,10,"FUNCIONARIO AUTORIZADO",false,0);	
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,94,562,71);//seccion 7
//							cb=pdf.addLabel(cb,27,84,10,"Quálitas Compañía de Seguros, S.A de C.V. (en lo sucesivo La Compañía), asegura de aucerdo a las Condiciones",false,1);
//							cb=pdf.addLabel(cb,27,72,10,"Generales y Especiales de esta Póliza, el vehiculo asegurado contra pérdidas o daños causados por cualquiera de los",false,1);
//							cb=pdf.addLabel(cb,27,60,10,"Riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							
							
							
							
							document.newPage();
							
							
							
							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
//								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
								int anioFormato = 0;
								int mesFormato = 0;  
								int diaFormato = 0;  
								
								anioFormato = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
								mesFormato = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
								diaFormato = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
								
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								if((diaFormato<18)&&(mesFormato<=01)&&(anioFormato <=2017) ){
									cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								}else{
									cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 Ciudad de México",false,1);
								}
								
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 1/1",true,1);							

							cb=pdf.addLabel(cb,100,708,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",false,1);
							cb=pdf.addLabel(cb,400,708,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,708,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,530,708,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,390,696,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,465,696,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,535,696,10,incisoAux,false,1);
								}
							}


							//**********CUERPO		
							//cb=pdf.addRectAngColor(cb,23,661,562,12);
							//cb=pdf.addRectAng(cb,23,648,562,43);
							cb=pdf.addRectAngColor(cb,23,684,562,12);
							cb=pdf.addRectAng(cb,23,671,562,73);


							//***********asegurado					
							cb=pdf.addLabel(cb,290,674,10,"INFORMACIÓN DEL ASEGURADO",true,0);
//							cb=pdf.addLabel(cb,27,654,10,"Nombre:",false,1);
//							cb=pdf.addLabel(cb,27,639,10,"RFC:",false,1);
//							cb=pdf.addLabel(cb,27,624,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,27,609,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,167,609,10,"Estado:",false,1);
							cb=pdf.addLabel(cb,27,612,10,"Vigencia           Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,330,612,10,"Hasta las 12:00 P.M. del:",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,27,652,10,nombre,false,1);
								
												
									//la información siguiente va de descripción del vehiculo a tipo de carga							
									if(polizaVO.getAmis()!=null){
										cb=pdf.addLabel(cb,27,632,10,polizaVO.getAmis(),false,1);}							
									if(polizaVO.getDescVehi()!=null){
										cb=pdf.addLabel(cb,57,632,10,polizaVO.getDescVehi(),false,1);}	
									
									if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,225,612,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
									}
									if(polizaVO.getFchFin()!=null){
										cb=pdf.addLabel(cb,460,612,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

									}

//									if(polizaVO.getTipo()!=null){
//
//										if(polizaVO.getTipo().length()>18){
//											//System.out.println("TIPO: "+polizaVO.getTipo());	
//											cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo().substring(0, 19),false,1);
//										}else{
//											//System.out.println("TIPO: "+polizaVO.getTipo());	
//											cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo(),false,1); 
//										}	
//									}
								
								
								
//								if(datosCliente){									
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,72,639,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,72,624,10,calle,false,1);}
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,72,609,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,202,609,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//
//								}						

							}				
							

							
							cb=pdf.addRectAng(cb,23,590,562,396);
							cb=pdf.addRectAngColor(cb,23,590,562,13);

							cb=pdf.addLabel(cb,210,580,10,"INFORMACIÓN IMPORTANTE",true,1);

							cb=pdf.addLabel(cb,27,550,10,"Estimado Asegurado con la finalidad de que conozca los alcances, exclusiones y restricciones con que cuenta el seguro",false,1);
							cb=pdf.addLabel(cb,27,538,10,"de  automóvil que acaba  de adquirir,  Quálitas  Compañia de Seguros,  lo invita a  que lea sus  Condiciones   Generales",false,1);
							cb=pdf.addLabel(cb,27,526,10,"mismas     que    se     adjuntan    a    esta    póliza,    o    bien,   puede    usted    ingresar    a     nuestra    página     Web.",false,1);
							cb=pdf.addLabel(cb,27,514,10,"https://www.qualitas.com.mx/portal/web/qualitas/condiciones-generales",true,1);
							cb=pdf.addLabel(cb,375,514,10,"o en el RECAS.",false,1);
							cb=pdf.addLabel(cb,27,502,10,"",false,1);
							cb=pdf.addLabel(cb,27,490,10,"Usted  puede consultar el folleto que contiene los Derechos de los Asegurados, Contratantes y Beneficiarios en  nuestra",false,1);
							cb=pdf.addLabel(cb,27,478,10,"página de internet (www.qualitas.com.mx).",false,1);
//							cb=pdf.addlineH(cb,27,464,343);
							cb=pdf.addLabel(cb,27,466,10,"",false,1);
							cb=pdf.addLabel(cb,27,454,10,"Artículo  25 de la  Ley sobre el  Contrato de Seguro.  Si el contenido de  la póliza o  sus modificaciones no  concordaren",false,1);							
							cb=pdf.addLabel(cb,27,442,10,"con  la  oferta,  el  Asegurado podrá  pedir la  rectificación   correspondiente  dentro de  los treinta  (30)  días  que  sigan",false,1);
							cb=pdf.addLabel(cb,27,430,10,"al  día en que  reciba su póliza,  transcurrido ese  plazo se considerán  aceptadas las  estipulaciones  de  la póliza o  de",false,1);
							cb=pdf.addLabel(cb,27,418,10,"sus modificaciones.",false,1);							
							cb=pdf.addLabel(cb,27,406,10,"",false,1);
							cb=pdf.addLabel(cb,27,394,10,"Nuestra Unidad Especializada de Atención a  Usuario (UNE) con  siguiente domicilio en: Boulevard Picacho Ajusco 236",false,1);
							
							int anioFormato = 0;
							int mesFormato = 0;  
							int diaFormato = 0;  
							
							mesFormato = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
							diaFormato = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
							anioFormato = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
							
							if((diaFormato<18)&&(mesFormato<=01)&&(anioFormato <=2017)){
								cb=pdf.addLabel(cb,27,382,10,"Colonia  Jardines de la  Montaña, Delegación Tlalpan,  México, Distrito Federal  C.P. 14210, horario de  atención",false,1);
							}else{
								cb=pdf.addLabel(cb,27,382,10,"Colonia   Jardines   de   la  Montaña,  Delegación   Tlalpan,  Ciudad   de   México,  C.P.  14210,   horario   de   atención",false,1);
							}							
							cb=pdf.addLabel(cb,27,370,10,"de  Lunes a Viernes de 9:00 a.m. a 6:00 p.m.,  teléfono 01 (55) 5002 5500,  correo electrónico:  uauf@quialitas.com.mx",false,1);
							cb=pdf.addLabel(cb,27,358,10,"",false,1);
							cb=pdf.addLabel(cb,27,346,10,"Comisión  Nacional  para  la Protección  y Defensa  de los  Usuarios  de Servicios  Financieros (CONDUSEF),  Avenida",false,1);
							
							if((diaFormato<18)&&(mesFormato<=01)&&(anioFormato <=2017)){
								cb=pdf.addLabel(cb,27,334,10,"Insurgentes  Sur #762,  Colonia del  Valle, México,  Distrito Federal,  C.P. 03100.  Teléfono  01 (55) 5340 0999 y",false,1);
							}else{
								cb=pdf.addLabel(cb,27,334,10,"Insurgentes   Sur    #762,   Colonia    del    Valle,   Ciudad   de   México,   C.P.  03100.   Teléfono  01  (55) 5340 0999  y",false,1);
							}						
							cb=pdf.addLabel(cb,27,322,10,"01  (800)  999  80  80.    Página     Web     www.condusef.gob.mx;     correo     electrónico    asesoría.condusef.gob.mx",false,1);
							cb=pdf.addLabel(cb,27,310,10,"",false,1);
							cb=pdf.addLabel(cb,27,298,10,"Quálitas  Compañía de Seguros, S.A.  de C.V.  (en lo  sucesivo La  Compañía), asegura  de acuerdo a las Condiciones",false,1);							
							cb=pdf.addLabel(cb,27,286,10,"Generales  y  Especiales  de  esta  Póliza  el  vehículo  contra   pérdidas  o  daños   causados   por  cualquiera   de  los",false,1);
							cb=pdf.addLabel(cb,27,274,10,"riesgos que se enumeran y que el Asegurado haya contratado, en testimonio de lo cual, La Compañía firma la presente.",false,1);
							cb=pdf.addLabel(cb,27,262,10,"",false,1);
							cb=pdf.addLabel(cb,27,250,10,"Póliza de Seguro Registrada en el RECAS con el número.",false,1);
							cb=pdf.addLabel(cb,27,238,10,"",false,1);
							cb=pdf.addLabel(cb,27,226,10,"Consulta de Significado de Abreviaturas en nuestra página Web: www.qualitas.com.mx",false,1);
														
							cb=pdf.addRectAng(cb,23,194,562,98);//seccion 5
							cb=pdf.addRectAngColor(cb,23,194,562,12);//seccion 5
							
							cb=pdf.addLabel(cb,300,184,10,"OFICINA DE SERVICIO",true,1);
//							cb=pdf.addLabel(cb,27,146,10,"Número:",false,1);
//							cb=pdf.addLabel(cb,215,146,10,"Teléfono",false,1);
							cb=pdf.addLabel(cb,27,170,10,"Oficina:",false,1);
//							cb=pdf.addLabel(cb,400,170,10,"Ciudad de México",false,1);
							
							cb=pdf.addLabel(cb,27,158,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,400,158,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,27,146,10,"Colonia:",false,1);
							cb=pdf.addLabel(cb,27,134,10,"Teléfono:",false,1);
							cb=pdf.addLabel(cb,400,134,10,"Fax:",false,1);
							cb=pdf.addLabel(cb,27,122,10,"De Lunes a Viernes de 8:30 a.m. a 6:30 p.m.",false,1);
							cb=pdf.addLabel(cb,27,110,10,"Canal de Venta",false,1);
							cb=pdf.addLabel(cb,300,110,10,"Teléfono:",false,1);
							cb=pdf.addLabel(cb,27,98,10,"Agente:",false,1);
							
							
							
							
							if(polizaVO!= null){
								//la siguiente información va del nombre del agente a telefono nacional
								String nombreAgente="";
								if(polizaVO.getNombreAgente()!=null){
									nombreAgente=polizaVO.getNombreAgente()+" ";}
								if(polizaVO.getPateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
								if(polizaVO.getMateAgente()!=null){
									nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
								if (polizaVO.getClavAgente().equals("52017")) {
									nombreAgente = "";
								}
								//Muestra Estado de Oficina de Servicio
								if(polizaVO.getEstado()!=null){										
									cb=pdf.addLabel(cb,400,170,10,polizaVO.getEstado(),false,1);}

								if (nombreAgente.length()>72){
								cb=pdf.addLabel(cb,130,98,10,nombreAgente.substring(0, 72),false,1);
								}else{
									cb=pdf.addLabel(cb,130,98,10,nombreAgente,false,1);
								}
								
								
								
								
								
								//cb=pdf.addLabel(cb,130,158,10,nombreAgente,false,1);
								if(polizaVO.getClavAgente()!=null){
									cb=pdf.addLabel(cb,67,98,10,polizaVO.getClavAgente(),false,1);}
								if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
									if(polizaVO.getTelComerAgente()!=null){
										cb=pdf.addLabel(cb,347,110,10,polizaVO.getTelComerAgente(),false,1);
									}
									else{
										cb=pdf.addLabel(cb,347,110,10,polizaVO.getTelPartAgente(),false,1);
									}

								}
								cb=pdf.addlineH(cb,20,119,563);
								if(polizaVO.getDescOficina()!=null){
									cb=pdf.addLabel(cb,77,170,10,polizaVO.getDescOficina(),false,1);}
//								if(polizaVO.getPoblacionOficina()!=null){
//									cb=pdf.addLabel(cb,350,198,10,polizaVO.getPoblacionOficina(),false,2);}							
								if(polizaVO.getCalleOficina()!=null){						
									cb=pdf.addLabel(cb,77,158,10,polizaVO.getCalleOficina(),false,1);}
								if(polizaVO.getCodPostalOficina()!=null){
									cb=pdf.addLabel(cb,435,158,10,polizaVO.getCodPostalOficina(),false,1);}
								if(polizaVO.getColoniaOficina()!=null){										
									cb=pdf.addLabel(cb,77,146,10,polizaVO.getColoniaOficina(),false,1);}
								if(polizaVO.getTelOficina()!=null){
									cb=pdf.addLabel(cb,77,134,10,polizaVO.getTelOficina(),false,1);}
								
								if(polizaVO.getFaxOficina()!=null){
									if(polizaVO.getFaxOficina().length() > 28){
										cb=pdf.addLabel(cb,440,134,10,polizaVO.getFaxOficina().substring(0, 27),false,1);
									}else{
										cb=pdf.addLabel(cb,440,134,10,polizaVO.getFaxOficina(),false,1);
									}								
								}
								
								
							}

//							cb=pdf.addRectAng(cb,386,182,199,26);//seccion 6
//							cb=pdf.addLabel(cb,388,172,10,"CONDICIONES VIGENTES",false,1);
//							cb=pdf.addLabel(cb,388,160,10,"TARIFA APLICADA",false,1);
							
							
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,605,172,10,polizaVO.getDescConVig(),false,2);
//							}
							
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									cb=pdf.addLabel(cb,570,175,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									cb=pdf.addLabel(cb,570,175,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//							}	
							
							
							
//							if(polizaVO != null){
//								String lugar="";
//								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//								cb=pdf.addLabel(cb,490,161,10,lugar,false,0);																
//								if(polizaVO.getFchEmi()!=null){
//									cb=pdf.addLabel(cb,490,149,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							}
//
//							if(polizaVO.getDirImagen()!=null){
//								document=pdf.addImage(document,438,115,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							}
//							cb=pdf.addLabel(cb,490,100,10,"FUNCIONARIO AUTORIZADO",false,0);	
							
							
							
							
							
							cb=pdf.addRectAng(cb,23,94,562,65);//seccion 7
							cb=pdf.addLabel(cb,27,81,12,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,",false,1);
							cb=pdf.addLabel(cb,27,67,12,"la documentación contractual y la nota técnica que integran este producto de seguro, quedaron",false,1);
							cb=pdf.addLabel(cb,27,53,12,"registrados ante la Comisión Nacional de Seguros y Fianzas a partir del día ",false,1);
							cb=pdf.addLabel(cb,27,39,12,"20 de Julio de 2015 con el número CNSF-S0046-3135-1005",false,1);
							
							
							
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							}
							
							
							
							
							
							
//							document.newPage();
//
//							
//							
//
//							
//							
//							
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 2/1",true,1);
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,717,562,46);//seccion 1
//							cb=pdf.addRectAng(cb,348,707,237,24);//seccion 1
//							
//							cb=pdf.addLabel(cb,60,695,10,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//							cb=pdf.addLabel(cb,355,695,10,"POLIZA",true,1);
//							cb=pdf.addLabel(cb,435,695,10,"ENDOSO",true,1);
//							cb=pdf.addLabel(cb,525,695,10,"INCISO",true,1);
//							
//							if(polizaVO != null){
//								String inciso="000";
//								String incisoAux;
//								if(polizaVO.getNumPoliza()!=null){
//									sizeNumPoliza=polizaVO.getNumPoliza().length();
//									cb=pdf.addLabel(cb,355,685,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//									cb=pdf.addLabel(cb,435,685,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//								}
//								if(polizaVO.getInciso()!=null){
//									inciso = inciso+polizaVO.getInciso();								
//									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//									cb=pdf.addLabel(cb,525,685,10,incisoAux,false,1);
//								}
//							}
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,669,562,77);//seccion 2
//							cb=pdf.addRectAng(cb,401,657,184,14);//seccion 2
//							cb=pdf.addRectAng(cb,401,618,184,26);//seccion 2
//							cb=pdf.addLabel(cb,27,658,10,"DATOS DEL CONTRATANTE:",true,1);
//							cb=pdf.addLabel(cb,250,658,10,"RFC:",true,1);
//							cb=pdf.addLabel(cb,401,658,10,"RENUEVA A:",false,1);
//							cb=pdf.addLabel(cb,435,646,10,"VIGENCIA:",true,1);
//							cb=pdf.addLabel(cb,27,630,10,"Domicilio:",false,1);
//							cb=pdf.addLabel(cb,401,633,10,"Desde las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,401,621,10,"Hasta las 12 horas del",false,1);
//							cb=pdf.addLabel(cb,27,616,10,"C.P.:",false,1);
//							cb=pdf.addLabel(cb,220,616,10,"Estado:",false,1);
//							cb=pdf.addLabel(cb,27,602,10,"Beneficiario Preferente:",false,1);
//							cb=pdf.addLabel(cb,410,606,10,"Fecha de vencimiento del pago",false,1);
//							if(polizaVO.getPolizaAnterior()!=null){
//								cb=pdf.addLabel(cb,472,658,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}
//							cb=pdf.addLabel(cb,472,658,10,"RENUEVAxxx",false,1);	
//							if(polizaVO != null){		
//								if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,511,633,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//								}
//								if(polizaVO.getFchFin()!=null){
//									cb=pdf.addLabel(cb,511,621,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//								}
//								if(polizaVO.getFechaLimPago()!=null){																												
//									cb=pdf.addLabel(cb,450,594,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//							}
//							
//							
//							if(polizaVO != null){
//								String nombre=""; 
//								if(polizaVO.getNombre()!=null){
//									nombre=polizaVO.getNombre()+" ";}
//								if(polizaVO.getApePate()!=null){
//									nombre=nombre+polizaVO.getApePate()+" ";}
//								if(polizaVO.getApeMate()!=null){
//									nombre=nombre+polizaVO.getApeMate()+" ";}
//
//								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//									nombre=nombre+" Y/O "+polizaVO.getConductor();
//								}	
//								cb=pdf.addLabel(cb,27,646,10,nombre,false,1);
//								if(datosCliente){										
//									if(polizaVO.getCalle()!=null){
//										String calle= polizaVO.getCalle();
//										if(polizaVO.getExterior()!= null){
//											calle += " No. EXT. " + polizaVO.getExterior();
//										}
//										if(polizaVO.getInterior()!= null){
//											calle += " No. INT. " + polizaVO.getInterior();
//										}
//										if(polizaVO.getColonia()!=null){
//											calle += " COL. " + polizaVO.getColonia();
//										}
//										cb=pdf.addLabel(cb,75,630,10,calle,false,1);}
//
//									if(polizaVO.getCodPostal()!=null){
//										cb=pdf.addLabel(cb,55,616,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//									if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//										cb=pdf.addLabel(cb,260,616,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//									if(polizaVO.getRfc()!=null){
//										cb=pdf.addLabel(cb,280,658,10,polizaVO.getRfc(),false,1);}
//									if(polizaVO.getBeneficiario()!=null){
//										cb=pdf.addLabel(cb,135,602,10,polizaVO.getBeneficiario(),false,1);}
//									cb=pdf.addLabel(cb,135,602,10,"PRUEBA BENEFICIARIO PREFERENTE XXX",false,1);
//
////									for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
////										LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
////										if(campo.getLabel().length()>13)
////											cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
////										else
////											cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
////										j=j+50;
////										if(campo.getValue().length()>13)
////											cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
////										else
////											cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
////										j=j+50;
////									}
//								}						
//
//							}				
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,590,148,33);//seccion 3
//							cb=pdf.addRectAng(cb,172,590,237,33);//seccion 3
//							cb=pdf.addRectAng(cb,410,590,172,33);//seccion 3
//							
//							
//							
//							
//							
//							cb=pdf.addRectAng(cb,23,556,562,62);//seccion 4
//							cb=pdf.addRectAng(cb,23,556,562,13);//seccion 4
//							cb=pdf.addLabel(cb,215,546,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,1);
//							cb=pdf.addLabel(cb,27,533,10,"Clave y Marca",false,1);
//							cb=pdf.addLabel(cb,27,522,10,"Tipo:",false,1);
//							cb=pdf.addLabel(cb,190,522,10,"Modelo:",false,1);
//							cb=pdf.addLabel(cb,320,522,10,"Color:",false,1);
//							cb=pdf.addLabel(cb,450,522,10,"Ocupantes:",false,1);
//							cb=pdf.addLabel(cb,27,510,10,"Serie:",false,1);
//							cb=pdf.addLabel(cb,190,510,10,"Motor:",false,1);
//							cb=pdf.addLabel(cb,390,510,10,"Placas:",false,1);
//
//							if(polizaVO != null){							
//								if(polizaVO.getAmis()!=null){
//									cb=pdf.addLabel(cb,97,533,10,polizaVO.getAmis(),false,1);}							
//								if(polizaVO.getDescVehi()!=null){
//									cb=pdf.addLabel(cb,127,533,10,polizaVO.getDescVehi(),false,1);}											
//								if(polizaVO.getTipo()!=null){
//									if(polizaVO.getTipo().length()>18){	
//										cb=pdf.addLabel(cb,57,522,10,polizaVO.getTipo().substring(0, 19),false,1);
//									}else{
//										cb=pdf.addLabel(cb,57,522,10,polizaVO.getTipo(),false,1); 
//									}	
//								}
//								if(polizaVO.getVehiAnio()!=null){												
//									cb=pdf.addLabel(cb,230,522,10,polizaVO.getVehiAnio(),false,1);}
//								if(polizaVO.getColor()!=null){
//
//									if(polizaVO.getColor().equals("SIN COLOR")){
//
//										cb=pdf.addLabel(cb,350,522,10,"",false,1);		
//									}else{
//										cb=pdf.addLabel(cb,350,522,10,polizaVO.getColor(),false,1);
//									}
//								}
//								//ANDRES-PASAJEROS
//								if (polizaVO.getNumPasajeros()!=null){
//									cb=pdf.addLabel(cb,507,522,10,polizaVO.getNumPasajeros(),false,1);
//								}
//								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,507,522,10,polizaVO.getNumOcupantes(),false,1);
//								}
//								if(polizaVO.getNumPlaca()!=null){												
//									cb=pdf.addLabel(cb,430,510,10,polizaVO.getNumPlaca(),false,1);}
//								if(polizaVO.getNumSerie()!=null){					
//									cb=pdf.addLabel(cb,60,510,10,polizaVO.getNumSerie(),false,1);}
//								if(polizaVO.getNumMotor()!=null){					
//									cb=pdf.addLabel(cb,224,510,10,polizaVO.getNumMotor(),false,1);}
//								
//								if (polizaVO.getCveServ().trim().equals("3")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,498,10,"Tipo de Carga",false,1);
//										cb=pdf.addLabel(cb,110,498,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//										cb=pdf.addLabel(cb,200,498,10,polizaVO.getTipoCarga()+" : ",true,2);}
//									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//										String descAux = "";
//										String valorRemolque="";
//										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//											descAux=polizaVO.getDescCarga();
//										}
//										
//										if(polizaVO.getDobleRemolque()!= null){
//											if(polizaVO.getDobleRemolque().equals("S")){
//												valorRemolque = "2° Remolque: AMPARADO";
//											}else{
//												valorRemolque = "2° Remolque: EXCLUIDO";
//											}
//										}								
//										if(descAux != "" || valorRemolque != ""){
//											cb=pdf.addLabel(cb,260,498,10,descAux+"  "+valorRemolque,false,1);
//										}
//										
//									}
//								}
//								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//										cb=pdf.addLabel(cb,27,498,10,"Tipo de Carga "+polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//									}
//								}
//								if(polizaVO.getNoEconomico()!=null){
//									cb=pdf.addLabel(cb,390,498,10,"No. Económico: "+polizaVO.getNoEconomico(),false,1);
//								}
//							}
//							
//						
							
					for (int l=0;l<=1;l++){								
							document.newPage();
//							cb=pdf.addLabel(cb,80,730,14,"HOJA 3/1",true,1);		
							
							if (membretado==null||membretado.equals("S")){
								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
							}
							//ANDRES-MEM
							if (membretado==null||membretado.equals("S")){
//								int anioFormato = 0;
								
								mesFormato = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
								diaFormato = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
								anioFormato = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
								if((diaFormato<18)&&(mesFormato<=01)&&(anioFormato <=2017)){
									cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
								}else{
									cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 Ciudad de México",false,1);
								}								
								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
							}
							
							
							
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addRectAngColorGreenWater(cb,23,718,562,30);
//								cb=pdf.addRectAngColorPurple(cb,388,716,175,25);
//								document=pdf.addImage(document,35,725,130,50,polizaVO.getDirImagen()+"images/logoQpoliza.jpg");
//							}
							
							
							if (polizaVO.getPlan()!=null){
								cb=pdf.addLabel(cb,390,725,10,"PLAN: "+polizaVO.getPlan(),false,1);
							}
							
							
							
							cb=pdf.addLabel(cb,100,705,10,"PÓLIZA DE SEGURO DE AUTOMÓVILES",true,1);										
							cb=pdf.addLabel(cb,400,705,10,"PÓLIZA",false,1);
							cb=pdf.addLabel(cb,460,705,10,"ENDOSO",false,1);
							cb=pdf.addLabel(cb,530,705,10,"INCISO",false,1);
							if(polizaVO != null){
								//el orden de los datos siguientes va de poliza a inciso
								String inciso="000";
								String incisoAux;

								if(polizaVO.getNumPoliza()!=null){
									sizeNumPoliza=polizaVO.getNumPoliza().length();
									cb=pdf.addLabel(cb,390,695,10,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);

									cb=pdf.addLabel(cb,465,695,10,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
								}

								if(polizaVO.getInciso()!=null){
									inciso = inciso+polizaVO.getInciso();								
									incisoAux =inciso.substring(inciso.length()-4,inciso.length());
									cb=pdf.addLabel(cb,535,695,10,incisoAux,false,1);
								}
							}


							//**********CUERPO			
							//cb=pdf.addRectAngColor(cb,23,684,562,12);
							//cb=pdf.addRectAng(cb,23,671,562,73);
							
							cb=pdf.addRectAngColor(cb,23,691,562,12);
							cb=pdf.addRectAng(cb,23,678,562,60);


							//***********asegurado					
							cb=pdf.addLabel(cb,290,681,10,"INFORMACIÓN DEL ASEGURADO",true,0);				
							cb=pdf.addLabel(cb,440,656,10,"R.F.C.:",false,1);
							cb=pdf.addLabel(cb,440,668,10,"RENUEVA A:",false,1);
							cb=pdf.addLabel(cb,30,656,10,"Domicilio:",false,1);
							cb=pdf.addLabel(cb,30,644,10,"C.P.:",false,1);
							cb=pdf.addLabel(cb,100,644,10,"Municipio:",false,1);
							cb=pdf.addLabel(cb,250,644,10,"Estado:",false,1);
							cb=pdf.addLabel(cb,398,644,10,"Colonia:",false,1);
							
							//cb=pdf.addLabel(cb,40,632,10,"Beneficiario Preferente:",false,1);	
							if(polizaVO.getPolizaAnterior()!=null){
								cb=pdf.addLabel(cb,510,668,10,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
							if(polizaVO != null){
								//el orden de los datos siguientes va del nombre del asegurado a beneficiario
								String nombre=""; 
								if(polizaVO.getNombre()!=null){
									nombre=polizaVO.getNombre()+" ";}
								if(polizaVO.getApePate()!=null){
									nombre=nombre+polizaVO.getApePate()+" ";}
								if(polizaVO.getApeMate()!=null){
									nombre=nombre+polizaVO.getApeMate()+" ";}

								if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
									nombre=nombre+" Y/O "+polizaVO.getConductor();
								}	

								cb=pdf.addLabel(cb,30,668,10,nombre,false,1);
								if(datosCliente){									
									cb=pdf.addLabel(cb,490,670,10,"  ",false,1);	

									
								if (l==0){
									if(polizaVO.getCalle()!=null){
										String calle= polizaVO.getCalle();
										if(polizaVO.getExterior()!= null){
											calle += " No. EXT. " + polizaVO.getExterior();
										}
										if(polizaVO.getInterior()!= null){
											calle += " No. INT. " + polizaVO.getInterior();
										}
										//ANDRES-prueba 
										//System.out.println("colonia:::"+polizaVO.getColonia());
//											if(polizaVO.getColonia()!=null){
//												calle += " COL. " + polizaVO.getColonia();
//											}
											cb=pdf.addLabel(cb,80,656,10,calle,false,1);
																																	
											if(polizaVO.getCodPostal()!=null){
												cb=pdf.addLabel(cb,55,644,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
											if(polizaVO.getMunicipio()!=null){
												int sizeMunicipio;
												sizeMunicipio=polizaVO.getMunicipio().length();
												if(sizeMunicipio>=16){
													cb=pdf.addLabel(cb,150,644,10,polizaVO.getMunicipio().substring(0, 14),false,1);
												}else{
													cb=pdf.addLabel(cb,150,644,10,polizaVO.getMunicipio(),false,1);
												}
											}
											if(polizaVO.getEstado()!=null){
												int sizeEstado;
												sizeEstado=polizaVO.getEstado().length();
												if (sizeEstado>=19) {
													cb=pdf.addLabel(cb,288,644,10,polizaVO.getEstado().substring(0, 17),false,1);
												}else{
													cb=pdf.addLabel(cb,288,644,10,polizaVO.getEstado(),false,1);
												}
											}
												
											if(polizaVO.getColonia()!=null){
												
												int sizeColonia;
												sizeColonia=polizaVO.getColonia().length();
												if (sizeColonia>=21) {
													cb=pdf.addLabel(cb,438,644,10,polizaVO.getColonia().substring(0, 19),false,1);
												}else{
													
													cb=pdf.addLabel(cb,438,644,10,polizaVO.getColonia(),false,1);
													}
												}
//											if(polizaVO.getColonia()!=null){
//												int sizeColonia;
//												sizeColonia=polizaVO.getColonia().length();
//												if(sizeColonia<22){
//												cb=pdf.addLabel(cb,435,644,10,polizaVO.getColonia().substring(0, 21),false,1);}
//												}else{
//													cb=pdf.addLabel(cb,435,644,10,polizaVO.getColonia(),false,1);
//												}
											if(polizaVO.getRfc()!=null){
												cb=pdf.addLabel(cb,475,656,10,polizaVO.getRfc(),false,1);}
											
											if(polizaVO.getBeneficiario()!=null){
												if(polizaVO.getBeneficiario().length()>1){
													cb=pdf.addLabel(cb,30,632,10,"BENEFICIARIO PREFERENTE "+polizaVO.getBeneficiario(),false,1);
												}
											}
											
											//Código Respaldo Anterior
//											if(polizaVO.getCodPostal()!=null){
//												cb=pdf.addLabel(cb,70,644,10,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//											if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//												cb=pdf.addLabel(cb,130,644,10,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//											if(polizaVO.getRfc()!=null){
//												cb=pdf.addLabel(cb,390,644,10,polizaVO.getRfc(),false,1);}
//											
//											if(polizaVO.getBeneficiario()!=null){
//												if(polizaVO.getBeneficiario().length()>1){
//													cb=pdf.addLabel(cb,40,632,10,"BENEFICIARIO PREFERENTE "+polizaVO.getBeneficiario(),false,1);
//												}
//											}
											
										}
								}
										
										
//									for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
//										LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
//										if(campo.getLabel().length()>13)
//											cb=pdf.addLabel(cb,j,630,10,campo.getLabel().substring(0,13),false,1);
//										else
//											cb=pdf.addLabel(cb,j,630,10,campo.getLabel(),false,1);
//										j=j+50;
//										if(campo.getValue().length()>13)
//											cb=pdf.addLabel(cb,j,630,10,campo.getValue().substring(0,13),false,1);
//										else
//											cb=pdf.addLabel(cb,j,630,10,campo.getValue(),false,1);
//										j=j+50;
//									}
								}
//								if(polizaVO.getCveApoderado()!=null){
//									cb=pdf.addLabel(cb,40,630,10,"APODERADO",false,1);
//									cb=pdf.addLabel(cb,100,630,10,polizaVO.getCveApoderado(),false,1);
//								}							

							}				


							//*************Vehiculo
							cb=pdf.addRectAngColor(cb,23,616,562,12);
							cb=pdf.addRectAng(cb,23,616,562,65);

							cb=pdf.addLabel(cb,290,607,10,"DESCRIPCIÓN DEL VEHÍCULO ASEGURADO",true,0);
							cb=pdf.addLabel(cb,40,580,10,"Tipo:",false,1);
							cb=pdf.addLabel(cb,210,580,10,"Modelo:",false,1);
							cb=pdf.addLabel(cb,320,580,10,"Color:",false,1);
							cb=pdf.addLabel(cb,40,567,10,"Serie:",false,1);
							cb=pdf.addLabel(cb,210,567,10,"Motor:",false,1);
							cb=pdf.addLabel(cb,358,567,10,"REPUVE:",false,1);
							cb=pdf.addLabel(cb,490,567,10,"Placas:",false,1);
							if(polizaVO != null){
								//la información siguiente va de descripción del vehiculo a tipo de carga							
								if(polizaVO.getAmis()!=null){
									cb=pdf.addLabel(cb,40,595,10,polizaVO.getAmis(),false,1);}							
								if(polizaVO.getDescVehi()!=null){
									cb=pdf.addLabel(cb,70,595,10,polizaVO.getDescVehi(),false,1);}											

								if(polizaVO.getTipo()!=null){

									if(polizaVO.getTipo().length()>18){
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo().substring(0, 19),false,1);
									}else{
										//System.out.println("TIPO: "+polizaVO.getTipo());	
										cb=pdf.addLabel(cb,70,580,10,polizaVO.getTipo(),false,1); 
									}	
								}

								if(polizaVO.getVehiAnio()!=null){												
									cb=pdf.addLabel(cb,267,580,10,polizaVO.getVehiAnio(),false,1);}
								if(polizaVO.getColor()!=null){

									if(polizaVO.getColor().equals("SIN COLOR")){

										cb=pdf.addLabel(cb,358,580,10,"",false,1);		
									}else{
										cb=pdf.addLabel(cb,358,580,10,polizaVO.getColor(),false,1);
										//cb=pdf.addLabel(cb,428,580,10,polizaVO.getColor().length()>7?polizaVO.getColor().substring(0,7):polizaVO.getColor(),false,1);
										
									}
								}
								//ANDRES-PASAJEROS
								if (polizaVO.getNumPasajeros()!=null){
									cb=pdf.addLabel(cb,490,580,10,polizaVO.getNumPasajeros(),false,1);
								}
								else if(polizaVO.getNumOcupantes()!=null){
//									cb=pdf.addLabel(cb,490,580,10,"OCUP.",false,1);
									cb=pdf.addLabel(cb,490,580,10,"Ocupantes:",false,1);
									cb=pdf.addLabel(cb,550,580,10,polizaVO.getNumOcupantes(),false,1);
								}

								if(polizaVO.getNumPlaca()!=null){												
									cb=pdf.addLabel(cb,530,567,10,polizaVO.getNumPlaca(),false,1);}
								if(polizaVO.getNumSerie()!=null){					
									cb=pdf.addLabel(cb,73,567,10,polizaVO.getNumSerie(),false,1);}
								if(polizaVO.getNumMotor()!=null){					
									cb=pdf.addLabel(cb,250,567,10,polizaVO.getNumMotor(),false,1);}
								if(polizaVO.getRenave()!=null){					
									cb=pdf.addLabel(cb,398,567,10,polizaVO.getRenave(),false,1);}
								
								if (polizaVO.getCveServ().trim().equals("3")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,"'"+polizaVO.getClaveCarga()+"'",false,1);}
									if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
										cb=pdf.addLabel(cb,200,554,10,polizaVO.getTipoCarga()+" : ",true,2);}
									//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
									if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
										String descAux = "";
										String valorRemolque="";
										if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
											descAux=polizaVO.getDescCarga();
										}
										
										if(polizaVO.getDobleRemolque()!= null){
											if(polizaVO.getDobleRemolque().equals("S")){
												valorRemolque = "2° Remolque: AMPARADO";
											}else{
												valorRemolque = "2° Remolque: EXCLUIDO";
											}
										}								
										if(descAux != "" || valorRemolque != ""){
											cb=pdf.addLabel(cb,210,554,10,descAux+"  "+valorRemolque,false,1);
										}
										
									}
								}
								
								
								if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
									if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
										cb=pdf.addLabel(cb,40,554,10,"Tipo de carga: ",true,1);
										cb=pdf.addLabel(cb,110,554,10,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
									}
								}
								
								
								if(polizaVO.getNoEconomico()!=null){
									cb=pdf.addLabel(cb,40,557,10,"No.Económico: ",false,1);
									cb=pdf.addLabel(cb,115,557,10,polizaVO.getNoEconomico(),false,1);
								}
								
								
								
								
								
								
								
							}


							//**************vigencia
							//cb=pdf.addRectAng(cb,23,615,562,65);
							
							cb=pdf.addRectAng(cb,23,550,183,45);
							cb=pdf.addRectAng(cb,215,550,181,45);
							cb=pdf.addRectAng(cb,403,550,181,45);


							cb=pdf.addLabel(cb,33,538,10,"VIGENCIA",false,1);
							cb=pdf.addLabel(cb,33,526,10,"Desde las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,33,510,10,"Hasta las 12:00 P.M. del:",false,1);
							cb=pdf.addLabel(cb,217,538,10,"Fecha Vencimiento del pago:",false,1);
							cb=pdf.addLabel(cb,217,510,10,"Plazo de pago:",false,1);
							cb=pdf.addLabel(cb,410,538,10,"Uso:",false,1);
							cb=pdf.addLabel(cb,410,526,10,"Servicio:",false,1);
							cb=pdf.addLabel(cb,410,510,10,"Movimiento:",false,1);
							
							
							if(polizaVO != null){		
								//la información siguiente va desde la fecha de vigencia hasta servicio
								if(polizaVO.getFchIni()!=null){
									cb=pdf.addLabel(cb,147,526,10,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
								}
								if(polizaVO.getFchFin()!=null){
									cb=pdf.addLabel(cb,147,510,10,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);

								}

								if(polizaVO.getFechaLimPago()!=null){																												
									cb=pdf.addLabel(cb,259,526,10,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
								if(polizaVO.getPlazoPago()!=null){
									cb=pdf.addLabel(cb,287,510,10, polizaVO.getPlazoPago()+" dias",false,1);}


								if(polizaVO.getMovimiento()!=null){
									cb=pdf.addLabel(cb,500,510,10,polizaVO.getMovimiento(),false,1);
								}

								if(polizaVO.getUso()!=null){
									ArrayList uso=pdf.trimString(polizaVO.getUso(),17,42,75);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      15 , 21*2 , 25*3
									if(uso!=null){
										if(uso.size()==1){
											cb=pdf.addLabel(cb,525,538,10,(String)uso.get(0),false,0);
										}
//										else if(uso.size()==2){
//											cb=pdf.addLabel(cb,500,540,10,(String)uso.get(0),false,0);
//											cb=pdf.addLabel(cb,500,538,10,(String)uso.get(1),false,0);
//										}	
//										else if(uso.size()==3){
//											cb=pdf.addLabel(cb,415,517,10,(String)uso.get(0),false,0);
//											cb=pdf.addLabel(cb,415,512,10,(String)uso.get(1),false,0);
//											cb=pdf.addLabel(cb,415,507,10,(String)uso.get(2),false,0);
//										}											
									}									
								}
								if(polizaVO.getServicio()!=null){
									ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
									//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
									//caracteres      23 , 31*2 , 37*3
									if(servicio!=null){
										if(servicio.size()==1){
											cb=pdf.addLabel(cb,500,526,10,(String)servicio.get(0),false,1);
										}
//										else if(servicio.size()==2){
//											cb=pdf.addLabel(cb,515,515,10,(String)servicio.get(0),false,0);
//											cb=pdf.addLabel(cb,515,509,10,(String)servicio.get(1),false,0);
//										}
//										else if(servicio.size()==3){
//											cb=pdf.addLabel(cb,515,517,10,(String)servicio.get(0),false,0);
//											cb=pdf.addLabel(cb,515,512,10,(String)servicio.get(1),false,0);
//											cb=pdf.addLabel(cb,515,507,10,(String)servicio.get(2),false,0);
//										}
									}									
								}					
							}


							//*************Datos de Riesgos
							//cb=pdf.addRectAng(cb,23,615,562,65);
							cb=pdf.addRectAngColor(cb,23,503,562,14);	
							cb=pdf.addRectAng(cb,23,486,562,235);

							cb=pdf.addLabel(cb,33,492,10,"COBERTURAS CONTRATADAS",true,1);
							cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
							cb=pdf.addLabel(cb,430,492,10,"DEDUCIBLE",true,1);
							cb=pdf.addLabel(cb,520,492,10,"$     PRIMAS",true,1);

							//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
							//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
							//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
							//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
							double primaAux=0;
							double primaExe=0;
							double derechoAux=0;
							double recargoAux=0;
							double subtotalAux=0;
							double impuestoAux=0;
							boolean exDM=false;
							boolean exRT=false;
							boolean agenEsp1 = false;
							boolean agenEsp2 = false;
							boolean validaAltoRiesgo = false;

							for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
								int temp = (Integer)polizaVO.getAgenteEsp().get(y);
								if(temp==1)
									agenEsp1=true;
								if(temp==AGEN_ESP_OCULTA_PRIMAS)
									agenEsp2=true;
							}
							
							boolean minimos=false;

							
							
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;

								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									if (coberturaVO.getDescrCobertura().trim().equals("CONSIDERACION5035")) {
									polizaVO.getCoberturasArr().remove(x);
									}
								}
							}
							
							if(polizaVO.getCoberturasArr()!=null){
								CoberturasPdfBean coberturaVO;

								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{
									coberturaVO= new CoberturasPdfBean();
									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									if(coberturaVO.getClaveCobertura().equals("12")){
										exDM=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}else if(coberturaVO.getClaveCobertura().equals("40")){
										exRT=true;
										if(coberturaVO.isFlagPrima()){
											primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										}
									}	
								}
								
								String cveServ = polizaVO.getCveServ().trim();
								for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
								{	
									boolean salto=false;
									if(x==toppage+27)
									{break;}

									coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
									String claveCobertura = coberturaVO.getClaveCobertura();

									int deducible = getNumber(coberturaVO.getDeducible());
									int anio = getNumber(polizaVO.getVehiAnio());
									if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
										validaAltoRiesgo = true;
									}

									//ANDRES-MINIMOS
									if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
										minimos=true;
									}
									
									log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
									log.debug("sumaAsegurada"+coberturaVO.getSumaAsegurada());
									
									int diaEmi = 0;
									int mesEmi = 0;
									int anioEmi = 0;
									
									if (polizaVO.getFchEmi() != null && !polizaVO.getFchEmi().isEmpty()) {
										diaEmi = Integer.parseInt(DateFormat.obtenerDia(polizaVO.getFchEmi()));
										mesEmi = Integer.parseInt(DateFormat.obtenerMes(polizaVO.getFchEmi()));
										anioEmi = Integer.parseInt(DateFormat.obtenerAnio(polizaVO.getFchEmi()));
									}
									if ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 9) || (anioEmi >= 2015 && mesEmi >= 8 && diaEmi>=17)){//a partir del 17 agosot 2015 se quitan los numeros de cobertura
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T" ,false,1);//quitar clave cobe
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exe. Ded. x PT, DM Y RT" ,false,1);//quitar clave cobe
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estandarizado" ,false,1);//quitar clave cobe
										}else{
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												
												if (coberturaVO.getClaveCobertura().equals("1")){
														if(polizaVO.getPlan().contains("PLUS")||polizaVO.getPlan().contains("plus")){
															cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Solo Perdida Total" ,false,1);
														}else{
															cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños materiales" ,false,1);
														}													
														/*if (coberturaVO.getDescrCobertura().contains("MATERIALES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños materiales" ,false,1);}
														if (coberturaVO.getDescrCobertura().contains("TOTAL")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Solo Perdida Total" ,false,1);}*/
													}
												else if (coberturaVO.getClaveCobertura().equals("10")){
													if (coberturaVO.getDescrCobertura().contains("OCUPANTES")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Daños a Ocupantes" ,false,1);
													}
													
													if (coberturaVO.getDescrCobertura().contains("EXT")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Extensión de coberturas" ,false,1);
													}
												}
												else if (coberturaVO.getClaveCobertura().equals("11")){	
													if (coberturaVO.getDescrCobertura().contains("DM")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible X PT DM" ,false,1);
													}
													if (coberturaVO.getDescrCobertura().contains("RT")){
														//Si se pinta la cobertura en el PDF, se hace un salto de renglón para la siguiente cobertura
														if (coberturaVO.getDescrCobertura().contains("DM")){
															x++;
														}
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT" ,false,1);
													}
												}
												else if (coberturaVO.getClaveCobertura().equals("12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Pasajero" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo Parcial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Ajuste Automático" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Vial Quálitas",false,1);
													if((diaFormato<18)&&(mesFormato<=01)&&(anioFormato <=2017)){
														cb=pdf.addLabel(cb,23,294 ,9,"Servicios de Asistencia Vial: D.F. y Area Metropolitana: 3300 4534 ; Interior de la República : 01 800 253 0553",false,1);
													}else{
														cb=pdf.addLabel(cb,23,294 ,9,"Servicios de Asistencia Vial: CDMX y Area Metropolitana: 3300 4534 ; Interior de la República : 01 800 253 0553",false,1);
													}													
													}
												else if (coberturaVO.getClaveCobertura().equals("16.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA DM" ,false,1);}				
												else if (coberturaVO.getClaveCobertura().equals("16.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"CIVA RT" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Asistencia Satelital" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("18")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Can. Ded por Colisión o vuelco" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("19")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AVC" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Robo total" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("20")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG EG" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("21")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"PEUG SM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("22")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Muerte del Conductor por Accidente Automovilístico" ,false,1);}
												
												
												else if (coberturaVO.getClaveCobertura().equals("24")){
													if (coberturaVO.getDescrCobertura().contains("RINES")){cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Rines" ,false,1);}
													else {
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Neumáticos" ,false,1);
													}
													}
												else if (coberturaVO.getClaveCobertura().equals("3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil por Daños a Terceros" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.11")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Daños a Terceros EUA" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.12")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Estand" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.13")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.14")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Complementaria Personas" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.15")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"RC Cruzada" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.16")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Maniobras de carga y descarga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.17")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Arrastre de remolque" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Bienes" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Complementaria" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.4")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Daños por carga" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.5")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Responsabilidad Civil Ecologica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("3.19")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Protección para Daños sin Responsabilidad" ,false,1);}		
												else if (coberturaVO.getClaveCobertura().equals("4")){
													if(polizaVO.getCveServ().trim().equals("2")){
													//if(polizaVO.getServicio().trim().equals("2")){
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Médicos Conductor y Familiares" ,false,1);
													}else{
														cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Médicos Ocupantes" ,false,1);
													}													
												}
												else if (coberturaVO.getClaveCobertura().equals("40")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Exención de Deducible RT",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("41")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Avería Mecánica" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("43")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Llantas",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("44")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Rines" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("51")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P T",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("52")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos por Perdida de Uso en P P",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("65")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Auto Sustituto por Pérdida Parcial y Total" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos de Transporte por PT del Vehículo Aseg" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("6.2")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"GTP" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("7")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Gastos Legales" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("8")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Equipo Especial" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Serv.Asist.AccidentePers.enViaje",false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.1")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"Adapt y/o Conversiones DM" ,false,1);}
												else if (coberturaVO.getClaveCobertura().equals("9.3")){
													cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"AdapT y/o Conversiones RT" ,false,1);}
												
												
												if (polizaVO.getLeyendaRCEUA()!=null){
													cb=pdf.addLabel(cb,23,288 ,9,polizaVO.getLeyendaRCEUA(),false,1);
												}
												
												if (polizaVO.getLeyendaUMA()!=null){
													cb=pdf.addLabel(cb,23,280 ,9,polizaVO.getLeyendaUMA(),false,1);
												}
												


												
												
												
												
												
												
												
												
												
												//cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
																								
//												if ( ((anioEmi >= 2016) || (anioEmi >= 2015 && mesEmi >= 10) || (anioEmi >= 2015 && mesEmi >= 9 && diaEmi>=21)) && (polizaVO.getCvePlan().equals("34"))){
//													//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
	//
//												}else{
//													cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getDescrCobertura().substring(ini) ,false,1);
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la página www.qualitas.com.mx para imprimir condiciones generales de la cobertura y certificado ", false, 1);
//												}

																					
										}
									}
									else{//se muestra la numeracion de coberturas
										if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
										}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
											
											salto = true;
										}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
											salto = true;
										}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
											cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
											
										}else{
											//cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
												int longitud= coberturaVO.getDescrCobertura().length();
												int ini=0;
												for (int ind=0;ind<longitud;ind++)
													{
														char car=coberturaVO.getDescrCobertura().charAt(ind);
														if (Character.isLetter(car)){
															ini=ind;
															break;
														}
													}
												;
												cb=pdf.addLabel(cb,23, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
									
//												if ( polizaVO.getCvePlan().equals("34")){
//													cb = pdf.addLabel(cb,35,260,7, "En caso de viaje a EUA y/o Canadá, consultar la pagina www.qualitas.com.mx para imprimir certificado", false, 1);
//												}
										}
									}
									
									
									
									
									
									
									
									

									if(!salto){
										//ANDRES-SUMASEG
										//suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if (coberturaVO.getClaveCobertura().contains("6.2")){
												double dias=0;
												try {
													if (coberturaVO.getSumaAsegurada().contains(",")){
														int indice=0;
														indice=coberturaVO.getSumaAsegurada().indexOf(",");
														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
														dias = Double.parseDouble(sumAseg)/500 ;
													}
													else{
														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
													}
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
												} catch (Exception e) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}				
												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
											}else if(coberturaVO.getClaveCobertura().equals("12")) {
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
												
											}
											else{
												cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
											} 

										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,coberturaVO.getPrima(),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
										//ANDRES-SUMASEG
										//}
										//										suma asegurada														
										if(coberturaVO.isFlagSumaAsegurada()){	
											if(coberturaVO.isFlagSumaAsegurada()){	
												if (coberturaVO.getClaveCobertura().contains("6.2")){
													double dias=0;
													try {
														if (coberturaVO.getSumaAsegurada().contains(",")){
															int indice=0;
															indice=coberturaVO.getSumaAsegurada().indexOf(",");
															String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
															dias = Double.parseDouble(sumAseg)/500 ;
														}
														else{
															dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
														}
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
													} catch (Exception e) {
														cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
													}				
													//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
													//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
												}
												else if(coberturaVO.getClaveCobertura().equals("12")) {
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada().replace("$", ""),false,1);
													
												}
												else{
													cb=pdf.addLabel(cb,276, 467-(9*(x-toppage)),10,coberturaVO.getSumaAsegurada(),false,1);
												}
												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
											}
										}
										//deducibles
										if(coberturaVO.isFlagDeducible()){
											if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"5%",false,1);
											}else if ("45".equals(coberturaVO.getClaveCobertura())){
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,"U$S 200",false,1);
											}else{
												cb=pdf.addLabel(cb,445, 467-(9*(x-toppage)),10,coberturaVO.getDeducible(),false,1);
											}


										}																					
										//primas
										if(coberturaVO.isFlagPrima()&& !agenEsp2){
											cb=pdf.addLabel(cb, 580, 467-(9*(x-toppage)),10,FormatDecimal.numDecimal(primaExe),false,2);	
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}else if(coberturaVO.isFlagPrima()){
											primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
											log.debug("este es el acumulado de prima "+primaAux);
										}
									}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
										log.debug("este es el acumulado de prima "+primaAux);
									}		
								}
							}

							boolean altoRiesgo = false;
							if (validaAltoRiesgo) {
								log.debug("Se validara el alto riesgo");
								
								Integer tarifa = getNumber(polizaVO.getTarifa());
								
								// Tarifas enero 1990 - diciembre 2029
								boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
								
								if (tarifa < 1309 && formatoTarifaNormal) {
									Integer amis = getNumber(polizaVO.getAmis());
									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
									altoRiesgo = claveAmisAltoRiesgo == 9999;
								} else {
									List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
									if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
										Integer amis = getNumber(polizaVO.getAmis());
										int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
										altoRiesgo = claveAmisAltoRiesgo == 9999;
									}
								}
							}
							if (altoRiesgo) {
								cb = pdf.addLabel(cb, 23, 290, 10, "IMPORTANTE", false, 1);
								cb = pdf.addLabel(cb, 23, 280, 10, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", false, 1);
								cb = pdf.addLabel(cb, 23, 270, 10, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", false, 1);
							}
							
							int dif = 0;
							if (minimos){
								dif = 20; // Para que no choquen leyendas
								cb=pdf.addLabel(cb,23,286,10,DEDUCIBLE_MINIMOP1,false,1);
								cb=pdf.addLabel(cb,23,278,10,DEDUCIBLE_MINIMOP2,false,1);
							}
							
							String servicio = polizaVO.getServicio().trim();
							String tarifa = polizaVO.getTarifa().trim();
							log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
							if (servicio.equals("PUBLICO")) {
								//cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPub1,true,1);
								//cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPub2,true,1);
								cb=pdf.addLabel(cb,23,270+dif,10,SEG_OBL_PUB_1,false,1);
								cb=pdf.addLabel(cb,23,262+dif,10,SEG_OBL_PUB_2,false,1);
								cb=pdf.addLabel(cb,23,254+dif,10,SEG_OBL_PUB_3,false,1);
							} else if (servicio.equals("PARTICULAR")
									&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
//								cb=pdf.addLabel(cb,35,270+dif,10,seguroObligPart1,true,1);
//								cb=pdf.addLabel(cb,35,263+dif,10,seguroObligPart2,true,1);
								
								cb=pdf.addLabel(cb,23,270+dif,10,SEG_OBL_PART_1,false,1);
								cb=pdf.addLabel(cb,23,263+dif,10,SEG_OBL_PART_2,false,1);
								cb=pdf.addLabel(cb,23,256+dif,10,SEG_OBL_PART_3,false,1);
							}
							
							//************Agente
							//cb=pdf.addRectAngColor(cb,35,242,335,12);
							cb=pdf.addRectAng(cb,23,248,355,50);
							cb=pdf.addLabel(cb,40,238,10,"Textos:",false,1);
							cb=pdf.addRectAng(cb,23,196,355,30);
							//************forma de pago
							cb=pdf.addLabel(cb,40,183,10,"Forma de:",false,1);
							cb=pdf.addLabel(cb,40,173,10,"Pago:",false,1);
							if(polizaVO != null){
								if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
								}
								else {
										//cb=pdf.addLabel(cb,100,208,10,polizaVO.getClavAgente(),false,1);
									
									if(polizaVO.getDescrFormPago()!=null){
											cb=pdf.addLabel(cb,88,177,10,polizaVO.getDescrFormPago(),false,1);
									}
		
									//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
									cb=pdf.addLabel(cb,315,183,10,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
									if(polizaVO.getNumRecibos()!=null && polizaVO.getNumRecibos() > 1){
										cb=pdf.addLabel(cb,185,183,10,"Primer pago",false,1);
										cb=pdf.addLabel(cb,185,173,10,"Pago(s) Subsecuente(s)",false,1);																						
										cb=pdf.addLabel(cb,315,173,10,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
									}else{
										cb=pdf.addLabel(cb,190,183,10,"Pago Unico",false,1);
									}
								}			
							}			

							cb=pdf.addRectAng(cb,23,164,355,47);
							cb=pdf.addLabel(cb,40,154,10,"Exclusivo para reporte de",true,1);
							cb=pdf.addLabel(cb,40,144,10,"Siniestros",true,1);
							cb=pdf.addLabel(cb,180,154,10,"01-800-288-6700",true,1);
							cb=pdf.addLabel(cb,180,144,10,"01-800-800-2880",true,1);
							cb=pdf.addlineH(cb, 23, 139, 355);
							//la siguiente información va del nombre del agente a telefono nacional
							
//							if((diaFormato<11)||(mesFormato<=04)&&(anioFormato <=2017)){
							if((mesFormato<04)&&(anioFormato <=2017)){
								cb=pdf.addLabel(cb,40,128,10,"Agente:",true,1);
								cb=pdf.addLabel(cb,40,118,10,"Clave:",true,1);
								cb=pdf.addLabel(cb,168,118,10,"Teléfono:",true,1);
								
								if (polizaVO!=null){
									String nombreAgente="";
									if(polizaVO.getNombreAgente()!=null){
										nombreAgente=polizaVO.getNombreAgente()+" ";}
									if(polizaVO.getPateAgente()!=null){
										nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
									if(polizaVO.getMateAgente()!=null){
										nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
									if (polizaVO.getClavAgente().equals("52017")) {
										nombreAgente = "";
									}
									
									if (nombreAgente.length()>42){
									cb=pdf.addLabel(cb,88,128,10,nombreAgente.substring(0, 42),true,1);
									}else{
										cb=pdf.addLabel(cb,88,128,10,nombreAgente,true,1);
									}
									
		
									if(polizaVO.getClavAgente()!=null){
										cb=pdf.addLabel(cb,88,118,10,polizaVO.getClavAgente(),false,1);}
		
									//ANDRES-TELEFONO AGENTE
									if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
										if(polizaVO.getTelComerAgente()!=null){
											//cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelComerAgente(),false,1);
											cb=pdf.addLabel(cb,218,118,10,polizaVO.getTelComerAgente(),true,1);
										}
										else{
											//cb=pdf.addLabel(cb,240,208,10,polizaVO.getTelPartAgente(),false,1);
											cb=pdf.addLabel(cb,218,118,10,polizaVO.getTelPartAgente(),true,1);
										}
									}
								}
								
							}else{
								cb=pdf.addLabel(cb,40,128,10,"Atención Bilingüe",true,1);
								cb=pdf.addLabel(cb,160,128,10,"Inglés",true,1);
								cb=pdf.addLabel(cb,160,118,10,"Japonés",true,1);
								cb=pdf.addLabel(cb,240,128,10,"01-800-062-0840",true,1);
								cb=pdf.addLabel(cb,240,118,10,"01-800-062-0841",true,1);
							}
							
							
							
//							cb=pdf.addLabel(cb,40,153,10,"Servicio de Asistencia Vial         3300 4534",true,1);
//							cb=pdf.addLabel(cb,40,153,10,"Quálitas                            01-800-253-0553",true,1);
							
							
//							if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
//								cb=pdf.addLabel(cb,35,140,10,polizaVO.getAsistencia(),true,1);
//							} else {
//								cb=pdf.addLabel(cb,35,140,10,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
//								
//								if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//								}
//								else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//								}
//								else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
//									cb=pdf.addLabel(cb,290,140,10,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
//								}
//								else{
//									cb=pdf.addLabel(cb,290,140,10,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
//								}
//							}
							
							



							cb=pdf.addRectAng(cb,23,115,355,60);
//							cb=pdf.addLabel(cb,43,117,10,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
//							cb=pdf.addLabel(cb,43,110,10,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
//							cb=pdf.addLabel(cb,43,103,10,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
//							cb=pdf.addLabel(cb,43,96,10,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);
//
////							cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
////							cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
////							cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
////							cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
//							
//							cb=pdf.addLabel(cb,43,81,10,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
//							cb=pdf.addLabel(cb,43,74,10,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
//							cb=pdf.addLabel(cb,43,67,10,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
//							cb=pdf.addLabel(cb,43,60,10,"del día",false,1);

							
//							//CHAVA-LEYENDA ARTICULO 25
//							cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
//							cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);



							//************importe
							cb=pdf.addRectAngColor(cb,390,247,195,13);
							cb=pdf.addLabel(cb,395,237,10,"MONEDA",true,1);
							if(polizaVO.getDescMoneda()!=null){
								cb=pdf.addLabel(cb,560,237,10,polizaVO.getDescMoneda(),true,2);}
							cb=pdf.addRectAng(cb,390,232,195,20);
																
							cb=pdf.addRectAng(cb,390,210,195,83);

//							cb=pdf.addLabel(cb,395,200,10,"PRIMA NETA",false,1);
//							cb=pdf.addLabel(cb,395,190,10,"TASA FINANCIAMIENTO POR PAGO",false,1);
//							cb=pdf.addLabel(cb,395,180,10,"FRACCIONADO",false,1);
//							cb=pdf.addLabel(cb,395,167,10,"GTOS. EXPEDICION POL.",false,1);
//							cb=pdf.addLabel(cb,395,147,10,"SUBTOTAL",false,1);
//							cb=pdf.addLabel(cb,395,135,10,"I.V.A.",false,1);
//							cb=pdf.addLabel(cb,395,116,10,"IMPORTE TOTAL",true,1);
//							cb=pdf.addLabel(cb,395,100,10,"CONDICIONES VIGENTES:",false,1);
//							cb=pdf.addLabel(cb,395,88,10,"TARIFA APLICADA:",false,1);
							
							
							cb=pdf.addLabel(cb,395,200,10,"Prima Neta",false,1);
							cb=pdf.addLabel(cb,395,190,10,"Tasa Financiamiento",false,1);
							//cb=pdf.addLabel(cb,395,180,10,"FRACCIONADO",false,1);
							//cb=pdf.addLabel(cb,395,167,10,"GTOS. EXPEDICION POL.",false,1);
							cb=pdf.addLabel(cb,395,180,10,"Gastos por Expedición.",false,1);
							cb=pdf.addLabel(cb,395,147,10,"Subtotal",false,1);
							cb=pdf.addLabel(cb,395,135,10,"I.V.A.   16%",false,1);
							cb=pdf.addLabel(cb,395,116,10,"IMPORTE TOTAL",true,1);
//							cb=pdf.addLabel(cb,395,100,10,"CONDICIONES VIGENTES:",false,1);
							cb=pdf.addLabel(cb,30,103,10,"Tarifa Aplicada:",false,1);
							

							if(polizaVO != null){
								//la información siguiente va de prima neta a tarifa aplicada
								if(polizaVO.getPrimaNeta()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(primaAux),false,2);
									}
									else
										cb=pdf.addLabel(cb,560,200,10,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
								}
								/*	if(polizaVO.getRecargo()!=null){ 
										    if(Double.parseDouble(polizaVO.getRecargo())>0){
										    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
										}*/
								if(polizaVO.getRecargo()!=null){
									if(Double.parseDouble(polizaVO.getRecargo())>0){
										if(Integer.parseInt(polizaVO.getNumIncisos())>1){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}
										else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}else{
										if(Integer.parseInt(polizaVO.getNumIncisos())<0){
											recargoAux=Double.parseDouble(polizaVO.getRecargo());
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(recargoAux),false,2);
										}else{
											cb=pdf.addLabel(cb,560,190,10,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
										}
									}
								}
								if(polizaVO.getDerecho()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(derechoAux),false,2);

									}
									else{
										cb=pdf.addLabel(cb,560,180,10,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
								}
								
								
								if(polizaVO.getCesionComision()!=null){
									    cb=pdf.addLabel(cb,395,157,10,"DESCUENTOS",false,1);
										cb=pdf.addLabel(cb,560,157,10,polizaVO.getCesionComision(),false,2);
								}
								
								
								if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								

									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										subtotalAux = primaAux+derechoAux;				
									}
									else{
										subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}

									cb=pdf.addLabel(cb,560,147,10,FormatDecimal.numDecimal(subtotalAux),false,2);
								}

								cb=pdf.addlineH(cb,460,143,115);
								if(polizaVO.getImpuesto()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
										if(polizaVO.getIva()!=null){
											impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
											cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(impuestoAux),false,2);
										}
									}
									else{
										cb=pdf.addLabel(cb,560,135,10,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
								}

								cb=pdf.addRectAng(cb,390,125,195,13);
								if(polizaVO.getPrimaTotal()!=null){
									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
									}
									else{
										cb=pdf.addLabel(cb,560,116,10,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
								}

								//***************************

//								if(polizaVO.getDescConVig()!=null){									
//									cb=pdf.addLabel(cb,555,100,10,polizaVO.getDescConVig(),false,2);
//								}

								//cb=pdf.addRectAng(cb,390,110,180,25);
								if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
									//String concatZonaId= "0000"+polizaVO.getClaveOfic();

									if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){

										String concatZonaId= "0000"+polizaVO.getTarifApDesc();
										//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}
									else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){

										String concatZonaId= "0000"+polizaVO.getTarifApCve();
										//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
										cb=pdf.addLabel(cb,180,103,10,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
									}

									//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
								}								

							}

							//****firma
							if(polizaVO != null){
								String lugar="";
								if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
								if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
								cb=pdf.addLabel(cb,490,100,10,lugar,false,0);
								//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
								if(polizaVO.getFchEmi()!=null){
									cb=pdf.addLabel(cb,490,90,10,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
								/*if(polizaVO.getFchIni()!=null){
										cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
							}


							if(polizaVO.getDirImagen()!=null){
								document=pdf.addImage(document,450,50,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
								//cb=pdf.addLabel(cb,490,35,10,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
							}
//							cb=pdf.addLabel(cb,490,28,10,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
							cb=pdf.addLabel(cb,490,40,10,"Funcionario Autorizado",false,0);	


							if(polizaVO.getDescConVig()!=null){									
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables "+polizaVO.getDescConVig()+ "mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}
							else{
								cb=pdf.addLabel(cb,21,83,10,"El asegurado recibe la impresión de la póliza junto con las condiciones generales",false,1);
								cb=pdf.addLabel(cb,21,73,10,"aplicables                              mismas que además puede consultar  e imprimir",false,1);
								cb=pdf.addLabel(cb,111,63,10,"en nuestra pagina www.qualitas.com.mx",false,0);
							}


//							if (polizaVO.getcNSF()!=null){
//								cb=pdf.addLabel(cb,43,53,10,polizaVO.getcNSF(),false,1);
//							}
							
							//ANDRES-MEM
//							if (membretado==null||membretado.equals("S")){
//								cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//								cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//								document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							}




							
							
						}//fin for para las dos hojas de la poliza
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
							
							

//							document.newPage();
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						}
						
						
						
						
						
						

//						cb=pdf.addLabel(cb,100,700,12,"POLIZA DE SEGURO DE AUTOMOVILES",true,1);										
//						cb=pdf.addLabel(cb,400,706,10,"POLIZA",false,1);
//						cb=pdf.addLabel(cb,460,706,10,"ENDOSO",false,1);
//						cb=pdf.addLabel(cb,525,706,10,"INCISO",false,1);
//						if(polizaVO != null){
//							//el orden de los datos siguientes va de poliza a inciso
//							String inciso="000";
//							String incisoAux;
//
//							if(polizaVO.getNumPoliza()!=null){
//								sizeNumPoliza=polizaVO.getNumPoliza().length();
//								cb=pdf.addLabel(cb,400,694,9,polizaVO.getNumPoliza().substring(2,sizeNumPoliza-6),false,1);
//
//								cb=pdf.addLabel(cb,460,694,9,polizaVO.getNumPoliza().substring(sizeNumPoliza-6,sizeNumPoliza),false,1);
//							}
//
//							if(polizaVO.getInciso()!=null){
//								inciso = inciso+polizaVO.getInciso();								
//								incisoAux =inciso.substring(inciso.length()-4,inciso.length());
//								cb=pdf.addLabel(cb,525,694,9,incisoAux,false,1);
//							}
//						}
//
//
//						//**********CUERPO										
//						cb=pdf.addRectAngColor(cb,35,661,535,12);
//						cb=pdf.addRectAng(cb,35,648,535,43);


//						//***********asegurado					
//						cb=pdf.addLabel(cb,290,651,10,"INFORMACION DEL ASEGURADO",true,0);				
//						cb=pdf.addLabel(cb,440,640,8,"RENUEVA A:",false,1);
//						cb=pdf.addLabel(cb,40,630,8,"DOMICILIO",false,1);
//						cb=pdf.addLabel(cb,40,620,8,"C.P.",false,1);
//						cb=pdf.addLabel(cb,340,620,8,"RFC",false,1);
//						//cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);	
//						if(polizaVO.getPolizaAnterior()!=null){
//							cb=pdf.addLabel(cb,515,640,8,String.valueOf(polizaVO.getPolizaAnterior()),false,1);	}	
//						if(polizaVO != null){
//							//el orden de los datos siguientes va del nombre del asegurado a beneficiario
//							String nombre=""; 
//							if(polizaVO.getNombre()!=null){
//								nombre=polizaVO.getNombre()+" ";}
//							if(polizaVO.getApePate()!=null){
//								nombre=nombre+polizaVO.getApePate()+" ";}
//							if(polizaVO.getApeMate()!=null){
//								nombre=nombre+polizaVO.getApeMate()+" ";}
//
//							if(polizaVO.getConductor() != null && polizaVO.getConductor().compareTo("")!=0){
//								nombre=nombre+" Y/O "+polizaVO.getConductor();
//							}	
//
//							cb=pdf.addLabel(cb,40,640,8,nombre,false,1);
//							if(datosCliente){									
//								cb=pdf.addLabel(cb,490,640,8,"  ",false,1);	
//
//								if(polizaVO.getCalle()!=null){
//									String calle= polizaVO.getCalle();
//									if(polizaVO.getExterior()!= null){
//										calle += " No. EXT. " + polizaVO.getExterior();
//									}
//									if(polizaVO.getInterior()!= null){
//										calle += " No. INT. " + polizaVO.getInterior();
//									}
//									//ANDRES-prueba 
//									//System.out.println("colonia:::"+polizaVO.getColonia());
//									if(polizaVO.getColonia()!=null){
//										calle += " COL. " + polizaVO.getColonia();
//									}
//									cb=pdf.addLabel(cb,90,630,8,calle,false,1);}
//
//								if(polizaVO.getCodPostal()!=null){
//									cb=pdf.addLabel(cb,70,620,8,String.valueOf(polizaVO.getCodPostal()),false,1);	}	
//								if(polizaVO.getMunicipio()!=null && polizaVO.getEstado()!=null){
//									cb=pdf.addLabel(cb,130,620,8,polizaVO.getMunicipio()+","+polizaVO.getEstado(),false,1);}
//								if(polizaVO.getRfc()!=null){
//									cb=pdf.addLabel(cb,390,620,8,polizaVO.getRfc(),false,1);}
//								if(polizaVO.getBeneficiario()!=null){
//									cb=pdf.addLabel(cb,40,610,8,polizaVO.getBeneficiario(),false,1);}
//
//								for(int i =0,j=340;i<polizaVO.getCamposEspeciales().size();i++){
//									LabelValueBean campo = (LabelValueBean)polizaVO.getCamposEspeciales().get(i);
//									if(campo.getLabel().length()>13)
//										cb=pdf.addLabel(cb,j,610,8,campo.getLabel().substring(0,13),false,1);
//									else
//										cb=pdf.addLabel(cb,j,610,8,campo.getLabel(),false,1);
//									j=j+50;
//									if(campo.getValue().length()>13)
//										cb=pdf.addLabel(cb,j,610,8,campo.getValue().substring(0,13),false,1);
//									else
//										cb=pdf.addLabel(cb,j,610,8,campo.getValue(),false,1);
//									j=j+50;
//								}
//							}
//							if(polizaVO.getCveApoderado()!=null){
//								cb=pdf.addLabel(cb,40,610,8,"APODERADO",false,1);
//								cb=pdf.addLabel(cb,100,610,8,polizaVO.getCveApoderado(),false,1);
//							}							
//
//						}				


						//*************Vehiculo
//						cb=pdf.addRectAngColor(cb,35,603,535,12);
//						cb=pdf.addRectAng(cb,35,589,535,55);
//
//						cb=pdf.addLabel(cb,290,592,10,"DESCRIPCION DEL VEHICULO ASEGURADO",true,0);
//						cb=pdf.addLabel(cb,40,565,8,"TIPO",true,1);
//						cb=pdf.addLabel(cb,170,565,8,"MODELO",true,1);
//						cb=pdf.addLabel(cb,260,565,8,"COLOR",true,1);
//						cb=pdf.addLabel(cb,490,565,8,"PLACAS",true,1);
//						cb=pdf.addLabel(cb,40,552,8,"SERIE",true,1);
//						cb=pdf.addLabel(cb,259,552,8,"MOTOR",true,1);
//						cb=pdf.addLabel(cb,390,552,8,"REPUVE",true,1);
//						if(polizaVO != null){
//							//la información siguiente va de descripción del vehiculo a tipo de carga							
//							if(polizaVO.getAmis()!=null){
//								cb=pdf.addLabel(cb,40,580,8,polizaVO.getAmis(),true,1);}							
//							if(polizaVO.getDescVehi()!=null){
//								cb=pdf.addLabel(cb,70,580,8,polizaVO.getDescVehi(),false,1);}											
//
//							if(polizaVO.getTipo()!=null){
//
//								if(polizaVO.getTipo().length()>18){
//									//System.out.println("TIPO: "+polizaVO.getTipo());	
//									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo().substring(0, 19),false,1);
//								}else{
//									//System.out.println("TIPO: "+polizaVO.getTipo());	
//									cb=pdf.addLabel(cb,70,565,8,polizaVO.getTipo(),false,1); 
//								}	
//							}
//
//							if(polizaVO.getVehiAnio()!=null){												
//								cb=pdf.addLabel(cb,210,565,8,polizaVO.getVehiAnio(),false,1);}
//							if(polizaVO.getColor()!=null){
//
//								if(polizaVO.getColor().equals("SIN COLOR")){
//
//									cb=pdf.addLabel(cb,300,565,8,"",false,1);		
//								}else{
//									cb=pdf.addLabel(cb,300,565,8,polizaVO.getColor(),false,1);
//								}
//							}
//							//ANDRES-PASAJEROS
//							if (polizaVO.getNumPasajeros()!=null){
//								cb=pdf.addLabel(cb,40,542,8,polizaVO.getNumPasajeros(),false,1);
//							}
//							else if(polizaVO.getNumOcupantes()!=null){
//								cb=pdf.addLabel(cb,390,565,8,"OCUP.",true,1);
//								cb=pdf.addLabel(cb,420,565,8,polizaVO.getNumOcupantes(),false,1);
//							}
//
//							if(polizaVO.getNumPlaca()!=null){												
//								cb=pdf.addLabel(cb,530,565,8,polizaVO.getNumPlaca(),false,1);}
//							if(polizaVO.getNumSerie()!=null){					
//								cb=pdf.addLabel(cb,70,552,8,polizaVO.getNumSerie(),false,1);}
//							if(polizaVO.getNumMotor()!=null){					
//								cb=pdf.addLabel(cb,300,552,8,polizaVO.getNumMotor(),false,1);}
//							if(polizaVO.getRenave()!=null){					
//								cb=pdf.addLabel(cb,440,552,8,polizaVO.getRenave(),false,1);}
//							
//							if (polizaVO.getCveServ().trim().equals("3")) {
//								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
//									cb=pdf.addLabel(cb,110,539,8,"'"+polizaVO.getClaveCarga()+"'",false,1);}
//								if(polizaVO.getTipoCarga()!=null && polizaVO.getTipoCarga()!=""){								
//									cb=pdf.addLabel(cb,200,539,8,polizaVO.getTipoCarga()+" : ",true,2);}
//								//CHAVA-DESCRIPCION DE LA CARGA Y DOBLE REMOLQUE
//								if((polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!="") || (polizaVO.getDobleRemolque() != null && polizaVO.getDobleRemolque() != "")){							
//									String descAux = "";
//									String valorRemolque="";
//									if(polizaVO.getDescCarga()!=null && polizaVO.getDescCarga()!=""){
//										descAux=polizaVO.getDescCarga();
//									}
//									
//									if(polizaVO.getDobleRemolque()!= null){
//										if(polizaVO.getDobleRemolque().equals("S")){
//											valorRemolque = "2° Remolque: AMPARADO";
//										}else{
//											valorRemolque = "2° Remolque: EXCLUIDO";
//										}
//									}								
//									if(descAux != "" || valorRemolque != ""){
//										cb=pdf.addLabel(cb,210,539,8,descAux+"  "+valorRemolque,false,1);
//									}
//									
//								}
//							}
//							
//							
//							if (polizaVO.getCveServ().trim().equals("1") && polizaVO.getUso().trim().equals("CARGA")) {
//								if(polizaVO.getTipoCarga()!= null && polizaVO.getTipoCarga()!= ""){
//									cb=pdf.addLabel(cb,40,539,8,"TIPO DE CARGA: ",true,1);
//									cb=pdf.addLabel(cb,110,539,8,polizaVO.getClaveCarga()+" "+polizaVO.getTipoCarga()+" : "+polizaVO.getDescCarga(),false,1);
//								}
//							}
//							
//							
//							if(polizaVO.getNoEconomico()!=null){
//								cb=pdf.addLabel(cb,480,552,8,"NO.ECO.",true,1);
//								cb=pdf.addLabel(cb,515,552,8,polizaVO.getNoEconomico(),false,1);
//							}
//							
//							
//							
//							
//							
//							
//							
//						}
//
//
//						//**************vigencia
//						cb=pdf.addRectAng(cb,35,531,150,25);
//						cb=pdf.addRectAng(cb,193,531,50,25);
//						cb=pdf.addRectAng(cb,251,531,60,25);
//						cb=pdf.addRectAng(cb,319,531,50,25);						
//						cb=pdf.addRectAng(cb,377,531,192,25);
//
//						cb=pdf.addLabel(cb,33,526,5,"VIGENCIA:",false,1);
//						cb=pdf.addLabel(cb,33,518,7,"DESDE LAS 12 HORAS P.M. DEL  ",false,1);
//						cb=pdf.addLabel(cb,33,510,7,"HASTA LAS 12 HORAS P.M. DEL  ",false,1);
//						cb=pdf.addLabel(cb,218,522,8,"PLAZO PAGO",false,0);
//						cb=pdf.addLabel(cb,251,522,8,"F. LIMITE PAGO",false,1);
//						cb=pdf.addLabel(cb,344,522,8,"MOVIMIENTO",false,0);
//						cb=pdf.addLabel(cb,415,522,8,"USO",false,0);
//						cb=pdf.addLabel(cb,515,522,8,"SERVICIO",false,0);
//						if(polizaVO != null){		
//							//la información siguiente va desde la fecha de vigencia hasta servicio
//							if(polizaVO.getFchIni()!=null){
//								cb=pdf.addLabel(cb,143,518,7,DateFormat.addFechaCorta(polizaVO.getFchIni()),false,1);															
//							}
//							if(polizaVO.getFchFin()!=null){
//								cb=pdf.addLabel(cb,143,510,7,DateFormat.addFechaCorta(polizaVO.getFchFin()),false,1);
//
//							}
//
//							if(polizaVO.getPlazoPago()!=null){
//								cb=pdf.addLabel(cb,218,512,8, polizaVO.getPlazoPago()+" dias",false,0);}
//
//							if(polizaVO.getFechaLimPago()!=null){																												
//								cb=pdf.addLabel(cb,259,512,8,DateFormat.addFechaCorta(polizaVO.getFechaLimPago()),false,1);}
//
//
//							if(polizaVO.getMovimiento()!=null){
//								cb=pdf.addLabel(cb,344,512,8,polizaVO.getMovimiento(),false,0);
//							}
//
//							if(polizaVO.getUso()!=null){
//								ArrayList uso=pdf.trimString(polizaVO.getUso(),15,42,75);
//								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
//								//caracteres      15 , 21*2 , 25*3
//								if(uso!=null){
//									if(uso.size()==1){
//										cb=pdf.addLabel(cb,415,512,8,(String)uso.get(0),false,0);
//									}
//									else if(uso.size()==2){
//										cb=pdf.addLabel(cb,415,515,6,(String)uso.get(0),false,0);
//										cb=pdf.addLabel(cb,415,509,6,(String)uso.get(1),false,0);
//									}	
//									else if(uso.size()==3){
//										cb=pdf.addLabel(cb,415,517,5,(String)uso.get(0),false,0);
//										cb=pdf.addLabel(cb,415,512,5,(String)uso.get(1),false,0);
//										cb=pdf.addLabel(cb,415,507,5,(String)uso.get(2),false,0);
//									}											
//								}									
//							}
//							if(polizaVO.getServicio()!=null){
//								ArrayList servicio=pdf.trimString(polizaVO.getServicio(),23,62,111);
//								//tamaño letra    #8 ,  #6  ,  #5		estos son los valores por default
//								//caracteres      23 , 31*2 , 37*3
//								if(servicio!=null){
//									if(servicio.size()==1){
//										cb=pdf.addLabel(cb,515,512,8,(String)servicio.get(0),false,0);
//									}
//									else if(servicio.size()==2){
//										cb=pdf.addLabel(cb,515,515,6,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,509,6,(String)servicio.get(1),false,0);
//									}
//									else if(servicio.size()==3){
//										cb=pdf.addLabel(cb,515,517,5,(String)servicio.get(0),false,0);
//										cb=pdf.addLabel(cb,515,512,5,(String)servicio.get(1),false,0);
//										cb=pdf.addLabel(cb,515,507,5,(String)servicio.get(2),false,0);
//									}
//								}									
//							}					
//						}
//
//
//						//*************Datos de Riesgos
//						cb=pdf.addRectAngColor(cb,35,502,535,12);	
//						cb=pdf.addRectAng(cb,35,482,535,235);
//
//						cb=pdf.addLabel(cb,60,492,10,"COBERTURAS CONTRATADAS",true,1);
//						cb=pdf.addLabel(cb,260,492,10,"SUMA ASEGURADA",true,1);
//						cb=pdf.addLabel(cb,420,492,10,"DEDUCIBLE",true,1);
//						cb=pdf.addLabel(cb,510,492,10,"$     PRIMAS",true,1);
//
//						//Dado que la lista de la información q se pinta en el cuerpo (coberturas) se tiene en un 
//						//ArrayList primero se genera un objeto por cada cobertura para poder hacer un cast ala posicion x del 
//						//arraylist del tipo de objeto de las coberturas, despues se hacen unas comparaciones para saber si se
//						//pinta la cobertura q se trae en el objeto o en su defecto un letrero ya definido.
//						double primaAux=0;
//						double primaExe=0;
//						double derechoAux=0;
//						double recargoAux=0;
//						double subtotalAux=0;
//						double impuestoAux=0;
//						boolean exDM=false;
//						boolean exRT=false;
//						boolean agenEsp1 = false;
//						boolean agenEsp2 = false;
//						boolean validaAltoRiesgo = false;					
//
//						for(int y=0;y<polizaVO.getAgenteEsp().size();y++){
//							int temp = (Integer)polizaVO.getAgenteEsp().get(y);
//							if(temp==1)
//								agenEsp1=true;
//							if(temp==AGEN_ESP_OCULTA_PRIMAS)
//								agenEsp2=true;
//						}
//						
//						boolean minimos=false;
//
//						if(polizaVO.getCoberturasArr()!=null){
//							CoberturasPdfBean coberturaVO;
//
//							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
//							{
//								coberturaVO= new CoberturasPdfBean();
//								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
//								if(coberturaVO.getClaveCobertura().equals("12")){
//									exDM=true;
//									if(coberturaVO.isFlagPrima()){
//										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									}
//								}else if(coberturaVO.getClaveCobertura().equals("40")){
//									exRT=true;
//									if(coberturaVO.isFlagPrima()){
//										primaExe=primaExe+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									}
//								}	
//							}
//							
//							String cveServ = polizaVO.getCveServ().trim();
//							for(int x=toppage;x<polizaVO.getCoberturasArr().size();x++)						
//							{	
//								boolean salto=false;
//								if(x==toppage+27)
//								{break;}
//
//								coberturaVO = (CoberturasPdfBean)polizaVO.getCoberturasArr().get(x);
//								String claveCobertura = coberturaVO.getClaveCobertura();
//
//								int deducible = getNumber(coberturaVO.getDeducible());
//								int anio = getNumber(polizaVO.getVehiAnio());
//								if (validaAltoRiesgo == false && cveServ.equals("1") && claveCobertura.trim().equals(ROBO_TOTAL_ID) && deducible == 20 && anio >= ANIO_ALTO_RIESGO_RT) {
//									validaAltoRiesgo = true;
//								}
//
//								//ANDRES-MINIMOS
//								if ((polizaVO.getTipo().contains("FRONTERIZOS"))&&(coberturaVO.getClaveCobertura().equals("1")||coberturaVO.getClaveCobertura().equals("2"))){
//									minimos=true;
//								}
//
//
//								log.debug("coberturaVO.getClaveCobertura():"+coberturaVO.getClaveCobertura());
//								//if("13".equals(coberturaVO.getClaveCobertura())&& polizaVO.getAgenteEsp()==1){
//								//	cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,"8.-Equipo Especial" ,false,1);
//								//}else{
//								if(StringUtils.isNotEmpty(cveServ)&&cveServ.equals("3")&&coberturaVO.getClaveCobertura().equals("6")){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Gastos por Perdida de Uso en P T" ,false,1);
//								}else if(coberturaVO.getClaveCobertura().equals("12")&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ "Exe. Ded. x PT, DM Y RT" ,false,1);
//									salto = true;
//								}else if((coberturaVO.getClaveCobertura().equals("40"))&&((exDM&&exRT)||"14".equals(polizaVO.getCvePlan()))){
//									salto = true;
//								}else if(StringUtils.isNotEmpty(polizaVO.getCvePlan())&&polizaVO.getCvePlan().equals("38")&&coberturaVO.getClaveCobertura().equals("3")){
//									cb=pdf.addLabel(cb,50, 467-(9*(x-toppage)),9,"3.12"+".-"+ " Responsabilidad Civil Estandarizado" ,false,1);
//								}else{
//									cb=pdf.addLabel(cb,40, 467-(9*(x-toppage)),9,coberturaVO.getClaveCobertura()+".-"+ coberturaVO.getDescrCobertura() ,false,1);
//								}
//								//}
//
//								if(!salto){
//									//ANDRES-SUMASEG
//									//suma asegurada														
//									if(coberturaVO.isFlagSumaAsegurada()){	
//										if (coberturaVO.getClaveCobertura().contains("6.2")){
//											double dias=0;
//											try {
//												if (coberturaVO.getSumaAsegurada().contains(",")){
//													int indice=0;
//													indice=coberturaVO.getSumaAsegurada().indexOf(",");
//													String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
//													dias = Double.parseDouble(sumAseg)/500 ;
//												}
//												else{
//													dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
//												}
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
//											} catch (Exception e) {
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//											}				
//											//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
//											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
//										}
//										else{
//											cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//										}
//
//									}
//									//deducibles
//									if(coberturaVO.isFlagDeducible()){
//										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else{
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
//										}
//
//
//									}																					
//									//primas
//									if(coberturaVO.isFlagPrima()&& !agenEsp2){
//										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,coberturaVO.getPrima(),false,2);	
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}else if(coberturaVO.isFlagPrima()){
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}
//								}else if(salto && coberturaVO.getClaveCobertura().equals("11")){
//									//ANDRES-SUMASEG
//									//}
//									//										suma asegurada														
//									if(coberturaVO.isFlagSumaAsegurada()){	
//										if(coberturaVO.isFlagSumaAsegurada()){	
//											if (coberturaVO.getClaveCobertura().contains("6.2")){
//												double dias=0;
//												try {
//													if (coberturaVO.getSumaAsegurada().contains(",")){
//														int indice=0;
//														indice=coberturaVO.getSumaAsegurada().indexOf(",");
//														String sumAseg=coberturaVO.getSumaAsegurada().substring(0,indice)+coberturaVO.getSumaAsegurada().substring(indice+1,coberturaVO.getSumaAsegurada().length());
//														dias = Double.parseDouble(sumAseg)/500 ;
//													}
//													else{
//														dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;
//													}
//													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);			        			
//												} catch (Exception e) {
//													cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//												}				
//												//dias = Double.parseDouble(coberturaVO.getSumaAsegurada())/500 ;			        							
//												//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,"500 x DIA HASTA "+ (int)dias + " DIAS",false,1);
//											}
//											else{
//												cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//											}
//											//cb=pdf.addLabel(cb,265, 467-(9*(x-toppage)),9,coberturaVO.getSumaAsegurada(),false,1);
//										}
//									}
//									//deducibles
//									if(coberturaVO.isFlagDeducible()){
//										if("13".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if("8".equals(coberturaVO.getClaveCobertura())&& agenEsp1){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"5%",false,1);
//										}else if ("45".equals(coberturaVO.getClaveCobertura())){
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,"U$S 200",false,1);
//										}else{
//											cb=pdf.addLabel(cb,425, 467-(9*(x-toppage)),9,coberturaVO.getDeducible(),false,1);
//										}
//
//
//									}																					
//									//primas
//									if(coberturaVO.isFlagPrima()&& !agenEsp2){
//										cb=pdf.addLabel(cb, 565, 467-(9*(x-toppage)),9,FormatDecimal.numDecimal(primaExe),false,2);	
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}else if(coberturaVO.isFlagPrima()){
//										primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//										log.debug("este es el acumulado de prima "+primaAux);
//									}
//								}else if(salto && coberturaVO.getClaveCobertura().equals("40")&&coberturaVO.isFlagPrima()){
//									primaAux=primaAux+Double.parseDouble(Numtotext.numeroStringToString(coberturaVO.getPrima()));
//									log.debug("este es el acumulado de prima "+primaAux);
//								}		
//							}
//						}
//
//						boolean altoRiesgo = false;
//						if (validaAltoRiesgo) {
//							log.debug("Se validara el alto riesgo");
//							
//							Integer tarifa = getNumber(polizaVO.getTarifa());
//							
//							// Tarifas enero 1990 - diciembre 2029
//							boolean formatoTarifaNormal = polizaVO.getTarifa().matches("[90123]\\d(0[1-9]|1[012])");
//							
//							if (tarifa < 1309 && formatoTarifaNormal) {
//								Integer amis = getNumber(polizaVO.getAmis());
//								int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
//								altoRiesgo = claveAmisAltoRiesgo == 9999;
//							} else {
//								List<Integer> estadosAltoRiesgo = altoRiesgoService.getEstadosAltoRiesgo();
//								if (estadosAltoRiesgo.contains(getNumber(polizaVO.getCveEstado()))) {
//									Integer amis = getNumber(polizaVO.getAmis());
//									int claveAmisAltoRiesgo = altoRiesgoService.getClaveAmisAltoRiesgo(amis.toString(), tarifa.toString());
//									altoRiesgo = claveAmisAltoRiesgo == 9999;
//								}
//							}
//						}
//						if (altoRiesgo) {
//							cb = pdf.addLabel(cb, 35, 290, 7, "IMPORTANTE", true, 1);
//							cb = pdf.addLabel(cb, 35, 280, 7, "* Instala sin costo el equipo de Recuperación Satelital Encontrack y reduce el deducible de la  cobertura de Robo Total en 10 puntos porcentuales.", true, 1);
//							cb = pdf.addLabel(cb, 35, 270, 7, "  Marque en el D.F. y zona metropolitana 01(55)53 37 09 00 y  en el interior de la República al 01 800 0013 625.", true, 1);
//						}
//						
//						int dif = 0;
//						if (minimos){
//							dif = 20; // Para que no choquen leyendas
//							cb=pdf.addLabel(cb,35,270,7,DEDUCIBLE_MINIMOP1,true,1);
//							cb=pdf.addLabel(cb,35,263,7,DEDUCIBLE_MINIMOP2,true,1);
//						}
//						
//						String servicio = polizaVO.getServicio().trim();
//						String tarifa = polizaVO.getTarifa().trim();
//						log.debug("servicio: " + servicio + " tarifa: "+ tarifa);
//						if (servicio.equals("PUBLICO")) {
//							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPub1,true,1);
//							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPub2,true,1);
//						} else if (servicio.equals("PARTICULAR")
//								&& (tarifa.equals("5203") || tarifa.equals("5363") || tarifa.equals("5377"))) {
//							cb=pdf.addLabel(cb,35,270+dif,7,seguroObligPart1,true,1);
//							cb=pdf.addLabel(cb,35,263+dif,7,seguroObligPart2,true,1);
//						}
//						
//						if (StringUtils.isNotEmpty(polizaVO.getAsistencia())) {
//							cb=pdf.addLabel(cb,35,250,7,polizaVO.getAsistencia(),true,1);
//						} else {
//							cb=pdf.addLabel(cb,35,250,7,"Para los servicios de Asistencia Vial marque en el D.F. y Area Metropolitana",true,1);	
//							
//							if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()!=null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//							}
//							else if(polizaVO.getTelProvAsistVialDF()==null && polizaVO.getTelProvAsistVialInt()!=null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+"  "+" y en el interior de la Republica al "+polizaVO.getTelProvAsistVialInt(),true,1);
//							}
//							else if(polizaVO.getTelProvAsistVialDF()!=null && polizaVO.getTelProvAsistVialInt()==null ){
//								cb=pdf.addLabel(cb,290,250,7,"al "+polizaVO.getTelProvAsistVialDF() +" y en el interior de la Republica al "+"  ",true,1);
//							}
//							else{
//								cb=pdf.addLabel(cb,290,250,7,"al "+"  " +" y en el interior de la Republica al "+"  ",true,1);
//							}
//						}
//
//
//						//************Agente
//						cb=pdf.addRectAngColor(cb,35,242,335,12);
//						cb=pdf.addRectAng(cb,35,228,335,53);
//
//						cb=pdf.addLabel(cb,190,232,10,"OFICINA DE SERVICIO",true,0);
//						cb=pdf.addLabel(cb,40,218,8,"AGENTE",false,1);
//						cb=pdf.addLabel(cb,40,208,8,"NUMERO",false,1);
//						cb=pdf.addLabel(cb,190,208,8,"TELEFONO",false,1);
//						cb=pdf.addLabel(cb,40,198,8,"OFICINA",false,1);
////						cb=pdf.addLabel(cb,40,188,8,"DOMICILIO",false,1);
////						cb=pdf.addLabel(cb,310,188,8,"C.P.",false,1);
////						cb=pdf.addLabel(cb,40,178,8,"COL.",false,1);
////						cb=pdf.addLabel(cb,188,178,8,"TEL.",false,1);
////						cb=pdf.addLabel(cb,274,178,8,"FAX",false,1);
////						//cb=pdf.addLabel(cb,40,168,8,"TELEFONO",false,1);
////						//cb=pdf.addLabel(cb,170,168,8,"LOCAL",false,1);
////						//cb=pdf.addLabel(cb,40,158,8,"FAX",false,1);
////						//cb=pdf.addLabel(cb,170,158,8,"NACIONAL",false,1);
//						if(polizaVO!= null){
//							//la siguiente información va del nombre del agente a telefono nacional
//							String nombreAgente="";
//							if(polizaVO.getNombreAgente()!=null){
//								nombreAgente=polizaVO.getNombreAgente()+" ";}
//							if(polizaVO.getPateAgente()!=null){
//								nombreAgente=nombreAgente+polizaVO.getPateAgente()+" ";}
//							if(polizaVO.getMateAgente()!=null){
//								nombreAgente=nombreAgente+polizaVO.getMateAgente()+" ";}
//							if (polizaVO.getClavAgente().equals("52017")) {
//								nombreAgente = "";
//							}
//
//							cb=pdf.addLabel(cb,100,218,8,nombreAgente,false,1);
//
//							if(polizaVO.getClavAgente()!=null){
//								cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);}
//
//							//ANDRES-TELEFONO AGENTE
//							//System.out.println("telParti"+polizaVO.getTelPartAgente());
//							//System.out.println("telcomer"+polizaVO.getTelComerAgente());
//							if ((polizaVO.getTelComerAgente()!=null)||(polizaVO.getTelPartAgente()!=null) && !polizaVO.getClavAgente().equals("52017")){
//								if(polizaVO.getTelComerAgente()!=null){
//									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelComerAgente(),false,1);
//								}
//								else{
//									cb=pdf.addLabel(cb,240,208,8,polizaVO.getTelPartAgente(),false,1);
//								}
//
//							}
//
//
//
//
//
//
//
//							cb=pdf.addlineH(cb,31,206,343);
//							if(polizaVO.getDescOficina()!=null){
//								cb=pdf.addLabel(cb,100,198,8,polizaVO.getDescOficina(),false,1);}
//							if(polizaVO.getPoblacionOficina()!=null){
//								cb=pdf.addLabel(cb,350,198,8,polizaVO.getPoblacionOficina(),false,2);}							
//							if(polizaVO.getCalleOficina()!=null){						
//								cb=pdf.addLabel(cb,100,188,8,polizaVO.getCalleOficina(),false,1);}
//							if(polizaVO.getCodPostalOficina()!=null){										
//								cb=pdf.addLabel(cb,330,188,8,polizaVO.getCodPostalOficina(),false,1);}
//							if(polizaVO.getColoniaOficina()!=null){										
//								//cb=pdf.addLabel(cb,100,178,8,polizaVO.getColoniaOficina(),false,1);}
//								cb=pdf.addLabel(cb,60,178,8,polizaVO.getColoniaOficina(),false,1);}
//
//							//cb=pdf.addLabel(cb,240,178,8,"- - - - REPORTE DE SINIESTROS",false,1);
//							//cb=pdf.addlineH(cb,240,177,125);
//
//							if(polizaVO.getTelOficina()!=null){
//								//cb=pdf.addLabel(cb,100,168,8,polizaVO.getTelOficina(),false,1);}
//								cb=pdf.addLabel(cb,208,178,8,polizaVO.getTelOficina(),false,1);}
//
//							//if(polizaVO.getTelLocal()!=null){
//							//cb=pdf.addLabel(cb,230,168,8,polizaVO.getTelLocal(),false,1);
//							//}
//
//							if(polizaVO.getFaxOficina()!=null){					
//								cb=pdf.addLabel(cb,293,178,8,polizaVO.getFaxOficina(),false,1);}
//
//							//if(polizaVO.getTelNacional()!=null){
//							//	cb=pdf.addLabel(cb,230,158,8,"01-800-288-6700, 01-800-800-2880",false,1);
//							//}
//						}				
//
//						cb=pdf.addRectAng(cb,35,175,335,25);
//						cb=pdf.addLabel(cb,40,159,8,"EXCLUSIVO PARA REPORTE DE SINIESTROS",true,1);
//						cb=pdf.addLabel(cb,220,164,8,"  (55) 5258-2880    01-800-288-6700",true,1);
//						//cb=pdf.addLabel(cb,218,153,8,"01-800-004-9600    01-800-800-2880",true,1);
//						cb=pdf.addLabel(cb,218,153,8,"                                01-800-800-2880",true,1);
//
//						//************forma de pago
//						cb=pdf.addRectAng(cb,35,150,335,20);
//						cb=pdf.addLabel(cb,40,137,8,"FORMA DE PAGO:",false,1);
//						if(polizaVO != null){
//							if(polizaVO.getClavAgente()!=null && polizaVO.getClavAgente().trim().equals("55380")){
//							}
//							else {
//									cb=pdf.addLabel(cb,100,208,8,polizaVO.getClavAgente(),false,1);
//								
//								if(polizaVO.getDescrFormPago()!=null){
//										cb=pdf.addLabel(cb,120,137,8,polizaVO.getDescrFormPago(),false,1);
//								}
//	
//								//Imprime Primer Pago y Pagos subsecuentes (solo si no es pago de contado)
//								cb=pdf.addLabel(cb,310,141,8,StringUtils.isNotEmpty(polizaVO.getPrimerPago())?FormatDecimal.numDecimal(polizaVO.getPrimerPago()):"",false,1);
//								if(polizaVO.getNumRecibos() > 1){
//									cb=pdf.addLabel(cb,190,141,8,"PRIMER PAGO",false,1);
//									cb=pdf.addLabel(cb,190,132,8,"PAGO(S) SUBSECUENTE(S)",false,1);																						
//									cb=pdf.addLabel(cb,310,132,8,StringUtils.isNotEmpty(polizaVO.getPagoSubsecuente())?FormatDecimal.numDecimal(polizaVO.getPagoSubsecuente()):"",false,1);
//								}else{
//									cb=pdf.addLabel(cb,190,141,8,"PAGO UNICO",false,1);
//								}
//							}			
//						}
//
//						cb=pdf.addRectAng(cb,35,127,335,75);
//						cb=pdf.addLabel(cb,43,117,7.5f,"Quálitas  Compañia  de  Seguros, S.A.  de  C.V.  (en  lo  sucesivo  La  compia),  asegura   de",false,1);
//						cb=pdf.addLabel(cb,43,110,7.5f,"acuerdo de las  Condiciones  Generales  y  Especiales  de  esta Poliza, el vehiculo asegurado",false,1);
//						cb=pdf.addLabel(cb,43,103,7.5f,"contra  perdidas o  daños  causados por cualquiera de los Riesgos que se enumeran y que El",false,1);
//						cb=pdf.addLabel(cb,43,96,7.5f,"Asegurado haya contratado, en testimonio de lo cual, La Compañia firma la presente",false,1);
//
////						cb=pdf.addLabel(cb,43,80,7.5f,"Este  documento  y  la Nota Tecnica que lo  fundamenta  estan  registrados  ante  la Comision",false,1);
////						cb=pdf.addLabel(cb,43,73,7.5f,"Nacional de Seguros y Finanzas., de conformidad con lo dispuesto en los articulos 36, 36A,",false,1);
////						cb=pdf.addLabel(cb,43,66,7.5f,"36-B y 36-D de  la  Ley  General  de  Instituciones y  Sociedades  Mutualistas de Seguros, con",false,1);
////						cb=pdf.addLabel(cb,43,59,7.5f,"no. de Registro CNSF-S0046-0628-2005 de fecha 16 de agosto de 2006.",false,1);
//						
//						cb=pdf.addLabel(cb,43,81,7.5f,"En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de",false,1);
//						cb=pdf.addLabel(cb,43,74,7.5f,"Fianzas, la documentación contractual y la nota técnica que integran este producto de ",false,1);
//						cb=pdf.addLabel(cb,43,67,7.5f,"seguro,quedaron registrados ante la Comisión Nacional de Seguros y Fianzas a partir",false,1);
//						cb=pdf.addLabel(cb,43,60,7.5f,"del día",false,1);
//						if (polizaVO.getcNSF()!=null){
//							cb=pdf.addLabel(cb,43,53,7.5f,polizaVO.getcNSF(),false,1);
//						}
//						
//						//CHAVA-LEYENDA ARTICULO 25
//						cb=pdf.addLabelr(cb,15,50,6.5f,"Artículo 25 de la ley sobre el Contrato de Seguro. \"Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el Asegurado podrá pedir la rectificación correspondiente",true,1,90,0,0,0);
//						cb=pdf.addLabelr(cb,23,50,6.5f,"dentro de los treinta (30) días que sigan al día en que reciba su póliza, transcurrido este plazo se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones.\"",true,1,90,0,0,0);
//
//
//
//						//************importe
//						cb=pdf.addRectAngColor(cb,390,242,180,13);
//						cb=pdf.addLabel(cb,395,232,10,"MONEDA",true,1);
//						if(polizaVO.getDescMoneda()!=null){
//							cb=pdf.addLabel(cb,560,232,10,polizaVO.getDescMoneda(),true,2);}
//
//						cb=pdf.addRectAng(cb,390,226,180,13);										
//						cb=pdf.addRectAng(cb,390,210,180,83);
//
//						cb=pdf.addLabel(cb,395,200,8,"PRIMA NETA",false,1);
//						cb=pdf.addLabel(cb,395,190,8,"TASA FINANCIAMIENTO POR PAGO",false,1);
//						cb=pdf.addLabel(cb,395,180,8,"FRACCIONADO",false,1);
//						cb=pdf.addLabel(cb,395,167,8,"GTOS. EXPEDICION POL.",false,1);
//						cb=pdf.addLabel(cb,395,147,8,"SUBTOTAL",false,1);
//						cb=pdf.addLabel(cb,395,135,8,"I.V.A.",false,1);
//						cb=pdf.addLabel(cb,395,116,8,"IMPORTE TOTAL",true,1);
//						cb=pdf.addLabel(cb,395,100,8,"CONDICIONES VIGENTES:",false,1);
//						cb=pdf.addLabel(cb,395,88,8,"TARIFA APLICADA:",false,1);
//
//						if(polizaVO != null){
//							//la información siguiente va de prima neta a tarifa aplicada
//							if(polizaVO.getPrimaNeta()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(primaAux),false,2);
//								}
//								else
//									cb=pdf.addLabel(cb,560,200,8,FormatDecimal.numDecimal(polizaVO.getPrimaNeta()),false,2);
//							}
//							/*	if(polizaVO.getRecargo()!=null){ 
//									    if(Double.parseDouble(polizaVO.getRecargo())>0){
//									    	cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);}
//									}*/
//							if(polizaVO.getRecargo()!=null){
//								if(Double.parseDouble(polizaVO.getRecargo())>0){
//									if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//										recargoAux=Double.parseDouble(polizaVO.getRecargo());
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
//									}
//									else{
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
//									}
//								}else{
//									if(Integer.parseInt(polizaVO.getNumIncisos())<0){
//										recargoAux=Double.parseDouble(polizaVO.getRecargo());
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(recargoAux),false,2);
//									}else{
//										cb=pdf.addLabel(cb,560,180,8,FormatDecimal.numDecimal(polizaVO.getRecargo()),false,2);
//									}
//								}
//							}
//							if(polizaVO.getDerecho()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									derechoAux=Double.parseDouble(polizaVO.getDerecho())/Integer.parseInt(polizaVO.getNumIncisos());
//									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(derechoAux),false,2);
//
//								}
//								else{
//									cb=pdf.addLabel(cb,560,167,8,FormatDecimal.numDecimal(polizaVO.getDerecho()),false,2);}
//							}
//							
//							
//							if(polizaVO.getCesionComision()!=null){
//								    cb=pdf.addLabel(cb,395,157,8,"DESCUENTOS",false,1);
//									cb=pdf.addLabel(cb,560,157,8,polizaVO.getCesionComision(),false,2);
//							}
//							
//							
//							if(polizaVO.getPrimaTotal()!=null && polizaVO.getImpuesto()!=null){								
//
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									subtotalAux = primaAux+derechoAux;				
//								}
//								else{
//									subtotalAux = Double.parseDouble(polizaVO.getPrimaTotal())-Double.parseDouble(polizaVO.getImpuesto());}
//
//								cb=pdf.addLabel(cb,560,147,8,FormatDecimal.numDecimal(subtotalAux),false,2);
//							}
//
//							cb=pdf.addlineH(cb,460,143,115);
//							if(polizaVO.getImpuesto()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){		
//									if(polizaVO.getIva()!=null){
//										impuestoAux=subtotalAux*(Double.parseDouble(polizaVO.getIva())/10000);
//										cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(impuestoAux),false,2);
//									}
//								}
//								else{
//									cb=pdf.addLabel(cb,560,135,8,FormatDecimal.numDecimal(polizaVO.getImpuesto()),false,2);}
//							}
//
//							cb=pdf.addRectAng(cb,390,125,180,13);
//							if(polizaVO.getPrimaTotal()!=null){
//								if(Integer.parseInt(polizaVO.getNumIncisos())>1){
//									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(impuestoAux+subtotalAux),true,2);
//								}
//								else{
//									cb=pdf.addLabel(cb,560,116,8,FormatDecimal.numDecimal(polizaVO.getPrimaTotal()),true,2);}
//							}
//
//							//***************************
//
//							if(polizaVO.getDescConVig()!=null){									
//								cb=pdf.addLabel(cb,555,100,8,polizaVO.getDescConVig(),false,2);
//							}
//
//							cb=pdf.addRectAng(cb,390,110,180,25);
//							if(polizaVO.getClaveOfic()!=null && polizaVO.getTarifa()!=null){
//								//String concatZonaId= "0000"+polizaVO.getClaveOfic();
//
//								if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&polizaVO.getCveServ().trim().equals("4")){
//
//									String concatZonaId= "0000"+polizaVO.getTarifApDesc();
//									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
//									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//								else if(StringUtils.isNotEmpty(polizaVO.getCveServ())&&!polizaVO.getCveServ().trim().equals("4")){
//
//									String concatZonaId= "0000"+polizaVO.getTarifApCve();
//									//cb=pdf.addLabel(cb,560,97,8,"0"+String.valueOf(polizaVO.getTarifa())+StringUtils.right(concatZonaId,4),false,2);
//									cb=pdf.addLabel(cb,555,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//								}
//
//								//cb=pdf.addLabel(cb,550,88,8,"0"+polizaVO.getTarifa()+StringUtils.right(concatZonaId,4),false,2);
//							}								
//
//						}
//
//						//****firma
//						if(polizaVO != null){
//							String lugar="";
//							if(polizaVO.getDescOficina()!=null){lugar=polizaVO.getDescOficina();}
//							if(polizaVO.getPoblacionOficina()!=null){lugar=lugar+","+polizaVO.getPoblacionOficina();}
//							cb=pdf.addLabel(cb,490,75,8,lugar,false,0);
//							//ANDRES-FechaEmision en lugar de fecha de inicio de vigencia
//							if(polizaVO.getFchEmi()!=null){
//								cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchEmi()),false,0);}
//							/*if(polizaVO.getFchIni()!=null){
//									cb=pdf.addLabel(cb,490,65,8,"A "+DateFormat.addFechaString(polizaVO.getFchIni()),false,0);}*/
//						}
//
//
//						if(polizaVO.getDirImagen()!=null){
//							document=pdf.addImage(document,450,35,80,30,polizaVO.getDirImagen()+"images/firma.jpg");
//							cb=pdf.addLabel(cb,490,35,7,"JUAN JOSE RODRIGUEZ TELLEZ",false,0);	
//						}
//						cb=pdf.addLabel(cb,490,28,7,"FIRMA Y NOMBRE DEL FUNCIONARIO",false,0);
//						cb=pdf.addLabel(cb,490,21,7,"AUTORIZADO",false,0);	
//
//
//						if(polizaVO.getDescConVig()!=null){									
//							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables "+polizaVO.getDescConVig(),true,0);
//							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
//						}
//						else{
//							cb=pdf.addLabel(cb,210,42,7,"El asegurado recibe la impresión de la póliza junto con las condiciones generales aplicables",true,0);
//							cb=pdf.addLabel(cb,180,35,7,"mismas que ademas puede consultar  e imprimir en nuestra pagina www.qualitas.com.mx",true,0);
//						}
//
//
//						//ANDRES-MEM
//						if (membretado==null||membretado.equals("S")){
//							cb=pdf.addLabelPurple(cb,30,22,7,"Quálitas Compañia de Seguros, S.A. de C.V. | José Ma. Castorena No. 426 Col. San José de los Cedros, Cuajimalpa 05200 México, D.F.",false,1);
//							cb=pdf.addLabelPurple(cb,30,16,7,"Reporte de Siniestros 01800 800 2880 | 01800 288 6700 Centro de Contacto Quálitas 01800 800 2021",false,1);	
//							//document=pdf.addImageWaterMark(document,610,660,140,-145,"C:/appsWKSP/P_AGENTES/operation_files/pdflogo/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//							document=pdf.addImageWaterMark(document,610,660,140,-145,polizaVO.getDirImagen()+"images/Qmembretado.jpg",cb=writer.getDirectContentUnder());
//						}


					}
					catch (DocumentException e1) {
						log.error(e1.getMessage(), e1);
					}
					//toppage=toppage+27;}//fin del for de las paginas
					toppage=0;





				}//fin del for de las copias de las polizas

			}//fin del for de las polizas
		}					
		catch(DocumentException de) {
			log.error(de.getMessage(), de);
		}				
		catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		document.close();
	}	
}
