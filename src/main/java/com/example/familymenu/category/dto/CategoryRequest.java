package com.example.familymenu.category.dto;
import javax.validation.constraints.NotBlank; import javax.validation.constraints.Size;
public class CategoryRequest { @NotBlank @Size(max=50) private String name; private Integer sort; private Boolean enabled; public String getName(){return name;} public void setName(String v){name=v;} public Integer getSort(){return sort;} public void setSort(Integer v){sort=v;} public Boolean getEnabled(){return enabled;} public void setEnabled(Boolean v){enabled=v;} }
