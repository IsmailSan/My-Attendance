import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.test.my_attendance.databinding.ItemPredictionsBinding
import com.test.my_attendance.util.MyDiffUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PredictionsAdapter : RecyclerView.Adapter<PredictionsAdapter.MyViewHolder>() {

    private var predictions = emptyList<AutocompletePrediction>()

    private val _placeId = MutableStateFlow("")
    val placeId: StateFlow<String> get() = _placeId

    class MyViewHolder(val binding: ItemPredictionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(prediction: AutocompletePrediction) {
            binding.prediction = prediction
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPredictionsBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPrediction = predictions[position]
        holder.bind(currentPrediction)

        holder.binding.rootLayout.setOnClickListener {
            setPlaceId(predictions[position].placeId)
        }
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    private fun setPlaceId(placeId: String){
        _placeId.value = placeId
    }

    fun setData(newPredictions: List<AutocompletePrediction>){
        val myDiffUtil = MyDiffUtil(predictions, newPredictions)
        val myDiffUtilResult = DiffUtil.calculateDiff(myDiffUtil)
        predictions = newPredictions
        myDiffUtilResult.dispatchUpdatesTo(this)
    }
}