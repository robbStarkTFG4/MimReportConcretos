/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.controllers;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mim.models.Equipo;
import com.mim.models.Fotos;
import com.mim.models.HistorialDetalles;
import com.mim.models.Lugar;
import com.mim.models.Orden;
import com.mim.session.FotosFacade;
import com.mim.session.HistorialDetallesFacade;
import com.mim.session.OrdenFacade;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcoisaac
 */
@Named("home")
@ViewScoped
public class HomeCtrl implements Serializable {

    private List<Orden> recentOrders;
    private Orden current;

    private List<HistorialDetalles> detallesList;
    private List<Fotos> fotosList;

    private long cantidad;
    private String ordenId;

    @EJB
    OrdenFacade ordenFacade;

    @EJB
    HistorialDetallesFacade hisFacade;

    @EJB
    FotosFacade fotoFacade;

    @Inject
    OrdenCtrl ordenCtrl;
    private Equipo equipo;

    @PostConstruct
    private void init() {
        recentOrders = ordenFacade.findARecent(ordenCtrl.getUser());
        cantidad = ordenFacade.countValid();
    }

    public void onRowSelect(SelectEvent event) {
        current = (Orden) event.getObject();
        System.out.println(current.getDescripcion());
        detallesList = hisFacade.findAllByOrder(current.getIdorden());
        fotosList = fotoFacade.findAllByOrder(current.getIdorden());
    }

    public String buildReport() {

        System.out.println("orden No. " + ordenId);

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        //response.setContentType("application/pdf");
        //response.setHeader("Content-disposition", "inline=filename=file.pdf");
      

            // Get the text that will be added to the PDF
            current = ordenFacade.find(Integer.parseInt(ordenId));
            System.out.println("equipo: " + current.getEquipoIdequipo().getLugarIdlugar().getNombre());
            // step 1
            Document document = new Document(new Rectangle(800, 700), 7f, 7f, 50f, 7f);

            if (ordenId.equals("n/a")) {
                current.setNumeroOrden(current.getActividad());
            }

            equipo = current.getEquipoIdequipo();
            Lugar lugar = equipo.getLugarIdlugar();

            //PdfWriter.getInstance(document, new FileOutputStream(orden.getNumeroOrden() + ".pdf"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
      
            document.open();
            ordenReport(document, lugar);
            //document.add(new Paragraph("dsadasdas"));
            document.close();

            // step 2
            // setting some response headers
            response.setHeader("Content-disposition", "attachment; filename=" + current.getNumeroOrden() + ".pdf");// esto hizo que fuera descarga directa
            //response.setHeader("Expires", "0");
            //response.setHeader("Cache-Control",
            //        "must-revalidate, post-check=0, pre-check=0");
            //response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            try ( // write ByteArrayOutputStream to the ServletOutputStream
                OutputStream os = response.getOutputStream()) {
                baos.writeTo(os);
                os.flush();
            }
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(HomeCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }

