package com.sapient.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Category {
	private Integer categoryID;
	private String name;
}
