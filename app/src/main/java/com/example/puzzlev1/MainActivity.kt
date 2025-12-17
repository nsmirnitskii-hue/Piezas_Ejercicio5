package com.example.puzzlev1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleApp()
        }
    }
}

data class BoxInfo( //Guarda información de cada espacio del tablero
    val x: Float = 0f, //Posición X en pantalla
    val y: Float = 0f, //Posición Y en pantalla
    val col: Int = -1, //Columna
    val row: Int = -1, // Fila
    )
@Composable
fun PuzzleApp() {
    val state = rememberScrollState()
    val image = ImageBitmap.imageResource(R.drawable.fondopuzzle) //Carga la imagen del rompecabezas desde drawable
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { //Crea una matriz 2x2 para guardar las posiciones del tablero cada celda tendrá su x, y, fila y columna.
        val arrayBox=Array(2) { Array(4) { BoxInfo(
        ) } }
        // Tablero arriba (solo marco vacío)
        Column {
            //Doble for = filas y columnas. dibuja cada casilla del tablero
            for (row in 0..arrayBox.size-1) {
                Row{
                    for (col in 0..arrayBox[row].size-1) {
                        Box(
                            modifier = Modifier
                                .size(100.dp) //Casilla de 100x100 dp
                                .alpha(0.5f) //Semi transparente (solo guía visual)
                                //.padding(4.dp)
                                //.clip(RoundedCornerShape(8.dp))
                                //Dibuja solo una parte de la imagen
                                //Cada casilla muestra su fragmento correcto
                                .drawBehind {
                                    drawImage(
                                        image = image,
                                        srcOffset = IntOffset(col*size.width.toInt(), row*size.height.toInt()),   // desde dónde recortar
                                        srcSize = IntSize(size.width.toInt(), size.height.toInt()),       // tamaño del recorte
                                        dstSize = IntSize(size.width.toInt(), size.height.toInt())
                                    )
                                }
                                //Guarda posición exacta en pantalla de cada casilla
                                .onGloballyPositioned { coords ->
                                    arrayBox[row][col]= BoxInfo( coords.positionInWindow().x,coords.positionInWindow().y,col,row)

                                }

                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Piezas abajo
        Row(Modifier.horizontalScroll(state)) {
            listOf(0, 1, 2, 3, 4, 5).shuffled().forEach { piece -> //Crea las piezas mezcladas
                //DraggablePiece(piece)
                //DraggablePieceConCabezas(piece)
                //DraggablePieceMap(piece)
                DraggablePieceMapAgujeros(piece,image,arrayBox)
                /*
                *Llama al composable que:
                Dibuja la pieza
                Permite arrastrarla
                Verifica si encaja */
            }
        }
    }
}
