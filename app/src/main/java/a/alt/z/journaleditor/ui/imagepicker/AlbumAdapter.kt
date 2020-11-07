package a.alt.z.journaleditor.ui.imagepicker

import a.alt.z.journaleditor.databinding.ItemAlbumBinding
import a.alt.z.journaleditor.model.Album
import a.alt.z.journaleditor.util.extension.layoutInflater
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(
    private val onClickAction: (Album, List<Uri>) -> Unit
): ListAdapter<Pair<Album, List<Uri>>, AlbumViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder
            = AlbumViewHolder(ItemAlbumBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position).let { pair ->
            val album = pair.first
            val uris = pair.second
            holder.bind(album, uris)
        }
    }

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Pair<Album, List<Uri>>>() {
            override fun areItemsTheSame(oldItem: Pair<Album, List<Uri>>, newItem: Pair<Album, List<Uri>>): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Pair<Album, List<Uri>>,
                newItem: Pair<Album, List<Uri>>
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }
        }
    }
}