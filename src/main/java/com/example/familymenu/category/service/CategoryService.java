package com.example.familymenu.category.service;
import com.example.familymenu.category.domain.DishCategory; import java.util.*;
public interface CategoryService { List<DishCategory> list(boolean all); DishCategory create(String n,Integer s,Boolean e); DishCategory update(Long id,String n,Integer s,Boolean e); void delete(Long id); }
