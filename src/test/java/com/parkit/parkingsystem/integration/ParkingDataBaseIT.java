package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static String vehicleRegNumber = "ABCDEF";
    private static ParkingType parkingType;
    private ParkingSpot specimenParkingSpot;
    private Ticket specimenTicket;
    private ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testProcessIncomingVehicleShouldSaveTicketAndUpdateAvailablity() {
        //GIVEN
        initData();

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticketIsSaved = ticketDAO.getTicket(vehicleRegNumber);
        int nextParkingSlot = parkingSpotDAO.getNextAvailableSlot(parkingType.CAR);

        //THEN
        assertEquals(specimenTicket, ticketIsSaved);
        System.out.println("The ticket is effectively saved in DB.");
        assertEquals(2, nextParkingSlot);
        System.out.println("And the parking table is availability updated.");

    }

    @Test
    @DisplayName("Test vehicule exit to check ticket")
    public void testParkingLotExit() {
        //GIVEN
        initData();
        parkingService.processIncomingVehicle();

        //WHEN
        parkingService.processExitingVehicle();
        Ticket selectedTicket = ticketDAO.getTicket(vehicleRegNumber);
        Date dbTicketOutTime = selectedTicket.getOutTime();

        //THEN
        assertNotNull(dbTicketOutTime);
        System.out.println("The Out Time isn't null, so the fare is generated and the out time is correctly populated when the method is called..");

    }

    private void initData() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        specimenParkingSpot = new ParkingSpot(1, parkingType.CAR, false);
        specimenTicket = new Ticket();
        specimenTicket.setId(1);
        specimenTicket.setParkingSpot(specimenParkingSpot);
        specimenTicket.setVehicleRegNumber(vehicleRegNumber);
        specimenTicket.setPrice(0);
        specimenTicket.setInTime(new Date());
        specimenTicket.setOutTime(null);
    }
}
