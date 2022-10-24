package com.example.oc_realestatemanager_p9.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.base.BaseFragment
import com.example.oc_realestatemanager_p9.utils.Utils.formatNumber
import kotlinx.android.synthetic.main.fragment_simulator.*
import kotlin.math.pow

class SimulatorFragment : BaseFragment() {

    //Fields
    private var year : Double = 0.0
    private var year2 : Double = 0.0
    private var rate : Double = 0.0
    private var rate2 : Double = 0.0
    private var paymentCapacity : Double = 0.0
    private var income : Double = 0.0
    private var coIncome : Double = 0.0
    private var notaryRate : Double = 0.0
    private var notary : Double = 0.0
    private var personalContribution : Double = 0.0
    private var borrowing : Double = 0.0
    private var cost : Double = 0.0
    private var mortgage : Double = 0.0


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_simulator
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        radio_couple.setOnClickListener{
            co_borrower_income.visibility = View.VISIBLE
        }

        radio_alone.setOnClickListener{
            co_borrower_income.visibility = View.GONE
        }

        button_calculate.setOnClickListener{
            getInformation()
            layout_simulator.visibility = View.VISIBLE
            updateUi()
        }
    }

    private fun updateUi() {
        project_cost.text = formatNumber(cost)
        project_cost2.text = formatNumber(cost)
        notary_fees.text = formatNumber(notary)
        notary_fees2.text = formatNumber(notary)
        mortgage1.text = formatNumber(mortgage)
        mortgage2.text = formatNumber(mortgage)
        personal_contribution.text =formatNumber(personalContribution)
        personal_contribution2.text = formatNumber(personalContribution)
        borrowing1.text = formatNumber(borrowing)
        borrowing2.text =formatNumber(borrowing)
        yearForTwoSimulation()
        year1.text = year.toString()
        year_2.text = year2.toString()
        rate = calculateCreditRate(year)
        rate1.text = (rate * 100).toString()
        rate2 = calculateCreditRate(year2)
        rate_2.text = (rate2 * 100).toString()
        payment.text = formatNumber(calculateMonthlyPayment(year, rate))
        payment2.text = formatNumber(calculateMonthlyPayment(year2, rate2))
        paymentCapacity_1.text = formatNumber(paymentCapacity)
        paymentCapacity_2.text = formatNumber(paymentCapacity)
    }

    private fun monthlyAmountCalculation(year : Double, rate : Double, capital : Double) : Double {
        return (capital * rate / 12) / (1- (1 + rate / 12).pow(-(12 * year)))
    }

    private fun getInformation(){
        income = Integer.parseInt(text_revenu.text.toString()).toDouble()
        if(coIncome != 0.0) coIncome = Integer.parseInt(text_revenu_coemprunteur.text.toString()).toDouble()
        personalContribution = Integer.parseInt(text_apport.text.toString()).toDouble()
        cost =  Integer.parseInt(text_cost.text.toString()).toDouble()
        mortgage = cost * 0.01
        notaryRate = if (radio_new.isChecked) 0.04 else 0.08
        notary = cost * notaryRate
        borrowing = calculateBorrowing(mortgage, notary)
    }

    private fun paymentCapacityCalculate() : Double{
        return if (coIncome != 0.0){
            (income + coIncome) * 0.33
        }else{
            income * 0.33
        }
    }

    private fun calculateBorrowing(mortgage : Double, notary : Double): Double {
        return cost + notary + mortgage - personalContribution
    }

    private fun calculateYearCredit(): Double {
        paymentCapacity = paymentCapacityCalculate()
        return ((borrowing / paymentCapacity)/12)
    }

    private fun yearForTwoSimulation(){
        val yearCalculate = calculateYearCredit()
        when{
            yearCalculate < 14.0 ->{
                year = 10.0
                year2 = 15.0
            }
            yearCalculate in 15.0..19.0 -> {
                year = 15.0
                year2 = 20.0
            }
            else -> {
                year = 20.0
                year2 = 25.0
            }
        }
    }

    private fun calculateCreditRate(year : Double) : Double {
        return when (year) {
            10.0 -> 0.005
            15.0 -> 0.0095
            20.0 -> 0.0125
            else -> 0.016
        }
    }

    private fun calculateMonthlyPayment(year: Double, rate: Double) : Double{
        return monthlyAmountCalculation(year, rate, borrowing)
    }


}
