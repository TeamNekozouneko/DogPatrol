package com.nekozouneko.dogPatrol.models

import com.nekozouneko.dogPatrol.checks.ContainsBadwords
import com.nekozouneko.dogPatrol.checks.DuplicateContent
import com.nekozouneko.dogPatrol.checks.IMEConversionAnalysis
import com.nekozouneko.dogPatrol.checks.SimilarityContent
import com.nekozouneko.dogPatrol.manager.CheckManager

class Checks {
    companion object{
        fun get() : ArrayList<CheckManager.CheckHandler>{
            return arrayListOf(
                DuplicateContent(),
                SimilarityContent(),
                ContainsBadwords(),
                IMEConversionAnalysis()
            )
        }
    }
}