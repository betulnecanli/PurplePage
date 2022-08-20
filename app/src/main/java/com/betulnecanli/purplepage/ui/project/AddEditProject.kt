package com.betulnecanli.purplepage.ui.project

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.data.model.Projects
import com.betulnecanli.purplepage.viewmodel.AddEditProjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProject: DialogFragment() {

    private val viewModel : AddEditProjectViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_project_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.editTextProject)

        with(builder){
            setTitle("Enter some Project!")
            editText.setText(viewModel.projectTitle)
            setPositiveButton("OK"){
                    dialog, which ->
                val projectName = editText.text.toString()
                val pro = Projects(0,projectName,false)
                viewModel.onSaveClick(pro)
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