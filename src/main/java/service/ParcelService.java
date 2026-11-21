package service;

import exceptions.ParcelNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import model.Parcel;

// Stores parcels in sorted order by ID using a TreeMap
public class ParcelService {
    private Map<String, Parcel> parcels = new TreeMap<>();

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelId(), parcel);
    }

    public Parcel getParcel(String parcelId) throws ParcelNotFoundException {
        Parcel parcel = parcels.get(parcelId);
        if (parcel == null) {
            throw new ParcelNotFoundException("Parcel not found: " + parcelId);
        }
        return parcel;
    }

    public Collection<Parcel> listAllParcels() {
        return parcels.values();
    }

    // Searches parcels where sender or receiver contains the query (case-insensitive)
    public Collection<Parcel> searchParcels(String query) {
        List<Parcel> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        String lowerQuery = query.trim().toLowerCase();
        for (Parcel p : parcels.values()) {
            if (p.getSenderName().toLowerCase().contains(lowerQuery) ||
                p.getReceiverName().toLowerCase().contains(lowerQuery)) {
                results.add(p);
            }
        }
        return results;
    }

    // Used when a parcel is cancelled
    public void removeParcel(String parcelId) {
        parcels.remove(parcelId);
    }
}
