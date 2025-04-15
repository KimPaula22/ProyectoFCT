package com.example.proyectofct.View

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

@Composable
fun QRConDescarga(numero: String) {
    val context = LocalContext.current
    val qrBitmap = remember(numero) { generarQR(numero) }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            qrBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "QR generado",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        guardarQRComoPDF(context, it, "codigo_qr.pdf") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    Text("Guardar como PDF")
                }
            } ?: Text("Error al generar QR")
        }
    }
}

// Función para guardar el QR como PDF
fun guardarQRComoPDF(context: Context, qrBitmap: Bitmap, nombreArchivo: String) {
    val pdfDocument = PdfDocument()
    val pageWidth = 300
    val pageHeight = 300
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Escalamos el QR para que no sobresalga
    val scaledQR = qrBitmap.scale(pageWidth - 100, pageHeight - 100)

    val left = (pageWidth - scaledQR.width) / 2f
    val top = (pageHeight - scaledQR.height) / 2f
    canvas.drawBitmap(scaledQR, left, top, null)

    pdfDocument.finishPage(page)

    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, nombreArchivo)

    try {
        FileOutputStream(file).use {
            pdfDocument.writeTo(it)
        }
        Toast.makeText(context, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}

// Función para generar el QR
fun generarQR(texto: String, ancho: Int = 400, alto: Int = 400): Bitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            texto,
            BarcodeFormat.QR_CODE,
            ancho,
            alto
        )
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}