package io.github.arch2be.realtime.measure.controller;

import io.github.arch2be.realtime.measure.dto.ErrorInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/measure")
class ReadingController {

    private final ReadingPresenter presenter;

    ReadingController(ReadingPresenter presenter) {
        this.presenter = presenter;
    }

    @GetMapping
    private ResponseEntity<?> getLatestValueByDate() {
        return presenter.getLatestValueByDate()
                .fold(ResponseEntity::ok, ResponseEntity::ok);
    }

    @PostMapping(path = "/average")
    private ResponseEntity<?> getAverageValueForCondition(@Valid @RequestBody ReadingRequest readingRequest) {
        return presenter.getAverageValueForCondition(readingRequest)
                .fold(left -> new ResponseEntity<>(left, HttpStatus.BAD_REQUEST), ResponseEntity::ok);
    }

    @PostMapping(path = "/values")
    private ResponseEntity<?> getGoodQualityValuesBetweenDates(@Valid @RequestBody ReadingRequest readingRequest) {
        return presenter.getGoodQualityValuesBetweenDates(readingRequest)
                .fold(left -> new ResponseEntity<>(left, HttpStatus.BAD_REQUEST), ResponseEntity::ok);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private List<ErrorInfoDto> handleWrongRequestBodyValueForAverage(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<ObjectError> allErrors = methodArgumentNotValidException.getAllErrors();

        return allErrors.stream()
                .map(error -> new ErrorInfoDto(error.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
