import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.models.NoteCellViewModel

class NotesRecyclerViewAdapter(private val noteList: List<NoteCellViewModel>, private val onNoteClicked: (position: Int) -> Unit) : RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_cell, parent, false)

        return ViewHolder(view, onNoteClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val noteCellViewModel = noteList[position]
        holder.noteTitle.text = noteCellViewModel.title

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}
