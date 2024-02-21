package com.intuit.demo.util;

import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.model.response.ResponseBaseModel;
import org.springframework.http.HttpStatus;

import java.util.List;

public class APIResponseUtil {

    public static <K extends ResponseBaseModel> APIResponse<K> createSuccessAPIResponse(K response) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setData(response);
        apiResponse.setStatus(HttpStatus.OK.value());
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createFailureBadRequestAPIResponse(List<Error> errorList, String message) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setErrors(errorList);
        apiResponse.setMessage(message);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createFailureUnauthorisedAPIResponse(String message) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createFailureInternalErrorResponse(String message) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createRateLimitErrorResponse() {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        apiResponse.setMessage(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createTimeoutErrorResponse(String message) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <K extends ResponseBaseModel> APIResponse<K> createServiceUnavailableErrorResponse(String message) {
        APIResponse<K> apiResponse = new APIResponse<>();
        apiResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        apiResponse.setMessage(message);
        return apiResponse;
    }
}
