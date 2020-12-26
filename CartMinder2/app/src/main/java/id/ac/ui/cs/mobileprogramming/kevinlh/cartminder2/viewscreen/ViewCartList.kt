package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.AddEditActivity


class ViewCartList : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cart_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_menu) {
            val intent = Intent(context, AddEditActivity::class.java)
            startActivity(intent)
        }
        return false
    }
}