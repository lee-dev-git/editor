package a.alt.z.journaleditor.ui

import a.alt.z.journaleditor.R
import a.alt.z.journaleditor.databinding.ActivityAddJournalBinding
import a.alt.z.journaleditor.ui.imagepicker.ImagePickerFragment
import a.alt.z.journaleditor.util.extension.viewBinding
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlin.math.roundToInt

class AddJournalActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityAddJournalBinding::inflate)

    private val permissionLauncher
            = registerForActivityResult(RequestPermission()) { permissionGranted -> if(permissionGranted) showImagePicker() }

    private var imageViews = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        setupFragmentResultListener()
    }

    private fun init() {
        binding.apply {
            addJournalGalleryImageView.setOnClickListener {
                val permissionGranted =
                        ContextCompat.checkSelfPermission(this@AddJournalActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

                if(permissionGranted) showImagePicker()
                else permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun showImagePicker() = supportFragmentManager.commit { replace(R.id.addJournalFragmentContainer, ImagePickerFragment()) }

    private fun hideImagePicker() = supportFragmentManager.findFragmentById(R.id.addJournalFragmentContainer)
        ?.let { supportFragmentManager.commit { remove(it) } }

    private fun setupFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener("image_picker_request_key", this) { _, bundle ->
            bundle
                    .getParcelableArrayList<Uri>("uris_key")
                    ?.toList()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { addImageViews(it) }

            hideImagePicker()
        }
    }

    private fun addImageViews(uris: List<Uri>) {
        uris.forEach { uri ->
            val imageView = ImageView(this)
                .apply { layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) }
            binding.addJournalEditorLayout.addView(imageView)

            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, resources.displayMetrics)
            Glide.with(this)
                .load(uri)
                .transform(
                    FitCenter(),
                    RoundedCorners(radius.roundToInt())
                )
                .into(imageView)
        }
        binding.addJournalEditorLayout.addView(
            EditText(this)
                .apply {
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    background = null
                }
        )
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.addJournalFragmentContainer)
                ?.let { supportFragmentManager.commit { remove(it) }; return }

        super.onBackPressed()
    }
}