import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.models.FolderCellViewModel
import com.example.notetakingapp.R
import com.example.notetakingapp.models.NoteCellViewModel

class FoldersRecyclerViewAdapter(private var folderCellList: List<FolderCellViewModel>, private val onFolderClicked: (position: Int) -> Unit) : RecyclerView.Adapter<FoldersRecyclerViewAdapter.ViewHolder>() {

    private var select: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_cell, parent, false)

        return ViewHolder(view, onFolderClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val folderCellViewModel = folderCellList[position]
        holder.folderTitle.text = folderCellViewModel.title
        holder.checkbox.isVisible = select
        holder.folderCount.isVisible = !select

    }

    override fun getItemCount(): Int {
        return folderCellList.size
    }

    fun editMode(){
        select = !select
        this.notifyDataSetChanged()
    }

    fun setFolders(folderCells: List<FolderCellViewModel>){
        folderCellList = folderCells
    }

    class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val folderTitle: TextView = itemView.findViewById(R.id.folderTitle)
        val folderCount: TextView = itemView.findViewById(R.id.folderCount)
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
