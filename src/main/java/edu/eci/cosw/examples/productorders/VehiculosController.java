package edu.eci.cosw.examples.productorders;

import edu.eci.cosw.examples.productorders.services.ApplicationServices;
import edu.eci.cosw.examples.productorders.services.ServicesException;
import edu.eci.cosw.samples.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by USUARIO on 07/03/2017.
 */
@RestController
@RequestMapping(path = "/vehiculos")
public class VehiculosController {
    @Autowired
    ApplicationServices services;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Vehiculo>> getVehiculo(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok().body(services.getVehiculos(id));
        } catch (ServicesException ex) {
            Logger.getLogger(DispatchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
