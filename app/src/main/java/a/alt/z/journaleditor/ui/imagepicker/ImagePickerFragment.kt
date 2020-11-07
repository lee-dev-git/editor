package a.alt.z.journaleditor.ui.imagepicker

import a.alt.z.journaleditor.R
import a.alt.z.journaleditor.databinding.FragmentImagePickerBinding
import a.alt.z.journaleditor.model.Album
import a.alt.z.journaleditor.util.extension.viewBinding
import a.alt.z.journaleditor.util.getImagesGroupByAlbum
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.properties.Delegates

class ImagePickerFragment : Fragment(R.layout.fragment_image_picker) {

    private val binding by viewBinding(FragmentImagePickerBinding::bind)

    private val albumAdapter = AlbumAdapter(::onAlbumSelect)

    private val imageAdapter = ImageAdapter(::onImageSelect)

    private val selectedImageAdapter = SelectedImageAdapter(::onSelectedImageClick)

    private var selectedImages: List<Uri> by Delegates.observable(emptyList()) { _, _, new ->
        binding.imagePickerSelectedImageRecyclerView.isVisible = new.isNotEmpty()
        selectedImageAdapter.submitList(new)
        imageAdapter.selectedImages = new
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val images = getImagesGroupByAlbum(requireContext())

        init()

        initImages(images)
    }

    private fun init() {
        binding.apply {
            imagePickerAlbumNameTextView.setOnClickListener { showOrHideAlbumList() }
            imagePickerAlbumCountTextView.setOnClickListener { showOrHideAlbumList() }
            imagePickerCompleteTextView.setOnClickListener { onComplete(selectedImages) }

            imagePickerAlbumRecyclerView.adapter = albumAdapter
            imagePickerAlbumRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            imagePickerSelectedImageRecyclerView.adapter = selectedImageAdapter
            imagePickerSelectedImageRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            imagePickerImageRecyclerView.adapter = imageAdapter
            imagePickerImageRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun initImages(images: Map<Album, List<Uri>>) {
        val all = Album(0L, "전체보기")
        val uris = images[all]

        binding.imagePickerAlbumNameTextView.text = all.name
        val countText = "${uris?.size ?: 0}장"
        binding.imagePickerAlbumCountTextView.text = countText

        albumAdapter.submitList(images.toList())
        imageAdapter.submitList(uris)
    }

    private fun onAlbumSelect(album: Album, uris: List<Uri>) {
        binding.apply {
            imagePickerAlbumNameTextView.text = album.name
            val countText = "${uris.size}장"
            imagePickerAlbumCountTextView.text = countText

            imageAdapter.submitList(uris)

            binding.imagePickerAlbumLayout.isVisible = false
        }
    }

    private fun showOrHideAlbumList() { binding.imagePickerAlbumLayout.isVisible = !binding.imagePickerAlbumLayout.isVisible }

    private fun onImageSelect(uri: Uri) {
        selectedImages = selectedImages.toMutableList().apply { if(selectedImages.contains(uri)) remove(uri) else add(uri) }
    }

    private fun onSelectedImageClick(uri: Uri) {
        selectedImages = selectedImages.toMutableList().apply { remove(uri) }
    }

    private fun onComplete(uris: List<Uri>) {
        setFragmentResult("image_picker_request_key", bundleOf("uris_key" to uris))
    }
}