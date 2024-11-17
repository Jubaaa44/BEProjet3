package com.projet3.response;

import java.util.List;

import com.projet3.dto.RentalDTO;

public class RentalsResponse {
    private List<RentalDTO> rentals;

    // Getter et Setter
    public List<RentalDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
