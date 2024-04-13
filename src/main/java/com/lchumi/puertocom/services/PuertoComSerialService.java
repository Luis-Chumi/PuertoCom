/*
 * Copyright (c) 2024 Luis Chumi.
 * Este software está licenciado bajo la Licencia Pública General de GNU versión 3.
 * Puedes encontrar una copia de la licencia en https://www.gnu.org/licenses/gpl-3.0.html.
 *
 * Para consultas o comentarios, puedes contactarme en luischumi.9@gmail.com
 */

package com.lchumi.puertocom.services;


import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PuertoComSerialService {

    private SerialPort serialPort;
    private final POS pos = new POS(true);

    public List<String> listaPuerto() {
        List<String> listaPuertosCom= new ArrayList<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            log.error("No se encontraron puertos COM disponibles");
            return new ArrayList<>();
        } else {
            log.info("Puertos COM disponibles ");
            for (SerialPort port : ports) {
                log.info("Puerto encontrado: {} ", port.getDescriptivePortName());
                listaPuertosCom.add(port.getDescriptivePortName());
            }
        }
        return listaPuertosCom;
    }

    public void connect(String portName, int baudRate){
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()){
            log.info("Puerto serial {} abierto correctamente," , portName);
        }else {
            log.error("Error al abir el puerto serial {}", portName);
        }
    }

    public void disconnect() {
        if (serialPort != null || serialPort.isOpen()) {
            serialPort.closePort();
            log.info("Puerto serial {} desconectado", serialPort.getDescriptivePortName());
        }
    }

    public String sendData(String data){
        if (serialPort == null || !serialPort.isOpen()) {
            log.error("El puerto serial no está abierto.");
            return null;
        }
        return null;
    }

    public String receiveData(){
        if (serialPort == null || !serialPort.isOpen()){
            log.error("El puerto serial no esta abierto. ");
            return null;
        }

        try {
            InputStream inputStream = serialPort.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead != -1) {
                String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                log.info("Datos recividos correctamente: {} ", receivedData);
                return receivedData;
            }else {
                log.warn("No se recivieron datos.");
                return "No se recivieron datos correctamente.";
            }
        }catch (Exception e){
            log.error("Error al recibir datos en el puerto serial: {}", e.getMessage(), e);
            return "Error al recibir datos en el puerto serial.";
        }
    }

    public boolean conectrarPuertoCom() {
        return pos.ConfigurarConexionPOS("COM4",9600,8,true);
    }

    public boolean desconectarPuertoCom() {
        return pos.ConfigurarConexionPOS("COM4",9600,8,false);
    }

    public boolean desconectarPOS() {
        return pos.DesconectarPuerto();
    }

    public DatosRecepcion recibir(DatosEnvio datosEnvio) throws Exception {
        return pos.ProcesarPago(datosEnvio);
    }
}
