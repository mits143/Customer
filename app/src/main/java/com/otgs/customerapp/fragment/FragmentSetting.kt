package com.otgs.customerapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.BuildConfig
import com.otgs.customerapp.R
import com.otgs.customerapp.activities.*
import com.otgs.customerapp.utils.TextViewDrawableSize
import com.otgs.customerapp.utils.Utils
import com.otgs.customerapp.utils.Utils.generateQR
import kotlinx.android.synthetic.main.fragement_setting.*
import java.io.File
import java.io.FileOutputStream

class FragementSetting : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragement_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtProfile.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, Profile::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtServicingCharts.setOnClickListener {
            context!!.startActivity(Intent(context, ServicingChart::class.java))
        }
        txtQRCode.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                showDialog()
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtChangePwd.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, ChangePasswordActivity::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtChat.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, ChatActivity::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtPrivacy.setOnClickListener {
            context!!.startActivity(Intent(context, PrivacyPolicy::class.java))
        }
        txtTerms.setOnClickListener {
            context!!.startActivity(Intent(context, TermsConditions::class.java))
        }
        txtAboutUs.setOnClickListener {
            context!!.startActivity(Intent(context, AboutUsActivity::class.java))
        }
        txtContactUs.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, ContactUsActivity::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtRating.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, RatingListActivity::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtFAQ.setOnClickListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
                context!!.startActivity(Intent(context, FAQ::class.java))
            } else {
                context!!.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        txtLogout.setOnClickListener(View.OnClickListener {
            showAlert()
        })
    }

    private fun showDialog() {

        var mQRBitmap: Bitmap? = null

        // custom dialog
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_qr_code)
        dialog.setCancelable(true)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imgQR = dialog.findViewById(R.id.imgQR) as ImageView
        val txtName = dialog.findViewById(R.id.txtName) as TextView
        val txtCode = dialog.findViewById(R.id.txtCode) as TextView
        val txtShare = dialog.findViewById(R.id.txtShare) as TextViewDrawableSize

        txtName.text = sessionManager.getName(context)
        txtCode.text = sessionManager.getUnique_No(context)
        mQRBitmap = generateQR(sessionManager.getUnique_No(context)!!)
        if (mQRBitmap != null) {
            imgQR.setImageBitmap(mQRBitmap)
        } else {
            imgQR.setImageBitmap(null)
            mQRBitmap = null
        }

        txtShare.setOnClickListener {
            //share image
            shareImage(mQRBitmap!!)
        }

        dialog.show()

    }

    fun shareImage(bitmap: Bitmap) {
        val file_path: String =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + context!!.getString(
                R.string.app_name
            )
        val dir = File(file_path)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "QRCode.png")
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        if (file != null) {
            val photoURI = FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file!!
            )
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            startActivity(Intent.createChooser(intent, "Share Cover Image"))
        }
    }

    fun showAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Do you really want to exit ?")
        builder.setTitle("Exit !")
        builder.setCancelable(true)
        builder
            .setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialog, which -> // When the user click yes button
                    sessionManager.clearData(context!!)
                    activity!!.finish()
                })
        builder
            .setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog, which -> // If user click no
                    // then dialog box is canceled.
                    dialog.cancel()
                })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(sessionManager.getUser_ID(context))) {
            txtLogout.visibility = View.VISIBLE
        } else {
            txtLogout.visibility = View.GONE
        }
    }
}