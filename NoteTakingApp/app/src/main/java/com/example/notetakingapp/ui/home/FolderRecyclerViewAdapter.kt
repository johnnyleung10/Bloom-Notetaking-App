import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.models.FolderViewModel
import com.example.notetakingapp.R

class FolderRecyclerViewAdapter(private val mList: List<FolderViewModel>) : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_cell, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val folderViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.folderTitle.text = folderViewModel.title

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val folderTitle: TextView = itemView.findViewById(R.id.folderTitle)
    }
}
