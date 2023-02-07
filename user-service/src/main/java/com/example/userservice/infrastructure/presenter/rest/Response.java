package com.example.userservice.infrastructure.presenter.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response {
    private Object data;
    private Object page;
    private Object errors;
    private String error;
    private String message;
    private String path;
    private int httpCode = 200;

    public Response(Object dataParam){
        this.data = dataParam;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public void setPageData(Page data){
        if (data.getNumberOfElements() > 0){
            this.data = data.getContent();
        }

        Map<String, Object> page = new HashMap<String, Object>();
        page.put("number_element", data.getNumberOfElements());
        page.put("total_pages", data.getTotalPages());
        page.put("total_elements", data.getTotalElements());
        page.put("last", data.isLast());
        page.put("size", data.getSize());
        page.put("page", data.getNumber());
        this.page = page;
    }
    public void setError(Exception e){
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }

    public ResponseEntity<Object> getResponse(){
        Map<String, Object> response = new HashMap<String, Object>();
        // Data Handler
        if (this.data != null){
            response.put("data", this.data);
        } else {
            if (this.httpCode == 200 && this.errors == null){
                this.httpCode = 404;
            }
        }
        if (this.page != null){
            response.put("page", this.page);
        }
        // Meta Handler
        Map<String, Object> meta = new HashMap<String, Object>();
        if (this.errors != null){
            meta.put("errors", this.errors);
            if (this.httpCode < 400){
                this.httpCode = 500;
            }
        }
        meta.put("code", httpCode);
        meta.put("timestamp", System.currentTimeMillis());
        if (this.error != null){
            meta.put("error", this.error);
            if (this.httpCode < 400){
                this.httpCode = 500;
            }
        }
        if (this.message != null){
            meta.put("message", this.message);
        }
        if (this.path != null){
            meta.put("path", this.path);
        }
        response.put("meta", meta);

        return new ResponseEntity<Object>(response, HttpStatusCode.valueOf(httpCode));
    }
}