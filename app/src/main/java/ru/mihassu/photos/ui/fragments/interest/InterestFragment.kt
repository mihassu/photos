package ru.mihassu.photos.ui.fragments.interest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.PhotosCallback
import ru.mihassu.photos.ui.fragments.common.PhotosRecyclerView
import ru.mihassu.photos.ui.fragments.common.RecyclerViewEvents
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class InterestFragment : Fragment() {

    companion object {
        var PER_PAGE = 40
        val ONE_DAY_SECONDS = 86400L
    }

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var photosRepository: PhotosRepository
    @Inject
    lateinit var dbInteractor: DataBaseInteractor

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvPhotos: PhotosRecyclerView
    private lateinit var viewModel: InterestViewModel
    private lateinit var animator: MyAnimator
    private val disposables = CompositeDisposable()
    private lateinit var currentDate: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, InterestViewModelFactory(photosRepository, dbInteractor))
                .get(InterestViewModel::class.java)
        val unixDate = System.currentTimeMillis()
        val date = Date(unixDate - ONE_DAY_SECONDS*2000)
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_interest, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animator = MyAnimator(requireContext())
        viewModel.getPhotosLiveData()
                .observe(viewLifecycleOwner, { photosCallback: PhotosCallback ->
                    when (photosCallback) {
                        is PhotosCallback.PhotosLoaded -> showPhotos(photosCallback.photos)
                        is PhotosCallback.PhotosError -> { showToast(photosCallback.th.message.toString()); hideProgress() }
                        is PhotosCallback.PhotosEmpty -> { showToast("No photos"); showPhotos(photosCallback.photos) }
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initViews(view)
    }

    override fun onResume() {
        super.onResume()
//        showProgress()
        viewModel.initLoad(currentDate, PER_PAGE)
    }

    private fun initViews(v: View) {
        progressBar = v.findViewById(R.id.progress_interest)
        swipeRefresh = v.findViewById(R.id.swipe_refresh_interest)
        swipeRefresh.setOnRefreshListener { refresh() }
        val dateField : TextView = v.findViewById(R.id.tv_date)
        dateField.text = currentDate
    }

    private fun initRecyclerView(v: View) {
        rvPhotos = v.findViewById(R.id.rv_photos_list_interest)
        val rvEventsObserver: DisposableObserver<RecyclerViewEvents> = object : DisposableObserver<RecyclerViewEvents>() {
            override fun onNext(event: RecyclerViewEvents) {
                when(event) {
                    is RecyclerViewEvents.LoadMoreEvent -> {
                        showProgress()
                        viewModel.loading(currentDate, PER_PAGE)
                    }
                    is RecyclerViewEvents.ScrollEvent ->
                        when (event.direction) {
//                        RvScrollListener.SCROLL_DOWN -> hideSearch()
//                        RvScrollListener.SCROLL_UP -> showSearch()
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_interest_to_single_photo, bundle)
                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }
    }

    private fun refresh() {
        showProgress()
        viewModel.onRefresh()
        viewModel.initLoad(currentDate, PER_PAGE)
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}