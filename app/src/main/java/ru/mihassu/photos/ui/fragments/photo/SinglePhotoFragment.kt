package ru.mihassu.photos.ui.fragments.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import ru.mihassu.photos.App.Companion.appComponent
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.db.DataBaseInteractor
import javax.inject.Inject

class SinglePhotoFragment : Fragment() {

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var photosRepository: PhotosRepository
    @Inject
    lateinit var dbInteractor: DataBaseInteractor

    private lateinit var photoTitleField: TextView
    private lateinit var imageField: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var addToFavorite: ImageView
    private lateinit var viewModel: SinglePhotoViewModel
    private var photoId: Long? = 0
    private var currentPhoto: Photo? = null
    private lateinit var animator : MyAnimator
//    private lateinit var photoViewAttacher: PhotoViewAttacher
//    private lateinit var callback: Callback
    private val disposables: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        args?.let {
            photoId = it.getLong(Constants.PHOTO_ID_EXTRA)
        }

        val appComponent = appComponent
        appComponent.inject(this)
        viewModel = ViewModelProvider(this, SinglePhotoViewModelFactory(photosRepository, dbInteractor))
                .get(SinglePhotoViewModel::class.java)
        animator = MyAnimator(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_single_photo, container, false)
        initViews(v)
        return v
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPhotoLiveData().observe(this, { photo: Photo ->
            currentPhoto = photo
            picasso
                    .load(photo.getMaxSizeUrl())
                    .into(imageField)
            photoTitleField.text = photo.title
            hideProgress()
            viewModel.checkFavorite(photo)
        })

        viewModel.getIsPhotoFavoriteLiveData().observe(this, { isFavorite: Boolean ->
            if (isFavorite) {
                addToFavorite.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_favorite_24_red))
            } else {
                addToFavorite.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_favorite_24))
            }
        })

        photoId?.let { viewModel.load(it) }
    }

    private fun initViews(v: View) {
        photoTitleField = v.findViewById(R.id.tv_title_field)
        imageField = v.findViewById(R.id.iv_photo_field)
        imageField.setOnClickListener { toggleAddFavorite() }
        progress = v.findViewById(R.id.progress_single_photo)
        addToFavorite = v.findViewById(R.id.iv_add_to_favorite)
        addToFavorite.setOnClickListener { addToFavorite() }
//        photoViewAttacher = PhotoViewAttacher(imageField)
//        photoViewAttacher.setZoomable(true)
//        callback = object : Callback {
//            override fun onSuccess() {
//                photoViewAttacher.update()
//            }
//
//            override fun onError() { }
//        }
    }

    private fun addToFavorite() {
        currentPhoto?.let {
            animator.showTouchAnimation(addToFavorite).subscribe()
            viewModel.addFavoritePhoto(it)
        }
    }

    private fun toggleAddFavorite() {
        if (addToFavorite.visibility == View.GONE) {
            showAddFavoriteButton()
        } else {
            hideAddFavoriteButton()
        }
    }

    private fun showAddFavoriteButton() {
        addToFavorite.visibility = View.VISIBLE
        animator.showShowAnimation(addToFavorite)
                .subscribe().apply { disposables.add(this) }
    }

    private fun hideAddFavoriteButton() {
        animator.showHideAnimation(addToFavorite)
                .subscribe { addToFavorite.visibility = View.GONE }
                .apply { disposables.add(this) }
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show() //привязать к лейауту
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}