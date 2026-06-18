package mx.utng.utngrunner.presentation.game

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import mx.utng.utngrunner.domain.model.GameState
import mx.utng.utngrunner.domain.model.Player
import mx.utng.utngrunner.domain.model.Obstacle
import mx.utng.utngrunner.domain.model.ObstacleType
import mx.utng.utngrunner.domain.model.Coin
import kotlin.math.abs
import kotlin.math.sin

class GameColors {
    val skyStart = Color(0xFF0D1B4A)
    val skyEnd = Color(0xFF1A237E)
    val ground = Color(0xFF388E3C)
    val groundHighlight = Color(0xFF81C784)
    val coinOuter = Color(0xFFFFD700)
    val coinInner = Color(0xFFFFF59D)
    val playerBody = Color(0xFFE65100)
    val playerHelmet = Color(0xFF1A237E)
    val playerVisor = Color(0xFF00E5FF)
}

/** GameRenderer: SOLO dibuja. No toca la lógica de juego. */
object GameRenderer {
 
    private val COLORS = GameColors()
 
    fun draw(canvas: Canvas, size: Size, state: GameState, frame: Long) {
        drawBackground(canvas, size)
        drawGround(canvas, size)
        drawCoins(canvas, state.coins, frame)
        drawObstacles(canvas, state.obstacles)
        drawPlayer(canvas, state.player, frame)
        drawHUD(canvas, size, state)
    }
 
    private fun drawBackground(canvas: Canvas, size: Size) {
        // Gradiente de cielo nocturno
        val paint = Paint().apply {
            shader = LinearGradientShader(
                from = Offset(0f, 0f), to = Offset(0f, size.height),
                colors = listOf(COLORS.skyStart, COLORS.skyEnd)
            )
        }
        canvas.drawRect(Rect(Offset.Zero, size), paint)
    }

    private fun drawGround(canvas: Canvas, size: Size) {
        val groundY = Player.FLOOR_Y + 20f
        
        // Ground body
        val bodyPaint = Paint().apply {
            color = COLORS.ground
        }
        canvas.drawRect(Rect(0f, groundY, size.width, size.height), bodyPaint)
        
        // Ground top line/grass highlight
        val linePaint = Paint().apply {
            color = COLORS.groundHighlight
            strokeWidth = 3f
        }
        canvas.drawLine(
            p1 = Offset(0f, groundY),
            p2 = Offset(size.width, groundY),
            paint = linePaint
        )
    }

    private fun drawCoins(canvas: Canvas, coins: List<Coin>, frame: Long) {
        val outerPaint = Paint().apply {
            color = COLORS.coinOuter
            isAntiAlias = true
        }
        val innerPaint = Paint().apply {
            color = COLORS.coinInner
            isAntiAlias = true
        }
        coins.forEach { coin ->
            if (!coin.collected) {
                // Scale factor for spinning/pulsating effect using its phase
                val scale = abs(sin(coin.phase))
                val radius = 8f
                
                // Draw a squashed circle (ellipse) to simulate spinning
                val rx = radius * scale
                val ry = radius
                
                canvas.drawOval(
                    rect = Rect(
                        left = coin.x - rx,
                        top = coin.y - ry,
                        right = coin.x + rx,
                        bottom = coin.y + ry
                    ),
                    paint = outerPaint
                )
                
                // Inner highlight
                canvas.drawOval(
                    rect = Rect(
                        left = coin.x - rx * 0.5f,
                        top = coin.y - ry * 0.5f,
                        right = coin.x + rx * 0.5f,
                        bottom = coin.y + ry * 0.5f
                    ),
                    paint = innerPaint
                )
            }
        }
    }

