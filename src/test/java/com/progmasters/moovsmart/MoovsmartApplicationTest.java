package com.progmasters.moovsmart;

import com.progmasters.moovsmart.dto.PropertyListItem;
import com.progmasters.moovsmart.service.PropertyService;
import com.progmasters.moovsmart.controller.PropertyController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoovsmartApplicationTest {

    @Mock
    private PropertyService propertyService;

    @Test
    public void testAddProperty() {
        List<PropertyListItem> propertyList = propertyService.getProperties();
    @InjectMocks
    private PropertyController propertyController;


    }
}
