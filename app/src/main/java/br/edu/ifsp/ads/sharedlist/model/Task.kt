package br.edu.ifsp.ads.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    @NonNull
    val title: String,
    @NonNull
    val userWhoCreated: String,
    @NonNull
    val dateCreation: String,
    @NonNull
    val description: String,
    @NonNull
    val datePreview: String,
    @NonNull
    val finished: Boolean,
): Parcelable