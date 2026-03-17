package com.bookmystay.app;

import java.util.*;

/**
 * UC11: Concurrent Booking Simulation
 */
public class BookMyStayApp {

    // ===== INVENTORY (SHARED RESOURCE) =====
    static class RoomInventory {
        private Map<String, Integer> inventory = new HashMap<>();

        public RoomInventory() {
            inventory.put("Single Room", 2);
        }

        // synchronized → critical section
        public synchronized boolean bookRoom(String type) {

            int available = inventory.getOrDefault(type, 0);

            if (available > 0) {
                System.out.println(Thread.currentThread().getName() +
                        " booking... Available before: " + available);

                inventory.put(type, available - 1);

                System.out.println(Thread.currentThread().getName() +
                        " booked successfully. Remaining: " + (available - 1));

                return true;
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed (No rooms left)");
                return false;
            }
        }
    }

    // ===== TASK (THREAD) =====
    static class BookingTask implements Runnable {

        private RoomInventory inventory;
        private String guest;

        public BookingTask(RoomInventory inventory, String guest) {
            this.inventory = inventory;
            this.guest = guest;
        }

        @Override
        public void run() {
            inventory.bookRoom("Single Room");
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // simulate multiple users (threads)
        Thread t1 = new Thread(new BookingTask(inventory, "Alice"));
        Thread t2 = new Thread(new BookingTask(inventory, "Bob"));
        Thread t3 = new Thread(new BookingTask(inventory, "Charlie"));

        t1.start();
        t2.start();
        t3.start();
    }
}