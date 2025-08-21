package com.ead.course.validations;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.enums.UserType;
import com.ead.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Component
public class CourseValidator implements Validator {
    private final Validator validator;
    private final CourseService courseService;
    private final AuthUserClient authUserClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseRecordDto courseRecordDto = (CourseRecordDto) o;
        validator.validate(courseRecordDto, errors);        // It works the same way as @Valid in the controller.

        if(!errors.hasErrors()) {
            validateCourseName(courseRecordDto, errors);
            validateUserInstructor(courseRecordDto.userInstructor(), errors);
        }
    }

    private void validateCourseName(CourseRecordDto courseRecordDto, Errors errors) {
        if(courseService.existsByName(courseRecordDto.name())) {
            errors.rejectValue("name", "courseNameConflict", "Course name is already taken.");
            log.error("Error validation courseName: {}", courseRecordDto.name());
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        ResponseEntity<UserRecordDto> responseUserInstructor =  authUserClient.getOneUserById(userInstructor);

        if(responseUserInstructor.getBody().userType().equals(UserType.STUDENT) ||
                responseUserInstructor.getBody().userType().equals(UserType.USER)) {
            errors.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR or ADMIN.");
            log.error("Error validation userInstructor: {}", userInstructor);
        }
    }
}
