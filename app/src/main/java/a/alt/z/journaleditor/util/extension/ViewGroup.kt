package a.alt.z.journaleditor.util.extension

import android.view.LayoutInflater
import android.view.ViewGroup

val ViewGroup.layoutInflater get() = LayoutInflater.from(context)