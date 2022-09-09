package ru.skqwk.kicksharingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skqwk.kicksharingservice.exception.BadInputParameters;
import ru.skqwk.kicksharingservice.exception.ConflictDataException;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.message.ErrorResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class BaseController {

  @ExceptionHandler(BadInputParameters.class)
  void handle(HttpServletResponse response, BadInputParameters exception) throws IOException {
    sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, exception);
  }

  @ExceptionHandler(ConflictDataException.class)
  void handleConflictData(HttpServletResponse response, ConflictDataException exception) throws IOException {
    sendResponse(response, HttpServletResponse.SC_CONFLICT, exception);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  void handleNotFound(HttpServletResponse response, Exception exception) throws IOException {
    sendResponse(response, HttpServletResponse.SC_NOT_FOUND, exception);
  }

  @ExceptionHandler(IllegalStateException.class)
  void handleIllegalState(HttpServletResponse response, IllegalStateException exception) throws IOException {
    sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, exception);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  void handleIllegalArgument(HttpServletResponse response, IllegalArgumentException exception) throws IOException {
    sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, exception);
  }

  void sendResponse(HttpServletResponse response, int status, Exception exception)
      throws IOException {
    log.error("Send error response: status: {}, message: {}", status, exception.getMessage());
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    ErrorResponse error =
        ErrorResponse.builder().code(status).message(exception.getMessage()).build();
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    mapper.writeValue(out, error);
    out.flush();
  }
}
