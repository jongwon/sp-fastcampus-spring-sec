package com.sp.fc.user.service.helper;


import com.sp.fc.user.domain.School;
import com.sp.fc.user.service.SchoolService;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class SchoolTestHelper {

    private final SchoolService service;

    public static School makeSchool(String name, String city){
        return School.builder()
                .name(name)
                .city(city)
                .build();
    }

    public School createSchool(String name, String city){
        return service.save(makeSchool(name, city));
    }

    public static void assertSchool(School school, String name, String city){
        assertNotNull(school.getSchoolId());
        assertNotNull(school.getCreated());
        assertNotNull(school.getUpdated());

        assertEquals(name, school.getName());
        assertEquals(city, school.getCity());
    }


}
