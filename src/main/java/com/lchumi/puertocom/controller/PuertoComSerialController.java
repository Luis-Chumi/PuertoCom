/*
 * Copyright (c) 2024 Luis Chumi.
 * Este software está licenciado bajo la Licencia Pública General de GNU versión 3.
 * Puedes encontrar una copia de la licencia en https://www.gnu.org/licenses/gpl-3.0.html.
 *
 * Para consultas o comentarios, puedes contactarme en luischumi.9@gmail.com
 */

package com.lchumi.puertocom.controller;

import com.lchumi.puertocom.services.PuertoComSerialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com")
@CrossOrigin("*")
@Slf4j
public class PuertoComSerialController {

    @Autowired
    private PuertoComSerialService puertoComSerialService;

    @GetMapping("/listaPuertosCom")
    public List<String> listaPuertosCom() {
        return puertoComSerialService.listaPuerto();
    }

    @PostMapping("/connect")
    public String connectSerialPort(@RequestParam String portName, @RequestParam int baudRate) {
        puertoComSerialService.connect(portName, baudRate);
        return "Puerto serial conectado: " + portName;
    }

    @PostMapping("/disconnect")
    public String disconnectSerialPort() {
        puertoComSerialService.disconnect();
        return "Puerto desconectado";
    }

    @GetMapping("/receive")
    public String receiveSerialPort() {
        return puertoComSerialService.receiveData();
    }

    @GetMapping("/datapos")
    public void pruebaDatapos() throws Exception {
         puertoComSerialService.prueba();
    }

}
