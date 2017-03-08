package edu.eci.cosw.examples.productorders.repositories;

import edu.eci.cosw.samples.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by USUARIO on 07/03/2017.
 */
public interface VehiculosRepository extends JpaRepository<Vehiculo, String> {
    @Query("select distinct vehiculo from Despacho despacho inner join despacho.pedidos as pedidos "+
            "inner join pedidos.detallesPedidos as detalles"+" inner join detalles.producto as producto "+
            "inner join despacho.vehiculo as vehiculo "+"where producto.idproducto = ?1")
    List<Vehiculo> getVehiculos(int idproducto);
}
