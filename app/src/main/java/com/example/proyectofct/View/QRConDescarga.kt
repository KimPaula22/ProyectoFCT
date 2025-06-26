package com.example.proyectofct.View

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

@Composable
fun QRConDescarga(numero: String) {
    val context = LocalContext.current
    val qrBitmap = remember(numero) { generarQR(numero) }
    val activity = context as? Activity
    var pendingPDF by remember { mutableStateOf<PdfDocument?>(null) }
    val createDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        if (uri != null && pendingPDF != null) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                pendingPDF!!.writeTo(outputStream)
            }
            Toast.makeText(context, "PDF guardado correctamente", Toast.LENGTH_LONG).show()
            pendingPDF!!.close()
            pendingPDF = null
        } else if (pendingPDF != null) {
            Toast.makeText(context, "No se pudo guardar el PDF", Toast.LENGTH_SHORT).show()
            pendingPDF!!.close()
            pendingPDF = null
        }
    }

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
                        val pdfDocument = crearPDFConQR(it)
                        pendingPDF = pdfDocument
                        if (Build.VERSION.SDK_INT >= 33) {
                            createDocumentLauncher.launch("codigo_qr.pdf")
                        } else {
                            // API 30-32: guardar directamente en Documents
                            guardarPDFDirecto(context, pdfDocument, "codigo_qr.pdf")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar como PDF")
                }
            } ?: Text("Error al generar QR")
        }
    }
}

fun crearPDFConQR(qrBitmap: Bitmap): PdfDocument {
    val pdfDocument = PdfDocument()
    val pageWidth = 300
    val pageHeight = 300
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val scaledQR = qrBitmap.scale(pageWidth - 100, pageHeight - 100)
    val left = (pageWidth - scaledQR.width) / 2f
    val top = (pageHeight - scaledQR.height) / 2f
    canvas.drawBitmap(scaledQR, left, top, null)
    pdfDocument.finishPage(page)
    return pdfDocument
}

// Función para guardar el QR como PDF
fun guardarPDFConPermisos(context: Context, pdfDocument: android.graphics.pdf.PdfDocument, fileName: String, pedirPermiso: Boolean = true) {
    val documentosDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val file = File(documentosDir, fileName)

    // Función para guardar el PDF
    fun guardarPDF() {
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

    // Comprobar permisos
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            guardarPDF()
        } else if (pedirPermiso) {
            mostrarDialogoPermisoAlmacenamiento(context)
        }
    } else {
        val permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permisoConcedido = ContextCompat.checkSelfPermission(context, permiso) == PermissionChecker.PERMISSION_GRANTED
        if (permisoConcedido) {
            guardarPDF()
        } else if (!pedirPermiso) {
            mostrarDialogoPermisoAlmacenamiento(context)
        }
    }
}

fun mostrarDialogoPermisoAlmacenamiento(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Permiso de almacenamiento requerido")
        .setMessage("Debes conceder permisos de almacenamiento para guardar el archivo. Pulsa 'Aceptar' para ir a la configuración de la app.")
        .setCancelable(false)
        .setPositiveButton("Aceptar") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
        .setNegativeButton("Cancelar", null)
        .show()
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

fun guardarPDFDirecto(context: Context, pdfDocument: PdfDocument, fileName: String) {
    val documentosDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val file = File(documentosDir, fileName)
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
