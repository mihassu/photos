package ru.mihassu.photos.ui.fragments.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
//import android.support.v4.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_photos.*
import ru.mihassu.photos.App
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.base.BaseFragment
import ru.mihassu.photos.ui.fragments.common.*
import javax.inject.Inject

class PhotosFragment : BaseFragment() //extends MvpAppCompatFragment implements IPhotoFragment
{

    companion object {
        private var instance: PhotosFragment? = null
        var PER_PAGE = 80
        fun getInstance() : Fragment {
            return if (instance == null) {
                instance = PhotosFragment()
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

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvPhotos: PhotosRecyclerView
    private lateinit var snackbar: Snackbar
    private lateinit var photosFragmentContainer: ConstraintLayout
    //    @InjectPresenter
    //    PhotosPresenter mainPresenter;
    private lateinit var navController: NavController
    private lateinit var viewModel: PhotosViewModel
    private lateinit var animator: MyAnimator
    private val disposables = CompositeDisposable()


    //    public PhotosFragment() {
    //    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, PhotosViewModelFactory(photosRepository, dbInteractor))
                .get(PhotosViewModel::class.java)
        //        Logi.logIt("PhotosFragment - onCreate()");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photos, container, false)
        //        Logi.logIt("PhotosFragment - onCreateView()");
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container_main)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_container_main)

        animator = MyAnimator(requireContext())
        viewModel.getPhotosLiveData()
                .observe(viewLifecycleOwner, { photosCallback: PhotosCallback ->
                    when (photosCallback) {
                        is PhotosCallback.PhotosLoaded -> {
                            if (photosCallback.photos.isNotEmpty()) {
                                showPhotos(photosCallback.photos)
                            } else hideProgress()
                        }
                        is PhotosCallback.PhotosError -> { showToast(photosCallback.th.message.toString()); hideProgress() }
                        is PhotosCallback.PhotosEmpty -> { showSnackBar(); showPhotos(photosCallback.photos) }
                    }
                })
        //Logi.logIt("PhotosFragment - onActivityCreated()");
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initViews(view)
//        initSnackbar(view,"Конец", "Вверх")
    }

    override fun onResume() {
        super.onResume()
        showProgress()
        viewModel.initLoad(PER_PAGE)
//        Logi.logIt("PhotosFragment - onResume()");
//        mainPresenter.onFragmentResume(getActivity());
    }

    private fun initViews(v: View) {
        progressBar = v.findViewById(R.id.progress_photos)
        swipeRefresh = v.findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener { refresh() }
        photosFragmentContainer = v.findViewById(R.id.fragment_photos_container)
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
//                    Navigation.findNavController(requireView()).navigate(R.id.action_photos_to_single_photo, bundle)
//                    navController.navigate(R.id.action_mainFragment_to_singlePhotoFragment, bundle)
                    navController.navigate(R.id.action_global_singlePhotoFragment, bundle)

                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }
    }

    private fun refresh() {
        showProgress()
        viewModel.onRefresh()
        viewModel.initLoad(PER_PAGE)
    }

    private fun showProgress() {
        if (progressBar.visibility != View.VISIBLE) {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun initSnackbar(v: View, text: String, actionText: String) {
        snackbar = Snackbar.make(v.findViewById(R.id.fragment_photos_container), text, Snackbar.LENGTH_SHORT)
                .setAnchorView(v.findViewById(R.id.snackbar_anchor_view))
//                .apply {
//                    val params = view.layoutParams as CoordinatorLayout.LayoutParams
//                }
                .setAction(actionText) { rvPhotos.scrollToPosition(0) }
    }

    private fun showSnackBar() {
        snackbar.show()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }


}