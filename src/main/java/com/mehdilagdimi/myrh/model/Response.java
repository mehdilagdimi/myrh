package com.mehdilagdimi.myrh.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Response {

    @JsonProperty(value = "status", access = JsonProperty.Access.READ_ONLY)
    private int statusCode;
    @JsonProperty(value = "responseStatus", access = JsonProperty.Access.READ_ONLY)
    private HttpStatus status;
    @JsonProperty(value = "message", access = JsonProperty.Access.READ_ONLY)
    private String msg;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String date;

    @JsonIgnore
    private Timestamp timestamp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private Map<String, Object> data;

    public Response(){

    }
    public Response(HttpStatus status, String msg) {
        this.status = status;
        this.statusCode = status.value();
        this.msg = msg;
        this.timestamp = Timestamp.from(Instant.now());
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this.timestamp);
    }

    public Response(HttpStatus status, String msg, String key, Object value) {
        this.status = status;
        this.statusCode = status.value();
        this.msg = msg;
        this.timestamp = Timestamp.from(Instant.now());
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this.timestamp);
        this.addData(key, value);
    }
    public Response(HttpStatus status, String msg, Map<String, Object> data) {
        this.status = status;
        this.statusCode = status.value();
        this.msg = msg;
        this.timestamp = Timestamp.from(Instant.now());
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this.timestamp);
        this.data = data;
    }

    public void addData(String key, Object value) {
        if(data == null) data = new HashMap<>();
        data.put(key, value);
    }

}
