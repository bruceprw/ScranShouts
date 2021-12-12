package com.bruceprw.scranshouts

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.io.Serializable

class ReviewAdapter (private val reviewModelList: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.review_cards, parent, false)

        return ViewHolder(v)
    }

    /*
     * Bind the data to the child views of the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = reviewModelList[position]

        holder.revTitleTxt.text = info.title
        holder.revNameTxt.text = info.estName
        Picasso.get().load(info.picUrl).into(holder.imgView)

        when (info.rating){
            1 -> {      holder.revStar1.visibility = View.VISIBLE
                        holder.revStar2.visibility = View.INVISIBLE
                        holder.revStar3.visibility = View.INVISIBLE
                        holder.revStar4.visibility = View.INVISIBLE
                        holder.revStar5.visibility = View.INVISIBLE
            }

            2 -> {
                holder.revStar1.visibility = View.VISIBLE
                holder.revStar2.visibility = View.VISIBLE
                holder.revStar3.visibility = View.INVISIBLE
                holder.revStar4.visibility = View.INVISIBLE
                holder.revStar5.visibility = View.INVISIBLE
            }

            3 -> {
                holder.revStar1.visibility = View.VISIBLE
                holder.revStar2.visibility = View.VISIBLE
                holder.revStar3.visibility = View.VISIBLE
                holder.revStar4.visibility = View.INVISIBLE
                holder.revStar5.visibility = View.INVISIBLE
            }

            4 -> {
                holder.revStar1.visibility = View.VISIBLE
                holder.revStar2.visibility = View.VISIBLE
                holder.revStar3.visibility = View.VISIBLE
                holder.revStar4.visibility = View.VISIBLE
                holder.revStar5.visibility = View.INVISIBLE


            }
            5 -> {
                holder.revStar1.visibility = View.VISIBLE
                holder.revStar2.visibility = View.VISIBLE
                holder.revStar3.visibility = View.VISIBLE
                holder.revStar4.visibility = View.VISIBLE
                holder.revStar5.visibility = View.VISIBLE
            }

        }

    }



    override fun getItemCount(): Int {
        return reviewModelList.size
    }


    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var imgView = itemView.findViewById<View>(R.id.review_img) as ImageView
        var revNameTxt = itemView.findViewById<View>(R.id.restaurant_name) as TextView
        var revTitleTxt = itemView.findViewById<View>(R.id.review_title) as TextView
        var revStar1 = itemView.findViewById<View>(R.id.star1) as ImageView
        var revStar2 = itemView.findViewById<View>(R.id.star2) as ImageView
        var revStar3 = itemView.findViewById<View>(R.id.star3) as ImageView
        var revStar4 = itemView.findViewById<View>(R.id.star4) as ImageView
        var revStar5 = itemView.findViewById<View>(R.id.star5) as ImageView

        init {
            itemView.setOnClickListener { view ->
                Log.i("ReviewClickListener", "Review clicked: ${reviewModelList[adapterPosition].title}")
                val intent = Intent(view.context, activity_load_review::class.java)
                intent.putExtra("review", reviewModelList[adapterPosition])
                view.context.startActivity(intent)


            }
        }

        override fun onClick(v: View) {


        }
    }
}


