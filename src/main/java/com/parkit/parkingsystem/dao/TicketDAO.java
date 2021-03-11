package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
    private List<String> checkList = new ArrayList<>();

    /**
     * Used to save a new ticket for an incoming user in the database.
     *
     * @param ticket
     * @return
     */
    public boolean saveTicket(Ticket ticket) {
        try (Connection con = dataBaseConfig.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET)) {
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                ps.setInt(1, ticket.getParkingSpot().getId());
                ps.setString(2, ticket.getVehicleRegNumber());
                ps.setDouble(3, ticket.getPrice());
                ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
                ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
                return ps.execute();
            }

        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        }
        return false;
    }

    /**
     * Used to get the ticket in the database in terms of the vehicle registration number.
     *
     * @param vehicleRegNumber
     * @return
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), rs.getBoolean(7));
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    /**
     * Used to update the ticket in the database when the user exiting the parking.
     *
     * @param ticket
     * @return
     */
    public boolean updateTicket(Ticket ticket) {
        try (Connection con = dataBaseConfig.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET)) {
                ps.setDouble(1, ticket.getPrice());
                ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
                ps.setInt(3, ticket.getId());
                ps.execute();
                return true;
            }
        } catch (Exception ex) {
            logger.error("Error saving ticket info", ex);
        }
        return false;
    }

    /**
     * Used to authorize or not 5% reduction in terms of the user's recurrence.
     *
     * @param ticket
     * @return
     */
    public boolean isRecurrentUser(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET_UNIQUE_REG_NUMBER);
            ps.setString(1, ticket.getVehicleRegNumber());
            ResultSet rs = ps.executeQuery();
            boolean isRecurrent = false;
            if (rs.next()) {
                isRecurrent = rs.getInt("nbr") > 2;
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            return isRecurrent;
        } catch (Exception ex) {
            logger.error("Error scanning if user is recurrent", ex);
        }
        return false;
    }
}
