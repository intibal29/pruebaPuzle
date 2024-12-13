package es.icjardin.puzzle;

import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val puzzlePieces = mutableListOf<ImageView>()
    private val pieceIds: Array<Int> = arrayOf(
        R.drawable.puzle_1, R.drawable.puzle_2, R.drawable.puzle_3,
        R.drawable.puzle_4, R.drawable.puzle_5, R.drawable.puzle_6,
        R.drawable.puzle_7, R.drawable.puzle_8, R.drawable.puzle_9,
        R.drawable.puzle_10, R.drawable.puzle_11, R.drawable.puzle_12,
        R.drawable.puzle_13, R.drawable.puzle_14, R.drawable.puzle_6,
        R.drawable.puzle_blanco // Usamos "blank" como el espacio vacío
    )
    private val correctOrder = mutableListOf<Int>()
    private var blankPosition = 15  // La posición vacía comienza en la última (índice 15)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)

        // Cargar las piezas del puzzle
        for (i in 0 until 16) {
            val imageView = ImageView(this)
            imageView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                rowSpec = GridLayout.spec(i / 4)
                columnSpec = GridLayout.spec(i % 4)
                setMargins(4, 4, 4, 4)
            }

            if (i == 15) { // La última posición es el espacio vacío
                imageView.setImageResource(R.drawable.puzle_blanco)  // Imagen del espacio vacío
            } else {
                imageView.setImageResource(pieceIds[i])
            }

            imageView.setOnClickListener { onPieceClicked(i) }
            puzzlePieces.add(imageView)
            gridLayout.addView(imageView)

            correctOrder.add(i)  // Guardamos el orden correcto
        }

        // Mezclar las piezas del puzzle
        shufflePieces()
    }

    // Función para mezclar las piezas del puzzle
    private fun shufflePieces() {
        val shuffledOrder = correctOrder.shuffled()
        for (i in puzzlePieces.indices) {
            if (i == 15) {
                puzzlePieces[i].setImageResource(R.drawable.puzle_blanco) // El espacio vacío siempre va al final
            } else {
                puzzlePieces[i].setImageResource(pieceIds[shuffledOrder[i]])
            }
        }
    }

    // Función que se ejecuta cuando una pieza es tocada
    private fun onPieceClicked(index: Int) {
        if (canMovePiece(index)) {
            // Intercambiar las piezas
            swapPieces(index)
            // Verificar si el puzzle está resuelto
            if (isPuzzleSolved()) {
                // Aquí puedes agregar una lógica para mostrar que el puzzle fue resuelto
            }
        }
    }

    // Verificar si una pieza puede moverse (si está al lado del espacio vacío)
    private fun canMovePiece(index: Int): Boolean {
        val row = index / 4
        val col = index % 4
        val blankRow = blankPosition / 4
        val blankCol = blankPosition % 4

        // Verificar si la pieza está adyacente al espacio vacío (en filas o columnas contiguas)
        return (row == blankRow && (col == blankCol - 1 || col == blankCol + 1)) ||
                (col == blankCol && (row == blankRow - 1 || row == blankRow + 1))
    }

    // Intercambiar las piezas
    private fun swapPieces(index: Int) {
        // Intercambiar la pieza seleccionada con la pieza vacía
        val temp = puzzlePieces[index].drawable
        puzzlePieces[index].setImageResource(R.drawable.puzle_blanco)
        puzzlePieces[blankPosition].setImageDrawable(temp)

        // Actualizar la posición del espacio vacío
        blankPosition = index
    }

    // Verificar si el puzzle está resuelto
    private fun isPuzzleSolved(): Boolean {
        for (i in puzzlePieces.indices) {
            if (i != 15 && puzzlePieces[i].drawable != resources.getDrawable(pieceIds[i], null)) {
                return false
            }
        }
        return true
    }
}