    private fun drawObstacles(canvas: Canvas, obstacles: List<Obstacle>) {
        val FLOOR = Player.FLOOR_Y + 20f
        
        obstacles.forEach { obs ->
            val left = obs.x
            val top = FLOOR - obs.height
            val right = obs.x + obs.width
            val bottom = FLOOR
            
            val paint = Paint().apply {
                isAntiAlias = true
            }
            
            when (obs.type) {
                ObstacleType.TAREA -> {
                    // Homework: Red notebook
                    paint.color = Color(0xFFD32F2F)
                    canvas.drawRect(Rect(left, top, right, bottom), paint)
                    // Notebook tab
                    canvas.drawRect(Rect(left, top - 4f, left + (obs.width / 2f), top), paint)
                    // White stripes representing lines
                    val linePaint = Paint().apply { color = Color.White }
                    canvas.drawRect(Rect(left + 4f, top + 6f, right - 4f, top + 8f), linePaint)
                    canvas.drawRect(Rect(left + 4f, top + 12f, right - 4f, top + 14f), linePaint)
                    canvas.drawRect(Rect(left + 4f, top + 18f, right - 4f, top + 20f), linePaint)
                }
                ObstacleType.EXAMEN -> {
                    // Exam: White sheet of paper with a border
                    paint.color = Color(0xFFEEEEEE)
                    canvas.drawRect(Rect(left, top, right, bottom), paint)
                    
                    val borderPaint = Paint().apply {
                        color = Color(0xFF9E9E9E)
                        style = PaintingStyle.Stroke
                        strokeWidth = 1f
                    }
                    canvas.drawRect(Rect(left, top, right, bottom), borderPaint)
                    
                    val redPaint = Paint().apply {
                        color = Color(0xFFD32F2F)
                        strokeWidth = 2f
                    }
                    canvas.drawLine(Offset(left + 3f, top + 5f), Offset(right - 3f, top + 5f), redPaint)
                    canvas.drawLine(Offset(left + 3f, top + 12f), Offset(right - 3f, top + 12f), redPaint)
                }
                ObstacleType.BUG -> {
                    // Bug: Orange neon bug with glowing green eyes
                    paint.color = Color(0xFFE64A19)
                    canvas.drawRect(Rect(left, top, right, bottom), paint)
                    canvas.drawRect(Rect(left + 2f, top - 4f, left + 5f, top), paint)
                    canvas.drawRect(Rect(right - 5f, top - 4f, right - 2f, top), paint)
                    
                    val eyePaint = Paint().apply { color = Color(0xFF00FF00) }
                    canvas.drawRect(Rect(left + 4f, top + 4f, left + 8f, top + 8f), eyePaint)
                    canvas.drawRect(Rect(right - 8f, top + 4f, right - 4f, top + 8f), eyePaint)
                }
                ObstacleType.REPO -> {
                    // Repo: Blue database storage
                    paint.color = Color(0xFF1976D2)
                    canvas.drawRect(Rect(left, top, right, bottom), paint)
                    val linePaint = Paint().apply {
                        color = Color(0xFF0D47A1)
                        strokeWidth = 2f
                    }
                    canvas.drawLine(Offset(left, top + 6f), Offset(right, top + 6f), linePaint)
                    canvas.drawLine(Offset(left, top + 12f), Offset(right, top + 12f), linePaint)
                }
            }
        }
    }
 
    private fun drawPlayer(canvas: Canvas, player: Player, frame: Long) {
        // Parpadeo de invencibilidad
        val alpha = if (player.isInvincible && (frame / 4) % 2 == 0L) 0.3f else 1f
        val legSwing = if (player.isJumping) 0f else sin(frame * 0.3f) * 8f
        val yPos = player.y
 
        val bodyPaint = Paint().apply {
            color = COLORS.playerBody.copy(alpha = alpha)
        }
        val helmetPaint = Paint().apply {
            color = COLORS.playerHelmet.copy(alpha = alpha)
        }
        val visorPaint = Paint().apply {
            color = COLORS.playerVisor.copy(alpha = alpha)
        }
        val shoePaint = Paint().apply { color = Color.White.copy(alpha = alpha) }

        if (player.isSliding) {
            // Crouched character (sliding)
            canvas.drawRect(Rect(player.x - 10f, yPos + 4f, player.x + 14f, yPos + 18f), bodyPaint)
            // Squished helmet
            canvas.drawRect(Rect(player.x - 8f, yPos - 4f, player.x + 10f, yPos + 4f), helmetPaint)
            // Visor
            canvas.drawRect(Rect(player.x + 4f, yPos - 1f, player.x + 9f, yPos + 3f), visorPaint)
        } else {
            // Normal standing character (running/jumping)
            canvas.drawRect(Rect(player.x - 6f, yPos - 10f, player.x + 12f, yPos + 14f), bodyPaint)
            
            // Helmet UTNG
            canvas.drawRect(Rect(player.x - 5f, yPos - 22f, player.x + 11f, yPos - 10f), helmetPaint)
            
            // Visor
            canvas.drawRect(Rect(player.x + 4f, yPos - 18f, player.x + 10f, yPos - 13f), visorPaint)
            
            // Running/jumping legs
            canvas.drawLine(
                p1 = Offset(player.x - 2f, yPos + 14f),
                p2 = Offset(player.x - 2f - legSwing, yPos + 22f),
                paint = shoePaint.apply { strokeWidth = 3f }
            )
            canvas.drawLine(
                p1 = Offset(player.x + 6f, yPos + 14f),
                p2 = Offset(player.x + 6f + legSwing, yPos + 22f),
                paint = shoePaint.apply { strokeWidth = 3f }
            )
        }
    }
 
    private fun drawHUD(canvas: Canvas, size: Size, state: GameState) {
        val cx = size.width / 2f
        // Hora del sistema en la parte superior
        drawCenteredText(canvas, getSystemTime(), cx, 24f, 14.sp)
        // Puntuación inferior
        drawCenteredText(canvas, "${state.score} pts", cx, size.height - 12f, 11.sp)
        // Vidas
        repeat(state.lives) { i ->
            drawHeart(canvas, 12f + i * 16f, 32f)
        }
    }

    private fun drawCenteredText(canvas: Canvas, text: String, x: Float, y: Float, fontSize: TextUnit) {
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = fontSize.value * 1.5f
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.nativeCanvas.drawText(text, x, y, paint)
    }

    private fun drawHeart(canvas: Canvas, x: Float, y: Float) {
        val paint = Paint().apply {
            color = Color(0xFFE53935)
            isAntiAlias = true
        }
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(x, y + 4f)
            cubicTo(x - 6f, y - 4f, x - 12f, y + 2f, x, y + 12f)
            cubicTo(x + 12f, y + 2f, x + 6f, y - 4f, x, y + 4f)
            close()
        }
        canvas.drawPath(path, paint)
    }

    private fun getSystemTime(): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}
