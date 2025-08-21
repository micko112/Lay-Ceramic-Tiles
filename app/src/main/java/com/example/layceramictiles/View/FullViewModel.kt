
import android.util.Log
import com.example.layceramictiles.View.ScreenCalculateViewModel
import com.example.layceramictiles.View.ScreenMaterialsViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


fun saveProjectToFirestore(
    fileName: String,
    viewModelCalc: ScreenCalculateViewModel,
    viewModelMat: ScreenMaterialsViewModel,
    wallTileArea: Float,
    floorTileArea: Float
) {
    val db = Firebase.firestore

    val data = hashMapOf(
        "timestamp" to FieldValue.serverTimestamp(),

        // calculate input
        "lengthB" to viewModelCalc.lengthB.value,
        "widthA" to viewModelCalc.widthA.value,
        "heightH" to viewModelCalc.heightH.value,

        // calculate result
        "resultsCalculate" to viewModelCalc.results.value,

        // materials input
        "tileWallWidth" to viewModelMat.tileWallWidth.value,
        "tileWallLength" to viewModelMat.tileWallLength.value,
        "tileFloorWidth" to viewModelMat.tileFloorWidth.value,
        "tileFloorLength" to viewModelMat.tileFloorLength.value,
        "wallGroutWidth" to viewModelMat.wallGroutWidth.value,
        "floorGroutWidth" to viewModelMat.floorGroutWidth.value,

        // materials result
        "wallTileArea" to wallTileArea,
        "floorTileArea" to floorTileArea,
        "adhesive" to viewModelMat.results.value.getOrNull(0),
        "grout" to viewModelMat.results.value.getOrNull(1)
    )

    db.collection("Full Baza")
        .document(fileName)
        .set(data, SetOptions.merge())
        .addOnSuccessListener {
            Log.d("Firestore", "Uspešno sačuvano pod imenom $fileName")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Greška pri čuvanju", e)
        }
}
