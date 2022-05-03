package com.example.androiddevelopmentgroup7.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Notification
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.viewModels.NotificationViewModel
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.models.Order

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_notification.newInstance] factory method to
 * create an instance of this fragment.
 */

class MyNotificationAdapter(var context: Context, private var notificationList:ArrayList<Notification>): RecyclerView.Adapter<MyNotificationAdapter.ViewHolder>(){
    var onClick: ((Notification) -> Unit)? = null
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var noti_title_tv: TextView = listItemView.findViewById(R.id.noti_title_tv)
        var description_tv: TextView = listItemView.findViewById(R.id.description_tv)

        var time_tv: TextView = listItemView.findViewById(R.id.time_tv)
        var notification_image_view: ImageView = listItemView.findViewById(R.id.notification_image_view)


        init {
            listItemView.setOnClickListener { onClick?.invoke(notificationList[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.list_noti, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Notification = notificationList.get(position)
        val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")
        val date = (notificationList.get(position).Time as Timestamp).toDate()
        val convertDay = formatter.format(date)
        val des_temp = notificationList.get(position).Description
        var des_display :String =des_temp

        if (des_temp.length>15) des_display=des_temp.substring(0,14)+"..."
        holder.notification_image_view.setImageResource(R.drawable.noti)
        holder.noti_title_tv.text  = notificationList.get(position).Name
        holder.description_tv.text  = des_display
        holder.time_tv.text  = convertDay

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

}






class fragment_notification : Fragment() {
    val db = Firebase.firestore

    private val notificationViewModel : NotificationViewModel by activityViewModels()
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.notification_recycler_view)

        notificationViewModel.setServiceList()


        val adapter = MyNotificationAdapter(requireContext(),notificationViewModel.selectedServiceList.value!!)


        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)

        val loader = rootView.findViewById<FrameLayout>(R.id.loader_layout)


        adapter.onClick = { contact ->
            cartItemClick(contact)
        }
        notificationViewModel.selectedServiceList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyDataSetChanged()
            Log.i("DATASET CHANGE", "data set change")
        })

        notificationViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            // Update the list UI
            when(status){
                "hide_loader" -> {
                    loader.visibility = View.GONE

                }
                "loading"-> {
                    loader.visibility = View.VISIBLE

                }
            }
        })


        return rootView
    }



    private fun cartItemClick(notification:Notification){
        val bundle = Bundle()
        val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")
        val date = (notification.Time as Timestamp).toDate()
        val convertDay = formatter.format(date)
        bundle.putString("name", notification.Name)
        bundle.putString("time", convertDay)
        bundle.putString("description", notification.Description)
        findNavController().navigate(R.id.action_fragment_notification_to_fragment_notification_details, bundle)
    }
}