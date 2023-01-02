package com.arp.citysipspreadster.activites


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityDemoBinding
import com.highsoft.highcharts.common.HIColor
import com.highsoft.highcharts.common.hichartsclasses.*
import com.highsoft.highcharts.core.HIChartView
import java.util.*


class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.newChart.theme = "sand-signika";

        val options = HIOptions()

        val title = HITitle()
        title.text = "Earning vs Reach"
        options.title = title

        val legend = HILegend()
        legend.layout = "vertical"
        legend.align = "left"
        legend.verticalAlign = "top"
        legend.x = 200
        legend.y = 300
        legend.floating = true
        legend.borderWidth = 1
        legend.backgroundColor = HIColor.initWithHexValue("FFFFFF")
        options.legend = legend

        val xaxis = HIXAxis()
        val categories = ArrayList<String>()
        categories.add("Monday")
        categories.add("Tuesday")
        categories.add("Wednesday")
        categories.add("Thursday")
        categories.add("Friday")
        categories.add("Saturday")
        categories.add("Sunday")
           // arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        xaxis.setCategories(categories)
        /*val plotband = HIPlotBands()
        plotband.from = 1
        plotband.to = 6.5
        plotband.color = HIColor.initWithRGBA(68, 170, 213, 2.0)
        xaxis.plotBands = ArrayList(Arrays.asList(plotband))*/
        options.xAxis = object : ArrayList<HIXAxis?>() {
            init {
                add(xaxis)
            }
        }

        val yaxis = HIYAxis()
        yaxis.title = HITitle()
        yaxis.title.text = "Fruit unit"
        options.yAxis = object : ArrayList<HIYAxis?>() {
            init {
                add(yaxis)
            }
        }

        val tooltip = HITooltip()
        tooltip.shared = true
        tooltip.valueSuffix = " units"
        options.tooltip = tooltip

        val credits = HICredits()
        credits.enabled = false
        options.credits = credits

        val plotOptions = HIPlotOptions()
        plotOptions.areaspline = HIAreaspline()
        plotOptions.areaspline.fillOpacity = 0.5
        options.plotOptions = plotOptions

        val areaspline1 = HIAreaspline()
        areaspline1.name = "Reach"
        val areaspline1Data = ArrayList<Number>()
        areaspline1Data.add(3)
        areaspline1Data.add(7)
        areaspline1Data.add(2)
        areaspline1Data.add(9)
        areaspline1Data.add(6)
        areaspline1Data.add(4)
        areaspline1Data.add(1)
        areaspline1.data = areaspline1Data

        val areaspline2 = HIAreaspline()
        areaspline2.name = "Earning"
        val areaspline2Data = ArrayList<Number>()
        areaspline2Data.add(4)
        areaspline2Data.add(2)
        areaspline2Data.add(9)
        areaspline2Data.add(6)
        areaspline2Data.add(3)
        areaspline2Data.add(2)
        areaspline2Data.add(2)
        areaspline2.data = areaspline2Data

        options.series = ArrayList(listOf(areaspline1, areaspline2))

        binding.newChart.options = options

       /* val xAxis = arrayOf<String>("Jan\n29","Jan\n30","Jan\n01","Jan\n02","Jan\n03","Jan\n04","Jan\n05","Jan\n06","Jan\n07","Jan\n08","Jan\n09","Jan\n10","Jan\n11","Jan\n12","Jan\n13")
        val reach = arrayOf<Number>(3737, 4390, 7979, 6892, 8016, 4941, 4475, 2594, 3117, 4153,2143,2805,5402,2794,9635)
        val earning = arrayOf<Number>(7009,7287,5269,6067,7043,6844,9837,2443,5112,4179,8908,7060,8195,2600,9882)

        // Turn the above arrays into XYSeries
        val series1: XYSeries = SimpleXYSeries(
            listOf(*reach),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Reach"
        )
        val series2: XYSeries = SimpleXYSeries(
            listOf(*earning),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Earning"
        )

        val series1Format = LineAndPointFormatter(Color.RED, Color.GREEN, null, null)
        val series2Format = LineAndPointFormatter(Color.YELLOW, Color.BLUE, null, null)

        series1Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )
        series2Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )

        binding.plot.addSeries(series1, series1Format)
        binding.plot.addSeries(series2, series2Format)

        binding.plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
            override fun format(
                obj: Any,
                toAppendTo: StringBuffer,
                pos: FieldPosition
            ): StringBuffer {
                val i = (obj as Number).toFloat().roundToInt()
                return toAppendTo.append(xAxis[i])
            }

            override fun parseObject(source: String, pos: ParsePosition): Any? {
                return null
            }
        }

        PanZoom.attach(binding.plot)*/

       // getchartData()


      /*  val anyChartView = findViewById<AnyChartView>(R.id.anyChart)

        val cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title("Earning vs Reach")
       *//* cartesian.yAxis(0).title("Earning")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        cartesian.xAxis(0).title("Reach")
        cartesian.yAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)
*//*
        val seriesData: MutableList<DataEntry> = ArrayList()
        seriesData.add(CustomDataEntry("18 jan", 3737, 7009))
        seriesData.add(CustomDataEntry("19 jan", 4390, 7287))
        seriesData.add(CustomDataEntry("20 jan", 7979, 5269))
        seriesData.add(CustomDataEntry("21 jan", 6892, 6067))
        seriesData.add(CustomDataEntry("22 jan", 8016, 7043))
        seriesData.add(CustomDataEntry("23 jan", 4941, 6844))
        seriesData.add(CustomDataEntry("24 jan", 4475, 9837))
        seriesData.add(CustomDataEntry("25 jan", 2594, 2443))
        seriesData.add(CustomDataEntry("26 jan", 3117, 5112))
        seriesData.add(CustomDataEntry("27 jan", 4153, 4179))
        seriesData.add(CustomDataEntry("28 jan", 2143, 8908))
        seriesData.add(CustomDataEntry("29 jan", 2805, 7060))
        seriesData.add(CustomDataEntry("30 jan", 5402, 8195))


        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        //val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")

        val series1 = cartesian.line(series1Mapping)
        series1.name("Reach")
        series1.hovered().markers().enabled(true)
        series1.stroke("3 #F06369");
        series1.hovered().stroke("3 #E20008");
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series2 = cartesian.line(series2Mapping)
        series2.name("Earning")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)


     *//*   val series3 = cartesian.line(series3Mapping)
        series3.name("Tequila")
        series3.hovered().markers().enabled(true)
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)*//*

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
*/
    }


 /*   private class CustomDataEntry internal constructor(x: String?, value: Number?, value2: Number?) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
        }
    }*/

   /* fun getchartData() {

        val xvalue = ArrayList<String>()
        xvalue.add("Jan\n0")
        xvalue.add("Jan\n1")
        xvalue.add("Jan\n2")
        xvalue.add("Jan\n3")
        xvalue.add("Jan\n4")
        xvalue.add("Jan\n5")


        val lineEntry = ArrayList<Entry>();
        lineEntry.add(Entry(20f,0f))
        lineEntry.add(Entry(30f,1f))
        lineEntry.add(Entry(50f,2f))
        lineEntry.add(Entry(20f,3f))
        lineEntry.add(Entry(10f,4f))

        val lineEntry1 = ArrayList<Entry>();
        lineEntry1.add(Entry(30f,0f))
        lineEntry1.add(Entry(40f,1f))
        lineEntry1.add(Entry(60f,2f))
        lineEntry1.add(Entry(30f,3f))
        lineEntry1.add(Entry(20f,4f))

      val lineDataSet = LineDataSet(lineEntry,"First")
        lineDataSet.color = resources.getColor(R.color.clr_EA2A31)

        val lineDataSet1 = LineDataSet(lineEntry1,"Second")
        lineDataSet1.color = resources.getColor(R.color.clr_6991FF)

        val finalDataSet = ArrayList<LineDataSet>()
        finalDataSet.add(lineDataSet)
        finalDataSet.add(lineDataSet1)


        val data = LineData(xvalue ,finalDataSet as List<ILineDataSet>?)
        binding.lineChart.data = data
        binding.lineChart.setBackgroundColor(resources.getColor(R.color.clr_FFFFFF))
        binding.lineChart.animateXY(3000,3000)
    }*/

}