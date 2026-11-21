# Courier and Parcel Tracking System

A console-based Java application for managing parcel booking, dynamic delivery agent assignment, and status tracking, built from scratch with standard Java and no external libraries, databases, or GUI.

## Features

- **Parcel Booking**: Book parcels with sender, receiver, and weight details.
- **Automatic Agent Assignment**: Dynamically assigns delivery agents in order using a custom circular FIFO queue.
- **Strict State Lifecycle**: Enforces sequential lifecycle transitions (`BOOKED -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED`).
- **Cancellation**: Only allowed in the `BOOKED` stage, releasing the assigned agent and removing the parcel from active records.
- **Tracking History**: Maintains an audit log of status changes with formatted timestamps.
- **Sorted Parcel Records**: Stores parcels using a `TreeMap`, keeping them sorted by parcel ID.
- **Parcel Search**: Search and filter parcels by sender or receiver name.

## Status Lifecycle

```
BOOKED ──> PICKED_UP ──> IN_TRANSIT ──> OUT_FOR_DELIVERY ──> DELIVERED
```

## Project Structure

```
src/
└── main/
    └── java/
        ├── app/
        │   └── Main.java
        ├── datastructures/
        │   └── CircularAgentQueue.java
        ├── exceptions/
        │   ├── AgentUnavailableException.java
        │   ├── IllegalStatusTransitionException.java
        │   └── ParcelNotFoundException.java
        ├── model/
        │   ├── Agent.java
        │   ├── Parcel.java
        │   ├── ParcelStatus.java
        │   ├── Person.java
        │   └── StatusLog.java
        ├── service/
        │   ├── AgentService.java
        │   ├── BookingService.java
        │   ├── ParcelService.java
        │   └── TrackingService.java
        └── util/
            └── IdGenerator.java
```

## How to Compile and Run

Compile all Java sources:
```bash
javac -d bin $(find src/main/java -name "*.java")
```

Run the application:
```bash
java -cp bin app.Main
```

## Application Menu

```
===== COURIER AND PARCEL TRACKING SYSTEM =====
1. Book a parcel
2. Register a delivery agent
3. Update parcel status
4. Track a parcel
5. View all parcels
6. Cancel a parcel
7. View all agents
8. Search parcels by sender or receiver
0. Exit
```
