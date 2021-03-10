package com.parkit.parkingsystem.unitaire;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot;
    private static ParkingType parkingType;
    private static int nextSlot;
    private static int slotStatus;
    private static boolean isSlotUpdated;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void resetUp() {
        nextSlot = 0;
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testGetNextAvailableCarSlot() {

        //GIVEN

        //WHEN
        nextSlot = parkingSpotDAO.getNextAvailableSlot(parkingType.CAR);

        //THEN
        assertEquals(1,nextSlot);
        System.out.println("Next Car Slot is 1, works.");
    }

    @Test
    public void testGetNextAvailableBikeSlot() {

        //GIVEN

        //WHEN
        nextSlot = parkingSpotDAO.getNextAvailableSlot(parkingType.BIKE);

        //THEN
        assertEquals(4,nextSlot);
        System.out.println("Next Bike Slot is 4, works.");
    }

    @Test
    public void testUpdatingParkingToNotAvailable() {

        //GIVEN
        parkingSpot = new ParkingSpot(1,parkingType.CAR,false);

        //WHEN
        isSlotUpdated = parkingSpotDAO.updateParking(parkingSpot);
        slotStatus = parkingSpotDAO.getNextAvailableSlot(parkingType.CAR);

        //THEN
        assertEquals(true,isSlotUpdated);
        System.out.println("The slot is well updated to not available.");
        assertEquals(2,slotStatus);
        System.out.println("The slot 1 is well available.");
    }
    @Test
    public void testUpdatingParkingToAvailable() {

        //GIVEN
        parkingSpot = new ParkingSpot(1,parkingType.CAR,false);
        parkingSpotDAO.updateParking(parkingSpot);

        //WHEN
        parkingSpot.setAvailable(true);
        isSlotUpdated = parkingSpotDAO.updateParking(parkingSpot);
        slotStatus = parkingSpotDAO.getNextAvailableSlot(parkingType.CAR);

        //THEN
        assertEquals(true,isSlotUpdated);
        System.out.println("The slot is well updated to not available.");
        assertEquals(1,slotStatus);
        System.out.println("The slot 1 is well available.");
    }
}
