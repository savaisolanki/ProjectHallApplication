import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecthallapplication.R
import com.example.projecthallapplication.databinding.AlphabetitemBinding
import com.example.projecthallapplication.dataclass.User


/*
* create a alphabetically adapter and pass context ,list of users, group of char and click listener
 */

class AlphabeticalExpandableAdapter(
    private val context: Context,
    private var userList: List<User>,
    private val groupList: List<Char>,
    private val userClickListener: OnUserClickListener
) : RecyclerView.Adapter<AlphabeticalExpandableAdapter.AlphabetViewHolder>() {


    /*
    * Define HashMap that contain Char and list or users
    * */
    private val alphabetMap = HashMap<Char, MutableList<User>>()

    init {
        sortUserDataByAlphabet()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }


    /*
    * Using a user list for loop  we get a all register first letter and convert to upper case
    * alphabet map not contains first char that create list of
    * else add particular char all that user data alphabetically we represent like A aman amaya asam
    */
    private fun sortUserDataByAlphabet() {
        for (userData in userList) {
            val firstChar = userData.firstName.first().uppercaseChar()
            if (!alphabetMap.containsKey(firstChar)) {
                alphabetMap[firstChar] = mutableListOf()
            }
            alphabetMap[firstChar]?.add(userData)
        }
    }


    /*
    * create a view holder
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = AlphabetitemBinding.inflate(inflater, parent, false)
        return AlphabetViewHolder(binding)
    }

    /*
    * Bind All Data using bind view holder
    */

    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        val alphabet = groupList[position]
        val userDataList = alphabetMap[alphabet]
        holder.bind(alphabet, userDataList)
    }

    /*
    * return how much group of char size
    */
    override fun getItemCount(): Int = groupList.size


    /*
    * Create a inner class define our binding  and display data
    * */
    inner class AlphabetViewHolder(private val binding: AlphabetitemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        /*
        * Initialize starting and define recycler view visibility gone and click listener alphabet S A V A I Click event
        */
        init {
            binding.rvUsers.visibility = View.GONE

            binding.tvAlphabet.setOnClickListener(this)

        }


        /*
        * on bind method define char and all user list
        */

        fun bind(alphabet: Char, userDataList: List<User>?) {
            binding.tvAlphabet.text = alphabet.toString()


            /*
            * if our user list null or empty or Rvuser  visibility is gone return visibility gone else visibility  is visible
             */
            val visibility =
                if (userDataList.isNullOrEmpty() || binding.rvUsers.visibility == View.GONE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            binding.rvUsers.visibility = visibility


            /*
            * SET our child adapter here and pass click listener and all register user list data
            */

            val adapter = UserAdapter(userDataList ?: emptyList(), userClickListener)
            val layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvUsers.layoutManager = layoutManager
            binding.rvUsers.adapter = adapter
        }

        override fun onClick(view: View) {


            /*
            * Particular adapter position is not equals recycler view no of position
            * take variable set group-list or all char pass position
            * userdata list variable we pass hashmap with all char adapter position
            * userdata list is not empty we check visibility
            * if Rvuser visibility is gone return visible else gone
            * set our visibility
            */
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val alphabet = groupList[adapterPosition]
                val userDataList = alphabetMap[alphabet]
                userDataList?.let { userList ->
                    val visibility = if (binding.rvUsers.visibility == View.GONE) {
                        Log.d("Adapter", "Changing visibility to VISIBLE")
                        View.VISIBLE
                    } else {
                        Log.d("Adapter", "Changing visibility to GONE")
                        View.GONE
                    }
                    binding.rvUsers.visibility = visibility

                }
            }
        }
    }


    /*
    * Define on click Listener  Via interface
    */
    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}

/*
* create a UserAdapter adapter and pass list of users and  click listener
 */

class UserAdapter(
    private val userList: List<User>,
    private val userClickListener: AlphabeticalExpandableAdapter.OnUserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    /*
    * create a view holder
    */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_data, parent, false)
        return UserViewHolder(view)
    }


    /*
    * on bind method define char and all user list
    */

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    /*
    * return  userList size
    */
    override fun getItemCount(): Int = userList.size


    /*
    * Define inner class pass view and click listener and set text view data set
    * */
    inner class UserViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val tvUserName: TextView = view.findViewById(R.id.tvUserName)

        /*
        * This class initialize automatically init block execute
        */
        init {
            view.setOnClickListener(this)
        }


        /*
        * bind function  user data class get value and set particular type in textview
        */

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            tvUserName.text = "${user.firstName} ${user.lastName}"
        }


        /*
        * adapter position  not equals recycler view no position
        * take user variable store duelist position
        * and click listener method use pass user list adapter position pass specific position
        */
        override fun onClick(view: View) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val user = userList[adapterPosition]
                userClickListener.onUserClick(user)
            }
        }
    }
}
