package com.progmasters.moovsmart.domain;

import com.progmasters.moovsmart.dto.PropertyForm;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime localDateTime;
    private Integer numberOfRooms;
    private Integer price;
    private Integer buildingYear;
    private Double area;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private PropertyState propertyState;

    @Enumerated(EnumType.STRING)
    private County county;

    private String city;
    private Integer zipCode;
    private String street;
    private String streetNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean isValid;

    @Enumerated(EnumType.STRING)
    private StatusOfProperty status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserProperty owner;

    private Double lngCoord;
    private Double latCoord;

    @Column(name = "imagesUrl")
    @ElementCollection(targetClass = String.class)
    private List<String>imageUrls = new ArrayList<>();

    @Column(name = "publicId")
    @ElementCollection(targetClass = String.class)
    private List<String> publicIds = new ArrayList<>();

    public Property() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Integer getBuildingYear() {
        return buildingYear;
    }

    public void setBuildingYear(Integer buildingYear) {
        this.buildingYear = buildingYear;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public PropertyState getPropertyState() {
        return propertyState;
    }

    public void setPropertyState(PropertyState propertyState) {
        this.propertyState = propertyState;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public List<String> getPublicIds() {
        return publicIds;
    }

    public void setPublicIds(List<String> publicIds) {
        this.publicIds = publicIds;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public UserProperty getOwner() {
        return owner;
    }

    public void setOwner(UserProperty owner) {
        this.owner = owner;
    }

    public Double getLngCoord() {
        return lngCoord;
    }

    public void setLngCoord(Double lngCoord) {
        this.lngCoord = lngCoord;
    }

    public Double getLatCoord() {
        return latCoord;
    }

    public void setLatCoord(Double latCoord) {
        this.latCoord = latCoord;
    }

    public StatusOfProperty getStatus() {
        return status;
    }

    public void setStatus(StatusOfProperty status) {
        this.status = status;
    }
}