        context.responseComplete();
        return null;

    }

    private void ordenReport(Document document, Lugar lugar) throws BadElementException, DocumentException, IOException {
        document.addTitle(current.getNumeroOrden());
        // step 3

        // step 4
        PdfPTable table = new PdfPTable(8);

        //LEFT HEADER CONTENT
        PdfPTable leftHeaderTable = new PdfPTable(4);

        PdfPCell imgCell = new PdfPCell();
        imgCell.setBorder(Rectangle.NO_BORDER);
        imgCell.setPaddingTop(14);
        imgCell.setColspan(1);
        imgCell.setFixedHeight(25);

        //Image img = Image.getInstance("/opt/shared/home/logo.png");
        Image img = Image.getInstance("http://mimconstructions.com/img/mim%20trendy.png");

        imgCell.addElement(img);

        PdfPCell reportTitleCell = new PdfPCell(new Paragraph("REPORTE MANTENIMIENTO"));
        reportTitleCell.setPaddingTop(14);
        reportTitleCell.setPaddingLeft(20);
        reportTitleCell.setColspan(3);
        reportTitleCell.setBorder(Rectangle.NO_BORDER);

        leftHeaderTable.addCell(imgCell);
        leftHeaderTable.addCell(reportTitleCell);

        PdfPCell leftHeaderMainCell = new PdfPCell(leftHeaderTable);
        leftHeaderMainCell.setColspan(4);

        //END CONTENT
        //RIGHT HEADER WITH INFO ABOUT ORDER AND DATE
        PdfPTable infHeader = new PdfPTable(3);

        PdfPCell numberOrderLabel = new PdfPCell(new Paragraph("#ORDEN"));
        numberOrderLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        numberOrderLabel.setColspan(1);
        String numeroOrden = null;
        if (current.getNumeroOrden() != null) {
            numeroOrden = current.getNumeroOrden();
        } else {
            numeroOrden = current.getActividad();
        }

        PdfPCell numberOrderValue = new PdfPCell(new Paragraph(numeroOrden));
        numberOrderValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        numberOrderValue.setColspan(2);

        PdfPCell prioridadLabel = new PdfPCell(new Paragraph("PRIORIDAD"));
        prioridadLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        prioridadLabel.setColspan(1);

        PdfPCell prioridadValue = new PdfPCell(new Paragraph(current.getPrioridad()));
        prioridadValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        prioridadValue.setColspan(2);

        PdfPCell fechaLabel = new PdfPCell(new Paragraph("FECHA"));
        fechaLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechaLabel.setColspan(1);
        //dd-MM-yyyy
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = current.getStartDate();
        if (startDate == null) {
            startDate = new Date();
        }
        String fecha = format1.format(startDate);

        PdfPCell fechaValue = new PdfPCell(new Paragraph(fecha));
        fechaValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechaValue.setColspan(2);

        infHeader.addCell(numberOrderLabel);
        infHeader.addCell(numberOrderValue);
        infHeader.addCell(prioridadLabel);
        infHeader.addCell(prioridadValue);
        infHeader.addCell(fechaLabel);
        infHeader.addCell(fechaValue);

        PdfPCell cellHeaderRight
                = new PdfPCell(infHeader);
        cellHeaderRight.setColspan(4);
        //END HEADER

        PdfPCell areaLabel = new PdfPCell(new Paragraph("AREA"));
        areaLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        areaLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        areaLabel.setFixedHeight(30);
        areaLabel.setPaddingTop(5);
        areaLabel.setColspan(2);

        PdfPCell actividadLabel = new PdfPCell(new Paragraph("ACTIVIDAD"));
        actividadLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        actividadLabel.setFixedHeight(30);
        actividadLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        actividadLabel.setPaddingTop(5);
        actividadLabel.setColspan(3);

        PdfPCell responsableLabel = new PdfPCell(new Paragraph("RESPONSABLE DE OPERACION"));
        responsableLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        responsableLabel.setFixedHeight(30);
        responsableLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        responsableLabel.setPaddingTop(5);
        responsableLabel.setColspan(3);

        PdfPCell areaValor = new PdfPCell(new Paragraph("concretera"));
        areaValor.setFixedHeight(25);
        areaValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        areaValor.setColspan(2);

        PdfPCell actividadValor = new PdfPCell(new Paragraph(current.getActividad()));
        actividadValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        actividadValor.setFixedHeight(25);
        actividadValor.setColspan(3);

        PdfPCell responsableValor = new PdfPCell(new Paragraph(current.getEncargado()));
        responsableValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        responsableValor.setFixedHeight(25);
        responsableValor.setColspan(3);

        // 2 FILAS PARA INF. EQUIPO Y LUGAR
        PdfPCell equipoLabel = new PdfPCell(new Paragraph("EQUIPO/CONJUNTO"));
        equipoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        equipoLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        equipoLabel.setFixedHeight(30);
        equipoLabel.setPaddingTop(5);
        equipoLabel.setColspan(4);

        PdfPCell lugarLabel = new PdfPCell(new Paragraph("LUGAR"));
        lugarLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        lugarLabel.setFixedHeight(30);
        lugarLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        lugarLabel.setPaddingTop(5);
        lugarLabel.setColspan(4);

        String numeroEquipo = null;
        if (equipo.getNumeroEquipo() != null) {
            numeroEquipo = equipo.getNumeroEquipo();
        } else {
            numeroEquipo = "n/a";
        }

        PdfPCell equipoValor = new PdfPCell(new Paragraph(numeroEquipo));
        equipoValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        equipoValor.setFixedHeight(25);
        equipoValor.setColspan(4);

        PdfPCell lugarValor = new PdfPCell(new Paragraph(lugar.getNombre()));
        lugarValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        lugarValor.setFixedHeight(25);
        lugarValor.setColspan(4);

        //END INFO EQUIPO
        // 4 ROW and 5 ROW
        PdfPCell descripcionLabel = new PdfPCell(new Paragraph("DESCRIPCION"));
        descripcionLabel.setPadding(12);
        descripcionLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        descripcionLabel.setColspan(8);

        PdfPCell descripcionValor = new PdfPCell(new Paragraph(current.getDescripcion()));
        descripcionValor.setPadding(10);
        descripcionValor.setColspan(8);
        //END ROWS

        //ROW BEFORE HISTORIAL_DETALLES
        PdfPCell historialLabel = new PdfPCell(new Paragraph("OBSERVACIONES"));
        historialLabel.setPadding(12);
        historialLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        historialLabel.setColspan(8);
        //END HISTORIAL

        table.addCell(leftHeaderMainCell);
        table.addCell(cellHeaderRight);
        table.addCell(areaLabel);
        table.addCell(actividadLabel);
        table.addCell(responsableLabel);

        table.addCell(areaValor);
        table.addCell(actividadValor);
        table.addCell(responsableValor);
        table.addCell(equipoLabel);
        table.addCell(lugarLabel);
        table.addCell(equipoValor);
        table.addCell(lugarValor);
        table.addCell(descripcionLabel);
        table.addCell(descripcionValor);

        List<HistorialDetalles> observaciones = hisFacade.findAllByOrder(current.getIdorden());

        if (observaciones != null) {
            if (observaciones.size() > 0) {
                table.addCell(historialLabel);

                //LOOP HISTORIAL_DETALLES
                for (int i = 0; i < observaciones.size(); i++) {
                    HistorialDetalles historial = observaciones.get(i);

                    if(historial.getValor()!=null){
                        if(historial.getValor().length()>0){
                          PdfPCell paramCell = new PdfPCell();
                         paramCell.setColspan(3);
                        paramCell.addElement(new Paragraph(historial.getParametro()));
                        paramCell.setVerticalAlignment(Element.ALIGN_CENTER);
                       paramCell.setPaddingLeft(10);
                         paramCell.setPaddingBottom(10);

                        PdfPCell valueParamCell = new PdfPCell();
                         valueParamCell.setColspan(5);
                        valueParamCell.setPaddingLeft(10);
                         valueParamCell.setVerticalAlignment(Element.ALIGN_CENTER);
                        valueParamCell.addElement(new Paragraph(historial.getValor()));
                          valueParamCell.setPaddingBottom(10);

                         table.addCell(paramCell);
                         table.addCell(valueParamCell);
                        }
                    }
                }
            }
        }
        //END LOOP HISTORIAL

        // FIRST ROWS OF FOTOGRAPHIC REPORT
        PdfPCell pasoLabel = new PdfPCell(new Paragraph("PASO"));
        pasoLabel.setFixedHeight(20);
        pasoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        pasoLabel.setColspan(1);

        PdfPCell accionLabel = new PdfPCell(new Paragraph("ACCION"));
        accionLabel.setFixedHeight(20);
        accionLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        accionLabel.setColspan(3);

        PdfPCell imagenLabel = new PdfPCell(new Paragraph("IMAGENES"));
        imagenLabel.setFixedHeight(20);
        imagenLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagenLabel.setColspan(4);
        //END ROWS

        PdfPTable table2 = new PdfPTable(8);

        //ROW BEFORE HISTORIAL_DETALLES
        PdfPCell headerPictures = new PdfPCell(new Paragraph("PROCEDIMIENTO"));
        headerPictures.setPadding(12);
        headerPictures.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerPictures.setColspan(8);

        table2.addCell(headerPictures);
        //END HISTORIAL

        table2.addCell(pasoLabel);
        table2.addCell(accionLabel);
        table2.addCell(imagenLabel);

        //fotos loop
        List<Fotos> fotos = fotoFacade.findAllByOrder(current.getIdorden());

        for (int i = 0; i < fotos.size(); i++) {
            Fotos foto = fotos.get(i);
            PdfPCell pasoVal = new PdfPCell(new Paragraph(String.valueOf(i)));
            pasoVal.setHorizontalAlignment(Element.ALIGN_CENTER);
            pasoVal.setColspan(1);

            PdfPCell detail = new PdfPCell(new Paragraph(foto.getTitulo()));
            detail.setPadding(5);
            detail.setBorder(Rectangle.NO_BORDER);

            PdfPCell accionVal = new PdfPCell();
            accionVal.addElement(new Paragraph(foto.getDescripcion()));
            accionVal.setHorizontalAlignment(Element.ALIGN_CENTER);
            accionVal.setBorder(Rectangle.NO_BORDER);
            //accionVal.setColspan(3);

            PdfPTable infoTable = new PdfPTable(1);
            infoTable.addCell(detail);
            infoTable.addCell(accionVal);

            PdfPCell infiCell = new PdfPCell();
            infiCell.setColspan(3);
            infiCell.addElement(infoTable);

            //Table collumn
            //System.getenv("OPENSHIFT_DATA_DIR") + "imagenes/" + name)
            //Image imgFoto = Image.getInstance("http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources/com.mim.entities.fotos/api/" + foto.getIdfotos());
            String archivo = foto.getArchivo();
            String[] split = archivo.split("/");
            int size = split.length;
            final String name = split[size - 1];
            System.out.println("Valor " + name);

            Image imgFoto = Image.getInstance("/opt/shared/home/" + "imagenes/" + name);

            PdfPTable imagenTable = new PdfPTable(1);

            PdfPCell fotoCell = new PdfPCell();
            fotoCell.setColspan(1);
            fotoCell.addElement(imgFoto);
            fotoCell.setFixedHeight(310);
            fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fotoCell.setBorder(Rectangle.NO_BORDER);

            imagenTable.addCell(fotoCell);

            PdfPCell imagenVal = new PdfPCell();
            imagenVal.setColspan(4);
            imagenVal.addElement(imagenTable);

            table2.addCell(pasoVal);
            table2.addCell(infiCell);
            table2.addCell(imagenVal);
        }
        //end loop

        //table.addCell(tiempoLabel);
        document.add(table);
        document.newPage();
        document.add(table2);
    }

    public List<Orden> getRecentOrders() {
        return recentOrders;
    }

    public Orden getCurrent() {
        return current;
    }

    public void setCurrent(Orden current) {
        this.current = current;
    }

    public List<HistorialDetalles> getDetallesList() {
        return detallesList;
    }

    public List<Fotos> getFotosList() {
        return fotosList;
    }

    public long getCantidad() {
        return cantidad;
    }

    public String getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(String ordenId) {
        this.ordenId = ordenId;
    }

}
