package com.bruceprw.scranshouts

/*
 * Data model class to store logos and team names from F1
 */
class MyModel {
    var reviewName: String? = null
    var reviewTitle: String? = null
    private var reviewImage: Int = 0
    private var reviewRating: Int = 0


    fun getNames(): String {
        return reviewName.toString()
    }


    fun setNames(name: String) {
        this.reviewName = name
    }

    fun getTitles(): String {
        return reviewTitle.toString()
    }


    fun setTitles(name: String) {
        this.reviewTitle = name
    }


    fun getImages(): Int {
        return reviewImage
    }


    fun setImages(image_drawable: Int) {
        this.reviewImage = image_drawable
    }

    fun getRatings(): Int {
        return reviewRating
    }

    fun setRatings(rating: Int) {
        this.reviewRating = rating
    }

}