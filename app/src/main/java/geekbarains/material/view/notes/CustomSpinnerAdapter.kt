package geekbarains.material.view.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import geekbarains.material.R

internal class CustomSpinnerAdapter(
    context: Context?,
    private val dataRecieved: List<MutableMap<String, *>?>,
    resource: Int,
    from: Array<String?>?,
    to: IntArray?
) :
    SimpleAdapter(context, dataRecieved, resource, from, to) {
    var mInflater: LayoutInflater
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mInflater.inflate(
                R.layout.spinner_view,
                null
            )
        }
        (convertView.findViewById<View>(R.id.imageNameSpinner) as TextView).text =
            dataRecieved[position]!!["Name"] as String?
        (convertView.findViewById<View>(R.id.imageIconSpinner) as ImageView)
            .setBackgroundResource(dataRecieved[position]!!["Icon"] as Int)
        return convertView
    }

    init {
        mInflater = LayoutInflater.from(context)
    }
}