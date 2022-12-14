package com.betulnecanli.purplepage.ui.subject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.data.model.Subjects
import com.betulnecanli.purplepage.viewmodel.AddEditSubjectViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditSubject : DialogFragment() {

    private val viewModel : AddEditSubjectViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_subject_layout,null)
        val editText: EditText = dialogLayout.findViewById(R.id.editTextSubject)

        with(builder){
            setTitle("Enter some Subject!")
            editText.setText(viewModel.subjectTitle)
            setPositiveButton("OK"){
                    dialog, which ->
                val subjectName = editText.text.toString()
                val sub = Subjects(0,subjectName,false)
                viewModel.onSaveClick(sub)

                Log.d("Positive",subjectName)

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