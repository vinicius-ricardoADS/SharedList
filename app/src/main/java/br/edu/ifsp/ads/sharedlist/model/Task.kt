package br.edu.ifsp.ads.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey (autoGenerate = false)
    val title: String,
    @NonNull
    val userWhoCreated: String,
    @NonNull
    val dateCreation: String,
    @NonNull
    val description: String,
    @NonNull
    val datePreview: String
): Parcelable