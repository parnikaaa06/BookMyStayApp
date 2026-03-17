import java.util.LinkedList;
import java.util.Queue;

class Reservation {
    private String name;
    private String roomType;

    public Reservation(String name, String roomType) {
        this.name = name;
        this.roomType = roomType;
    }

    public String getName() {
        return name;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return "Name: " + name + ", Room Type: " + roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Booking Request Queue");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        while (bookingQueue.hasPendingRequests()) {
            Reservation next = bookingQueue.getNextRequest();
            System.out.println("Processing booking -> " + next);
        }
    }
}