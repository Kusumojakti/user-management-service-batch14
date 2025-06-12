package com.batch14.user_management_service.exception

import com.batch14.user_management_service.domain.dto.response.BaseResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerArgumentNotValidException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<BaseResponses<Any?>> {
        val errors = mutableListOf<String?>()
        exception.bindingResult.fieldErrors.forEach {
            errors.add(it.defaultMessage)
        }
        return ResponseEntity(
            BaseResponses(
                data = errors
            ),
            HttpStatus.BAD_REQUEST
        )
    }
    @ExceptionHandler(CustomException::class)
    fun handlerCustomException(
        exception: CustomException
    ) : ResponseEntity<BaseResponses<Any?>> {
        return ResponseEntity(
            BaseResponses(
                status = "T",
                message = exception.exceptionMessage,
                error = exception.data
            ),
            HttpStatus.valueOf(exception.statusCode)
        )
    }
}