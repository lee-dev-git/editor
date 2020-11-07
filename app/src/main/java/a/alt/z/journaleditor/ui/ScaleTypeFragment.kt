package a.alt.z.journaleditor.ui

import a.alt.z.journaleditor.R
import a.alt.z.journaleditor.databinding.FragmentScaleTypeBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import a.alt.z.journaleditor.util.extension.viewBinding

class ScaleTypeFragment : Fragment(R.layout.fragment_scale_type) {

    private val binding by viewBinding(FragmentScaleTypeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.apply {
            Glide.with(requireContext())
                .load("")
                .into(scaleTypeImageView)
        }
    }
}