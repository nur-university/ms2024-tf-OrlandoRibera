package infrastructure.repositories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodJpaModel;
import infrastructure.model.FoodPackageJpaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import infrastructure.repositories.FoodRepository;
import infrastructure.repositories.foodpackage.FoodPackageCrudRepository;
import infrastructure.utils.FoodUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class FoodJpaRepository implements FoodRepository {

  @Autowired
  private FoodCrudRepository foodCrudRepository;

  @Autowired
  private FoodPackageCrudRepository foodPackageCrudRepository;

  @Override
  public UUID update(Food food) {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findById(food.getFoodPackageId()).orElse(null);
    if (foodPackageJpaModel == null) return null;

    FoodJpaModel foodJpaModel = FoodUtils.foodToJpaEntity(food, foodPackageJpaModel);
    return foodCrudRepository.save(foodJpaModel).getId();
  }

  @Override
  public List<Food> findByFoodPackageId(UUID foodPackageId) throws BusinessRuleValidationException {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findById(foodPackageId).orElse(null);
    if (foodPackageJpaModel == null) return Collections.emptyList();

    List<FoodJpaModel> jpaModels = foodCrudRepository.findByFoodPackageId(foodPackageJpaModel);
    if (jpaModels == null || jpaModels.isEmpty()) return Collections.emptyList();

    List<Food> foods = new ArrayList<>();
    for (FoodJpaModel jpaModel : jpaModels) {
      foods.add(FoodUtils.jpaToFood(jpaModel));
    }

    return foods;
  }
}