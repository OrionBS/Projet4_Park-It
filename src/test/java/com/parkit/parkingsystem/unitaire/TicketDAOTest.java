package com.parkit.parkingsystem.unitaire;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;
    private static TicketDAO ticketDAO;
    private static ParkingSpot parkingSpot;
    private static ParkingType parkingType;
    private static String vehicleRegNumber = "ABCDEF";
    private static boolean isSavedTicket;
    private static boolean isUpdatedTicket;
    private static boolean isRecurrent;

    @BeforeAll
    private static void setUp(){
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void resetUp() {
        dataBasePrepareService.clearDataBaseEntries();
    }


    private void setTicketUp(){
        parkingSpot = new ParkingSpot(1,parkingType.CAR,false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
    }

    @Test
    public void testSaveTicket() {
        //GIVEN
        setTicketUp();

        //WHEN
        isSavedTicket = ticketDAO.saveTicket(ticket);

        //THEN
        assertEquals(false,isSavedTicket);
        System.out.println("Ticket saved in DB.");
    }

    @Test
    public void testGetTicket() {
        //GIVEN
        setTicketUp();
        ticketDAO.saveTicket(ticket);
        Ticket getTicket = new Ticket();

        //WHEN
        getTicket = ticketDAO.getTicket(vehicleRegNumber);

        //THEN
        assertNotNull(getTicket);
        assertEquals(parkingSpot, getTicket.getParkingSpot());
        assertEquals(0, getTicket.getPrice(),0);
        System.out.println("The ticket has been getting.");

    }

    @Test
    public void testUpdateTicket() {
        //GIVEN
        setTicketUp();
        ticket.setPrice(10);
        ticket.setOutTime(new Date());
        ticketDAO.saveTicket(ticket);

        //WHEN
        isUpdatedTicket = ticketDAO.updateTicket(ticket);

        //THEN
        assertEquals(true,isUpdatedTicket);
        System.out.println("Ticket well updated.");

    }

    @Test
    public void testIsRecurrentUser() {
        //GIVEN
        setTicketUp();
        ticket.setPrice(10);
        ticket.setOutTime(new Date());


        //WHEN
        isRecurrent = ticketDAO.isRecurrentUser(ticket);

        //THEN
        assertEquals(false, isRecurrent);
        System.out.println("Ticket isn't recurrent.");

    }
}
