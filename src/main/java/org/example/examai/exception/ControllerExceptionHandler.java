package org.example.examai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Denne klasse fanger exceptions globalt i hele din applikation.
 * Den håndterer fejl på en ensartet måde og returnerer brugerdefinerede fejlmeddelelser.
 */
@ControllerAdvice // Gør denne klasse til en global "fejl-håndterer" for alle controllere
public class ControllerExceptionHandler {


     //Håndterer alle typer exceptions, der ikke er fanget andetsteds.

    @ExceptionHandler(Exception.class) // Hvis en hvilken som helst exception opstår
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        System.out.println("GLOBAL EXCEPTION KALDT"); // Udskriver en log

        // Opretter en fejlbesked med statuskode, dato, fejlbeskrivelse og request-info
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Noget gik galt, prøv igen eller kontakt support.", // Brugervenlig besked
                request.getDescription(false)
        );
        // Returnerer fejlbeskeden som HTTP-svar med 500-status
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
