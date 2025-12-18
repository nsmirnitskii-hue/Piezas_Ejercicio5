package com.example.puzzlev1

import android.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.xr.compose.testing.toDp

@Composable
/*
Dibuja una pieza de rompecabezas real, con:
Cabezas
Huecos
Imagen recortada
*/
fun PuzzlePieceShape(
    modifier: Modifier = Modifier,
    topHead: Boolean = false,
    topHole: Boolean = false,
    rightHead: Boolean = false,
    rightHole: Boolean = false,
    bottomHead: Boolean = false,
    bottomHole: Boolean = false,
    leftHead: Boolean = false,
    leftHole: Boolean = false,
    image: ImageBitmap,
    row: Int,
    column: Int,
    isTouching: Boolean
) {
    Canvas(modifier = modifier) { //Area de dibujo personalizada
        val w = size.width
        val h = size.height //Tamaño de la pieza
        val knobSize = w * 0.2f //Tamaño de las orejas del puzzle

        val path = Path().apply { //Define la forma exacta de la pieza
            moveTo(0f, 0f)

            // borde superior
            //Decide si hay:
            if (topHead) { // Cabeza
                //lineTo(0f, knobSize)
                lineTo(w * 0.4f, knobSize)
                cubicTo(w * 0.45f, 0f, w * 0.55f, 0f, w * 0.6f, knobSize)
                lineTo(w, 0f)

            } else if (topHole) { //Hueco
                lineTo(w * 0.4f, 0f)
                cubicTo(w * 0.45f, +knobSize, w * 0.55f, +knobSize, w * 0.6f, 0f)
                lineTo(w, 0f)
            } else { //Linea recta
                lineTo(w, 0f)
            }

            // borde derecho
            if (rightHead) {
                lineTo(w, h * 0.4f)
                cubicTo(w + knobSize, h * 0.45f, w + knobSize, h * 0.55f, w, h * 0.6f)
                lineTo(w, h)
            } else if (rightHole) {
                lineTo(w+knobSize, h * 0.4f)
                cubicTo(w , h * 0.45f, w , h * 0.55f, w+knobSize, h * 0.6f)
                lineTo(w, h)
            } else {
                lineTo(w, h)
            }

            // borde inferior
            if (bottomHead) {
                lineTo(w * 0.6f, h)
                cubicTo(w * 0.55f, h + knobSize, w * 0.45f, h + knobSize, w * 0.4f, h)
                lineTo(0f, h)
            } else if (bottomHole) {
                lineTo(w * 0.6f, h+knobSize)
                cubicTo(w * 0.55f, h , w * 0.45f, h , w * 0.4f, h+knobSize)
                lineTo(0f, h)



            } else {
                lineTo(0f, h)
            }

            // borde izquierdo
            if (leftHead) {

                lineTo(knobSize, h * 0.6f)
                cubicTo(0f, h * 0.55f, 0f, h * 0.45f,knobSize , h * 0.4f)
                //cubicTo(w , h * 0.45f, w , h * 0.55f, w+knobSize, h * 0.6f)

                lineTo(0f, 0f)
            } else if (leftHole) {
                lineTo(0f, h * 0.6f)
                cubicTo(+knobSize, h * 0.55f, +knobSize, h * 0.45f, 0f, h * 0.4f)
                lineTo(0f, 0f)
            } else {
                lineTo(0f, 0f)
            }

            close()
        }


        clipPath(path) { //Recorta la imagen con la forma del rompecabezas
            drawImage(
                image = image,
                srcOffset = IntOffset(((column*w)).toInt(), ((row*h)).toInt()),
                srcSize = IntSize(((w+2*knobSize)).toInt(), ((h+2*knobSize)).toInt()),
                dstSize = IntSize((size.width+2*knobSize).toInt(), (size.height+2*knobSize).toInt())
            )
        }
        if(isTouching){ //Si está bien posicionada se dibuja borde verde
            drawPath(
                path = path,
                color = Color(0xFF39FF14),
                style = Stroke(width = 6f)
            )
        }

    }
}
data class PieceShapev4(
    val topHead: Boolean = false,
    val topHole: Boolean = false,
    val rightHead: Boolean = false,
    val rightHole: Boolean = false,
    val bottomHead: Boolean = false,
    val bottomHole: Boolean = false,
    val leftHead: Boolean = false,
    val leftHole: Boolean = false,
    val row: Int=-1,
    val column: Int=-1
)
/*
* Describe cómo es cada pieza:
Qué lados tienen cabeza o hueco
A qué fila y columna pertenece
 */
val pieceMapv2: List<PieceShapev4> = listOf( //Define las 4 piezas del puzzle
    // Fila 0, Columna 0
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=0,column=0 ),
    // Fila 0, Columna 1
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=true,bottomHole=false,leftHead=false,leftHole=true,row=0,column=1 ),
    // Fila 1, Columna 0
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=1,column=0),
    // Fila 1, Columna 1
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=true,bottomHole=false,leftHead=true,leftHole=false,row=1,column=1),
    //Fila 0, Columna 2
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=2 ),
    //FIla 1, Columna 2
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=2),
    //Fila 0, Columna 3
    PieceShapev4(topHead = false, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=3),
    //Fila 1, Columna 3
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=3),
    //Fila 2, Columna 0
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=false,row=2,column=0),
    //Fila 2, Columna 1
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=1),
    //Fila 2, Columna 2
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=true,leftHole=false,row=2,column=2),
    //Fila 2, Columna 3
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=3)
)
@Composable
fun DraggablePieceMapAgujeros(pieceIndex: Int, image: ImageBitmap,arrayBox: Array<Array<BoxInfo>>) { //Pieza arrastrable
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0.dp) } //Guarda cuánto se movió la pieza
    var offsetY by remember { mutableStateOf(0.dp) } //Guarda cuánto se movió la pieza
    var isTouching by remember { mutableStateOf(false) }

    val shape = pieceMapv2[pieceIndex]

    PuzzlePieceShape(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(100.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount -> //Detecta el arrastre con el dedo
                    change.consume()
                    offsetX += with(density) { dragAmount.x.toDp() } //Mueve la pieza en pantalla
                    offsetY += with(density) { dragAmount.y.toDp() } //Mueve la pieza en pantalla

                }

            }.onGloballyPositioned { coords ->
                val canvasRect = coords.boundsInWindow()
                if(((canvasRect.topLeft.y-(arrayBox[shape.row][shape.column].y)).absoluteValue<10) and ((canvasRect.topLeft.x-(arrayBox[shape.row][shape.column].x)).absoluteValue<10)){
                    /*Compara:
                     Posición de la pieza
                    Posición del tablero*/
                    isTouching=true
                }else{
                    isTouching=false
                }
            },
        topHead = shape.topHead,
        topHole = shape.topHole,
        rightHead = shape.rightHead,
        rightHole = shape.rightHole,
        bottomHead = shape.bottomHead,
        bottomHole = shape.bottomHole,
        leftHead = shape.leftHead,
        leftHole = shape.leftHole,
        image =image,
        row=shape.row,
        column=shape.column,
        isTouching
    )}