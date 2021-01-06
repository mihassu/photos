package ru.mihassu.photos.ui.fragments.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mihassu.photos.App
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.db.DataBaseInteractor
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var dbInteractor: DataBaseInteractor

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter : FavoriteRvAdapter
//    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = App.appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, FavoriteViewModelFactory(dbInteractor)).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favorite, container, false)
        initRecyclerView(v)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.getPhotosLiveData().observe(viewLifecycleOwner, { photos: List<Photo> ->
            adapter.setDataList(photos)
        } )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun initRecyclerView(v: View) {
        val rv = v.findViewById<RecyclerView>(R.id.rv_favorites)
        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = FavoriteRvAdapter(picasso)
        adapter.setOnPhotoClickListener(object : FavoriteRvAdapter.OnPhotoClickListener{
            override fun onPhotoClick(photo: Photo) {
                openSinglePhoto(photo)
            }
        })
        val rvWidth = rv.layoutParams.width
        val rvWidth1 = rv.width
        val rvWidth2 = rv.measuredWidth

        adapter.setRvWidth(rvWidth)
        rv.layoutManager = lm
        rv.adapter = adapter
    }

    private fun openSinglePhoto(photo: Photo) {
        val bundle = Bundle()
        bundle.putLong(Constants.PHOTO_ID_EXTRA, photo.id)
        Navigation.findNavController(requireView()).navigate(R.id.action_favorites_to_single_photo, bundle)
//        navController.navigate(R.id.action_to_single_photo_fragment, bundle)
    }

 }