package dev.trindadedev.tinyinstaller.data.recycle.model.impl

import com.rosan.app_process.AppProcess

import dev.trindadedev.tinyinstaller.data.recycle.model.exception.AppProcessNotWorkException
import dev.trindadedev.tinyinstaller.data.recycle.model.exception.RootNotWorkException
import dev.trindadedev.tinyinstaller.data.recycle.repo.Recycler

import java.util.StringTokenizer

class AppProcessRecycler(private val shell: String) : Recycler<AppProcess>() {
    private class CustomizeAppProcess(private val shell: String) : AppProcess.Terminal() {
        override fun newTerminal(): MutableList<String> {
            val st = StringTokenizer(shell)
            val cmdList = mutableListOf<String>()
            while (st.hasMoreTokens()) {
                cmdList.add(st.nextToken());
            }
            return cmdList;
        }
    }

    override fun onMake(): AppProcess {
        return CustomizeAppProcess(shell).apply {
            if (init()) return@apply
            if (shell == "su") throw RootNotWorkException()
            else throw AppProcessNotWorkException()
        }
    }
}