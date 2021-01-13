package ru.mihassu.photos.ui.fragments.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    private lateinit var commentsTitle: TextView
    private lateinit var imageField: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var addToFavorite: ImageView
    private lateinit var closeBottomSheetButton: ImageButton
    private lateinit var viewModel: SinglePhotoViewModel
    private var photoId: Long? = 0
    private var currentPhoto: Photo? = null
    private lateinit var animator : MyAnimator
    private lateinit var commentsRvAdapter: CommentsRvAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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
        initRecyclerView(v)
        initBottomSheet(v)
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
        imageField.setOnClickListener { toggleVisibilityAddFavorite() }
        progress = v.findViewById(R.id.progress_single_photo)
        addToFavorite = v.findViewById(R.id.iv_add_to_favorite)
        addToFavorite.setOnClickListener { addToFavorite() }
        val buttonComments = v.findViewById<ImageButton>(R.id.button_comments)
        buttonComments.setOnClickListener { showBottomSheet() }
        commentsTitle = v.findViewById(R.id.tv_comments_title)
        closeBottomSheetButton = v.findViewById(R.id.button_close_bottom_sheet)
        closeBottomSheetButton.setOnClickListener { hideBottomSheet() }
//        photoViewAttacher = PhotoViewAttacher(imageField)
//        photoViewAttacher.setZoomable(true)
//        callback = object : Callback {
//            override fun onSuccess() {
//                photoViewAttacher.update()
//            }
//            override fun onError() { }
//        }
    }

    private fun initRecyclerView(v: View) {
        val rvComments = v.findViewById<RecyclerView>(R.id.rv_comments)
        rvComments.run {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            commentsRvAdapter = CommentsRvAdapter()
            adapter = commentsRvAdapter
            val divider = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.item_divider, null))
            }
            addItemDecoration(divider)
        }
    }

    private fun initBottomSheet(v: View) {
        val bottomSheetComments = v.findViewById<ConstraintLayout>(R.id.bottom_sheet_comments)
        BottomSheetBehavior.from(bottomSheetComments).apply {
            bottomSheetBehavior = this
            isFitToContents = false
            setExpandedOffset(800)
            isHideable = false
//            isDraggable = false
            peekHeight = 0
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when(newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            currentPhoto?.let {
                                if (it.comments.isNotEmpty()) {
                                    commentsTitle.text = getString(R.string.comments)
                                    commentsRvAdapter.commentsList = it.comments
                                    commentsRvAdapter.notifyDataSetChanged()
                                } else {
                                    commentsTitle.text = getString(R.string.no_comments)
                                }
                            }
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> { }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }
    }

    private fun addToFavorite() {
        currentPhoto?.let {
            animator.showTouchAnimation(addToFavorite).subscribe()
            viewModel.addFavoritePhoto(it)
        }
    }

    private fun toggleVisibilityAddFavorite() {
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

    private fun showBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show() //привязать к лейауту
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}