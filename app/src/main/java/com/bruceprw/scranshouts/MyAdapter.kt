package com.bruceprw.scranshouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bruceprw.scranshouts.MyModel
import com.bruceprw.scranshouts.R

class MyAdapter (private val reviewModelArrayList: MutableList<MyModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

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
        val info = reviewModelArrayList[position]

        holder.imgView.setImageResource(info.getImages())
        holder.revNameTxt.setText(info.getNames())
        holder.revTitleTxt.setText(info.getTitles())
        holder.revRating.setImageResource(info.getRatings())

    }

    /*
     * Get the maximum size of the
     */
    override fun getItemCount(): Int {
        return reviewModelArrayList.size
    }

    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var imgView = itemView.findViewById<View>(R.id.review_img) as ImageView
        var revNameTxt = itemView.findViewById<View>(R.id.restaurant_name) as TextView
        var revTitleTxt = itemView.findViewById<View>(R.id.review_title) as TextView
        var revRating = itemView.findViewById<View>(R.id.star1) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
//            val msg = txtMsg.text
//            val snackbar = Snackbar.make(v, "$msg" + R.string.msg, Snackbar.LENGTH_LONG)
//            snackbar.show()
        }
    }
}




//var intent = Intent(itemView.context, TeamDetail::class.java)
//itemView.context.startActivity(intent)