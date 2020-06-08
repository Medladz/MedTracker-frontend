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
        println(drugFeed.data.count())
        return drugFeed.data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.substance_row, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val drug = drugFeed.data.get(position)
        holder.view.Substance_name.text = drug.attributes.name
        holder.view.Liters.text = drug.id.toString()
        holder.view.Percantage.text = drug.type
        val thumbnailimageview = holder.view.Preview_image
        Picasso.with(holder.view.context).load(drug.attributes.thumbnailURL).into(thumbnailimageview)

        // maakt de video publicly accesable
        holder?.drug = drug

    }

}

// video is de functie waar de json data binnenkomt
class CustomViewHolder(val view: View, var drug: drugdata?= null): RecyclerView.ViewHolder(view){
    //hier stuur je de info via een intent mee naar de add_substance
    companion object{
        const val Substance_Name_Key = "Substance title"
        const val Substance_Liter_Key = "Substance liter"
        const val Substance_Percentage_Key = "Substance Percentage"
        const val Substance_Image_Key = "Substance Image"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, AddSubstance::class.java)

            //meesturen van gegevens naar de addsubstance pagina
            intent.putExtra(Substance_Name_Key, drug?.attributes?.name)
            intent.putExtra(Substance_Liter_Key, drug?.id.toString())
            intent.putExtra(Substance_Percentage_Key, drug?.type)
            intent.putExtra(Substance_Image_Key, drug?.attributes?.thumbnailURL)

            view.context.startActivity(intent)
        }
    }
}