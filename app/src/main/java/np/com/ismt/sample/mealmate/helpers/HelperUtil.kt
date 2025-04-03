package np.com.ismt.sample.mealmate.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import np.com.ismt.sample.mealmate.R

object HelperUtil {
    fun base64toBitmap(base64String: String, context: Context): Bitmap {
        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (exception: Exception) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_image_24)
            return drawable!!.toBitmap()
        }

    }
}