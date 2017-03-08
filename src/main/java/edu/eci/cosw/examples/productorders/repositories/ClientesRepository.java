package edu.eci.cosw.examples.productorders.repositories;

import edu.eci.cosw.samples.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by USUARIO on 07/03/2017.
 */
public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
    @Query("select distinct clientes from Pedido pedido "
            + "inner join pedido.cliente as clientes "
            + "inner join pedido.detallesPedidos as detalle "
            + "inner join detalle.producto as producto "
            + "where producto.precio > ?1")
    List<Cliente> getCLientes(long value);
}
