package com.example.medtracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.substance_row.view.*

class SubstanceAdapter(val drugFeed: DrugFeed): RecyclerView.Adapter<CustomViewHolder>(){

    override fun getItemCount(): Int {
        return drugFeed.data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.substance_row, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val drug = drugFeed.data[position]
        holder.view.Substance_name.text = drug.attributes.name
        holder.view.Liters.text = drug.id.toString()
        holder.view.Percantage.text = drug.type
        val thumbnailimageview = holder.view.Preview_image
        Picasso.with(holder.view.context).load(drug.attributes.thumbnailURL).into(thumbnailimageview)
        //posts the made changes to the holder
        holder.drug = drug

    }

}

class CustomViewHolder(val view: View, var drug: Drugdata?= null): RecyclerView.ViewHolder(view){ // inst the intent names
    companion object{
        const val SubstanceNameKey = "Substance title"
        const val SubstanceIdKey = "Substance Id"
        const val SubstancePercentageKey = "Substance Percentage"
        const val SubstanceImageKey = "Substance Image"
    }

    init { // send the extra's with the intent
        view.setOnClickListener {
            val intent = Intent(view.context, AddSubstance::class.java)

            intent.putExtra(SubstanceIdKey, drug?.id.toString())
            intent.putExtra(SubstancePercentageKey, drug?.type)
            intent.putExtra(SubstanceImageKey, drug?.attributes?.thumbnailURL)
            intent.putExtra(SubstanceNameKey, drug?.attributes?.name)
            view.context.startActivity(intent)
        }
    }
}
