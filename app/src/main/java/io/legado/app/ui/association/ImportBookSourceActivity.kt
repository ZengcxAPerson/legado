package io.legado.app.ui.association

import android.os.Bundle
import androidx.activity.viewModels
import io.legado.app.R
import io.legado.app.base.VMBaseActivity
import io.legado.app.constant.Theme
import io.legado.app.databinding.ActivityTranslucenceBinding
import io.legado.app.help.IntentDataHelp
import io.legado.app.lib.dialogs.alert
import io.legado.app.utils.viewbindingdelegate.viewBinding


class ImportBookSourceActivity :
    VMBaseActivity<ActivityTranslucenceBinding, ImportBookSourceViewModel>(
        theme = Theme.Transparent
    ) {

    override val binding by viewBinding(ActivityTranslucenceBinding::inflate)
    override val viewModel by viewModels<ImportBookSourceViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.rotateLoading.show()
        viewModel.errorLiveData.observe(this, {
            binding.rotateLoading.hide()
            errorDialog(it)
        })
        viewModel.successLiveData.observe(this, {
            binding.rotateLoading.hide()
            if (it > 0) {
                ImportBookSourceDialog().show(supportFragmentManager, "SourceDialog")
            } else {
                errorDialog(getString(R.string.wrong_format))
            }
        })
        initData()
    }

    private fun initData() {
        intent.getStringExtra("dataKey")?.let {
            IntentDataHelp.getData<String>(it)?.let { source ->
                viewModel.importSource(source)
                return
            }
        }
        intent.getStringExtra("source")?.let {
            viewModel.importSource(it)
            return
        }
        finish()
    }

    private fun errorDialog(msg: String) {
        alert(getString(R.string.error), msg) {
            okButton { }
            onDismiss {
                finish()
            }
        }.show()
    }

}