package geekbarains.material.view.notes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import geekbarains.material.model.note.Note
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_note.view.*
import geekbarains.material.R
import geekbarains.material.model.note.ItemTouchHelperAdapter
import geekbarains.material.model.note.ItemTouchHelperViewHolder

class RecyclerActivityAdapter(
    private val onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Note, Boolean>>,
    private val dragListener: OnStartDragListener,
) :
    RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    private val imageNameDatabase = arrayOf(
        "Earth",
        "Jupiter",
        "Mars",
        "Mercury",
        "Moon",
        "Neptune",
        "Pluto",
        "Saturn",
        "Uranus",
        "Venus"
    )
    private val imageIconDatabase = arrayOf(
        R.drawable.planets_earth,
        R.drawable.planets_jupiter,
        R.drawable.planets_mars,
        R.drawable.planets_mercury,
        R.drawable.planets_moon,
        R.drawable.planets_neptune,
        R.drawable.planets_pluto,
        R.drawable.planets_saturn,
        R.drawable.planets_uranus,
        R.drawable.planets_venus
    )
    private lateinit var parentLoc: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        parentLoc = parent
        return when (viewType) {
            TYPE_PLANET ->
                NoteViewHolder(
                    inflater.inflate(R.layout.item_note, parent, false) as View
                )
            else -> HeaderViewHolder(
                inflater.inflate(R.layout.item_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<Note, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.someText != oldData.first.someText) {
                holder.itemView.noteTextView.text = newData.first.someText
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_PLANET
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItems(newItems: List<Pair<Note, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(Note(1, "Плутон", "Описание заметки", 4, 5), false)

    inner class DiffUtilCallback(
        private var oldItems: List<Pair<Note, Boolean>>,
        private var newItems: List<Pair<Note, Boolean>>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.someText == newItems[newItemPosition].first.someText

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                oldItem,
                newItem
            )
        }
    }

    inner class NoteViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {

        private var editMode = false
        private var idNote: Int = -1

        override fun bind(dataItem: Pair<Note, Boolean>) {
            idNote = dataItem.first.id
            itemView.noteImageView.setImageResource(idToPlanetRes(dataItem.first.planet))
            itemView.noteImageView.setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            itemView.editItemImageView.setOnClickListener { editItem(dataItem.first) }
            editMode = false
            itemView.editItemImageView.setImageResource(R.drawable.ic_edit_24)
            itemView.removeItemImageView.setOnClickListener { removeItem() }
            itemView.moveItemDown.setOnClickListener { moveDown() }
            itemView.moveItemUp.setOnClickListener { moveUp() }
            itemView.noteTextView.text = idToNamePlanet(dataItem.first.planet)
            itemView.noteDescriptionTextView.text = dataItem.first.description
            itemView.noteDescriptionTextView.visibility =
                if (dataItem.second) View.VISIBLE else View.GONE
            itemView.noteTextView.setOnClickListener { toggleText() }
            itemView.dragHandleImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }

        private fun idToPlanetRes(id: Int): Int {
            return when (id) {
                0 -> R.drawable.planets_earth
                1 -> R.drawable.planets_jupiter
                2 -> R.drawable.planets_mars
                3 -> R.drawable.planets_mercury
                4 -> R.drawable.planets_neptune
                5 -> R.drawable.planets_pluto
                6 -> R.drawable.planets_saturn
                7 -> R.drawable.planets_uranus
                8 -> R.drawable.planets_venus
                else -> R.drawable.planets_moon
            }
        }

        private fun idToNamePlanet(id: Int): String {
            return when (id) {
                0 -> "Земля"
                1 -> "Юпитер"
                2 -> "Марс"
                3 -> "Меркурий"
                4 -> "Нептун"
                5 -> "Плутон"
                6 -> "Сатурн"
                7 -> "Уран"
                8 -> "Венера"
                else -> "Луна"
            }
        }

        private fun editItem(itemNote: Note) {

            editMode = !editMode

            if (editMode) {

                itemView.editItemImageView.setImageResource(R.drawable.ic_save_24)

                itemView.noteDescriptionTextView.visibility = View.INVISIBLE
                itemView.noteDescriptionEditView.visibility = View.VISIBLE
                itemView.spinner.visibility = View.VISIBLE

                itemView.noteDescriptionEditView.setText(itemView.noteDescriptionTextView.text.toString())


                itemView.noteImageView.visibility = View.INVISIBLE
                itemView.spinner.visibility = View.VISIBLE


                val spinnerList = mutableListOf<HashMap<String, Any>>()
                for (i in imageNameDatabase.indices) {
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["Name"] = imageNameDatabase[i]
                    hashMap["Icon"] = imageIconDatabase[i]
                    spinnerList.add(hashMap)
                }

                val adapter = CustomSpinnerAdapter(
                    parentLoc.context,
                    spinnerList,
                    R.layout.spinner_view,
                    arrayOf("Name", "Icon"),
                    intArrayOf(R.id.imageNameSpinner, R.id.imageIconSpinner)
                )

                itemView.spinner.adapter = adapter
                itemView.spinner.setSelection(itemNote.planet)


            } else {
                val itemIndex = getIndexNoteByNoteId(idNote)
                data[itemIndex].first.description = itemView.noteDescriptionEditView.text.toString()
                data[itemIndex].first.planet = itemView.spinner.selectedItemPosition - 1
                itemView.noteDescriptionTextView.text = itemView.noteDescriptionEditView.text

                itemView.noteImageView.visibility = View.VISIBLE
                itemView.editItemImageView.setImageResource(R.drawable.ic_edit_24)

                itemView.spinner.visibility = View.GONE

                itemView.noteDescriptionTextView.visibility = View.VISIBLE
                itemView.noteDescriptionEditView.visibility = View.INVISIBLE

                itemView.noteImageView.setImageResource(idToPlanetRes(itemNote.planet))

                notifyItemChanged(itemIndex)

            }
        }

        private fun getIndexNoteByNoteId(noteId: Int): Int {
            var result: Int = -1
            for (i in data.indices) {
                if (data[i].first.id == noteId) {
                    result = i
                }
            }
            return result
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem: Pair<Note, Boolean>) {
            if (dataItem.first.someText != "Header") {
                itemView.header.text = "Результаты поиска:"
            } else {
                itemView.header.text = "Список заметок:"
            }

            itemView.setOnClickListener {
                onListItemClickListener.onItemClick(dataItem.first)
            }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Note)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    companion object {
        private const val TYPE_HEADER = 2
        private const val TYPE_PLANET = 3
    }
}