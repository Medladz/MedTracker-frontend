package com.example.medtracker

import DrugFeed
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import drugdata
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
        const val SubstanceNameKey = "Substance title"
        const val SubstanceLiterKey = "Substance liter"
        const val SubstancePercentageKey = "Substance Percentage"
        const val SubstanceImageKey = "Substance Image"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, AddSubstance::class.java)

            //meesturen van gegevens naar de addsubstance pagina
            intent.putExtra(SubstanceNameKey, drug?.attributes?.name)
            intent.putExtra(SubstanceLiterKey, drug?.id.toString())
            intent.putExtra(SubstancePercentageKey, drug?.type)
            intent.putExtra(SubstanceImageKey, drug?.attributes?.thumbnailURL)

            view.context.startActivity(intent)
        }
    }
}
