package com.betulnecanli.purplepage.ui.goal

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.data.model.Goals
import com.betulnecanli.purplepage.viewmodel.AddEditGoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditGoal : DialogFragment(){

    private val viewModel : AddEditGoalViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_goal_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.editTextGoal)

        with(builder){
            setTitle("Enter some Goal!")
            editText.setText(viewModel.goalTitle)
            setPositiveButton("OK"){
                    dialog, which ->
                val goalName = editText.text.toString()
                val goa = Goals(0,goalName,false)
                viewModel.onSaveClick(goa)
            }
            setNegativeButton("Cancel"){
                    dialog, which ->
                Log.d("Negative", "Fail")
            }

            setView(dialogLayout)
        }
        return builder.show()
    }
}