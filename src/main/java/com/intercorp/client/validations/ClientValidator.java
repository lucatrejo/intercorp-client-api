package com.intercorp.client.validations;

import com.intercorp.client.dto.ClientRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ClientValidator implements ConstraintValidator<ValidClientRequest, ClientRequest> {
    public void initialize(ValidClientRequest constraint) {
    }

    public boolean isValid(ClientRequest newClientRequest, ConstraintValidatorContext context) {
        final ZonedDateTime zonedBirthDate = newClientRequest.getBirthDate().atZone(ZoneOffset.UTC);
        return  newClientRequest.getAge() - (calculateAge(zonedBirthDate.toLocalDate(), LocalDate.now())) == 0;
    }

    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}

