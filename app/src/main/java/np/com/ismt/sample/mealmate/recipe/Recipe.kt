package np.com.ismt.sample.mealmate.recipe

data class Recipe(
    val name: String? = "",
    val image: String? = "",
    val description: String? = "",
    val category: String? = "",
    val cookingTime: String? = "",
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList(),
    val nutritionValue: NutritionValue? = null,
    var isMyRecipe: Boolean = false,
    var isSelectedForMeal: Boolean = false
)

data class Ingredient(
    val name: String? = "",
    val quantity: String? = "",
    val measuringValue: String? = ""
)

data class NutritionValue(
    val carbs: String? = "",
    val protein: String? = "",
    val calories: String? = ""
)