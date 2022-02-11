import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.models.FolderViewModel
import com.example.notetakingapp.R

class FolderRecyclerViewAdapter(private val folderList: List<FolderViewModel>, private val onFolderClicked: (position: Int) -> Unit) : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_cell, parent, false)

        return ViewHolder(view, onFolderClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val folderViewModel = folderList[position]
        holder.folderTitle.text = folderViewModel.title

    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val folderTitle: TextView = itemView.findViewById(R.id.folderTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}
