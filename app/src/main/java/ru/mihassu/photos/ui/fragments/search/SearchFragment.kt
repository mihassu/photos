package ru.mihassu.photos.ui.fragments.search

import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.photos.App
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.interactor.SearchInteractor
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.*
import ru.mihassu.photos.ui.fragments.photos.PhotosFragment
import ru.mihassu.photos.util.hideKeyboard
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    companion object {
        private var instance: SearchFragment? = null
        var PER_PAGE = 40
        fun getInstance() : Fragment {
            return if (instance == null) {
                instance = SearchFragment()
                instance!!
            } else {
                instance!!
            }
        }
    }


    @Inject
    lateinit var picasso: Picasso
//    @Inject
//    lateinit var searchInteractor: SearchInteractor
    @Inject
    lateinit var dbInteractor: DataBaseInteractor
    @Inject
    lateinit var photosRepository: PhotosRepository

    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var searchLayout: ConstraintLayout
    private lateinit var rvPhotos: PhotosRecyclerView
    private lateinit var viewModel: SearchViewModel
    private lateinit var animator: MyAnimator
    private val disposables = CompositeDisposable()
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, SearchViewModelFactory(photosRepository, dbInteractor))
                .get(SearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animator = MyAnimator(requireContext())
        navController = Navigation.findNavController(requireActivity(), R.id.nav_container_main)
        viewModel.getPhotosLiveData()
                .observe(viewLifecycleOwner) { photosCallback: PhotosCallback ->
                    when (photosCallback) {
                        is PhotosCallback.PhotosLoaded -> showPhotos(photosCallback.photos)
                        is PhotosCallback.PhotosError -> showToast(photosCallback.th.message.toString())
                        is PhotosCallback.PhotosEmpty -> { showToast("No photos"); showPhotos(photosCallback.photos) }
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initViews(view)
    }


    private fun initViews(v: View) {
        searchField = v.findViewById(R.id.et_search_field)
        searchButton = v.findViewById(R.id.search_button)
        searchLayout = v.findViewById(R.id.layout_search)
        progressBar = v.findViewById(R.id.progress_photos)
        // Кнопка поиска
        searchButton.setOnClickListener { startSearch() }
    }

    private fun initRecyclerView(v: View) {
        rvPhotos = v.findViewById(R.id.rv_photos_list)
        val rvEventsObserver: DisposableObserver<RecyclerViewEvents> = object : DisposableObserver<RecyclerViewEvents>() {
            override fun onNext(event: RecyclerViewEvents) {
                when(event) {
                    is RecyclerViewEvents.LoadMoreEvent -> {
                        showProgress()
                        viewModel.loading(PER_PAGE)
                    }
                    is RecyclerViewEvents.ScrollEvent ->
                        when (event.direction) {
                        RvScrollListener.SCROLL_DOWN -> hideSearch()
                        RvScrollListener.SCROLL_UP -> showSearch()
                    }
                    is RecyclerViewEvents.PhotoClickEvent -> openSinglePhoto(event.photo)
                }
            }

            override fun onError(t: Throwable) {
                Logi.logIt("rvEventsObserver - onError")
            }

            override fun onComplete() {}
        }
        disposables.add(rvPhotos.rvEventsProcessor.subscribeWith(rvEventsObserver))
    }

    private fun showPhotos(photosList: List<Photo>) {
        rvPhotos.setDataList(photosList)
        hideProgress()
    }

    private fun openSinglePhoto(photo: Photo) {
        viewModel.addToCache(photo)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val bundle = Bundle()
                    bundle.putLong(Constants.PHOTO_ID_EXTRA, photo.id)
//                    Navigation.findNavController(requireView()).navigate(R.id.action_search_to_single_photo, bundle)
                    navController.navigate(R.id.action_global_singlePhotoFragment, bundle)
                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }

    }

    private fun startSearch() {
        if (searchField.text.toString().isNotEmpty()) {
            viewModel.onRefresh()
            viewModel.initLoad(searchField.text.toString(), PER_PAGE)
            searchField.clearFocus()
            hideKeyboard(requireContext(), searchField.windowToken)
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showSearch() {
        if (searchLayout.visibility == View.INVISIBLE) {
            searchLayout.visibility = View.VISIBLE
            animator.scaleUpAnimation(searchLayout).subscribe()
        }
    }

    private fun hideSearch() {
        if (searchLayout.visibility == View.VISIBLE) {
            animator.scaleDownAnimation(searchLayout).subscribe { searchLayout.visibility = View.INVISIBLE }
                    .apply { disposables.add(this) }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}