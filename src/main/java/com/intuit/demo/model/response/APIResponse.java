package com.intuit.demo.model.response;

import com.intuit.demo.model.response.Error;
import com.intuit.demo.model.response.ResponseBaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public final class APIResponse<K extends ResponseBaseModel> implements Serializable {

    private K data;
    private String message;
    private List<Error> errors;
    private Integer status;

    public boolean isSuccess() {
        return 200 == status;
    }
}
