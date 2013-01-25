/**
 * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 * <p/>
 * *************************************************************************** */
package com.oracle.demo.ops.rest.v1;

import com.oracle.demo.ops.domain.Address;
import com.oracle.demo.ops.domain.Parcel;
import com.oracle.demo.ops.domain.Shipment;
import com.oracle.demo.ops.entitymanager.ParcelManager;
import com.oracle.demo.ops.entitymanager.ShipmentManager;

import javax.naming.InitialContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

/**
 * REST Web Service
 *
 * @author sbutton
 */
@Path("shipment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentResource
{
  @Context
  private UriInfo context;

  protected ParcelManager parcelManager;
  protected ShipmentManager shipmentManager;

  public ShipmentResource()
  {
    try
    {
      InitialContext ic = new InitialContext();
      parcelManager = (ParcelManager) ic.lookup("java:comp/env/ejb/ParcelManager");
      shipmentManager = (ShipmentManager) ic.lookup("java:comp/env/ejb/ShipmentManager");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /* ****************************************************************************************
    LIST ALL SHIPMENTS
   **************************************************************************************** */

  private Collection<Shipment> findAllShipments()
  {
    return shipmentManager.findAllShipments();
  }

  @GET
  @Path("/all")
  public Collection<Shipment> listShipments_default()
  {
    return findAllShipments();
  }

  @GET
  @Path("/all.json")
  public Collection<Shipment> listShipments_json()
  {
    return findAllShipments();
  }

  @GET
  @Path("/all.xml")
  @Produces(MediaType.APPLICATION_XML)
  public Collection<Shipment> listShipments_xml()
  {
    return findAllShipments();
  }

  /* ****************************************************************************************
    GET SHIPMENT
   **************************************************************************************** */

  private Shipment getShipmentById(int id)
  {
    return shipmentManager.findShipmentById(id);
  }

  @GET
  @Path("{id}")
  public Shipment getShipmentById_default(@PathParam("id") int id)
  {
    return getShipmentById(id);
  }

  @GET
  @Path("{id}.json")
  public Shipment getShipmentById_json(@PathParam("id") int id)
  {
    return getShipmentById(id);
  }

  @GET
  @Path("{id}.xml")
  @Produces(MediaType.APPLICATION_XML)
  public Shipment getShipmentById_xml(@PathParam("id") int id)
  {
    return getShipmentById(id);
  }

  /**
   * PUT method for updating or creating an instance of ParcelServiceResource
   *
   * @param shipment representation for the resource
   * @return an HTTP response with content of the updated or created resource.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Shipment putJSON(Shipment shipment)
  {
    return shipmentManager.createShipment(shipment);
  }

  /**
   * POST method for updating or creating an instance of ParcelServiceResource
   *
   * @param shipment representation for the resource
   * @return an HTTP response with content of the updated or created resource.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Shipment postJSON(Shipment shipment)
  {
    return shipmentManager.createShipment(shipment);
  }

  @GET
  @Path("/prototype.json")
  @Produces("application/json")
  public Shipment getShipmentPrototype()
  {
    Shipment shipment = new Shipment();
    Address blankAddr = new Address();

    blankAddr.setAddressee("");
    blankAddr.setAddressLine1("");
    blankAddr.setAddressLine2("");
    blankAddr.setCity("");
    blankAddr.setState("");
    blankAddr.setPostalCode("");

    shipment.setFromAddress(blankAddr);
    shipment.setToAddress(blankAddr);
    shipment.getParcels().clear();

    return shipment;
  }


  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Shipment createShipment(
          // toAddress
          @FormParam("to_addressLine1") String to_addressLine1,
          @FormParam("to_addressLine2") String to_addressLine2,
          @FormParam("to_city") String to_city,
          @FormParam("to_state") String to_state,
          @FormParam("to_postalCode") String to_postalCode,
          // fromAddress
          @FormParam("from_addressLine1") String from_addressLine1,
          @FormParam("from_addressLine2") String from_addressLine2,
          @FormParam("from_city") String from_city,
          @FormParam("from_state") String from_state,
          @FormParam("from_postalCode") String from_postalCode,
          // Parcel
          @FormParam("parcel_contents") String parcel_contents,
          @FormParam("parcel_weight") String parcel_weight,
          @FormParam("parcel_height") String parcel_height,
          @FormParam("parcel_width") String parcel_width,
          @FormParam("parcel_length") String parcel_length
                                )
  {

    Address toAddress = new Address();
    toAddress.setAddressLine1(to_addressLine1);
    toAddress.setAddressLine2(to_addressLine2);
    toAddress.setCity(to_city);
    toAddress.setState(to_state);
    toAddress.setPostalCode(to_postalCode);

    Address fromAddress = new Address();
    fromAddress.setAddressLine1(from_addressLine1);
    fromAddress.setAddressLine2(from_addressLine2);
    fromAddress.setCity(from_city);
    fromAddress.setState(from_state);
    fromAddress.setPostalCode(from_postalCode);

    Parcel parcel = new Parcel();
    parcel.setContents(parcel_contents);
    parcel.setWeight(Integer.parseInt(parcel_weight));
    parcel.setHeight(Integer.parseInt(parcel_height));
    parcel.setLength(Integer.parseInt(parcel_length));

    Shipment shipment = new Shipment();
    shipment.setToAddress(toAddress);
    shipment.setFromAddress(fromAddress);
    shipment.getParcels().add(parcel);

    Shipment ret = shipmentManager.createShipment(shipment);
    return ret;
  }


}