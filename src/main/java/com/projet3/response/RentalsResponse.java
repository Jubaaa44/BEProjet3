package com.projet3.response;

import java.util.List;

import com.projet3.dto.RentalDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class RentalsResponse {

    @Schema(description = "Liste des locations récupérées", required = true)
    private List<RentalDTO> rentals;

    // Getter et Setter
    /**
     * Récupère la liste des locations.
     * 
     * @return La liste des locations.
     */
    public List<RentalDTO> getRentals() {
        return rentals;
    }

    /**
     * Définit la liste des locations.
     * 
     * @param rentals Liste des locations à définir.
     */
    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
