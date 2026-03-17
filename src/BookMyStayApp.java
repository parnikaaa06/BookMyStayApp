import java.util.Scanner;

public class BookMyStayApp {

    protected String roomNumber;
    protected int numberOfBeds;
    protected double roomSize;
    protected double pricePerNight;
    protected String roomType;

    // Constructor
    public BookMyStayApp(String roomNumber, int numberOfBeds, double roomSize, double pricePerNight, String roomType) {
        this.roomNumber = roomNumber;
        this.numberOfBeds = numberOfBeds;
        this.roomSize = roomSize;
        this.pricePerNight = pricePerNight;
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getRoomSize() {
        return roomSize;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("\nRoom Details");
        System.out.println("Room Number   : " + roomNumber);
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size     : " + roomSize + " sq ft");
        System.out.println("Price/Night   : $" + pricePerNight);
        System.out.println("-----------------------------");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        System.out.println(" Welcome to Hotel Booking System");
        System.out.println("System initialized successfully.");

        System.out.print("Enter Room Number: ");
        String roomNumber = sc.nextLine();

        System.out.print("Enter Room Type (Single/Double/Suite): ");
        String roomType = sc.nextLine();

        System.out.print("Enter Number of Beds: ");
        int beds = sc.nextInt();

        System.out.print("Enter Room Size (sq ft): ");
        double size = sc.nextDouble();

        System.out.print("Enter Price per Night: ");
        double price = sc.nextDouble();

        BookMyStayApp room = new BookMyStayApp(roomNumber, beds, size, price, roomType);

        System.out.println("\nApplication started successfully.");
        System.out.println("System is ready to manage hotel bookings.");

        room.displayRoomDetails();

        sc.close();
    }
}