package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
public class LessonController {
    private final ModuleService moduleService;
    private final LessonService lessonService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonRecordDto lessonRecordDto) {
        log.debug("POST saveLesson lessonRecordDto received {}", lessonRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.save(lessonRecordDto, moduleService.findById(moduleId)));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.findAllLessonsIntoModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findLessonIntoModule(moduleId, lessonId));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){
        log.debug("DELETE deleteLesson lessonId received {}", lessonId);
        var lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        lessonService.delete(lessonModel);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonRecordDto lessonRecordDto) {
        log.debug("PUT updateLesson lessonRecordDto received {}", lessonRecordDto);
        var lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.update(lessonRecordDto, lessonModel));
    }
}
