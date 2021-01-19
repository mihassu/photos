package ru.mihassu.photos.ui.fragments.photos

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
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
import ru.mihassu.photos.ui.fragments.common.PhotosCallback
import ru.mihassu.photos.ui.fragments.common.PhotosRecyclerView
import ru.mihassu.photos.ui.fragments.common.RecyclerViewEvents
import ru.mihassu.photos.ui.fragments.common.RvScrollListener
import javax.inject.Inject

class PhotosFragment : Fragment() //extends MvpAppCompatFragment implements IPhotoFragment
{

    companion object {
        var PER_PAGE = 40
    }

    @Inject
    lateinit var picasso: Picasso
//    @Inject
//    lateinit var searchInteractor: SearchInteractor
    @Inject
    lateinit var dbInteractor: DataBaseInteractor
    @Inject
    lateinit var photosRepository: PhotosRepository

//    private lateinit var searchField: EditText
//    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
//    private lateinit var searchLayout: ConstraintLayout
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvPhotos: PhotosRecyclerView
    private lateinit var snackbar: Snackbar
    private lateinit var photosFragmentContainer: ConstraintLayout
    //    @InjectPresenter
    //    PhotosPresenter mainPresenter;
//    private lateinit var navController: NavController
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
        animator = MyAnimator(requireContext())
        viewModel.getPhotosLiveData()
                .observe(viewLifecycleOwner, { photosCallback: PhotosCallback ->
                    when (photosCallback) {
                        is PhotosCallback.PhotosLoaded -> showPhotos(photosCallback.photos)
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
        initSnackbar(view,"Конец", "Вверх")
    }

    override fun onResume() {
        super.onResume()
        viewModel.initLoad(PER_PAGE)
//        Logi.logIt("PhotosFragment - onResume()");
//        mainPresenter.onFragmentResume(getActivity());
    }

    private fun initViews(v: View) {
//        searchField = v.findViewById(R.id.et_search_field)
//        searchButton = v.findViewById(R.id.search_button)
//        searchLayout = v.findViewById(R.id.layout_search)
        progressBar = v.findViewById(R.id.progress_photos)
        // Кнопка поиска
//        searchButton.setOnClickListener { startSearch() }
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_photos_to_single_photo, bundle)
                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }
    }

//    private fun startSearch() {
//        if (searchField.text.toString().isNotEmpty()) {
//            viewModel.clearDataList()
//            viewModel.initLoad(searchField.text.toString(), PER_PAGE)
//        }
//    }

    private fun refresh() {
        showProgress()
        viewModel.onRefresh()
        viewModel.initLoad(PER_PAGE)
//        searchField.text.clear()
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

//    private fun showSearch() {
//        if (searchLayout.visibility == View.INVISIBLE) {
//            searchLayout.visibility = View.VISIBLE
//            animator.scaleUpAnimation(searchLayout).subscribe()
//        }
//    }
//
//    private fun hideSearch() {
//        if (searchLayout.visibility == View.VISIBLE) {
//            animator.scaleDownAnimation(searchLayout).subscribe { searchLayout.visibility = View.INVISIBLE }
//                    .apply { disposables.add(this) }
//        }
//    }

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


    //    @Override
    //    public void showPhotos(List<Photo> photos) {
    //        adapter.setDataList(photos);
    //        adapter.notifyDataSetChanged();
    //    }
    //
    //    @Override
    //    public void showPhotosPaging(PagedList<Photo> photosPagedList) {
    //        Logi.logIt("PhotosFragment - showPhotosPaging(), photosPagedList.size(): " + photosPagedList.size());
    //        Logi.logIt("PhotosFragment - showPhotosPaging() " + photosPagedList.get(0).getUrl());
    //        adapter.submitList(photosPagedList);
    //    }
    //
    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }


}