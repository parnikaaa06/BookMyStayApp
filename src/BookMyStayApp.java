package com.bookmystay.app;

import java.io.*;
import java.util.*;

/**
 * UC12: Data Persistence & System Recovery
 */
public class BookMyStayApp {

    // ===== INVENTORY =====
    static class RoomInventory implements Serializable {
        Map<String, Integer> inventory = new HashMap<>();

        public void addRoom(String type, int count) {
            inventory.put(type, count);
        }
    }

    // ===== RESERVATION =====
    static class Reservation implements Serializable {
        String guestName;
        String roomType;
        String reservationId;

        public Reservation(String guestName, String roomType, String id) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.reservationId = id;
        }
    }

    // ===== SYSTEM STATE (IMPORTANT) =====
    static class SystemState implements Serializable {
        RoomInventory inventory;
        List<Reservation> history;

        public SystemState(RoomInventory inventory, List<Reservation> history) {
            this.inventory = inventory;
            this.history = history;
        }
    }

    // ===== PERSISTENCE SERVICE =====
    static class PersistenceService {

        private static final String FILE_NAME = "data.ser";

        // SAVE
        public void save(SystemState state) {
            try (ObjectOutputStream out =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                out.writeObject(state);
                System.out.println("Data saved successfully");

            } catch (Exception e) {
                System.out.println("Error saving data");
            }
        }

        // LOAD
        public SystemState load() {
            try (ObjectInputStream in =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                System.out.println("Data loaded successfully");
                return (SystemState) in.readObject();

            } catch (Exception e) {
                System.out.println("No previous data found. Starting fresh.");
                return null;
            }
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        PersistenceService ps = new PersistenceService();

        // TRY TO LOAD DATA
        SystemState state = ps.load();

        RoomInventory inventory;
        List<Reservation> history;

        if (state != null) {
            inventory = state.inventory;
            history = state.history;
        } else {
            // fresh start
            inventory = new RoomInventory();
            inventory.addRoom("Single Room", 2);

            history = new ArrayList<>();
            history.add(new Reservation("Alice", "Single Room", "SI1"));
        }

        // Display recovered data
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.inventory.keySet()) {
            System.out.println(type + " -> " + inventory.inventory.get(type));
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r.guestName + " | " + r.reservationId);
        }

        // SAVE BEFORE EXIT
        SystemState newState = new SystemState(inventory, history);
        ps.save(newState);
    }
}