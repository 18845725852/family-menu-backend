package com.example.familymenu.menu;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.category.repository.CategoryRepository;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.dto.CreateDishRequest;
import com.example.familymenu.dish.dto.DishResponse;
import com.example.familymenu.dish.repository.DishRepository;
import com.example.familymenu.family.repository.FamilyRepository;
import com.example.familymenu.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FamilyMenuService {
    private final FamilyMenuRepository menuRepository;
    private final FamilyRepository familyRepository;
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public FamilyMenuService(FamilyMenuRepository menuRepository, FamilyRepository familyRepository,
                              DishRepository dishRepository, CategoryRepository categoryRepository,
                              OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.familyRepository = familyRepository;
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    public FamilyMenuResponse get(Long familyId, Long userId) {
        requireMember(familyId, userId);
        return response(familyId);
    }

    public FamilyMenuResponse setCustomMenu(Long familyId, Long userId, Boolean customMenu) {
        requireMember(familyId, userId);
        if (customMenu == null) throw new BusinessException("请选择是否使用自定义菜谱");
        if (customMenu.booleanValue() && dishRepository.countByFamily(familyId) == 0) {
            List<Dish> defaults = dishRepository.findAvailable(null);
            for (Dish dish : defaults) {
                dishRepository.save(new Dish(null, dish.getName(), dish.getCategory(), dish.getDescription(), dish.getRecipe(),
                        dish.getImageUrl(), true, dish.getSort(), familyId));
            }
        }
        menuRepository.setCustomMenu(familyId, customMenu.booleanValue());
        return response(familyId);
    }

    public Dish create(Long familyId, Long userId, CreateDishRequest request) {
        requireCustomMenu(familyId, userId);
        String name = cleanName(request.getName());
        String category = cleanCategory(request.getCategory());
        ensureUnique(familyId, name, category, null);
        int sort = request.getSort() == null ? 0 : request.getSort().intValue();
        return dishRepository.save(new Dish(null, name, category, cleanText(request.getDescription(), 200),
                cleanText(request.getRecipe(), 4000), cleanImage(request.getImageUrl()), true, sort, familyId));
    }

    public Dish update(Long familyId, Long userId, Long dishId, CreateDishRequest request) {
        requireCustomMenu(familyId, userId);
        Dish dish = requireFamilyDish(familyId, dishId);
        String name = cleanName(request.getName());
        String category = cleanCategory(request.getCategory());
        ensureUnique(familyId, name, category, dishId);
        int sort = request.getSort() == null ? dish.getSort() : request.getSort().intValue();
        String imageUrl = request.getImageUrl() == null ? dish.getImageUrl() : cleanImage(request.getImageUrl());
        return dishRepository.save(new Dish(dish.getId(), name, category, cleanText(request.getDescription(), 200),
                cleanText(request.getRecipe(), 4000), imageUrl, dish.isAvailable(), sort, familyId));
    }

    public void delete(Long familyId, Long userId, Long dishId) {
        requireCustomMenu(familyId, userId);
        requireFamilyDish(familyId, dishId);
        if (orderRepository.countItemsByDishId(dishId) > 0) {
            throw new BusinessException("菜品已存在历史订单，不能删除");
        }
        if (!dishRepository.deleteById(dishId)) throw new BusinessException("菜品不存在");
    }

    public void requireOrderDish(Long familyId, Dish dish) {
        boolean custom = menuRepository.isCustomMenu(familyId);
        if (custom) {
            if (!familyId.equals(dish.getFamilyId())) throw new BusinessException("只能点当前家庭菜谱里的菜");
        } else if (dish.getFamilyId() != null) {
            throw new BusinessException("只能点默认菜谱里的菜");
        }
    }

    private FamilyMenuResponse response(Long familyId) {
        boolean custom = menuRepository.isCustomMenu(familyId);
        List<Dish> dishes = custom ? dishRepository.findAvailableByFamily(familyId) : dishRepository.findAvailable(null);
        List<DishResponse> list = new ArrayList<DishResponse>();
        for (Dish dish : dishes) list.add(DishResponse.from(dish));
        return new FamilyMenuResponse(custom, list);
    }

    private void requireCustomMenu(Long familyId, Long userId) {
        requireMember(familyId, userId);
        if (!menuRepository.isCustomMenu(familyId)) throw new BusinessException("请先开启家庭自定义菜谱");
    }

    private Dish requireFamilyDish(Long familyId, Long dishId) {
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new BusinessException("菜品不存在"));
        if (!familyId.equals(dish.getFamilyId())) throw new BusinessException("不能修改其他菜谱的菜品");
        return dish;
    }

    private void requireMember(Long familyId, Long userId) {
        if (familyId == null || !familyRepository.findById(familyId).isPresent() || !familyRepository.isMember(familyId, userId)) {
            throw new BusinessException("你不是该家庭成员");
        }
    }

    private void ensureUnique(Long familyId, String name, String category, Long excludeId) {
        if (dishRepository.existsFamilyDish(familyId, name, category, excludeId)) {
            throw new BusinessException("同分类下已存在相同菜名");
        }
    }

    private String cleanName(String name) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) throw new BusinessException("菜名不能为空");
        if (value.length() > 50) throw new BusinessException("菜名不能超过50个字");
        return value;
    }

    private String cleanCategory(String category) {
        String value = category == null ? "" : category.trim();
        if (!categoryRepository.findByName(value).filter(item -> item.isEnabled()).isPresent()) {
            throw new BusinessException("菜品类型不存在或已停用");
        }
        return value;
    }

    private String cleanText(String value, int max) {
        if (value == null) return null;
        String text = value.trim();
        if (text.isEmpty()) return null;
        if (text.length() > max) throw new BusinessException("内容不能超过" + max + "个字");
        return text;
    }

    private String cleanImage(String imageUrl) {
        if (imageUrl == null) return null;
        String value = imageUrl.trim();
        if (value.isEmpty()) return null;
        if (value.length() > 500) throw new BusinessException("图片地址过长");
        return value;
    }
}
