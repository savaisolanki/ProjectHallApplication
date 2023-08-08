import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecthallapplication.R
import com.example.projecthallapplication.databinding.AlphabetitemBinding
import com.example.projecthallapplication.dataclass.User

class AlphabeticalExpandableAdapter(
    private val context: Context,
    private var userList: List<User>,
    private val groupList: List<Char>,
    private val userClickListener: OnUserClickListener
) : RecyclerView.Adapter<AlphabeticalExpandableAdapter.AlphabetViewHolder>() {

    private val alphabetMap = HashMap<Char, MutableList<User>>()

    init {
        sortUserDataByAlphabet()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    private fun sortUserDataByAlphabet() {
        for (userData in userList) {
            val firstChar = userData.firstName.first().toUpperCase()
            if (!alphabetMap.containsKey(firstChar)) {
                alphabetMap[firstChar] = mutableListOf()
            }
            alphabetMap[firstChar]?.add(userData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = AlphabetitemBinding.inflate(inflater, parent, false)
        return AlphabetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        val alphabet = groupList[position]
        val userDataList = alphabetMap[alphabet]
        holder.bind(alphabet, userDataList)
    }

    override fun getItemCount(): Int = groupList.size

    inner class AlphabetViewHolder(private val binding: AlphabetitemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.tvAlphabet.setOnClickListener(this)
        }

        fun bind(alphabet: Char, userDataList: List<User>?) {
            binding.tvAlphabet.text = alphabet.toString()
            binding.rvUsers.visibility = if (userDataList.isNullOrEmpty()) View.GONE else View.VISIBLE

            val adapter = UserAdapter(userDataList ?: emptyList(), userClickListener)
            val layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvUsers.layoutManager = layoutManager
            binding.rvUsers.adapter = adapter
        }

        override fun onClick(view: View) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val alphabet = groupList[adapterPosition]
                val userDataList = alphabetMap[alphabet]
                userDataList?.let { userList ->
                    val visibility = if (binding.rvUsers.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    binding.rvUsers.visibility = visibility
                }
            }
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}

class UserAdapter(
    private val userList: List<User>,
    private val userClickListener: AlphabeticalExpandableAdapter.OnUserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_data, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = userList.size

    inner class UserViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val tvUserName: TextView = view.findViewById(R.id.tvUserName)

        init {
            view.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            tvUserName.text = "${user.firstName} ${user.lastName}"
        }

        override fun onClick(view: View) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val user = userList[adapterPosition]
                userClickListener.onUserClick(user)
            }
        }
    }
}
