package edu.eci.cosw.examples.productorders;

import edu.eci.cosw.examples.productorders.services.ApplicationServices;
import edu.eci.cosw.examples.productorders.services.ServicesException;
import edu.eci.cosw.samples.model.Cliente;
import org.hibernate.service.spi.ServiceException;
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
@RequestMapping(path = "/clientes")
public class ClientesController {
    @Autowired
    ApplicationServices services;

    @RequestMapping(path = "/{value}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Cliente>> getClientes(@PathVariable("value") long value) throws ServicesException {
        try{
            return ResponseEntity.ok().body(services.getClientes(value));
        }catch(ServiceException ex){
            Logger.getLogger(DispatchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
