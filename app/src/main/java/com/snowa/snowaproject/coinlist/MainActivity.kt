package com.snowa.snowaproject.coinlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.snowa.snowaproject.R
import com.snowa.snowaproject.ViewState
import com.snowa.snowaproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<CoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        initList()
    }

    private fun initList() {
        val adapter = CoinAdapter()
        adapter.setHasStableIds(true)
        binding.content.list.layoutManager = LinearLayoutManager(this)
        binding.content.list.adapter = adapter
        binding.content.list.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        android.R.drawable.divider_horizontal_bright,
                        null
                    )!!
                )
            }
        )

        viewModel.getCoinList()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coinList.collectLatest { viewState ->
                    when (viewState) {
                        is ViewState.Loading -> binding.content.progressBar.isVisible = true
                        is ViewState.Success -> {
                            binding.content.progressBar.isVisible = false
                            adapter.submitList(viewState.response)
                        }
                        is ViewState.Failed -> {
                            binding.content.progressBar.isVisible = false
                            showFailedSnack(viewState.exception.message, viewModel::getCoinList)
                        }
                    }
                }
            }
        }
    }

    private fun showFailedSnack(failedMessage: String?, onRetry: () -> Unit) {
        Snackbar.make(
            binding.root,
            getString(R.string.failed_msg_snack).format(failedMessage),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.retry)) { onRetry() }
            .show()
    }
}