package com.example.familymenu.category.repository;
import com.example.familymenu.category.domain.DishCategory; import java.util.*;
public interface CategoryRepository { List<DishCategory> findAll(boolean includeDisabled); Optional<DishCategory> findById(Long id); Optional<DishCategory> findByName(String name); DishCategory save(DishCategory c); boolean deleteById(Long id); }
