/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cosw.examples.productorders;

import edu.eci.cosw.samples.model.Despacho;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.cosw.samples.model.Pedido;
import edu.eci.cosw.samples.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import edu.eci.cosw.examples.productorders.services.ApplicationServices;
import edu.eci.cosw.examples.productorders.services.ServicesException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(path = "/dispatches")
public class DispatchController {

    @Autowired
    ApplicationServices services;

    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Despacho> getDespacho(@PathVariable("id") int id) {        
        try {
            return ResponseEntity.ok().body(services.dispatchByID(id));
        } catch (ServicesException ex) {
            Logger.getLogger(DispatchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/qrcode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getQRCode(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(new InputStreamResource(services.dispatchByID(id).getQrcode().getBinaryStream()));
        } catch (ServicesException ex) {
            Logger.getLogger(DispatchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (SQLException ex) {
            Logger.getLogger(DispatchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(MultipartHttpServletRequest request, @RequestParam(name = "idpedido")int idpedido, @RequestParam(name =  "idvehiculo") String idVehiculo){
        try{
            Iterator<String> itr = request.getFileNames();
            while(itr.hasNext()){
                String uploadFile = itr.next();
                MultipartFile file = request.getFile(uploadFile);

                Pedido pedido = services.orderById(idpedido);
                Vehiculo vehiculo = services.vehicleById(idVehiculo);
                Despacho despacho = new Despacho(pedido, vehiculo);
                despacho.setQrcode(new SerialBlob(StreamUtils.copyToByteArray(file.getInputStream())));

                services.addDispatch(despacho);
            }
        }catch(Exception e){
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("{}",HttpStatus.OK);
    }
}
