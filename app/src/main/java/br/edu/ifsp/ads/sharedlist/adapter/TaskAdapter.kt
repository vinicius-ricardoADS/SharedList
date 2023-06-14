package br.edu.ifsp.ads.sharedlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import br.edu.ifsp.ads.sharedlist.R
import br.edu.ifsp.ads.sharedlist.databinding.TileTaskBinding
import br.edu.ifsp.ads.sharedlist.model.Task

class TaskAdapter(
    context: Context,
    private val taskList: MutableList<Task>
): ArrayAdapter<Task>(context, R.layout.tile_task, taskList){
    private lateinit var tcb: TileTaskBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task: Task = taskList[position]
        var tileContactView = convertView
        if(tileContactView == null){
            //infla uma nova célula
            tcb = TileTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false

            )
            tileContactView = tcb.root

            //criando um holder e guardando referência para os TextViews
            val tcvh: TileContactViewHolder = TileContactViewHolder(
                tcb.titleTv,
                tcb.checkIv

            )

            //armazenando um ViewHolder na célula
            tileContactView.tag = tcvh
        }

        // substituir os valores
        with(tileContactView.tag as TileContactViewHolder){
            title.text = task.title
            image.visibility = if (task.finished) View.VISIBLE else View.INVISIBLE
        }

        return tileContactView
    }

    private data class TileContactViewHolder(
        val title: TextView,
        val image: ImageView
    )

}