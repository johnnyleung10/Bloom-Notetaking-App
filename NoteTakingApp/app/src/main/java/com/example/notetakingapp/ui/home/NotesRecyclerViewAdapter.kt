import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.models.NoteCellViewModel

class NotesRecyclerViewAdapter(private var noteList: List<NoteCellViewModel>, private val onNoteClicked: (position: Int) -> Unit) : RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    private var select: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_cell, parent, false)

        return ViewHolder(view, onNoteClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val noteCellViewModel = noteList[position]
        holder.noteTitle.text = noteCellViewModel.title
        holder.checkbox.isVisible = select

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun editMode(){
        select = !select
        this.notifyDataSetChanged()
    }

    fun setNotes(notes: List<NoteCellViewModel>){
        noteList = notes
    }

    class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val checkbox: TextView = itemView.findViewById(R.id.checkbox)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}
