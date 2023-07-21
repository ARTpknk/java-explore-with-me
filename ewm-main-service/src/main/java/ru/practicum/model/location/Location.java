package ru.practicum.model.location;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
public class Location {
    private Float lat;
    private Float lon;

    public Location() {
    }

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}