package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CourseUserController {
    final private CourseService courseService;
    final private UserService userService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec,
                                                      @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "courseId") UUID courseId) {
        courseService.findById(courseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto) {
        CourseModel courseModel = courseService.findById(courseId);

        // verifications with state transfer

        return ResponseEntity.status(HttpStatus.CREATED).body(" "); //refactor
    }
}
