package np.com.ismt.sample.mealmate.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object HelperUtil {
    fun Base64toBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}