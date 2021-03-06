package com.progmasters.moovsmart.integration;

import com.progmasters.moovsmart.domain.*;
import com.progmasters.moovsmart.dto.*;
import com.progmasters.moovsmart.repository.PropertyRepository;
import com.progmasters.moovsmart.repository.UserRepository;
import com.progmasters.moovsmart.service.PropertyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PropertyServiceIT {

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    UserRepository userRepository;

    private PropertyService propertyService;
    private  Long propertyId;
    private UserProperty user;

    @BeforeEach
    public void init() {
        this.propertyService = new PropertyService(propertyRepository, userRepository);

        user = new UserProperty();
        user.setMail("xy@xy.com");
        user.setId(1L);
        userRepository.save(user);

        PropertyForm property = new PropertyForm();
        property.setName("Ház");
        property.setArea(150.0);
        property.setNumberOfRooms(5);
        property.setBuildingYear(1999);
        property.setCounty("BUDAPEST");
        property.setPropertyType("HOUSE");
        property.setPropertyState("RENEWABLE");
        property.setCity("Budapest");
        property.setStreet("Csipke út");
        property.setStreetNumber("3");
        property.setZipCode(1125);
        property.setPrice(10000000);
        property.setDescription("");
        property.setLatCoord(47.507855);
        property.setLngCoord(18.987466);

        propertyService.createProperty(property, user.getMail());

        List<Property> props = propertyRepository.findAllByIsHolding();

        propertyId = props.get(0).getId();

        propertyRepository.findById(propertyId).get().setStatus(StatusOfProperty.ACCEPTED);

    }

    @AfterEach
    public void clean(){
        userRepository.delete(user);
    }

    @Test
    public void testGetPropertyDetails(){
        PropertyDetails propertyDetails = propertyService.getPropertyDetails(propertyId);

        assertEquals("Ház", propertyDetails.getName());
        assertEquals("Budapest", propertyDetails.getCounty());
        assertEquals("Felújítandó", propertyDetails.getPropertyState());
    }

    @Test
    public void testCreateAndGetProperties(){
        List<PropertyListItem> properties = propertyService.getProperties();

        assertEquals(1, properties.size());
        assertEquals("Ház", properties.get(0).getName());
    }

    @Test
    public void testUpdateProperty() {
        PropertyForm propUpdate = new PropertyForm();
        propUpdate.setName("Nagy ház");
        propUpdate.setArea(150.0);
        propUpdate.setBuildingYear(1999);
        propUpdate.setCounty("BUDAPEST");
        propUpdate.setPropertyType("HOUSE");
        propUpdate.setPropertyState("RENEWED");

        Property updatedProperty = propertyService.updateProperty(propUpdate, propertyId, user.getMail());

        assertEquals(propUpdate.getName(), updatedProperty.getName());
        assertEquals("Budapest", updatedProperty.getCounty().getDisplayName());
        assertEquals("Ház", updatedProperty.getPropertyType().getDisplayName());
        assertEquals("Felújított", updatedProperty.getPropertyState().getDisplayName());

    }

    @Test
    public void testDeleteProperty() {
        boolean isDeleted = propertyService.deleteProperty(propertyId, user.getMail());

        assertTrue(isDeleted);
        assertFalse(propertyRepository.findById(propertyId).get().isValid());
    }

    @Test
    public void testGetFilteredList_WithoutRoom() {
        PropertyForm property1 = new PropertyForm();
        property1.setName("House1");
        property1.setNumberOfRooms(2);
        property1.setArea(50.0);
        property1.setPrice(10000000);
        property1.setPropertyType("HOUSE");
        property1.setPropertyState("NEW");
        property1.setCity("Budapest");
        property1.setCounty("BUDAPEST");

        propertyService.createProperty(property1, user.getMail());

        PropertyForm property2 = new PropertyForm();
        property2.setName("House2");
        property2.setNumberOfRooms(3);
        property2.setArea(80.0);
        property2.setPrice(30000000);
        property2.setPropertyType("HOUSE");
        property2.setPropertyState("NEW");
        property2.setCounty("FEJER");
        property2.setCity("Székesfehérvár");

        propertyService.createProperty(property2, user.getMail());

        List<Property> props = propertyRepository.findAllByIsHolding();
        for (Property p : props) {
            p.setStatus(StatusOfProperty.ACCEPTED);
        }

        CreateFilteredCommand createFilteredCommand = new CreateFilteredCommand();
        createFilteredCommand.setMinPrice(5000000);
        createFilteredCommand.setMaxPrice(35000000);
        createFilteredCommand.setMinSize(40.0);
        createFilteredCommand.setMaxSize(100.0);

        List<PropertyListItem> propertyListItems = propertyService.makeFilterList(createFilteredCommand);
        assertEquals(2, propertyListItems.size());

        propertyListItems = propertyService.getFilteredPropertiesWithoutRooms(createFilteredCommand);
        assertEquals(2, propertyListItems.size());

        createFilteredCommand.setCity("Székesfehérvár");
        propertyListItems = propertyService.makeFilterList(createFilteredCommand);
        assertEquals(1, propertyListItems.size());

        propertyListItems = propertyService.getFilteredPropertiesWithoutRooms(createFilteredCommand);
        assertEquals(1, propertyListItems.size());
    }

    @Test
    public void testGetFilteredList() throws Exception {
        PropertyForm property1 = new PropertyForm();
        property1.setName("House1");
        property1.setNumberOfRooms(2);
        property1.setArea(50.0);
        property1.setPrice(10000000);
        property1.setPropertyType("HOUSE");
        property1.setPropertyState("NEW");
        property1.setCity("Budapest");
        property1.setCounty("BUDAPEST");

        propertyService.createProperty(property1, user.getMail());

        PropertyForm property2 = new PropertyForm();
        property2.setName("House2");
        property2.setNumberOfRooms(3);
        property2.setArea(80.0);
        property2.setPrice(30000000);
        property2.setPropertyType("HOUSE");
        property2.setPropertyState("NEW");
        property2.setCounty("FEJER");
        property2.setCity("Székesfehérvár");

        propertyService.createProperty(property2, user.getMail());

        List<Property> props = propertyRepository.findAllByIsHolding();
        for (Property p : props) {
            p.setStatus(StatusOfProperty.ACCEPTED);
        }

        CreateFilteredCommand createFilteredCommand = new CreateFilteredCommand();
        createFilteredCommand.setMinPrice(5000000);
        createFilteredCommand.setMaxPrice(35000000);
        createFilteredCommand.setMinSize(40.0);
        createFilteredCommand.setMaxSize(100.0);
        createFilteredCommand.setNumberOfRooms(3);

        List<PropertyListItem> propertyListItems = propertyService.makeFilterList(createFilteredCommand);
        assertEquals(1, propertyListItems.size());

        propertyListItems = propertyService.getFilteredProperties(createFilteredCommand);
        assertEquals(1, propertyListItems.size());

        createFilteredCommand.setCity("Budapest");
        propertyListItems = propertyService.makeFilterList(createFilteredCommand);
        assertEquals(0, propertyListItems.size());

        propertyListItems = propertyService.getFilteredProperties(createFilteredCommand);
        assertEquals(0, propertyListItems.size());

    }

    @Test
    public void testGetOwnProperties() {
        PropertyForm property2 = new PropertyForm();
        property2.setName("Ház");
        property2.setCounty("BUDAPEST");
        property2.setPropertyType("HOUSE");
        property2.setPropertyState("RENEWABLE");

        PropertyForm property3 = new PropertyForm();
        property3.setName("Ház2");
        property3.setCounty("BARANYA");
        property3.setPropertyType("APARTMENT");
        property3.setPropertyState("RENEWED");

        propertyService.createProperty(property2, user.getMail());
        propertyService.createProperty(property3, user.getMail());

        List<Property> props = propertyRepository.findAllByIsHolding();
        for (Property p : props) {
            p.setStatus(StatusOfProperty.ACCEPTED);
        }

        List<PropertyListItem> properties = propertyService.getOwnProperties(user.getMail());

        assertEquals(3, properties.size());
    }

    @Test
    public void testGetAllHoldingProperty() {
        PropertyForm property1 = new PropertyForm();
        property1.setName("Ház");
        property1.setCounty("BUDAPEST");
        property1.setPropertyType("HOUSE");
        property1.setPropertyState("RENEWABLE");

        propertyService.createProperty(property1, user.getMail());

        List<PropertyForm> properties = propertyService.getAllHoldingProperty();

        assertEquals(1, properties.size());

        List<Property> props = propertyRepository.findAllByIsHolding();
        for (Property p : props) {
            p.setStatus(StatusOfProperty.ACCEPTED);
        }

        properties = propertyService.getAllHoldingProperty();

        assertEquals(0, properties.size());
    }

    @Test
    public void testGetArchivedProperties() {
        CreateQueryByDatesCommand command = new CreateQueryByDatesCommand(
                LocalDateTime.of(2020, Month.JANUARY, 01, 19, 30, 40),
                LocalDateTime.of(2020, Month.JANUARY, 15, 19, 30, 40));

        List<PropertyForm> archivedProperties = propertyService.getArchivedProperties(command);

        assertEquals(0, archivedProperties.size());

        Property property = propertyRepository.findById(propertyId).get();
        property.setLocalDateTime(LocalDateTime.of(2020, Month.JANUARY, 5, 19, 30, 40));
        property.setStatus(StatusOfProperty.ARCHIVED);
        property.setValid(false);
        propertyRepository.save(property);

        archivedProperties = propertyService.getArchivedProperties(command);

        assertEquals(1, archivedProperties.size());
    }

    @Test
    public void testActivateProperty() {
        PropertyForm property2 = new PropertyForm();
        property2.setName("Ház");
        property2.setCounty("BUDAPEST");
        property2.setPropertyType("HOUSE");
        property2.setPropertyState("RENEWABLE");

        PropertyForm property3 = new PropertyForm();
        property3.setName("Ház2");
        property3.setCounty("BARANYA");
        property3.setPropertyType("APARTMENT");
        property3.setPropertyState("RENEWED");

        propertyService.createProperty(property2, user.getMail());
        propertyService.createProperty(property3, user.getMail());

        List<Property> props = propertyRepository.findAllByIsHolding();
        for (Property p : props) {
            propertyService.activateProperty(p.getId());
        }

        List<PropertyListItem> properties = propertyService.getProperties();

        assertEquals(3, properties.size());
    }
}
