package np.com.ismt.sample.mealmate.models

data class Grocery(
    val name: String = "",

    @field:JvmField
    var isPurchased: Boolean = false
)
