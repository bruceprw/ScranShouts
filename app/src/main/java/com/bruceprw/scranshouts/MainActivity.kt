package com.bruceprw.scranshouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_feed)

        val imageModelArrayList = populateList()

        val recyclerView = findViewById<View>(R.id.main_feed_recycler) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(this) // Get the layout manager
        recyclerView.layoutManager = layoutManager
        val mAdapter = MyAdapter(imageModelArrayList)
        recyclerView.adapter = mAdapter
    }

    /*
     * A function to add names of F1 teams and logos to a list of MyModel
     */
    private fun populateList(): ArrayList<MyModel> {
        val list = ArrayList<MyModel>()
        val myImageList = arrayOf(R.drawable.logo_chicken_only, R.drawable.sg_logo, R.drawable.logo_chicken_only, R.drawable.logo_chicken_only)
        val myReviewTitleList = arrayOf(R.string.dummy_title, R.string.dummy_title, R.string.dummy_title, R.string.dummy_title)
        val myReviewNameList = arrayOf(R.string.dummy_name, R.string.dummy_name, R.string.dummy_name, R.string.dummy_name,)
//        val myReviewRatingList = arrayOf(4,4,4,4)
        for (i in 0..3) {
            val reviewModel = MyModel()
            reviewModel.setTitles(getString(myReviewTitleList[i]))
            reviewModel.setImages(myImageList[i])
            reviewModel.setNames(getString(myReviewNameList[i]))
//            reviewModel.setRatings(myReviewRatingList[i])
            list.add(reviewModel)
        }
        return list
    }
}