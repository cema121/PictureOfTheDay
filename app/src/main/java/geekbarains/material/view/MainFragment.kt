package geekbarains.material.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.chip.Chip
import geekbarains.material.Constant.MEDIA_TYPE_IMAGE
import geekbarains.material.Constant.WIKI_URL
import geekbarains.material.R
import kotlinx.android.synthetic.main.fragment_main.*
import geekbarains.material.Constant
import geekbarains.material.model.AppState
import geekbarains.material.util.OnSwipeTouchListener
import geekbarains.material.util.toast
import geekbarains.material.viewmodel.MainFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true

        viewModel.getData(null).observe(viewLifecycleOwner) { appState -> renderData(appState) }

        wiki_button.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                motionLayout.transitionToEnd()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                motionLayout.transitionToStart()
            }
        })

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {

                val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
                sdf.timeZone = TimeZone.getTimeZone(Constant.NASA_TIME_ZONE)
                val cal = Calendar.getInstance(TimeZone.getTimeZone(Constant.NASA_TIME_ZONE))

                when (it.text) {
                    resources.getString(R.string.two_yesterday) -> cal.add(Calendar.DAY_OF_YEAR, -2)
                    resources.getString(R.string.yesterday) -> cal.add(Calendar.DAY_OF_YEAR, -1)
                    resources.getString(R.string.today) -> cal.add(Calendar.DAY_OF_YEAR, 0)
                }
                val itemDate: String? = sdf.format(cal.time)

                viewModel.getData(itemDate)
                    .observe(viewLifecycleOwner) { appState -> renderData(appState) }
            }
        }

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(WIKI_URL + input_edit_text.text.toString())
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.SuccessAPOD -> {
                val serverResponseData = data.serverResponseData

                val url: String?

                if (serverResponseData.mediaType != MEDIA_TYPE_IMAGE
                    && serverResponseData.url != null
                ) {
                    webView.visibility = View.VISIBLE
                    webView.loadUrl(serverResponseData.url)
                    image_view.visibility = View.INVISIBLE
                    url = serverResponseData.thumbs
                } else {
                    webView.visibility = View.INVISIBLE
                    image_view.visibility = View.VISIBLE
                    image_view.load(serverResponseData.url)
                    url = serverResponseData.url
                }

                if (url.isNullOrEmpty()) {
                    toast(getString(R.string.emptyLink))
                } else {
                    header.text = serverResponseData.title
                    description.text = serverResponseData.explanation
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(data.error.message)
            }
        }
    }

    companion object;

    private inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return true
        }

        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?,
        ): Boolean {
            return super.onRenderProcessGone(view, detail)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            super.onUnhandledKeyEvent(view, event)
        }
    }
}