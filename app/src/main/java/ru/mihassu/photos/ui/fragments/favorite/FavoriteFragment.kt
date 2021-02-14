package ru.mihassu.photos.ui.fragments.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.mihassu.photos.App
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.BaseFragment
import javax.inject.Inject

class FavoriteFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var dbInteractor: DataBaseInteractor

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var rvAdapter : FavoriteRvAdapter
    private val disposables = CompositeDisposable()
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, FavoriteViewModelFactory(dbInteractor)).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favorite, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container_main)
        viewModel.getPhotosLiveData().observe(viewLifecycleOwner, { photos: List<Photo> ->
            rvAdapter.setDataList(photos)
        } )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun initRecyclerView(v: View) {
        val favoritesRv = v.findViewById<RecyclerView>(R.id.rv_favorites)
        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAdapter = FavoriteRvAdapter(picasso)
        rvAdapter.setAdapterEventListener(object : FavoriteRvAdapter.AdapterEventListener{
            override fun onEvent(event: FavoriteAdaptedEvent) {
                when(event) {
                    is FavoriteAdaptedEvent.Click -> openSinglePhoto(event.photo)
                    is FavoriteAdaptedEvent.Swipe -> viewModel.toggleFavorite(event.photo) // через dialog
                }
            }
        })

        val favoriteTouchCallback = FavoriteTouchCallback(rvAdapter)
        val itemTouchHelper = ItemTouchHelper(favoriteTouchCallback)
        itemTouchHelper.attachToRecyclerView(favoritesRv)

        val rvWidth = favoritesRv.layoutParams.width
        val rvWidth1 = favoritesRv.width
        val rvWidth2 = favoritesRv.measuredWidth
        rvAdapter.setRvWidth(rvWidth)

        favoritesRv.layoutManager = lm
        favoritesRv.adapter = rvAdapter
    }

    private fun openSinglePhoto(photo: Photo) {
        viewModel.addToCache(photo)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val bundle = Bundle()
                    bundle.putLong(Constants.PHOTO_ID_EXTRA, photo.id)
//                    Navigation.findNavController(requireView()).navigate(R.id.action_favorites_to_single_photo, bundle)
                    navController.navigate(R.id.action_mainFragment_to_singlePhotoFragment, bundle)

                }, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
                .apply { disposables.add(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
 }