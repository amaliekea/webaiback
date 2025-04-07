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
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500 statuskode
                new Date(), // Tidspunkt for fejlen
                ex.getMessage() + " xxxxxxxxxxxxxx", // Fejlbeskeden fra exception
                request.getDescription(false)); // Info om hvilken request der udløste fejlen

        // Returnerer fejlbeskeden som HTTP-svar med 500-status
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArithmeticException.class) // Fanger kun matematiske fejl som division med 0
    public ResponseEntity<ErrorMessage> globalArithmeticExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500 statuskode
                new Date(), // Tidspunkt for fejlen
                ex.getMessage() + " Du dividerede lige med nul", // Fejlbesked + ekstra tekst
                request.getDescription(false)); // Info om requesten

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR); // Returnér som svar
    }
}
