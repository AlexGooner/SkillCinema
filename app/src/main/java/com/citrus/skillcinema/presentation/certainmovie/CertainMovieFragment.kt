package com.citrus.skillcinema.presentation.certainmovie

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.database.AppDataBase
import com.citrus.skillcinema.data.database.DataBaseRepository
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.databinding.FragmentCertainMovieBinding
import com.citrus.skillcinema.presentation.adapters.certainmovie.ActorListAdapter
import com.citrus.skillcinema.presentation.adapters.certainmovie.GalleryListAdapter
import com.citrus.skillcinema.presentation.adapters.certainmovie.SimilarMovieListAdapter
import com.citrus.skillcinema.presentation.adapters.certainmovie.StuffListAdapter
import com.citrus.skillcinema.presentation.adapters.profilepage.CheckBoxCollectionAdapter
import com.citrus.skillcinema.presentation.customview.MovieItemHorizontal
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertainMovieFragment : Fragment() {

    private var _binding: FragmentCertainMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CertainMovieViewModel by viewModels()
    private var isExpanded = false
    private lateinit var navController: NavController
    private lateinit var actorListAdapter: ActorListAdapter
    private lateinit var stuffListAdapter: StuffListAdapter
    private lateinit var galleryListAdapter: GalleryListAdapter
    private lateinit var similarListAdapter: SimilarMovieListAdapter
    private lateinit var dbRepository: DataBaseRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertainMovieBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CertainMovieFragmentArgs by navArgs()
        val movieId = args.movieId

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        navController = findNavController()

        actorListAdapter = ActorListAdapter(navController)
        stuffListAdapter = StuffListAdapter(navController)
        galleryListAdapter = GalleryListAdapter(navController)
        similarListAdapter = SimilarMovieListAdapter(navController)

        binding.progressBarCertain.visibility = View.VISIBLE

        binding.actorNewRecycler.adapter = actorListAdapter
        binding.stuffRecyclerView.adapter = stuffListAdapter
        binding.galleryRecyclerView.adapter = galleryListAdapter
        binding.similarRecycler.adapter = similarListAdapter
        val db = AppDataBase.Companion.getDatabase(requireContext())
        dbRepository = DataBaseRepository(db.savedFilmDao())


        viewModel.loadMovieCertain(movieId)
        viewModel.loadImages(movieId, "STILL", 1)
        viewModel.loadSeasonsInfo(movieId)



        binding.certainMovieBackButton.setOnClickListener {
            navController.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarCertain.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.actorNewRecycler.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.stuffRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.galleryRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.similarRecycler.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isWatched.collect { isWatched ->
                if (isWatched) {
                    binding.eyeButton.setImageResource(R.drawable.ic_eye)
                    viewModel.onIconButtonClick(viewModel.collectionsName.watched)

                } else {
                    binding.eyeButton.setImageResource(R.drawable.ic_slash_eye)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFilmLiked.collect { isLiked ->
                if (isLiked) {
                    binding.favouriteBtn.setImageResource(R.drawable.ic_heart)
                } else {
                    binding.favouriteBtn.setImageResource(R.drawable.ic_slashed_heart)
                }
            }
        }




        binding.eyeButton.setOnClickListener {
            val isWatched = !viewModel.isWatched.value
            viewModel.toggleWatchedStatus(movieId, isWatched)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFilmBookmark.collect { isBookmark ->
                if (isBookmark) {
                    binding.bookmarkBtn.setImageResource(R.drawable.ic_selected_bookmark)
                } else {
                    binding.bookmarkBtn.setImageResource(R.drawable.ic_bookmark)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stuffList.collect { stuffList ->
                val filteredActorList = stuffList.filter { it.professionKey == "ACTOR" }
                val limitedActorList = filteredActorList.take(20)
                actorListAdapter.submitList(limitedActorList)
                binding.actorsCountTextView.text = ("${filteredActorList.size}")
                binding.actorsCountTextView.setOnClickListener {
                    val action =
                        CertainMovieFragmentDirections.actionNavigationCertainToActorFullList(
                            movieId,
                            "ACTOR"
                        )
                    navController.navigate(action)
                }
                binding.actorsListTextView.text = "В фильме снимались"


                binding.filmStuffTextView.text = "Над фильмом работали"
                val stuffListFilter = stuffList.size - filteredActorList.size
                binding.stuffCountTextView.text = ("$stuffListFilter")
                val filteredStuffList = stuffList.filterNot { it.professionKey == "ACTOR" }
                val limitedStuffList = filteredStuffList.take(6)
                val mergedList = stuffListAdapter.mergeStuffList(limitedStuffList)
                binding.stuffCountTextView.setOnClickListener {
                    val action =
                        CertainMovieFragmentDirections.actionNavigationCertainToActorFullList(
                            movieId,
                            "NOTACTOR"
                        )
                    navController.navigate(action)
                }
                stuffListAdapter.submitList(mergedList)

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imagesList.collect { imageList ->
                binding.galleryTextView.text = "Галерея"
                binding.galleryAllTextView.text = "Все"
                val limitedGalleryList = imageList.take(20)
                galleryListAdapter.submitList(limitedGalleryList)
                binding.galleryAllTextView.setOnClickListener {
                    val action =
                        CertainMovieFragmentDirections.actionNavigationCertainToFullGallery(movieId)
                    navController.navigate(action)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.similarList.collect { similarList ->
                binding.similarMoviesTextView.text = "Похожие фильмы"
                binding.similarCountTextView.text = similarList.size.toString()
                val limitedSimilarList = similarList.take(20)
                similarListAdapter.submitList(limitedSimilarList)
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieCertain.collect { movie ->
                movie?.let {
                    updateMovieDetails(it)
                    Log.d("FRAGMENT", "collect is ready")
                }
            }
        }

        similarListAdapter.onItemClick = { film -> viewModel.loadMovieCertain(film.kinopoiskId) }


    }


    private fun showBottomDialog(
        title: String,
        year: String?,
        genres: String,
        rating: Double?,
        url: String,
        filmId: Int
    ) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_dialog_filmpage)
        dialog.setCancelable(true)
        dialog.dismissWithAnimation
        val closeBtn = dialog.findViewById<AppCompatImageView>(R.id.close_dialog_profile)
        val recycler = dialog.findViewById<RecyclerView>(R.id.check_box_recyclerview)
        val createCollection =
            dialog.findViewById<AppCompatTextView>(R.id.create_collection_profile)
        val movieItem = dialog.findViewById<MovieItemHorizontal>(R.id.movie_item_horizontal)
        movieItem?.setTitle(title)
        movieItem?.setSubtitle(year, genres)
        movieItem?.setRating(rating)
        movieItem?.setPicture(url)

        val adapter = CheckBoxCollectionAdapter(filmId)
        recycler?.adapter = adapter
        viewModel.savedCollections.onEach {
            adapter.submitList(it)
            adapter.needAdd = { collectionName ->
                viewModel.checkFilm(collectionName, filmId, title, genres, rating, url)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        createCollection?.setOnClickListener {
            addCollectionDialog()
        }

        dialog.show()

        closeBtn?.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun addCollectionDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_collection)
        dialog.setCancelable(false)
        val editText = dialog.findViewById<AppCompatEditText>(R.id.edittext_dialog_profile)
        val doneButton = dialog.findViewById<MaterialButton>(R.id.done_dialog_profile)
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_edit_dialog)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        doneButton.setOnClickListener {
            val collectionName = editText.text.toString()
            viewModel.addCollection(collectionName)
            viewModel.collectionAdded.onEach {
                if (it == collectionName) {
                    errorBottomDialog()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            dialog.cancel()
        }
        dialog.show()
    }


    private fun errorBottomDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_error_message)
        dialog.setCancelable(true)
        dialog.dismissWithAnimation
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_error)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    @SuppressLint("SetTextI18n")
    private fun updateMovieDetails(movie: Movie) {
        val ageLimit = when (movie.ratingAgeLimits) {
            "age18" -> "18+"
            "age16" -> "16+"
            "age12" -> "12+"
            "age6" -> "6+"
            "age0" -> "0+"
            else -> movie.ratingAgeLimits
        }


        val information: List<String> = listOfNotNull(
            movie.ratingKinopoisk?.toString(),
            movie.nameOriginal,
            movie.year.toString(),
            movie.genres?.joinToString(", ") { it.genre ?: "" },
            movie.countries?.firstOrNull()?.country,
            formatTime(movie.filmLength),
            ageLimit
        )

        binding.favouriteBtn.setOnClickListener {
            val collectionName = viewModel.collectionName.liked
            (movie.nameRu ?: movie.nameOriginal)?.let { title ->
                movie.genres?.joinToString(", ") { it.genre ?: "" }?.let { genres ->
                    movie.posterUrl?.let { it1 ->
                        viewModel.checkFilm(collectionName, movie.kinopoiskId, title, genres, movie.ratingKinopoisk,
                            it1
                        )
                    }
                }
            }
        }

        binding.bookmarkBtn.setOnClickListener {
            val collectionName = viewModel.collectionName.wantWatch
            (movie.nameRu ?: movie.nameOriginal)?.let { title ->
                movie.genres?.joinToString(", ") { it.genre ?: "" }?.let { genres ->
                    movie.posterUrl?.let { it1 ->
                        viewModel.checkFilm(collectionName, movie.kinopoiskId, title, genres, movie.ratingKinopoisk,
                            it1
                        )
                    }
                }
            }
        }

        val collectionName = viewModel.collectionName.interested
        (movie.nameRu ?: movie.nameOriginal)?.let { title ->
            movie.genres?.joinToString(", ") { it.genre ?: "" }?.let { genres ->
                movie.posterUrl?.let { it1 ->
                    viewModel.checkFilm(collectionName, movie.kinopoiskId, title, genres, movie.ratingKinopoisk,
                        it1
                    )
                }
            }
        }



        binding.informationTextView.text = information.joinToString(", ")
        binding.shortDescriptionTextView.text = movie.shortDescription

        binding.threePointsBtn.setOnClickListener {
            (movie.nameRu ?: movie.nameOriginal)?.let { it1 ->
                movie.genres?.joinToString(", ") { it.genre ?: "" }?.let { it2 ->
                    movie.posterUrlPreview?.let { it3 ->
                        showBottomDialog(
                            title = it1,
                            genres = it2,
                            year = movie.year.toString(),
                            rating = movie.ratingKinopoisk,
                            url = it3,
                            filmId = movie.kinopoiskId
                        )
                    }
                }
            }
        }



        (movie.nameRu ?: movie.nameOriginal)?.let {
            movie.posterUrl?.let { it1 ->
                viewModel.checkIcons(
                    movie.kinopoiskId,
                    it, movie.genres.toString(), movie.ratingKinopoisk, it1
                )
            }
        }

        binding.descriptionTextView.text =
            movie.description?.take(250)?.plus("...") ?: "Описание недоступно"


        binding.descriptionTextView.setOnClickListener {
            toggleDescription(movie.description ?: "")
        }

        Glide.with(binding.certainCover.context)
            .load(movie.posterUrl)
            .into(binding.certainCover)

        movie.logoUrl?.let {
            Glide.with(binding.filmLogoImageView.context)
                .load(it)
                .into(binding.filmLogoImageView)
        } ?: run {
            binding.filmLogoImageView.visibility = View.GONE
            binding.informationTextView.visibility = View.VISIBLE
        }

        if (movie.serial) {
            binding.seasonsSeriesTextView.text = "Сезоны и серии"
            binding.seasonsSeriesAllTextView.text = "Все"

            binding.seasonsSeriesAllTextView.setOnClickListener {
                val action =
                    CertainMovieFragmentDirections.actionNavigationCertainMovieToNavigationSeasons(
                        movie.kinopoiskId
                    )
                navController.navigate(action)
            }


            val seasonsCount = viewModel.seasonsInfo.value?.total ?: 0
            val episodesCount = viewModel.getTotalEpisodesCount()

            val seasonsText = getSeasonText(seasonsCount)
            val episodesText = "$episodesCount серий"

            binding.seasonsSeriesCountTextView.text = "$seasonsText, $episodesText"


        } else {
            binding.seriesLinearLayout.visibility = View.GONE
        }
    }


    private fun formatTime(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours == 0 -> "$minutes мин"
            minutes == 0 -> "$hours ч"
            else -> "$hours ч $minutes мин"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun toggleDescription(fullDescription: String?) {
        val textView = binding.descriptionTextView

        if (isExpanded) {
            textView.text = fullDescription?.take(250) + "..."
        } else {
            textView.maxLines = Int.MAX_VALUE
            textView.text = fullDescription
        }
        isExpanded = !isExpanded
    }

    private fun getSeasonText(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "$count сезон"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "$count сезона"
            else -> "$count сезонов"
        }
    }
}