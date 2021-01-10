package com.example.diabetes.model.UserReponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("statuscode")
@Expose
private Integer statuscode;
@SerializedName("total")
@Expose
private Integer total;
@SerializedName("data")
@Expose
private List<User> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Integer getStatuscode() {
return statuscode;
}

public void setStatuscode(Integer statuscode) {
this.statuscode = statuscode;
}

public Integer getTotal() {
return total;
}

public void setTotal(Integer total) {
this.total = total;
}

public List<User> getData() {
return data;
}

public void setData(List<User> data) {
this.data = data;
}

}