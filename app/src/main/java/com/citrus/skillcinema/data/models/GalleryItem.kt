package com.citrus.skillcinema.data.models

data class GalleryItemsDto(
    val total: Int,
    val totalPages: Int,
    val items: List<GalleryItem>
)

data class GalleryItem(
    val imageUrl : String,
    val previewUrl : String?
)

