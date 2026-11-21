package app;

import exceptions.AgentUnavailableException;
import exceptions.IllegalStatusTransitionException;
import exceptions.ParcelNotFoundException;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import model.Agent;
import model.Parcel;
import model.ParcelStatus;
import model.StatusLog;
import service.AgentService;
import service.BookingService;
import service.ParcelService;
import service.TrackingService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParcelService parcelService = new ParcelService();
        AgentService agentService = new AgentService();
        BookingService bookingService = new BookingService(parcelService, agentService);
        TrackingService trackingService = new TrackingService(parcelService, agentService);

        while (true) {
            System.out.println("\n===== COURIER AND PARCEL TRACKING SYSTEM =====");
            System.out.println("1. Book a parcel");
            System.out.println("2. Register a delivery agent");
            System.out.println("3. Update parcel status");
            System.out.println("4. Track a parcel");
            System.out.println("5. View all parcels");
            System.out.println("6. Cancel a parcel");
            System.out.println("7. View all agents");
            System.out.println("8. Search parcels by sender or receiver");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                // Book a new parcel and assign an agent
                case 1:
                    System.out.print("Enter sender name: ");
                    String sender = scanner.nextLine();
                    System.out.print("Enter receiver name: ");
                    String receiver = scanner.nextLine();
                    System.out.print("Enter weight: ");
                    double weight;
                    try {
                        weight = scanner.nextDouble();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input, please enter a number");
                        scanner.nextLine();
                        break;
                    }
                    try {
                        Parcel parcel = bookingService.bookParcel(sender, receiver, weight);
                        System.out.println("Parcel booked successfully! ID: " + parcel.getParcelId()
                                + ", Assigned Agent: " + parcel.getAssignedAgentId());
                    } catch (AgentUnavailableException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                // Register a new delivery agent
                case 2:
                    System.out.print("Enter agent name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    Agent agent = agentService.registerAgent(name, phone);
                    System.out.println("Agent registered successfully! ID: " + agent.getAgentId());
                    break;

                // Update the status of a parcel
                case 3:
                    System.out.print("Enter parcel ID: ");
                    String parcelId = scanner.nextLine();
                    System.out.println("Select status: 1=PICKED_UP, 2=IN_TRANSIT, 3=OUT_FOR_DELIVERY, 4=DELIVERED");
                    System.out.print("Enter choice: ");
                    int statusChoice;
                    try {
                        statusChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input, please enter a number");
                        scanner.nextLine();
                        break;
                    }
                    ParcelStatus newStatus = null;
                    switch (statusChoice) {
                        case 1:
                            newStatus = ParcelStatus.PICKED_UP;
                            break;
                        case 2:
                            newStatus = ParcelStatus.IN_TRANSIT;
                            break;
                        case 3:
                            newStatus = ParcelStatus.OUT_FOR_DELIVERY;
                            break;
                        case 4:
                            newStatus = ParcelStatus.DELIVERED;
                            break;
                        default:
                            System.out.println("Invalid status choice");
                            break;
                    }
                    if (newStatus != null) {
                        try {
                            trackingService.updateStatus(parcelId, newStatus);
                            System.out.println("Parcel status updated successfully to " + newStatus);
                        } catch (ParcelNotFoundException | IllegalStatusTransitionException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;

                // View history of a parcel
                case 4:
                    System.out.print("Enter parcel ID: ");
                    String trackId = scanner.nextLine();
                    try {
                        List<StatusLog> history = trackingService.viewHistory(trackId);
                        for (StatusLog log : history) {
                            System.out.println(log);
                        }
                    } catch (ParcelNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                // View all parcels
                case 5:
                    Collection<Parcel> parcels = parcelService.listAllParcels();
                    for (Parcel p : parcels) {
                        System.out.println("ID: " + p.getParcelId()
                                + ", Sender: " + p.getSenderName()
                                + ", Receiver: " + p.getReceiverName()
                                + ", Weight: " + p.getWeight()
                                + ", Status: " + p.getStatus()
                                + ", Assigned Agent: " + p.getAssignedAgentId());
                    }
                    break;

                // Cancel a booked parcel
                case 6:
                    System.out.print("Enter parcel ID: ");
                    String cancelId = scanner.nextLine();
                    try {
                        bookingService.cancelParcel(cancelId);
                    } catch (ParcelNotFoundException | IllegalStatusTransitionException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                // View all agents
                case 7:
                    Collection<Agent> agents = agentService.listAgents();
                    for (Agent a : agents) {
                        System.out.println("ID: " + a.getAgentId()
                                + ", Name: " + a.getName()
                                + ", Phone: " + a.getPhone()
                                + ", Available: " + a.isAvailable());
                    }
                    break;

                // Search parcels by sender or receiver
                case 8:
                    System.out.print("Enter sender or receiver name to search: ");
                    String query = scanner.nextLine().trim();
                    Collection<Parcel> matchingParcels = parcelService.searchParcels(query);
                    if (matchingParcels.isEmpty()) {
                        System.out.println("No parcels found matching: " + query);
                    } else {
                        System.out.println("\nMatching Parcels (" + matchingParcels.size() + "):");
                        for (Parcel p : matchingParcels) {
                            System.out.println("ID: " + p.getParcelId()
                                    + ", Sender: " + p.getSenderName()
                                    + ", Receiver: " + p.getReceiverName()
                                    + ", Weight: " + p.getWeight()
                                    + ", Status: " + p.getStatus()
                                    + ", Assigned Agent: " + p.getAssignedAgentId());
                        }
                    }
                    break;

                // Exit the application
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice, try again");
                    break;
            }
        }
    }
}
