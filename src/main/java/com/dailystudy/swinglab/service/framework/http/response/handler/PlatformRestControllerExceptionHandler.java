package com.dailystudy.swinglab.service.framework.http.response.handler;

import com.dailystudy.swinglab.service.framework.http.request.HttpRequestThreadLocal;
import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabHttpException;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabUnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RestExceptionHandler is only applied for RestController exceptions.
 *
 * @author yjkim
 */
@Slf4j
@RestControllerAdvice
public class PlatformRestControllerExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException (Exception exception)
    {
        log.error("Exception handler executed", exception);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorResponse.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        String message = "Internal Server Error"; //exception.getMessage();
        errorResponse.setErrorMessage(message);

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    /**
     * 로그인 Exception Handler
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException (AuthenticationException exception)
    {
        log.error("AuthenticationException handler executed", exception);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(Integer.toString(HttpStatus.UNAUTHORIZED.value()));
        errorResponse.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());

        String message = "로그인 실패 했습니다. 관리자에게 문의 바랍니다.";
        if (BadCredentialsException.class.equals(exception.getClass()) //
                || UsernameNotFoundException.class.equals(exception.getClass()))
        {
            message = "아이디 또는 비밀번호가 올바르지 않습니다.";
        }
        errorResponse.setErrorMessage(message);

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.UNAUTHORIZED, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * [Swinglab] com.innerwave.Swinglab.core.exception.http.SwinglabUnauthorizedException.class
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(SwinglabUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException (SwinglabUnauthorizedException exception)
    {
        log.error("[Swinglab] SwinglabUnauthorizedException handler executed", exception);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(StringUtils.isNotEmpty(exception.getErrorCode()) ? exception.getErrorCode() : String.valueOf(exception.getStatusCode().value()));
        errorResponse.setTitle(StringUtils.isEmpty(exception.getTitle()) ? exception.getStatusCode().getReasonPhrase() : exception.getTitle());
        errorResponse.setErrorMessage(exception.getMessage());

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.valueOf(exception.getStatusCode().value()),
                errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * [Swinglab] com.innerwave.Swinglab.core.exception.http.SwinglabHttpException.class
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(SwinglabHttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException (SwinglabHttpException exception)
    {
        log.error("[Swinglab] SwinglabHttpException handler executed", exception);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(StringUtils.isNotEmpty(exception.getErrorCode()) ? exception.getErrorCode() : String.valueOf(exception.getStatusCode().value()));
        errorResponse.setTitle(StringUtils.isEmpty(exception.getTitle()) ? exception.getStatusCode().getReasonPhrase() : exception.getTitle());
        errorResponse.setErrorMessage(exception.getMessage());

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.valueOf(exception.getStatusCode().value()),
                errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException (HttpServletRequest request,
                                                                                HttpMessageNotReadableException exception)
    {

        logRequestBody(request);
        log.error("HttpMessageNotReadableException handler executed", exception);

        ErrorResponse errorMessage = new ErrorResponse();
        errorMessage.setErrorCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
        errorMessage.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = exception.getMessage();
        if (message == null || message.length() > 10)
        {
            message = exception.getClass().getSimpleName();
        }
        errorMessage.setErrorMessage(message);

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException (HttpServletRequest request,
                                                                                MethodArgumentNotValidException exception)
    {
        logRequestBody(request);
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors())
        {
            String message = "Invalid Parameters";
            List<String> details = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            SwinglabBadRequestException badRequestException = new SwinglabBadRequestException(message, details);
            return handleHttpException(badRequestException);
        }

        HttpRequestThreadLocal.setRestApiResponse(PlatformHttpStatus.BAD_REQUEST);
        return handleException(exception);
    }

    private void logRequestBody (HttpServletRequest request)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[METHOD] >>> ").append(request.getMethod()).append(" [URI] >>> ").append(request.getRequestURI())
                .append(" [PARAMS] >>> ").append(HttpRequestThreadLocal.getRestApiResponse().getRequestBody());
        log.error(sb.toString());
    }
}
