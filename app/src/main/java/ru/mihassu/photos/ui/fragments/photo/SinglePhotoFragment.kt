package ru.mihassu.photos.ui.fragments.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_single_photo.*
import ru.mihassu.photos.App.Companion.appComponent
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Constants
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.animation.MyAnimator
import ru.mihassu.photos.ui.custom.FitWidthTransformation
import javax.inject.Inject

class SinglePhotoFragment : Fragment() {

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var photosRepository: PhotosRepository
//    @Inject
//    lateinit var dbInteractor: DataBaseInteractor

    private lateinit var photoTitleField: TextView
    private lateinit var commentsTitle: TextView
    private lateinit var imageField: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var addToFavorite: ImageView
    private lateinit var isFavoriteIcon: ImageView
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
        viewModel = ViewModelProvider(this, SinglePhotoViewModelFactory(photosRepository))
                .get(SinglePhotoViewModel::class.java)
        animator = MyAnimator(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_single_photo, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initRecyclerView(view)
        initBottomSheet(view)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPhotoLiveData().observe(this, { photoCallback: SinglePhotoCallback ->
            when(photoCallback) {
                is SinglePhotoCallback.PhotoLoaded -> {
                    currentPhoto = photoCallback.photo
                    showPhoto(photoCallback.photo)
                    hideProgress()
                    viewModel.checkFavorite(photoCallback.photo)
                }
                is SinglePhotoCallback.PhotoError -> {
                    showToast(photoCallback.error.message.toString())
                    hideProgress()
                }
            }
        })

        viewModel.getIsPhotoFavoriteLiveData().observe(this, { isFavorite: Boolean ->
            if (isFavorite) {
                addToFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24_red))
                isFavoriteIcon.visibility = View.VISIBLE
                hideAddFavoriteButton()

            } else {
                addToFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24))
                isFavoriteIcon.visibility = View.GONE
                hideAddFavoriteButton()
            }
        })

        photoId?.let { showProgress(); viewModel.load(it) }
    }

    private fun initViews(v: View) {
        photoTitleField = v.findViewById(R.id.tv_title_field)
        imageField = v.findViewById(R.id.iv_photo_field)
        imageField.setOnClickListener { toggleVisibilityAddFavorite() }
        progress = v.findViewById(R.id.progress_single_photo)
        addToFavorite = v.findViewById(R.id.iv_add_to_favorite)
        addToFavorite.setOnClickListener { addToFavorite() }
        isFavoriteIcon = v.findViewById(R.id.iv_is_favorite)
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
        icon_date.visibility = View.INVISIBLE
        icon_views.visibility = View.INVISIBLE
    }

    private fun initRecyclerView(v: View) {
        val rvComments = v.findViewById<RecyclerView>(R.id.rv_comments)
        rvComments.run {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            commentsRvAdapter = CommentsRvAdapter()
            adapter = commentsRvAdapter
            val divider = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
                ContextCompat.getDrawable(requireContext(), R.drawable.item_divider)?.let { setDrawable(it) }
            }
            addItemDecoration(divider)
        }
    }

    private fun initBottomSheet(v: View) {
        val bottomSheetComments = v.findViewById<ConstraintLayout>(R.id.bottom_sheet_comments)
        BottomSheetBehavior.from(bottomSheetComments).apply {
            bottomSheetBehavior = this
            isFitToContents = false
            expandedOffset = 800
            isHideable = false
//            isDraggable = false
            peekHeight = 0
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when(newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            currentPhoto?.let {
                                if (it.comments.isNotEmpty()) {
                                    commentsTitle.text = buildString {
                                        append(getString(R.string.comments))
                                        append("(")
                                        append(it.comments.size)
                                        append(")")
                                    }
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

    private fun showPhoto(photo: Photo) {
        picasso
                .load(photo.getMaxSizeUrl())
                .error(R.drawable.placeholder_error)
                .transform(FitWidthTransformation(requireContext().resources.displayMetrics.widthPixels))
//                    .resize(requireContext().resources.displayMetrics.widthPixels, 1000)
//                    .centerInside()
                .into(imageField, object : Callback {
                    override fun onSuccess() {
                        showPhotoDetails(photo)
                    }

                    override fun onError() {

                    }
                })

        photoTitleField.text = photo.title
    }

    private fun showPhotoDetails(photo: Photo) {
        icon_date.visibility = View.VISIBLE
        icon_views.visibility = View.VISIBLE

        photo.dates?.let {
    //tv_date_field.text = "${getString(R.string.photo_date)}: ${it.taken}"
            tv_date_field.text = it.taken
        }
        photo.views?.let {
    //tv_views_field.text = "${getString(R.string.photo_views)}: $it"
            tv_views_field.text = it
        }
        photo.owner?.let {
            val ownerString = buildString {
                append(getString(R.string.photo_owner))
                append(": ")
                append(it.username)
            }
            tv_owner_field.text = ownerString
        }
        photo.tags?.let { tags ->
            if (tags.isNotEmpty()) {
                val tagsString = buildString {
                    append(getString(R.string.photo_tags))
                    append(": ")
                    tags.forEach { tag ->
                        tag.content?.let { append("#"); append(it); append(" ") }
                    }
                }
                tv_tags_field.text = tagsString.trimEnd(' ')
            }
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