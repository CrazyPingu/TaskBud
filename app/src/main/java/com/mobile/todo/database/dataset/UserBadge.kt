package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Badge::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("badgeId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserBadge(
    val obtained: Boolean,
    val userId: Int,
    val badgeId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)



