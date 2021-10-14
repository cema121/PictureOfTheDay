package geekbarains.material.view.notes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import geekbarains.material.model.note.Note

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem: Pair<Note, Boolean>)
}