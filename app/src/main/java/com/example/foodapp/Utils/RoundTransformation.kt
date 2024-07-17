import android.graphics.*
import com.squareup.picasso.Transformation

class RoundTransformation(private val radius: Float) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, radius, radius, paint)

        source.recycle()

        return bitmap
    }

    override fun key(): String {
        return "round(radius=$radius)"
    }
}
