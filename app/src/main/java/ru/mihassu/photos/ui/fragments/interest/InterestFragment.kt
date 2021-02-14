package ru.mihassu.photos.ui.fragments.interest

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_interest.*
import ru.mihassu.photos.App
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class InterestFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var photosRepository: PhotosRepository
    @Inject
    lateinit var dbInteractor: DataBaseInteractor

    private lateinit var progressBar: ProgressBar
    private lateinit var rvPhotos: PhotosRecyclerView
    private lateinit var viewModel: InterestViewModel
    private lateinit var animator: MyAnimator
    private val disposables = CompositeDisposable()
//    private lateinit var currentDate: String
//    private var currentDatePos: Int = 0
    private lateinit var navController: NavController
    private lateinit var datesScroll: DatesScrollConstraint


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, InterestViewModelFactory(photosRepository, dbInteractor))
                .get(InterestViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_interest, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animator = MyAnimator(requireContext())
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container_main)

        viewModel.getPhotosLiveData()
                .observe(viewLifecycleOwner, { photosCallback: PhotosCallback ->
                    when (photosCallback) {
                        is PhotosCallback.PhotosLoaded -> {
                            if (photosCallback.photos.isNotEmpty()) {
                                showPhotos(photosCallback.photos)
                            } else hideProgress()
                        }
                        is PhotosCallback.PhotosError -> { showToast(photosCallback.th.message.toString()); hideProgress() }
                        is PhotosCallback.PhotosEmpty -> { showToast("No photos"); showPhotos(photosCallback.photos) }
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initViews(view)
//        initSpinner()
        initDatesChooser(view)
    }

    override fun onResume() {
        super.onResume()
        showProgress()
        viewModel.initLoad(0)
    }

    private fun initViews(v: View) {
        progressBar = v.findViewById(R.id.progress_interest)
    }

    private fun initRecyclerView(v: View) {
        rvPhotos = v.findViewById(R.id.rv_photos_list_interest)
        val rvEventsObserver: DisposableObserver<RecyclerViewEvents> = object : DisposableObserver<RecyclerViewEvents>() {
            override fun onNext(event: RecyclerViewEvents) {
                when(event) {
                    is RecyclerViewEvents.LoadMoreEvent -> {
                        showProgress()
                        viewModel.loading()
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

    private fun initDatesChooser(v: View) {
        datesScroll = v.findViewById(R.id.dates_scroll)
        viewModel.getDatesLiveData().observe(viewLifecycleOwner) { datesListState ->
            datesScroll.addViewForDates(datesListState.getDatesList(), datesListState.getCurrentDatePos())
        }
        datesScroll.setOnDateClick { datePos ->
            showProgress()
            viewModel.onRefresh()
            viewModel.initLoad(datePos)
        }

        val dateButton = v.findViewById<TextView>(R.id.tv_date)
        dateButton.setOnClickListener { datesScroll.showAllItems() }
    }



//    private fun initSpinner() {
//
//        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, datesList)
//                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
//
//        spinner_dates.apply {
//            adapter = spinnerAdapter
//            visibility = View.VISIBLE
//            prompt = "Дата"
//            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                }
//
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    currentDate = datesList[position]
//                    viewModel.onRefresh()
//                    viewModel.initLoad(currentDate, PER_PAGE)
//                    showProgress()
//                }
//            }
//        }
//    }

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
//                    Navigation.findNavController(requireView()).navigate(R.id.action_interest_to_single_photo, bundle)
                    navController.navigate(R.id.action_mainFragment_to_singlePhotoFragment, bundle)
                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }
    }


    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }


}