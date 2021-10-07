package geekbarains.material.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import geekbarains.material.R
import geekbarains.material.model.note.Note
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.toolbar.*


class NotesFragment : Fragment() {

    companion object;

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: RecyclerActivityAdapter

    private var listOnNotes = arrayListOf<Pair<Note, Boolean>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOnNotes = arrayListOf(
            Pair(
                Note(
                    0,
                    "Mars",
                    "Марс — четвёртая по удалённости от Солнца и седьмая по размерам планета Солнечной системы. Названа в честь Марса — древнеримского бога войны, соответствующего древнегреческому Аресу. Иногда Марс называют «красной планетой» из-за красноватого оттенка поверхности, придаваемого ей минералом маггемитом (оксидом железа).",
                    0,
                    2
                ), false
            ),
            Pair(Note(1, "Earth", "Земля - это Земля", 0, 0), false)
        )

        listOnNotes.add(0, Pair(Note(0, "Header"), false))

        initRV(listOnNotes)

        searchButton.setOnClickListener {
            val searchResult = arrayListOf<Pair<Note, Boolean>>()
            searchResult.add(0, Pair(Note(0, "HeaderSearch"), false))
            for (i in listOnNotes.indices) {
                listOnNotes[i].first.description?.let {
                    if (it.uppercase().contains(searchEditText.text.toString().uppercase())) {
                        searchResult.add(listOnNotes[i])
                    }
                }
            }
            initRV(searchResult)

        }
    }

    private fun initRV(listNotes: ArrayList<Pair<Note, Boolean>>) {
        adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Note) {
                    Toast.makeText(requireContext(), data.someText, Toast.LENGTH_SHORT).show()
                }
            },
            listNotes,
            object : RecyclerActivityAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerActivityFAB.setOnClickListener {
            adapter.appendItem()

        }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }


}