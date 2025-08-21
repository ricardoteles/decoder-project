package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionRecordDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    final private CourseUserService courseUserService;
    final private AuthUserClient userClient;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserRecordDto>> getAllUsersByCourse(
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto) {
        CourseModel courseModel = courseService.findById(courseId);

        if(courseUserService.existsByCourseAndUserId(courseModel, subscriptionRecordDto.userId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        // TODO: user verification

        CourseUserModel courseUserModel = courseModel.convertToCourseUserModel(subscriptionRecordDto.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseUserService.saveAndSendSubscriptionUserInCourse(courseUserModel));
    }
}
