package com.example.androiddevelopmentgroup7.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_rating_detail.newInstance] factory method to
 * create an instance of this fragment.
 */

class MyRatingAdapter(var context: Context, private var ratingList:ArrayList<RatingDetails>): RecyclerView.Adapter<MyRatingAdapter.ViewHolder>(){


    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var customer_name: TextView = listItemView.findViewById(R.id.customer_name)
        var customer_rating_bar: RatingBar = listItemView.findViewById(R.id.customer_rating_bar)
        var feedback_message: TextView = listItemView.findViewById(R.id.feedback_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.rating_list_item, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customer_name.text = ratingList.get(position).customerName
        holder.customer_rating_bar.rating = ratingList.get(position).customerRating.toString().toFloat()
        holder.feedback_message.text = ratingList.get(position).feedbackMessage
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

}

data class RatingDetails(
    var customerName:String,
    var customerRating:Float,
    var feedbackMessage: String,
)

class fragment_rating_detail : Fragment() {
    // TODO: Rename and change types of parameters
    private var serviceID: String? = null
    private var toolbar: MaterialToolbar? = null
    private var loader: FrameLayout? = null
    private var emptyTextView:TextView? = null
    private var ratingRecyclerView:RecyclerView? = null
    private var ratingList:ArrayList<RatingDetails>? = null
    //private var ratingAdapter = MyRatingAdapter(ArrayList<HashMap<String,Any>>())
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceID = it.getString("serviceID")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating_detail, container, false)
        initComponent(view)
        setEvalueList()


        return view
    }
    private fun initComponent(view:View){
        toolbar = view.findViewById(R.id.topAppBar)
        loader = view.findViewById(R.id.loader_layout)
        toolbar?.setTitle(getString(R.string.rating_app_tittle_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
        ratingRecyclerView = view.findViewById(R.id.rating_recycle_view)
        emptyTextView = view.findViewById(R.id.empty_rating_textview)
        emptyTextView?.visibility = View.GONE
    }
    private fun setEvalueList(){
        ratingList = ArrayList()
        db.collection("ServiceRatings").document(serviceID!!).get().addOnSuccessListener { doc ->
            if(!doc.exists()){
                loader?.visibility = View.GONE
                ratingRecyclerView?.visibility = View.GONE
                emptyTextView?.visibility = View.VISIBLE
            } else {
                val ratingDetailMap = doc.data?.get("RatingDetails") as Map<*,*>
                val idCustomerList = ratingDetailMap.keys
                for(key in idCustomerList){
                    val personMap = ratingDetailMap.get(key) as Map<*,*>
                    val ratingDetails = RatingDetails(
                        personMap.get("CustomerName").toString(),
                        personMap.get("Rating").toString().toFloat(),
                        personMap.get("FeedBack").toString()
                    )
                    ratingList?.add(ratingDetails)
                }
                val ratingAdapter = MyRatingAdapter(requireContext(),ratingList!!)
                ratingRecyclerView!!.adapter = ratingAdapter
                loader?.visibility = View.GONE
                ratingRecyclerView?.visibility = View.VISIBLE
                emptyTextView?.visibility = View.GONE
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_rating_detail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_rating_detail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}